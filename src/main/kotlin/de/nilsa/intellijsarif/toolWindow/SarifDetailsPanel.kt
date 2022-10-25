package de.nilsa.intellijsarif.toolWindow

import com.intellij.ui.components.JBPanel
import com.intellij.ui.layout.panel

class SarifDetailsPanel : JBPanel<SarifDetailsPanel>() {
    init {
        val component = panel {
            row {
                label("TODO: Hier fehlt noch alles.")
            }
        }
        add(component)
    }
}