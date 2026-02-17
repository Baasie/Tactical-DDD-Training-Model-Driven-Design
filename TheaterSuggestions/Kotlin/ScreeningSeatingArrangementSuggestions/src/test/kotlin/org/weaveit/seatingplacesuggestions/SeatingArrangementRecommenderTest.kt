package org.weaveit.seatingplacesuggestions

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class SeatingArrangementRecommenderTest {

    @Test
    fun `can be tested with in-memory stub without infrastructure`() {
        //     1   2   3   4   5
        // A:  1   1   1   1   1
        val seats = listOf(
            SeatingPlace("A", 1, PricingCategory.FIRST, SeatingPlaceAvailability.AVAILABLE),
            SeatingPlace("A", 2, PricingCategory.FIRST, SeatingPlaceAvailability.AVAILABLE),
            SeatingPlace("A", 3, PricingCategory.FIRST, SeatingPlaceAvailability.AVAILABLE),
            SeatingPlace("A", 4, PricingCategory.FIRST, SeatingPlaceAvailability.AVAILABLE),
            SeatingPlace("A", 5, PricingCategory.FIRST, SeatingPlaceAvailability.AVAILABLE)
        )
        val rows = linkedMapOf("A" to Row("A", seats))
        val arrangement = AuditoriumSeatingArrangement(rows)

        val stub = object : AuditoriumSeatingArrangements {
            override fun findByShowId(showId: String) = arrangement
        }
        val recommender = SeatingArrangementRecommender(stub)

        val suggestions = recommender.makeSuggestions("any-show", 2)

        assertThat(suggestions.matchExpectations()).isTrue()
        assertThat(suggestions.seatNames(PricingCategory.FIRST)).containsExactly("A2-A3", "A4-A5")
    }
}
