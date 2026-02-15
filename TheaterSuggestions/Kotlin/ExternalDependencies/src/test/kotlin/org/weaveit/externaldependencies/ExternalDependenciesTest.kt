package org.weaveit.externaldependencies

import org.weaveit.externaldependencies.auditoriumlayoutrepository.AuditoriumLayoutRepository
import org.weaveit.externaldependencies.reservationsprovider.ReservationsProvider
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class ExternalDependenciesTest {

    @Test
    fun `should allow us to retrieve reserved seats for a given ShowId`() {
        val seatsRepository = ReservationsProvider()
        val reservedSeatsDto = seatsRepository.getReservedSeats("1")

        assertEquals(19, reservedSeatsDto.reservedSeats.size, "Expected 19 reserved seats")
    }

    @Test
    fun `should allow us to retrieve AuditoriumLayout for a given ShowId`() {
        val eventRepository = AuditoriumLayoutRepository()
        val theaterDto = eventRepository.findByShowId("2")

        assertEquals(6, theaterDto.rows.size, "Expected 6 rows in the auditorium")
        assertEquals(2, theaterDto.corridors.size, "Expected 2 corridors in the auditorium")

        val firstSeatOfFirstRow = theaterDto.rows["A"]?.get(0)
        assertEquals(2, firstSeatOfFirstRow?.category, "Expected category 2 for the first seat of the first row")
    }
}
