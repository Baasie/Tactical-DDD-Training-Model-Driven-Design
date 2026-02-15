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

---

## Project Layout

```
TheaterSuggestions/
├── Java/
│   ├── ExternalDependencies/                    # Read-only
│   ├── ScreeningSeatingArrangementSuggestions/  # Domain code
│   └── ScreeningSeatingArrangementSuggestionsAcceptanceTests/
├── CSharp/
│   ├── ExternalDependencies/                    # Read-only
│   ├── ScreeningSeatingArrangementSuggestions/  # Domain code
│   └── ScreeningSeatingArrangementSuggestions.Tests/
└── Kotlin/
    ├── ExternalDependencies/                    # Read-only
    ├── ScreeningSeatingArrangementSuggestions/  # Domain code
    └── ScreeningSeatingArrangementSuggestionsAcceptanceTests/
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

**Current State (Lab 3 Green Test):**
- Unit test prototype passes: middle-outward algorithm implemented in standalone helper method
- Acceptance test still fails: domain model not yet updated to use the new algorithm
- Domain code unchanged from lab-2-end — all existing tests still pass

**Domain Objects Implemented:**
- `SeatingArrangementRecommender` - Service, orchestrates 3 suggestions per pricing category including MIXED
- `AuditoriumSeatingArrangements` - Repository and Factory, anti-corruption layer, converts DTOs to domain objects
- `AuditoriumSeatingArrangement` - Aggregate, immutable record, coordinates seat search and returns new instance on allocate
- `Row` - Value Object, immutable record, finds available seats and returns new instance on allocate
- `SeatingPlace` - Value Object, immutable record, allocate returns new instance with ALLOCATED status
- `SeatingPlaceAvailability` - Value Object, enum: AVAILABLE, RESERVED, ALLOCATED
- `SeatingOption` - Value Object, sealed interface defining the polymorphic contract for seating suggestions
- `SeatingOptionIsSuggested` - Value Object, immutable record implementing SeatingOption, holds matching seats from a row
- `SeatingOptionIsNotAvailable` - Value Object (Null Object), immutable record implementing SeatingOption
- `SuggestionIsMade` - Value Object, immutable snapshot of a confirmed suggestion
- `SuggestionsAreMade` - Value Object, collects suggestions by pricing category
- `SuggestionsAreNotAvailable` - Value Object (Null Object), signals no suggestions could be made
- `PricingCategory` - Value Object, enum: FIRST, SECOND, THIRD, MIXED

