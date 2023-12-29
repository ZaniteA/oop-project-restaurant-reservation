-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Host: localhost
-- Generation Time: Dec 29, 2023 at 07:07 AM
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

-- --------------------------------------------------------

--
-- Table structure for table `MsLocalRestaurant`
--

CREATE TABLE `MsLocalRestaurant` (
  `LocalID` int(11) NOT NULL,
  `Name` varchar(50) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Table structure for table `MsMainRestaurant`
--

CREATE TABLE `MsMainRestaurant` (
  `MainID` int(11) NOT NULL,
  `Name` varchar(50) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Table structure for table `MsMenu`
--

CREATE TABLE `MsMenu` (
  `MenuID` int(11) NOT NULL,
  `RegularID` int(11) NOT NULL,
  `SpecialID` int(11) NOT NULL,
  `LocalID` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

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

-- --------------------------------------------------------

--
-- Table structure for table `MsRestaurant`
--

CREATE TABLE `MsRestaurant` (
  `RestaurantID` int(11) NOT NULL,
  `MainID` int(11) NOT NULL,
  `LocalID` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

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

-- --------------------------------------------------------

--
-- Table structure for table `MsTable`
--

CREATE TABLE `MsTable` (
  `TableID` int(11) NOT NULL,
  `Capacity` int(11) NOT NULL,
  `Taken` tinyint(1) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

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
-- Table structure for table `RestaurantMenuMap`
--

CREATE TABLE `RestaurantMenuMap` (
  `RestaurantID` int(11) NOT NULL,
  `MenuID` int(11) NOT NULL
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
-- Indexes for table `RestaurantMenuMap`
--
ALTER TABLE `RestaurantMenuMap`
  ADD PRIMARY KEY (`RestaurantID`,`MenuID`),
  ADD KEY `ConnectorRestaurantMenuMap-MsMenu` (`MenuID`);

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
  MODIFY `EmployeeID` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `MsLocalMenu`
--
ALTER TABLE `MsLocalMenu`
  MODIFY `LocalID` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `MsLocalRestaurant`
--
ALTER TABLE `MsLocalRestaurant`
  MODIFY `LocalID` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `MsMainRestaurant`
--
ALTER TABLE `MsMainRestaurant`
  MODIFY `MainID` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `MsMenu`
--
ALTER TABLE `MsMenu`
  MODIFY `MenuID` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `MsOrder`
--
ALTER TABLE `MsOrder`
  MODIFY `OrderID` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `MsRegularMenu`
--
ALTER TABLE `MsRegularMenu`
  MODIFY `RegularID` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `MsRestaurant`
--
ALTER TABLE `MsRestaurant`
  MODIFY `RestaurantID` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `MsSpecialMenu`
--
ALTER TABLE `MsSpecialMenu`
  MODIFY `SpecialID` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `MsTable`
--
ALTER TABLE `MsTable`
  MODIFY `TableID` int(11) NOT NULL AUTO_INCREMENT;

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
-- Constraints for table `RestaurantMenuMap`
--
ALTER TABLE `RestaurantMenuMap`
  ADD CONSTRAINT `ConnectorRestaurantMenuMap-MsMenu` FOREIGN KEY (`MenuID`) REFERENCES `MsMenu` (`MenuID`),
  ADD CONSTRAINT `ConnectorRestaurantMenuMap-MsRestaurant` FOREIGN KEY (`RestaurantID`) REFERENCES `MsRestaurant` (`RestaurantID`);

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
