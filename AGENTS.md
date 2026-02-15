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

**Current State (First Test Green):**
- First acceptance test passes, tests 2 and 3 are commented out
- Minimal implementation — only enough code to pass the first test

**Domain Objects Implemented:**
- `SeatingArrangementRecommender` - makes suggestions (currently hardcoded to FIRST category)
- `AuditoriumSeatingArrangements` - anti-corruption layer, returns rows as a map
- `Row` - data holder for seating places
- `SeatingPlace` - single seat with availability as boolean
- `SuggestionsAreMade` - collects seat names by pricing category
- `PricingCategory` - enum: FIRST, SECOND, THIRD

