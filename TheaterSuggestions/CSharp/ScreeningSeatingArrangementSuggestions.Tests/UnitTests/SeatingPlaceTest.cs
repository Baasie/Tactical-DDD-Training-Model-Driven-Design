using NFluent;
using NUnit.Framework;
using SeatsSuggestions;

namespace SeatsSuggestions.Tests.UnitTests;

[TestFixture]
public class SeatingPlaceTest
{
    [TestFixture]
    public class Immutability
    {
        [Test]
        public void Allocate_returns_new_instance_and_does_not_mutate_original()
        {
            var original = new SeatingPlace("A", 1, PricingCategory.First, SeatingPlaceAvailability.Available);

            var allocated = original.Allocate();

            Check.That(allocated).Not.IsSameReferenceAs(original);
            Check.That(original.IsAvailable()).IsTrue();
            Check.That(allocated.IsAvailable()).IsFalse();
        }

        [Test]
        public void Allocate_returns_same_instance_when_already_allocated()
        {
            var alreadyAllocated = new SeatingPlace("A", 1, PricingCategory.First, SeatingPlaceAvailability.Allocated);

            var result = alreadyAllocated.Allocate();

            Check.That(result).IsSameReferenceAs(alreadyAllocated);
        }
    }

    [TestFixture]
    public class ValueEquality
    {
        [Test]
        public void Two_seating_places_with_same_values_are_equal()
        {
            var seat1 = new SeatingPlace("A", 1, PricingCategory.First, SeatingPlaceAvailability.Available);
            var seat2 = new SeatingPlace("A", 1, PricingCategory.First, SeatingPlaceAvailability.Available);

            Check.That(seat1).IsEqualTo(seat2);
            Check.That(seat1.GetHashCode()).IsEqualTo(seat2.GetHashCode());
        }

        [Test]
        public void Two_seating_places_with_different_values_are_not_equal()
        {
            var seat1 = new SeatingPlace("A", 1, PricingCategory.First, SeatingPlaceAvailability.Available);
            var seat2 = new SeatingPlace("A", 2, PricingCategory.First, SeatingPlaceAvailability.Available);

            Check.That(seat1).IsNotEqualTo(seat2);
        }

        [Test]
        public void Seating_place_and_its_allocated_version_are_not_equal()
        {
            var available = new SeatingPlace("A", 1, PricingCategory.First, SeatingPlaceAvailability.Available);
            var allocated = available.Allocate();

            Check.That(available).IsNotEqualTo(allocated);
        }
    }
}
