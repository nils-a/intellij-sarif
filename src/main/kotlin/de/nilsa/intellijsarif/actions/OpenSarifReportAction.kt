package de.nilsa.intellijsarif.actions

import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.fileChooser.FileChooser
import com.intellij.openapi.fileChooser.FileChooserDescriptorFactory
import com.intellij.openapi.project.Project
import com.intellij.openapi.project.guessProjectDir
import com.intellij.openapi.vfs.VirtualFile
import de.nilsa.intellijsarif.report.SarifReport
import de.nilsa.intellijsarif.shared.SarifDataKeys
import de.nilsa.intellijsarif.toolWindow.SarifToolWindowFactory

class OpenSarifReportAction : AnAction() {

    override fun actionPerformed(e: AnActionEvent) {
        val project = e.project ?: return
        val file = e.getData(SarifDataKeys.OpenSarifReport)
            ?: openSelectFileDialog(project)
            ?: return
        val report = SarifReport(project, file.toNioPath())
        SarifToolWindowFactory.clearContent(project)
        SarifToolWindowFactory.openReport(project, report)
    }

    private fun openSelectFileDialog(project: Project) : VirtualFile? {
        val fileChooserDescriptor = FileChooserDescriptorFactory.createSingleFileDescriptor()
        fileChooserDescriptor.title = "Select a SARIF Report"
        fileChooserDescriptor.description = "Select the Sarif report, not the json or text."

        return FileChooser.chooseFile(
            fileChooserDescriptor,
            project,
            project.guessProjectDir())
    }
}