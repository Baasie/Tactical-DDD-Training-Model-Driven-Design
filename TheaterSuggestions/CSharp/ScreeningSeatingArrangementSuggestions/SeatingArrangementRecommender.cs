namespace SeatsSuggestions;

public class SeatingArrangementRecommender
{
    private const int NumberOfSuggestions = 3;
    private readonly AuditoriumSeatingArrangements _auditoriumSeatingArrangements;

    public SeatingArrangementRecommender(AuditoriumSeatingArrangements auditoriumSeatingArrangements)
    {
        _auditoriumSeatingArrangements = auditoriumSeatingArrangements;
    }

    public SuggestionsAreMade MakeSuggestions(string showId, int partyRequested)
    {
        var auditoriumSeating = _auditoriumSeatingArrangements.FindByShowId(showId);

        var suggestionsMade = new SuggestionsAreMade(showId, partyRequested);

        suggestionsMade.Add(GiveMeSuggestionsFor(auditoriumSeating, partyRequested, PricingCategory.First));
        suggestionsMade.Add(GiveMeSuggestionsFor(auditoriumSeating, partyRequested, PricingCategory.Second));
        suggestionsMade.Add(GiveMeSuggestionsFor(auditoriumSeating, partyRequested, PricingCategory.Third));
        suggestionsMade.Add(GiveMeSuggestionsFor(auditoriumSeating, partyRequested, PricingCategory.Mixed));

        if (suggestionsMade.MatchExpectations())
        {
            return suggestionsMade;
        }

        return new SuggestionsAreNotAvailable(showId, partyRequested);
    }

    private static List<SuggestionIsMade> GiveMeSuggestionsFor(
        AuditoriumSeatingArrangement auditoriumSeatingArrangement,
        int partyRequested,
        PricingCategory pricingCategory)
    {
        var foundedSuggestions = new List<SuggestionIsMade>();
        var currentArrangement = auditoriumSeatingArrangement;

        for (int i = 0; i < NumberOfSuggestions; i++)
        {
            var seatingOptionSuggested = currentArrangement.SuggestSeatingOptionFor(partyRequested, pricingCategory);

            if (seatingOptionSuggested.MatchExpectation())
            {
                currentArrangement = currentArrangement.Allocate(seatingOptionSuggested.Seats());
                foundedSuggestions.Add(new SuggestionIsMade(seatingOptionSuggested));
            }
        }

        return foundedSuggestions;
    }
}
