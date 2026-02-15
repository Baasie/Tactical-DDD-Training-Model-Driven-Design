package org.weaveit.seatingplacesuggestions;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public record Row(String name, List<SeatingPlace> seatingPlaces) {

    public Row {
        seatingPlaces = List.copyOf(seatingPlaces);
    }

    public SeatingOption suggestSeatingOption(int partyRequested, PricingCategory pricingCategory) {
        var selectedSeats = new ArrayList<SeatingPlace>();
        int rowSize = seatingPlaces.size();

        var availableSeatsCloserToCenter = seatingPlaces.stream()
                .filter(SeatingPlace::isAvailable)
                .filter(seat -> seat.matchCategory(pricingCategory))
                .sorted(Comparator.comparing(seat -> DistanceFromRowCenter.of(seat.number(), rowSize)))
                .toList();

        for (var seat : availableSeatsCloserToCenter) {
            selectedSeats.add(seat);

            if (selectedSeats.size() == partyRequested) {
                return new SeatingOptionIsSuggested(partyRequested, pricingCategory, selectedSeats);
            }
        }

        return new SeatingOptionIsNotAvailable(partyRequested, pricingCategory);
    }

    public Row allocate(List<SeatingPlace> seatsToAllocate) {
        Set<String> seatNamesToAllocate = seatsToAllocate.stream()
                .map(SeatingPlace::name)
                .collect(Collectors.toSet());

        List<SeatingPlace> updatedSeats = new ArrayList<>();
        for (var seat : seatingPlaces) {
            if (seatNamesToAllocate.contains(seat.name())) {
                updatedSeats.add(seat.allocate());
            } else {
                updatedSeats.add(seat);
            }
        }
        return new Row(name, updatedSeats);
    }
}
