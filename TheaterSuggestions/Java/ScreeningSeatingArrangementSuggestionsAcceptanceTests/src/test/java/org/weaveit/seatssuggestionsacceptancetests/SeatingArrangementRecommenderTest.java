package org.weaveit.seatssuggestionsacceptancetests;

import org.weaveit.externaldependencies.auditoriumlayoutrepository.AuditoriumLayoutRepository;
import org.weaveit.externaldependencies.reservationsprovider.ReservationsProvider;
import org.weaveit.seatingplacesuggestions.*;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;
import static org.assertj.core.api.Assertions.assertThat;

class SeatingArrangementRecommenderTest {

    @Test
    void suggest_one_seatingPlace_when_Auditorium_contains_one_available_seatingPlace() throws IOException {
        // Ford Auditorium-1
        //       1   2   3   4   5   6   7   8   9  10
        //  A : (2) (2)  1  (1) (1) (1) (1) (1) (2) (2)
        //  B : (2) (2) (1) (1) (1) (1) (1) (1) (2) (2)
        final String showId = "1";
        final int partyRequested = 1;

        var auditoriumSeatingArrangements =
                new AuditoriumSeatingArrangements(new AuditoriumLayoutRepository(), new ReservationsProvider());
        var seatingArrangementRecommender = new SeatingArrangementRecommender(auditoriumSeatingArrangements);
        var suggestionsAreMade = seatingArrangementRecommender.makeSuggestions(showId, partyRequested);

        assertThat(suggestionsAreMade.seatNames(PricingCategory.FIRST)).containsExactly("A3");
    }

    @Test
    void return_SuggestionNotAvailable_when_Auditorium_has_all_its_seatingPlaces_reserved() throws IOException {
        // Madison Auditorium-5
        //      1   2   3   4   5   6   7   8   9  10
        // A : (2) (2) (1) (1) (1) (1) (1) (1) (2) (2)
        // B : (2) (2) (1) (1) (1) (1) (1) (1) (2) (2)
        final String showId = "5";
        final int partyRequested = 1;

        var auditoriumSeatingArrangements =
                new AuditoriumSeatingArrangements(new AuditoriumLayoutRepository(), new ReservationsProvider());
        var seatingArrangementRecommender = new SeatingArrangementRecommender(auditoriumSeatingArrangements);
        var suggestionsAreMade = seatingArrangementRecommender.makeSuggestions(showId, partyRequested);

        assertEquals(partyRequested, suggestionsAreMade.partyRequested(), "Party requested should match");
        assertEquals(showId, suggestionsAreMade.showId(), "Show ID should match");
        assertInstanceOf(SuggestionsAreNotAvailable.class, suggestionsAreMade, "Suggestions made should be an instance of SuggestionNotAvailable");
    }

    @Test
    void suggest_adjacent_seats_for_a_party_of_two() throws IOException {
        // Lincoln-17
        //
        //     1   2   3   4   5   6   7   8   9  10
        //  A: 2   2   1   1   1   1   1   1   2   2
        //  B: 2   2   1   1   1   1   1   1   2   2
        final String showId = "17";
        final int partyRequested = 2;

        var auditoriumSeatingArrangements =
                new AuditoriumSeatingArrangements(new AuditoriumLayoutRepository(), new ReservationsProvider());
        var seatingArrangementRecommender = new SeatingArrangementRecommender(auditoriumSeatingArrangements);
        var suggestionsAreMade = seatingArrangementRecommender.makeSuggestions(showId, partyRequested);

        assertThat(suggestionsAreMade.seatNames(PricingCategory.SECOND)).containsExactly("A1-A2", "A9-A10", "B1-B2");
    }


    @Test
    void suggest_three_availabilities_per_PricingCategory() throws IOException {
        // New Amsterdam-18
        //     1   2   3   4   5   6   7   8   9  10
        //  A: 2   2   1   1   1   1   1   1   2   2
        //  B: 2   2   1   1   1   1   1   1   2   2
        //  C: 2   2   2   2   2   2   2   2   2   2
        //  D: 2   2   2   2   2   2   2   2   2   2
        //  E: 3   3   3   3   3   3   3   3   3   3
        //  F: 3   3   3   3   3   3   3   3   3   3
        final String showId = "18";
        final int partyRequested = 1;

        var auditoriumSeatingArrangements =
                new AuditoriumSeatingArrangements(new AuditoriumLayoutRepository(), new ReservationsProvider());
        var seatingArrangementRecommender = new SeatingArrangementRecommender(auditoriumSeatingArrangements);
        var suggestionsAreMade = seatingArrangementRecommender.makeSuggestions(showId, partyRequested);

        // Middle-out ordering: seats closest to middle (5.5) come first
        assertThat(suggestionsAreMade.seatNames(PricingCategory.FIRST)).containsExactly("A5", "A6", "A4");
        assertThat(suggestionsAreMade.seatNames(PricingCategory.SECOND)).containsExactly("A2", "A9", "A1");
        assertThat(suggestionsAreMade.seatNames(PricingCategory.THIRD)).containsExactly("E5", "E6", "E4");
        assertThat(suggestionsAreMade.seatNames(PricingCategory.MIXED)).containsExactly("A5", "A6", "A4");
    }

    @Test
    public void should_offer_seats_nearer_the_middle_of_a_row() throws IOException {
        // Mogador Auditorium-9
        //
        //    1   2   3   4   5   6   7   8   9  10
        // A: 2   2   1   1  (1) (1) (1) (1)  2   2
        // B: 2   2   1   1   1   1   1   1   2   2
        //
        // Available FIRST category: A3, A4 (row A) and B3-B8 (row B)
        // Middle of row: between seats 5 and 6
        final String showId = "9";
        final int partyRequested = 1;

        var auditoriumSeatingArrangements =
                new AuditoriumSeatingArrangements(new AuditoriumLayoutRepository(), new ReservationsProvider());
        var seatingArrangementRecommender = new SeatingArrangementRecommender(auditoriumSeatingArrangements);
        var suggestionsAreMade = seatingArrangementRecommender.makeSuggestions(showId, partyRequested);

        // Order matters: A4 before A3 (A4 is closer to middle), then B5 (middle of row B)
        assertThat(suggestionsAreMade.seatNames(PricingCategory.FIRST)).containsExactly("A4", "A3", "B5");
    }

    @Test
    public void offer_adjacent_seats_nearer_the_middle_of_a_row_when_it_is_possible() throws IOException {
        // Dock Street Auditorium-3
        //
        //      1   2   3   4   5   6   7   8   9  10
        // A:  (2) (2) (1) (1) (1)  1   1   1   2   2
        // B:   2   2   1   1  (1) (1) (1) (1)  2   2
        // C:   2   2   2   2   2   2   2   2   2   2
        // D:   2   2   2   2   2   2   2   2   2   2
        // E:   3   3   3   3   3   3   3   3   3   3
        // F:   3   3   3   3   3   3   3   3   3   3
        final String showId = "3";
        final int partyRequested = 4;

        var auditoriumSeatingArrangements =
                new AuditoriumSeatingArrangements(new AuditoriumLayoutRepository(), new ReservationsProvider());
        var seatingArrangementRecommender = new SeatingArrangementRecommender(auditoriumSeatingArrangements);
        var suggestionsAreMade = seatingArrangementRecommender.makeSuggestions(showId, partyRequested);

        assertThat(suggestionsAreMade.seatNames(PricingCategory.FIRST)).isEmpty();
        assertThat(suggestionsAreMade.seatNames(PricingCategory.SECOND)).containsExactly("C4-C5-C6-C7", "D4-D5-D6-D7");
        assertThat(suggestionsAreMade.seatNames(PricingCategory.THIRD)).containsExactly("E4-E5-E6-E7", "F4-F5-F6-F7");
        assertThat(suggestionsAreMade.seatNames(PricingCategory.MIXED)).containsExactly("A6-A7-A8-A9", "B1-B2-B3-B4", "C4-C5-C6-C7");
    }

    @Test
    public void should_offer_3_adjacent_seats_nearer_the_middle_of_a_row_when_it_is_possible() throws IOException {
        // Dock Street Auditorium-3
        //
        //      1   2   3   4   5   6   7   8   9  10
        // A : (2) (2) (1) (1) (1)  1   1   1   2   2
        // B :  2   2   1   1  (1) (1) (1) (1)  2   2
        // C :  2   2   2   2   2   2   2   2   2   2
        // D :  2   2   2   2   2   2   2   2   2   2
        // E :  3   3   3   3   3   3   3   3   3   3
        // F :  3   3   3   3   3   3   3   3   3   3
        final String showId = "3";
        final int partyRequested = 3;

        var auditoriumSeatingArrangements =
                new AuditoriumSeatingArrangements(new AuditoriumLayoutRepository(), new ReservationsProvider());
        var seatingArrangementRecommender = new SeatingArrangementRecommender(auditoriumSeatingArrangements);
        var suggestionsAreMade = seatingArrangementRecommender.makeSuggestions(showId, partyRequested);

        assertThat(suggestionsAreMade.seatNames(PricingCategory.FIRST)).containsExactly("A6-A7-A8");
        assertThat(suggestionsAreMade.seatNames(PricingCategory.SECOND)).containsExactly("C4-C5-C6", "C7-C8-C9", "C1-C2-C3");
        assertThat(suggestionsAreMade.seatNames(PricingCategory.THIRD)).containsExactly("E4-E5-E6", "E7-E8-E9", "E1-E2-E3");
        assertThat(suggestionsAreMade.seatNames(PricingCategory.MIXED)).containsExactly("A6-A7-A8", "B2-B3-B4", "C4-C5-C6");
    }

}
