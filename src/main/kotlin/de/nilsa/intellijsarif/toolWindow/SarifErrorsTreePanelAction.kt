package de.nilsa.intellijsarif.toolWindow

import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import de.nilsa.intellijsarif.shared.SarifDataKeys

abstract class SarifErrorsTreePanelAction : AnAction() {

    protected fun getPanel(e: AnActionEvent): SarifErrorsTreePanel? {
        return SarifDataKeys.SarifResultsTreePanel.getData(e.dataContext)
    }

    class CollapseAll : SarifErrorsTreePanelAction() {
        override fun actionPerformed(e: AnActionEvent) {
            val win = getPanel(e) ?: return
            win.collapseAll()
        }
    }

    class ExpandAll : SarifErrorsTreePanelAction() {
        override fun actionPerformed(e: AnActionEvent) {
            val win = getPanel(e) ?: return
            win.expandAll()
        }
    }
}