package org.weaveit.seatingplacesuggestions;

/**
 * Value Object representing how far a seating place is from the center of its row.
 *
 * Domain concept: "Seats closer to the center provide a better viewing experience"
 * Lower distance = closer to center = more desirable
 */
public record DistanceFromRowCenter(int value) implements Comparable<DistanceFromRowCenter> {

    public static DistanceFromRowCenter of(int seatNumber, int rowSize) {
        int center = rowSize / 2;

        if (rowSize % 2 == 0) {
            // Even row: center is between two seats (e.g., row of 10 → center between 5 and 6)
            return seatNumber > center
                    ? new DistanceFromRowCenter(Math.abs(seatNumber - center))
                    : new DistanceFromRowCenter(Math.abs(seatNumber - (center + 1)));
        }

        // Odd row: center is a single seat
        return new DistanceFromRowCenter(Math.abs(seatNumber - (center + 1)));
    }

    @Override
    public int compareTo(DistanceFromRowCenter other) {
        return Integer.compare(this.value, other.value);
    }
}
