package org.weaveit.seatingplacesuggestions;

import java.util.ArrayList;
import java.util.List;

public class SeatingArrangementRecommender {
    private static final int NUMBER_OF_SUGGESTIONS = 3;
    private final AuditoriumSeatingArrangements auditoriumSeatingArrangements;

    public SeatingArrangementRecommender(AuditoriumSeatingArrangements auditoriumSeatingArrangements) {
        this.auditoriumSeatingArrangements = auditoriumSeatingArrangements;
    }

    public SuggestionsAreMade makeSuggestions(String showId, int partyRequested) {
        var auditoriumSeating = auditoriumSeatingArrangements.findByShowId(showId);

        var allSuggestions = new ArrayList<SuggestionIsMade>();
        allSuggestions.addAll(giveMeSuggestionsFor(auditoriumSeating, partyRequested, PricingCategory.FIRST));
        allSuggestions.addAll(giveMeSuggestionsFor(auditoriumSeating, partyRequested, PricingCategory.SECOND));
        allSuggestions.addAll(giveMeSuggestionsFor(auditoriumSeating, partyRequested, PricingCategory.THIRD));
        allSuggestions.addAll(giveMeSuggestionsFor(auditoriumSeating, partyRequested, PricingCategory.MIXED));

        var suggestionsMade = new SuggestionsAreMade(showId, partyRequested, allSuggestions);

        if (suggestionsMade.matchExpectations())
            return suggestionsMade;

        return new SuggestionsAreNotAvailable(showId, partyRequested);
    }

    private static List<SuggestionIsMade> giveMeSuggestionsFor(
            AuditoriumSeatingArrangement auditoriumSeatingArrangement, int partyRequested, PricingCategory pricingCategory) {
        var foundSuggestions = new ArrayList<SuggestionIsMade>();
        var currentArrangement = auditoriumSeatingArrangement;

        for (int i = 0; i < NUMBER_OF_SUGGESTIONS; i++) {
            var seatingOptionSuggested = currentArrangement.suggestSeatingOptionFor(partyRequested, pricingCategory);

            if (seatingOptionSuggested.matchExpectation()) {
                currentArrangement = currentArrangement.allocate(seatingOptionSuggested.seats());
                foundSuggestions.add(new SuggestionIsMade(seatingOptionSuggested));
            }
        }

        return foundSuggestions;
    }
}
