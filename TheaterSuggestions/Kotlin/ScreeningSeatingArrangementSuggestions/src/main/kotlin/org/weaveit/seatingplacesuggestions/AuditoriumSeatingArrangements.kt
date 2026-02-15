package org.weaveit.seatingplacesuggestions

import org.weaveit.externaldependencies.auditoriumlayoutrepository.AuditoriumDto
import org.weaveit.externaldependencies.auditoriumlayoutrepository.AuditoriumLayoutRepository
import org.weaveit.externaldependencies.reservationsprovider.ReservationsProvider
import org.weaveit.externaldependencies.reservationsprovider.ReservedSeatsDto

class AuditoriumSeatingArrangements(
    private val auditoriumLayoutRepository: AuditoriumLayoutRepository,
    private val reservedSeatsRepository: ReservationsProvider
) {
    fun findByShowId(showId: String): AuditoriumSeatingArrangement {
        return adapt(
            auditoriumLayoutRepository.findByShowId(showId),
            reservedSeatsRepository.getReservedSeats(showId)
        )
    }

    private fun adapt(auditoriumDto: AuditoriumDto, reservedSeatsDto: ReservedSeatsDto): AuditoriumSeatingArrangement {
        val rows = linkedMapOf<String, Row>()

        for ((rowName, seatDtos) in auditoriumDto.rows) {
            val seats = mutableListOf<SeatingPlace>()

            for (seatDto in seatDtos) {
                val number = extractNumber(seatDto.name)
                val pricingCategory = PricingCategory.valueOf(seatDto.category)
                val isReserved = reservedSeatsDto.reservedSeats.contains(seatDto.name)

                seats.add(
                    SeatingPlace(
                        rowName,
                        number,
                        pricingCategory,
                        if (isReserved) SeatingPlaceAvailability.RESERVED else SeatingPlaceAvailability.AVAILABLE
                    )
                )
            }

            rows[rowName] = Row(rowName, seats.toList())
        }

        return AuditoriumSeatingArrangement(rows)
    }

    private fun extractNumber(name: String): Int {
        return name.substring(1).toInt()
    }
}
