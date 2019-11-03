## Screenshot
<img src="Screenshot.png" alt="Screenshot" width="300"/>

## Instructions
- Android Studio 3.5.1, Gradle 5.4.1, Kotlin 1.3.50
- Just unzip and open the folder in Android Studio, wait for the project to sync, then click "Run"
- If it doesn't compile, there is a prebuilt `app/release/app-release.apk`

## Features
- Requests the top posts from http://reddit.com/
- Displays a progress indicator while the request is performed
- Shows title, author, and subreddit for each of the posts
- Posts are sorted by the number of upvotes, descending
- If a post has images, displays the smallest of them (smallest by area)
- If an error happens while requesting images, an error message is displayed along with "Retry" button
- Orientation changes are handled and the posts are displayed without reloading
- Images are cached by Glide library
- A click on a post opens the screen with post details

## Architecture
The project uses MVVM architecture in which the `View` (`PostsActivity`) is subscribed to `PostsViewModel`'s stream of `PostsUiState` objects, each of them representing the entire state of `View` at a given point in time. `PostsUiState` is Kotlin's sealed class, and it has 3 subclasses (`Loading`, `Loaded`, and `Error`), for each of the states currently supported, with their own properties: `Loaded` contains a list of posts, while `Error` holds an error message. The `View` in this approach is as dumb as possible and doesn't contain any logic - all logic is put into unit-testable classes.
There is a layer of data transfer objects (DTO) in `GetPostsResponse.kt` which are used to communicate with the server. They are deserialized from JSON using Jackson library and transformed into a list of `model.Post` instances that are closer to the UI representation than the DTO classes.
The activity with post details is trivial and only contains a `WebView`, so UI logic is not separated for it.

## Third-party libraries
- [RxJava 2](https://github.com/ReactiveX/RxJava)/[RxKotlin](https://github.com/ReactiveX/RxKotlin)/[RxAndroid](https://github.com/ReactiveX/RxAndroid) for reactive programming
- [Glide](https://github.com/bumptech/glide) for easy image loading with caching functionality
- [Jackson](https://github.com/FasterXML/jackson) for JSON deserialization
- [Retrofit](https://square.github.io/retrofit/) for network, plus adapters for [Jackson](https://github.com/square/retrofit/tree/master/retrofit-converters/jackson) and [RxJava 2](https://github.com/square/retrofit/tree/master/retrofit-adapters/rxjava2)
- `ConstraintLayout`, `RecyclerView`, and `CardView` from AndroidX
- `JUnit` for unit testing
- [mockk](https://github.com/mockk/mockk) to mock objects in unit tests

## Libaries I considered
- `Gson` for Jackson deserialization. It's tricky to make it deserialize sealed classes, which might be needed in future given the specific protocol Reddit uses (the same structure for different kinds of responses).
- `Picasso` and `Fresco` for image loading. The currently used solution, Glide, makes it hard to limit the maximum height of the image being loaded - when you try to do it, you observe various visual artifacts.
- `Dagger` for dependency injection. To be honest, I just don't like Dagger - it has a steep learning curve, it requires a lot of boilerplate and leads to confusing compilation errors. So I took this challenge as an opportunity to try implementing DI myself.
- `Koin` for dependency injection. That would actually be nice to try, but didn't have time for it.
- `Mockito` for mocking in unit tests. I used it successfully before, but it's Java-centric, while `mockk` is Kotlin-first.

## Unit tests
See `PostTransformerTest.kt` and `PostViewModelTest.kt` - they are very self-descriptive.

## Ideas:
- Currently there is no way to refresh the feed during the app session. A pull-to-refresh feature or "refresh" button if you scroll far enough would be cool.
- As only the first page is displayed, it's easy to reach the bottom of the feed. Would be nice to implement infinite scroll.
- To improve user experience and show images quicker, we could start with a low-resolution version of the image, displaying the full resolution on click or after it's loaded.
- On large screens (such as tablets), we could display the list of posts at the left and selected post's details at the right part of the screen.
- Some of the solutions I used are a bit outdated. This tech debt could be resolved by introducing Android Jetpack components to the codebase, namely ViewModel, LiveData, Lifecycle, Compose.
- There is no toolbar in the app as I couldn't find any use for it, but there could be one with search field.
- We could add a "Share" button to the post details screen, possibly as part of a toolbar.
- Would be nice to limit the maximum height for images, as they can be pretty tall and occupy the entire screen.
