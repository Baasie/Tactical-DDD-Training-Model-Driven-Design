# AGENTS.md - Java

Instructions for AI coding agents working on the Java implementation.

> See `../../PROJECT.md` for domain rules and CRC cards, `../../TRAINING.md` for lab instructions.

---

## Build

```bash
mvn test
```

---

## Project Layout

```
TheaterSuggestions/Java/
├── ExternalDependencies/                              # Read-only - DO NOT MODIFY
├── ScreeningSeatingArrangementSuggestions/            # Domain code
│   └── src/main/java/org/weaveit/seatingplacesuggestions/
└── ScreeningSeatingArrangementSuggestionsAcceptanceTests/
    └── src/test/java/org/weaveit/seatssuggestionsacceptancetests/
```

**Package:** `org.weaveit.seatingplacesuggestions`

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
- **Lab 2 Bug Hunt: Do NOT diagnose the root cause.** If the participant asks why the MIXED test fails, guide them through the investigation steps in `TRAINING.md` instead of explaining the answer. Do not suggest immutability, Value Objects, or state mutation as the cause.

---

## External Dependencies (Read-Only)

### AuditoriumLayoutRepository
```java
AuditoriumDto findByShowId(String showId)
// auditoriumDto.rows() → Map<String, List<SeatDto>>
```

### SeatDto
```java
String name()      // e.g., "A3"
int category()     // 1, 2, or 3
```

### ReservationsProvider
```java
ReservedSeatsDto getReservedSeats(String showId)
// reservedSeatsDto.reservedSeats() → List<String> e.g., ["A1", "A2"]
```

---

## Domain Objects (Lab 2 Begin)

| Object | Description |
|--------|-------------|
| `SeatingArrangementRecommender` | Orchestrates 3 suggestions per pricing category |
| `AuditoriumSeatingArrangements` | Anti-corruption layer, converts DTOs to domain objects |
| `AuditoriumSeatingArrangement` | Coordinates seat search across all rows |
| `Row` | Finds groups of available seats matching party size and pricing category |
| `SeatingPlace` | Single seat with availability enum, can allocate itself |
| `SeatingPlaceAvailability` | Enum: AVAILABLE, RESERVED, ALLOCATED |
| `SeatingOptionIsSuggested` | Accumulates matching seats from a row |
| `SeatingOptionIsNotAvailable` | Null object when row cannot satisfy request |
| `SuggestionIsMade` | Immutable snapshot of a confirmed suggestion |
| `SuggestionsAreMade` | Collects suggestions by pricing category |
| `SuggestionsAreNotAvailable` | Null object when no suggestions could be made |
| `PricingCategory` | Enum: FIRST, SECOND, THIRD |

---

## Auditorium Notation

```
      1   2   3   4   5   6   7   8   9  10
 A : (2) (2)  1  (1) (1) (1) (1) (1) (2) (2)
```
- Numbers = pricing category
- Parentheses = reserved
- Letters = row name
