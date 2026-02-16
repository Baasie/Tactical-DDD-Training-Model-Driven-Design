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
├── ScreeningSeatingArrangementSuggestionsAcceptanceTests/
│   └── src/test/java/org/weaveit/seatssuggestionsacceptancetests/
└── ScreeningSeatingArrangementSuggestionsApi/         # API adapter (Spring Boot)
    └── src/main/java/org/weaveit/seatingplacesuggestions/api/
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
- **Lab 3 Integration (lab-3-green-test): Do NOT provide the integration design directly.** When participants ask how to integrate the middle-outward algorithm, ask DDD questions instead: "Is this a responsibility of Row, or a separate concern?", "What would a domain expert call this concept?", "Does this change Row's responsibility, or introduce a new object?"
- **Lab 4 Adjacent Seating (lab-4-begin): Do NOT solve the design challenges directly.** Help with the sliding window prototype if asked, but let participants discover the `seatNames()` format change and the Lincoln-17 test breakage themselves. Guide them: "What does `seatNames()` currently return?", "Why does the acceptance test expect hyphens?", "What changed in how seats are suggested?"
- **Lab 4 Integration (lab-4-green-test): Do NOT provide the integration design directly.** Ask design questions: "Should Row have two methods or one?", "What happens to party=1 with the adjacent algorithm?", "Where should seat name formatting live?", "Is DistanceFromRowCenter still needed, or has AdjacentSeats absorbed its concept?"
- **Lab 5 Hexagonal Architecture (lab-5-begin): Do NOT classify objects for the participant.** Guide them with: "Does this class depend on anything outside the domain?", "Could you replace this implementation without changing the domain?", "What would a port interface for this look like?" Help with Spring Boot wiring when asked.

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

## Domain Objects (Lab 4 End)

| Object | Description |
|--------|-------------|
| `SeatingArrangementRecommender` | Service, orchestrates 3 suggestions per pricing category including MIXED |
| `AuditoriumSeatingArrangements` | Repository and Factory, anti-corruption layer, converts DTOs to domain objects (Lab 5: becomes port interface + adapter) |
| `AuditoriumSeatingArrangement` | Aggregate, immutable record, coordinates seat search, allocate returns new instance |
| `Row` | Value Object, immutable record, finds contiguous groups of available seats using sliding window, ranked by AdjacentSeats center distance |
| `SeatingPlace` | Value Object, immutable record, allocate returns new instance with ALLOCATED status |
| `SeatingPlaceAvailability` | Value Object, enum: AVAILABLE, RESERVED, ALLOCATED |
| `SeatingOption` | Value Object, sealed interface defining the polymorphic contract for seating suggestions |
| `SeatingOptionIsSuggested` | Value Object, immutable record implementing SeatingOption, holds matching seats |
| `SeatingOptionIsNotAvailable` | Value Object (Null Object), immutable record implementing SeatingOption |
| `SuggestionIsMade` | Value Object, immutable record, returns individual seat names |
| `SuggestionsAreMade` | Value Object, collects suggestions by pricing category, joins seat names with hyphens for display |
| `SuggestionsAreNotAvailable` | Value Object (Null Object), signals no suggestions could be made |
| `AdjacentSeats` | Value Object, immutable record, contiguous group of seats with center distance for ranking |
| `DistanceFromRowCenter` | Value Object (unused), superseded by AdjacentSeats — retained as discussion point |
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
