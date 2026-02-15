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

## Starting Point

- `SeatingArrangementRecommender` skeleton exists
- Tests are commented - uncomment one at a time
- Use your CRC cards to guide implementation

---

## Auditorium Notation

```
      1   2   3   4   5   6   7   8   9  10
 A : (2) (2)  1  (1) (1) (1) (1) (1) (2) (2)
```
- Numbers = pricing category
- Parentheses = reserved
- Letters = row name
