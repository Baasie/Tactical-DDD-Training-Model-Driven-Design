# Training Lab Instructions

This file contains training-specific instructions. In a real project, this file would not exist.

---

## Lab 1: Outside-In TDD

### Learning Goal

Learn to translate CRC cards into working code using Outside-In TDD.

### What You Have

- **Acceptance tests** with commented code showing exactly what to call
- **Your CRC cards** from the earlier modeling session
- **One example CRC card** in PROJECT.md showing how cards translate to code
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

