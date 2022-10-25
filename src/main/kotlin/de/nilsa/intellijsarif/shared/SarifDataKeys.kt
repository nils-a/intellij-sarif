package de.nilsa.intellijsarif.shared

import com.intellij.openapi.actionSystem.DataKey

object SarifDataKeys {
    val SelectedSarifResult: DataKey<de.nilsa.intellijsarif.json.Result> =
        DataKey.create("${SarifDataKeys.javaClass.canonicalName}.SelectedSarifResult")
}