namespace SeatsSuggestions;

public record SeatingOptionIsNotAvailable(int PartyRequested, PricingCategory PricingCategory) : SeatingOption
{
    public bool MatchExpectation() => false;

    public IReadOnlyList<SeatingPlace> Seats() => Array.Empty<SeatingPlace>();
}
