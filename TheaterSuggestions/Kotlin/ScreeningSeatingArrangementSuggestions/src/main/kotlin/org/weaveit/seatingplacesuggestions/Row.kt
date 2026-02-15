package org.weaveit.seatingplacesuggestions

data class Row private constructor(
    val name: String,
    private val _seatingPlaces: List<SeatingPlace>
) {
    val seatingPlaces: List<SeatingPlace> get() = _seatingPlaces

    companion object {
        operator fun invoke(name: String, seatingPlaces: List<SeatingPlace>): Row =
            Row(name, seatingPlaces.toList())
    }

    fun suggestSeatingOption(partyRequested: Int, pricingCategory: PricingCategory): SeatingOption {
        val foundSeats = mutableListOf<SeatingPlace>()

        for (seat in seatingPlaces) {
            if (seat.isAvailable() && seat.matchCategory(pricingCategory)) {
                foundSeats.add(seat)

                if (foundSeats.size == partyRequested) {
                    return SeatingOptionIsSuggested(partyRequested, pricingCategory, foundSeats)
                }
            }
        }

        return SeatingOptionIsNotAvailable(partyRequested, pricingCategory)
    }

    fun allocate(seatsToAllocate: List<SeatingPlace>): Row {
        val seatNamesToAllocate = seatsToAllocate.map { it.name() }.toSet()

        val updatedSeats = seatingPlaces.map { seat ->
            if (seat.name() in seatNamesToAllocate) {
                seat.allocate()
            } else {
                seat
            }
        }
        return Row(name, updatedSeats)
    }
}
