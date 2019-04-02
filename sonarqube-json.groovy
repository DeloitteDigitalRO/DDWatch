#!/usr/bin/env groovy
import ch.qos.logback.classic.Level
import com.jayway.jsonpath.JsonPath

@Grab(group='ch.qos.logback', module='logback-classic', version='1.0.13')
@Grab(group='com.jayway.jsonpath', module='json-path', version='2.4.0')

import java.util.logging.Logger
import groovy.json.JsonSlurper

import static groovy.json.JsonOutput.prettyPrint as pretty
import static groovy.json.JsonOutput.toJson as toJson



// ---------------------------------------------------------------------------------------------------------------------
// Setup script logging
// ---------------------------------------------------------------------------------------------------------------------

log = Logger.getAnonymousLogger()
System.setProperty("java.util.logging.SimpleFormatter.format", "[%1\$tF %1\$tT] [%4\$-7s] %5\$s %n")
// A "hack" to disable logging for 'com.jayway.jsonpath'
org.slf4j.LoggerFactory.getLogger(ch.qos.logback.classic.Logger.ROOT_LOGGER_NAME).setLevel(Level.ERROR)

// ---------------------------------------------------------------------------------------------------------------------
// Script Constants & Utils & Helper classes
// ---------------------------------------------------------------------------------------------------------------------\
def version = "v0.1"
def propsFileName = "sonarqube-json.properties"
def now = new Date().format("yyyyMMdd-HHMMss.SSS")
def outputFileName = "sonarqube-export-${now}.json"
def outputFile = new File(outputFileName)


// Metrics related
def parseInt = { String str -> (str != null) ? Integer.parseInt(str) : null }
def parseFloat = { String str -> (str != null) ? Float.parseFloat(str) : null }

def metricsQParams = [
        "ncloc" : parseInt,
         "complexity" : parseInt,
         "coverage" : parseFloat,
         "cognitive_complexity" : parseInt,
         "duplicated_blocks" : parseInt,
        "duplicated_lines" : parseInt,
        "duplicated_lines_density" : parseFloat,
        "violations" : parseInt,
        "code_smells" : parseInt,
        "bugs" : parseInt,
        "vulnerabilities" : parseInt,
        "branch_coverage" : parseFloat,
        "line_coverage" : parseFloat
        // You can add here new metrics
]
def metricsParam = (metricsQParams.keySet() as String[]).join(",")
def metricsJPExpr = { p -> "\$['component']['measures'][?(@.metric == '${p}')]['value']" }

// Get util method
def GET(String url) {
    log.info "GET Request: '${url}'"

    def get = new URL(url).openConnection()
    def rCode = get.getResponseCode()

    log.info "Response code: '${rCode}'"

    if (rCode!=200) {
        log.error "Error calling the API. Please check if sonar.url=${sonarUrl} and the component.key=${compKey} " +
                "are coorect and SonarQube is up and running and WEB APIs are enabled."
        System.exit(-1)
    }

    return get.getInputStream().getText()
}

// ---------------------------------------------------------------------------------------------------------------------
// Read script properties file
// ---------------------------------------------------------------------------------------------------------------------
log.info "SonarQube JSON Creator ($version)."
log.info "Reading properties from file: '$propsFileName':"

File propsFile = new File(propsFileName)

if (!propsFile.exists()) {
    log.error "File doesn't exist."
    return -1
}

Properties props = new Properties()
props.load(propsFile.newDataInputStream())

def sonarUrl = props.getProperty("sonar.url")
def compKey = props.getProperty("component.key")

log.info "\tsonar.url=$sonarUrl"
log.info "\tcomponent.key=$compKey"

// ---------------------------------------------------------------------------------------------------------------------
// GET Metrics
// ---------------------------------------------------------------------------------------------------------------------
log.info "Getting metrics."

def metricsUrl = "${sonarUrl}/api/measures/component?componentKey=${compKey}&metricKeys=${metricsParam}"
def metricsRaw = GET(metricsUrl)

def metricsJsonPath = JsonPath.parse(metricsRaw)
def metrics = (metricsQParams.keySet() as String[]).collectEntries
              { [it, (metricsQParams[it])(metricsJsonPath.read(metricsJPExpr(it))[0])] }

log.info "Response body: ${metrics}"

// ---------------------------------------------------------------------------------------------------------------------
// GET issues
// ---------------------------------------------------------------------------------------------------------------------
log.info "Getting issues."

def issueUrlForTypeAndSeverity = { String types, String severities ->
    "${sonarUrl}/api/issues/search?componentKeys=${compKey}&types=${types}&severities=${severities}"
}

def issueUrlForType = { String type ->
    return "${sonarUrl}/api/issues/search?componentKeys=${compKey}&types=${type}"
}

def totals = { String url ->
    def slurp = new JsonSlurper()
    return slurp.parseText(GET(url))["total"]
}


def issues = [
        "bugs" : [
                "total" : totals(issueUrlForType("BUG")),
                "severity" : [
                        "info"          : totals(issueUrlForTypeAndSeverity("BUG", "INFO")),
                        "minor"         : totals(issueUrlForTypeAndSeverity("BUG", "MINOR")),
                        "major"         : totals(issueUrlForTypeAndSeverity("BUG", "MAJOR")),
                        "critical"      : totals(issueUrlForTypeAndSeverity("BUG", "CRITICAL")),
                        "blockers"      : totals(issueUrlForTypeAndSeverity("BUG", "BLOCKER"))
                ]
        ],
        "codeSmells" : [
                "total" : totals(issueUrlForType("CODE_SMELL")),
                "severity" : [
                        "info"          : totals(issueUrlForTypeAndSeverity("CODE_SMELL", "INFO")),
                        "minor"         : totals(issueUrlForTypeAndSeverity("CODE_SMELL", "MINOR")),
                        "major"         : totals(issueUrlForTypeAndSeverity("CODE_SMELL", "MAJOR")),
                        "critical"      : totals(issueUrlForTypeAndSeverity("CODE_SMELL", "CRITICAL")),
                        "blockers"      : totals(issueUrlForTypeAndSeverity("CODE_SMELL", "BLOCKER"))
                ]
        ],
        "vulnerabilities" : [
                "total" : totals(issueUrlForType("VULNERABILITY")),
                "severity" : [
                        "info"          : totals(issueUrlForTypeAndSeverity("VULNERABILITY", "INFO")),
                        "minor"         : totals(issueUrlForTypeAndSeverity("VULNERABILITY", "MINOR")),
                        "major"         : totals(issueUrlForTypeAndSeverity("VULNERABILITY", "MAJOR")),
                        "critical"      : totals(issueUrlForTypeAndSeverity("VULNERABILITY", "CRITICAL")),
                        "blockers"      : totals(issueUrlForTypeAndSeverity("VULNERABILITY", "BLOCKER"))
                ]
        ]
]

log.info "Aggregating issues into file."
log.info "Issues report:"

log.info "${issues}"

// ---------------------------------------------------------------------------------------------------------------------
// Agreggating results
// ---------------------------------------------------------------------------------------------------------------------

def aggr = pretty(toJson([
        "metrics" : metrics,
        "issues" : issues
]))

log.info "Writing files to ${outputFileName}."

outputFile.write(aggr)

log.info "Files succesfuly written."



