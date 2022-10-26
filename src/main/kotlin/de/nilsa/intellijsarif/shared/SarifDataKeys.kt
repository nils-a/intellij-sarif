package de.nilsa.intellijsarif.shared

import com.intellij.openapi.actionSystem.DataKey
import de.nilsa.intellijsarif.toolWindow.SarifErrorsTreePanel

object SarifDataKeys {
    val SarifResultsTreePanel: DataKey<SarifErrorsTreePanel> =
        DataKey.create("${SarifDataKeys.javaClass.canonicalName}.SarifResultsTreePanel")
}