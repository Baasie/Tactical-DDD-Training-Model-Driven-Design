package org.weaveit.seatingplacesuggestions

class SeatingArrangementRecommender(
    private val auditoriumSeatingArrangements: AuditoriumSeatingArrangements
) {
    fun makeSuggestions(showId: String, partyRequested: Int): SuggestionsAreMade {
        val rows = auditoriumSeatingArrangements.findByShowId(showId)
        val suggestionsAreMade = SuggestionsAreMade(showId, partyRequested)

        for (row in rows.values) {
            val seatsFound = mutableListOf<SeatingPlace>()

            for (seat in row.seatingPlaces()) {
                if (seat.isAvailable() && seat.matchCategory(PricingCategory.FIRST)) {
                    seatsFound.add(seat)

                    if (seatsFound.size == partyRequested) {
                        suggestionsAreMade.addSeats(PricingCategory.FIRST, seatsFound.map { it.name() })
                        return suggestionsAreMade
                    }
                }
            }
        }

        return suggestionsAreMade
    }
}
