package de.nilsa.intellijsarif.report

import com.google.gson.Gson
import com.intellij.notification.Notification
import com.intellij.notification.NotificationType
import com.intellij.notification.Notifications
import com.intellij.openapi.diagnostic.Logger
import com.intellij.openapi.project.Project
import com.intellij.util.io.exists
import de.nilsa.intellijsarif.GlobalConstants
import de.nilsa.intellijsarif.json.Run
import de.nilsa.intellijsarif.json.Sarif210Rtm5Schema
import java.io.FileNotFoundException
import java.io.FileReader
import java.io.Reader
import java.nio.file.FileSystem
import java.nio.file.FileSystems
import java.nio.file.Path

class SarifReport(
    project: Project,
    reportPath: Path
) {

    constructor(project: Project, reportPath: String, fileSystem: FileSystem = FileSystems.getDefault())
            : this(project, fileSystem.getPath(reportPath))

    private val log = Logger.getInstance(SarifReport::class.java)

    var run : Run? = null

    init {
        if (!reportPath.exists()) {
            throw FileNotFoundException(reportPath.toString())
        }

        try {
            val jsonReader: Reader = FileReader(reportPath.toFile())
            // TODO: Currently we only parse SARIF 2.1 format. Do we care about older versions?
            val parsed = Gson().fromJson(jsonReader, Sarif210Rtm5Schema::class.java)
            run = parsed.runs.firstOrNull()
        } catch (e: Exception) {
            log.error("Could not parse sarif-json.", e)
            Notifications.Bus.notify(
                Notification(
                    GlobalConstants.notificationGroup,
                    "Could not parse sarif-json!",
                    "${e.javaClass}: ${e.message}",
                    NotificationType.WARNING
                ), project
            )
        }
    }
}
