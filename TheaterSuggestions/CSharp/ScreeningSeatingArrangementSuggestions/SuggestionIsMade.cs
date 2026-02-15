namespace SeatsSuggestions;

public record SuggestionIsMade
{
    public IReadOnlyList<SeatingPlace> SuggestedSeats { get; }
    public int PartyRequested { get; }
    public PricingCategory PricingCategory { get; }

    public SuggestionIsMade(IReadOnlyList<SeatingPlace> suggestedSeats, int partyRequested, PricingCategory pricingCategory)
    {
        SuggestedSeats = suggestedSeats.ToList().AsReadOnly();
        PartyRequested = partyRequested;
        PricingCategory = pricingCategory;
    }

    public SuggestionIsMade(SeatingOption seatingOption)
        : this(seatingOption.Seats(), seatingOption.PartyRequested, seatingOption.PricingCategory)
    {
    }

    public List<string> SeatNames()
    {
        return SuggestedSeats.Select(s => s.ToString()).ToList();
    }

    public bool MatchExpectation()
    {
        return SuggestedSeats.Count == PartyRequested;
    }

    public virtual bool Equals(SuggestionIsMade? other)
    {
        if (other is null) return false;
        if (ReferenceEquals(this, other)) return true;
        return PartyRequested == other.PartyRequested
            && PricingCategory == other.PricingCategory
            && SuggestedSeats.SequenceEqual(other.SuggestedSeats);
    }

    public override int GetHashCode()
    {
        var hash = new HashCode();
        hash.Add(PartyRequested);
        hash.Add(PricingCategory);
        foreach (var seat in SuggestedSeats)
        {
            hash.Add(seat);
        }
        return hash.ToHashCode();
    }
}
