namespace SeatsSuggestions;

public record AuditoriumSeatingArrangement
{
    public IReadOnlyDictionary<string, Row> Rows { get; }

    public AuditoriumSeatingArrangement(IDictionary<string, Row> rows)
    {
        Rows = new Dictionary<string, Row>(rows);
    }

    public SeatingOption SuggestSeatingOptionFor(int partyRequested, PricingCategory pricingCategory)
    {
        foreach (var row in Rows.Values)
        {
            var seatingOption = row.SuggestSeatingOption(partyRequested, pricingCategory);

            if (seatingOption.MatchExpectation())
            {
                return seatingOption;
            }
        }

        return new SeatingOptionIsNotAvailable(partyRequested, pricingCategory);
    }

    public AuditoriumSeatingArrangement Allocate(IReadOnlyList<SeatingPlace> seatsToAllocate)
    {
        var updatedRows = new Dictionary<string, Row>();
        foreach (var (key, row) in Rows)
        {
            updatedRows[key] = row.Allocate(seatsToAllocate);
        }
        return new AuditoriumSeatingArrangement(updatedRows);
    }

    public virtual bool Equals(AuditoriumSeatingArrangement? other)
    {
        if (other is null) return false;
        if (ReferenceEquals(this, other)) return true;
        if (Rows.Count != other.Rows.Count) return false;

        foreach (var (key, value) in Rows)
        {
            if (!other.Rows.TryGetValue(key, out var otherValue) || !value.Equals(otherValue))
            {
                return false;
            }
        }
        return true;
    }

    public override int GetHashCode()
    {
        var hash = new HashCode();
        foreach (var (key, value) in Rows.OrderBy(kv => kv.Key))
        {
            hash.Add(key);
            hash.Add(value);
        }
        return hash.ToHashCode();
    }
}
