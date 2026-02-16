namespace SeatsSuggestions;

/// <summary>
/// Value Object representing a contiguous group of seats that can be offered together.
///
/// Domain concept: "Adjacent seating" — a party should sit together in consecutive seats,
/// and the group closest to the center of the row is preferred.
/// </summary>
public record AdjacentSeats(IReadOnlyList<SeatingPlace> Seats, double DistanceFromRowCenter)
    : IComparable<AdjacentSeats>
{
    public static AdjacentSeats Of(IReadOnlyList<SeatingPlace> window, int rowSize)
    {
        var windowCenter = (window[0].Number + window[^1].Number) / 2.0;
        var rowCenter = (rowSize + 1) / 2.0;
        return new AdjacentSeats(window.ToList().AsReadOnly(), Math.Abs(windowCenter - rowCenter));
    }

    public int CompareTo(AdjacentSeats? other)
    {
        if (other is null) return 1;
        var distanceCompare = DistanceFromRowCenter.CompareTo(other.DistanceFromRowCenter);
        if (distanceCompare != 0) return distanceCompare;
        return Seats[0].Number.CompareTo(other.Seats[0].Number);
    }
}
