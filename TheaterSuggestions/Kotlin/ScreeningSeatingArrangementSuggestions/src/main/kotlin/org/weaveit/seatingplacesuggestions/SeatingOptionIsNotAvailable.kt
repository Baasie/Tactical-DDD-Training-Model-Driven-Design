package org.weaveit.seatingplacesuggestions

class SeatingOptionIsNotAvailable(
    partyRequested: Int,
    pricingCategory: PricingCategory
) : SeatingOptionIsSuggested(partyRequested, pricingCategory)
