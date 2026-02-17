package org.weaveit.seatingplacesuggestions.api;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.weaveit.seatingplacesuggestions.*;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@RestController
public class SuggestionsController {

    private final SeatingArrangementRecommender recommender;

    public SuggestionsController(SeatingArrangementRecommender recommender) {
        this.recommender = recommender;
    }

    @GetMapping("/api/suggestions")
    public Map<String, Object> getSuggestions(
            @RequestParam String showId,
            @RequestParam int partySize) throws IOException {

        var suggestions = recommender.makeSuggestions(showId, partySize);

        return toResponse(suggestions);
    }

    private static Map<String, Object> toResponse(SuggestionsAreMade suggestions) {
        var response = new LinkedHashMap<String, Object>();
        response.put("showId", suggestions.showId());
        response.put("partySize", suggestions.partyRequested());

        var suggestionsMap = new LinkedHashMap<String, List<String>>();
        for (PricingCategory category : PricingCategory.values()) {
            suggestionsMap.put(category.name(), suggestions.seatNames(category));
        }
        response.put("suggestions", suggestionsMap);

        return response;
    }
}
