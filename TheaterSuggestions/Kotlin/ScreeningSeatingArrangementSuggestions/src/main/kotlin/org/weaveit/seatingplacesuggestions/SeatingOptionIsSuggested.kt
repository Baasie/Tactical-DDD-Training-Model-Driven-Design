package org.weaveit.seatingplacesuggestions

open class SeatingOptionIsSuggested(
    val partyRequested: Int,
    val pricingCategory: PricingCategory
) {
    private val seats: MutableList<SeatingPlace> = mutableListOf()

    fun addSeat(seat: SeatingPlace) {
        seats.add(seat)
    }

    fun matchExpectation(): Boolean {
        return seats.size == partyRequested
    }

    fun seats(): List<SeatingPlace> {
        return seats
    }
}
