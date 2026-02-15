package org.weaveit.seatingplacesuggestions

import kotlin.math.abs

/**
 * Value Object representing how far a seating place is from the center of its row.
 *
 * Domain concept: "Seats closer to the center provide a better viewing experience"
 * Lower distance = closer to center = more desirable
 */
data class DistanceFromRowCenter(val value: Int) : Comparable<DistanceFromRowCenter> {

    companion object {
        fun of(seatNumber: Int, rowSize: Int): DistanceFromRowCenter {
            val center = rowSize / 2

            return if (rowSize % 2 == 0) {
                // Even row: center is between two seats (e.g., row of 10 → center between 5 and 6)
                if (seatNumber > center) {
                    DistanceFromRowCenter(abs(seatNumber - center))
                } else {
                    DistanceFromRowCenter(abs(seatNumber - (center + 1)))
                }
            } else {
                // Odd row: center is a single seat
                DistanceFromRowCenter(abs(seatNumber - (center + 1)))
            }
        }
    }

    override fun compareTo(other: DistanceFromRowCenter): Int {
        return value.compareTo(other.value)
    }
}
