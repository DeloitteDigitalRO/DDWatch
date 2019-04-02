#!/usr/bin/env groovy
import java.util.logging.Logger

import java.util.zip.ZipOutputStream
import java.util.zip.ZipEntry

import static groovy.json.JsonOutput.prettyPrint as pretty
import static groovy.json.JsonOutput.toJson as toJson
import groovy.json.JsonSlurper

// ---------------------------------------------------------------------------------------------------------------------
// Setup script logging
// ---------------------------------------------------------------------------------------------------------------------
log = Logger.getAnonymousLogger()
System.setProperty("java.util.logging.SimpleFormatter.format", "[%1\$tF %1\$tT] [%4\$-7s] %5\$s %n")


// ---------------------------------------------------------------------------------------------------------------------
// Script Constants & Utils & Helper classes
// ---------------------------------------------------------------------------------------------------------------------

def version = "v0.1"
def propsFileName = "sonarqube-json.properties"
def now = new Date().format("yyyyMMdd-HHMMss.SSS")

// Metrics related
def metricsQParams = [
        "ncloc",
        "complexity",
        "coverage",
        "cognitive_complexity",
        "duplicated_blocks",
        "duplicated_lines",
        "duplicated_lines_density",
        "violations",
        "code_smells",
        "bugs",
        "vulnerabilities",
        "branch_coverage",
        "line_coverage"
        // You can add here new metrics
]
def metricsParam = metricsQParams.join(",")
def metricsFileName = "metrics-${now}.json"
def metricsFile = new File(metricsFileName)

// Issues
def issuesFileName = "issues-${now}.json"
def issuesFile = new File(issuesFileName)

// Zip Archive related
def filesToZip = [ metricsFile, issuesFile ]
def zipArchiveFileName = "sonarQube-${now}.zip"


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
def metricsResponse = GET(metricsUrl)

log.info "Response body: ${pretty(metricsResponse)}"

// ---------------------------------------------------------------------------------------------------------------------
// Writing metrics to file
// ---------------------------------------------------------------------------------------------------------------------

log.info "Writing metrics to temporary file: ${metricsFileName}."

if (metricsFile.canWrite()) {
    log.error "Cannot write to file location: ${metricsFileName}."
    return -1
}

metricsFile.write(metricsResponse)

// ---------------------------------------------------------------------------------------------------------------------
// GET issues
// ---------------------------------------------------------------------------------------------------------------------
log.info "Getting issues."

def issueUrlForTypeAndSeverity = { types, severities ->
    "${sonarUrl}/api/issues/search?componentKeys=${compKey}&types=${types}&severities=${severities}"
}

def issueUrlForType = { type ->
    return "${sonarUrl}/api/issues/search?componentKeys=${compKey}&types=${type}"
}

def totals = { url ->
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

def issuesResponse = pretty(toJson(issues))

log.info "${issuesResponse}"

// ---------------------------------------------------------------------------------------------------------------------
// Writing metrics to file
// ---------------------------------------------------------------------------------------------------------------------

log.info "Writing issues to temporary file: ${issuesFileName}."

if (issuesFile.canWrite()) {
    log.error "Cannot write to file location: ${issuesFileName}."
    return -1
}

issuesFile.write(issuesResponse)

// ---------------------------------------------------------------------------------------------------------------------
// Zipping files
// ---------------------------------------------------------------------------------------------------------------------


log.info "Creating zip archive with the full report."

ZipOutputStream zos = new ZipOutputStream(new FileOutputStream(zipArchiveFileName))

filesToZip.each {
    file ->
        zos.putNextEntry(new ZipEntry(file.name))
        byte[] buffer = new byte[1024]
        log.info "Adding ${file.name} to zip archive."
        file.withInputStream { i ->
            l = i.read(buffer)
            if (l>0)
                zos.write(buffer, 0, l)
        }
        zos.closeEntry()
}
zos.close()

// ---------------------------------------------------------------------------------------------------------------------
// Cleanup
// ---------------------------------------------------------------------------------------------------------------------

filesToZip.each {
    file ->
        log.info "Deleting temporary file: ${file.name}"
        file.delete()
}

log.info "Success. Results were archived in: ${zipArchiveFileName}."

