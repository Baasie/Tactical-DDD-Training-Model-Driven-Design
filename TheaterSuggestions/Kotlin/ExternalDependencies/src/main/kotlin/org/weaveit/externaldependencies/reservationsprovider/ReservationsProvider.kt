package org.weaveit.externaldependencies.reservationsprovider

import kotlinx.serialization.json.Json
import java.nio.file.Files
import java.nio.file.Paths

class ReservationsProvider {
    private val repository: MutableMap<String, ReservedSeatsDto> = mutableMapOf()
    private val json = Json { ignoreUnknownKeys = true }

    init {
        val jsonDirectory = Paths.get(System.getProperty("user.dir"))
            .parent.parent.parent
            .resolve("Stubs/AuditoriumLayouts")

        Files.newDirectoryStream(jsonDirectory).use { directoryStream ->
            for (path in directoryStream) {
                if (path.toString().contains("_booked_seats.json")) {
                    val fileName = path.fileName.toString()
                    val showId = fileName.split("-")[0]
                    val content = Files.readString(path)
                    repository[showId] = json.decodeFromString<ReservedSeatsDto>(content)
                }
            }
        }
    }

    fun getReservedSeats(showId: String): ReservedSeatsDto {
        return repository[showId] ?: ReservedSeatsDto()
    }
}
