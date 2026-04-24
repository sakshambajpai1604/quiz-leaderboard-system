# Quiz Leaderboard System - Backend Integration

## Project Overview
This project is a Java-based backend integration application developed for the SRM Quiz Task Qualifier. It simulates a real-world distributed system scenario where an external validator API provides intermittent, paginated, and potentially duplicated scoring data for a quiz show. 

The application reliably consumes this data, ensures absolute accuracy through strict deduplication, aggregates the final scores, and securely submits the computed leaderboard back to the validator.

## Key Features
- **Automated Polling Mechanism:** Executes exactly 10 sequential GET requests with a strict, mandated 5-second network delay between calls.
- **Robust Deduplication:** Prevents duplicate scoring by generating a unique composite key (`roundId + participant`) mapped against a high-performance `HashSet`.
- **Score Aggregation:** Dynamically calculates total scores for an arbitrary number of participants using a `HashMap`.
- **Automated Sorting:** Generates a final leaderboard strictly ordered by `totalScore` in descending order.
- **Idempotent Submission:** Packages the aggregated data into a structured JSON payload and executes a single, final POST submission.

## Tech Stack
- **Language:** Java 11+ (Utilizing the native `java.net.http.HttpClient`)
- **Build Tool:** Maven
- **JSON Parser:** Jackson Databind (v2.15.2) - *Chosen for robust POJO serialization/deserialization.*

## Project Structure
This project uses a streamlined, highly readable structure:
```text
quiz-leaderboard/
├── src/
│   ├── main/java/
│   │   ├── QuizLeaderboard.java     # Core logic: Polling, Deduplication, Math, and Submission
│   │   └── DataModels.java          # POJOs for mapping GET and POST JSON payloads
├── .gitignore                       # Excludes target/ and IDE settings
├── pom.xml                          # Maven dependencies and configuration
└── README.md                        # Project documentation
