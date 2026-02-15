namespace SeatsSuggestions;

/// <summary>
/// Value Object representing how far a seating place is from the center of its row.
///
/// Domain concept: "Seats closer to the center provide a better viewing experience"
/// Lower distance = closer to center = more desirable
/// </summary>
public record DistanceFromRowCenter(int Value) : IComparable<DistanceFromRowCenter>
{
    public static DistanceFromRowCenter Of(int seatNumber, int rowSize)
    {
        var center = rowSize / 2;

        if (rowSize % 2 == 0)
        {
            // Even row: center is between two seats (e.g., row of 10 → center between 5 and 6)
            return seatNumber > center
                ? new DistanceFromRowCenter(Math.Abs(seatNumber - center))
                : new DistanceFromRowCenter(Math.Abs(seatNumber - (center + 1)));
        }

        // Odd row: center is a single seat
        return new DistanceFromRowCenter(Math.Abs(seatNumber - (center + 1)));
    }

    public int CompareTo(DistanceFromRowCenter? other)
    {
        if (other is null) return 1;
        return Value.CompareTo(other.Value);
    }
}
