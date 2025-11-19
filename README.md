# Live Event Ticketing DBMS

A comprehensive Database Management System for live event ticketing, developed using JavaFX for the user interface and MySQL for the database backend. This application provides a multi-user environment with distinct roles and functionalities for Administrators, Event Organizers, Artists, and Customers.

## Features

The system is designed with four user roles, each with a dedicated dashboard and specific capabilities:

### 1. Admin
The administrator has full oversight and control over the platform's core data.
- **User Management:** Perform full CRUD (Create, Read, Update, Delete) operations on Artists, Customers, Venues, and Organizers. Entities are soft-deleted by setting their status to 'Inactive'.
- **Data Viewing:** View events associated with any Artist, Venue, or Organizer.
- **Reporting:** Generate and view detailed monthly reports on:
    - **Ticket Sales:** Total revenue and tickets sold per event.
    - **Customer Purchases:** Top spending customers.
    - **Ticket Refunds:** Number of tickets refunded per event.
    - **Artist Payments:** Total commission payouts to artists.

### 2. Organizer
Event organizers are responsible for creating and managing events.
- **Event Creation:** Create new events with details such as venue, artist, date, time, capacity, and ticket price. The system validates that the selected artist and venue are 'Active' and that event capacity does not exceed venue capacity.
- **Commission Payout:** Initiate and record commission payouts for completed events.

### 3. Artist
Artists can view their performance schedules.
- **View Events:** Access a read-only list of all events they are scheduled to perform in and events they finished, including details like event name, venue, organizer, and date/time.

### 4. Customer
Customers can browse events and manage their tickets.
- **Purchase Tickets:** View a list of all 'Upcoming' events and purchase tickets. The available capacity for each event is displayed in real-time.
- **Cancel Tickets:** Cancel tickets for 'Upcoming' events, which automatically processes a refund and updates the ticket status.

## Technology Stack
- **Frontend:** JavaFX, FXML
- **Backend:** Java
- **Database:** MySQL
- **Build Tool:** Gradle

## Database Schema
The application utilizes a relational database schema to manage the data efficiently.

- **`Venue`**: Stores details about event locations, including name, capacity, and location.
- **`Artist`**: Contains information about performers, such as name, email, genre, and management company.
- **`Organizer`**: Holds data for event organizers.
- **`Customer`**: Manages customer profiles and registration information.
- **`Event`**: A central table linking venues, artists, and organizers for each event. It includes event-specific details like date, time, and ticket price.
- **`Ticket`**: Represents a ticket purchased by a customer for an event, tracking its status ('Active', 'Cancelled').
- **`Cancellation`**: Logs all ticket cancellations, including the refund amount and date.
- **`CommissionPayout`**: Records commission payments made to artists and organizers for events.

## Setup and Installation

Follow these steps to set up and run the project locally.

### Prerequisites
- Java Development Kit (JDK) 11 or higher
- MySQL Server

### 1. Database Setup
1.  Start your MySQL server.
2.  Create a database named `ticketsystem`.
    ```sql
    CREATE DATABASE ticketsystem;
    ```
3.  Connect to the new database and run the schema script to create the necessary tables:
    - Execute the SQL commands in `src/main/sql/ticketsystem.sql`.
4.  (Optional) To populate the database with sample data, run the mock data script:
    - Execute the SQL commands in `src/main/sql/mockdata.sql`.
5.  Configure the database connection:
    - Open `src/main/java/com/dbms/utils/Database.java`.
    - Update the `URL`, `USER`, and `PASSWORD` constants to match your MySQL configuration. The default values are:
        - **URL:** `jdbc:mysql://localhost:3306/ticketsystem`
        - **USER:** `root`
        - **PASSWORD:** `admin`

### 2. Running the Application
1.  Clone the repository:
    ```bash
    git clone https://github.com/wayneqmatthew/live-event-ticketing-dbms.git
    ```
2.  Navigate to the project's root directory:
    ```bash
    cd live-event-ticketing-dbms
    ```
3.  Build and run the application using the Gradle wrapper:
    - On macOS/Linux:
      ```bash
      ./gradlew run
      ```
    - On Windows:
      ```bash
      .\gradlew.bat run
      ```

## How to Use

The application features a role-based login system.

-   **Admin:**
    -   **Username:** `admin`
    -   **Password:** `admin`

-   **Organizer, Artist, and Customer:**
    -   **Identifier:** Use the corresponding `organizer_id`, `artist_id`, or `customer_id` from the database. If you used the `mockdata.sql` script, you can use any ID from 1 to 10 for each role.
    -   **Password:** `admin` (for all non-admin users)

Once logged in, you can navigate through the user-specific interface to access the features outlined above.
