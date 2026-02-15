namespace SeatsSuggestions;

public class SuggestionsAreNotAvailable : SuggestionsAreMade
{
    public SuggestionsAreNotAvailable(string showId, int partyRequested)
        : base(showId, partyRequested, Array.Empty<SuggestionIsMade>())
    {
    }
}
