using System.Collections.Generic;
using NFluent;
using NUnit.Framework;
using SeatsSuggestions;

namespace SeatsSuggestions.Tests.UnitTests;

[TestFixture]
public class RowTest
{
    /// <summary>
    /// Seats should be suggested starting from the middle of the row, working outward.
    /// For a row with 10 seats, the middle is between seats 5 and 6.
    /// Seats closer to the middle are preferred.
    ///
    /// Row layout for this test:
    ///      1   2   3   4   5   6   7   8   9  10
    ///  A:  2   2   1  (1)  1   1   1  (1)  2   2
    ///
    /// Available FIRST category seats: A3, A5, A6, A7
    /// Middle of row: between 5 and 6
    /// For party of 1: A5 (the seat closest to middle)
    /// </summary>
    [Test]
    public void Should_suggest_seats_starting_from_middle_of_row()
    {
        var partySize = 1;

        // Row with 10 seats - middle is between seat 5 and 6
        var a1 = new SeatingPlace("A", 1, PricingCategory.Second, SeatingPlaceAvailability.Available);
        var a2 = new SeatingPlace("A", 2, PricingCategory.Second, SeatingPlaceAvailability.Available);
        var a3 = new SeatingPlace("A", 3, PricingCategory.First, SeatingPlaceAvailability.Available);
        var a4 = new SeatingPlace("A", 4, PricingCategory.First, SeatingPlaceAvailability.Reserved);
        var a5 = new SeatingPlace("A", 5, PricingCategory.First, SeatingPlaceAvailability.Available);
        var a6 = new SeatingPlace("A", 6, PricingCategory.First, SeatingPlaceAvailability.Available);
        var a7 = new SeatingPlace("A", 7, PricingCategory.First, SeatingPlaceAvailability.Available);
        var a8 = new SeatingPlace("A", 8, PricingCategory.First, SeatingPlaceAvailability.Reserved);
        var a9 = new SeatingPlace("A", 9, PricingCategory.Second, SeatingPlaceAvailability.Available);
        var a10 = new SeatingPlace("A", 10, PricingCategory.Second, SeatingPlaceAvailability.Available);

        var row = new Row("A", new List<SeatingPlace> { a1, a2, a3, a4, a5, a6, a7, a8, a9, a10 });

        var seatingOption = row.SuggestSeatingOption(partySize, PricingCategory.First);

        // A5 is the seat closest to the middle (between 5 and 6)
        Check.That(seatingOption).IsInstanceOf<SeatingOptionIsSuggested>();
        var suggested = (SeatingOptionIsSuggested)seatingOption;
        Check.That(suggested.Seats()).ContainsExactly(a5);
    }

    [TestFixture]
    public class Immutability
    {
        [Test]
        public void Allocate_returns_new_instance_and_does_not_mutate_original()
        {
            var seat1 = new SeatingPlace("A", 1, PricingCategory.First, SeatingPlaceAvailability.Available);
            var seat2 = new SeatingPlace("A", 2, PricingCategory.First, SeatingPlaceAvailability.Available);
            var original = new Row("A", new List<SeatingPlace> { seat1, seat2 });

            var allocated = original.Allocate(new List<SeatingPlace> { seat1 });

            Check.That(allocated).Not.IsSameReferenceAs(original);
            Check.That(original.SeatingPlaces[0].IsAvailable()).IsTrue();
            Check.That(allocated.SeatingPlaces[0].IsAvailable()).IsFalse();
        }

        [Test]
        public void Original_list_mutation_does_not_affect_row()
        {
            var seat1 = new SeatingPlace("A", 1, PricingCategory.First, SeatingPlaceAvailability.Available);
            var seat2 = new SeatingPlace("A", 2, PricingCategory.First, SeatingPlaceAvailability.Available);
            var mutableList = new List<SeatingPlace> { seat1 };
            var row = new Row("A", mutableList);

            mutableList.Add(seat2);

            Check.That(row.SeatingPlaces).HasSize(1);
        }
    }

    [TestFixture]
    public class ValueEquality
    {
        [Test]
        public void Two_rows_with_same_values_are_equal()
        {
            var seat = new SeatingPlace("A", 1, PricingCategory.First, SeatingPlaceAvailability.Available);
            var row1 = new Row("A", new List<SeatingPlace> { seat });
            var row2 = new Row("A", new List<SeatingPlace> { seat });

            Check.That(row1).IsEqualTo(row2);
            Check.That(row1.GetHashCode()).IsEqualTo(row2.GetHashCode());
        }

        [Test]
        public void Two_rows_with_different_seats_are_not_equal()
        {
            var seat1 = new SeatingPlace("A", 1, PricingCategory.First, SeatingPlaceAvailability.Available);
            var seat2 = new SeatingPlace("A", 2, PricingCategory.First, SeatingPlaceAvailability.Available);
            var row1 = new Row("A", new List<SeatingPlace> { seat1 });
            var row2 = new Row("A", new List<SeatingPlace> { seat2 });

            Check.That(row1).IsNotEqualTo(row2);
        }

        [Test]
        public void Row_and_its_allocated_version_are_not_equal()
        {
            var seat = new SeatingPlace("A", 1, PricingCategory.First, SeatingPlaceAvailability.Available);
            var original = new Row("A", new List<SeatingPlace> { seat });
            var allocated = original.Allocate(new List<SeatingPlace> { seat });

            Check.That(original).IsNotEqualTo(allocated);
        }
    }
}
