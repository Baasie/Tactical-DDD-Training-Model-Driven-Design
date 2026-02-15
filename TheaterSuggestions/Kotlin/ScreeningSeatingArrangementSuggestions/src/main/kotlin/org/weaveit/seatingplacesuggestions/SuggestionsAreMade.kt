package org.weaveit.seatingplacesuggestions

open class SuggestionsAreMade(
    val showId: String,
    val partyRequested: Int
) {
    private val forCategory: MutableMap<PricingCategory, MutableList<SuggestionIsMade>> = mutableMapOf()

    init {
        instantiateAnEmptyListForEveryPricingCategory()
    }

    fun seatNames(pricingCategory: PricingCategory): List<String> {
        return forCategory[pricingCategory]!!
            .flatMap { it.seatNames() }
    }

    private fun instantiateAnEmptyListForEveryPricingCategory() {
        for (pricingCategory in PricingCategory.entries) {
            forCategory[pricingCategory] = mutableListOf()
        }
    }

    fun add(suggestions: Iterable<SuggestionIsMade>) {
        suggestions.forEach { suggestionIsMade ->
            forCategory[suggestionIsMade.pricingCategory]!!.add(suggestionIsMade)
        }
    }

    fun matchExpectations(): Boolean {
        return forCategory.values
            .flatten()
            .any { it.matchExpectation() }
    }
}
