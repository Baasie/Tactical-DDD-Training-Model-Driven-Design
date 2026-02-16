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
        var rowSize = SeatingPlaces.Count;

        var availableSeats = SeatingPlaces
            .Where(seat => seat.IsAvailable())
            .Where(seat => seat.MatchCategory(pricingCategory))
            .ToList();

        if (availableSeats.Count < partyRequested)
        {
            return new SeatingOptionIsNotAvailable(partyRequested, pricingCategory);
        }

        // Find contiguous blocks of available seats
        var contiguousBlocks = new List<List<SeatingPlace>>();
        var currentBlock = new List<SeatingPlace> { availableSeats[0] };

        for (var i = 1; i < availableSeats.Count; i++)
        {
            if (availableSeats[i].Number == availableSeats[i - 1].Number + 1)
            {
                currentBlock.Add(availableSeats[i]);
            }
            else
            {
                contiguousBlocks.Add(new List<SeatingPlace>(currentBlock));
                currentBlock.Clear();
                currentBlock.Add(availableSeats[i]);
            }
        }
        contiguousBlocks.Add(new List<SeatingPlace>(currentBlock));

        // Find the best adjacent seating option
        AdjacentSeats? bestAdjacentSeats = null;

        foreach (var block in contiguousBlocks)
        {
            if (block.Count < partyRequested) continue;

            for (var i = 0; i <= block.Count - partyRequested; i++)
            {
                var candidate = AdjacentSeats.Of(block.GetRange(i, partyRequested), rowSize);
                if (bestAdjacentSeats == null || candidate.CompareTo(bestAdjacentSeats) < 0)
                {
                    bestAdjacentSeats = candidate;
                }
            }
        }

        if (bestAdjacentSeats != null)
        {
            return new SeatingOptionIsSuggested(partyRequested, pricingCategory, bestAdjacentSeats.Seats.ToList());
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
