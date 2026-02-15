namespace SeatsSuggestions;

public record SeatingOptionIsSuggested : SeatingOption
{
    public int PartyRequested { get; }
    public PricingCategory PricingCategory { get; }
    private readonly IReadOnlyList<SeatingPlace> _seats;

    public SeatingOptionIsSuggested(int partyRequested, PricingCategory pricingCategory, IReadOnlyList<SeatingPlace> seats)
    {
        PartyRequested = partyRequested;
        PricingCategory = pricingCategory;
        _seats = seats.ToList().AsReadOnly();
    }

    public bool MatchExpectation()
    {
        return _seats.Count == PartyRequested;
    }

    public IReadOnlyList<SeatingPlace> Seats() => _seats;

    public virtual bool Equals(SeatingOptionIsSuggested? other)
    {
        if (other is null) return false;
        if (ReferenceEquals(this, other)) return true;
        return PartyRequested == other.PartyRequested
            && PricingCategory == other.PricingCategory
            && _seats.SequenceEqual(other._seats);
    }

    public override int GetHashCode()
    {
        var hash = new HashCode();
        hash.Add(PartyRequested);
        hash.Add(PricingCategory);
        foreach (var seat in _seats)
        {
            hash.Add(seat);
        }
        return hash.ToHashCode();
    }
}
