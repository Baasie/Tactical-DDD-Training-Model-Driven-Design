using ExternalDependencies.AuditoriumLayoutRepository;
using ExternalDependencies.ReservationsProvider;
using SeatsSuggestions;

namespace ScreeningSeatingArrangementSuggestions.Api;

public class AuditoriumSeatingArrangements : IAuditoriumSeatingArrangements
{
    private readonly ReservationsProvider _reservedSeatsRepository;
    private readonly AuditoriumLayoutRepository _auditoriumLayoutRepository;

    public AuditoriumSeatingArrangements(AuditoriumLayoutRepository auditoriumLayoutRepository, ReservationsProvider reservationsProvider)
    {
        _auditoriumLayoutRepository = auditoriumLayoutRepository;
        _reservedSeatsRepository = reservationsProvider;
    }

    public AuditoriumSeatingArrangement FindByShowId(string showId)
    {
        return Adapt(
            _auditoriumLayoutRepository.FindByShowId(showId),
            _reservedSeatsRepository.GetReservedSeats(showId));
    }

    private AuditoriumSeatingArrangement Adapt(AuditoriumDto auditoriumDto, ReservedSeatsDto reservedSeatsDto)
    {
        var rows = new Dictionary<string, Row>();

        foreach (var rowDto in auditoriumDto.Rows)
        {
            var seats = new List<SeatingPlace>();

            foreach (var seatDto in rowDto.Value)
            {
                var rowName = rowDto.Key;
                var number = ExtractNumber(seatDto.Name);
                var pricingCategory = PricingCategoryExtensions.FromValue(seatDto.Category);

                var isReserved = reservedSeatsDto.ReservedSeats?.Contains(seatDto.Name) ?? false;

                seats.Add(new SeatingPlace(
                    rowName,
                    number,
                    pricingCategory,
                    isReserved ? SeatingPlaceAvailability.Reserved : SeatingPlaceAvailability.Available));
            }

            rows[rowDto.Key] = new Row(rowDto.Key, seats);
        }

        return new AuditoriumSeatingArrangement(rows);
    }

    private static int ExtractNumber(string name)
    {
        return int.Parse(name.Substring(1));
    }
}
