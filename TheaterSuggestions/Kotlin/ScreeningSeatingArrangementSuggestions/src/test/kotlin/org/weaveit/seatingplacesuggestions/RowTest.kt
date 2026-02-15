package org.weaveit.seatingplacesuggestions

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import kotlin.math.abs

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
    fun `should offer seats starting from middle of row`() {
        val partySize = 2

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

        // Get seats ordered by distance from middle, then take first 'partySize' seats
        val seatingPlaces = offerSeatsNearerTheMiddleOfTheRow(row).take(partySize)

        // A5 and A6 are the two seats closest to the middle (between 5 and 6)
        assertThat(seatingPlaces).containsExactly(a5, a6)
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
    private fun offerSeatsNearerTheMiddleOfTheRow(row: Row): List<SeatingPlace> {
        val rowSize = row.seatingPlaces.size
        val middleOfTheRow = (rowSize + 1) / 2.0

        return row.seatingPlaces
            .filter { it.isAvailable() }
            .sortedWith(
                compareBy<SeatingPlace> { distanceFromMiddleOfTheRow(it, middleOfTheRow) }
                    .thenBy { it.number }
            )
    }

    private fun distanceFromMiddleOfTheRow(seat: SeatingPlace, middleOfTheRow: Double): Double {
        return abs(seat.number - middleOfTheRow)
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
