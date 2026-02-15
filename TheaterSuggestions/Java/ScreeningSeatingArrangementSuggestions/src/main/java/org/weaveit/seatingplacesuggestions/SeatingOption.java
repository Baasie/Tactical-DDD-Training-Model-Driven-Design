package org.weaveit.seatingplacesuggestions;

import java.util.List;

public sealed interface SeatingOption permits SeatingOptionIsSuggested, SeatingOptionIsNotAvailable {
    boolean matchExpectation();
    List<SeatingPlace> seats();
    PricingCategory pricingCategory();
    int partyRequested();
}
