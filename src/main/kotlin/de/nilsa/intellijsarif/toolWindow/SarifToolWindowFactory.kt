package de.nilsa.intellijsarif.toolWindow

import com.intellij.openapi.diagnostic.Logger
import com.intellij.openapi.options.ConfigurationException
import com.intellij.openapi.project.DumbAware
import com.intellij.openapi.project.Project
import com.intellij.openapi.wm.ToolWindow
import com.intellij.openapi.wm.ToolWindowFactory
import com.intellij.openapi.wm.ToolWindowManager
import com.intellij.ui.components.JBPanelWithEmptyText
import com.intellij.ui.content.ContentFactory
import de.nilsa.intellijsarif.MyBundle
import de.nilsa.intellijsarif.report.SarifReport
import javax.swing.JPanel

class SarifToolWindowFactory : ToolWindowFactory, DumbAware {
    companion object {
        private const val ToolWindowId = "de.nilsa.intellijsarif.sarifToolWindow"

        fun openReport(project: Project, report: SarifReport) {
            val toolWindow = getToolWindow(project)
            setContent(toolWindow, SarifReportWindow(report))
            toolWindow.show()
        }

        private fun getToolWindow(project: Project): ToolWindow {
            val toolWindow = ToolWindowManager.getInstance(project).getToolWindow(ToolWindowId)
            if (toolWindow == null) {
                val log = Logger.getInstance(SarifToolWindowFactory::class.java)
                val err = MyBundle.message("err_toolWindowNotFound")
                log.error(err)
                //TODO: We need a "better" Exception.
                throw ConfigurationException(err)
            }
            return toolWindow
        }

        private fun setContent(toolWindow: ToolWindow, view: JPanel) {
            val contentFactory = ContentFactory.SERVICE.getInstance()
            toolWindow.contentManager.removeAllContents(false)
            val content = contentFactory.createContent(view, "", false)
            content.setDisposer(toolWindow.contentManager)
            toolWindow.contentManager.addContent(content)
        }
    }

    override fun createToolWindowContent(project: Project, toolWindow: ToolWindow) {
        setContent(toolWindow, JBPanelWithEmptyText().apply {
            emptyText.text = "No SARIF report loaded."
        })
    }

    override fun init(toolWindow: ToolWindow) {
        super.init(toolWindow)
        toolWindow.stripeTitle = MyBundle.message("toolWindowTitle")
    }
}