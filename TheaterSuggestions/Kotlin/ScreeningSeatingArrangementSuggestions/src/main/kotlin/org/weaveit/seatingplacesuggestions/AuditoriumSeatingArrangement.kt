package org.weaveit.seatingplacesuggestions

class AuditoriumSeatingArrangement(
    private val rows: Map<String, Row>
) {
    fun suggestSeatingOptionFor(partyRequested: Int, pricingCategory: PricingCategory): SeatingOptionIsSuggested {
        for (row in rows.values) {
            val seatingOptionSuggested = row.suggestSeatingOption(partyRequested, pricingCategory)

            if (seatingOptionSuggested.matchExpectation()) {
                return seatingOptionSuggested
            }
        }

        return SeatingOptionIsNotAvailable(partyRequested, pricingCategory)
    }
}
