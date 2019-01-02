package agenda.view

import agenda.data.dao.IDao
import agenda.model.Contact
import agenda.model.Group
import agenda.model.isDefault
import agenda.view.styles.GroupStyles
import javafx.geometry.Pos
import javafx.scene.control.Button
import javafx.scene.control.ButtonBar
import javafx.scene.control.ButtonType
import javafx.scene.control.ScrollPane
import org.kordamp.ikonli.javafx.FontIcon
import org.kordamp.ikonli.material.Material
import tornadofx.*

class GroupsViewer : Fragment() {

    private val contacts: IDao<Contact> by di("contacts")
    private val groups: IDao<Group> by di("groups")

    private val groupModel = Group.ViewModel(Group.empty())

    override val root = borderpane {
        addClass(GroupStyles.selector)
        top {
            hbox {
                addClass(GroupStyles.header)
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
                    addClass(GroupStyles.content)

                    vbox(5).bindChildren(groups.observable) { group ->
                        hbox(5) {
                            alignment = Pos.CENTER_LEFT

                            label(group.name)
                            spacer()
                            vbox {
                                button {
                                    padding = insets(4)
                                    graphic = FontIcon.of(Material.REMOVE, 18)

                                    if (group.isDefault) {
                                        parent.tooltip(messages["tooltip.defaultGroup"])
                                        isDisable = true
                                    } else {
                                        tooltip(messages["tooltip.deleteGroup"])
                                        action {
                                            val isUsed = contacts.observable.any { contact -> group in contact.groups }

                                            fun removeGroup() = groups.remove(group.id)

                                            if (isUsed) {
                                                warning(messages["alert.header"], messages["alert.header"], ButtonType.YES, ButtonType.NO) {
                                                    if (it == ButtonType.YES) removeGroup()
                                                }
                                            } else {
                                                removeGroup()
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }

                    hbox(5) {
                        alignment = Pos.CENTER_LEFT

                        @Suppress("JoinDeclarationAndAssignment")
                        lateinit var btn: Button

                        textfield(groupModel.name) {
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
                            tooltip(messages["tooltip.deleteGroup"])
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
                addClass(GroupStyles.buttons)
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
