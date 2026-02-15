package org.weaveit.seatingplacesuggestions;

import java.util.List;

public class Row {
    private final String name;
    private final List<SeatingPlace> seatingPlaces;

    public Row(String name, List<SeatingPlace> seatingPlaces) {
        this.name = name;
        this.seatingPlaces = seatingPlaces;
    }

    public SeatingOptionIsSuggested suggestSeatingOption(int partyRequested, PricingCategory pricingCategory) {

        var seatAllocation = new SeatingOptionIsSuggested(partyRequested, pricingCategory);

        for (var seat : seatingPlaces) {
            if (seat.isAvailable() && seat.matchCategory(pricingCategory)) {
                seatAllocation.addSeat(seat);

                if (seatAllocation.matchExpectation())
                    return seatAllocation;

            }
        }
        return new SeatingOptionIsNotAvailable(partyRequested, pricingCategory);
    }

}
