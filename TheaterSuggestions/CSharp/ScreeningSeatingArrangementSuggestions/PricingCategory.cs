namespace SeatsSuggestions;

public enum PricingCategory
{
    First = 1,
    Second = 2,
    Third = 3
}

public static class PricingCategoryExtensions
{
    public static PricingCategory FromValue(int value)
    {
        return value switch
        {
            1 => PricingCategory.First,
            2 => PricingCategory.Second,
            3 => PricingCategory.Third,
            _ => throw new ArgumentException($"No category for value: {value}")
        };
    }
}
