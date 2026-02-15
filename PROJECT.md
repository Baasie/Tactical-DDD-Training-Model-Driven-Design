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

These CRC cards follow Rebecca Wirfs-Brock's responsibility-driven design approach. Each card captures what an object **knows** (its knowledge responsibilities), what it **does** (its behavioural responsibilities), and who it **collaborates** with to fulfil those responsibilities. These cards represent the full domain model from the modelling session. 

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
- SeatingOptionIsSuggested
- SuggestionsAreMade

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

### Auditorium Seating Arrangement

Purpose: Represents the full seating layout of an auditorium — coordinates the search for available seats across all rows.

**Knows:**
- An ordered map of rows

**Does:**
- Delegate seat suggestion requests to rows in order
- Return the first matching seating option from any row
- Allocate seats across rows (return a new instance with updated rows)

**Collaborators:**
- Row

### Row

Purpose: Represents a single row of seats — finds groups of available seats that match a requested party size and pricing category.

**Knows:**
- Its name (A, B, C...)
- Its ordered collection of seating places

**Does:**
- Find available seats matching a party size and pricing category
- Return a SeatingOptionIsSuggested or SeatingOptionIsNotAvailable
- Allocate seats (return a new instance with updated seating places)

**Collaborators:**
- SeatingPlace
- SeatingOptionIsSuggested

### Seating Option Is Suggested

Purpose: Represents a potential seating suggestion being assembled — collects seats from a row that match a party request.

**Knows:**
- Its pricing category
- Its party size requested
- Its collected seats so far

**Does:**
- Accumulate matching seats into the suggestion
- Check if the collected seats satisfy the party size

**Collaborators:**
- None

### Seating Option Is Not Available

Purpose: Signals that a row could not satisfy the seating request — acts as a null object for Seating Option Is Suggested.

**Knows:**
- The pricing category and party size (inherited, empty seats)

**Does:**
- Signal "no seating option could be found in this row"

**Collaborators:**
- None

### Seating Place

Purpose: Represents a single seat in the auditorium — knows its identity, category, and availability, and can allocate itself.

**Knows:**
- Its row name and seat number
- Its pricing category
- Its availability status

**Does:**
- Report whether it is available
- Match itself against a requested pricing category
- Allocate itself (return a new instance with allocated status)
- Generate its display name (e.g. "A3")

**Collaborators:**
- None

### Seating Place Availability

Purpose: Represents the possible states a seat can be in.

**Knows:**
- The three possible states: AVAILABLE, RESERVED, and ALLOCATED

**Does:**
- Nothing (pure value object)

**Collaborators:**
- None

### Suggestion Is Made

Purpose: Represents a confirmed suggestion — an immutable snapshot of seats that fulfilled a party request.

**Knows:**
- Its suggested seats
- Its party size requested
- Its pricing category

**Does:**
- Return seat names for display
- Check if the suggestion matches the party size expectation

**Collaborators:**
- SeatingPlace

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

### Suggestions Are Not Available

Purpose: Signals that no valid suggestions could be made for the entire request — acts as a null object.

**Knows:**
- The show ID and party size (inherited, empty suggestions)

**Does:**
- Signal "no suggestions could be made"

**Collaborators:**
- None

### Pricing Category

Purpose: Represents the pricing tiers available in an auditorium, including a wildcard category for mixed suggestions.

**Knows:**
- Its numeric value (1, 2, 3, or 4)
- The four categories: FIRST, SECOND, THIRD, MIXED

**Does:**
- Convert from integer value (1, 2, 3) to enum constant

**Collaborators:**
- None