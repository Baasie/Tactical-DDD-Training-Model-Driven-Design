package org.weaveit.seatingplacesuggestions

data class AuditoriumSeatingArrangement private constructor(
    private val _rows: Map<String, Row>
) {
    val rows: Map<String, Row> get() = _rows

    companion object {
        operator fun invoke(rows: Map<String, Row>): AuditoriumSeatingArrangement =
            AuditoriumSeatingArrangement(LinkedHashMap(rows))
    }

    fun suggestSeatingOptionFor(partyRequested: Int, pricingCategory: PricingCategory): SeatingOptionIsSuggested {
        for (row in rows.values) {
            val seatingOptionSuggested = row.suggestSeatingOption(partyRequested, pricingCategory)

            if (seatingOptionSuggested.matchExpectation()) {
                return seatingOptionSuggested
            }
        }

        return SeatingOptionIsNotAvailable(partyRequested, pricingCategory)
    }

    fun allocate(seatsToAllocate: List<SeatingPlace>): AuditoriumSeatingArrangement {
        val updatedRows = rows.mapValues { (_, row) ->
            row.allocate(seatsToAllocate)
        }
        return AuditoriumSeatingArrangement(LinkedHashMap(updatedRows))
    }
}
