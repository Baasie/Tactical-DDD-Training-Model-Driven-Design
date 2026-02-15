package org.weaveit.seatingplacesuggestions;

import java.util.List;

public class Row {
    private final String name;
    private final List<SeatingPlace> seatingPlaces;

    public Row(String name, List<SeatingPlace> seatingPlaces) {
        this.name = name;
        this.seatingPlaces = seatingPlaces;
    }

    public List<SeatingPlace> seatingPlaces() {
        return seatingPlaces;
    }
}
