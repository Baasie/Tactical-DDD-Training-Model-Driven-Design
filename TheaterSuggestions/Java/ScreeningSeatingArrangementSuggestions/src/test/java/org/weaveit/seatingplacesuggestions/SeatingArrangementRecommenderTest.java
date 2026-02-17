package org.weaveit.seatingplacesuggestions;

import org.junit.jupiter.api.Test;

import java.util.LinkedHashMap;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class SeatingArrangementRecommenderTest {

    @Test
    void can_be_tested_with_in_memory_stub_without_infrastructure() {
        //     1   2   3   4   5
        // A:  1   1   1   1   1
        var seats = List.of(
                new SeatingPlace("A", 1, PricingCategory.FIRST, SeatingPlaceAvailability.AVAILABLE),
                new SeatingPlace("A", 2, PricingCategory.FIRST, SeatingPlaceAvailability.AVAILABLE),
                new SeatingPlace("A", 3, PricingCategory.FIRST, SeatingPlaceAvailability.AVAILABLE),
                new SeatingPlace("A", 4, PricingCategory.FIRST, SeatingPlaceAvailability.AVAILABLE),
                new SeatingPlace("A", 5, PricingCategory.FIRST, SeatingPlaceAvailability.AVAILABLE)
        );
        var rows = new LinkedHashMap<String, Row>();
        rows.put("A", new Row("A", seats));
        var arrangement = new AuditoriumSeatingArrangement(rows);

        var stub = new StubAuditoriumSeatingArrangements(arrangement);
        var recommender = new SeatingArrangementRecommender(stub);

        var suggestions = recommender.makeSuggestions("any-show", 2);

        assertThat(suggestions.matchExpectations()).isTrue();
        assertThat(suggestions.seatNames(PricingCategory.FIRST)).containsExactly("A2-A3", "A4-A5");
    }

    private record StubAuditoriumSeatingArrangements(
            AuditoriumSeatingArrangement arrangement) implements AuditoriumSeatingArrangements {
        @Override
        public AuditoriumSeatingArrangement findByShowId(String showId) {
            return arrangement;
        }
    }
}
