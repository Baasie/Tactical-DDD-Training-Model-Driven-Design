package org.weaveit.seatssuggestionsacceptancetests

import org.weaveit.externaldependencies.auditoriumlayoutrepository.AuditoriumLayoutRepository
import org.weaveit.externaldependencies.reservationsprovider.ReservationsProvider
import org.weaveit.seatingplacesuggestions.*
import org.weaveit.seatingplacesuggestions.api.FileBasedAuditoriumSeatingArrangements
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

        val auditoriumSeatingArrangements = FileBasedAuditoriumSeatingArrangements(
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

        val auditoriumSeatingArrangements = FileBasedAuditoriumSeatingArrangements(
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
    fun `suggest adjacent seats for a party of two`() {
        // Lincoln-17
        //
        //     1   2   3   4   5   6   7   8   9  10
        //  A: 2   2   1   1   1   1   1   1   2   2
        //  B: 2   2   1   1   1   1   1   1   2   2
        val showId = "17"
        val partyRequested = 2

        val auditoriumSeatingArrangements = FileBasedAuditoriumSeatingArrangements(
            AuditoriumLayoutRepository(),
            ReservationsProvider()
        )
        val seatingArrangementRecommender = SeatingArrangementRecommender(auditoriumSeatingArrangements)
        val suggestionsAreMade = seatingArrangementRecommender.makeSuggestions(showId, partyRequested)

        assertThat(suggestionsAreMade.seatNames(PricingCategory.SECOND)).containsExactly("A1-A2", "A9-A10", "B1-B2")
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

        val auditoriumSeatingArrangements = FileBasedAuditoriumSeatingArrangements(
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

        val auditoriumSeatingArrangements = FileBasedAuditoriumSeatingArrangements(
            AuditoriumLayoutRepository(),
            ReservationsProvider()
        )
        val seatingArrangementRecommender = SeatingArrangementRecommender(auditoriumSeatingArrangements)
        val suggestionsAreMade = seatingArrangementRecommender.makeSuggestions(showId, partyRequested)

        // Order matters: A4 before A3 (A4 is closer to middle), then B5 (middle of row B)
        assertThat(suggestionsAreMade.seatNames(PricingCategory.FIRST)).containsExactly("A4", "A3", "B5")
    }

    @Test
    fun `offer adjacent seats nearer the middle of a row when it is possible`() {
        // Dock Street Auditorium-3
        //
        //      1   2   3   4   5   6   7   8   9  10
        // A:  (2) (2) (1) (1) (1)  1   1   1   2   2
        // B:   2   2   1   1  (1) (1) (1) (1)  2   2
        // C:   2   2   2   2   2   2   2   2   2   2
        // D:   2   2   2   2   2   2   2   2   2   2
        // E:   3   3   3   3   3   3   3   3   3   3
        // F:   3   3   3   3   3   3   3   3   3   3
        val showId = "3"
        val partyRequested = 4

        val auditoriumSeatingArrangements = FileBasedAuditoriumSeatingArrangements(
            AuditoriumLayoutRepository(),
            ReservationsProvider()
        )
        val seatingArrangementRecommender = SeatingArrangementRecommender(auditoriumSeatingArrangements)
        val suggestionsAreMade = seatingArrangementRecommender.makeSuggestions(showId, partyRequested)

        assertThat(suggestionsAreMade.seatNames(PricingCategory.FIRST)).isEmpty()
        assertThat(suggestionsAreMade.seatNames(PricingCategory.SECOND)).containsExactly("C4-C5-C6-C7", "D4-D5-D6-D7")
        assertThat(suggestionsAreMade.seatNames(PricingCategory.THIRD)).containsExactly("E4-E5-E6-E7", "F4-F5-F6-F7")
        assertThat(suggestionsAreMade.seatNames(PricingCategory.MIXED)).containsExactly("A6-A7-A8-A9", "B1-B2-B3-B4", "C4-C5-C6-C7")
    }

    @Test
    fun `should offer 3 adjacent seats nearer the middle of a row when it is possible`() {
        // Dock Street Auditorium-3
        //
        //      1   2   3   4   5   6   7   8   9  10
        // A : (2) (2) (1) (1) (1)  1   1   1   2   2
        // B :  2   2   1   1  (1) (1) (1) (1)  2   2
        // C :  2   2   2   2   2   2   2   2   2   2
        // D :  2   2   2   2   2   2   2   2   2   2
        // E :  3   3   3   3   3   3   3   3   3   3
        // F :  3   3   3   3   3   3   3   3   3   3
        val showId = "3"
        val partyRequested = 3

        val auditoriumSeatingArrangements = FileBasedAuditoriumSeatingArrangements(
            AuditoriumLayoutRepository(),
            ReservationsProvider()
        )
        val seatingArrangementRecommender = SeatingArrangementRecommender(auditoriumSeatingArrangements)
        val suggestionsAreMade = seatingArrangementRecommender.makeSuggestions(showId, partyRequested)

        assertThat(suggestionsAreMade.seatNames(PricingCategory.FIRST)).containsExactly("A6-A7-A8")
        assertThat(suggestionsAreMade.seatNames(PricingCategory.SECOND)).containsExactly("C4-C5-C6", "C7-C8-C9", "C1-C2-C3")
        assertThat(suggestionsAreMade.seatNames(PricingCategory.THIRD)).containsExactly("E4-E5-E6", "E7-E8-E9", "E1-E2-E3")
        assertThat(suggestionsAreMade.seatNames(PricingCategory.MIXED)).containsExactly("A6-A7-A8", "B2-B3-B4", "C4-C5-C6")
    }
}
