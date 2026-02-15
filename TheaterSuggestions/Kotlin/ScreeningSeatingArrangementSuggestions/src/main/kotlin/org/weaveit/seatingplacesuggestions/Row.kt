package org.weaveit.seatingplacesuggestions

class Row(
    private val name: String,
    private val seatingPlaces: List<SeatingPlace>
) {
    fun seatingPlaces(): List<SeatingPlace> = seatingPlaces
}
