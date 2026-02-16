package org.weaveit.seatingplacesuggestions;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class RowTest {

    /**
     * Seats should be suggested starting from the middle of the row, working outward.
     * For a row with 10 seats, the middle is between seats 5 and 6.
     * Seats closer to the middle are preferred.
     *
     * Row layout for this test:
     *      1   2   3   4   5   6   7   8   9  10
     *  A:  2   2   1  (1)  1   1   1  (1)  2   2
     *
     * Available FIRST category seats: A3, A5, A6, A7
     * Middle of row: between 5 and 6
     * For party of 1: A5 (the seat closest to middle)
     */
    @Test
    public void should_suggest_seats_starting_from_middle_of_row_even() {
        int partySize = 1;

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

        SeatingOption seatingOption = row.suggestSeatingOption(partySize, PricingCategory.FIRST);

        // A5 is the seat closest to the middle (between 5 and 6)
        assertThat(seatingOption).isInstanceOf(SeatingOptionIsSuggested.class);
        var suggested = (SeatingOptionIsSuggested) seatingOption;
        assertThat(suggested.seats()).containsExactly(a5);
    }

    /**
     * For a row with 11 seats (odd), the middle is exactly seat 6.
     *
     * Row layout for this test:
     *      1   2   3   4   5   6   7   8   9  10  11
     *  A:  2   2   1  (1)  1   1   1   1  (1)  2   2
     *
     * Available FIRST category seats: A3, A5, A6, A7, A8
     * Middle of row: seat 6
     * For party of 1: A6 (the exact middle seat)
     */
    @Test
    public void should_suggest_seats_starting_from_middle_of_row_uneven() {
        int partySize = 1;

        // Row with 11 seats - middle is seat 6
        SeatingPlace a1 = new SeatingPlace("A", 1, PricingCategory.SECOND, SeatingPlaceAvailability.AVAILABLE);
        SeatingPlace a2 = new SeatingPlace("A", 2, PricingCategory.SECOND, SeatingPlaceAvailability.AVAILABLE);
        SeatingPlace a3 = new SeatingPlace("A", 3, PricingCategory.FIRST, SeatingPlaceAvailability.AVAILABLE);
        SeatingPlace a4 = new SeatingPlace("A", 4, PricingCategory.FIRST, SeatingPlaceAvailability.RESERVED);
        SeatingPlace a5 = new SeatingPlace("A", 5, PricingCategory.FIRST, SeatingPlaceAvailability.AVAILABLE);
        SeatingPlace a6 = new SeatingPlace("A", 6, PricingCategory.FIRST, SeatingPlaceAvailability.AVAILABLE);
        SeatingPlace a7 = new SeatingPlace("A", 7, PricingCategory.FIRST, SeatingPlaceAvailability.AVAILABLE);
        SeatingPlace a8 = new SeatingPlace("A", 8, PricingCategory.FIRST, SeatingPlaceAvailability.AVAILABLE);
        SeatingPlace a9 = new SeatingPlace("A", 9, PricingCategory.FIRST, SeatingPlaceAvailability.RESERVED);
        SeatingPlace a10 = new SeatingPlace("A", 10, PricingCategory.SECOND, SeatingPlaceAvailability.AVAILABLE);
        SeatingPlace a11 = new SeatingPlace("A", 11, PricingCategory.SECOND, SeatingPlaceAvailability.AVAILABLE);

        Row row = new Row("A", Arrays.asList(a1, a2, a3, a4, a5, a6, a7, a8, a9, a10, a11));

        SeatingOption seatingOption = row.suggestSeatingOption(partySize, PricingCategory.FIRST);

        // A6 is the exact middle seat (seat 6 of 11)
        assertThat(seatingOption).isInstanceOf(SeatingOptionIsSuggested.class);
        var suggested = (SeatingOptionIsSuggested) seatingOption;
        assertThat(suggested.seats()).containsExactly(a6);
    }

    /**
     * Adjacent seating: when a party of 3 requests FIRST category seats,
     * we should offer 3 contiguous available seats nearest to the middle.
     *
     * Row layout for this test:
     *      1   2   3   4   5   6   7   8   9  10
     *  A:  2   2   1  (1)  1   1   1  (1)  2   2
     *
     * Available FIRST category seats: A3, A5, A6, A7
     * Contiguous blocks: [A3], [A5, A6, A7]
     * Only [A5, A6, A7] has 3 adjacent seats -> that's the suggestion
     */
    @Test
    public void offer_adjacent_seats_nearer_the_middle_of_the_row_when_the_middle_is_not_reserved() {
        int partySize = 3;

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

        SeatingOption seatingOption = row.suggestSeatingOption(partySize, PricingCategory.FIRST);

        assertThat(seatingOption).isInstanceOf(SeatingOptionIsSuggested.class);
        var suggested = (SeatingOptionIsSuggested) seatingOption;
        assertThat(suggested.seats()).containsExactly(a5, a6, a7);
    }

    /**
     * When multiple contiguous windows of the requested size exist,
     * the window closest to the middle of the row should be selected.
     *
     * Row layout for this test:
     *      1   2   3   4   5   6   7   8   9  10
     *  A:  1   1   1   1   1   1   1   1   1   1
     *
     * All 10 seats are FIRST and available.
     * For party of 3, possible windows: [1,2,3], [2,3,4], ..., [8,9,10]
     * Window [4,5,6] is closest to middle (center of window = seat 5, distance 1)
     */
    @Test
    public void offer_adjacent_seats_closest_to_the_middle_when_multiple_options_exist() {
        int partySize = 3;

        SeatingPlace a1 = new SeatingPlace("A", 1, PricingCategory.FIRST, SeatingPlaceAvailability.AVAILABLE);
        SeatingPlace a2 = new SeatingPlace("A", 2, PricingCategory.FIRST, SeatingPlaceAvailability.AVAILABLE);
        SeatingPlace a3 = new SeatingPlace("A", 3, PricingCategory.FIRST, SeatingPlaceAvailability.AVAILABLE);
        SeatingPlace a4 = new SeatingPlace("A", 4, PricingCategory.FIRST, SeatingPlaceAvailability.AVAILABLE);
        SeatingPlace a5 = new SeatingPlace("A", 5, PricingCategory.FIRST, SeatingPlaceAvailability.AVAILABLE);
        SeatingPlace a6 = new SeatingPlace("A", 6, PricingCategory.FIRST, SeatingPlaceAvailability.AVAILABLE);
        SeatingPlace a7 = new SeatingPlace("A", 7, PricingCategory.FIRST, SeatingPlaceAvailability.AVAILABLE);
        SeatingPlace a8 = new SeatingPlace("A", 8, PricingCategory.FIRST, SeatingPlaceAvailability.AVAILABLE);
        SeatingPlace a9 = new SeatingPlace("A", 9, PricingCategory.FIRST, SeatingPlaceAvailability.AVAILABLE);
        SeatingPlace a10 = new SeatingPlace("A", 10, PricingCategory.FIRST, SeatingPlaceAvailability.AVAILABLE);

        Row row = new Row("A", Arrays.asList(a1, a2, a3, a4, a5, a6, a7, a8, a9, a10));

        SeatingOption seatingOption = row.suggestSeatingOption(partySize, PricingCategory.FIRST);

        assertThat(seatingOption).isInstanceOf(SeatingOptionIsSuggested.class);
        var suggested = (SeatingOptionIsSuggested) seatingOption;
        assertThat(suggested.seats()).containsExactly(a4, a5, a6);
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
