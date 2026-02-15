package org.weaveit.seatingplacesuggestions;

import java.util.*;

public class SuggestionsAreMade {
    private final String showId;
    private final int partyRequested;
    private final Map<PricingCategory, List<SuggestionIsMade>> forCategory;

    public SuggestionsAreMade(String showId, int partyRequested, List<SuggestionIsMade> suggestions) {
        this.showId = showId;
        this.partyRequested = partyRequested;
        this.forCategory = organizeSuggestionsByCategory(suggestions);
    }

    private static Map<PricingCategory, List<SuggestionIsMade>> organizeSuggestionsByCategory(
            List<SuggestionIsMade> suggestions) {
        var result = new LinkedHashMap<PricingCategory, List<SuggestionIsMade>>();

        for (PricingCategory category : PricingCategory.values()) {
            result.put(category, new ArrayList<>());
        }

        for (SuggestionIsMade suggestion : suggestions) {
            result.get(suggestion.pricingCategory()).add(suggestion);
        }

        // Make lists unmodifiable
        var immutableResult = new LinkedHashMap<PricingCategory, List<SuggestionIsMade>>();
        for (var entry : result.entrySet()) {
            immutableResult.put(entry.getKey(), List.copyOf(entry.getValue()));
        }

        return Collections.unmodifiableMap(immutableResult);
    }

    public List<String> seatNames(PricingCategory pricingCategory) {
        return forCategory.get(pricingCategory).stream()
                .map(SuggestionIsMade::seatNames)
                .flatMap(Collection::stream)
                .toList();
    }

    public boolean matchExpectations() {
        return forCategory.values().stream()
                .flatMap(List::stream)
                .anyMatch(SuggestionIsMade::matchExpectation);
    }

    public String showId() {
        return showId;
    }

    public int partyRequested() {
        return partyRequested;
    }
}
