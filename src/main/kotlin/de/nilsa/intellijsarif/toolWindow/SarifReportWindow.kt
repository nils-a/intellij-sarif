@file:Suppress("UnstableApiUsage")

package de.nilsa.intellijsarif.toolWindow

import com.intellij.openapi.actionSystem.ActionGroup
import com.intellij.openapi.actionSystem.ActionManager
import com.intellij.openapi.actionSystem.DataProvider
import com.intellij.openapi.ui.SimpleToolWindowPanel
import com.intellij.ui.dsl.builder.panel
import com.intellij.ui.dsl.gridLayout.VerticalAlign
import de.nilsa.intellijsarif.report.SarifReport
import de.nilsa.intellijsarif.shared.SarifDataKeys

class SarifReportWindow(report: SarifReport)
    : SimpleToolWindowPanel(true, true), DataProvider {
    private val treePanel = SarifErrorsTreePanel(report)

    init {
        initToolbar()
        val content = panel {
            resizableRow {
                splitPane({
                    resizableRow {
                        scrollPane({
                            resizableRow {
                                cell(treePanel)
                                    .verticalAlign(VerticalAlign.TOP)
                            }
                        })
                    }
                }, {
                    resizableRow {
                        scrollPane({
                            resizableRow {
                                cell(SarifDetailsPanel())
                                    .verticalAlign(VerticalAlign.TOP)
                            }
                        })
                    }
                }, true)
            }
        }

        setContent(content)
    }

    private fun initToolbar() {
        val actionManager = ActionManager.getInstance()
        val actionToolbar =
            actionManager.createActionToolbar(
                "Sarif ReportWindow Toolbar",
                actionManager.getAction("SarifReportWindowToolbar") as ActionGroup,
                true
            )
        actionToolbar.targetComponent = this
        toolbar = actionToolbar.component
    }

    override fun getData(dataId: String): Any? {
        if(SarifDataKeys.SarifResultsTreePanel.`is`(dataId)) {
            return treePanel
        }
        return null
    }
}
