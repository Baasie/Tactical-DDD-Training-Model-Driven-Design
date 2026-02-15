package org.weaveit.externaldependencies.auditoriumlayoutrepository

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class SeatDto(
    @SerialName("Name") val name: String,
    @SerialName("Category") val category: Int
)
