package agenda.view.styles

import javafx.scene.text.FontWeight
import tornadofx.CssRule
import tornadofx.Stylesheet
import tornadofx.cssclass

class EditorStyles : Stylesheet() {
    companion object {
        val editor: CssRule by cssclass()
        val fieldset: CssRule by cssclass()
    }

    init {
        editor {
            with(CommonStyles.Companion) {
                content {
                    fieldset {
                        fontWeight = FontWeight.BOLD
                    }
                }
            }
        }
    }
}
