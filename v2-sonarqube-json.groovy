#!/usr/bin/env groovy
import java.util.logging.Logger

import groovy.json.JsonSlurper

import static groovy.json.JsonOutput.prettyPrint as pretty
import static groovy.json.JsonOutput.toJson as json

import static SonarClient.*
import static Constants.*

// ---------------------------------------------------------------------------------------------------------------------
// Constants
// ---------------------------------------------------------------------------------------------------------------------

class Constants {
    static final String now = "${new Date().format("yyyyMMdd-HHMMss.SSS")}"
    static final File outputFile = new File("sonarqube-export-${now}.json")
    static final File configFile = new File("v2-sonarqube-json.properties")
}

// ---------------------------------------------------------------------------------------------------------------------
// Get Query classes
// ---------------------------------------------------------------------------------------------------------------------

def toInt = { String value -> value == null ? null : Integer.parseInt(value) }
def toFloat = { String value -> value == null ? null : Float.parseFloat(value) }

class SonarClient {

    static final Logger log = Logger.getLogger("v2-sonarqube-json")

    static {
        System.setProperty("java.util.logging.SimpleFormatter.format", "[%1\$tF %1\$tT] [%4\$-7s] %5\$s %n")
    }

    final String sonarUrl
    final String compKey
    final JsonSlurper slurp = new JsonSlurper()

    SonarClient() {
        log.info "Initializing SonarClient."
        log.info "Reading script configuration file: '${configFile.name}'."

        if (!configFile.exists()) {
            log.info "Configuration file doesn't exist."
        }

        Properties props = new Properties()
        props.load(configFile.newDataInputStream())

        this.sonarUrl = props.getProperty("sonar.url")
        this.compKey = props.getProperty("component.key")

        log.info "Configuration read:"
        log.info "\tsonar.url=${this.sonarUrl}"
        log.info "\tcomponent.key=${this.compKey}"
    }

    private String getReq(String url) {
        log.info "GET Request: '${url}'"

        def result = new URL(url).openConnection()
        def code = result.getResponseCode()

        log.info "Response code: '${code}'"

        if (code != 200) {
            log.error "Error calling the API. Please check if sonar.url=${this.sonarUrl} and " +
                    "the component.key=${this.compKey} " +
                    "are coorect and SonarQube is up and running and WEB APIs are enabled. Exiting."
            System.exit(-1)
        }

        return result.getInputStream().getText()
    }

    def metric(String metricName, toType = { x -> x}) {

        log.info " => GET Metric: ${metricName}"
        String url =
                "${this.sonarUrl}/api/measures/component?componentKey=${this.compKey}&metricKeys=${metricName}"
        String getResult = getReq(url)
        def result = slurp.parseText(getResult)
        def value = toType(result.component.measures[0].value)
        log.info "Extracting value: ${value}"
        return value

    }

    def issue(String type = "BUG,CODE_SMELL,VULNERABILITY",
              String severity = "INFO,MINOR,MAJOR,CRITICAL,BLOCKER",
              toType = {x -> x}) {

        log.info " => GET Issue: ${type} / ${severity}"
        String url =
                "${this.sonarUrl}/api/issues/search?componentKeys=${this.compKey}&types=${type}&severities=${severity}"
        String getResult = getReq(url)
        def result = slurp.parseText(getResult)
        def value = toType(result.total)
        log.info "Extracting value: ${value}"
        return value
    }
}

// ---------------------------------------------------------------------------------------------------------------------
// Writing results to the output file
// ---------------------------------------------------------------------------------------------------------------------

SonarClient sc = new SonarClient()

Map export = [

        metrics : [
                "ncloc"                     :       sc.metric("ncloc"),
                "complexity"                :       sc.metric("complexity"),
                "coverage"                  :       sc.metric("coverage"),
                "cognitive_complexity"      :       sc.metric("cognitive_complexity"),
                "duplicated_blocks"         :       sc.metric("duplicated_blocks"),
                "duplicated_lines"          :       sc.metric("duplicated_lines"),
                "duplicated_lines_density"  :       sc.metric("duplicated_lines_density"),
                "violations"                :       sc.metric("violations"),
                "code_smells"               :       sc.metric("code_smells"),
                "bugs"                      :       sc.metric("bugs"),
                "vulnerabilities"           :       sc.metric("vulnerabilities"),
                "branch_coverage"           :       sc.metric("branch_coverage"),
                "line_coverage"             :       sc.metric("line_coverage")
        ],


        issues : [
                "total"                     :       sc.issue(),

                "bugs": [
                    "total"                 :       sc.issue("BUG"),
                    "severity": [
                        "info"              :       sc.issue("BUG", "INFO"),
                        "minor"             :       sc.issue("BUG", "MINOR"),
                        "major"             :       sc.issue("BUG", "MAJOR"),
                        "critical"          :       sc.issue("BUG", "CRITICAL"),
                        "blockers"          :       sc.issue("BUG", "BLOCKER"),
                    ]
                ],

                "codeSmells": [
                    "total"                 :       sc.issue("CODE_SMELL"),
                    "severity": [
                        "info"              :       sc.issue("CODE_SMELL", "INFO"),
                        "minor"             :       sc.issue("CODE_SMELL", "MINOR"),
                        "major"             :       sc.issue("CODE_SMELL", "MAJOR"),
                        "critical"          :       sc.issue("CODE_SMELL", "CRITICAL"),
                        "blockers"          :       sc.issue("CODE_SMELL", "BLOCKER")
                    ]
                ],

                "vulnerabilities": [
                    "total"                 :       sc.issue("VULNERABILITY"),
                    "severity": [
                        "info"              :       sc.issue("VULNERABILITY", "INFO"),
                        "minor"             :       sc.issue("VULNERABILITY", "MINOR"),
                        "major"             :       sc.issue("VULNERABILITY", "MAJOR"),
                        "critical"          :       sc.issue("VULNERABILITY", "CRITICAL"),
                        "blockers"          :       sc.issue("VULNERABILITY", "BLOCKER")
                    ]
                ]
        ]
]

// ---------------------------------------------------------------------------------------------------------------------
// Main script method
// ---------------------------------------------------------------------------------------------------------------------

String output = pretty(json(export))

log.info "Results have been aggregated:"
log.info "\n\n${output}\n"

log.info "Writing content to file: ${outputFile.name}"
outputFile.write(output)

log.info "Job done."





