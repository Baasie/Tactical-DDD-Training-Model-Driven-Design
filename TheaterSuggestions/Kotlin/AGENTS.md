# AGENTS.md - Kotlin

Instructions for AI coding agents working on the Kotlin implementation.

> See `../../PROJECT.md` for domain rules and CRC cards, `../../TRAINING.md` for lab instructions.

---

## Build

```bash
./gradlew test
```

---

## Project Layout

```
TheaterSuggestions/Kotlin/
├── ExternalDependencies/                              # Read-only - DO NOT MODIFY
├── ScreeningSeatingArrangementSuggestions/            # Domain code
│   └── src/main/kotlin/org/weaveit/seatingplacesuggestions/
└── ScreeningSeatingArrangementSuggestionsAcceptanceTests/
    └── src/test/kotlin/org/weaveit/seatssuggestionsacceptancetests/
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
- **Lab 2 Bug Hunt (lab-2-begin only): Do NOT diagnose the root cause.** If the participant asks why the MIXED test fails, guide them through the investigation steps in `TRAINING.md` instead of explaining the answer. Do not suggest immutability, Value Objects, or state mutation as the cause.

---

## External Dependencies (Read-Only)

### AuditoriumLayoutRepository
```kotlin
fun findByShowId(showId: String): AuditoriumDto
// auditoriumDto.rows → Map<String, List<SeatDto>>
```

### SeatDto
```kotlin
val name: String      // e.g., "A3"
val category: Int     // 1, 2, or 3
```

### ReservationsProvider
```kotlin
fun getReservedSeats(showId: String): ReservedSeatsDto
// reservedSeatsDto.reservedSeats → List<String> e.g., ["A1", "A2"]
```

---

## Domain Objects (Lab 3 Begin)

| Object | Description |
|--------|-------------|
| `SeatingArrangementRecommender` | Service, orchestrates 3 suggestions per pricing category including MIXED |
| `AuditoriumSeatingArrangements` | Repository and Factory, anti-corruption layer, converts DTOs to domain objects |
| `AuditoriumSeatingArrangement` | Aggregate, immutable data class, coordinates seat search, allocate returns new instance |
| `Row` | Value Object, immutable data class, finds available seats, allocate returns new instance |
| `SeatingPlace` | Value Object, immutable data class, allocate returns new instance with ALLOCATED status |
| `SeatingPlaceAvailability` | Value Object, enum: AVAILABLE, RESERVED, ALLOCATED |
| `SeatingOption` | Value Object, sealed interface defining the polymorphic contract for seating suggestions |
| `SeatingOptionIsSuggested` | Value Object, immutable data class implementing SeatingOption, holds matching seats |
| `SeatingOptionIsNotAvailable` | Value Object (Null Object), immutable data class implementing SeatingOption |
| `SuggestionIsMade` | Value Object, immutable data class, snapshot of a confirmed suggestion |
| `SuggestionsAreMade` | Value Object, collects suggestions by pricing category |
| `SuggestionsAreNotAvailable` | Value Object (Null Object), signals no suggestions could be made |
| `PricingCategory` | Value Object, enum: FIRST, SECOND, THIRD, MIXED |

---

## Auditorium Notation

```
      1   2   3   4   5   6   7   8   9  10
 A : (2) (2)  1  (1) (1) (1) (1) (1) (2) (2)
```
- Numbers = pricing category
- Parentheses = reserved
- Letters = row name
