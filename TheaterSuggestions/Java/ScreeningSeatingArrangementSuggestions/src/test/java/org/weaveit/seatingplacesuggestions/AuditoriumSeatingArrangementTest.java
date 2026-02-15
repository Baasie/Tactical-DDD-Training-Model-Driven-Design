package org.weaveit.seatingplacesuggestions;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class AuditoriumSeatingArrangementTest {

    @Nested
    class Immutability {

        @Test
        void allocate_returns_new_instance_and_does_not_mutate_original() {
            var seat = new SeatingPlace("A", 1, PricingCategory.FIRST, SeatingPlaceAvailability.AVAILABLE);
            var row = new Row("A", List.of(seat));
            var original = new AuditoriumSeatingArrangement(Map.of("A", row));

            var allocated = original.allocate(List.of(seat));

            assertThat(allocated).isNotSameAs(original);
            assertThat(original.rows().get("A").seatingPlaces().get(0).isAvailable()).isTrue();
            assertThat(allocated.rows().get("A").seatingPlaces().get(0).isAvailable()).isFalse();
        }

        @Test
        void internal_map_cannot_be_mutated_from_outside() {
            var seat = new SeatingPlace("A", 1, PricingCategory.FIRST, SeatingPlaceAvailability.AVAILABLE);
            var row = new Row("A", List.of(seat));
            var arrangement = new AuditoriumSeatingArrangement(Map.of("A", row));

            assertThatThrownBy(() -> arrangement.rows().put("B", row))
                    .isInstanceOf(UnsupportedOperationException.class);
        }

        @Test
        void original_map_mutation_does_not_affect_arrangement() {
            var seat = new SeatingPlace("A", 1, PricingCategory.FIRST, SeatingPlaceAvailability.AVAILABLE);
            var rowA = new Row("A", List.of(seat));
            var rowB = new Row("B", List.of(seat));
            var mutableMap = new HashMap<String, Row>();
            mutableMap.put("A", rowA);
            var arrangement = new AuditoriumSeatingArrangement(mutableMap);

            mutableMap.put("B", rowB);

            assertThat(arrangement.rows()).hasSize(1);
        }

        @Test
        void preserves_row_order() {
            var seat = new SeatingPlace("A", 1, PricingCategory.FIRST, SeatingPlaceAvailability.AVAILABLE);
            var rowA = new Row("A", List.of(seat));
            var rowB = new Row("B", List.of(seat));
            var rowC = new Row("C", List.of(seat));
            var orderedMap = new LinkedHashMap<String, Row>();
            orderedMap.put("A", rowA);
            orderedMap.put("B", rowB);
            orderedMap.put("C", rowC);

            var arrangement = new AuditoriumSeatingArrangement(orderedMap);

            assertThat(arrangement.rows().keySet()).containsExactly("A", "B", "C");
        }
    }

    @Nested
    class ValueEquality {

        @Test
        void two_arrangements_with_same_values_are_equal() {
            var seat = new SeatingPlace("A", 1, PricingCategory.FIRST, SeatingPlaceAvailability.AVAILABLE);
            var row = new Row("A", List.of(seat));
            var arrangement1 = new AuditoriumSeatingArrangement(Map.of("A", row));
            var arrangement2 = new AuditoriumSeatingArrangement(Map.of("A", row));

            assertThat(arrangement1).isEqualTo(arrangement2);
            assertThat(arrangement1.hashCode()).isEqualTo(arrangement2.hashCode());
        }

        @Test
        void two_arrangements_with_different_rows_are_not_equal() {
            var seat1 = new SeatingPlace("A", 1, PricingCategory.FIRST, SeatingPlaceAvailability.AVAILABLE);
            var seat2 = new SeatingPlace("A", 2, PricingCategory.FIRST, SeatingPlaceAvailability.AVAILABLE);
            var row1 = new Row("A", List.of(seat1));
            var row2 = new Row("A", List.of(seat2));
            var arrangement1 = new AuditoriumSeatingArrangement(Map.of("A", row1));
            var arrangement2 = new AuditoriumSeatingArrangement(Map.of("A", row2));

            assertThat(arrangement1).isNotEqualTo(arrangement2);
        }

        @Test
        void arrangement_and_its_allocated_version_are_not_equal() {
            var seat = new SeatingPlace("A", 1, PricingCategory.FIRST, SeatingPlaceAvailability.AVAILABLE);
            var row = new Row("A", List.of(seat));
            var original = new AuditoriumSeatingArrangement(Map.of("A", row));
            var allocated = original.allocate(List.of(seat));

            assertThat(original).isNotEqualTo(allocated);
        }
    }
}
