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
        int rowSize = seatingPlaces.size();

        var availableSeats = seatingPlaces.stream()
                .filter(SeatingPlace::isAvailable)
                .filter(seat -> seat.matchCategory(pricingCategory))
                .toList();

        if (availableSeats.size() < partyRequested) {
            return new SeatingOptionIsNotAvailable(partyRequested, pricingCategory);
        }

        // Find contiguous blocks of available seats
        var contiguousBlocks = new ArrayList<List<SeatingPlace>>();
        var currentBlock = new ArrayList<SeatingPlace>();
        currentBlock.add(availableSeats.get(0));

        for (int i = 1; i < availableSeats.size(); i++) {
            if (availableSeats.get(i).number() == availableSeats.get(i - 1).number() + 1) {
                currentBlock.add(availableSeats.get(i));
            } else {
                contiguousBlocks.add(List.copyOf(currentBlock));
                currentBlock.clear();
                currentBlock.add(availableSeats.get(i));
            }
        }
        contiguousBlocks.add(List.copyOf(currentBlock));

        // Find the best adjacent seating option
        return contiguousBlocks.stream()
                .filter(block -> block.size() >= partyRequested)
                .flatMap(block -> {
                    var windows = new ArrayList<AdjacentSeats>();
                    for (int i = 0; i <= block.size() - partyRequested; i++) {
                        windows.add(AdjacentSeats.of(block.subList(i, i + partyRequested), rowSize));
                    }
                    return windows.stream();
                })
                .min(Comparator.naturalOrder())
                .map(adj -> (SeatingOption) new SeatingOptionIsSuggested(partyRequested, pricingCategory, adj.seats()))
                .orElse(new SeatingOptionIsNotAvailable(partyRequested, pricingCategory));
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
