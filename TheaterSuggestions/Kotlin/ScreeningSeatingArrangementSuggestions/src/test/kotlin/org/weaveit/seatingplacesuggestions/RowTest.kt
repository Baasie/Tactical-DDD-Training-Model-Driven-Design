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
    fun `should suggest seats starting from middle of row even`() {
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
    fun `should suggest seats starting from middle of row uneven`() {
        val partySize = 1

        // Row with 11 seats - middle is seat 6
        val a1 = SeatingPlace("A", 1, PricingCategory.SECOND, SeatingPlaceAvailability.AVAILABLE)
        val a2 = SeatingPlace("A", 2, PricingCategory.SECOND, SeatingPlaceAvailability.AVAILABLE)
        val a3 = SeatingPlace("A", 3, PricingCategory.FIRST, SeatingPlaceAvailability.AVAILABLE)
        val a4 = SeatingPlace("A", 4, PricingCategory.FIRST, SeatingPlaceAvailability.RESERVED)
        val a5 = SeatingPlace("A", 5, PricingCategory.FIRST, SeatingPlaceAvailability.AVAILABLE)
        val a6 = SeatingPlace("A", 6, PricingCategory.FIRST, SeatingPlaceAvailability.AVAILABLE)
        val a7 = SeatingPlace("A", 7, PricingCategory.FIRST, SeatingPlaceAvailability.AVAILABLE)
        val a8 = SeatingPlace("A", 8, PricingCategory.FIRST, SeatingPlaceAvailability.AVAILABLE)
        val a9 = SeatingPlace("A", 9, PricingCategory.FIRST, SeatingPlaceAvailability.RESERVED)
        val a10 = SeatingPlace("A", 10, PricingCategory.SECOND, SeatingPlaceAvailability.AVAILABLE)
        val a11 = SeatingPlace("A", 11, PricingCategory.SECOND, SeatingPlaceAvailability.AVAILABLE)

        val row = Row("A", listOf(a1, a2, a3, a4, a5, a6, a7, a8, a9, a10, a11))

        val seatingOption = row.suggestSeatingOption(partySize, PricingCategory.FIRST)

        // A6 is the exact middle seat (seat 6 of 11)
        assertThat(seatingOption).isInstanceOf(SeatingOptionIsSuggested::class.java)
        val suggested = seatingOption as SeatingOptionIsSuggested
        assertThat(suggested.seats()).containsExactly(a6)
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
    fun `offer adjacent seats nearer the middle of the row when the middle is not reserved`() {
        val partySize = 3

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
        val rowSize = row.seatingPlaces.size

        val availableSeatsInCategory = row.seatingPlaces
            .filter { it.isAvailable() }
            .filter { it.matchCategory(PricingCategory.FIRST) }

        val adjacentSeats = offerAdjacentSeats(availableSeatsInCategory, partySize, rowSize)

        assertThat(adjacentSeats).containsExactly(a5, a6, a7)
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
    fun `offer adjacent seats closest to the middle when multiple options exist`() {
        val partySize = 3

        val a1 = SeatingPlace("A", 1, PricingCategory.FIRST, SeatingPlaceAvailability.AVAILABLE)
        val a2 = SeatingPlace("A", 2, PricingCategory.FIRST, SeatingPlaceAvailability.AVAILABLE)
        val a3 = SeatingPlace("A", 3, PricingCategory.FIRST, SeatingPlaceAvailability.AVAILABLE)
        val a4 = SeatingPlace("A", 4, PricingCategory.FIRST, SeatingPlaceAvailability.AVAILABLE)
        val a5 = SeatingPlace("A", 5, PricingCategory.FIRST, SeatingPlaceAvailability.AVAILABLE)
        val a6 = SeatingPlace("A", 6, PricingCategory.FIRST, SeatingPlaceAvailability.AVAILABLE)
        val a7 = SeatingPlace("A", 7, PricingCategory.FIRST, SeatingPlaceAvailability.AVAILABLE)
        val a8 = SeatingPlace("A", 8, PricingCategory.FIRST, SeatingPlaceAvailability.AVAILABLE)
        val a9 = SeatingPlace("A", 9, PricingCategory.FIRST, SeatingPlaceAvailability.AVAILABLE)
        val a10 = SeatingPlace("A", 10, PricingCategory.FIRST, SeatingPlaceAvailability.AVAILABLE)

        val row = Row("A", listOf(a1, a2, a3, a4, a5, a6, a7, a8, a9, a10))
        val rowSize = row.seatingPlaces.size

        val availableSeatsInCategory = row.seatingPlaces
            .filter { it.isAvailable() }
            .filter { it.matchCategory(PricingCategory.FIRST) }

        val adjacentSeats = offerAdjacentSeats(availableSeatsInCategory, partySize, rowSize)

        assertThat(adjacentSeats).containsExactly(a4, a5, a6)
    }

    // Deep Modeling: probing the code should start with a prototype.
    private fun offerAdjacentSeats(availableSeatsInCategory: List<SeatingPlace>, partySize: Int, rowSize: Int): List<SeatingPlace> {
        // Implement your prototype here
        return emptyList()
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
