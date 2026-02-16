# AGENTS.md

Instructions for AI coding agents working on this training project.

> See `PROJECT.md` for domain rules and CRC cards, `TRAINING.md` for lab instructions.

---

## Build Commands

| Language | Command |
|----------|---------|
| Java | `cd TheaterSuggestions/Java && mvn test` |
| C# | `cd TheaterSuggestions/CSharp && dotnet test` |
| Kotlin | `cd TheaterSuggestions/Kotlin && ./gradlew test` |

### Run API (Lab 5+)

| Language | Command |
|----------|---------|
| Java | `cd TheaterSuggestions/Java && mvn spring-boot:run -pl ScreeningSeatingArrangementSuggestionsApi` |
| C# | `cd TheaterSuggestions/CSharp/ScreeningSeatingArrangementSuggestions.Api && dotnet run` |
| Kotlin | `cd TheaterSuggestions/Kotlin && ./gradlew :ScreeningSeatingArrangementSuggestionsApi:run` |

---

## Agent Behavior

### How to Help
1. **Reference the CRC cards** in `PROJECT.md` - they define class responsibilities
2. **Work incrementally** - help with one test at a time, not the entire solution
3. **Explain reasoning** - when suggesting code, explain why it fits the domain model
4. **Use domain language** - SeatingPlace, Row, PricingCategory, etc.
5. **Surface design tensions** - when existing code resists new requirements, discuss why

### What to Avoid
- Don't implement all tests at once - the learning is in the incremental process
- Don't add patterns or abstractions not required by the current failing test
- Don't modify files in `ExternalDependencies/` - that module is read-only
- **Don't immediately solve design problems** - see `TRAINING.md` for lab-specific guidance
- **Lab 2 Bug Hunt (lab-2-begin only): Do NOT diagnose the root cause.** If the participant asks why the MIXED test fails, guide them through the investigation steps in `TRAINING.md` instead of explaining the answer. Do not suggest immutability, Value Objects, or state mutation as the cause. The learning is in the discovery.
- **Lab 3 Integration (lab-3-green-test): Do NOT provide the integration design directly.** When participants ask how to integrate the middle-outward algorithm into the domain model, ask DDD questions instead of giving the answer. Help them discover whether "distance from middle" is a deeper domain concept, whether it belongs on Row or elsewhere, and what a domain expert would call it. Example questions to ask: "Is this a responsibility of Row, or a separate concern?", "What would a domain expert call this concept?", "Does this change Row's responsibility, or introduce a new object?"
- **Lab 4 Adjacent Seating (lab-4-begin): Do NOT solve the design challenges directly.** When participants discover that `seatNames()` returns the wrong format or that the Lincoln-17 test breaks, guide them to understand *why* rather than handing them the fix. Help them trace through the flow: "What does `seatNames()` currently return?", "Why does the acceptance test expect hyphens?", "What changed in how seats are suggested?" For the prototype, help with the sliding window algorithm if asked, but let participants discover the integration challenges themselves.
- **Lab 4 Integration (lab-4-green-test): Do NOT provide the integration design directly.** When participants ask how to integrate the adjacent algorithm into the domain model, ask design questions: "Should Row have two methods or one?", "What happens to party=1 with the adjacent algorithm?", "Where should seat name formatting live — in the domain or at the display boundary?", "Is DistanceFromRowCenter still needed, or has AdjacentSeats absorbed its concept?"
- **Lab 5 Hexagonal Architecture (lab-5-begin): Do NOT classify objects for the participant.** When they ask whether a class belongs in the domain or the adapter, guide them with: "Does this class depend on anything outside the domain?", "Could you replace this class's implementation without changing the domain?", "What would a port interface for this look like?" Help with web framework wiring (Spring Boot, Ktor, ASP.NET Core) when asked, but let participants discover which class is the adapter themselves.

---

## Project Layout

```
TheaterSuggestions/
├── Java/
│   ├── ExternalDependencies/                              # Read-only
│   ├── ScreeningSeatingArrangementSuggestions/            # Domain code
│   ├── ScreeningSeatingArrangementSuggestionsAcceptanceTests/
│   └── ScreeningSeatingArrangementSuggestionsApi/         # API adapter (Spring Boot)
├── CSharp/
│   ├── ExternalDependencies/                              # Read-only
│   ├── ScreeningSeatingArrangementSuggestions/            # Domain code
│   ├── ScreeningSeatingArrangementSuggestions.Tests/
│   └── ScreeningSeatingArrangementSuggestions.Api/        # API adapter (ASP.NET Core)
└── Kotlin/
    ├── ExternalDependencies/                              # Read-only
    ├── ScreeningSeatingArrangementSuggestions/            # Domain code
    ├── ScreeningSeatingArrangementSuggestionsAcceptanceTests/
    └── ScreeningSeatingArrangementSuggestionsApi/         # API adapter (Ktor)
```

---

## Domain Quick Reference

**Auditorium Layout Notation:**
```
      1   2   3   4   5   6   7   8   9  10
 A : (2) (2)  1  (1) (1) (1) (1) (1) (2) (2)
```
- Numbers (1, 2, 3) = pricing category
- Parentheses = seat is reserved
- Letters (A, B, C) = row name
- Numbers in header = seat number

**Current State (Lab 5 Begin):**
- Seven acceptance tests pass (middle-outward ordering, adjacent seating for party of 3 and 4)
- Row unit tests verify middle-outward single seat and adjacent group seat selection
- `AdjacentSeats` Value Object handles contiguous group ranking by center distance
- Single `suggestSeatingOption()` method on Row handles both individual and group seating
- Seat name formatting (hyphen-joining) lives at the display layer (`SuggestionsAreMade`)
- New API/adapter projects added with a working REST endpoint (`GET /api/suggestions?showId={showId}&partySize={partySize}`)
- Domain module is unchanged — `AuditoriumSeatingArrangements` still lives in domain as a concrete class (participants will extract it into a port interface + adapter)

**Domain Objects Implemented:**
- `SeatingArrangementRecommender` - Service, orchestrates 3 suggestions per pricing category including MIXED
- `AuditoriumSeatingArrangements` - Repository and Factory, anti-corruption layer, converts DTOs to domain objects (Lab 5: will become a port interface in domain + adapter implementation in API project)
- `AuditoriumSeatingArrangement` - Aggregate, immutable record, coordinates seat search and returns new instance on allocate
- `Row` - Value Object, immutable record, finds contiguous groups of available seats using sliding window algorithm, ranked by AdjacentSeats center distance
- `SeatingPlace` - Value Object, immutable record, allocate returns new instance with ALLOCATED status
- `SeatingPlaceAvailability` - Value Object, enum: AVAILABLE, RESERVED, ALLOCATED
- `SeatingOption` - Value Object, sealed interface defining the polymorphic contract for seating suggestions
- `SeatingOptionIsSuggested` - Value Object, immutable record implementing SeatingOption, holds matching seats from a row
- `SeatingOptionIsNotAvailable` - Value Object (Null Object), immutable record implementing SeatingOption
- `SuggestionIsMade` - Value Object, immutable snapshot of a confirmed suggestion, returns individual seat names
- `SuggestionsAreMade` - Value Object, collects suggestions by pricing category, joins seat names with hyphens for display
- `SuggestionsAreNotAvailable` - Value Object (Null Object), signals no suggestions could be made
- `AdjacentSeats` - Value Object, represents a contiguous group of seats with center distance for ranking
- `DistanceFromRowCenter` - Value Object (unused), superseded by AdjacentSeats — retained as discussion point
- `PricingCategory` - Value Object, enum: FIRST, SECOND, THIRD, MIXED

