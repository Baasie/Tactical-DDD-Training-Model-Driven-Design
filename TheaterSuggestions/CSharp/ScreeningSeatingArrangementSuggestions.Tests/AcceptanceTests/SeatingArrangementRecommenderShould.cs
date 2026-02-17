using ExternalDependencies.AuditoriumLayoutRepository;
using ExternalDependencies.ReservationsProvider;
using NFluent;
using NUnit.Framework;
using ScreeningSeatingArrangementSuggestions.Api;
using SeatsSuggestions;

namespace SeatsSuggestions.Tests.AcceptanceTests
{
    [TestFixture]
    public class SeatingArrangementRecommenderShould
    {
        [Test]
        public void Suggest_one_seatingPlace_when_Auditorium_contains_one_available_seatingPlace()
        {
            // Ford Auditorium-1
            //
            //       1   2   3   4   5   6   7   8   9  10
            //  A : (2) (2)  1  (1) (1) (1) (1) (1) (2) (2)
            //  B : (2) (2) (1) (1) (1) (1) (1) (1) (2) (2)
            const string showId = "1";
            const int partyRequested = 1;

            var auditoriumSeatingArrangements =
                new AuditoriumSeatingArrangements(new AuditoriumLayoutRepository(), new ReservationsProvider());
            var seatingArrangementRecommender = new SeatingArrangementRecommender(auditoriumSeatingArrangements);
            var suggestionsAreMade = seatingArrangementRecommender.MakeSuggestions(showId, partyRequested);

            Check.That(suggestionsAreMade.SeatNames(PricingCategory.First)).ContainsExactly("A3");
        }

        [Test]
        public void Return_SuggestionNotAvailable_when_Auditorium_has_all_its_seatingPlaces_reserved()
        {
            // Madison Auditorium-5
            //
            //      1   2   3   4   5   6   7   8   9  10
            // A : (2) (2) (1) (1) (1) (1) (1) (1) (2) (2)
            // B : (2) (2) (1) (1) (1) (1) (1) (1) (2) (2)
            const string showId = "5";
            const int partyRequested = 1;

            var auditoriumSeatingArrangements =
                new AuditoriumSeatingArrangements(new AuditoriumLayoutRepository(), new ReservationsProvider());
            var seatingArrangementRecommender = new SeatingArrangementRecommender(auditoriumSeatingArrangements);
            var suggestionsAreMade = seatingArrangementRecommender.MakeSuggestions(showId, partyRequested);

            Check.That(suggestionsAreMade.PartyRequested).IsEqualTo(partyRequested);
            Check.That(suggestionsAreMade.ShowId).IsEqualTo(showId);

            Check.That(suggestionsAreMade).IsInstanceOf<SuggestionsAreNotAvailable>();
        }

        [Test]
        public void Suggest_adjacent_seats_for_a_party_of_two()
        {
            // Lincoln Auditorium-17
            //     1   2   3   4   5   6   7   8   9  10
            //  A: 2   2   1   1   1   1   1   1   2   2
            //  B: 2   2   1   1   1   1   1   1   2   2
            const string showId = "17";
            const int partyRequested = 2;

            var auditoriumSeatingArrangements =
                new AuditoriumSeatingArrangements(new AuditoriumLayoutRepository(), new ReservationsProvider());
            var seatingArrangementRecommender = new SeatingArrangementRecommender(auditoriumSeatingArrangements);
            var suggestionsAreMade = seatingArrangementRecommender.MakeSuggestions(showId, partyRequested);

            var seatNames = suggestionsAreMade.SeatNames(PricingCategory.Second);
            Check.That(seatNames).ContainsExactly("A1-A2", "A9-A10", "B1-B2");
        }

        [Test]
        public void Suggest_three_availabilities_per_PricingCategory()
        {
            // New Amsterdam-18
            //
            //     1   2   3   4   5   6   7   8   9  10
            //  A: 2   2   1   1   1   1   1   1   2   2
            //  B: 2   2   1   1   1   1   1   1   2   2
            //  C: 2   2   2   2   2   2   2   2   2   2
            //  D: 2   2   2   2   2   2   2   2   2   2
            //  E: 3   3   3   3   3   3   3   3   3   3
            //  F: 3   3   3   3   3   3   3   3   3   3
            const string showId = "18";
            const int partyRequested = 1;
            
            var auditoriumSeatingArrangements =
                new AuditoriumSeatingArrangements(new AuditoriumLayoutRepository(), new ReservationsProvider());
            var seatingArrangementRecommender = new SeatingArrangementRecommender(auditoriumSeatingArrangements);
            var suggestionsAreMade = seatingArrangementRecommender.MakeSuggestions(showId, partyRequested);

            // Middle-out ordering: seats closest to middle (5.5) come first
            Check.That(suggestionsAreMade.SeatNames(PricingCategory.First)).ContainsExactly("A5", "A6", "A4");
            Check.That(suggestionsAreMade.SeatNames(PricingCategory.Second)).ContainsExactly("A2", "A9", "A1");
            Check.That(suggestionsAreMade.SeatNames(PricingCategory.Third)).ContainsExactly("E5", "E6", "E4");
            Check.That(suggestionsAreMade.SeatNames(PricingCategory.Mixed)).ContainsExactly("A5", "A6", "A4");
        }

        
        [Test]
        public void Should_offer_seats_nearer_the_middle_of_a_row()
        {
            // Mogador Auditorium-9
            //
            //    1   2   3   4   5   6   7   8   9  10
            // A: 2   2   1   1  (1) (1) (1) (1)  2   2
            // B: 2   2   1   1   1   1   1   1   2   2
            //
            // Available FIRST category: A3, A4 (row A) and B3-B8 (row B)
            // Middle of row: between seats 5 and 6
            const string showId = "9";
            const int partyRequested = 1;

            var auditoriumSeatingArrangements =
                new AuditoriumSeatingArrangements(new AuditoriumLayoutRepository(), new ReservationsProvider());
            var seatingArrangementRecommender = new SeatingArrangementRecommender(auditoriumSeatingArrangements);
            var suggestionsAreMade = seatingArrangementRecommender.MakeSuggestions(showId, partyRequested);

            // Order matters: A4 before A3 (A4 is closer to middle), then B5 (middle of row B)
            Check.That(suggestionsAreMade.SeatNames(PricingCategory.First)).ContainsExactly("A4", "A3", "B5");
        }

        [Test]
        public void Offer_adjacent_seats_nearer_the_middle_of_a_row_when_it_is_possible()
        {
            // Dock Street Auditorium-3
            //
            //      1   2   3   4   5   6   7   8   9  10
            // A:  (2) (2) (1) (1) (1)  1   1   1   2   2
            // B:   2   2   1   1  (1) (1) (1) (1)  2   2
            // C:   2   2   2   2   2   2   2   2   2   2
            // D:   2   2   2   2   2   2   2   2   2   2
            // E:   3   3   3   3   3   3   3   3   3   3
            // F:   3   3   3   3   3   3   3   3   3   3
            const string showId = "3";
            const int partyRequested = 4;

            var auditoriumSeatingArrangements =
                new AuditoriumSeatingArrangements(new AuditoriumLayoutRepository(), new ReservationsProvider());
            var seatingArrangementRecommender = new SeatingArrangementRecommender(auditoriumSeatingArrangements);
            var suggestionsAreMade = seatingArrangementRecommender.MakeSuggestions(showId, partyRequested);

            Check.That(suggestionsAreMade.SeatNames(PricingCategory.First)).IsEmpty();
            Check.That(suggestionsAreMade.SeatNames(PricingCategory.Second)).ContainsExactly("C4-C5-C6-C7", "D4-D5-D6-D7");
            Check.That(suggestionsAreMade.SeatNames(PricingCategory.Third)).ContainsExactly("E4-E5-E6-E7", "F4-F5-F6-F7");
            Check.That(suggestionsAreMade.SeatNames(PricingCategory.Mixed)).ContainsExactly("A6-A7-A8-A9", "B1-B2-B3-B4", "C4-C5-C6-C7");
        }

        [Test]
        public void Should_offer_3_adjacent_seats_nearer_the_middle_of_a_row_when_it_is_possible()
        {
            // Dock Street Auditorium-3
            //
            //      1   2   3   4   5   6   7   8   9  10
            // A : (2) (2) (1) (1) (1)  1   1   1   2   2
            // B :  2   2   1   1  (1) (1) (1) (1)  2   2
            // C :  2   2   2   2   2   2   2   2   2   2
            // D :  2   2   2   2   2   2   2   2   2   2
            // E :  3   3   3   3   3   3   3   3   3   3
            // F :  3   3   3   3   3   3   3   3   3   3
            const string showId = "3";
            const int partyRequested = 3;

            var auditoriumSeatingArrangements =
                new AuditoriumSeatingArrangements(new AuditoriumLayoutRepository(), new ReservationsProvider());
            var seatingArrangementRecommender = new SeatingArrangementRecommender(auditoriumSeatingArrangements);
            var suggestionsAreMade = seatingArrangementRecommender.MakeSuggestions(showId, partyRequested);

            Check.That(suggestionsAreMade.SeatNames(PricingCategory.First)).ContainsExactly("A6-A7-A8");
            Check.That(suggestionsAreMade.SeatNames(PricingCategory.Second)).ContainsExactly("C4-C5-C6", "C7-C8-C9", "C1-C2-C3");
            Check.That(suggestionsAreMade.SeatNames(PricingCategory.Third)).ContainsExactly("E4-E5-E6", "E7-E8-E9", "E1-E2-E3");
            Check.That(suggestionsAreMade.SeatNames(PricingCategory.Mixed)).ContainsExactly("A6-A7-A8", "B2-B3-B4", "C4-C5-C6");
        }
    }
}
