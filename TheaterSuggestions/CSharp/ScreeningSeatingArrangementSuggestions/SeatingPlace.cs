namespace SeatsSuggestions;

public record SeatingPlace(
    string RowName,
    int Number,
    PricingCategory PricingCategory,
    SeatingPlaceAvailability SeatingPlaceAvailability
)
{
    public bool IsAvailable()
    {
        return SeatingPlaceAvailability == SeatingPlaceAvailability.Available;
    }

    public bool MatchCategory(PricingCategory pricingCategory)
    {
        if (pricingCategory == PricingCategory.Mixed)
        {
            return true;
        }
        return PricingCategory == pricingCategory;
    }

    public SeatingPlace Allocate()
    {
        if (SeatingPlaceAvailability == SeatingPlaceAvailability.Available)
        {
            return this with { SeatingPlaceAvailability = SeatingPlaceAvailability.Allocated };
        }
        return this;
    }

    public string Name => $"{RowName}{Number}";

    public override string ToString() => Name;
}
