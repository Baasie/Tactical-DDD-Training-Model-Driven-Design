namespace SeatsSuggestions;

public interface SeatingOption
{
    int PartyRequested { get; }
    PricingCategory PricingCategory { get; }
    bool MatchExpectation();
    IReadOnlyList<SeatingPlace> Seats();
}
