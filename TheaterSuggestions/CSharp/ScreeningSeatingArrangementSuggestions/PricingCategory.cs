namespace SeatsSuggestions;

public enum PricingCategory
{
    First = 1,
    Second = 2,
    Third = 3,
    Mixed = 4
}

public static class PricingCategoryExtensions
{
    private static readonly Dictionary<int, PricingCategory> Map = new()
    {
        { 1, PricingCategory.First },
        { 2, PricingCategory.Second },
        { 3, PricingCategory.Third }
    };

    public static PricingCategory FromValue(int value)
    {
        if (Map.TryGetValue(value, out var category))
        {
            return category;
        }
        throw new ArgumentException($"No enum constant with value {value}");
    }
}
