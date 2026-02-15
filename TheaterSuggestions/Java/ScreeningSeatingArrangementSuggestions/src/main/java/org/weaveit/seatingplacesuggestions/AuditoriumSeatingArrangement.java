package org.weaveit.seatingplacesuggestions;

import java.util.Map;

public class AuditoriumSeatingArrangement {
    private final Map<String, Row> rows;

    public AuditoriumSeatingArrangement(Map<String, Row> rows) {
        this.rows = rows;
    }

    public SeatingOptionIsSuggested suggestSeatingOptionFor(int partyRequested, PricingCategory pricingCategory) {
        for (Row row : rows.values()) {
            var seatingOptionSuggested = row.suggestSeatingOption(partyRequested, pricingCategory);

            if (seatingOptionSuggested.matchExpectation()) {
                return seatingOptionSuggested;
            }
        }

        return new SeatingOptionIsNotAvailable(partyRequested, pricingCategory);
    }
}
