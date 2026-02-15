package org.weaveit.seatingplacesuggestions;

import java.util.List;

public class SuggestionsAreNotAvailable extends SuggestionsAreMade {

    public SuggestionsAreNotAvailable(String showId, int partyRequested) {
        super(showId, partyRequested, List.of());
    }
}
