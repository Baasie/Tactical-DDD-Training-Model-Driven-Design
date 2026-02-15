namespace SeatsSuggestions;

public class SuggestionsAreMade
{
    private readonly string _showId;
    private readonly int _partyRequested;
    private readonly Dictionary<PricingCategory, List<SuggestionIsMade>> _forCategory = new();

    public SuggestionsAreMade(string showId, int partyRequested)
    {
        _showId = showId;
        _partyRequested = partyRequested;

        InstantiateAnEmptyListForEveryPricingCategory();
    }

    public List<string> SeatNames(PricingCategory pricingCategory)
    {
        return _forCategory[pricingCategory]
            .SelectMany(s => s.SeatNames())
            .ToList();
    }

    private void InstantiateAnEmptyListForEveryPricingCategory()
    {
        foreach (PricingCategory pricingCategory in Enum.GetValues(typeof(PricingCategory)))
        {
            _forCategory[pricingCategory] = new List<SuggestionIsMade>();
        }
    }

    public void Add(IEnumerable<SuggestionIsMade> suggestions)
    {
        foreach (var suggestionIsMade in suggestions)
        {
            _forCategory[suggestionIsMade.PricingCategory].Add(suggestionIsMade);
        }
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
