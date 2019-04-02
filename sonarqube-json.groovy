#!/usr/bin/env groovy

import java.util.logging.Logger

import java.util.zip.ZipOutputStream
import java.util.zip.ZipEntry

import static groovy.json.JsonOutput.prettyPrint as pretty

// ---------------------------------------------------------------------------------------------------------------------
// Script Constants & Utils
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

// Zip Archive related
def filesToZip = [ metricsFile ]
def zipArchiveFileName = "sonarQube-${now}.zip"

// ---------------------------------------------------------------------------------------------------------------------
// Setup script logging
// ---------------------------------------------------------------------------------------------------------------------
def log = Logger.getAnonymousLogger()
System.setProperty("java.util.logging.SimpleFormatter.format", "[%1\$tF %1\$tT] [%4\$-7s] %5\$s %n")


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
def metricsUrl = "${sonarUrl}/api/measures/component?componentKey=${compKey}&metricKeys=${metricsParam}"

log.info "GET Metrics Request: ${sonarUrl}api/measures/component?componentKey=${compKey}&metricKeys=..."
metricsQParams.each {
    log.info "\t ${it},"
}

def get = new URL(metricsUrl).openConnection()
def responseCode = get.getResponseCode()

log.info "Response code: ${responseCode}."

if (responseCode!=200) {
    log.error "Error calling the API. Please check if sonar.url=${sonarUrl} and the component.key=${compKey} " +
            "are coorect and SonarQube is up and running and WEB APIs are enabled."
    return -1
}

def metricsResponse = get.getInputStream().getText()

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
// Zipping file metrics to file
// ---------------------------------------------------------------------------------------------------------------------


log.info "Creating zip archive with the full report."

ZipOutputStream zos = new ZipOutputStream(new FileOutputStream(zipArchiveFileName))

filesToZip.each {
    file ->
        zos.putNextEntry(new ZipEntry(file.name))
        def buffer = new byte[1024]
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