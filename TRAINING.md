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

---

## Lab 3: Green Test — Prototype Algorithm Works

This branch represents the "green" step for the deep modeling prototype. The standalone helper method passes its unit test, but the domain model hasn't been updated yet — the acceptance test still fails.

### What Changed From Begin

The `offerSeatsNearerTheMiddleOfTheRow()` helper method is now implemented in all 3 languages:

1. Calculate the middle of the row: `(rowSize + 1) / 2.0`
2. Filter to available seats only
3. Sort by distance from middle (ascending), with seat number as tiebreaker
4. Return the sorted list

A `distanceFromMiddleOfTheRow()` helper extracts the distance calculation.

### Current State

- **Unit test**: Green — the prototype algorithm correctly orders seats from middle outward
- **Acceptance test**: Still red — the domain model (Row's `suggestSeatingOption()`) hasn't been updated to use this algorithm

### What to Review

Before integrating into the domain model, notice:
- The algorithm introduces the concept of "distance from middle" — is this a domain concept that deserves its own representation?
- The helper works on a Row but lives outside it — where should this behaviour go?
- The current `suggestSeatingOption()` on Row finds consecutive seats from left to right — the new algorithm fundamentally changes the ordering. How should these two behaviours relate?

### Next Step: Integrate Into the Domain Model

The prototype works. Now the real design question: **where does this behaviour belong?**

This is not just a coding exercise — it's a modeling question. Consider:

1. **Is this simply a new method on Row?** The algorithm operates on a Row's seats. But `suggestSeatingOption()` already does something similar — would adding another method make Row's responsibilities too broad?

2. **Is "distance from middle" a deeper concept?** The prototype revealed that seat ordering depends on a seat's position relative to the center of its row. Is this a property of the seat, the row, or something else entirely?

3. **Would a domain expert recognize this concept?** If you described this to someone who manages theater seating, what words would they use? "Center seats"? "Preferred seating"? "Seat desirability"?

4. **Does the current model need to change, or does a new concept need to emerge?** Deep modeling often reveals concepts that were implicit in the domain but absent from the model.

### Using AI Assistants (Integration Phase)

You may use AI to help with the integration. AI should help you think through the design, not hand you the answer. Expect it to ask you questions like:
- What does "distance from middle" mean in the domain?
- Should Row know about seat ordering strategies, or is that a separate concern?
- How would this change if the business wanted different ordering rules in the future?

---

## Lab 3: End — Deep Model Integrated

This branch represents the completed Lab 3. The middle-outward algorithm is integrated into the domain model, all acceptance tests pass with updated expectations, and a new domain concept has emerged.

### What Changed From Green Test

- **`DistanceFromRowCenter`** introduced as a new Value Object — makes the implicit concept of "how far a seat is from the center of its row" explicit in the domain model
- **`Row.suggestSeatingOption()`** now sorts available seats using `DistanceFromRowCenter` instead of left-to-right ordering
- **Acceptance test expectations updated** — all existing tests now expect middle-outward ordering (e.g., FIRST: `A5, A6, A4` instead of `A3, A4, A5`)
- **Unit test prototype dissolved** — the standalone helper method is gone; the test now calls `row.suggestSeatingOption()` directly

### The Deep Modeling Insight

The prototype revealed that "distance from row center" is a domain concept worth naming. By extracting it into `DistanceFromRowCenter`:
- The concept is **explicit** — anyone reading the code sees that seat ordering depends on proximity to center
- The calculation is **isolated** — the even/odd row logic lives in one place
- The integration is **minimal** — only one line changed in Row's suggestion method

### What to Review

Look at the completed integration and notice:
- How the standalone prototype was dissolved into the model — it served its purpose and is gone
- How `DistanceFromRowCenter` as a named Value Object makes the implicit concept explicit — this is Evans' "making implicit concepts explicit"
- How the change in Row is a single sorting line, but all acceptance test expectations shifted — a deep model change has wide but predictable impact
- How the rest of the domain model (AuditoriumSeatingArrangement, Recommender, SeatingOption) is completely untouched

### Discussion Points

1. Is `DistanceFromRowCenter` a concept a domain expert would recognize? What would they call it?
2. Could the same deep modeling approach reveal other hidden concepts in your own projects?
3. How did prototyping outside the model help you avoid retrofit bias?
4. What would happen if the business wanted different ordering rules for different auditoriums?

---

## Lab 4: Adjacent Seating

### Learning Goal

Continue deep modeling by prototyping a more complex algorithm: finding groups of contiguous seats nearest to the center of a row. This builds on Lab 3's deep modeling practice with a requirement that challenges the current model more fundamentally.

### What Changed

A new business requirement has been added: **When a party requests multiple seats, they should be seated together in adjacent (contiguous) seats.** The group of adjacent seats closest to the middle of the row should be preferred.

New failing tests have been added:

1. **Acceptance tests** — Two new tests using the Dock Street Auditorium (show ID "3"):
   - Party of 4: expects groups like `"C5-C6-C7-C8"` (hyphenated format for adjacent seats)
   - Party of 3: expects groups like `"A6-A7-A8"`

2. **Row unit tests** — Two prototype tests with a stub method `offerAdjacentSeats()`:
   - **Single valid block**: only one contiguous group of the right size exists — forces you to find contiguous windows
   - **Multiple valid blocks**: all seats are available, so many windows of the right size exist — forces you to rank windows by proximity to center

### Your Task

1. Start with the **unit tests**, not the acceptance tests
2. Implement the `offerAdjacentSeats()` prototype method:
   - Find all contiguous windows of available seats of the requested size
   - Select the window closest to the center of the row
3. Get both unit tests green
4. Then think about integration:
   - How does `offerAdjacentSeats()` relate to Row's existing `suggestSeatingOption()`?
   - Does `suggestSeatingOption()` need to change, or should a new method emerge?
   - What happens to seat name formatting when seats are suggested as a group?

### The Design Challenges

This lab surfaces several design tensions:

1. **Algorithm change**: The current `suggestSeatingOption()` picks the N closest individual seats. Adjacent seating requires finding contiguous windows — a fundamentally different algorithm. How should these two behaviours coexist?

2. **Output format change**: The acceptance tests expect hyphenated seat names for groups (e.g., `"A5-A6-A7"` instead of separate `"A5"`, `"A6"`, `"A7"`). This means `SuggestionIsMade.seatNames()` needs to change. Discover what needs to change and where.

3. **Breaking existing tests**: When you integrate adjacent seating into the domain model, the Lincoln-17 (party=2) acceptance test will break. The current test expects individual seat names, but the adjacent algorithm will return grouped names. Think about why and how to fix the expectations.

### Using AI Assistants

You may use AI to help implement the prototype algorithm. Consider:
- Ask AI to help with the sliding window algorithm in the prototype
- Discuss how contiguous seat selection differs from individual seat selection
- Use AI to help trace through the Dock Street auditorium data to verify your algorithm
- When integrating, discuss with AI where the adjacent logic should live in the domain model

---

## Lab 4: Green Test — Prototype Algorithm Works

This branch represents the "green" step for the adjacent seating prototype. The standalone `offerAdjacentSeats()` method passes its unit tests, but the domain model hasn't been updated yet — the acceptance tests still fail.

### What Changed From Begin

The `offerAdjacentSeats()` prototype method is now implemented in all 3 languages:

1. Find contiguous blocks of available seats (consecutive seat numbers)
2. For each block large enough, slide a window of `partySize` across it
3. For each window, calculate its center and distance from the row center
4. Select the window with the smallest distance (ties broken by lower seat number)

The row center formula matches Lab 3: `(rowSize + 1) / 2.0`

### Current State

- **Unit tests**: Green — the prototype correctly finds the best contiguous window for both the single-block and multi-block scenarios
- **Acceptance tests**: Still red — the domain model hasn't been updated to use this algorithm

### Overlapping Suggestions — A Design Discussion

When tracing the party=4 acceptance test expectations, you'll notice something interesting: **the best window (e.g., seats 4-5-6-7) sits in the exact middle of a 10-seat row, splitting the remaining seats into two blocks of 3 (seats 1-2-3 and 8-9-10).** Neither block is large enough for another group of 4.

This means:
- For SECOND category (rows C and D): only **2 suggestions** are possible per row, for a total of 2 across both rows
- For THIRD category (rows E and F): same situation — only **2 suggestions** total

Compare this with party=3, where the best window (4-5-6) leaves blocks of 3 (seats 1-2-3) and 4 (seats 7-8-9-10) — both large enough for another group of 3. So party=3 gets the full 3 suggestions per category.

This is a natural consequence of the "pick the most centered window first" algorithm combined with allocation. It's worth discussing:
- Is 2 suggestions acceptable, or should the algorithm try to maximize the number of suggestions?
- Would a domain expert prefer the most centered window, or would they prefer to spread groups more evenly across the row?
- What happens with even larger party sizes relative to row size?

### What to Review

Before integrating into the domain model, notice:
- The sliding window algorithm is fundamentally different from Lab 3's individual seat selection — it operates on contiguous groups, not individual seats
- The prototype's center calculation `(rowSize + 1) / 2.0` is consistent with `DistanceFromRowCenter` from Lab 3
- The tiebreaker (lower first seat number) means windows to the left of center are preferred when two windows are equidistant

### Next Step: Integrate Into the Domain Model

The prototype works. Now the real design questions:

1. **How does `offerAdjacentSeats()` relate to `suggestSeatingOption()`?** Should Row have two methods, or should the existing method handle both cases?

2. **Output format**: The acceptance tests expect hyphenated seat names (e.g., `"A5-A6-A7"`) for groups. Currently `SuggestionIsMade.seatNames()` returns individual names. What needs to change?

3. **Breaking changes**: When you integrate adjacent seating, the Lincoln-17 (party=2) acceptance test will break — it currently expects individual seat names, but the adjacent algorithm will return grouped names. Think about why and how to fix the expectations.

---

## Lab 4: End — Adjacent Seating Integrated

This branch represents the completed Lab 4. The adjacent seating algorithm is fully integrated into the domain model, all acceptance tests pass, and two key design improvements have been applied.

### What Changed From Green Test

- **`AdjacentSeats`** introduced as a new Value Object — represents a contiguous group of seats that can be offered together, with a distance-from-center measure for ranking. Uses a factory method `of()` that calculates the window center's distance from the row center.
- **`Row.suggestSeatingOption()`** now uses the adjacent algorithm — finds contiguous blocks of available seats, slides a window of `partySize` across each block, creates `AdjacentSeats` for each window, and selects the one closest to the center. There is no separate method; the existing `suggestSeatingOption()` was unified to handle both individual and group seating through the same contiguous-window approach.
- **Seat name formatting moved to display layer** — `SuggestionIsMade.seatNames()` returns individual seat names (e.g., `["A5", "A6", "A7"]`), while `SuggestionsAreMade.seatNames()` joins them with hyphens for display (e.g., `"A5-A6-A7"`). This follows the principle that formatting is a presentation concern, not a domain concern.
- **`DistanceFromRowCenter`** is now unused — the concept of "distance from row center" lives on inside `AdjacentSeats.of()`, which calculates window-center distance using the same `(rowSize + 1) / 2.0` formula. The class remains in the codebase as a discussion point.
- **Acceptance test expectations updated** — Lincoln-17 (party=2) now expects grouped format: `"A1-A2"`, `"A9-A10"`, `"B1-B2"` instead of individual seat names.

### The Deep Modeling Insight

The prototype revealed `AdjacentSeats` as a domain concept worth naming. It captures two ideas in one Value Object:
1. **Adjacency** — a group of contiguous seats that a party can sit in together
2. **Desirability** — measured by the group's center distance from the row center

By making this a named concept with natural ordering (`Comparable`), the algorithm becomes declarative: "find all possible adjacent seat groups, pick the best one."

### Design Decisions Worth Discussing

1. **Single method vs. two methods on Row**: The prototype initially suggested adding `suggestAdjacentSeatingOption()` alongside `suggestSeatingOption()`. But since adjacent seating with a window of 1 produces the same result as individual seat selection, we unified into a single method. This eliminated dead code and simplified the API. An alternative approach would use separate strategy classes — both are valid with different trade-offs.

2. **Display-layer formatting**: Moving hyphen-joining from `SuggestionIsMade` to `SuggestionsAreMade` is a separation of concerns decision. `SuggestionIsMade` is a domain object that knows about seats; `SuggestionsAreMade` is closer to the application boundary where formatting decisions live. This keeps the domain model free of presentation logic.

3. **DistanceFromRowCenter as dead code**: After unifying the method, `DistanceFromRowCenter` is no longer called anywhere. It represents the Lab 3 concept that was superseded by `AdjacentSeats`. This is a natural consequence of deep modeling — earlier concepts may be absorbed into deeper ones. Should it be deleted, or kept as documentation of the modeling journey?

4. **Window-center vs. sum-of-distances**: The algorithm ranks windows by their center's distance from the row center. An alternative approach sums each individual seat's distance from the center. Both produce the same results for the test cases, but they represent different domain intuitions: "is the group centered?" vs. "are the individual seats close?"

### What to Review

Look at the completed integration and notice:
- How `AdjacentSeats` encapsulates both the grouping concept and the ranking logic — a clean, small Value Object
- How the sliding window algorithm reads naturally: filter → find contiguous blocks → slide windows → rank by AdjacentSeats → pick best
- How the unified `suggestSeatingOption()` handles party=1 and party=N identically — no special cases
- How the formatting responsibility split between `SuggestionIsMade` and `SuggestionsAreMade` keeps domain objects free of display concerns
- How `DistanceFromRowCenter` becoming unused is a natural consequence of the model evolving — earlier stepping stones may be absorbed into deeper concepts

### Discussion Points

1. Is `AdjacentSeats` a concept a domain expert would recognize? What would they call it — "seating group"? "party block"? "seat cluster"?
2. Should `DistanceFromRowCenter` be deleted since it's unused, or does its presence document the modeling journey from Lab 3?
3. Compare the single-method approach with a two-strategy-classes approach. When would you prefer one over the other?
4. Where should formatting decisions like hyphenated seat names live in a DDD architecture? Is `SuggestionsAreMade` the right boundary?
5. How does the `AdjacentSeats` Value Object demonstrate the "making implicit concepts explicit" principle from Evans?

---

## After the Labs: Final Reflection

Take a few minutes to reflect on the full journey across all four labs.

### On the Domain Model

1. **How did the model evolve?** Compare the domain model at the end of Lab 1 with what you have now. Which objects survived unchanged? Which were refactored? Which are new?

2. **What did deep modeling reveal?** Labs 3 and 4 introduced prototyping outside the model. Did this approach help you discover concepts you wouldn't have found otherwise?

3. **Where did the model resist change?** Were there moments where adding a new requirement felt harder than expected? What does that tell you about the design?

### On Using AI Assistants

4. **How did your use of AI change across the labs?** Did you rely on it more or less as the labs progressed? Did you use it differently for prototyping vs. integration?

5. **Where did AI help most?** Was it more useful for implementing algorithms, reviewing designs, or exploring trade-offs?

6. **Where did AI fall short?** Were there design decisions where AI gave you an answer but you needed to think it through yourself?

7. **What would you do differently?** If you could restart all four labs, how would you balance your own thinking with AI assistance?

### On DDD Tactical Patterns

8. **Which tactical pattern was most valuable?** Value Objects, sealed interfaces, immutability, prototyping outside the model — which had the biggest impact on the code?

9. **What would you take to your own projects?** Which practices from these labs would improve your day-to-day codebase?
