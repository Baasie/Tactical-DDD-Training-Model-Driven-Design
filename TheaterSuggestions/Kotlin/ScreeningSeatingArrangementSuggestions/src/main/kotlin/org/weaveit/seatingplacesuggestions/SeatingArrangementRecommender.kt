package org.weaveit.seatingplacesuggestions

class SeatingArrangementRecommender(
    private val auditoriumSeatingArrangements: AuditoriumSeatingArrangements
) {
    companion object {
        private const val NUMBER_OF_SUGGESTIONS = 3
    }

    fun makeSuggestions(showId: String, partyRequested: Int): SuggestionsAreMade {
        val auditoriumSeating = auditoriumSeatingArrangements.findByShowId(showId)

        val suggestionsMade = SuggestionsAreMade(showId, partyRequested)

        suggestionsMade.add(giveMeSuggestionsFor(auditoriumSeating, partyRequested, PricingCategory.FIRST))
        suggestionsMade.add(giveMeSuggestionsFor(auditoriumSeating, partyRequested, PricingCategory.SECOND))
        suggestionsMade.add(giveMeSuggestionsFor(auditoriumSeating, partyRequested, PricingCategory.THIRD))
        suggestionsMade.add(giveMeSuggestionsFor(auditoriumSeating, partyRequested, PricingCategory.MIXED))

        if (suggestionsMade.matchExpectations()) {
            return suggestionsMade
        }

        return SuggestionsAreNotAvailable(showId, partyRequested)
    }

    private fun giveMeSuggestionsFor(
        auditoriumSeatingArrangement: AuditoriumSeatingArrangement,
        partyRequested: Int,
        pricingCategory: PricingCategory
    ): List<SuggestionIsMade> {
        val foundedSuggestions = mutableListOf<SuggestionIsMade>()
        var currentArrangement = auditoriumSeatingArrangement

        for (i in 0 until NUMBER_OF_SUGGESTIONS) {
            val seatingOptionSuggested = currentArrangement.suggestSeatingOptionFor(partyRequested, pricingCategory)

            if (seatingOptionSuggested.matchExpectation()) {
                currentArrangement = currentArrangement.allocate(seatingOptionSuggested.seats())
                foundedSuggestions.add(SuggestionIsMade(seatingOptionSuggested))
            }
        }

        return foundedSuggestions
    }
}
