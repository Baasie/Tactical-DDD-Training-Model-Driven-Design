namespace SeatsSuggestions;

public class AuditoriumSeatingArrangement
{
    private readonly Dictionary<string, Row> _rows;

    public AuditoriumSeatingArrangement(Dictionary<string, Row> rows)
    {
        _rows = rows;
    }

    public SeatingOptionIsSuggested SuggestSeatingOptionFor(int partyRequested, PricingCategory pricingCategory)
    {
        foreach (var row in _rows.Values)
        {
            var seatingOptionSuggested = row.SuggestSeatingOption(partyRequested, pricingCategory);

            if (seatingOptionSuggested.MatchExpectation())
            {
                return seatingOptionSuggested;
            }
        }

        return new SeatingOptionIsNotAvailable(partyRequested, pricingCategory);
    }
}
