package org.weaveit.seatingplacesuggestions;

import java.util.List;

public record SeatingOptionIsNotAvailable(
        int partyRequested,
        PricingCategory pricingCategory
) implements SeatingOption {

    @Override
    public boolean matchExpectation() {
        return false;
    }

    @Override
    public List<SeatingPlace> seats() {
        return List.of();
    }
}
