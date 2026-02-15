package org.weaveit.seatingplacesuggestions;

import java.util.List;

public record SeatingOptionIsSuggested(
        int partyRequested,
        PricingCategory pricingCategory,
        List<SeatingPlace> seats
) implements SeatingOption {

    public SeatingOptionIsSuggested {
        seats = List.copyOf(seats);
    }

    @Override
    public boolean matchExpectation() {
        return seats.size() == partyRequested;
    }
}
