using ExternalDependencies.AuditoriumLayoutRepository;
using ExternalDependencies.ReservationsProvider;

namespace SeatsSuggestions;

public class AuditoriumSeatingArrangements
{
    private readonly AuditoriumLayoutRepository _auditoriumLayoutRepository;
    private readonly ReservationsProvider _reservationsProvider;

    public AuditoriumSeatingArrangements(AuditoriumLayoutRepository auditoriumLayoutRepository, ReservationsProvider reservationsProvider)
    {
        _auditoriumLayoutRepository = auditoriumLayoutRepository;
        _reservationsProvider = reservationsProvider;
    }

    public Dictionary<string, Row> FindByShowId(string showId)
    {
        var auditoriumDto = _auditoriumLayoutRepository.FindByShowId(showId);
        var reservedSeatsDto = _reservationsProvider.GetReservedSeats(showId);

        var rows = new Dictionary<string, Row>();

        foreach (var rowEntry in auditoriumDto.Rows)
        {
            var rowName = rowEntry.Key;
            var seatingPlaces = new List<SeatingPlace>();

            foreach (var seatDto in rowEntry.Value)
            {
                var number = int.Parse(seatDto.Name.Substring(1));
                var category = PricingCategoryExtensions.FromValue(seatDto.Category);
                var isReserved = reservedSeatsDto.ReservedSeats?.Contains(seatDto.Name) ?? false;

                seatingPlaces.Add(new SeatingPlace(rowName, number, category, !isReserved));
            }

            rows[rowName] = new Row(rowName, seatingPlaces);
        }

        return rows;
    }
}
