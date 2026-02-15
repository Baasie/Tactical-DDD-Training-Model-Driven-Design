namespace SeatsSuggestions;

public class SeatingOptionIsNotAvailable : SeatingOptionIsSuggested
{
    public SeatingOptionIsNotAvailable(int partyRequested, PricingCategory pricingCategory)
        : base(partyRequested, pricingCategory)
    {
    }
}
