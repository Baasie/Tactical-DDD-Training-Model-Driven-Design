using System.Collections.Generic;
using NFluent;
using NUnit.Framework;
using SeatsSuggestions;

namespace SeatsSuggestions.Tests.UnitTests;

[TestFixture]
public class RowTest
{
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
