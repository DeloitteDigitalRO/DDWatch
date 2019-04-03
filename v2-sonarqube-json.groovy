#!/usr/bin/env groovy
import java.util.logging.Logger

@Grab(group="net.andreinc.ansiscape", module="ansiscape", version="0.0.2")

import groovy.json.JsonSlurper

import static groovy.json.JsonOutput.prettyPrint as pretty
import static groovy.json.JsonOutput.toJson as json

import net.andreinc.ansiscape.AnsiScape

import static SonarClient.*
import static Constants.*

// ---------------------------------------------------------------------------------------------------------------------
// Constants
// ---------------------------------------------------------------------------------------------------------------------

class Constants {
    static final String now = "${new Date().format("yyyyMMdd-HHMMss.SSS")}"
    static final File outputFile = new File("sonarqube-export-${now}.json")
    static final File configFile = new File("v2-sonarqube-json.properties")
    static final AnsiScape color = new AnsiScape()
    static final info = { text -> log.info "${color.format(text)}"}
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
        info "Initializing {blackBg {bold {white SonarClient}{green .}}}"
        info "Reading script configuration file: '{blue {u ${configFile.name}}}'."

        if (!configFile.exists()) {
            info "{red Configuration file doesn't exist.}"
        }

        Properties props = new Properties()
        props.load(configFile.newDataInputStream())

        this.sonarUrl = props.getProperty("sonar.url")
        this.compKey = props.getProperty("component.key")

        info "Configuration read:"
        info "\tsonar.url=${this.sonarUrl}"
        info "\tcomponent.key=${this.compKey}"
    }

    private String getReq(String url) {
        info "{b GET} Request: '${url}'"

        def result = new URL(url).openConnection()
        def code = result.getResponseCode()

        info "Response code: '${code}'"

        if (code != 200) {
            info "{red Error calling the API. Please check if sonar.url=${this.sonarUrl} and " +
                    "the component.key=${this.compKey} " +
                    "are coorect and SonarQube is up and running and WEB APIs are enabled. Exiting.}"
            System.exit(-1)
        }

        return result.getInputStream().getText()
    }

    def metric(String metricName, toType = { x -> x}) {

        info " => {b GET} Metric: {green ${metricName}}"
        String url =
                "${this.sonarUrl}/api/measures/component?componentKey=${this.compKey}&metricKeys=${metricName}"
        String getResult = getReq(url)
        def result = slurp.parseText(getResult)
        def value = toType(result?.component?.measures[0]?.value)
        info "Extracting value: {blue ${value}}"
        return value

    }

    def issue(String type = "BUG,CODE_SMELL,VULNERABILITY",
              String severity = "INFO,MINOR,MAJOR,CRITICAL,BLOCKER",
              toType = {x -> x}) {

        info " => {b GET} Issue: {green ${type} / ${severity}}"
        String url =
                "${this.sonarUrl}/api/issues/search?componentKeys=${this.compKey}&types=${type}&severities=${severity}"
        String getResult = getReq(url)
        def result = slurp.parseText(getResult)
        def value = toType(result?.total)
        info "Extracting value: ${value}"
        return value
    }
}

// ---------------------------------------------------------------------------------------------------------------------
// Writing results to the output file
// ---------------------------------------------------------------------------------------------------------------------

SonarClient sc = new SonarClient()

Map export = [

        metrics : [
                "ncloc"                     :       sc.metric("ncloc", toInt),
                "complexity"                :       sc.metric("complexity", toInt),
                "coverage"                  :       sc.metric("coverage", toFloat),
                "cognitive_complexity"      :       sc.metric("cognitive_complexity", toInt),
                "duplicated_blocks"         :       sc.metric("duplicated_blocks", toInt),
                "duplicated_lines"          :       sc.metric("duplicated_lines", toInt),
                "duplicated_lines_density"  :       sc.metric("duplicated_lines_density", toFloat),
                "violations"                :       sc.metric("violations", toInt),
                "code_smells"               :       sc.metric("code_smells", toInt),
                "bugs"                      :       sc.metric("bugs", toInt),
                "vulnerabilities"           :       sc.metric("vulnerabilities", toInt),
                "branch_coverage"           :       sc.metric("branch_coverage", toFloat),
                "line_coverage"             :       sc.metric("line_coverage", toFloat)
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

info "Results have been aggregated:"
log.info "\n\n${output}\n"

info "Writing content to file: {u {blue ${outputFile.name}}}"
outputFile.write(output)

info "{b {blue Job done.}}"





