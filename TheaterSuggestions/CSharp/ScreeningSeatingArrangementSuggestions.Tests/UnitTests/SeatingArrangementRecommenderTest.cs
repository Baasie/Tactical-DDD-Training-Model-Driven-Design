using System.Collections.Generic;
using NFluent;
using NUnit.Framework;
using SeatsSuggestions;

namespace SeatsSuggestions.Tests.UnitTests;

[TestFixture]
public class SeatingArrangementRecommenderTest
{
    [Test]
    public void Can_be_tested_with_in_memory_stub_without_infrastructure()
    {
        //     1   2   3   4   5
        // A:  1   1   1   1   1
        var seats = new List<SeatingPlace>
        {
            new("A", 1, PricingCategory.First, SeatingPlaceAvailability.Available),
            new("A", 2, PricingCategory.First, SeatingPlaceAvailability.Available),
            new("A", 3, PricingCategory.First, SeatingPlaceAvailability.Available),
            new("A", 4, PricingCategory.First, SeatingPlaceAvailability.Available),
            new("A", 5, PricingCategory.First, SeatingPlaceAvailability.Available)
        };
        var rows = new Dictionary<string, Row> { { "A", new Row("A", seats) } };
        var arrangement = new AuditoriumSeatingArrangement(rows);

        var stub = new StubAuditoriumSeatingArrangements(arrangement);
        var recommender = new SeatingArrangementRecommender(stub);

        var suggestions = recommender.MakeSuggestions("any-show", 2);

        Check.That(suggestions.MatchExpectations()).IsTrue();
        Check.That(suggestions.SeatNames(PricingCategory.First)).ContainsExactly("A2-A3", "A4-A5");
    }

    private class StubAuditoriumSeatingArrangements : IAuditoriumSeatingArrangements
    {
        private readonly AuditoriumSeatingArrangement _arrangement;

        public StubAuditoriumSeatingArrangements(AuditoriumSeatingArrangement arrangement)
        {
            _arrangement = arrangement;
        }

        public AuditoriumSeatingArrangement FindByShowId(string showId) => _arrangement;
    }
}
