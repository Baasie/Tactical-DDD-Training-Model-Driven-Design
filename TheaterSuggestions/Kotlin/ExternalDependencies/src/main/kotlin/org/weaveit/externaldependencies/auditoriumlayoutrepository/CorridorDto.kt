package org.weaveit.externaldependencies.auditoriumlayoutrepository

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CorridorDto(
    @SerialName("Number") val number: Int,
    @SerialName("InvolvedRowNames") val involvedRowNames: List<String>
)
