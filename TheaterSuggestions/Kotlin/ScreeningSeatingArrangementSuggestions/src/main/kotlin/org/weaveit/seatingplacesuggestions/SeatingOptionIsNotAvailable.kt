package org.weaveit.seatingplacesuggestions

data class SeatingOptionIsNotAvailable(
    override val partyRequested: Int,
    override val pricingCategory: PricingCategory
) : SeatingOption {

    override fun matchExpectation(): Boolean = false

    override fun seats(): List<SeatingPlace> = emptyList()
}
