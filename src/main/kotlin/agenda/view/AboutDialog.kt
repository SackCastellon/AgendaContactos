package agenda.view

import agenda.util.browse
import agenda.view.styles.CommonStyles
import javafx.scene.control.ButtonBar
import javafx.scene.text.FontWeight
import javafx.scene.text.TextAlignment
import tornadofx.*
import java.net.URI

class AboutDialog : Fragment() {
    override val root = borderpane {
        prefWidth = 325.0
        top {
            hbox {
                addClass(CommonStyles.header)
                vbox(5) {
                    label(messages["about.title"])
                }
            }
        }
        center {
            vbox(7) {
                addClass(CommonStyles.content)
                scrollpane(true) {
                    style {
                        backgroundColor = multi(c("#F4F4F4"))
                    }
                    prefHeight = 75.0
                    label(messages["about.description"]) {
                        textAlignment = TextAlignment.JUSTIFY
                        isWrapText = true
                    }
                }
                vbox(5) {
                    prefHeight = 200.0
                    label(messages["about.libraries"]) {
                        style { fontWeight = FontWeight.BOLD }
                    }

                    class Lib(val name: String, val version: String, val url: String)

                    val libraries = listOf(
                        Lib("Koltin Standard Library", "1.3.11", "https://github.com/JetBrains/kotlin"),
                        Lib("KOIN", "1.0.2", "https://github.com/InsertKoinIO/koin"),
                        Lib("Caffeine", "2.6.2", "https://github.com/ben-manes/caffeine"),
                        Lib("Guava", "27.0.1-jre", "https://github.com/google/guava"),
                        Lib("TornadoFX", "1.7.19-SNAPSHOT", "https://github.com/edvin/tornadofx"),
                        Lib("TornadoFX-ControlsFX", "0.1.1", "https://github.com/edvin/tornadofx-controlsfx"),
                        Lib("ControlsFX", "8.40.14", "https://github.com/controlsfx/controlsfx"),
                        Lib("Ikonli", "2.4.0", "https://github.com/aalmiray/ikonli"),
                        Lib("Exposed", "0.11.2", "https://github.com/JetBrains/Exposed"),
                        Lib("SQLite", "3.25.2", "https://github.com/xerial/sqlite-jdbc"),
                        Lib("Jackson Kotlin Module", "2.9.8", "https://github.com/FasterXML/jackson-module-kotlin"),
                        Lib("ez-vcard", "0.10.5", "https://github.com/mangstadt/ez-vcard"),
                        Lib("Directories", "10", "https://github.com/soc/directories-jvm"),
                        Lib("Apache Commons Validator", "1.6", "https://github.com/apache/commons-validator"),
                        Lib("libphonenumber", "8.10.2", "https://github.com/googlei18n/libphonenumber"),
                        Lib("Logback", "1.3.0-alpha4", "https://github.com/qos-ch/logback"),
                        Lib("kotlin-logging", "1.6.22", "https://github.com/MicroUtils/kotlin-logging"),
                        Lib("Apache Commons Text", "1.5", "https://github.com/apache/commons-text")
                    )

                    listview(libraries.observable()) {
                        cellCache {
                            hyperlink("${it.name}@${it.version}") {
                                tooltip(it.url)
                                action { browse(URI(it.url)) }
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
        title = messages["about.title"]

        runLater { currentStage?.sizeToScene() }
    }
}
