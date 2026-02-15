package org.weaveit.seatingplacesuggestions

import org.weaveit.externaldependencies.auditoriumlayoutrepository.AuditoriumLayoutRepository
import org.weaveit.externaldependencies.reservationsprovider.ReservationsProvider

class AuditoriumSeatingArrangements(
    private val auditoriumLayoutRepository: AuditoriumLayoutRepository,
    private val reservationsProvider: ReservationsProvider
) {
    fun findByShowId(showId: String): Map<String, Row> {
        val auditoriumDto = auditoriumLayoutRepository.findByShowId(showId)
        val reservedSeatsDto = reservationsProvider.getReservedSeats(showId)

        val rows = linkedMapOf<String, Row>()

        for ((rowName, seats) in auditoriumDto.rows) {
            val seatingPlaces = mutableListOf<SeatingPlace>()

            for (seatDto in seats) {
                val number = seatDto.name.substring(1).toInt()
                val category = PricingCategory.fromValue(seatDto.category)
                val isReserved = reservedSeatsDto.reservedSeats.contains(seatDto.name)

                seatingPlaces.add(SeatingPlace(rowName, number, category, !isReserved))
            }

            rows[rowName] = Row(rowName, seatingPlaces)
        }

        return rows
    }
}
