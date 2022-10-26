@file:Suppress("UnstableApiUsage")

package de.nilsa.intellijsarif.toolWindow

import com.intellij.ui.OnePixelSplitter
import com.intellij.ui.components.JBScrollPane
import com.intellij.ui.dsl.builder.Cell
import com.intellij.ui.dsl.builder.Panel
import com.intellij.ui.dsl.builder.Row
import com.intellij.ui.dsl.gridLayout.HorizontalAlign
import com.intellij.ui.dsl.gridLayout.VerticalAlign
import com.intellij.ui.layout.PropertyBinding
import javax.swing.JLabel
import kotlin.reflect.KMutableProperty0
import de.nilsa.intellijsarif.json.Result

fun Panel.resizableRow(label: JLabel? = null, init: (Row.() -> Unit)): Row {
    return this.row(label, init).resizableRow()
}

fun Row.scrollPane(
    component: Panel.() -> Unit,
    init: (JBScrollPane.() -> Unit)? = null
) : Cell<JBScrollPane> = cell(
    JBScrollPane(com.intellij.ui.dsl.builder.panel(component))
        .apply {
            init?.invoke(this)
        })
    .apply {
        horizontalAlign(HorizontalAlign.FILL)
        verticalAlign(VerticalAlign.FILL)
    }

fun Row.splitPane(
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
