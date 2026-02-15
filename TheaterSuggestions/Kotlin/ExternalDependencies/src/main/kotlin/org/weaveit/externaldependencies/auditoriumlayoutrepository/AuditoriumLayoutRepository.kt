package org.weaveit.externaldependencies.auditoriumlayoutrepository

import kotlinx.serialization.json.Json
import java.nio.file.Files
import java.nio.file.Paths

class AuditoriumLayoutRepository {
    private val repository: MutableMap<String, AuditoriumDto> = mutableMapOf()
    private val json = Json { ignoreUnknownKeys = true }

    init {
        val jsonDirectory = Paths.get(System.getProperty("user.dir"))
            .parent.parent.parent
            .resolve("Stubs/AuditoriumLayouts")

        Files.newDirectoryStream(jsonDirectory).use { directoryStream ->
            for (path in directoryStream) {
                if (path.toString().contains("_theater.json")) {
                    val fileName = path.fileName.toString()
                    val showId = fileName.split("-")[0]
                    val content = Files.readString(path)
                    repository[showId] = json.decodeFromString<AuditoriumDto>(content)
                }
            }
        }
    }

    fun findByShowId(showId: String): AuditoriumDto {
        return repository[showId] ?: AuditoriumDto()
    }
}
