namespace SeatsSuggestions;

public class SeatingOptionIsSuggested
{
    private readonly PricingCategory _pricingCategory;
    private readonly List<SeatingPlace> _seats = new();
    private readonly int _partyRequested;

    public SeatingOptionIsSuggested(int partyRequested, PricingCategory pricingCategory)
    {
        _pricingCategory = pricingCategory;
        _partyRequested = partyRequested;
    }

    public void AddSeat(SeatingPlace seat)
    {
        _seats.Add(seat);
    }

    public bool MatchExpectation()
    {
        return _seats.Count == _partyRequested;
    }

    public List<SeatingPlace> Seats()
    {
        return _seats;
    }

    public PricingCategory PricingCategory => _pricingCategory;

    public int PartyRequested => _partyRequested;
}
