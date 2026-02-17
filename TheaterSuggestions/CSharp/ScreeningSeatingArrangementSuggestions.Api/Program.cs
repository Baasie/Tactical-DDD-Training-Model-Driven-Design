using ExternalDependencies.AuditoriumLayoutRepository;
using ExternalDependencies.ReservationsProvider;
using ScreeningSeatingArrangementSuggestions.Api;
using SeatsSuggestions;

var builder = WebApplication.CreateBuilder(args);

builder.Services.AddSingleton<IAuditoriumSeatingArrangements>(
    _ => new AuditoriumSeatingArrangements(new AuditoriumLayoutRepository(), new ReservationsProvider()));
builder.Services.AddSingleton<SeatingArrangementRecommender>();

var app = builder.Build();

app.MapGet("/api/suggestions", (string showId, int partySize, SeatingArrangementRecommender recommender) =>
{
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
