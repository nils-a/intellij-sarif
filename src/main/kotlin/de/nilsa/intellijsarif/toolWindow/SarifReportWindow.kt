@file:Suppress("UnstableApiUsage")

package de.nilsa.intellijsarif.toolWindow

import com.intellij.openapi.actionSystem.ActionGroup
import com.intellij.openapi.actionSystem.ActionManager
import com.intellij.openapi.ui.SimpleToolWindowPanel
import com.intellij.ui.OnePixelSplitter
import com.intellij.ui.components.JBScrollPane
import com.intellij.ui.dsl.builder.Cell
import com.intellij.ui.dsl.builder.Panel
import com.intellij.ui.dsl.builder.Row
import com.intellij.ui.dsl.builder.panel
import com.intellij.ui.dsl.gridLayout.HorizontalAlign
import com.intellij.ui.dsl.gridLayout.VerticalAlign
import de.nilsa.intellijsarif.report.SarifReport
import java.awt.Component

class SarifReportWindow(report: SarifReport) : SimpleToolWindowPanel(true, true) {
    init {
        initToolbar()
        val content = panel {
            row {
                splitPane({
                    row {
                        scrollPane(SarifErrorsTreePanel(report))
                    }.resizableRow()
                }, {
                    row {
                        scrollPane(SarifDetailsPanel())
                    }.resizableRow()
                }, true)
            }.resizableRow()
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

    private fun Row.scrollPane(component: Component? = null, init: (JBScrollPane.() -> Unit)? = null)
            : Cell<JBScrollPane> = cell(JBScrollPane(component).apply {
                init?.invoke(this)
            }).apply {
                horizontalAlign(HorizontalAlign.FILL)
                verticalAlign(VerticalAlign.FILL)
            }

    private fun Row.splitPane(
        first: Panel.() -> Unit,
        second: Panel.() -> Unit,
        verticalSplit: Boolean = false,
        init: (OnePixelSplitter.() -> Unit)? = null
    ): Cell<OnePixelSplitter> = cell(
        OnePixelSplitter()
            .apply {
                firstComponent = com.intellij.ui.dsl.builder.panel(first)
                secondComponent = com.intellij.ui.dsl.builder.panel(second)
                orientation = verticalSplit
                init?.invoke(this)
            })
        .apply {
            horizontalAlign(HorizontalAlign.FILL)
            verticalAlign(VerticalAlign.FILL)
        }
}


