# AGENTS.md - C#

Instructions for AI coding agents working on the C# implementation.

> See `../../PROJECT.md` for domain rules and CRC cards, `../../TRAINING.md` for lab instructions.

---

## Build

```bash
dotnet test
```

---

## Project Layout

```
TheaterSuggestions/CSharp/
├── ExternalDependencies/                              # Read-only - DO NOT MODIFY
├── ScreeningSeatingArrangementSuggestions/            # Domain code
└── ScreeningSeatingArrangementSuggestions.Tests/
    ├── AcceptanceTests/
    └── UnitTests/
```

**Namespace:** `SeatsSuggestions`

---

## Agent Behavior

### How to Help
1. **Reference the CRC cards** in `../../PROJECT.md`
2. **Work incrementally** - one test at a time
3. **Use domain language** - SeatingPlace, Row, PricingCategory
4. **Explain reasoning** - why code fits the domain model

### What to Avoid
- Don't implement all tests at once
- Don't modify files in `ExternalDependencies/`
- Don't over-engineer
- **Lab 2 Bug Hunt (lab-2-begin only): Do NOT diagnose the root cause.** If the participant asks why the MIXED test fails, guide them through the investigation steps in `TRAINING.md` instead of explaining the answer. Do not suggest immutability, Value Objects, or state mutation as the cause.

---

## External Dependencies (Read-Only)

### AuditoriumLayoutRepository
```csharp
AuditoriumDto FindByShowId(string showId)
// auditoriumDto.Rows → Dictionary<string, IReadOnlyList<SeatDto>>
```

### SeatDto
```csharp
string Name { get; }      // e.g., "A3"
int Category { get; }     // 1, 2, or 3
```

### ReservationsProvider
```csharp
ReservedSeatsDto GetReservedSeats(string showId)
// reservedSeatsDto.ReservedSeats → List<string> e.g., ["A1", "A2"]
```

---

## Domain Objects (Lab 2 End)

| Object | Description |
|--------|-------------|
| `SeatingArrangementRecommender` | Service, orchestrates 3 suggestions per pricing category including Mixed |
| `AuditoriumSeatingArrangements` | Repository and Factory, anti-corruption layer, converts DTOs to domain objects |
| `AuditoriumSeatingArrangement` | Aggregate, immutable record, coordinates seat search, Allocate returns new instance |
| `Row` | Value Object, immutable record, finds available seats, Allocate returns new instance |
| `SeatingPlace` | Value Object, immutable record, Allocate returns new instance with Allocated status |
| `SeatingPlaceAvailability` | Value Object, enum: Available, Reserved, Allocated |
| `SeatingOption` | Value Object, interface defining the polymorphic contract for seating suggestions |
| `SeatingOptionIsSuggested` | Value Object, immutable record implementing SeatingOption, holds matching seats |
| `SeatingOptionIsNotAvailable` | Value Object (Null Object), immutable record implementing SeatingOption |
| `SuggestionIsMade` | Value Object, immutable record, snapshot of a confirmed suggestion |
| `SuggestionsAreMade` | Value Object, collects suggestions by pricing category |
| `SuggestionsAreNotAvailable` | Value Object (Null Object), signals no suggestions could be made |
| `PricingCategory` | Value Object, enum: First, Second, Third, Mixed |

---

## Auditorium Notation

```
      1   2   3   4   5   6   7   8   9  10
 A : (2) (2)  1  (1) (1) (1) (1) (1) (2) (2)
```
- Numbers = pricing category
- Parentheses = reserved
- Letters = row name
