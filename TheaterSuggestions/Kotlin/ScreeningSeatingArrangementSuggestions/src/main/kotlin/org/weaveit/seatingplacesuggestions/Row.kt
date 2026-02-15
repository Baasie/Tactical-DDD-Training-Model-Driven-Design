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
        val selectedSeats = mutableListOf<SeatingPlace>()
        val rowSize = seatingPlaces.size

        val availableSeatsCloserToCenter = seatingPlaces
            .filter { it.isAvailable() }
            .filter { it.matchCategory(pricingCategory) }
            .sortedBy { DistanceFromRowCenter.of(it.number, rowSize) }

        for (seat in availableSeatsCloserToCenter) {
            selectedSeats.add(seat)

            if (selectedSeats.size == partyRequested) {
                return SeatingOptionIsSuggested(partyRequested, pricingCategory, selectedSeats.toList())
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
