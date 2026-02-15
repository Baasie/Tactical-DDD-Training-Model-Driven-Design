namespace SeatsSuggestions;

public class SuggestionsAreMade
{
    private readonly Dictionary<PricingCategory, List<string>> _seatsByCategory = new();

    public string ShowId { get; }
    public int PartyRequested { get; }

    public SuggestionsAreMade(string showId, int partyRequested)
    {
        ShowId = showId;
        PartyRequested = partyRequested;
    }

    public void AddSeats(PricingCategory category, List<string> seatNames)
    {
        if (!_seatsByCategory.ContainsKey(category))
        {
            _seatsByCategory[category] = new List<string>();
        }
        _seatsByCategory[category].AddRange(seatNames);
    }

    public IEnumerable<string> SeatNames(PricingCategory pricingCategory)
    {
        return _seatsByCategory.TryGetValue(pricingCategory, out var seats)
            ? seats
            : Enumerable.Empty<string>();
    }
}
