package org.weaveit.seatingplacesuggestions

class SeatingPlace(
    private val rowName: String,
    private val number: Int,
    private val pricingCategory: PricingCategory,
    private val isAvailable: Boolean
) {
    fun isAvailable(): Boolean = isAvailable

    fun matchCategory(pricingCategory: PricingCategory): Boolean = this.pricingCategory == pricingCategory

    fun name(): String = "$rowName$number"
}
