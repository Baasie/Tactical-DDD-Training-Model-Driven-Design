using ExternalDependencies.AuditoriumLayoutRepository;
using ExternalDependencies.ReservationsProvider;
using SeatsSuggestions;

var builder = WebApplication.CreateBuilder(args);
var app = builder.Build();

app.MapGet("/api/suggestions", (string showId, int partySize) =>
{
    var auditoriumSeatingArrangements = new AuditoriumSeatingArrangements(
        new AuditoriumLayoutRepository(),
        new ReservationsProvider()
    );

    var recommender = new SeatingArrangementRecommender(auditoriumSeatingArrangements);
    var suggestions = recommender.MakeSuggestions(showId, partySize);

    var suggestionsMap = new Dictionary<string, List<string>>();
    foreach (PricingCategory category in Enum.GetValues(typeof(PricingCategory)))
    {
        suggestionsMap[category.ToString()] = suggestions.SeatNames(category);
    }

    return new
    {
        showId = suggestions.ShowId,
        partySize = suggestions.PartyRequested,
        suggestions = suggestionsMap
    };
});

app.Run();
