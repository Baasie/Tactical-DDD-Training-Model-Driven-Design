package org.weaveit.seatingplacesuggestions

sealed interface SeatingOption {
    val partyRequested: Int
    val pricingCategory: PricingCategory
    fun matchExpectation(): Boolean
    fun seats(): List<SeatingPlace>
}
