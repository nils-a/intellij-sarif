package de.nilsa.intellijsarif.toolWindow

import com.intellij.icons.AllIcons
import com.intellij.openapi.actionSystem.*
import com.intellij.openapi.ui.SimpleToolWindowPanel
import com.intellij.openapi.vfs.VirtualFile
import com.intellij.openapi.vfs.VirtualFileManager
import com.intellij.ui.components.JBPanel
import com.intellij.ui.treeStructure.Tree
import de.nilsa.intellijsarif.json.Result
import de.nilsa.intellijsarif.report.SarifReport
import de.nilsa.intellijsarif.shared.SarifDataKeys
import java.awt.Component
import java.net.URL
import javax.swing.JComponent
import javax.swing.JLabel
import javax.swing.JTree
import javax.swing.ToolTipManager
import javax.swing.tree.*

class SarifErrorsTreePanel(private val report: SarifReport)
    : JBPanel<SarifErrorsTreePanel>(true), DataProvider {
    private val root = DefaultMutableTreeNode("")
    private val model = DefaultTreeModel(root)
    private val tree = Tree(model)

    init {
        tree.cellRenderer = MyTreeCellRenderer()
        tree.selectionModel.selectionMode = TreeSelectionModel.SINGLE_TREE_SELECTION
        buildTree()
        ToolTipManager.sharedInstance().registerComponent(tree)
        add(tree)
        model.reload()
        expandAll()
    }

    private fun expandAll() {
        expandCollapse(true)
    }

    private fun collapseAll() {
        expandCollapse(false)
    }

    private fun getSelectedResult(): Result? {
        val selected = tree.getSelectedNodes(DefaultMutableTreeNode::class.java) { it.isLeaf }.firstOrNull()
            ?: return null
        return when (selected.userObject) {
            is Result -> selected.userObject as Result
            else -> null
        }
    }

    private fun expandCollapse(expand: Boolean, path: TreePath? = null) {
        if (path == null) {
            expandCollapse(expand, TreePath(tree.model.root))
            return
        }

        val node = path.lastPathComponent as TreeNode
        for (it in node.children()) {
            val nextPath = path.pathByAddingChild(it)
            expandCollapse(expand, nextPath)
        }

        if (expand) {
            if (!tree.isExpanded(path)) {
                tree.expandPath(path)
            }
        } else {
            if (!tree.isCollapsed(path)) {
                tree.collapsePath(path)
            }
        }
    }

    override fun getData(dataId: String): Any? {
        if(SarifDataKeys.SelectedSarifResult.`is`(dataId)) {
            return getSelectedResult()
        }
        return null
    }

    private fun buildTree() {
        // group by location, show fileName, sort alphabetically
        root.removeAllChildren()
        val run = report.run
        if(run == null) {
            // TODO: An empty "thing" would be better than crippling the tree to display a single message.
            root.userObject = "No run in SARIF report"
            return
        }

        val driver = run.tool.driver
        root.userObject = "${driver.name} version ${driver.version}"

        val resultsByLoc = run.results.groupBy {
            URL(it.locations.first().physicalLocation.artifactLocation.uri)
        }


        resultsByLoc.entries.map {
            MyFileNodeData(
                it.key,
                it.key.file,
                VirtualFileManager.getInstance().findFileByUrl(it.key.toString()),
                it.value
            )
        }.sortedBy {
            it.fileName
        }.forEach {
            val node = DefaultMutableTreeNode(it)

            it.results.sortedBy { r ->
                r.locations.first().physicalLocation.region.startLine
            }.forEach {r ->
                node.add(DefaultMutableTreeNode(r))
            }

            root.add(node)
        }

        model.reload()
    }

    data class MyFileNodeData(
        val fileUrl: URL,
        val fileName: String,
        val file: VirtualFile?,
        val results: List<Result>
    )

    class MyTreeCellRenderer : DefaultTreeCellRenderer() {

        init {
            setClosedIcon(null)
            setOpenIcon(null)
            setLeafIcon(null)
        }

        override fun getTreeCellRendererComponent(
            tree: JTree?,
            value: Any?,
            selected: Boolean,
            expanded: Boolean,
            leaf: Boolean,
            row: Int,
            hasFocus: Boolean
        ): Component {
            val cell: Component =
                super.getTreeCellRendererComponent(tree, value, selected, expanded, leaf, row, hasFocus)
            if (cell is JComponent) {
                val label = cell as JLabel
                cell.toolTipText = null
                when (val data = (value as DefaultMutableTreeNode).userObject) {
                    is Result -> {
                        label.text = data.message.text
                        val loc = data.locations.first().physicalLocation
                        label.toolTipText =
                            "(${loc.region.startLine}, ${loc.region.startColumn}) - ${loc.artifactLocation.uri}"
                        label.icon = when (data.level) {
                            Result.Level.ERROR -> AllIcons.General.Error
                            Result.Level.WARNING -> AllIcons.General.Warning
                            Result.Level.NOTE -> AllIcons.General.Note
                            else -> AllIcons.General.TodoQuestion
                        }
                    }

                    is MyFileNodeData -> {
                        label.text = data.fileName
                        label.toolTipText = data.fileUrl.toString()
                        label.icon = if (data.file == null) AllIcons.FileTypes.Unknown else AllIcons.FileTypes.Text
                    }

                    else -> {
                        // do not modify the label - it's probably fine the way it is.
                    }
                }
            }
            return cell
        }

    }
}