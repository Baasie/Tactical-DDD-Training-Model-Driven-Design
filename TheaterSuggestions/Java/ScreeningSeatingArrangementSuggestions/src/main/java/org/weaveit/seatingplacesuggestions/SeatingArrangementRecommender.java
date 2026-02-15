package org.weaveit.seatingplacesuggestions;

import java.util.ArrayList;
import java.util.List;

public class SeatingArrangementRecommender {
    private final AuditoriumSeatingArrangements auditoriumSeatingArrangements;

    public SeatingArrangementRecommender(AuditoriumSeatingArrangements auditoriumSeatingArrangements) {
        this.auditoriumSeatingArrangements = auditoriumSeatingArrangements;
    }

    public SuggestionsAreMade makeSuggestions(String showId, int partyRequested) {
        var rows = auditoriumSeatingArrangements.findByShowId(showId);
        var suggestionsAreMade = new SuggestionsAreMade(showId, partyRequested);

        for (var row : rows.values()) {
            List<SeatingPlace> seatsFound = new ArrayList<>();

            for (var seat : row.seatingPlaces()) {
                if (seat.isAvailable() && seat.matchCategory(PricingCategory.FIRST)) {
                    seatsFound.add(seat);

                    if (seatsFound.size() == partyRequested) {
                        suggestionsAreMade.addSeats(PricingCategory.FIRST, seatsFound.stream().map(SeatingPlace::name).toList());
                        return suggestionsAreMade;
                    }
                }
            }
        }

        return suggestionsAreMade;
    }
}
