package de.nilsa.intellijsarif.shared

import com.intellij.openapi.actionSystem.DataKey
import com.intellij.openapi.vfs.VirtualFile

object SarifDataKeys {
    val OpenSarifReport: DataKey<VirtualFile> =
        DataKey.create("${SarifDataKeys.javaClass.canonicalName}.OpenSarifReport")
}