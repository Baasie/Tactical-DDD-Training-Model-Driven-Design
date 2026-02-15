namespace SeatsSuggestions;

public class SeatingPlace
{
    private readonly string _rowName;
    private readonly int _number;
    private readonly PricingCategory _pricingCategory;
    private readonly bool _isAvailable;

    public SeatingPlace(string rowName, int number, PricingCategory pricingCategory, bool isAvailable)
    {
        _rowName = rowName;
        _number = number;
        _pricingCategory = pricingCategory;
        _isAvailable = isAvailable;
    }

    public bool IsAvailable() => _isAvailable;

    public bool MatchCategory(PricingCategory pricingCategory) => _pricingCategory == pricingCategory;

    public string Name => $"{_rowName}{_number}";
}
