# LibraTrack - Library Management System

LibraTrack is a modular, console-based Library Management System built in Java. The application is designed around core Object-Oriented Programming (OOP) principles, featuring strict separation of concerns, loose coupling via Java interfaces, and centralized custom exception handling for domain operations.

---

## Features

* **Book Management**: Add, track, and search for books using unique ISBN identifiers.
* **Member Management**: Register library members, track active borrow counts, and manage fine amounts.
* **Borrow & Return System**: Issue books with automated 30-day return policy tracking and standard date processing.
* **Automated Fine Calculation**: Calculates late return penalties (₹100/day after the 30-day borrowing window).
* **Search & System Reporting**: Query individual book availability or print real-time tabular system reports.
* **Robust Exception Handling**: Custom exception management for handling storage limits, missing entries, and state errors.

---

## Architecture & OOP Design Principles

* **Interface-Driven Design**: Core operations are decoupled using `LibraryService` and `Searchable` interfaces, ensuring loose coupling between UI components and backend business logic.
* **Separation of Concerns**: `MenuHandler` handles input/output processing via `Scanner`, while `Library` manages data persistence and operational logic.
* **Custom Exception Handling**: Centralized domain error management via `LibraryException` to cleanly handle invalid transactions without crashing the runtime.

---

## Project Structure

```text
LibraTrack/
└── src/
    ├── Book.java             # Entity class representing library books
    ├── Member.java           # Entity class representing registered members
    ├── Searchable.java       # Interface defining contract for search operations
    ├── LibraryService.java   # Core service interface for library operations
    ├── Library.java          # Service implementation containing business logic
    ├── LibraryException.java # Custom exception class for domain errors
    ├── MenuHandler.java      # UI layer processing menu options and CLI input
    └── LibraTrack.java       # Main entry point instantiating application context
