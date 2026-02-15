namespace SeatsSuggestions;

public record Row
{
    public string Name { get; }
    public IReadOnlyList<SeatingPlace> SeatingPlaces { get; }

    public Row(string name, IReadOnlyList<SeatingPlace> seatingPlaces)
    {
        Name = name;
        SeatingPlaces = seatingPlaces.ToList().AsReadOnly();
    }

    public SeatingOption SuggestSeatingOption(int partyRequested, PricingCategory pricingCategory)
    {
        var foundSeats = new List<SeatingPlace>();

        foreach (var seat in SeatingPlaces)
        {
            if (seat.IsAvailable() && seat.MatchCategory(pricingCategory))
            {
                foundSeats.Add(seat);

                if (foundSeats.Count == partyRequested)
                {
                    return new SeatingOptionIsSuggested(partyRequested, pricingCategory, foundSeats);
                }
            }
        }

        return new SeatingOptionIsNotAvailable(partyRequested, pricingCategory);
    }

    public Row Allocate(IReadOnlyList<SeatingPlace> seatsToAllocate)
    {
        var seatNamesToAllocate = seatsToAllocate.Select(s => s.Name).ToHashSet();

        var updatedSeats = SeatingPlaces.Select(seat =>
            seatNamesToAllocate.Contains(seat.Name) ? seat.Allocate() : seat
        ).ToList();

        return new Row(Name, updatedSeats);
    }

    public virtual bool Equals(Row? other)
    {
        if (other is null) return false;
        if (ReferenceEquals(this, other)) return true;
        return Name == other.Name && SeatingPlaces.SequenceEqual(other.SeatingPlaces);
    }

    public override int GetHashCode()
    {
        var hash = new HashCode();
        hash.Add(Name);
        foreach (var seat in SeatingPlaces)
        {
            hash.Add(seat);
        }
        return hash.ToHashCode();
    }
}
