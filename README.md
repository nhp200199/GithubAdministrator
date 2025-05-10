# GitHub User Explorer (Android)

This Android application allows users to browse and view details of GitHub users using the [GitHub REST API](https://docs.github.com/en/rest/users?apiVersion=2022-11-28). It leverages modern Android development technologies to provide a smooth and efficient user experience.

## Features

* **Browse Users:** Displays a paginated list of GitHub users fetched from the API.
* **User Details:** Navigate to a detailed screen for each user to view their information (e.g., avatar, bio, followers, following, public repos).
* **Offline Support:** Fetched user data is cached locally using Room, allowing users to view previously accessed information even when offline.
* **Efficient Data Loading:** Utilizes the Paging library to load data in chunks, improving performance and reducing network usage.
* **Clean Architecture:** Follows a clean architecture pattern to separate concerns and improve code maintainability and testability.
* **Unit Testing:** Includes comprehensive unit tests to ensure the reliability and correctness of the core logic.

## Technology Stacks

This project is built using the following Android technologies:

* **Compose:** A modern declarative UI toolkit for building native Android UIs.
* **Navigation:** Jetpack Navigation component for managing in-app navigation between screens.
* **Coroutines:** Kotlin's concurrency framework for handling asynchronous operations.
* **Flow:** Kotlin's reactive streams for handling asynchronous data streams.
* **Paging:** Jetpack Paging 3 library for efficiently loading and displaying large lists of data from the network.
* **Room:** Jetpack Room persistence library for local data caching and offline access.
* **JUnit:** Standard Java testing framework for unit testing.
* **MockK:** A mocking library for Kotlin to create test doubles.
* **Hamcrest:** An assertion library for writing expressive and readable unit tests.

## Architecture

The project follows a Clean Architecture pattern with the following layers:

* **Presentation (Compose UI):** Responsible for displaying data to the user and handling user interactions. Built using Jetpack Compose.
* **UI Model (ViewModel):** Holds the UI state and interacts with the Domain/Data layers to fetch and process data. Uses Kotlin Flow to emit UI state updates.
* **Data (Repository):** Acts as a single source of truth for data. Abstracts the data sources (remote API and local database).
* **Remote (Network):** Handles communication with the GitHub API using Retrofit.
* **Local (Database):** Manages the local data storage using Jetpack Room.
