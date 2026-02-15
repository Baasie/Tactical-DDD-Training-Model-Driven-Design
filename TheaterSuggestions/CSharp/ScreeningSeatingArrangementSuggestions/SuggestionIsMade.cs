namespace SeatsSuggestions;

public class SuggestionIsMade
{
    private readonly List<SeatingPlace> _suggestedSeats;
    private readonly int _partyRequested;
    private readonly PricingCategory _pricingCategory;

    public SuggestionIsMade(SeatingOptionIsSuggested seatingOptionIsSuggested)
    {
        _suggestedSeats = seatingOptionIsSuggested.Seats();
        _partyRequested = seatingOptionIsSuggested.PartyRequested;
        _pricingCategory = seatingOptionIsSuggested.PricingCategory;
    }

    public List<string> SeatNames()
    {
        return _suggestedSeats.Select(s => s.ToString()).ToList();
    }

    public bool MatchExpectation()
    {
        return _suggestedSeats.Count == _partyRequested;
    }

    public PricingCategory PricingCategory => _pricingCategory;
}
