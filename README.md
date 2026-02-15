# Tactical Domain-Driven Design Training
*Model-Driven Design: From Collaborative Modelling to Code*

**Have you attended collaborative modelling sessions and wondered: "How does this translate into actual code?"**

Many developers learn EventStorming and bounded contexts, then struggle when it's time to implement. The sticky notes capture rich domain knowledge, but that knowledge gets lost when translating to code. Classes don't reflect the domain model. Business logic scatters across services. The alignment between what you modeled and what you built disappears within the first sprint.

Domain-Driven Design's tactical patterns offer a structured approach to maintain that alignment. Entities, value objects, aggregates, and domain events aren't just patterns—they're tools for preserving the shared understanding you built during collaborative modelling.

**In this intensive 2-day workshop, you'll learn how to translate collaborative modelling into production-ready code that authentically represents your domain.** Starting with EventStorming and Example Mapping, you'll practice responsibility-driven design to shape your domain model, then implement it using Test-Driven Development in Java, C#, or Kotlin. You'll experience the complete journey from problem space exploration to working code that maintains alignment with business needs.

## Prerequisites

Before starting the labs, ensure you have the following installed:

| Language | Required SDK |
|----------|-------------|
| **Java** | JDK 21+ |
| **C#** | .NET 10 SDK (LTS) |
| **Kotlin** | JDK 21+ (Gradle wrapper included) |

### Verify your installation

```bash
# Java
java -version   # Should show 21+

# C# / .NET
dotnet --version   # Should show 10.x

# Kotlin (uses included Gradle wrapper)
cd TheaterSuggestions/Kotlin && ./gradlew --version
```

### Verify you're ready for Lab 1

Run the ExternalDependencies tests to confirm your environment is set up correctly:

```bash
# Java
cd TheaterSuggestions/Java && mvn test

# C# / .NET
cd TheaterSuggestions/CSharp && dotnet test

# Kotlin
cd TheaterSuggestions/Kotlin && ./gradlew test
```

All tests should pass. If they do, you're ready to start Lab 1.

### Optional: AI-Assisted Development

The labs support AI-assisted coding with any AI tool (Claude Code, GitHub Copilot, Cursor, etc.). Each language folder contains an `AGENTS.md` file with project context, build commands, and domain reference that works with any AI assistant.

## Getting Started

1. **Open your language folder in your IDE:**
   - Java: `TheaterSuggestions/Java`
   - C#: `TheaterSuggestions/CSharp`
   - Kotlin: `TheaterSuggestions/Kotlin`
2. **Key files in each language folder:**
   - `PROJECT.md` - Domain rules and project structure
   - `AuditoriumLayoutExamples.md` - Visual examples of auditorium layouts
   - `AGENTS.md` - Context for AI-assisted development

Opening the language-specific folder as your project root ensures your IDE and AI tools have the correct context.

## About the workshop

Building software that truly reflects business needs requires more than understanding domain patterns—it requires maintaining alignment between your domain model and your code as both evolve. Many teams excel at collaborative modelling but struggle when implementation begins. The rich conversations from EventStorming sessions don't translate into code structure. Domain logic leaks into infrastructure. Six months later, nobody remembers why certain design decisions were made.
These model-driven design skills remain essential regardless of which development tools you use. As development automation advances—including AI-assisted coding—the ability to create clear, well-structured domain models becomes more critical, not less. To generate code that authentically represents your domain, you need explicit context about the domain model, responsibility allocation, and business rules—context that comes from intentional design work, not from better prompting.

This two-day hands-on workshop takes you through the complete model exploration whirlpool, from collaborative discovery to production code. Day one follows our Domain-Driven Design for Software Teams curriculum—you'll use EventStorming to explore the problem space, Context Mapping to identify bounded contexts, and build shared understanding through collaborative modelling.

Day two focuses on implementation. Through Example Mapping, you'll clarify acceptance criteria and business rules. Using responsibility-driven design with CRC cards, you'll deliberately allocate responsibilities across your domain model. Then, through three hands-on labs, you'll implement your design using outside-in Test-Driven Development, apply DDD tactical patterns (entities, value objects, aggregates, domain events), and explore deeper modelling techniques. We'll conclude by examining how hexagonal architecture (ports and adapters) protects your domain model from infrastructure concerns.

Throughout the workshop, you'll work with Java, C#, or Kotlin, implementing patterns that maintain the alignment between your collaborative model and your code. You'll leave with practical experience translating domain knowledge into maintainable, testable code that evolves alongside business needs.

**Training Approach:**

My training sessions are rooted in the "Training from the Back of the Room" and "Deep Democracy" methodology, emphasizing an immersive, hands-on experience. Approximately 70% of the content is practical and hands-on, with a higher proportion of implementation work on day two ensuring you actively engage with coding patterns you can apply immediately.

## Program

### Day 1: Understanding the Problem Space Through Collaborative modelling

**Module 1 - The What and Why of Domain-Driven Design**

- Check-in: Setting intentions and building connection
- Presentation: Why Domain-Driven Design matters for sustainable software
- Dialogue: The challenge of maintaining model-code alignment

**Module 2 - Understanding the Problem with EventStorming**

- Break-out: Exploring how you currently tackle problem discovery
- Hands-on: Use EventStorming to model business processes and uncover the problem space together
- Dialogue: Making the implicit explicit through collaborative visualization

**Module 3 - Distilling Bounded Contexts**

- Hands-on: Use Context Mapping to distill bounded contexts from your EventStorming insights
- Presentation: The Bounded Context pattern and why it matters
- Hands-on: Define purpose, identify user needs, and develop a ubiquitous language
- Presentation: Reframing the problem we're solving

### Day 2: From Domain Model to Production Code

**Module 4 - Clarifying Business Rules with Example Mapping**

- Presentation: Example Mapping for discovering business rules and acceptance criteria
- Hands-on: Use Example Mapping to make domain rules explicit
- Dialogue: How clear examples guide implementation decisions

**Module 5 - Designing Your Domain Model**

- Presentation: Responsibility-driven design and CRC cards
- Hands-on: Use CRC cards to allocate responsibilities across your domain model
- Reflection: Ensuring your design reflects the ubiquitous language

**Module 6 - Lab 1: Outside-In Test-Driven Development**

- Presentation: Outside-in TDD for domain modelling
- Hands-on: Implement your first domain behaviour using outside-in TDD
- Dialogue: How tests document domain knowledge

**Module 7 - Lab 2: Implementing DDD Tactical Patterns**

- Presentation: Entities, value objects, aggregates, and domain events
- Hands-on: Implement core tactical patterns in Java, C#, or Kotlin
- Dialogue: When to use each pattern and how they interconnect
- Reflection: Maintaining alignment between model and code

**Module 8 - Lab 3: Deeper Modelling Techniques**

- Hands-on: Apply advanced modelling techniques to complex scenarios
- Dialogue: Refining your model as understanding deepens
- Presentation: Protecting your domain with hexagonal architecture (ports and adapters)

**Module 9 - Integrating Model-Driven Design into Your Practice**

- Dialogue: Maintaining model-code alignment over time
- Presentation: Next steps—from workshop code to production systems
- Check-out: Reflect, share takeaways, and commit to next steps

## What you will learn

- **Translate Collaborative Models into Working Code**
Discover how to maintain the alignment between collaborative modelling sessions and code implementation. You'll learn how DDD tactical patterns—entities, value objects, aggregates, and domain events—preserve the domain knowledge from EventStorming and Example Mapping in your codebase. This explicit domain modelling becomes essential when using AI coding assistants: to generate code that authentically represents your domain, AI needs clear context about business rules, responsibility allocation, and invariants—context that emerges from intentional design and collaborative modelling, not from prompts alone.
- **Practice Responsibility-Driven Design**
Learn to deliberately allocate responsibilities across your domain model using CRC cards. Through hands-on exercises, you'll discover how to identify which concepts should be entities versus value objects, where behavior belongs, and how to design aggregates that protect business invariants.
- **Implement Domain Models with Test-Driven Development**
Apply outside-in TDD to implement your domain model in Java, C#, or Kotlin. You'll write tests that document domain knowledge, implement tactical patterns that reflect your ubiquitous language, and create code that remains maintainable as requirements evolve.
- **Protect Your Domain from Infrastructure Concerns**
Understand how hexagonal architecture (ports and adapters) isolates your domain model from frameworks, databases, and external services. You'll see how this separation enables your domain code to focus purely on business logic while remaining adaptable to infrastructure changes.

Approximately 70% of this workshop is practical and hands-on, with day two focused heavily on implementation. You won't just learn about these patterns—you'll implement them through realistic scenarios, building confidence you can take directly into your next development sprint.

## After This Training

You'll be able to:

- Translate EventStorming and Example Mapping outcomes into object-oriented domain models
- Use responsibility-driven design and CRC cards to allocate behaviour across your model
- Identify when to use entities, value objects, aggregates, and domain events in your design
- Apply outside-in Test-Driven Development to implement domain behaviour
- Write code in Java, C#, or Kotlin that authentically represents your domain model
- Implement DDD tactical patterns that maintain alignment with your ubiquitous language
- Protect domain logic using hexagonal architecture principles
- Maintain model-code alignment as both your understanding and requirements evolve
- Provide clear domain context for development—whether coding manually or using AI assistants—through explicit models and responsibility allocation
- Refactor toward deeper insight as your domain understanding grows

## About the instructors

### Kenny Baas-Schwegler
Kenny Baas-Schwegler believes in collaborative software design where ‘every voice shapes the software.’ Leveraging a domain-driven design approach, Kenny facilitates more transparent communication between stakeholders and software creators by collaborative modeling and deep democracy, decoding complexities, resolving conflicts, and ensuring software remains agile to business demands.

As an independent software consultant, tech lead, and software architect, Kenny catalyzes organizations and teams to design and build sustainable and resilient software architectures.


