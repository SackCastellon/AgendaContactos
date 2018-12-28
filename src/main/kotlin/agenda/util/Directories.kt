package agenda.util

import io.github.soc.directories.BaseDirectories
import mu.KotlinLogging
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths

object Directories {
    private val logger = KotlinLogging.logger {}

    private val root: Path = Paths.get(BaseDirectories.get().dataLocalDir, "UJI", "Agenda")

    private val data: Path = root / "data"
    private val cache: Path = root / "cache"
    private val config: Path = root / "config"

    val databaseFile: Path = data / "database.sqlite"
    val configFile: Path = config / "configuration.json"
    val lockFile: Path = cache / ".lock"

    init {
        logger.debug { "Root directory: $root" }
    }

    fun create() {
        listOf(data, cache, config)
            .filter { Files.notExists(it) }
            .also { if (it.isNotEmpty()) logger.debug { "Creating sub-directories:" } }
            .forEach { dir ->
                kotlin.runCatching { Files.createDirectories(dir) }.fold(
                    onSuccess = { logger.debug { " [OK]  $dir" } },
                    onFailure = { logger.warn(it) { "[FAIL] $dir" } }
                )
            }
    }
}

@Suppress("NOTHING_TO_INLINE")
private inline operator fun Path.div(other: String): Path = this.resolve(other)
