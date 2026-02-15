package org.weaveit.seatingplacesuggestions;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class RowTest {

    /**
     * Deep Modeling: Before changing the domain model, prototype the new behavior
     * in isolation to understand the algorithm and validate the approach.
     *
     * New Rule: Seats should be suggested starting from the middle of the row,
     * working outward. For a row with 10 seats, the middle is between seats 5 and 6.
     * Seats closer to the middle are preferred.
     *
     * Row layout for this test:
     *      1   2   3   4   5   6   7   8   9  10
     *  A:  2   2   1  (1)  1   1   1  (1)  2   2
     *
     * Available FIRST category seats: A3, A5, A6, A7
     * Middle of row: between 5 and 6
     * Expected order from middle outward: A5, A6, A7, A3
     * For party of 2: A5, A6 (the two seats closest to middle)
     */
    @Test
    public void should_offer_seats_starting_from_middle_of_row() {
        int partySize = 2;

        // Row with 10 seats - middle is between seat 5 and 6
        SeatingPlace a1 = new SeatingPlace("A", 1, PricingCategory.SECOND, SeatingPlaceAvailability.AVAILABLE);
        SeatingPlace a2 = new SeatingPlace("A", 2, PricingCategory.SECOND, SeatingPlaceAvailability.AVAILABLE);
        SeatingPlace a3 = new SeatingPlace("A", 3, PricingCategory.FIRST, SeatingPlaceAvailability.AVAILABLE);
        SeatingPlace a4 = new SeatingPlace("A", 4, PricingCategory.FIRST, SeatingPlaceAvailability.RESERVED);
        SeatingPlace a5 = new SeatingPlace("A", 5, PricingCategory.FIRST, SeatingPlaceAvailability.AVAILABLE);
        SeatingPlace a6 = new SeatingPlace("A", 6, PricingCategory.FIRST, SeatingPlaceAvailability.AVAILABLE);
        SeatingPlace a7 = new SeatingPlace("A", 7, PricingCategory.FIRST, SeatingPlaceAvailability.AVAILABLE);
        SeatingPlace a8 = new SeatingPlace("A", 8, PricingCategory.FIRST, SeatingPlaceAvailability.RESERVED);
        SeatingPlace a9 = new SeatingPlace("A", 9, PricingCategory.SECOND, SeatingPlaceAvailability.AVAILABLE);
        SeatingPlace a10 = new SeatingPlace("A", 10, PricingCategory.SECOND, SeatingPlaceAvailability.AVAILABLE);

        Row row = new Row("A", Arrays.asList(a1, a2, a3, a4, a5, a6, a7, a8, a9, a10));

        // Get seats ordered by distance from middle, then take first 'partySize' seats
        List<SeatingPlace> seatingPlaces = offerSeatsNearerTheMiddleOfTheRow(row).stream()
                .limit(partySize)
                .collect(Collectors.toList());

        // A5 and A6 are the two seats closest to the middle (between 5 and 6)
        assertThat(seatingPlaces).containsExactly(a5, a6);
    }

    /**
     * Deep Modeling: Prototype the algorithm here before integrating into Row.
     * This method should return all available seats ordered by distance from the middle.
     *
     * Once the algorithm works, consider:
     * - Should this become a method on Row?
     * - Should Row have a different internal representation?
     * - Is there a new concept trying to emerge (e.g., "MiddleOutSeatingStrategy")?
     */
    public List<SeatingPlace> offerSeatsNearerTheMiddleOfTheRow(Row row) {
        // TODO: Implement your logic here
        // Hint: Calculate distance of each seat from the middle of the row
        // For a row of size N, middle = (N + 1) / 2.0
        // Sort seats by their distance from middle (ascending)
        return new ArrayList<>();
    }

    @Nested
    class Immutability {

        @Test
        void allocate_returns_new_instance_and_does_not_mutate_original() {
            var seat1 = new SeatingPlace("A", 1, PricingCategory.FIRST, SeatingPlaceAvailability.AVAILABLE);
            var seat2 = new SeatingPlace("A", 2, PricingCategory.FIRST, SeatingPlaceAvailability.AVAILABLE);
            var original = new Row("A", List.of(seat1, seat2));

            var allocated = original.allocate(List.of(seat1));

            assertThat(allocated).isNotSameAs(original);
            assertThat(original.seatingPlaces().get(0).isAvailable()).isTrue();
            assertThat(allocated.seatingPlaces().get(0).isAvailable()).isFalse();
        }

        @Test
        void internal_list_cannot_be_mutated_from_outside() {
            var seat = new SeatingPlace("A", 1, PricingCategory.FIRST, SeatingPlaceAvailability.AVAILABLE);
            var row = new Row("A", List.of(seat));

            assertThatThrownBy(() -> row.seatingPlaces().add(seat))
                    .isInstanceOf(UnsupportedOperationException.class);
        }

        @Test
        void original_list_mutation_does_not_affect_row() {
            var seat1 = new SeatingPlace("A", 1, PricingCategory.FIRST, SeatingPlaceAvailability.AVAILABLE);
            var seat2 = new SeatingPlace("A", 2, PricingCategory.FIRST, SeatingPlaceAvailability.AVAILABLE);
            var mutableList = new ArrayList<>(List.of(seat1));
            var row = new Row("A", mutableList);

            mutableList.add(seat2);

            assertThat(row.seatingPlaces()).hasSize(1);
        }
    }

    @Nested
    class ValueEquality {

        @Test
        void two_rows_with_same_values_are_equal() {
            var seat = new SeatingPlace("A", 1, PricingCategory.FIRST, SeatingPlaceAvailability.AVAILABLE);
            var row1 = new Row("A", List.of(seat));
            var row2 = new Row("A", List.of(seat));

            assertThat(row1).isEqualTo(row2);
            assertThat(row1.hashCode()).isEqualTo(row2.hashCode());
        }

        @Test
        void two_rows_with_different_seats_are_not_equal() {
            var seat1 = new SeatingPlace("A", 1, PricingCategory.FIRST, SeatingPlaceAvailability.AVAILABLE);
            var seat2 = new SeatingPlace("A", 2, PricingCategory.FIRST, SeatingPlaceAvailability.AVAILABLE);
            var row1 = new Row("A", List.of(seat1));
            var row2 = new Row("A", List.of(seat2));

            assertThat(row1).isNotEqualTo(row2);
        }

        @Test
        void row_and_its_allocated_version_are_not_equal() {
            var seat = new SeatingPlace("A", 1, PricingCategory.FIRST, SeatingPlaceAvailability.AVAILABLE);
            var original = new Row("A", List.of(seat));
            var allocated = original.allocate(List.of(seat));

            assertThat(original).isNotEqualTo(allocated);
        }
    }
}
