-- =============================================
-- TICKETING DATABASE INITIALIZATION SCRIPT
-- CCINFOM S21
-- Group 2: Bueno, Jonathan R.
-- 			De Gracia, Kaleela Ysabel B.
-- 			Guanzon, Melangelo P.
-- 			Quebrado, Wayne Matthew V.
-- =============================================

-- Drop and recreate the user with correct password
DROP USER IF EXISTS 'root'@'localhost';
CREATE USER 'root'@'localhost' IDENTIFIED BY 'admin';

-- Grant all privileges
GRANT ALL PRIVILEGES ON *.* TO 'root'@'localhost' WITH GRANT OPTION;
FLUSH PRIVILEGES;

DROP DATABASE IF EXISTS ticketsystem;
CREATE DATABASE ticketsystem/*!40100 DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci */;
USE ticketsystem;

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;


-- ============================
-- Table setup for Venue table
-- ============================
DROP TABLE IF EXISTS Venue;
/*!40101 SET @saved_cs_client = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE Venue (
    venue_id INT PRIMARY KEY AUTO_INCREMENT,		-- Unique ID for each venue
    name VARCHAR(100),				-- Name of the venue
    capacity INT,					-- Seating capacity
    city VARCHAR(50),				-- City where the venue is located
    country VARCHAR(50),			-- Country of the venue
    region VARCHAR(50),				-- Region
    status VARCHAR(15)              -- Status
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;


-- =============================
-- Table setup for Artist table
-- =============================
DROP TABLE IF EXISTS Artist;
/*!40101 SET @saved_cs_client = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE Artist (
    artist_id INT PRIMARY KEY AUTO_INCREMENT,				-- Unique ID for each artist
    name VARCHAR (100),                     -- Artist name
    email VARCHAR (255),                    -- Artist email
    genre VARCHAR(50),						-- Genre of the artist
    management_company VARCHAR(100),		-- Name of the managing company
    status VARCHAR(15)                      -- status
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

-- ================================
-- Table setup for Organizer table
-- ================================
DROP TABLE IF EXISTS Organizer;
/*!40101 SET @saved_cs_client = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE Organizer (
    organizer_id INT PRIMARY KEY AUTO_INCREMENT,		-- Unique ID for each organizer
    name VARCHAR (100),                 -- name of organizer
    email VARCHAR (255),                -- email of organizer
    region VARCHAR(50),					-- Region where it is located
    country VARCHAR(50),				-- Country where it is located
    city VARCHAR(50),					-- City where it is located
    status VARCHAR(15)                  -- status
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

-- ================================
-- Table setup for Customer venuetable
-- ================================
DROP TABLE IF EXISTS Customer;
/*!40101 SET @saved_cs_client = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE Customer (
    customer_id INT PRIMARY KEY AUTO_INCREMENT,		-- Unique ID for each customer
    last_name VARCHAR(50),				-- Customer's last name
    first_name VARCHAR(50),				-- Customer's first name		
    email VARCHAR(100),					-- Customer's email
    phone_number VARCHAR(20),			-- Customer's phone number		
    registration_date date,				-- Date when customer registered
    status VARCHAR(15)                  -- status
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

-- ================================
-- Table setup for Event table
-- ================================
DROP TABLE IF EXISTS Event;
/*!40101 SET @saved_cs_client = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE Event (
    event_id INT PRIMARY KEY AUTO_INCREMENT,		-- Unique ID fore each event
    venue_id INT,					-- Foreign key connecting Event to Venue
    artist_id INT,					-- Foreign key connecting Event to Artist
    organizer_id INT,				-- Foreign key connecting Event to Organizer
    event_name VARCHAR(100),		-- Name of the event
    time TIME,						-- Time of the event
    date DATE,						-- Date of the event
    capacity INT,					-- Number of tickets the event can sell
    ticket_price FLOAT,             -- Ticket price for each evemnt
    status VARCHAR(20),				-- The status of the ticket (i.e "Upcoming")
    FOREIGN KEY (venue_id) REFERENCES Venue(venue_id),
    FOREIGN KEY (artist_id) REFERENCES Artist(artist_id),
    FOREIGN KEY (organizer_id) REFERENCES Organizer(organizer_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

-- ===================================
-- Table setup for Cancellation table
-- ===================================
DROP TABLE IF EXISTS Cancellation;
/*!40101 SET @saved_cs_client = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE Cancellation (
    cancellation_ref_id INT PRIMARY KEY AUTO_INCREMENT,		-- Unique ID for each cancellation
    ticket_id INT,								-- Foreign key connecting Cancellation to Ticket
    refund_amount DECIMAL(10,2),				-- Amount to refund
    cancellation_date DATE,						-- Date the ticket was cancelled
    FOREIGN KEY (ticket_id) REFERENCES Ticket(ticket_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

-- ================================
-- Table setup for Ticket table
-- ================================
DROP TABLE IF EXISTS Ticket;
/*!40101 SET @saved_cs_client = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE Ticket (
    ticket_id INT PRIMARY KEY AUTO_INCREMENT,			-- Unique ID for each ticket
    event_id INT,						-- Foreign key connecting Ticket to Event
    customer_id INT,					-- Foreign key connecting Ticket to Customer
    purchase_date DATE,					-- Date when the ticket was purchased
    price DECIMAL(10,2),				-- Price of the ticket
    status VARCHAR(20),					-- Status of the ticket (i.e "Active", "Cancelled")
    FOREIGN KEY (event_id) REFERENCES Event(event_id),
    FOREIGN KEY (customer_id) REFERENCES Customer(customer_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

-- =======================================
-- Table setup for CommissionPayout table
-- =======================================
DROP TABLE IF EXISTS CommissionPayout;
/*!40101 SET @saved_cs_client = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE CommissionPayout (
    payout_ref_id INT PRIMARY KEY AUTO_INCREMENT,			-- Unique ID for each payout
    artist_id INT,							-- artist_id for artist payout
    organizer_id INT,                       -- organizer_id for organizer payout
    commission_amount DECIMAL(10,2),		-- Total commission amount
    total_ticket_sales INT,					-- Total sales from tickets
    commission_percentage DECIMAL(5,2),		-- Commission rate of the organizer/artist
    payout_date DATE,						-- Date when the amount was paid
    FOREIGN KEY (artist_id) REFERENCES Artist(artist_id),
    FOREIGN KEY (organizer_id) REFERENCES Organizer(organizer_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;
