package org.weaveit.seatingplacesuggestions

enum class PricingCategory(val value: Int) {
    FIRST(1),
    SECOND(2),
    THIRD(3),
    MIXED(4);

    companion object {
        private val map = entries.associateBy { it.value }

        fun valueOf(value: Int): PricingCategory {
            return map[value]
                ?: throw IllegalArgumentException("No enum constant with value $value")
        }
    }
}
