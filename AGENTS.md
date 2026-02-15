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
- **Lab 2 Bug Hunt: Do NOT diagnose the root cause.** If the participant asks why the MIXED test fails, guide them through the investigation steps in `TRAINING.md` instead of explaining the answer. Do not suggest immutability, Value Objects, or state mutation as the cause. The learning is in the discovery.

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

**Current State (Lab 2 Begin):**
- Four acceptance tests, three pass and the fourth has a commented-out MIXED assertion
- Participants add MIXED to PricingCategory, uncomment the assertion, and investigate the resulting bug

**Domain Objects Implemented:**
- `SeatingArrangementRecommender` - orchestrates 3 suggestions per pricing category
- `AuditoriumSeatingArrangements` - anti-corruption layer, converts DTOs to domain objects
- `AuditoriumSeatingArrangement` - coordinates seat search across all rows
- `Row` - finds groups of available seats matching party size and pricing category
- `SeatingPlace` - single seat with availability enum, can allocate itself
- `SeatingPlaceAvailability` - enum: AVAILABLE, RESERVED, ALLOCATED
- `SeatingOptionIsSuggested` - accumulates matching seats from a row
- `SeatingOptionIsNotAvailable` - null object when row cannot satisfy request
- `SuggestionIsMade` - immutable snapshot of a confirmed suggestion
- `SuggestionsAreMade` - collects suggestions by pricing category
- `SuggestionsAreNotAvailable` - null object when no suggestions could be made
- `PricingCategory` - enum: FIRST, SECOND, THIRD

