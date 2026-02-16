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
        val rowSize = seatingPlaces.size

        val availableSeats = seatingPlaces
            .filter { it.isAvailable() }
            .filter { it.matchCategory(pricingCategory) }

        if (availableSeats.size < partyRequested) {
            return SeatingOptionIsNotAvailable(partyRequested, pricingCategory)
        }

        // Find contiguous blocks of available seats
        val contiguousBlocks = mutableListOf<List<SeatingPlace>>()
        val currentBlock = mutableListOf(availableSeats[0])

        for (i in 1 until availableSeats.size) {
            if (availableSeats[i].number == availableSeats[i - 1].number + 1) {
                currentBlock.add(availableSeats[i])
            } else {
                contiguousBlocks.add(currentBlock.toList())
                currentBlock.clear()
                currentBlock.add(availableSeats[i])
            }
        }
        contiguousBlocks.add(currentBlock.toList())

        // Find the best adjacent seating option
        val bestAdjacentSeats = contiguousBlocks
            .filter { it.size >= partyRequested }
            .flatMap { block ->
                (0..block.size - partyRequested).map { i ->
                    AdjacentSeats.of(block.subList(i, i + partyRequested), rowSize)
                }
            }
            .minOrNull()

        return if (bestAdjacentSeats != null) {
            SeatingOptionIsSuggested(partyRequested, pricingCategory, bestAdjacentSeats.seats)
        } else {
            SeatingOptionIsNotAvailable(partyRequested, pricingCategory)
        }
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
