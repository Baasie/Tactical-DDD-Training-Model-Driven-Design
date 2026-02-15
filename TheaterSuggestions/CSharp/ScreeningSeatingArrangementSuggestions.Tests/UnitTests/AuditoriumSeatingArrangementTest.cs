using System.Collections.Generic;
using NFluent;
using NUnit.Framework;
using SeatsSuggestions;

namespace SeatsSuggestions.Tests.UnitTests;

[TestFixture]
public class AuditoriumSeatingArrangementTest
{
    [TestFixture]
    public class Immutability
    {
        [Test]
        public void Allocate_returns_new_instance_and_does_not_mutate_original()
        {
            var seat = new SeatingPlace("A", 1, PricingCategory.First, SeatingPlaceAvailability.Available);
            var row = new Row("A", new List<SeatingPlace> { seat });
            var original = new AuditoriumSeatingArrangement(new Dictionary<string, Row> { { "A", row } });

            var allocated = original.Allocate(new List<SeatingPlace> { seat });

            Check.That(allocated).Not.IsSameReferenceAs(original);
            Check.That(original.Rows["A"].SeatingPlaces[0].IsAvailable()).IsTrue();
            Check.That(allocated.Rows["A"].SeatingPlaces[0].IsAvailable()).IsFalse();
        }

        [Test]
        public void Original_map_mutation_does_not_affect_arrangement()
        {
            var seat = new SeatingPlace("A", 1, PricingCategory.First, SeatingPlaceAvailability.Available);
            var rowA = new Row("A", new List<SeatingPlace> { seat });
            var rowB = new Row("B", new List<SeatingPlace> { seat });
            var mutableDict = new Dictionary<string, Row> { { "A", rowA } };
            var arrangement = new AuditoriumSeatingArrangement(mutableDict);

            mutableDict["B"] = rowB;

            Check.That(arrangement.Rows).HasSize(1);
        }

        [Test]
        public void Preserves_row_order()
        {
            var seat = new SeatingPlace("A", 1, PricingCategory.First, SeatingPlaceAvailability.Available);
            var rowA = new Row("A", new List<SeatingPlace> { seat });
            var rowB = new Row("B", new List<SeatingPlace> { seat });
            var rowC = new Row("C", new List<SeatingPlace> { seat });
            var orderedDict = new Dictionary<string, Row>
            {
                { "A", rowA },
                { "B", rowB },
                { "C", rowC }
            };

            var arrangement = new AuditoriumSeatingArrangement(orderedDict);

            Check.That(arrangement.Rows.Keys).ContainsExactly("A", "B", "C");
        }
    }

    [TestFixture]
    public class ValueEquality
    {
        [Test]
        public void Two_arrangements_with_same_values_are_equal()
        {
            var seat = new SeatingPlace("A", 1, PricingCategory.First, SeatingPlaceAvailability.Available);
            var row = new Row("A", new List<SeatingPlace> { seat });
            var arrangement1 = new AuditoriumSeatingArrangement(new Dictionary<string, Row> { { "A", row } });
            var arrangement2 = new AuditoriumSeatingArrangement(new Dictionary<string, Row> { { "A", row } });

            Check.That(arrangement1).IsEqualTo(arrangement2);
            Check.That(arrangement1.GetHashCode()).IsEqualTo(arrangement2.GetHashCode());
        }

        [Test]
        public void Two_arrangements_with_different_rows_are_not_equal()
        {
            var seat1 = new SeatingPlace("A", 1, PricingCategory.First, SeatingPlaceAvailability.Available);
            var seat2 = new SeatingPlace("A", 2, PricingCategory.First, SeatingPlaceAvailability.Available);
            var row1 = new Row("A", new List<SeatingPlace> { seat1 });
            var row2 = new Row("A", new List<SeatingPlace> { seat2 });
            var arrangement1 = new AuditoriumSeatingArrangement(new Dictionary<string, Row> { { "A", row1 } });
            var arrangement2 = new AuditoriumSeatingArrangement(new Dictionary<string, Row> { { "A", row2 } });

            Check.That(arrangement1).IsNotEqualTo(arrangement2);
        }

        [Test]
        public void Arrangement_and_its_allocated_version_are_not_equal()
        {
            var seat = new SeatingPlace("A", 1, PricingCategory.First, SeatingPlaceAvailability.Available);
            var row = new Row("A", new List<SeatingPlace> { seat });
            var original = new AuditoriumSeatingArrangement(new Dictionary<string, Row> { { "A", row } });
            var allocated = original.Allocate(new List<SeatingPlace> { seat });

            Check.That(original).IsNotEqualTo(allocated);
        }
    }
}
