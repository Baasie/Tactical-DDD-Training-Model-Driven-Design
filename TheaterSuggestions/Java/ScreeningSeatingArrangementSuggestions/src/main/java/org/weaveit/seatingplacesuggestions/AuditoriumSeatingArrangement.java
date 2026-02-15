package org.weaveit.seatingplacesuggestions;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public record AuditoriumSeatingArrangement(Map<String, Row> rows) {

    public AuditoriumSeatingArrangement {
        rows = Collections.unmodifiableMap(new LinkedHashMap<>(rows));
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

    public AuditoriumSeatingArrangement allocate(List<SeatingPlace> seatsToAllocate) {
        Map<String, Row> updatedRows = new LinkedHashMap<>();
        for (var entry : rows.entrySet()) {
            Row updatedRow = entry.getValue().allocate(seatsToAllocate);
            updatedRows.put(entry.getKey(), updatedRow);
        }
        return new AuditoriumSeatingArrangement(updatedRows);
    }
}
