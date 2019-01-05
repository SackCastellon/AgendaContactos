package agenda.view.styles

import javafx.scene.paint.Color
import javafx.scene.text.FontWeight
import tornadofx.*

class CommonStyles : Stylesheet() {
    companion object {
        @JvmStatic val header: CssRule by cssclass()
        @JvmStatic val heading: CssRule by cssclass()
        @JvmStatic val content: CssRule by cssclass()
        @JvmStatic val buttons: CssRule by cssclass()
    }

    init {
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
                fontSize = 1.166667.em //14px
                fontWeight = FontWeight.NORMAL
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
