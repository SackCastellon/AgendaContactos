package agenda.view

import agenda.data.dao.IDao
import agenda.model.Contact
import agenda.model.Email
import agenda.model.Phone
import agenda.util.StringConverter
import agenda.view.styles.CommonStyles
import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.github.benmanes.caffeine.cache.Caffeine
import com.github.benmanes.caffeine.cache.LoadingCache
import ezvcard.VCard
import ezvcard.VCardVersion
import ezvcard.io.text.VCardWriter
import ezvcard.parameter.EmailType
import ezvcard.parameter.TelephoneType
import ezvcard.property.StructuredName
import io.github.soc.directories.UserDirectories
import javafx.beans.property.SimpleBooleanProperty
import javafx.beans.property.SimpleObjectProperty
import javafx.collections.transformation.SortedList
import javafx.geometry.Pos
import javafx.scene.control.ButtonBar
import javafx.scene.control.ButtonType
import javafx.stage.FileChooser.ExtensionFilter
import mu.KotlinLogging
import org.apache.commons.lang3.SystemUtils
import org.koin.standalone.KoinComponent
import org.koin.standalone.get
import org.kordamp.ikonli.javafx.FontIcon
import org.kordamp.ikonli.material.Material
import tornadofx.*
import java.io.File
import java.io.Writer
import java.nio.file.Files
import java.nio.file.StandardOpenOption.*
import java.util.stream.Stream
import kotlin.reflect.KClass
import kotlin.reflect.KMutableProperty1
import kotlin.reflect.KProperty1
import kotlin.reflect.KVisibility
import kotlin.reflect.full.memberProperties
import kotlin.reflect.full.superclasses
import kotlin.streams.asSequence

class ContactExporter : Fragment(), KoinComponent {

    private val contactsList: SortedList<Contact> = get<IDao<Contact>>("contacts").observable.sorted()

    private val field = SimpleObjectProperty<SortField>(SortField.FIRST_NAME)
    private val order = SimpleObjectProperty<SortOrder>(SortOrder.ASC)
    private val file = SimpleObjectProperty<String>(defaultFile.path)
    private val showInFolder = SimpleBooleanProperty(false)

    override val root = borderpane {
        top {
            hbox {
                addClass(CommonStyles.header)
                vbox(5) {
                    label(messages["export.title"])
                }
            }
        }
        center {
            vbox(7) {
                addClass(CommonStyles.content)
                hbox(5, Pos.CENTER) {
                    label(messages["export.sort.field"])
                    choicebox(field, SortField.values().asList()) {
                        converter = StringConverter { messages[it.key] }
                    }
                }
                hbox(5, Pos.CENTER) {
                    label(messages["export.sort.order"])
                    choicebox(order, SortOrder.values().asList()) {
                        converter = StringConverter { messages[it.key] }
                    }
                }
                hbox(5, Pos.CENTER) {
                    label(messages["export.file"])
                    textfield(file) {
                        prefWidth = 250.0
                        isEditable = false
                    }
                    button {
                        padding = insets(4)
                        graphic = FontIcon.of(Material.FOLDER, 18)
                        action(::selectOutputFile)
                    }
                }
                hbox(5, Pos.CENTER) {
                    checkbox(messages["export.showInFolder"], showInFolder)
                }
            }
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
                    action(::handleExport)
                }
            }
        }
    }

    init {
        title = messages["export.title"]

        contactsList.comparatorProperty().bind(objectBinding(field, order) { order.value.comparator(field.value.getter) })

        runLater { currentStage?.sizeToScene() }
    }

    private fun selectOutputFile() {
        val files = chooseFile(
            messages["export.selectFile"],
            ExportFormat.filters,
            FileChooserMode.Save
        ) {
            val initialFile = File(file.value)
            selectedExtensionFilter = ExportFormat.byExtension(initialFile.extension)?.filter ?: ExportFormat.filters.first()
            initialDirectory = initialFile.parentFile
            initialFileName = initialFile.name
        }

        files.takeIf { it.isNotEmpty() }?.single()?.let { file.value = it.absolutePath }
    }

    private fun handleExport() {
        val outFile = File(file.value)

        if (outFile.exists() && !outFile.isFile) {
            warning(messages["export.warn.header"], messages["export.warn.invalidPath"], ButtonType.CLOSE)
        } else {
            val format = ExportFormat.byExtension(outFile.extension)

            if (format == null) {
                warning(
                    messages["export.warn.header"],
                    messages["export.warn.invalidFormat"].format(ExportFormat.values().joinToString { "(${it.extension})" }),
                    ButtonType.CLOSE
                )
            } else {
                with(format) { contactsList exportTo outFile }
                if (showInFolder.value) outFile.showInDesktop()
                close()
            }
        }
    }

    private fun File.showInDesktop() {
        runLater {
            val commands = when {
                SystemUtils.IS_OS_WINDOWS -> listOf(listOf("explorer.exe", "/select,", absolutePath))
                SystemUtils.IS_OS_MAC_OSX -> listOf(listOf("open", "-R", absolutePath))
                SystemUtils.IS_OS_LINUX -> listOf(listOf("nautilus", absolutePath), listOf("dolphin", "--select", absolutePath))
                else -> emptyList()
            }

            if (commands.isEmpty()) logger.error { "Unknown OS, cannot open file in system explorer" }
            else commands.firstOrNull { cmd ->
                ProcessBuilder(cmd).runCatching { start() }
                    .onFailure { logger.error("Failed to run command: $cmd", it) }.isSuccess
            }
        }
    }

    companion object {
        private val logger = KotlinLogging.logger {}

        private val defaultFile: File
            get() {
                val dir = UserDirectories.get().documentDir!!
                val name = "contacts"
                val ext = ExportFormat.filters.first().extensions.first().substring(1)

                return File(dir, "$name$ext")
                    .takeIf { !it.exists() || !it.isFile } ?: (1..Int.MAX_VALUE).asSequence()
                    .map { File(dir, "$name ($it)$ext") }
                    .first { !it.exists() || !it.isFile }
            }
    }

    private enum class ExportFormat(description: String, val extension: String) {
        TXT("Text file", "*.txt") {
            private val properties: LoadingCache<KClass<out Any>, List<KProperty1<out Any, Any?>>> = Caffeine.newBuilder().build { kClass ->
                val data2 = kClass.java.annotations.filterIsInstance<Metadata>().single().data2
                kClass.memberProperties.filter { prop -> prop.getter.annotations.none { ann -> ann.annotationClass == JsonIgnore::class } }
                    .sortedBy { prop -> data2.indexOf(prop.name).takeIf { it >= 0 } ?: Int.MAX_VALUE }
            }

            override fun export(contacts: List<Contact>, file: File) {
                fun Any.stringify(depth: Int = 0): String {

                    val className = Stream.iterate(this::class) { kClass -> kClass.superclasses.first { !it.java.isInterface } }
                        .asSequence().first { it.visibility == KVisibility.PUBLIC }.simpleName!!

                    val padding = "\t\t".repeat(depth)

                    return properties[this::class]!!
                        .map { it.name to it.call(this) }
                        .joinToString("\n\t$padding", "$className:\n\t$padding") { (name, value) ->
                            "$name: ${when (value) {
                                is String -> "\"$value\""
                                is Iterable<*> -> value.filterNotNull().joinToString("\n\t\t", "\n\t\t") { it.stringify(depth + 1) }
                                else -> value.toString()
                            }}"
                        }
                }

                val data = contacts.map { it.stringify() }

                file.use { w -> data.forEach { w.appendln(it) } }
            }
        },
        JSON("JavaScript Object Notation", "*.json") {
            override fun export(contacts: List<Contact>, file: File) {
                file.use { jacksonObjectMapper().writerWithDefaultPrettyPrinter().writeValue(it, contacts) }
            }
        },
        VCF("Virtual Contact File", "*.vcf") {
            override fun export(contacts: List<Contact>, file: File) {
                fun Phone.Label.toType(): TelephoneType = when (this) {
                    Phone.Label.HOME -> TelephoneType.HOME
                    Phone.Label.WORK -> TelephoneType.WORK
                    Phone.Label.MOBILE -> TelephoneType.CELL
                }

                fun Email.Label.toType(): EmailType = when (this) {
                    Email.Label.PERSONAL -> EmailType.HOME
                    Email.Label.WORK -> EmailType.WORK
                }

                val vCards = contacts.map { contact ->
                    VCard().apply {
                        structuredName = StructuredName().apply {
                            given = contact.firstName
                            family = contact.lastName
                        }
                        setFormattedName(contact.fullName)
                        contact.phones.forEach { addTelephoneNumber(it.phone, it.label.toType()) }
                        contact.emails.forEach { addEmail(it.email, it.label.toType()) }
                        setCategories(*contact.groups.map { it.name }.toTypedArray())
                    }
                }

                file.use { VCardWriter(it, VCardVersion.V4_0).use { vCardWriter -> vCards.forEach(vCardWriter::write) } }
            }
        };

        val filter: ExtensionFilter = ExtensionFilter(description, extension)

        protected abstract fun export(contacts: List<Contact>, file: File)

        protected inline fun File.use(block: (Writer) -> Unit) {
            val file = this.toPath().toAbsolutePath()
            if (Files.notExists(file.parent)) Files.createDirectories(file.parent)
            Files.newBufferedWriter(file, Charsets.UTF_8, WRITE, TRUNCATE_EXISTING, CREATE).use(block)
        }

        infix fun List<Contact>.exportTo(file: File) {
            export(this, file)
            logger.debug { "Saved ${this.size} contacts to '${file.absolutePath}'" }
        }

        companion object {
            @JvmField val filters: Array<ExtensionFilter> = values().map(ExportFormat::filter).toTypedArray()
            @JvmStatic fun byExtension(ext: String): ExportFormat? = values().firstOrNull { ext.equals(it.extension.substring(2), ignoreCase = true) }
        }
    }

    private enum class SortField(val key: String) {
        FIRST_NAME("export.sort.field.firstName") {
            override val getter: KMutableProperty1<Contact, String> = Contact::firstName
        },
        LAST_NAME("export.sort.field.lastName") {
            override val getter: KMutableProperty1<Contact, String> = Contact::lastName
        };

        abstract val getter: KMutableProperty1<Contact, String>
    }

    private enum class SortOrder(val key: String) {
        ASC("export.sort.order.asc") {
            override fun <T> comparator(selector: (T) -> Comparable<*>?): Comparator<T> = compareBy(selector)
        },
        DESC("export.sort.order.desc") {
            override fun <T> comparator(selector: (T) -> Comparable<*>?): Comparator<T> = compareByDescending(selector)
        };

        abstract fun <T> comparator(selector: (T) -> Comparable<*>?): Comparator<T>
    }
}
