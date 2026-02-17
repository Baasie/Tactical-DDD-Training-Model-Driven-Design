package org.weaveit.seatingplacesuggestions

interface AuditoriumSeatingArrangements {
    fun findByShowId(showId: String): AuditoriumSeatingArrangement
}
