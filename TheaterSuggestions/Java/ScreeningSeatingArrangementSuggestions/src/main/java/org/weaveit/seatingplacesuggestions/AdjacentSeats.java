package org.weaveit.seatingplacesuggestions;

import java.util.List;

/**
 * Value Object representing a contiguous group of seats that can be offered together.
 *
 * Domain concept: "Adjacent seating" — a party should sit together in consecutive seats,
 * and the group closest to the center of the row is preferred.
 */
public record AdjacentSeats(List<SeatingPlace> seats, double distanceFromRowCenter)
        implements Comparable<AdjacentSeats> {

    public AdjacentSeats {
        seats = List.copyOf(seats);
    }

    public static AdjacentSeats of(List<SeatingPlace> window, int rowSize) {
        double windowCenter = (window.get(0).number() + window.get(window.size() - 1).number()) / 2.0;
        double rowCenter = (rowSize + 1) / 2.0;
        return new AdjacentSeats(window, Math.abs(windowCenter - rowCenter));
    }

    @Override
    public int compareTo(AdjacentSeats other) {
        int distanceCompare = Double.compare(this.distanceFromRowCenter, other.distanceFromRowCenter);
        if (distanceCompare != 0) return distanceCompare;
        return Integer.compare(this.seats.get(0).number(), other.seats.get(0).number());
    }
}
