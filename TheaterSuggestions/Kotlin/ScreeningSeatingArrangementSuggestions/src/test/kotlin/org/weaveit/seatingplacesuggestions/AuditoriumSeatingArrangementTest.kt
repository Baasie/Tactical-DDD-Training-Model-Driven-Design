package org.weaveit.seatingplacesuggestions

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

class AuditoriumSeatingArrangementTest {

    @Nested
    inner class Immutability {

        @Test
        fun `allocate returns new instance and does not mutate original`() {
            val seat = SeatingPlace("A", 1, PricingCategory.FIRST, SeatingPlaceAvailability.AVAILABLE)
            val row = Row("A", listOf(seat))
            val original = AuditoriumSeatingArrangement(mapOf("A" to row))

            val allocated = original.allocate(listOf(seat))

            assertThat(allocated).isNotSameAs(original)
            assertThat(original.rows["A"]!!.seatingPlaces[0].isAvailable()).isTrue()
            assertThat(allocated.rows["A"]!!.seatingPlaces[0].isAvailable()).isFalse()
        }

        @Test
        fun `original map mutation does not affect arrangement`() {
            val seat = SeatingPlace("A", 1, PricingCategory.FIRST, SeatingPlaceAvailability.AVAILABLE)
            val rowA = Row("A", listOf(seat))
            val rowB = Row("B", listOf(seat))
            val mutableMap = mutableMapOf("A" to rowA)
            val arrangement = AuditoriumSeatingArrangement(mutableMap)

            mutableMap["B"] = rowB

            assertThat(arrangement.rows).hasSize(1)
        }

        @Test
        fun `preserves row order`() {
            val seat = SeatingPlace("A", 1, PricingCategory.FIRST, SeatingPlaceAvailability.AVAILABLE)
            val rowA = Row("A", listOf(seat))
            val rowB = Row("B", listOf(seat))
            val rowC = Row("C", listOf(seat))
            val orderedMap = linkedMapOf("A" to rowA, "B" to rowB, "C" to rowC)

            val arrangement = AuditoriumSeatingArrangement(orderedMap)

            assertThat(arrangement.rows.keys).containsExactly("A", "B", "C")
        }
    }

    @Nested
    inner class ValueEquality {

        @Test
        fun `two arrangements with same values are equal`() {
            val seat = SeatingPlace("A", 1, PricingCategory.FIRST, SeatingPlaceAvailability.AVAILABLE)
            val row = Row("A", listOf(seat))
            val arrangement1 = AuditoriumSeatingArrangement(mapOf("A" to row))
            val arrangement2 = AuditoriumSeatingArrangement(mapOf("A" to row))

            assertThat(arrangement1).isEqualTo(arrangement2)
            assertThat(arrangement1.hashCode()).isEqualTo(arrangement2.hashCode())
        }

        @Test
        fun `two arrangements with different rows are not equal`() {
            val seat1 = SeatingPlace("A", 1, PricingCategory.FIRST, SeatingPlaceAvailability.AVAILABLE)
            val seat2 = SeatingPlace("A", 2, PricingCategory.FIRST, SeatingPlaceAvailability.AVAILABLE)
            val row1 = Row("A", listOf(seat1))
            val row2 = Row("A", listOf(seat2))
            val arrangement1 = AuditoriumSeatingArrangement(mapOf("A" to row1))
            val arrangement2 = AuditoriumSeatingArrangement(mapOf("A" to row2))

            assertThat(arrangement1).isNotEqualTo(arrangement2)
        }

        @Test
        fun `arrangement and its allocated version are not equal`() {
            val seat = SeatingPlace("A", 1, PricingCategory.FIRST, SeatingPlaceAvailability.AVAILABLE)
            val row = Row("A", listOf(seat))
            val original = AuditoriumSeatingArrangement(mapOf("A" to row))
            val allocated = original.allocate(listOf(seat))

            assertThat(original).isNotEqualTo(allocated)
        }
    }
}
