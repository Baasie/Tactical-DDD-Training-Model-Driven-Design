package org.weaveit.externaldependencies.auditoriumlayoutrepository

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class AuditoriumDto(
    @SerialName("Rows") val rows: Map<String, List<SeatDto>> = emptyMap(),
    @SerialName("Corridors") val corridors: List<CorridorDto> = emptyList()
)
