namespace SeatsSuggestions;

public interface IAuditoriumSeatingArrangements
{
    AuditoriumSeatingArrangement FindByShowId(string showId);
}
