namespace SeatsSuggestions;

public class SeatingPlace
{
    private readonly string _rowName;
    private readonly int _number;
    private readonly PricingCategory _pricingCategory;
    private SeatingPlaceAvailability _seatingPlaceAvailability;

    public SeatingPlace(string rowName, int number, PricingCategory pricingCategory, SeatingPlaceAvailability seatingPlaceAvailability)
    {
        _rowName = rowName;
        _number = number;
        _pricingCategory = pricingCategory;
        _seatingPlaceAvailability = seatingPlaceAvailability;
    }

    public bool IsAvailable()
    {
        return _seatingPlaceAvailability == SeatingPlaceAvailability.Available;
    }

    public bool MatchCategory(PricingCategory pricingCategory)
    {
        return _pricingCategory == pricingCategory;
    }

    public void Allocate()
    {
        if (_seatingPlaceAvailability == SeatingPlaceAvailability.Available)
        {
            _seatingPlaceAvailability = SeatingPlaceAvailability.Allocated;
        }
    }

    public override string ToString()
    {
        return $"{_rowName}{_number}";
    }
}
