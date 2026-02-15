package org.weaveit.seatingplacesuggestions

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

class RowTest {

    @Nested
    inner class Immutability {

        @Test
        fun `allocate returns new instance and does not mutate original`() {
            val seat1 = SeatingPlace("A", 1, PricingCategory.FIRST, SeatingPlaceAvailability.AVAILABLE)
            val seat2 = SeatingPlace("A", 2, PricingCategory.FIRST, SeatingPlaceAvailability.AVAILABLE)
            val original = Row("A", listOf(seat1, seat2))

            val allocated = original.allocate(listOf(seat1))

            assertThat(allocated).isNotSameAs(original)
            assertThat(original.seatingPlaces[0].isAvailable()).isTrue()
            assertThat(allocated.seatingPlaces[0].isAvailable()).isFalse()
        }

        @Test
        fun `original list mutation does not affect row`() {
            val seat1 = SeatingPlace("A", 1, PricingCategory.FIRST, SeatingPlaceAvailability.AVAILABLE)
            val seat2 = SeatingPlace("A", 2, PricingCategory.FIRST, SeatingPlaceAvailability.AVAILABLE)
            val mutableList = mutableListOf(seat1)
            val row = Row("A", mutableList)

            mutableList.add(seat2)

            assertThat(row.seatingPlaces).hasSize(1)
        }
    }

    @Nested
    inner class ValueEquality {

        @Test
        fun `two rows with same values are equal`() {
            val seat = SeatingPlace("A", 1, PricingCategory.FIRST, SeatingPlaceAvailability.AVAILABLE)
            val row1 = Row("A", listOf(seat))
            val row2 = Row("A", listOf(seat))

            assertThat(row1).isEqualTo(row2)
            assertThat(row1.hashCode()).isEqualTo(row2.hashCode())
        }

        @Test
        fun `two rows with different seats are not equal`() {
            val seat1 = SeatingPlace("A", 1, PricingCategory.FIRST, SeatingPlaceAvailability.AVAILABLE)
            val seat2 = SeatingPlace("A", 2, PricingCategory.FIRST, SeatingPlaceAvailability.AVAILABLE)
            val row1 = Row("A", listOf(seat1))
            val row2 = Row("A", listOf(seat2))

            assertThat(row1).isNotEqualTo(row2)
        }

        @Test
        fun `row and its allocated version are not equal`() {
            val seat = SeatingPlace("A", 1, PricingCategory.FIRST, SeatingPlaceAvailability.AVAILABLE)
            val original = Row("A", listOf(seat))
            val allocated = original.allocate(listOf(seat))

            assertThat(original).isNotEqualTo(allocated)
        }
    }
}
