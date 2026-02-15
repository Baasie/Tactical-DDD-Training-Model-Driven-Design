package org.weaveit.seatingplacesuggestions;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class RowTest {

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
