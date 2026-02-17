package org.weaveit.seatingplacesuggestions;

public interface AuditoriumSeatingArrangements {
    AuditoriumSeatingArrangement findByShowId(String showId);
}
