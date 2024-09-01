# MovieApp

MovieApp is an Android application that allows users to explore movies, view details, and save their favorite movies. This project demonstrates the use of modern Android development tools and practices, including MVVM architecture, Hilt for dependency injection, Room for local data storage, Retrofit for API requests, and Jetpack Compose for the UI.

## Screenshots

<img src="https://github.com/user-attachments/assets/f5337801-6da3-48ff-ad88-fbf6a5d86770" alt="App Screenshot" width="300"/>
<img src="https://github.com/user-attachments/assets/842a648e-7524-4189-a461-838d938f05ef" alt="App Screenshot" width="300"/>
<img src="https://github.com/user-attachments/assets/d8f4f3d5-9180-4812-85e0-6f2cb8b64eda" alt="App Screenshot" width="300"/>
<img src="https://github.com/user-attachments/assets/43537017-a9e3-43fc-bed1-c16db8668c6d" alt="App Screenshot" width="300"/>
<img src="https://github.com/user-attachments/assets/975660b0-b922-4410-b9f1-638cce1d474a" alt="App Screenshot" width="300"/>
<img src="https://github.com/user-attachments/assets/6fea8465-af04-41c9-8160-880c0143b8a7" alt="App Screenshot" width="300"/>

## Features

- **Browse Popular Movies**: Explore a list of popular movies fetched from the MovieDB API.
- **Search Movies**: Search for movies by title.
- **View Movie Details**: Click on any movie to view detailed information, including ratings and reviews.
- **Favorites**: Save your favorite movies for easy access later.
- **Responsive UI**: The app is designed to work on different screen sizes and orientations.

## Technologies Used

- **Kotlin**: Programming language.
- **Jetpack Compose**: For building the UI.
- **MVVM Architecture**: Model-View-ViewModel architecture pattern.
- **Hilt**: Dependency injection.
- **Retrofit**: For making HTTP requests to the MovieDB API.
- **Room**: Local database for saving favorite movies.
- **Paging 3**: For efficient pagination of movie lists.
- **Coroutines**: For handling asynchronous tasks.

## Setup Instructions

1. **Clone the repository**:
    ```bash
    git clone https://github.com/muratozcan8/MovieAppWithJetpackCompose.git
    ```

2. **Open the project in Android Studio**:
   - Make sure you have the latest version of Android Studio installed.

3. **Add your API key**:
   - Obtain an API key from [The Movie Database (TMDB)](https://www.themoviedb.org/documentation/api).
   - Add your API key to the `local.properties` file:
     ```
     BEARER=your_bearer_key_here
     ```

4. **Build the project**:
   - Sync the project with Gradle files and build it.

5. **Run the app**:
   - Select a device or emulator and run the app.

