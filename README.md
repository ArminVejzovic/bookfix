# bookfix

📚 Bookfix

Bookfix is an Android application designed to help users manage their reading journey by tracking books, marking their status, adding them to a wishlist, and sharing them with friends. The app allows users to easily interact with books via features like searching for books, viewing details, and buying them from Amazon. It offers a personalized reading experience with clear navigation and status management for each book.

🛠️ Tech Stack

UI Development: Jetpack Compose with fully Composable functions to build dynamic and responsive UIs.

Architecture: The app follows the MVVM (Model-View-ViewModel) pattern for better state management and clean separation of concerns.

Navigation: Utilizes NavHost, NavController, and NavGraph to efficiently handle screen transitions and user flow.

Adaptive Layout: Supports different layouts for mobile and tablet devices, following the list-details pattern for larger screens.

API Integration: Connects to a book database API using the Retrofit library for fetching book details and keeping the app data up-to-date.

Offline Support: The app handles cases when no internet connection is available

📱 App Features and Components
1. Book List

    The home screen displays a list of all books, with cover images and Buy on Amazon buttons for every book

   Users can search for specific books using the search bar at the top.

3. Book Details

    Clicking on a book opens a detailed view showing:
        Book Cover Image

        Author

        ISBN

        Rank

        Description

        Amazon Purchase Link: Buy the book directly from the Amazon store using the Buy on Amazon button.

   Below the book details, users can:

       Mark as Finished

       Add to Wishlist

       Mark as Reading

   Only one status can be active at a time, and the active status can be reversed with options like Unfinish Book if it’s marked as finished.

5. Book Status Management

    Easily switch between different book statuses:

         - Mark as Finished: Moves the book to the finished books category.

         - Add to Wishlist: Saves the book for future reading in the wishlist.

         - Mark as Reading: Keeps track of books the user is currently reading.

   Users can reverse the active status if desired

7. Navigation Bar

    The navbar allows users to filter their book lists:

       All Books: Displays the full list of books.

       Finished Books: Only shows books that have been marked as finished.

       Wishlist Books: Displays books added to the wishlist.

       Reading Books: Lists books currently marked as reading.

9. Share Feature

    Users can share book details with friends using a built-in share button, accessible from the book details view.

10. Offline Mode

    The app will notify users when there is no internet connection
   
📦 Installation

Clone the repository:

bash

    git clone https://github.com/your-username/bookfix.git

Open the project in Android Studio.

Sync the Gradle files to install the necessary dependencies.

Run the app on an emulator or Android device:

- Select an emulator or device from the AVD Manager.

- Press Run (Shift + F10).


