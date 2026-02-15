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

These CRC cards follow Rebecca Wirfs-Brock's responsibility-driven design approach. Each card captures what an object **knows** (its knowledge responsibilities), what it **does** (its behavioural responsibilities), and who it **collaborates** with to fulfil those responsibilities. Use your own CRC cards from the modelling session as your primary guide — the card below is one example showing how they translate into code.

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