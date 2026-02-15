package org.weaveit.seatingplacesuggestions

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

class SeatingPlaceTest {

    @Nested
    inner class Immutability {

        @Test
        fun `allocate returns new instance and does not mutate original`() {
            val original = SeatingPlace("A", 1, PricingCategory.FIRST, SeatingPlaceAvailability.AVAILABLE)

            val allocated = original.allocate()

            assertThat(allocated).isNotSameAs(original)
            assertThat(original.isAvailable()).isTrue()
            assertThat(allocated.isAvailable()).isFalse()
        }

        @Test
        fun `allocate returns same instance when already allocated`() {
            val alreadyAllocated = SeatingPlace("A", 1, PricingCategory.FIRST, SeatingPlaceAvailability.ALLOCATED)

            val result = alreadyAllocated.allocate()

            assertThat(result).isSameAs(alreadyAllocated)
        }
    }

    @Nested
    inner class ValueEquality {

        @Test
        fun `two seating places with same values are equal`() {
            val seat1 = SeatingPlace("A", 1, PricingCategory.FIRST, SeatingPlaceAvailability.AVAILABLE)
            val seat2 = SeatingPlace("A", 1, PricingCategory.FIRST, SeatingPlaceAvailability.AVAILABLE)

            assertThat(seat1).isEqualTo(seat2)
            assertThat(seat1.hashCode()).isEqualTo(seat2.hashCode())
        }

        @Test
        fun `two seating places with different values are not equal`() {
            val seat1 = SeatingPlace("A", 1, PricingCategory.FIRST, SeatingPlaceAvailability.AVAILABLE)
            val seat2 = SeatingPlace("A", 2, PricingCategory.FIRST, SeatingPlaceAvailability.AVAILABLE)

            assertThat(seat1).isNotEqualTo(seat2)
        }

        @Test
        fun `seating place and its allocated version are not equal`() {
            val available = SeatingPlace("A", 1, PricingCategory.FIRST, SeatingPlaceAvailability.AVAILABLE)
            val allocated = available.allocate()

            assertThat(available).isNotEqualTo(allocated)
        }
    }
}
