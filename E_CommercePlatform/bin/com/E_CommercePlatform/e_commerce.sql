-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1
-- Generation Time: Aug 27, 2024 at 06:49 AM
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
-- Database: `e_commerce`
--

-- --------------------------------------------------------

--
-- Table structure for table `admininfo`
--

CREATE TABLE `admininfo` (
  `adminId` int(10) NOT NULL,
  `password` varchar(20) NOT NULL,
  `Name` varchar(20) NOT NULL,
  `Age` int(10) NOT NULL,
  `Email` varchar(20) NOT NULL,
  `Address` varchar(30) NOT NULL,
  `ContactNumber` varchar(12) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `admininfo`
--

INSERT INTO `admininfo` (`adminId`, `password`, `Name`, `Age`, `Email`, `Address`, `ContactNumber`) VALUES
(1, '', '', 0, '', '', ''),
(100, '', 'twh', 56, 'tbwwbt', 'tt tbbt g5', '785478');

-- --------------------------------------------------------

--
-- Stand-in structure for view `admin_credentials`
-- (See below for the actual view)
--
CREATE TABLE `admin_credentials` (
`adminId` int(10)
,`password` varchar(20)
,`Email` varchar(20)
);

-- --------------------------------------------------------

--
-- Table structure for table `custinfo`
--

CREATE TABLE `custinfo` (
  `custId` int(10) NOT NULL,
  `password` varchar(20) NOT NULL,
  `Name` varchar(20) NOT NULL,
  `Age` int(10) NOT NULL,
  `email` varchar(20) NOT NULL,
  `Address` varchar(20) NOT NULL,
  `ContactNumber` varchar(12) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `custinfo`
--

INSERT INTO `custinfo` (`custId`, `password`, `Name`, `Age`, `email`, `Address`, `ContactNumber`) VALUES
(1, 'pass123', 'John Doe', 30, 'john.doe@example.com', '123 Main St, Anytown', '555-1234'),
(2, 'secure456', 'Jane Smith', 25, 'jane.smith@example.c', '456 Elm St, Othertow', '555-5678'),
(3, 'abc789', 'Robert Johnson', 40, 'robert.johnson@examp', '789 Maple St, Anycit', '555-9101'),
(4, 'QrMBB?==B', 'Dev', 55, 'dev@dk.in', 'jy r', '7874393540');

-- --------------------------------------------------------

--
-- Stand-in structure for view `customer_credentials`
-- (See below for the actual view)
--
CREATE TABLE `customer_credentials` (
`custId` int(10)
,`password` varchar(20)
,`email` varchar(20)
);

-- --------------------------------------------------------

--
-- Stand-in structure for view `customer_details`
-- (See below for the actual view)
--
CREATE TABLE `customer_details` (
`custId` int(10)
,`Name` varchar(20)
,`Age` int(10)
,`email` varchar(20)
,`Address` varchar(20)
,`ContactNumber` varchar(12)
);

-- --------------------------------------------------------

--
-- Table structure for table `products`
--

CREATE TABLE `products` (
  `pid` int(10) NOT NULL,
  `name` varchar(20) NOT NULL,
  `type` varchar(20) NOT NULL,
  `qty` int(10) NOT NULL,
  `price` float NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Stand-in structure for view `view_all_products`
-- (See below for the actual view)
--
CREATE TABLE `view_all_products` (
`pid` int(10)
,`name` varchar(20)
,`type` varchar(20)
,`qty` int(10)
,`price` float
);

-- --------------------------------------------------------

--
-- Structure for view `admin_credentials`
--
DROP TABLE IF EXISTS `admin_credentials`;

CREATE ALGORITHM=UNDEFINED DEFINER=`root`@`localhost` SQL SECURITY DEFINER VIEW `admin_credentials`  AS SELECT `admininfo`.`adminId` AS `adminId`, `admininfo`.`password` AS `password`, `admininfo`.`Email` AS `Email` FROM `admininfo` ;

-- --------------------------------------------------------

--
-- Structure for view `customer_credentials`
--
DROP TABLE IF EXISTS `customer_credentials`;

CREATE ALGORITHM=UNDEFINED DEFINER=`root`@`localhost` SQL SECURITY DEFINER VIEW `customer_credentials`  AS SELECT `custinfo`.`custId` AS `custId`, `custinfo`.`password` AS `password`, `custinfo`.`email` AS `email` FROM `custinfo` ;

-- --------------------------------------------------------

--
-- Structure for view `customer_details`
--
DROP TABLE IF EXISTS `customer_details`;

CREATE ALGORITHM=UNDEFINED DEFINER=`root`@`localhost` SQL SECURITY DEFINER VIEW `customer_details`  AS SELECT `custinfo`.`custId` AS `custId`, `custinfo`.`Name` AS `Name`, `custinfo`.`Age` AS `Age`, `custinfo`.`email` AS `email`, `custinfo`.`Address` AS `Address`, `custinfo`.`ContactNumber` AS `ContactNumber` FROM `custinfo` ;

-- --------------------------------------------------------

--
-- Structure for view `view_all_products`
--
DROP TABLE IF EXISTS `view_all_products`;

CREATE ALGORITHM=UNDEFINED DEFINER=`root`@`localhost` SQL SECURITY DEFINER VIEW `view_all_products`  AS SELECT `products`.`pid` AS `pid`, `products`.`name` AS `name`, `products`.`type` AS `type`, `products`.`qty` AS `qty`, `products`.`price` AS `price` FROM `products` ;

--
-- Indexes for dumped tables
--

--
-- Indexes for table `admininfo`
--
ALTER TABLE `admininfo`
  ADD PRIMARY KEY (`adminId`);

--
-- Indexes for table `custinfo`
--
ALTER TABLE `custinfo`
  ADD PRIMARY KEY (`custId`);

--
-- Indexes for table `products`
--
ALTER TABLE `products`
  ADD PRIMARY KEY (`pid`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `admininfo`
--
ALTER TABLE `admininfo`
  MODIFY `adminId` int(10) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=101;

--
-- AUTO_INCREMENT for table `custinfo`
--
ALTER TABLE `custinfo`
  MODIFY `custId` int(10) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=5;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
