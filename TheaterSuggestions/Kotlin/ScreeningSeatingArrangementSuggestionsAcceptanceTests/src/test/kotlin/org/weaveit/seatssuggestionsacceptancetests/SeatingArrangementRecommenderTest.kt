package org.weaveit.seatssuggestionsacceptancetests

import org.weaveit.externaldependencies.auditoriumlayoutrepository.AuditoriumLayoutRepository
import org.weaveit.externaldependencies.reservationsprovider.ReservationsProvider
import org.weaveit.seatingplacesuggestions.*
import org.junit.jupiter.api.Test
import org.assertj.core.api.Assertions.assertThat
import kotlin.test.assertEquals
import kotlin.test.assertIs

class SeatingArrangementRecommenderTest {
    /*
     *  Business Rule - Only Suggest available seats
     */

    @Test
    fun `suggest one seatingPlace when Auditorium contains one available seatingPlace`() {
        // Ford Auditorium-1
        //       1   2   3   4   5   6   7   8   9  10
        //  A : (2) (2)  1  (1) (1) (1) (1) (1) (2) (2)
        //  B : (2) (2) (1) (1) (1) (1) (1) (1) (2) (2)
        val showId = "1"
        val partyRequested = 1

        val auditoriumSeatingArrangements = AuditoriumSeatingArrangements(
            AuditoriumLayoutRepository(),
            ReservationsProvider()
        )
        val seatingArrangementRecommender = SeatingArrangementRecommender(auditoriumSeatingArrangements)
        val suggestionsAreMade = seatingArrangementRecommender.makeSuggestions(showId, partyRequested)

        assertThat(suggestionsAreMade.seatNames(PricingCategory.FIRST)).containsExactly("A3")
    }

    @Test
    fun `return SuggestionNotAvailable when Auditorium has all its seatingPlaces reserved`() {
        // Madison Auditorium-5
        //      1   2   3   4   5   6   7   8   9  10
        // A : (2) (2) (1) (1) (1) (1) (1) (1) (2) (2)
        // B : (2) (2) (1) (1) (1) (1) (1) (1) (2) (2)
        val showId = "5"
        val partyRequested = 1

        val auditoriumSeatingArrangements = AuditoriumSeatingArrangements(
            AuditoriumLayoutRepository(),
            ReservationsProvider()
        )
        val seatingArrangementRecommender = SeatingArrangementRecommender(auditoriumSeatingArrangements)
        val suggestionsAreMade = seatingArrangementRecommender.makeSuggestions(showId, partyRequested)

        assertEquals(partyRequested, suggestionsAreMade.partyRequested, "Party requested should match")
        assertEquals(showId, suggestionsAreMade.showId, "Show ID should match")
        assertIs<SuggestionsAreNotAvailable>(suggestionsAreMade, "Suggestions made should be an instance of SuggestionNotAvailable")
    }

    @Test
    fun `suggest two seatingPlaces when Auditorium contains all available seatingPlaces`() {
        // Lincoln-17
        //
        //     1   2   3   4   5   6   7   8   9  10
        //  A: 2   2   1   1   1   1   1   1   2   2
        //  B: 2   2   1   1   1   1   1   1   2   2
        val showId = "17"
        val partyRequested = 2

        val auditoriumSeatingArrangements = AuditoriumSeatingArrangements(
            AuditoriumLayoutRepository(),
            ReservationsProvider()
        )
        val seatingArrangementRecommender = SeatingArrangementRecommender(auditoriumSeatingArrangements)
        val suggestionsAreMade = seatingArrangementRecommender.makeSuggestions(showId, partyRequested)

        assertThat(suggestionsAreMade.seatNames(PricingCategory.SECOND)).containsExactly("A2", "A9", "A1", "A10", "B2", "B9")
    }


    @Test
    fun `suggest three availabilities per PricingCategory`() {
        // New Amsterdam-18
        //     1   2   3   4   5   6   7   8   9  10
        //  A: 2   2   1   1   1   1   1   1   2   2
        //  B: 2   2   1   1   1   1   1   1   2   2
        //  C: 2   2   2   2   2   2   2   2   2   2
        //  D: 2   2   2   2   2   2   2   2   2   2
        //  E: 3   3   3   3   3   3   3   3   3   3
        //  F: 3   3   3   3   3   3   3   3   3   3
        val showId = "18"
        val partyRequested = 1

        val auditoriumSeatingArrangements = AuditoriumSeatingArrangements(
            AuditoriumLayoutRepository(),
            ReservationsProvider()
        )
        val seatingArrangementRecommender = SeatingArrangementRecommender(auditoriumSeatingArrangements)
        val suggestionsAreMade = seatingArrangementRecommender.makeSuggestions(showId, partyRequested)

        // Middle-out ordering: seats closest to middle (5.5) come first
        assertThat(suggestionsAreMade.seatNames(PricingCategory.FIRST)).containsExactly("A5", "A6", "A4")
        assertThat(suggestionsAreMade.seatNames(PricingCategory.SECOND)).containsExactly("A2", "A9", "A1")
        assertThat(suggestionsAreMade.seatNames(PricingCategory.THIRD)).containsExactly("E5", "E6", "E4")
        assertThat(suggestionsAreMade.seatNames(PricingCategory.MIXED)).containsExactly("A5", "A6", "A4")
    }

    @Test
    fun `should offer seats nearer the middle of a row`() {
        // Mogador Auditorium-9
        //
        //    1   2   3   4   5   6   7   8   9  10
        // A: 2   2   1   1  (1) (1) (1) (1)  2   2
        // B: 2   2   1   1   1   1   1   1   2   2
        //
        // Available FIRST category: A3, A4 (row A) and B3-B8 (row B)
        // Middle of row: between seats 5 and 6
        val showId = "9"
        val partyRequested = 1

        val auditoriumSeatingArrangements = AuditoriumSeatingArrangements(
            AuditoriumLayoutRepository(),
            ReservationsProvider()
        )
        val seatingArrangementRecommender = SeatingArrangementRecommender(auditoriumSeatingArrangements)
        val suggestionsAreMade = seatingArrangementRecommender.makeSuggestions(showId, partyRequested)

        // Order matters: A4 before A3 (A4 is closer to middle), then B5 (middle of row B)
        assertThat(suggestionsAreMade.seatNames(PricingCategory.FIRST)).containsExactly("A4", "A3", "B5")
    }
}
