namespace SeatsSuggestions;

public class Row
{
    private readonly string _name;
    private readonly List<SeatingPlace> _seatingPlaces;

    public Row(string name, List<SeatingPlace> seatingPlaces)
    {
        _name = name;
        _seatingPlaces = seatingPlaces;
    }

    public SeatingOptionIsSuggested SuggestSeatingOption(int partyRequested, PricingCategory pricingCategory)
    {
        var seatAllocation = new SeatingOptionIsSuggested(partyRequested, pricingCategory);

        foreach (var seat in _seatingPlaces)
        {
            if (seat.IsAvailable() && seat.MatchCategory(pricingCategory))
            {
                seatAllocation.AddSeat(seat);

                if (seatAllocation.MatchExpectation())
                {
                    return seatAllocation;
                }
            }
        }

        return new SeatingOptionIsNotAvailable(partyRequested, pricingCategory);
    }
}
