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

        var suggestionsMade = new SuggestionsAreMade(showId, partyRequested);

        suggestionsMade.add(giveMeSuggestionsFor(auditoriumSeating, partyRequested,
                PricingCategory.FIRST));
        suggestionsMade.add(giveMeSuggestionsFor(auditoriumSeating, partyRequested,
                PricingCategory.SECOND));
        suggestionsMade.add(giveMeSuggestionsFor(auditoriumSeating, partyRequested,
                PricingCategory.THIRD));
        suggestionsMade.add(giveMeSuggestionsFor(auditoriumSeating, partyRequested,
                PricingCategory.MIXED));

        if (suggestionsMade.matchExpectations())
            return suggestionsMade;

        return new SuggestionsAreNotAvailable(showId, partyRequested);
    }

    private static List<SuggestionIsMade> giveMeSuggestionsFor(
            AuditoriumSeatingArrangement auditoriumSeatingArrangement, int partyRequested, PricingCategory pricingCategory) {
        var foundedSuggestions = new ArrayList<SuggestionIsMade>();
        var currentArrangement = auditoriumSeatingArrangement;

        for (int i = 0; i < NUMBER_OF_SUGGESTIONS; i++) {
            var seatingOptionSuggested = currentArrangement.suggestSeatingOptionFor(partyRequested, pricingCategory);

            if (seatingOptionSuggested.matchExpectation()) {
                currentArrangement = currentArrangement.allocate(seatingOptionSuggested.seats());
                foundedSuggestions.add(new SuggestionIsMade(seatingOptionSuggested));
            }
        }

        return foundedSuggestions;
    }
}
