package org.weaveit.seatingplacesuggestions;

import java.util.List;

public record SuggestionIsMade(
        List<SeatingPlace> suggestedSeats,
        int partyRequested,
        PricingCategory pricingCategory
) {
    public SuggestionIsMade {
        suggestedSeats = List.copyOf(suggestedSeats);
    }

    public SuggestionIsMade(SeatingOption seatingOption) {
        this(seatingOption.seats(), seatingOption.partyRequested(), seatingOption.pricingCategory());
    }

    public List<String> seatNames() {
        return suggestedSeats.stream().map(SeatingPlace::toString).toList();
    }

    public boolean matchExpectation() {
        return suggestedSeats.size() == partyRequested;
    }
}
