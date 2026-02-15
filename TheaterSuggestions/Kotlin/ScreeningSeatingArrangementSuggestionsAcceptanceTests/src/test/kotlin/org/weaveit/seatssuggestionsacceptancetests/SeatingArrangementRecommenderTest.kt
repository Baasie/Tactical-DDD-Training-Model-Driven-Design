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

//        val auditoriumSeatingArrangements = AuditoriumSeatingArrangements(
//            AuditoriumLayoutRepository(),
//            ReservationsProvider()
//        )
//        val seatingArrangementRecommender = SeatingArrangementRecommender(auditoriumSeatingArrangements)
//        val suggestionsAreMade = seatingArrangementRecommender.makeSuggestions(showId, partyRequested)
//
//        assertEquals(partyRequested, suggestionsAreMade.partyRequested, "Party requested should match")
//        assertEquals(showId, suggestionsAreMade.showId, "Show ID should match")
//        assertIs<SuggestionsAreNotAvailable>(suggestionsAreMade, "Suggestions made should be an instance of SuggestionNotAvailable")
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

//        val auditoriumSeatingArrangements = AuditoriumSeatingArrangements(
//            AuditoriumLayoutRepository(),
//            ReservationsProvider()
//        )
//        val seatingArrangementRecommender = SeatingArrangementRecommender(auditoriumSeatingArrangements)
//        val suggestionsAreMade = seatingArrangementRecommender.makeSuggestions(showId, partyRequested)
//
//        assertThat(suggestionsAreMade.seatNames(PricingCategory.SECOND)).containsExactly("A1", "A2", "A9", "A10", "B1", "B2")
    }

}
