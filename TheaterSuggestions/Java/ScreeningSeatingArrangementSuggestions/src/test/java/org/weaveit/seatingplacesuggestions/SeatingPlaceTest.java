package org.weaveit.seatingplacesuggestions;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class SeatingPlaceTest {

    @Nested
    class Immutability {

        @Test
        void allocate_returns_new_instance_and_does_not_mutate_original() {
            var original = new SeatingPlace("A", 1, PricingCategory.FIRST, SeatingPlaceAvailability.AVAILABLE);

            var allocated = original.allocate();

            assertThat(allocated).isNotSameAs(original);
            assertThat(original.isAvailable()).isTrue();
            assertThat(allocated.isAvailable()).isFalse();
        }

        @Test
        void allocate_returns_same_instance_when_already_allocated() {
            var alreadyAllocated = new SeatingPlace("A", 1, PricingCategory.FIRST, SeatingPlaceAvailability.ALLOCATED);

            var result = alreadyAllocated.allocate();

            assertThat(result).isSameAs(alreadyAllocated);
        }
    }

    @Nested
    class ValueEquality {

        @Test
        void two_seating_places_with_same_values_are_equal() {
            var seat1 = new SeatingPlace("A", 1, PricingCategory.FIRST, SeatingPlaceAvailability.AVAILABLE);
            var seat2 = new SeatingPlace("A", 1, PricingCategory.FIRST, SeatingPlaceAvailability.AVAILABLE);

            assertThat(seat1).isEqualTo(seat2);
            assertThat(seat1.hashCode()).isEqualTo(seat2.hashCode());
        }

        @Test
        void two_seating_places_with_different_values_are_not_equal() {
            var seat1 = new SeatingPlace("A", 1, PricingCategory.FIRST, SeatingPlaceAvailability.AVAILABLE);
            var seat2 = new SeatingPlace("A", 2, PricingCategory.FIRST, SeatingPlaceAvailability.AVAILABLE);

            assertThat(seat1).isNotEqualTo(seat2);
        }

        @Test
        void seating_place_and_its_allocated_version_are_not_equal() {
            var available = new SeatingPlace("A", 1, PricingCategory.FIRST, SeatingPlaceAvailability.AVAILABLE);
            var allocated = available.allocate();

            assertThat(available).isNotEqualTo(allocated);
        }
    }
}
