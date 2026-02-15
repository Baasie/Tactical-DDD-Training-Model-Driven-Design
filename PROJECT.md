# Seating Place Suggestions

## Domain

This project implements a seat recommendation engine for auditoriums. Given a show ID
and a requested party size, it finds groups of consecutive available seats and organises
them into suggestions grouped by pricing category.

### Key Rules

- Only suggest **available** seats (not reserved).
- Make **3 different suggestions per pricing category**.

## Module Structure

| Module | Responsibility |
|---|---|
| `ExternalDependencies` | Loads auditorium layouts and reservations from JSON stub files. Contains DTOs and repository classes. |
| `ScreeningSeatingArrangementSuggestions` | Core domain logic: recommendation algorithm, domain objects (Row, SeatingPlace, etc.). |
| `ScreeningSeatingArrangementSuggestionsAcceptanceTests` | End-to-end acceptance tests exercising the full recommendation flow against the stub data. |

## Notes

- Test stub data lives under `Stubs/AuditoriumLayouts/`.
- Java, C#, and Kotlin implementations are aligned and use the same domain model with language-specific naming conventions.

## CRC Cards

These CRC cards describe the domain model. They evolved during Lab 1 as the implementation progressed.

### Seating Arrangement Recommender

Purpose: Orchestrates the suggestion workflow — makes sure seating place suggestions get generated for each pricing category across the auditorium.

**Knows:**
- The fixed number of suggestions to generate (3)

**Does:**
- Make 3 different suggestions per pricing category
- Allocate seats after each suggestion to prevent re-suggestion
- Return either "Suggestions Are Made" or "Suggestions Are Not Available"

**Collaborators:**
- AuditoriumSeatingArrangements
- AuditoriumSeatingArrangement

### Auditorium Seating Arrangements

Purpose: Acts as the anti-corruption layer — fetches external data (layout + reservations) and translates it into the domain model.

**Knows:**
- How to reach the external repositories

**Does:**
- Fetch auditorium layout by show ID
- Fetch reservations by show ID
- Convert DTOs to domain objects (Row, SeatingPlace)
- Combine layout information with reservation status

**Collaborators:**
- AuditoriumLayoutRepository (external)
- ReservationsProvider (external)
- AuditoriumSeatingArrangement

### Auditorium Seating Arrangement

Purpose: Represents the full seating layout of an auditorium — coordinates the search for available seats across all rows.

**Knows:**
- An ordered map of rows

**Does:**
- Delegate seat suggestion requests to rows in order
- Return the first matching seating option from any row

**Collaborators:**
- Row
- SeatingOptionIsSuggested
- SeatingOptionIsNotAvailable

### Row

Purpose: Represents a single row of seats — finds groups of available seats that match a requested party size and pricing category.

**Knows:**
- Its name (A, B, C...)
- Its ordered collection of seating places

**Does:**
- Find available seats matching a party size and pricing category
- Return a SeatingOptionIsSuggested or SeatingOptionIsNotAvailable

**Collaborators:**
- SeatingPlace
- SeatingOptionIsSuggested
- SeatingOptionIsNotAvailable

### Seating Place

Purpose: Represents a single seat in the auditorium — knows its identity, category, and availability, and can allocate itself.

**Knows:**
- Its row name and seat number
- Its pricing category
- Its availability status

**Does:**
- Report whether it is available
- Match itself against a requested pricing category
- Allocate itself (transition from available to allocated)
- Generate its display name (e.g. "A3")

**Collaborators:**
- PricingCategory
- SeatingPlaceAvailability

### Seating Place Availability

Purpose: Represents the possible states a seat can be in.

**Knows:**
- The three possible states: AVAILABLE, RESERVED, and ALLOCATED

**Does:**
- Nothing (pure value object)

**Collaborators:**
- None

### Seating Option Is Suggested

Purpose: Accumulates seats during a suggestion attempt — tracks whether enough seats have been collected to satisfy the party size.

**Knows:**
- The party size requested
- The pricing category
- The collected seats so far

**Does:**
- Collect seats one by one during a row scan
- Report whether collected seats match the requested party size

**Collaborators:**
- SeatingPlace
- PricingCategory

### Seating Option Is Not Available

Purpose: Signals that a row could not produce a valid seating option — acts as a null object.

**Knows:**
- The party size requested and pricing category (inherited, empty seat list)

**Does:**
- Signal "no seats found" (matchExpectation always returns false)

**Collaborators:**
- PricingCategory

### Suggestion Is Made

Purpose: Wraps a successful seating option into a confirmed suggestion with displayable seat names.

**Knows:**
- The suggested seats
- The pricing category
- The party size requested

**Does:**
- Provide seat names as strings (e.g. ["A1", "A2"])
- Report whether the suggestion matches the expectation

**Collaborators:**
- SeatingOptionIsSuggested
- SeatingPlace
- PricingCategory

### Suggestions Are Made

Purpose: Holds the complete set of suggestions for a show request — organizes them by pricing category.

**Knows:**
- The show ID and party size
- Suggestions organized by pricing category

**Does:**
- Accept and categorize new suggestions
- Return seat names for a given pricing category
- Check if any suggestion across all categories matched expectations

**Collaborators:**
- SuggestionIsMade
- PricingCategory

### Suggestions Are Not Available

Purpose: Signals that no valid suggestions could be made for the entire request — acts as a null object.

**Knows:**
- The show ID and party size (inherited, empty suggestions)

**Does:**
- Signal "no suggestions could be made"

**Collaborators:**
- None

### Pricing Category

Purpose: Represents the three pricing tiers available in an auditorium.

**Knows:**
- Its numeric value (1, 2, or 3)
- The three categories: FIRST, SECOND, THIRD

**Does:**
- Convert from integer value (1, 2, 3) to enum constant

**Collaborators:**
- None