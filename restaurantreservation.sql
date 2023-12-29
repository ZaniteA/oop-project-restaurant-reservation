-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Host: localhost
-- Generation Time: Dec 29, 2023 at 09:53 AM
-- Server version: 10.4.28-MariaDB
-- PHP Version: 8.2.4

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `restaurantreservation`
--

-- --------------------------------------------------------

--
-- Table structure for table `MsEmployee`
--

CREATE TABLE `MsEmployee` (
  `EmployeeID` int(11) NOT NULL,
  `Name` varchar(50) NOT NULL,
  `RestaurantID` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `MsEmployee`
--

INSERT INTO `MsEmployee` (`EmployeeID`, `Name`, `RestaurantID`) VALUES
(91, 'Alice', 1),
(92, 'Bob', 2),
(93, 'Charlie', 3),
(94, 'David', 4),
(95, 'Emma', 5),
(96, 'Fatih', 6),
(97, 'Gunawan', 1),
(98, 'Herlina', 2),
(99, 'Irfan', 3),
(100, 'Joko', 4),
(101, 'Kiki', 5),
(102, 'Lina', 6),
(103, 'Maya', 1),
(104, 'Nino', 2),
(105, 'Oki', 3),
(106, 'Putri', 4),
(107, 'Qadri', 5),
(108, 'Rini', 6),
(109, 'Supri', 1),
(110, 'Tayo', 2),
(111, 'Udin', 3),
(112, 'Vina', 4),
(113, 'William', 5),
(114, 'Xena', 6),
(115, 'Yani', 1),
(116, 'Zara', 2),
(117, 'Amin', 3),
(118, 'Bella', 4),
(119, 'Cakra', 5),
(120, 'Doni', 6);

-- --------------------------------------------------------

--
-- Table structure for table `MsLocalMenu`
--

CREATE TABLE `MsLocalMenu` (
  `LocalID` int(11) NOT NULL,
  `Name` varchar(50) NOT NULL,
  `Price` double NOT NULL,
  `Location` varchar(50) NOT NULL,
  `Lore` varchar(200) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `MsLocalMenu`
--

INSERT INTO `MsLocalMenu` (`LocalID`, `Name`, `Price`, `Location`, `Lore`) VALUES
(1, 'Rendang Juara', 65000, 'Padang Pusat', 'Rendang ini adalah mahakarya masakan Padang, dengan daging yang empuk dan bumbu rendang yang meresap hingga ke tulang dari Padang Pusat.'),
(2, 'Petai Balado Berani', 50000, 'Padang Barat', 'Petai Balado ini adalah paduan sempurna rasa pedas dan petai yang khas, menciptakan ledakan rasa di lidah dari Padang Barat.'),
(3, 'Ikan Bakar Kelapa Pariaman', 60000, 'Padang Timur', 'Ikan bakar ini diberi sentuhan khusus dengan kelapa dari Pariaman, memberikan aroma khas dan rasa yang lezat dari Padang Timur.'),
(4, 'Es Teh Tarik Ranah Minang', 15000, 'Padang Selatan', 'Es Teh Tarik ini menggambarkan keterampilan menarik teh khas Minangkabau. Menyegarkan dan mendalam, sejuknya ranah Minang tersaji dalam satu gelas dari Padang Selatan.'),
(5, 'Soto Banjar Legendaris', 40000, 'Samarinda Utara', 'Soto Banjar yang melegenda ini menawarkan cita rasa khas dan kehangatan, menciptakan pengalaman kuliner dari Borneo Timur di Samarinda Utara.'),
(6, 'Mangut Lele Khas Sungai Mahakam', 35000, 'Samarinda Barat', 'Mangut Lele ini diambil langsung dari Sungai Mahakam, dengan bumbu khas yang menggoda selera dari Samarinda Barat.'),
(7, 'Bubur Pedas Mantap Kalimantan', 25000, 'Samarinda Timur', 'Bubur Pedas ini menggabungkan cita rasa pedas khas Kalimantan dengan kelembutan bubur, menciptakan kehangatan di setiap sendokannya dari Samarinda Timur.'),
(8, 'Cendol Sirup Nusantara', 18000, 'Samarinda Selatan', 'Cendol Sirup yang segar ini menghadirkan rasa manis khas Nusantara, membawa kesegaran dari sungai dan hutan Kalimantan di Samarinda Selatan.'),
(9, 'Rawon Iga Jagoan', 55000, 'Surabaya Utara', 'Resep rahasia keluarga Jagoan, hidangan rawon ini menggabungkan kelezatan iga dengan bumbu khas Surabaya Utara yang mendalam.'),
(10, 'Lontong Kikil Kenyal', 45000, 'Surabaya Barat', 'Lontong Kikil yang kenyal ini menciptakan pengalaman makan yang autentik, diambil dari tradisi kuliner khas Surabaya Barat.'),
(11, 'Sate Klopo Mantap', 30000, 'Surabaya Timur', 'Sate Klopo kaya akan cita rasa kelapa yang gurih. Setiap tusukan sate ini adalah petualangan rasa dari kota pahlawan di Surabaya Timur.'),
(12, 'Es Teler Segarnya Surabaya', 20000, 'Surabaya Selatan', 'Es Teler yang menyegarkan ini adalah campuran buah tropis, kelapa muda, dan sirup khas Surabaya Selatan yang membangkitkan semangat kota bersejarah.');

-- --------------------------------------------------------

--
-- Table structure for table `MsLocalRestaurant`
--

CREATE TABLE `MsLocalRestaurant` (
  `LocalID` int(11) NOT NULL,
  `Name` varchar(50) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `MsLocalRestaurant`
--

INSERT INTO `MsLocalRestaurant` (`LocalID`, `Name`) VALUES
(1, 'Padang'),
(2, 'Samarinda'),
(3, 'Surabaya');

-- --------------------------------------------------------

--
-- Table structure for table `MsMainRestaurant`
--

CREATE TABLE `MsMainRestaurant` (
  `MainID` int(11) NOT NULL,
  `Name` varchar(50) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `MsMainRestaurant`
--

INSERT INTO `MsMainRestaurant` (`MainID`, `Name`) VALUES
(1, 'Bali'),
(2, 'Bandung'),
(3, 'Jakarta');

-- --------------------------------------------------------

--
-- Table structure for table `MsMenu`
--

CREATE TABLE `MsMenu` (
  `MenuID` int(11) NOT NULL,
  `RegularID` int(11) DEFAULT NULL,
  `SpecialID` int(11) DEFAULT NULL,
  `LocalID` int(11) DEFAULT NULL,
  `RestaurantID` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `MsMenu`
--

INSERT INTO `MsMenu` (`MenuID`, `RegularID`, `SpecialID`, `LocalID`, `RestaurantID`) VALUES
(1, 1, NULL, NULL, 1),
(2, 2, NULL, NULL, 2),
(3, 3, NULL, NULL, 3),
(4, 4, NULL, NULL, 4),
(5, 5, NULL, NULL, 5),
(6, 6, NULL, NULL, 6),
(7, 7, NULL, NULL, 1),
(8, 8, NULL, NULL, 2),
(9, 9, NULL, NULL, 3),
(10, 10, NULL, NULL, 4),
(11, 11, NULL, NULL, 5),
(12, 12, NULL, NULL, 6),
(13, NULL, 1, NULL, 1),
(14, NULL, 2, NULL, 1),
(15, NULL, 3, NULL, 1),
(16, NULL, 4, NULL, 1),
(17, NULL, 5, NULL, 2),
(18, NULL, 6, NULL, 2),
(19, NULL, 7, NULL, 2),
(20, NULL, 8, NULL, 2),
(21, NULL, 9, NULL, 3),
(22, NULL, 10, NULL, 3),
(23, NULL, 11, NULL, 3),
(24, NULL, 12, NULL, 3),
(25, NULL, NULL, 1, 4),
(26, NULL, NULL, 2, 4),
(27, NULL, NULL, 3, 4),
(28, NULL, NULL, 4, 4),
(29, NULL, NULL, 5, 5),
(30, NULL, NULL, 6, 5),
(31, NULL, NULL, 7, 5),
(32, NULL, NULL, 8, 5),
(33, NULL, NULL, 9, 6),
(34, NULL, NULL, 10, 6),
(35, NULL, NULL, 11, 6),
(36, NULL, NULL, 12, 6);

-- --------------------------------------------------------

--
-- Table structure for table `MsOrder`
--

CREATE TABLE `MsOrder` (
  `OrderID` int(11) NOT NULL,
  `CustomerName` varchar(50) NOT NULL,
  `Persons` int(11) NOT NULL,
  `Status` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Table structure for table `MsRegularMenu`
--

CREATE TABLE `MsRegularMenu` (
  `RegularID` int(11) NOT NULL,
  `Name` varchar(50) NOT NULL,
  `Price` double NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `MsRegularMenu`
--

INSERT INTO `MsRegularMenu` (`RegularID`, `Name`, `Price`) VALUES
(1, 'Nasi Goreng Jumbo', 35000),
(2, 'Ayam Bakar Taliwang', 45000),
(3, 'Mie Goreng Seafood', 40000),
(4, 'Sop Buntut', 50000),
(5, 'Ikan Bakar Rica-Rica', 55000),
(6, 'Capcay Goreng', 35000),
(7, 'Teh Manis Hangat', 10000),
(8, 'Teh Tawar', 8000),
(9, 'Air Putih', 5000),
(10, 'Es Teh Manis', 10000),
(11, 'Jus Alpukat', 15000),
(12, 'Es Teler', 20000);

-- --------------------------------------------------------

--
-- Table structure for table `MsRestaurant`
--

CREATE TABLE `MsRestaurant` (
  `RestaurantID` int(11) NOT NULL,
  `MainID` int(11) DEFAULT NULL,
  `LocalID` int(11) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `MsRestaurant`
--

INSERT INTO `MsRestaurant` (`RestaurantID`, `MainID`, `LocalID`) VALUES
(1, 1, NULL),
(2, 2, NULL),
(3, 3, NULL),
(4, NULL, 1),
(5, NULL, 2),
(6, NULL, 3);

-- --------------------------------------------------------

--
-- Table structure for table `MsSpecialMenu`
--

CREATE TABLE `MsSpecialMenu` (
  `SpecialID` int(11) NOT NULL,
  `Name` varchar(50) NOT NULL,
  `Price` double NOT NULL,
  `Lore` varchar(200) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `MsSpecialMenu`
--

INSERT INTO `MsSpecialMenu` (`SpecialID`, `Name`, `Price`, `Lore`) VALUES
(1, 'Raja Lautan Abadi', 75000, 'Hidangan istimewa terinspirasi perjalanan laut pelaut legendaris, cita rasa laut menciptakan perjalanan tak terlupakan melintasi samudra.'),
(2, 'Sihir Taliwang', 65000, 'Dibalut bumbu rahasia nenek moyang, Ayam Taliwang membawa kesan magis dari pulau eksotis. Keajaiban cita rasa dan kenangan abadi dari Tanah Sumbawa.'),
(3, 'Mie Legenda Ratu', 55000, 'Hidangan kesukaan ratu zaman dulu, keanggunan rasa dan kehalusan tekstur menciptakan perjalanan ke dunia kuliner yang diwarnai oleh elegansi dan kelembutan.'),
(4, 'Es Teh Sembilan Dewa', 15000, 'Minuman es teh diciptakan oleh sembilan dewa rasa. Dingin dan menyegarkan, setiap tegukan adalah berkah dari alam yang magis.'),
(5, 'Ikan Mistis Rica-Rica', 70000, 'Keajaiban rempah-rempah dan rasa pedas segar dari ikan laut, menciptakan semangat petualangan dan keunikan bumbu dunia terpencil.'),
(6, 'Capcay Megah', 50000, 'Perwujudan keberagaman dan persatuan cita rasa, setiap sayuran mewakili keunikan, menciptakan harmoni rasa yang luar biasa.'),
(7, 'Nasi Kerajaan Tersembunyi', 90000, 'Nasi dari resep istana kerajaan tersembunyi, setiap suapan mengungkapkan keanggunan dan kemegahan rahasia sejarah kuliner.'),
(8, 'Jus Alpukat Kesatria', 20000, 'Jus alpukat melambangkan kekuatan dan keberanian seorang kesatria. Rasakan energi segar dan kenikmatan setiap kali Anda menikmati minuman ini.'),
(9, 'Sate Pahlawan', 30000, 'Dibuat untuk menghormati pahlawan sejati, Sate Pahlawan memberikan penghormatan kepada keberanian dan semangat kepahlawanan. Setiap tusukan adalah pesta rasa yang menggetarkan hati.'),
(10, 'Mie Legendaris Pencarian Cinta', 60000, 'Terinspirasi dari kisah legendaris pencarian cinta abadi, setiap gigitan membawa Anda dalam perjalanan romantis melintasi ladang bumbu dan kelezatan.'),
(11, 'Rahasia Gado-Gado Dewa', 40000, 'Anugerah dewa-dewa rasa, saus kacang ajaib dan paduan sayuran segar, kelezatan tak terlupakan.'),
(12, 'Es Teh Sembilan Dewa', 15000, 'Minuman es teh diciptakan oleh sembilan dewa rasa. Dingin dan menyegarkan, setiap tegukan adalah berkah dari alam yang magis.');

-- --------------------------------------------------------

--
-- Table structure for table `MsTable`
--

CREATE TABLE `MsTable` (
  `TableID` int(11) NOT NULL,
  `Capacity` int(11) NOT NULL,
  `Taken` tinyint(1) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `MsTable`
--

INSERT INTO `MsTable` (`TableID`, `Capacity`, `Taken`) VALUES
(1, 2, 0),
(2, 4, 0),
(3, 10, 0),
(4, 2, 0),
(5, 4, 0),
(6, 10, 0),
(7, 2, 0),
(8, 4, 0),
(9, 10, 0),
(10, 2, 0),
(11, 4, 0),
(12, 10, 0),
(13, 2, 0),
(14, 4, 0),
(15, 10, 0),
(16, 2, 0),
(17, 4, 0),
(18, 10, 0),
(19, 2, 0),
(20, 4, 0),
(21, 10, 0),
(22, 2, 0),
(23, 4, 0),
(24, 10, 0),
(25, 2, 0),
(26, 4, 0),
(27, 10, 0),
(28, 2, 0),
(29, 4, 0),
(30, 10, 0),
(31, 2, 0),
(32, 4, 0),
(33, 10, 0),
(34, 2, 0),
(35, 4, 0),
(36, 10, 0);

-- --------------------------------------------------------

--
-- Table structure for table `OrderMenuMap`
--

CREATE TABLE `OrderMenuMap` (
  `MenuID` int(11) NOT NULL,
  `OrderID` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Table structure for table `OrderTableMap`
--

CREATE TABLE `OrderTableMap` (
  `OrderID` int(11) NOT NULL,
  `TableID` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Table structure for table `RestaurantOrderMap`
--

CREATE TABLE `RestaurantOrderMap` (
  `RestaurantID` int(11) NOT NULL,
  `OrderID` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Table structure for table `RestaurantTableMap`
--

CREATE TABLE `RestaurantTableMap` (
  `RestaurantID` int(11) NOT NULL,
  `TableID` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `RestaurantTableMap`
--

INSERT INTO `RestaurantTableMap` (`RestaurantID`, `TableID`) VALUES
(1, 1),
(1, 2),
(1, 3),
(1, 4),
(1, 5),
(1, 6),
(2, 7),
(2, 8),
(2, 9),
(2, 10),
(2, 11),
(2, 12),
(3, 13),
(3, 14),
(3, 15),
(3, 16),
(3, 17),
(3, 18),
(4, 19),
(4, 20),
(4, 21),
(4, 22),
(4, 23),
(4, 24),
(5, 25),
(5, 26),
(5, 27),
(5, 28),
(5, 29),
(5, 30),
(6, 31),
(6, 32),
(6, 33),
(6, 34),
(6, 35),
(6, 36);

--
-- Indexes for dumped tables
--

--
-- Indexes for table `MsEmployee`
--
ALTER TABLE `MsEmployee`
  ADD PRIMARY KEY (`EmployeeID`),
  ADD KEY `ConnectorMsEmployee-MsRestaurant` (`RestaurantID`);

--
-- Indexes for table `MsLocalMenu`
--
ALTER TABLE `MsLocalMenu`
  ADD PRIMARY KEY (`LocalID`);

--
-- Indexes for table `MsLocalRestaurant`
--
ALTER TABLE `MsLocalRestaurant`
  ADD PRIMARY KEY (`LocalID`);

--
-- Indexes for table `MsMainRestaurant`
--
ALTER TABLE `MsMainRestaurant`
  ADD PRIMARY KEY (`MainID`);

--
-- Indexes for table `MsMenu`
--
ALTER TABLE `MsMenu`
  ADD PRIMARY KEY (`MenuID`),
  ADD UNIQUE KEY `RegularID` (`RegularID`),
  ADD UNIQUE KEY `SpecialID` (`SpecialID`),
  ADD UNIQUE KEY `LocalID` (`LocalID`);

--
-- Indexes for table `MsOrder`
--
ALTER TABLE `MsOrder`
  ADD PRIMARY KEY (`OrderID`);

--
-- Indexes for table `MsRegularMenu`
--
ALTER TABLE `MsRegularMenu`
  ADD PRIMARY KEY (`RegularID`);

--
-- Indexes for table `MsRestaurant`
--
ALTER TABLE `MsRestaurant`
  ADD PRIMARY KEY (`RestaurantID`),
  ADD UNIQUE KEY `MainID` (`MainID`),
  ADD UNIQUE KEY `LocalID` (`LocalID`);

--
-- Indexes for table `MsSpecialMenu`
--
ALTER TABLE `MsSpecialMenu`
  ADD PRIMARY KEY (`SpecialID`);

--
-- Indexes for table `MsTable`
--
ALTER TABLE `MsTable`
  ADD PRIMARY KEY (`TableID`);

--
-- Indexes for table `OrderMenuMap`
--
ALTER TABLE `OrderMenuMap`
  ADD PRIMARY KEY (`MenuID`,`OrderID`),
  ADD KEY `ConnectorOrderMenuMap-MsOrder` (`OrderID`);

--
-- Indexes for table `OrderTableMap`
--
ALTER TABLE `OrderTableMap`
  ADD PRIMARY KEY (`OrderID`,`TableID`),
  ADD KEY `ConnectorOrderTableMap-MsTable` (`TableID`);

--
-- Indexes for table `RestaurantOrderMap`
--
ALTER TABLE `RestaurantOrderMap`
  ADD PRIMARY KEY (`RestaurantID`,`OrderID`),
  ADD KEY `ConnectorRestaurantOrderMap-MsOrder` (`OrderID`);

--
-- Indexes for table `RestaurantTableMap`
--
ALTER TABLE `RestaurantTableMap`
  ADD PRIMARY KEY (`RestaurantID`,`TableID`),
  ADD KEY `ConnectorRestaurantTableMap-MsTable` (`TableID`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `MsEmployee`
--
ALTER TABLE `MsEmployee`
  MODIFY `EmployeeID` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=121;

--
-- AUTO_INCREMENT for table `MsLocalMenu`
--
ALTER TABLE `MsLocalMenu`
  MODIFY `LocalID` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=13;

--
-- AUTO_INCREMENT for table `MsLocalRestaurant`
--
ALTER TABLE `MsLocalRestaurant`
  MODIFY `LocalID` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=4;

--
-- AUTO_INCREMENT for table `MsMainRestaurant`
--
ALTER TABLE `MsMainRestaurant`
  MODIFY `MainID` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=4;

--
-- AUTO_INCREMENT for table `MsMenu`
--
ALTER TABLE `MsMenu`
  MODIFY `MenuID` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=37;

--
-- AUTO_INCREMENT for table `MsOrder`
--
ALTER TABLE `MsOrder`
  MODIFY `OrderID` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `MsRegularMenu`
--
ALTER TABLE `MsRegularMenu`
  MODIFY `RegularID` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=13;

--
-- AUTO_INCREMENT for table `MsRestaurant`
--
ALTER TABLE `MsRestaurant`
  MODIFY `RestaurantID` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=7;

--
-- AUTO_INCREMENT for table `MsSpecialMenu`
--
ALTER TABLE `MsSpecialMenu`
  MODIFY `SpecialID` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=13;

--
-- AUTO_INCREMENT for table `MsTable`
--
ALTER TABLE `MsTable`
  MODIFY `TableID` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=73;

--
-- Constraints for dumped tables
--

--
-- Constraints for table `MsEmployee`
--
ALTER TABLE `MsEmployee`
  ADD CONSTRAINT `ConnectorMsEmployee-MsRestaurant` FOREIGN KEY (`RestaurantID`) REFERENCES `MsRestaurant` (`RestaurantID`);

--
-- Constraints for table `MsMenu`
--
ALTER TABLE `MsMenu`
  ADD CONSTRAINT `ConnectorMsMenu-MsLocalMenu` FOREIGN KEY (`LocalID`) REFERENCES `MsLocalMenu` (`LocalID`),
  ADD CONSTRAINT `ConnectorMsMenu-MsRegularMenu` FOREIGN KEY (`RegularID`) REFERENCES `MsRegularMenu` (`RegularID`),
  ADD CONSTRAINT `ConnectorMsMenu-MsSpecialMenu` FOREIGN KEY (`SpecialID`) REFERENCES `MsSpecialMenu` (`SpecialID`);

--
-- Constraints for table `MsRestaurant`
--
ALTER TABLE `MsRestaurant`
  ADD CONSTRAINT `ConnectorMsRestaurant-MsLocalRestaurant` FOREIGN KEY (`LocalID`) REFERENCES `MsLocalRestaurant` (`LocalID`),
  ADD CONSTRAINT `ConnectorMsRestaurant-MsMainRestaurant` FOREIGN KEY (`MainID`) REFERENCES `MsMainRestaurant` (`MainID`);

--
-- Constraints for table `OrderMenuMap`
--
ALTER TABLE `OrderMenuMap`
  ADD CONSTRAINT `ConnectorOrderMenuMap-MsMenu` FOREIGN KEY (`MenuID`) REFERENCES `MsMenu` (`MenuID`),
  ADD CONSTRAINT `ConnectorOrderMenuMap-MsOrder` FOREIGN KEY (`OrderID`) REFERENCES `MsOrder` (`OrderID`);

--
-- Constraints for table `OrderTableMap`
--
ALTER TABLE `OrderTableMap`
  ADD CONSTRAINT `ConnectorOrderTableMap-MsOrder` FOREIGN KEY (`OrderID`) REFERENCES `MsOrder` (`OrderID`),
  ADD CONSTRAINT `ConnectorOrderTableMap-MsTable` FOREIGN KEY (`TableID`) REFERENCES `MsTable` (`TableID`);

--
-- Constraints for table `RestaurantOrderMap`
--
ALTER TABLE `RestaurantOrderMap`
  ADD CONSTRAINT `ConnectorRestaurantOrderMap-MsOrder` FOREIGN KEY (`OrderID`) REFERENCES `MsOrder` (`OrderID`),
  ADD CONSTRAINT `ConnectorRestaurantOrderMap-MsRestaurant` FOREIGN KEY (`RestaurantID`) REFERENCES `MsRestaurant` (`RestaurantID`);

--
-- Constraints for table `RestaurantTableMap`
--
ALTER TABLE `RestaurantTableMap`
  ADD CONSTRAINT `ConnectorRestaurantTableMap-MsRestaurant` FOREIGN KEY (`RestaurantID`) REFERENCES `MsRestaurant` (`RestaurantID`),
  ADD CONSTRAINT `ConnectorRestaurantTableMap-MsTable` FOREIGN KEY (`TableID`) REFERENCES `MsTable` (`TableID`);
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
