package org.weaveit.seatingplacesuggestions

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

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
    fun `should suggest seats starting from middle of row`() {
        val partySize = 1

        // Row with 10 seats - middle is between seat 5 and 6
        val a1 = SeatingPlace("A", 1, PricingCategory.SECOND, SeatingPlaceAvailability.AVAILABLE)
        val a2 = SeatingPlace("A", 2, PricingCategory.SECOND, SeatingPlaceAvailability.AVAILABLE)
        val a3 = SeatingPlace("A", 3, PricingCategory.FIRST, SeatingPlaceAvailability.AVAILABLE)
        val a4 = SeatingPlace("A", 4, PricingCategory.FIRST, SeatingPlaceAvailability.RESERVED)
        val a5 = SeatingPlace("A", 5, PricingCategory.FIRST, SeatingPlaceAvailability.AVAILABLE)
        val a6 = SeatingPlace("A", 6, PricingCategory.FIRST, SeatingPlaceAvailability.AVAILABLE)
        val a7 = SeatingPlace("A", 7, PricingCategory.FIRST, SeatingPlaceAvailability.AVAILABLE)
        val a8 = SeatingPlace("A", 8, PricingCategory.FIRST, SeatingPlaceAvailability.RESERVED)
        val a9 = SeatingPlace("A", 9, PricingCategory.SECOND, SeatingPlaceAvailability.AVAILABLE)
        val a10 = SeatingPlace("A", 10, PricingCategory.SECOND, SeatingPlaceAvailability.AVAILABLE)

        val row = Row("A", listOf(a1, a2, a3, a4, a5, a6, a7, a8, a9, a10))

        val seatingOption = row.suggestSeatingOption(partySize, PricingCategory.FIRST)

        // A5 is the seat closest to the middle (between 5 and 6)
        assertThat(seatingOption).isInstanceOf(SeatingOptionIsSuggested::class.java)
        val suggested = seatingOption as SeatingOptionIsSuggested
        assertThat(suggested.seats()).containsExactly(a5)
    }

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
