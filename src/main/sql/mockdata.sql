-- =============================================
-- INSERTING MOCK DATA INTO TICKETING DATABASE
-- CCINFOM S21
-- Group 2: Bueno, Jonathan R.
-- 			De Gracia, Kaleela Ysabel B.
-- 			Guanzon, Melangelo P.
-- 			Quebrado, Wayne Matthew V.
-- =============================================

-- =============================
-- Dumping data for table Venue
-- =============================
LOCK TABLES Venue WRITE;
/*!40000 ALTER TABLE Venue DISABLE KEYS */;
INSERT INTO Venue (name, capacity, city, country, region, status) VALUES
('Mall of Asia Arena', 15000, 'Pasay City', 'Philippines', 'Asia', 'Active'),
('Philippine Arena', 55000, 'Bocaue', 'Philippines', 'Asia', 'Active'),
('Smart Araneta Coliseum', 16000, 'Quezon City', 'Philippines', 'Asia', 'Active'),
('Seoul Olympic Stadium', 68900, 'Songpa-gu District', 'South Korea', 'Asia', 'Active'),
('Jakarta International Stadium', 82000, 'Jakarta', 'Indonesia', 'Asia', 'Active'),
('SM North Edsa Skydome', 1500, 'Quezon City', 'Philippines', 'Asia', 'Active'),
('Beijing National Stadium', 80000, 'Chaoyang', 'China', 'Asia', 'Active'),
('Husky Stadium', 70038, 'Seattle', 'United States', 'North America', 'Active'),
('Nissan Stadium', 72327, 'Yokohama', 'Japan', 'Asia', 'Active'),
('SoFi Stadium', 70240, 'Inglewood', 'United States', 'North America', 'Active');
/*!40000 ALTER TABLE Venue ENABLE KEYS */;
UNLOCK TABLES;

-- ==============================
-- Dumping data for table Artist
-- ==============================
LOCK TABLES Artist WRITE;
/*!40000 ALTER TABLE Artist DISABLE KEYS */;
INSERT INTO Artist (name, email, genre, management_company, status) VALUES
('Taylor Swift', 'taylorswift@gmail.com', 'Pop', '13 Management', 'Active'),
('Sabrina Carpenter', 'sabrinacarpenter@gmail.com', 'Pop', 'Volara Management', 'Active'),
('SEVENTEEN', 'seventeen@gmail.com', 'Kpop', 'HYBE Entertainment', 'Active'),
('Lea Salonga', 'leasalonga@gmail.com', 'OPM', NULL, 'Active'),
('Jose Mari Chan', 'josemarichan@gmail.com', 'Pop', NULL, 'Active'),
('EJ Miranda', 'ejmiranda@gmail.com', 'Christian Music', NULL, 'Active'),
('BTS', 'bts@gmail.com', 'Kpop', 'HYBE Entertainment', 'Active'),
('TWICE', 'twice@gmail.com', 'Kpop', 'JYP Entertainment', 'Active'),
('KATSEYE', 'katseye@gmail.com', 'Pop', 'HYBE Entertainment', 'Active'),
('ARTMS', 'artms@gmail.com', 'Kpop', 'Modhaus Incorporated', 'Active');
/*!40000 ALTER TABLE Artist ENABLE KEYS */;
UNLOCK TABLES;

-- =================================
-- Dumping data for table Organizer
-- =================================
LOCK TABLES Organizer WRITE;
/*!40000 ALTER TABLE Organizer DISABLE KEYS */;
INSERT INTO Organizer (name, email, city, country, region, status) VALUES
('Live Nation PH', 'livenation@gmail.com', 'Taguig City', 'Philippines', 'Asia', 'Active'),
('PULP Live World', 'pulpliveworld@gmail.com', 'Quezon City', 'Philippines', 'Asia', 'Active'),
('Insignia Presents', 'insigniapresents@gmail.com', 'Taguig City', 'Philippines', 'Asia', 'Active'),
('SHOWNOTE Incorporated', 'shownote@gmail.com', 'Seoul', 'South Korea', 'Asia', 'Active'),
('FNC Entertainment', 'fncent@gmail.com', 'Seoul', 'South Korea', 'Asia', 'Active'),
('TINYgMUSIC', 'tinygmusic@gmail.com', 'London', 'United Kingdom', 'Europe', 'Active'),
('Anschutz Entertainment Group', 'aegpresents@gmail.com', 'London', 'United Kingdom', 'Europe', 'Active'),
('Insomniac Events', 'insomniacevents@gmail.com', 'Orlando', 'United Sates', 'North America', 'Active'),
('Music Viva Australia', 'musicvivaaus@gmail.com', 'New South Wales', 'Australia', 'Australia', 'Active'),
('Untitled Group', 'untitledgroup@gmail.com', 'Melbourne', 'Australia', 'Australia', 'Active'),
('Smash', 'smash@gmail.com', 'Osaka', 'Japan', 'Asia', 'Active');
/*!40000 ALTER TABLE Organizer ENABLE KEYS */;
UNLOCK TABLES;

-- ================================
-- Dumping data for table Customer
-- ================================
LOCK TABLES Customer WRITE;
/*!40000 ALTER TABLE Customer DISABLE KEYS */;
INSERT INTO Customer (last_name, first_name, email, phone_number, registration_date, status) VALUES
('Harrison', 'Tyson', 'harrisontyson@gmail.com', '09370976099', '2022-02-28', 'Active'),
('Li', 'Hosea', 'hoseali@gmail.com', '09788507978', '2023-12-15', 'Active'),
('Woodward', 'Sandford', 'sandfordwood@gmail.com', '09977676722', '2023-08-30', 'Active'),
('Silva', 'Juliet', 'julietsilva@gmail.com', '09107202889', '2023-08-13', 'Active'),
('Andrews', 'Abigail', 'abigailandrews@gmail.com', '09491162032', '2021-08-31', 'Active'),
('Johns', 'Herbert', 'herbertjohns@gmail.com', '09623464605', '2021-08-08', 'Active'),
('Pacheco', 'Lucile', 'lucilepacheco@gmail.com', '09445388236', '2018-05-06', 'Active'),
('Mathis', 'Lonny', 'lonnymathis@gmail.com', '09459034062', '2017-12-10', 'Active'),
('Tate', 'Gale', 'galetate@gmail.com', '09936226463', '2017-09-04', 'Active'),
('Holder', 'Michael', 'michaelholder@gmail.com', '09491231816', '2017-08-25', 'Active'),
('De Gracia', 'Kaleela', 'kaleelaysabel@gmail.com', '09957451018', '2025-11-13', 'Active');
/*!40000 ALTER TABLE Customer ENABLE KEYS */;
UNLOCK TABLES;

-- =============================
-- Dumping data for table Event
-- =============================
/*!40000 ALTER TABLE Event DISABLE KEYS */;
INSERT INTO Event (venue_id, artist_id, organizer_id, event_name, time, date, capacity, ticket_price , status) VALUES
(7, 1, 3, 'The Eras Tour', '18:00:00', '2026-03-09', 70000, 3000.00,'Upcoming'),
(6, 2, 1, 'Short n Sweet Tour', '18:00:00', '2025-03-17', '1500', 13000.50, 'Done'),
(5, 4, 6, 'Stage, Screen and Everything In Between', '19:00:00', '2026-06-18', 81500, 2000.25,'Upcoming'),
(3, 5, 4, 'Here and Now: Celebrating the Music of Jose Mari Chan', '17:00:00', '2025-11-25', 16000, 13000.50,'Upcoming'),
(3, 7, 4, 'Permission to Dance on Stage', '18:00:00', '2027-02-28', 16000, 3000.00,'Upcoming'),
(6, 1, 6, 'The Eras Tour', '19:00:00', '2025-10-28', 1500, 2000.25,'Done'),
(9, 10, 6, 'Lunar Theory World Tour', '20:00:00', '2025-05-14', 72300, 13000.50,'Done'),
(4, 8, 1, 'This is For: World Tour', '19:00:00', '2025-10-04', 68900, 16000,'Done'),
(1, 2, 7, 'Short n Sweet Tour', '18:00:00', '2025-07-18', 14500, 3000.00,'Done');
/*!40000 ALTER TABLE Event ENABLE KEYS */;
UNLOCK TABLES;

-- ==============================
-- Dumping data for table Ticket
-- ==============================
/*!40000 ALTER TABLE Ticket DISABLE KEYS */;
<<<<<<< HEAD
INSERT INTO Ticket (event_id, customer_id, purchase_date, price, status) VALUES
(4, 5,'2025-03-28', 4700.00, 'Cancelled'),
(3, 2,'2026-02-28', 15699.00, 'Active'),
(1, 9, '2026-12-03', 5700.00, 'Active'),
(5, 10, '2025-12-03', 7400.00, 'Cancelled'),
(5, 8, '2026-09-22', 15100.00, 'Active'),
(5, 8, '2026-09-22', 15100.00, 'Active');
=======
INSERT INTO Ticket (event_id, customer_id, purchase_date, status) VALUES
(4, 5, '2025-03-28', 'Cancelled'),
(3, 2, '2026-02-28', 'Active'),
(1, 9, '2026-12-03', 'Active'),
(5, 10, '2025-12-03', 'Cancelled'),
(5, 8, '2026-09-22', 'Active');
>>>>>>> 190a384de40e4bea25c9f467ed42389b657bf158
/*!40000 ALTER TABLE Ticket ENABLE KEYS */;
UNLOCK TABLES;

-- ====================================
-- Dumping data for table Cancellation
-- ====================================
/*!40000 ALTER TABLE Cancellation DISABLE KEYS */;
INSERT INTO Cancellation (ticket_id, refund_amount, cancellation_date) VALUES
(1, 4700.00, '2025-04-10'),
(4, 7400.00, '2025-12-25');
/*!40000 ALTER TABLE Cancellation ENABLE KEYS */;
UNLOCK TABLES;

-- ========================================
-- Dumping data for table CommissionPayout
-- ========================================
/*!40000 ALTER TABLE CommissionPayout DISABLE KEYS */;
INSERT INTO CommissionPayout (artist_id, organizer_id, event_id, commission_percentage, payout_date) VALUES
(1, 1, 1, 13.00, '2025-04-01'),
(3, 1, 1, 15.00, '2025-11-22'),
(3, 2, 1, 12.00, '2025-08-12');
UNLOCK TABLES;