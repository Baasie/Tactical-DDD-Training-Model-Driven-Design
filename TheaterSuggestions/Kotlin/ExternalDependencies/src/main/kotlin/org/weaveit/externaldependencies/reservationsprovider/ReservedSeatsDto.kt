package org.weaveit.externaldependencies.reservationsprovider

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ReservedSeatsDto(
    @SerialName("ReservedSeats") val reservedSeats: List<String> = emptyList()
)
