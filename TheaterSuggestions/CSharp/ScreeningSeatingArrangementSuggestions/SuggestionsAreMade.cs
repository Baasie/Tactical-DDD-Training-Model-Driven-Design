namespace SeatsSuggestions;

public class SuggestionsAreMade
{
    private readonly string _showId;
    private readonly int _partyRequested;
    private readonly IReadOnlyDictionary<PricingCategory, IReadOnlyList<SuggestionIsMade>> _forCategory;

    public SuggestionsAreMade(string showId, int partyRequested, IEnumerable<SuggestionIsMade> suggestions)
    {
        _showId = showId;
        _partyRequested = partyRequested;
        _forCategory = OrganizeSuggestionsByCategory(suggestions);
    }

    private static IReadOnlyDictionary<PricingCategory, IReadOnlyList<SuggestionIsMade>> OrganizeSuggestionsByCategory(
        IEnumerable<SuggestionIsMade> suggestions)
    {
        var result = new Dictionary<PricingCategory, List<SuggestionIsMade>>();

        foreach (PricingCategory category in Enum.GetValues(typeof(PricingCategory)))
        {
            result[category] = new List<SuggestionIsMade>();
        }

        foreach (var suggestion in suggestions)
        {
            result[suggestion.PricingCategory].Add(suggestion);
        }

        return result.ToDictionary(
            kvp => kvp.Key,
            kvp => (IReadOnlyList<SuggestionIsMade>)kvp.Value.AsReadOnly()
        );
    }

    public List<string> SeatNames(PricingCategory pricingCategory)
    {
        return _forCategory[pricingCategory]
            .SelectMany(s => s.SeatNames())
            .ToList();
    }

    public bool MatchExpectations()
    {
        return _forCategory.Values
            .SelectMany(list => list)
            .Any(s => s.MatchExpectation());
    }

    public string ShowId => _showId;

    public int PartyRequested => _partyRequested;
}
