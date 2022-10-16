package de.nilsa.intellijsarif.toolWindow

import com.intellij.openapi.ui.SimpleToolWindowPanel
import com.intellij.ui.layout.panel
import de.nilsa.intellijsarif.report.SarifReport

class SarifReportWindow(report: SarifReport) : SimpleToolWindowPanel(true, true) {
    init {
        val content = panel {
            row {
                cell {
                    label(report.run!!.results.first().message.text)
                }
            }
        }

        setContent(content)
    }
}