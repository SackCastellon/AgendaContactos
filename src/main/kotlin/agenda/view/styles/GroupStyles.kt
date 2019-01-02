package agenda.view.styles

import javafx.scene.paint.Color
import javafx.scene.text.FontWeight
import tornadofx.*

class GroupStyles : Stylesheet() {
    companion object {
        val selector: CssRule by cssclass()
        val header: CssRule by cssclass()
        val content: CssRule by cssclass()
        val buttons: CssRule by cssclass()
    }

    init {
        selector {
            header {
                padding = box(0.833333.em) //10px
                spacing = 0.833333.em //10px
                borderColor += box(Color.TRANSPARENT, Color.TRANSPARENT, Color.LIGHTGRAY, Color.TRANSPARENT)
                backgroundColor += Color.WHITE
                label {
                    fontSize = 1.25.em //15px
                    fontWeight = FontWeight.BOLD
                }
            }
            content {
                padding = box(0.833333.em, 1.25.em) //10px, 15px
                spacing = 0.833333.em //10px
            }
            buttons {
                padding = box(0.833333.em) //10px
                borderColor += box(Color.LIGHTGRAY, Color.TRANSPARENT, Color.TRANSPARENT, Color.TRANSPARENT)
            }
        }
    }
}
