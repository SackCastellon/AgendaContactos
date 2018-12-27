package es.uji.ei1039.agenda.view.styles

import javafx.scene.paint.Color
import javafx.scene.text.FontWeight
import tornadofx.*

class EditorStyles : Stylesheet() {
    companion object {
        val editor: CssRule by cssclass()
        val header: CssRule by cssclass()
        val heading: CssRule by cssclass()
        val content: CssRule by cssclass()
        val buttons: CssRule by cssclass()
        val fieldset: CssRule by cssclass()
    }

    init {
        editor {
            header {
                padding = box(0.833333.em) //10px
                spacing = 0.833333.em //10px
                borderColor += box(Color.TRANSPARENT, Color.TRANSPARENT, Color.LIGHTGRAY, Color.TRANSPARENT)
                backgroundColor += Color.WHITE
                label {
                    fontSize = 1.5.em //18px
                    fontWeight = FontWeight.BOLD
                }
                heading {
                    fontSize = 1.25.em //15px
                    fontWeight = FontWeight.NORMAL
                }
            }
            content {
                padding = box(0.833333.em, 1.25.em) //10px, 15px
                spacing = 0.833333.em //10px
                fieldset {
                    fontWeight = FontWeight.BOLD
                }
            }
            buttons {
                padding = box(0.833333.em) //10px
                borderColor += box(Color.LIGHTGRAY, Color.TRANSPARENT, Color.TRANSPARENT, Color.TRANSPARENT)
            }
        }
    }
}
