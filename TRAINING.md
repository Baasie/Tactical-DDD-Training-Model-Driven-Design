# Training Lab Instructions

This file contains training-specific instructions. In a real project, this file would not exist.

---

## Lab 1: Outside-In TDD

### Learning Goal

Learn to translate CRC cards into working code using Outside-In TDD.

### What You Have

- **Acceptance tests** with commented code showing exactly what to call
- **Your CRC cards** from the earlier modeling session
- **CRC cards** in PROJECT.md as a reference for the full domain model
- **A skeleton class** (`SeatingArrangementRecommender`) as a starting point
- **ExternalDependencies** module already implemented (do not modify)

### Your Task

Make the acceptance tests pass by implementing the domain model.

1. Go to the acceptance tests in your language of choice
2. Uncomment the first test
3. Make it pass
4. Repeat for the remaining tests

### Using AI Assistants

You can use AI assistants (Claude, Copilot, etc.) however you like. Try different approaches and see what works.

Some options to explore:
- Ask AI to implement everything at once based on your CRC cards
- Work test by test, asking AI for help on each step
- Write code yourself and use AI to review or debug
- Mix approaches as you go

There is no "wrong" way to use AI in this lab. The goal is to discover what works.

---

## Lab 1: Green — First Acceptance Test Passes

This branch represents the first "green" step of outside-in TDD. The first acceptance test passes with a minimal implementation. Tests 2 and 3 are still commented out.

### What to Review

Before moving to the next test, look at the current implementation and notice:
- Which CRC card responsibilities are already implemented and which are missing
- Where behaviour is concentrated (is it well-distributed across objects?)
- What will need to change when the next test is uncommented
- How the current design will resist or support the remaining requirements

---

## Lab 1: End — All Acceptance Tests Pass

This branch represents the completed Lab 1. All three acceptance tests pass with a refactored implementation that covers all pricing categories, handles the "no seats available" case, and suggests multiple seats for larger parties.

### What Changed From First Green

- `SeatingArrangementRecommender` now iterates all three pricing categories (not hardcoded to FIRST)
- `AuditoriumSeatingArrangement` introduced as a domain object that coordinates seat search across rows
- `Row` has behaviour — finds groups of available seats matching a party size and pricing category
- `SeatingPlace` uses `SeatingPlaceAvailability` enum instead of a boolean, and can allocate itself
- `SeatingOptionIsSuggested` / `SeatingOptionIsNotAvailable` — polymorphic return from Row and AuditoriumSeatingArrangement
- `SuggestionIsMade` — immutable snapshot created from a confirmed SeatingOptionIsSuggested
- `SuggestionsAreNotAvailable` — null object signalling no suggestions could be made

### What to Review

Look at the completed implementation and notice:
- How CRC card responsibilities map to the actual code
- Where state mutation happens (SeatingPlace allocation) — this becomes a design discussion in Lab 2
- How the polymorphic return pattern (SeatingOptionIsSuggested / SeatingOptionIsNotAvailable) eliminates null checks
- How the Recommender orchestrates the workflow: suggest → allocate → repeat

---

## After the Lab: Reflection

Take a few minutes to reflect on your experience:

1. **What approach did you try first?**
   - Did you ask AI to generate everything at once, or work incrementally?

2. **What worked well?**
   - Which interactions with AI were most helpful?

3. **What didn't work?**
   - Did you hit any dead ends or have to backtrack?

4. **If you could start over, what would you do differently?**
   - Would you change your approach based on what you learned?

5. **How did the CRC cards help (or not help)?**
   - Were they useful as-is, or did you need to adapt them?

---

## Lab 2: Value Objects vs Entities

### Learning Goal

Discover how mutability creates subtle bugs, and learn why DDD favors immutable Value Objects over mutable Entities.

### What Changed

A new business requirement has been added: **Mixed category suggestions**.

The Mixed category suggests the best available seats regardless of pricing category, starting from the front-left of the auditorium. The hypothesis is that this will help fill seats at the boundaries between pricing categories.

### Your Task

1. Add the `MIXED` value to the `PricingCategory` enum
2. Uncomment the new test assertion for `PricingCategory.MIXED`
3. Run the test and observe what happens

### The Bug Hunt (Do This Yourself!)

**IMPORTANT: Do NOT ask AI to find or fix the bug.**

This lab is about developing your debugging intuition and understanding *why* something fails, not just *how* to fix it. The learning happens in the investigation.

#### Step 1: Observe the Failure

Run the test. What does the assertion say? What values did you get vs. what was expected?

#### Step 2: Form a Hypothesis

Before looking at code, think:
- What seats should MIXED suggest? (Hint: look at the auditorium layout in the test)
- What seats is it actually suggesting (or not suggesting)?
- What could cause seats to "disappear" or appear differently than expected?

#### Step 3: Trace the Execution

Add some debug output (print statements, breakpoints, or logging) to answer:
- What is the state of seats *before* processing FIRST, SECOND, THIRD?
- What is the state of seats *before* processing MIXED?
- Is there a difference? Why?

#### Step 4: Find the Root Cause

When you understand what's happening, ask yourself:
- Which objects are being modified during the suggestion process?
- Are those modifications intentional for MIXED, or a side effect?
- What kind of objects can be safely shared vs. need isolation?

### Reflection Questions (Before Fixing)

Discuss with your pair or write down your answers:

1. **What happened?** Describe the bug in your own words.

2. **Why did it happen?** What design decision from Lab 1 caused this?

3. **Mutable vs Immutable**:
   - Which objects in the domain are currently mutable (change state)?
   - Which *should* be immutable (represent a fixed value)?

4. **Value Object vs Entity**:
   - A **Value Object** is defined by its attributes, has no identity, and should be immutable (e.g., Money, Date, Address)
   - An **Entity** has identity that persists over time and may change state (e.g., Customer, Order)
   - Is a `SeatingPlace` more like a Value Object or an Entity? Why?
   - Is a `Row` more like a Value Object or an Entity?

5. **The fix**: Without writing code yet, describe what you would need to change to make MIXED work correctly.

### Now Fix It

Once you understand the root cause and have discussed the reflection questions:

1. Refactor toward immutability where appropriate
2. Consider: should finding suggestions *modify* the arrangement, or *return a new one*?
3. Make the test pass

### Using AI Assistants

Now that you've done the investigation yourself, you may use AI to:
- Help implement the refactoring you've designed
- Discuss trade-offs of different approaches
- Review your solution

But resist the urge to ask AI "why is this failing?" - the insight comes from discovering it yourself.

### Discussion Points (After Completion)

1. What was the root cause of the bug?
2. How did you fix it? What objects became immutable?
3. Why does DDD recommend immutable Value Objects as the default?
4. What are the trade-offs of immutability? (Performance? Complexity?)
5. How would you recognize this pattern in future designs?

---

## Lab 2: Green Test — MIXED Category Passes

This branch represents the "green" step after fixing the mutation bug. All four acceptance tests pass, including the MIXED pricing category.

### What Changed

- **SeatingPlace** is now an immutable Value Object (Java `record`, C# `record`, Kotlin `data class`). `allocate()` returns a new instance instead of mutating state. `matchCategory()` returns `true` for MIXED.
- **Row** is now an immutable Value Object. `allocate()` returns a new Row with updated seats. Constructor makes a defensive copy of the seat list.
- **AuditoriumSeatingArrangement** is now an immutable Value Object. `allocate()` returns a new instance with updated rows.
- **SeatingArrangementRecommender** processes all four pricing categories (FIRST, SECOND, THIRD, MIXED). Uses local variable reassignment (`currentArrangement = currentArrangement.allocate(...)`) instead of mutating the shared arrangement.
- **PricingCategory** now includes MIXED.
- **Unit tests** added for SeatingPlace, Row, and AuditoriumSeatingArrangement to verify immutability and value equality.

### What to Review

Look at the refactored implementation and notice:
- How `allocate()` methods at every level return new instances — the immutability pattern cascades from SeatingPlace up through Row and AuditoriumSeatingArrangement
- How the Recommender no longer corrupts shared state — each pricing category gets a fresh arrangement
- How the unit tests verify both immutability (allocate returns different instance) and value equality (same data = equal objects)
- How MIXED works as a wildcard category — `matchCategory()` returns true for any seat when the requested category is MIXED
- The Recommender no longer needs to directly allocate individual seats — allocation is delegated through AuditoriumSeatingArrangement to Row to SeatingPlace

---

## Lab 2: End — Refactored with DDD Tactical Patterns

This branch represents the completed Lab 2. All domain objects are fully refactored with explicit DDD tactical patterns, sealed types replace inheritance hierarchies, and all value objects are fully immutable.

### What Changed From Green Test

- **`SeatingOption`** introduced as a sealed interface (Java/Kotlin) / interface (C#) — defines the polymorphic contract for seating suggestions, replacing the inheritance relationship between SeatingOptionIsSuggested and SeatingOptionIsNotAvailable
- **`SeatingOptionIsSuggested`** refactored from mutable class with `addSeat()` to immutable record/data class — seats are passed at construction, not accumulated
- **`SeatingOptionIsNotAvailable`** no longer inherits from SeatingOptionIsSuggested — now a peer implementing SeatingOption directly
- **`SuggestionIsMade`** refactored from mutable class to immutable record/data class — accepts SeatingOption interface at construction
- **`SuggestionsAreMade`** now receives all suggestions at construction instead of via mutable `add()` method
- **`Row`** and **`AuditoriumSeatingArrangement`** return `SeatingOption` (the interface) instead of concrete `SeatingOptionIsSuggested`

### DDD Tactical Patterns Applied

| Pattern | Objects |
|---------|---------|
| **Service** | SeatingArrangementRecommender |
| **Repository and Factory** | AuditoriumSeatingArrangements |
| **Aggregate** | AuditoriumSeatingArrangement |
| **Value Object** | SeatingPlace, Row, SeatingOption, SeatingOptionIsSuggested, SuggestionIsMade, SuggestionsAreMade, PricingCategory, SeatingPlaceAvailability |
| **Value Object (Null Object)** | SeatingOptionIsNotAvailable, SuggestionsAreNotAvailable |

### What to Review

Look at the completed refactoring and notice:
- How the sealed interface `SeatingOption` replaced class inheritance with composition — SeatingOptionIsSuggested and SeatingOptionIsNotAvailable are now peers, not parent/child
- How removing `addSeat()` from SeatingOptionIsSuggested made it a proper Value Object — constructed once, never modified
- How every object in the domain is now either a Service, Repository, Aggregate, or Value Object — the tactical DDD building blocks
- How immutability propagates: Row no longer collaborates with SeatingOptionIsSuggested (it just creates it), reducing coupling

---

## Lab 3: Deep Modeling

### Learning Goal

Learn to prototype new domain behaviour in isolation before integrating it into the existing model. This is the "Deep Modeling" practice described by Eric Evans — by experimenting outside the current model, you avoid the bias of retrofitting new requirements into existing structures.

### What Changed

A new business requirement has been added: **Seats should be suggested starting from the middle of the row, working outward.** The best seats in a row are near the center, not at the edges.

Two new failing tests have been added:

1. **Acceptance test** — `should_offer_adjacent_seats_nearer_the_middle_of_a_row` uses the Mogador Auditorium (show ID "9") and expects FIRST category seats ordered by proximity to the middle: A4, A3, B5.

2. **Row unit test** — `should_offer_seats_starting_from_middle_of_row` prototypes the middle-outward algorithm in a standalone helper method, deliberately outside the domain model.

### Your Task

1. Start with the **unit test**, not the acceptance test
2. Implement the helper method `offerSeatsNearerTheMiddleOfTheRow()` — it's a standalone function that takes a Row and returns seats sorted by distance from the center
3. Get the unit test green
4. Then think about where this behaviour belongs in the domain model — does it go on Row? Does a new concept emerge? Does the existing `suggestSeatingOption()` method need to change?
5. Integrate the algorithm and make the acceptance test pass

### Why Prototype Outside the Model?

The helper method is deliberately a standalone function, not a method on Row. This is intentional:

- **Lowers retrofit bias** — if you start by modifying Row directly, you'll unconsciously try to fit the new behaviour into the existing structure
- **Focuses on the algorithm** — you can experiment with sorting logic without worrying about how it integrates
- **Reveals hidden concepts** — once the algorithm works, you may notice it introduces a concept (like "distance from middle") that deserves its own representation in the model
- **Matches how domain experts think** — they likely describe this as "suggest seats near the center" not "modify the row's internal suggestion algorithm"

### Using AI Assistants

You may use AI to help implement the algorithm. However, consider:
- Ask AI to help with the standalone prototype first
- Discuss with AI where the behaviour should live in the domain model — what are the trade-offs?
- Use AI to review whether your integration preserves the existing model's design qualities
