package org.weaveit.seatingplacesuggestions

open class SuggestionsAreMade(
    val showId: String,
    val partyRequested: Int,
    suggestions: List<SuggestionIsMade>
) {
    private val forCategory: Map<PricingCategory, List<SuggestionIsMade>> =
        organizeSuggestionsByCategory(suggestions)

    private fun organizeSuggestionsByCategory(
        suggestions: List<SuggestionIsMade>
    ): Map<PricingCategory, List<SuggestionIsMade>> {
        val result = mutableMapOf<PricingCategory, MutableList<SuggestionIsMade>>()

        for (category in PricingCategory.entries) {
            result[category] = mutableListOf()
        }

        for (suggestion in suggestions) {
            result[suggestion.pricingCategory]!!.add(suggestion)
        }

        return result.mapValues { it.value.toList() }
    }

    fun seatNames(pricingCategory: PricingCategory): List<String> {
        return forCategory[pricingCategory]!!
            .map { it.seatNames().joinToString("-") }
    }

    fun matchExpectations(): Boolean {
        return forCategory.values
            .flatten()
            .any { it.matchExpectation() }
    }
}
