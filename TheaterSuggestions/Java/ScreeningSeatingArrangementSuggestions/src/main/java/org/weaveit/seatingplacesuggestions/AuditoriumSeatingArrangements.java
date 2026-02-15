package org.weaveit.seatingplacesuggestions;

import org.weaveit.externaldependencies.auditoriumlayoutrepository.AuditoriumLayoutRepository;
import org.weaveit.externaldependencies.reservationsprovider.ReservationsProvider;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class AuditoriumSeatingArrangements {
    private final AuditoriumLayoutRepository auditoriumLayoutRepository;
    private final ReservationsProvider reservationsProvider;

    public AuditoriumSeatingArrangements(AuditoriumLayoutRepository auditoriumLayoutRepository, ReservationsProvider reservationsProvider) {
        this.auditoriumLayoutRepository = auditoriumLayoutRepository;
        this.reservationsProvider = reservationsProvider;
    }

    public Map<String, Row> findByShowId(String showId) {
        var auditoriumDto = auditoriumLayoutRepository.findByShowId(showId);
        var reservedSeatsDto = reservationsProvider.getReservedSeats(showId);

        var rows = new LinkedHashMap<String, Row>();

        for (var rowEntry : auditoriumDto.rows().entrySet()) {
            var rowName = rowEntry.getKey();
            List<SeatingPlace> seatingPlaces = new ArrayList<>();

            for (var seatDto : rowEntry.getValue()) {
                var number = Integer.parseInt(seatDto.name().substring(1));
                var category = PricingCategory.valueOf(seatDto.category());
                var isReserved = reservedSeatsDto.reservedSeats().contains(seatDto.name());

                seatingPlaces.add(new SeatingPlace(rowName, number, category, !isReserved));
            }

            rows.put(rowName, new Row(rowName, seatingPlaces));
        }

        return rows;
    }
}
