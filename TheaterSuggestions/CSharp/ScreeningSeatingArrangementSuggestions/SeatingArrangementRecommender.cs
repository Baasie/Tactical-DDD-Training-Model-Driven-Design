namespace SeatsSuggestions;

public class SeatingArrangementRecommender
{
    private readonly AuditoriumSeatingArrangements _auditoriumSeatingArrangements;

    public SeatingArrangementRecommender(AuditoriumSeatingArrangements auditoriumSeatingArrangements)
    {
        _auditoriumSeatingArrangements = auditoriumSeatingArrangements;
    }

    public SuggestionsAreMade MakeSuggestions(string showId, int partyRequested)
    {
        var rows = _auditoriumSeatingArrangements.FindByShowId(showId);
        var suggestionsAreMade = new SuggestionsAreMade(showId, partyRequested);

        foreach (var row in rows.Values)
        {
            var seatsFound = new List<SeatingPlace>();

            foreach (var seat in row.SeatingPlaces)
            {
                if (seat.IsAvailable() && seat.MatchCategory(PricingCategory.First))
                {
                    seatsFound.Add(seat);

                    if (seatsFound.Count == partyRequested)
                    {
                        suggestionsAreMade.AddSeats(PricingCategory.First, seatsFound.Select(s => s.Name).ToList());
                        return suggestionsAreMade;
                    }
                }
            }
        }

        return suggestionsAreMade;
    }
}
