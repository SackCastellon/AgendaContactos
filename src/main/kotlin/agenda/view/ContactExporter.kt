package agenda.view

import agenda.view.styles.CommonStyles
import javafx.scene.control.ButtonBar
import tornadofx.*

class ContactExporter : View() {
    override val root = borderpane {
        center {

        }
        bottom {
            buttonbar {
                addClass(CommonStyles.buttons)
                button(messages["button.cancel"], ButtonBar.ButtonData.CANCEL_CLOSE) {
                    isCancelButton = true
                    action(::close)
                }
                button(messages["button.export"], ButtonBar.ButtonData.OK_DONE) {
                    isDefaultButton = true
                    action { TODO("Export file") }
                }
            }
        }
    }

}
