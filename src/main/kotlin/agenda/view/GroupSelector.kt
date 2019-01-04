package agenda.view

import agenda.data.dao.IDao
import agenda.model.Group
import agenda.view.styles.CommonStyles
import javafx.beans.property.ListProperty
import javafx.beans.property.SimpleListProperty
import javafx.collections.FXCollections
import javafx.geometry.Pos
import javafx.scene.control.Button
import javafx.scene.control.ButtonBar
import javafx.scene.control.ScrollPane
import javafx.scene.layout.Priority
import org.kordamp.ikonli.javafx.FontIcon
import org.kordamp.ikonli.material.Material
import tornadofx.*

class GroupSelector : Fragment() {

    private val groups: IDao<Group> by di("groups")

    private val groupModel = Group.ViewModel(Group.empty())
    val groupsList: ListProperty<Group> = SimpleListProperty(FXCollections.observableArrayList())

    override val root = borderpane {
        top {
            hbox {
                addClass(CommonStyles.header)
                vbox(5) {
                    label(messages["title"])
                }
            }
        }
        center {
            scrollpane(fitToWidth = true) {
                vbarPolicy = ScrollPane.ScrollBarPolicy.ALWAYS
                prefHeight = 250.0
                prefWidth = 225.0

                vbox(7) {
                    addClass(CommonStyles.content)

                    vbox(5).bindChildren(groups.observable) {
                        hbox(5) {
                            alignment = Pos.CENTER_LEFT

                            checkbox(it.name) {
                                runLater { isSelected = it in groupsList }
                                selectedProperty().onChange { selected ->
                                    if (selected) {
                                        val i = groupsList.binarySearchBy(it.id, selector = Group::id)
                                        if (i < 0) groupsList.add(-i - 1, it)
                                    } else groupsList.remove(it)
                                }
                            }
                        }
                    }

                    hbox(5) {
                        alignment = Pos.CENTER_LEFT

                        @Suppress("JoinDeclarationAndAssignment")
                        lateinit var btn: Button

                        textfield(groupModel.name) {
                            hgrow = Priority.ALWAYS
                            promptText = messages["field.newGroup.prompt"]
                            validator {
                                when {
                                    it.isNullOrBlank() -> error(messages["error.field.blank"])
                                    groups.observable.any { group -> group.name == it } -> error(messages["error.field.nameAlreadyUsed"])
                                    else -> null
                                }
                            }
                            action { btn.fire() }
                        }
                        btn = button {
                            padding = insets(4)
                            graphic = FontIcon.of(Material.ADD, 18)
                            action {
                                groupModel.commit {
                                    groups.add(groupModel.item)
                                    groupModel.item = Group.empty()
                                    runLater { groupModel.validate(decorateErrors = false) }
                                }
                            }
                        }
                    }
                }
            }
        }
        bottom {
            buttonbar {
                addClass(CommonStyles.buttons)
                button(messages["button.close"], ButtonBar.ButtonData.CANCEL_CLOSE) {
                    isCancelButton = true
                    action(::close)
                }
            }
        }
    }

    init {
        title = messages["title"]
    }
}
