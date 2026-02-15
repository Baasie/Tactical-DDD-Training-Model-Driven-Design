package org.weaveit.seatingplacesuggestions

data class SeatingOptionIsSuggested private constructor(
    override val partyRequested: Int,
    override val pricingCategory: PricingCategory,
    private val _seats: List<SeatingPlace>
) : SeatingOption {

    override fun seats(): List<SeatingPlace> = _seats

    companion object {
        operator fun invoke(
            partyRequested: Int,
            pricingCategory: PricingCategory,
            seats: List<SeatingPlace>
        ): SeatingOptionIsSuggested =
            SeatingOptionIsSuggested(partyRequested, pricingCategory, seats.toList())
    }

    override fun matchExpectation(): Boolean {
        return _seats.size == partyRequested
    }
}
