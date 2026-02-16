package org.weaveit.seatingplacesuggestions

import kotlin.math.abs

/**
 * Value Object representing a contiguous group of seats that can be offered together.
 *
 * Domain concept: "Adjacent seating" — a party should sit together in consecutive seats,
 * and the group closest to the center of the row is preferred.
 */
data class AdjacentSeats(
    val seats: List<SeatingPlace>,
    val distanceFromRowCenter: Double
) : Comparable<AdjacentSeats> {

    companion object {
        fun of(window: List<SeatingPlace>, rowSize: Int): AdjacentSeats {
            val windowCenter = (window.first().number + window.last().number) / 2.0
            val rowCenter = (rowSize + 1) / 2.0
            return AdjacentSeats(window.toList(), abs(windowCenter - rowCenter))
        }
    }

    override fun compareTo(other: AdjacentSeats): Int {
        val distanceCompare = this.distanceFromRowCenter.compareTo(other.distanceFromRowCenter)
        if (distanceCompare != 0) return distanceCompare
        return this.seats.first().number.compareTo(other.seats.first().number)
    }
}
