using ExternalDependencies.AuditoriumLayoutRepository;
using ExternalDependencies.ReservationsProvider;
using NFluent;
using NUnit.Framework;
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
        public void Suggest_two_seatingPlaces_when_Auditorium_contains_all_available_seatingPlaces()
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
            Check.That(seatNames).ContainsExactly("A2", "A9", "A1", "A10", "B2", "B9");
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
    }
}
