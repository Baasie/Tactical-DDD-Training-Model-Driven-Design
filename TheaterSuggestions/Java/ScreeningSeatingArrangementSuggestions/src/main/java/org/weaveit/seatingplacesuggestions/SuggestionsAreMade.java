package org.weaveit.seatingplacesuggestions;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SuggestionsAreMade {
    private final String showId;
    private final int partyRequested;
    private final Map<PricingCategory, List<String>> seatsByCategory = new HashMap<>();

    public SuggestionsAreMade(String showId, int partyRequested) {
        this.showId = showId;
        this.partyRequested = partyRequested;
    }

    public void addSeats(PricingCategory category, List<String> seatNames) {
        seatsByCategory.computeIfAbsent(category, k -> new ArrayList<>()).addAll(seatNames);
    }

    public List<String> seatNames(PricingCategory pricingCategory) {
        return seatsByCategory.getOrDefault(pricingCategory, List.of());
    }

    public String showId() {
        return showId;
    }

    public int partyRequested() {
        return partyRequested;
    }
}
