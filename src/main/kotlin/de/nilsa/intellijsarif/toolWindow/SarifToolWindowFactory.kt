package de.nilsa.intellijsarif.toolWindow

import com.intellij.openapi.diagnostic.Logger
import com.intellij.openapi.project.DumbAware
import com.intellij.openapi.project.Project
import com.intellij.openapi.wm.ToolWindow
import com.intellij.openapi.wm.ToolWindowFactory
import com.intellij.openapi.wm.ToolWindowManager
import com.intellij.ui.components.JBPanelWithEmptyText
import com.intellij.ui.content.ContentFactory
import de.nilsa.intellijsarif.MyBundle

class SarifToolWindowFactory : ToolWindowFactory, DumbAware {
    companion object {
        private const val ToolWindowId = "de.nilsa.intellijsarif.sarifToolWindow"
        fun getInstance(project: Project) : ToolWindow {
            val toolWindow = ToolWindowManager.getInstance(project).getToolWindow(ToolWindowId)
            if (toolWindow == null) {
                val log = Logger.getInstance(SarifToolWindowFactory::class.java)
                val err = MyBundle.message("err_toolWindowNotFound")
                log.error(err)
                throw InterruptedException(err)
            }
            return toolWindow
        }
    }

    override fun createToolWindowContent(project: Project, toolWindow: ToolWindow) {
        val contentFactory = ContentFactory.SERVICE.getInstance()
        val view = JBPanelWithEmptyText()
        val content = contentFactory.createContent(view, "", false)
        toolWindow.contentManager.addContent(content)
    }

    override fun init(toolWindow: ToolWindow) {
        super.init(toolWindow)
        toolWindow.stripeTitle = MyBundle.message("toolWindowTitle")
    }
}