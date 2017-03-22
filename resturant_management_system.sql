-- phpMyAdmin SQL Dump
-- version 4.5.1
-- http://www.phpmyadmin.net
--
-- Host: 127.0.0.1
-- Generation Time: Oct 21, 2016 at 12:47 PM
-- Server version: 10.1.10-MariaDB
-- PHP Version: 7.0.3

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `resturant_management_system`
--

-- --------------------------------------------------------

--
-- Table structure for table `tbl_assets`
--

CREATE TABLE `tbl_assets` (
  `id` varchar(255) NOT NULL,
  `name` varchar(255) NOT NULL,
  `type` varchar(255) NOT NULL,
  `remaining_quantity` int(10) NOT NULL,
  `amount` double(10,2) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `tbl_assets`
--

INSERT INTO `tbl_assets` (`id`, `name`, `type`, `remaining_quantity`, `amount`) VALUES
('fur2', 'furniture', 'Current', 15, 1575.00),
('fur3', 'fridge', 'Current', 1, 40000.00),
('fur5', 'AC', 'Fixed', 2, 24000.00),
('Fur8', 'Furniture', 'Current', 2, 24000.00),
('G1', 'glass', 'Current', 35, 7825.00);

-- --------------------------------------------------------

--
-- Table structure for table `tbl_cashinhand`
--

CREATE TABLE `tbl_cashinhand` (
  `cashinhand` double(10,2) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Table structure for table `tbl_cashinhand_details`
--

CREATE TABLE `tbl_cashinhand_details` (
  `sn` bigint(20) NOT NULL,
  `date` date NOT NULL,
  `particular` varchar(255) NOT NULL,
  `debit_amount` double(10,2) DEFAULT NULL,
  `credit_amount` double(10,2) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `tbl_cashinhand_details`
--

INSERT INTO `tbl_cashinhand_details` (`sn`, `date`, `particular`, `debit_amount`, `credit_amount`) VALUES
(1, '2016-09-01', 'sales-bill no:1234', NULL, 500.00),
(2, '2016-09-01', 'sdcascfas', NULL, 600.00);

--
-- Triggers `tbl_cashinhand_details`
--
DELIMITER $$
CREATE TRIGGER `update on insert tbl_cashinhand` AFTER INSERT ON `tbl_cashinhand_details` FOR EACH ROW BEGIN
IF NEW.credit_amount is NOT NULL THEN
 UPDATE tbl_cashinhand
SET tbl_cashinhand.cashinhand = tbl_cashinhand.cashinhand +
new.credit_amount ;
ELSEIF NEW.debit_amount is NOT NULL THEN
 UPDATE tbl_cashinhand
SET tbl_cashinhand.cashinhand = tbl_cashinhand.cashinhand -
new.debit_amount;
END IF;
END
$$
DELIMITER ;

-- --------------------------------------------------------

--
-- Table structure for table `tbl_category`
--

CREATE TABLE `tbl_category` (
  `type` varchar(255) NOT NULL,
  `cat_id` varchar(255) NOT NULL,
  `cat_name` varchar(255) NOT NULL,
  `subcat_num` int(10) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `tbl_category`
--

INSERT INTO `tbl_category` (`type`, `cat_id`, `cat_name`, `subcat_num`) VALUES
('sales', '001', 'drinks', 4),
('sales', '002', 'food', 10);

-- --------------------------------------------------------

--
-- Table structure for table `tbl_credit_purchase`
--

CREATE TABLE `tbl_credit_purchase` (
  `did` int(10) NOT NULL,
  `dname` varchar(255) NOT NULL,
  `total_rem_amount` double(10,2) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Table structure for table `tbl_credit_sales`
--

CREATE TABLE `tbl_credit_sales` (
  `cid` varchar(255) NOT NULL,
  `cname` varchar(255) NOT NULL,
  `total_rem_amount` double(10,2) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `tbl_credit_sales`
--

INSERT INTO `tbl_credit_sales` (`cid`, `cname`, `total_rem_amount`) VALUES
('001', 'Ram', 1040.00),
('002', 'Shyam', 400.00),
('003', 'Hari', 120.00),
('004', 'Nibesh', 100.00);

-- --------------------------------------------------------

--
-- Table structure for table `tbl_credit_sales_subdetails`
--

CREATE TABLE `tbl_credit_sales_subdetails` (
  `sn` bigint(20) NOT NULL,
  `cid` varchar(255) NOT NULL,
  `bill_id` bigint(20) DEFAULT NULL,
  `date` date NOT NULL,
  `total_amount` double(10,2) NOT NULL,
  `received_amount` double(10,2) NOT NULL,
  `remaining_amount` double(10,2) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `tbl_credit_sales_subdetails`
--

INSERT INTO `tbl_credit_sales_subdetails` (`sn`, `cid`, `bill_id`, `date`, `total_amount`, `received_amount`, `remaining_amount`) VALUES
(25, '001', NULL, '2016-08-12', 800.00, 400.00, 400.00),
(26, '001', NULL, '2016-08-12', 400.00, 200.00, 200.00),
(28, '001', NULL, '2016-08-12', 200.00, 100.00, 100.00),
(34, '002', NULL, '2016-08-12', 450.00, 150.00, 300.00),
(55, '002', NULL, '2016-08-16', 300.00, 100.00, 200.00),
(56, '002', 29, '2016-08-16', 60.00, 10.00, 50.00),
(57, '001', 30, '2016-08-16', 60.00, 10.00, 50.00),
(58, '001', 31, '2016-08-16', 125.00, 0.00, 125.00),
(59, '001', 32, '2016-08-16', 125.00, 25.00, 100.00),
(60, '001', 33, '2016-08-16', 125.00, 25.00, 100.00),
(61, '001', 34, '2016-08-16', 125.00, 25.00, 100.00),
(62, '001', 35, '2016-08-16', 185.00, 85.00, 100.00),
(63, '002', 36, '2016-08-16', 185.00, 85.00, 100.00),
(64, '003', 37, '2016-08-16', 110.00, 10.00, 100.00),
(65, '004', 38, '2016-08-16', 60.00, 10.00, 50.00),
(66, '001', 39, '2016-08-16', 60.00, 10.00, 50.00),
(67, '001', NULL, '2016-08-21', 905.00, 100.00, 805.00),
(68, '001', 46, '2016-09-06', 60.00, 10.00, 50.00),
(69, '004', NULL, '2016-09-07', 150.00, 50.00, 100.00),
(77, '002', NULL, '2016-09-17', 410.00, 50.00, 360.00),
(79, '002', NULL, '2016-09-17', 515.00, 115.00, 400.00),
(80, '001', 57, '2016-09-18', 185.00, 0.00, 185.00);

--
-- Triggers `tbl_credit_sales_subdetails`
--
DELIMITER $$
CREATE TRIGGER `update on delete tbl_credit_sales` AFTER DELETE ON `tbl_credit_sales_subdetails` FOR EACH ROW UPDATE tbl_credit_sales 
SET tbl_credit_sales.total_rem_amount = tbl_credit_sales.total_rem_amount +
old.received_amount WHERE tbl_credit_sales.cid = old.cid
$$
DELIMITER ;
DELIMITER $$
CREATE TRIGGER `update on insert tbl_credit_sales` AFTER INSERT ON `tbl_credit_sales_subdetails` FOR EACH ROW BEGIN
IF NEW.bill_id is NOT NULL THEN
 UPDATE tbl_credit_sales
SET tbl_credit_sales.total_rem_amount = tbl_credit_sales.total_rem_amount +
new.remaining_amount WHERE tbl_credit_sales.cid = new.cid;
ELSE
UPDATE tbl_credit_sales
SET tbl_credit_sales.total_rem_amount = tbl_credit_sales.total_rem_amount -
new.received_amount WHERE tbl_credit_sales.cid = new.cid;
END IF;
END
$$
DELIMITER ;

-- --------------------------------------------------------

--
-- Table structure for table `tbl_distributer`
--

CREATE TABLE `tbl_distributer` (
  `did` varchar(255) NOT NULL,
  `dname` varchar(255) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `tbl_distributer`
--

INSERT INTO `tbl_distributer` (`did`, `dname`) VALUES
('2432', 'dgd'),
('d1', 'saf'),
('d2', 'Laxmi Bakery Products');

-- --------------------------------------------------------

--
-- Table structure for table `tbl_employee_details`
--

CREATE TABLE `tbl_employee_details` (
  `eid` varchar(255) NOT NULL,
  `ename` varchar(255) NOT NULL,
  `post` varchar(255) NOT NULL,
  `address` varchar(255) NOT NULL,
  `contact` int(10) NOT NULL,
  `join_date` date NOT NULL,
  `monthly_salary` double(10,2) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `tbl_employee_details`
--

INSERT INTO `tbl_employee_details` (`eid`, `ename`, `post`, `address`, `contact`, `join_date`, `monthly_salary`) VALUES
('10', 'ankur', 'cook', 'ktm', 1443151, '2016-08-05', 20000.00),
('1234', 'ram', 'waiter', 'ktm', 9845612, '2016-09-02', 10000.00),
('7', 'soman', 'worker', 'lalitpur', 8978454, '2016-07-13', 1323.00),
('9', 'somamn', 'accountant', 'banepa', 45452, '2016-07-31', 1000.00);

-- --------------------------------------------------------

--
-- Table structure for table `tbl_employee_expenses`
--

CREATE TABLE `tbl_employee_expenses` (
  `eid` varchar(255) NOT NULL,
  `total_amount` double(10,2) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `tbl_employee_expenses`
--

INSERT INTO `tbl_employee_expenses` (`eid`, `total_amount`) VALUES
('1', 0.00),
('10', 153.00),
('12', 0.00),
('1234', 0.00),
('2', 0.00),
('3', 0.00),
('4', 0.00),
('5', 0.00),
('6', 0.00),
('7', 0.00),
('9', 0.00);

-- --------------------------------------------------------

--
-- Table structure for table `tbl_employee_expenses_details`
--

CREATE TABLE `tbl_employee_expenses_details` (
  `sn` bigint(20) NOT NULL,
  `eid` varchar(255) NOT NULL,
  `date` date NOT NULL,
  `item_code` varchar(255) NOT NULL,
  `item_name` varchar(255) NOT NULL,
  `item_rate` double(8,2) NOT NULL,
  `quantity` double(8,2) NOT NULL,
  `amount` double(10,2) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `tbl_employee_expenses_details`
--

INSERT INTO `tbl_employee_expenses_details` (`sn`, `eid`, `date`, `item_code`, `item_name`, `item_rate`, `quantity`, `amount`) VALUES
(1, '1', '2016-08-09', 'c1', 'coke', 45.00, 2.00, 90.00),
(3, '1', '2016-08-25', 'c1', 'coke', 45.00, 3.00, 135.00),
(4, '1', '2016-08-25', 'c1', 'coke', 45.00, 1.00, 45.00),
(5, '10', '2016-09-23', 'cm101', 'c momo', 108.00, 1.00, 108.00),
(6, '10', '2016-09-23', 'f1', 'fanta', 45.00, 1.00, 45.00);

-- --------------------------------------------------------

--
-- Table structure for table `tbl_items_mp`
--

CREATE TABLE `tbl_items_mp` (
  `subcat_id` varchar(255) NOT NULL,
  `item_code` varchar(255) NOT NULL,
  `item_name` varchar(255) NOT NULL,
  `cost_price` double(10,2) NOT NULL,
  `rate` int(10) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `tbl_items_mp`
--

INSERT INTO `tbl_items_mp` (`subcat_id`, `item_code`, `item_name`, `cost_price`, `rate`) VALUES
('cake', 'bf100', 'black forest', 0.00, 500),
('cake', 'br100', 'bread', 0.00, 60),
('cd', 'c1', 'coke', 0.00, 50),
('hd', 'cf102', 'hot chocolate', 0.00, 240),
('mo', 'cf103', 'tiramisu', 0.00, 130),
('mo', 'cm101', 'c momo', 0.00, 120),
('cd', 'dkc2', 'dark chocolate', 0.00, 120),
('cd', 'f1', 'fanta', 0.00, 50),
('cd', 'mil1', 'milk shake', 0.00, 90),
('mo', 'ms100', 'moss', 0.00, 60),
('cake', 'wf100', 'white forest', 0.00, 190);

-- --------------------------------------------------------

--
-- Table structure for table `tbl_misc`
--

CREATE TABLE `tbl_misc` (
  `date` date NOT NULL,
  `expenses_type` varchar(255) NOT NULL,
  `particular` varchar(255) NOT NULL,
  `amount` double(10,2) NOT NULL,
  `quantity` int(10) NOT NULL,
  `total_amount` double(10,2) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `tbl_misc`
--

INSERT INTO `tbl_misc` (`date`, `expenses_type`, `particular`, `amount`, `quantity`, `total_amount`) VALUES
('2016-09-07', 'Miscelloneous Expenses', 'parking', 50.00, 1, 50.00),
('2016-09-07', 'Administrative Expenses', 'rent', 12000.00, 1, 12000.00),
('2016-09-23', 'Administrative Expenses', 'Rent', 50000.00, 1, 50000.00);

-- --------------------------------------------------------

--
-- Table structure for table `tbl_mp`
--

CREATE TABLE `tbl_mp` (
  `cat_id` varchar(255) NOT NULL,
  `sub_cat_id` varchar(255) NOT NULL,
  `item_code` varchar(255) NOT NULL,
  `item_name` varchar(255) NOT NULL,
  `rate` int(10) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `tbl_mp`
--

INSERT INTO `tbl_mp` (`cat_id`, `sub_cat_id`, `item_code`, `item_name`, `rate`) VALUES
('002', 'cake', 'bf100', 'black forest', 500),
('002', 'cake', 'br100', 'bread', 60),
('001', 'cd', 'c1', 'coke', 50),
('001', 'hd', 'cf102', 'hot chocolate', 240),
('002', 'mo', 'cf103', 'tiramisu', 130),
('002', 'mo', 'cm101', 'c momo', 120),
('001', 'cd', 'dkc2', 'dark chocolate', 120),
('001', 'cd', 'f1', 'fanta', 50),
('001', 'cd', 'mil1', 'milk shake', 90),
('002', 'mo', 'ms100', 'moss', 60),
('002', 'cake', 'wf100', 'white forest', 190);

-- --------------------------------------------------------

--
-- Table structure for table `tbl_payroll`
--

CREATE TABLE `tbl_payroll` (
  `sn` int(10) NOT NULL,
  `eid` varchar(255) NOT NULL,
  `monthly_salary_paid` double(10,2) DEFAULT NULL,
  `employee_expenses` double(10,2) DEFAULT NULL,
  `payment_amount` double(10,2) DEFAULT NULL,
  `date` date NOT NULL,
  `remaining_salary` double(10,2) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `tbl_payroll`
--

INSERT INTO `tbl_payroll` (`sn`, `eid`, `monthly_salary_paid`, `employee_expenses`, `payment_amount`, `date`, `remaining_salary`) VALUES
(151, '3', 1000.00, 0.00, NULL, '2016-08-01', 1000.00),
(152, '4', 100000.00, 0.00, NULL, '2016-08-01', 100000.00),
(153, '5', 100.00, 0.00, NULL, '2016-08-01', 100.00),
(154, '6', 1000.00, 0.00, NULL, '2016-08-01', 1000.00),
(155, '7', 1323.00, 0.00, NULL, '2016-08-01', 1323.00),
(168, '9', 1000.00, 0.00, NULL, '2016-08-01', 1000.00),
(169, 'e1', NULL, 0.00, NULL, '2016-08-01', 0.00),
(170, 'e2', NULL, 0.00, NULL, '2016-08-01', 0.00),
(192, '2', NULL, NULL, 10000.00, '2016-08-09', 40000.00),
(196, '2', 100000.00, 0.00, 50000.00, '2016-08-01', 50000.00),
(198, '1', 4651.00, 90.00, NULL, '2016-08-01', 4741.00),
(199, '1', 4651.00, 0.00, NULL, '2016-09-01', 9392.00),
(200, '10', 20000.00, 0.00, NULL, '2016-09-01', 20000.00),
(201, '12', 234234.00, 0.00, NULL, '2016-09-01', 234234.00),
(202, '3', 1000.00, 0.00, NULL, '2016-09-01', 2000.00),
(203, '4', 100000.00, 0.00, NULL, '2016-09-01', 200000.00),
(204, '5', 100.00, 0.00, NULL, '2016-09-01', 200.00),
(205, '7', 1323.00, 0.00, NULL, '2016-09-01', 2646.00),
(206, '9', 1000.00, 0.00, NULL, '2016-09-01', 2000.00),
(207, '2', NULL, 0.00, NULL, '2016-09-01', 40000.00),
(208, '6', NULL, 0.00, NULL, '2016-09-01', 1000.00),
(209, '10', NULL, NULL, 17000.00, '2016-09-23', 10000.00),
(210, '9', NULL, NULL, 1000.00, '2016-09-23', 1000.00),
(211, '1234', 10000.00, 0.00, NULL, '2016-09-01', 10000.00),
(212, '10', NULL, NULL, 5000.00, '2016-09-24', 5000.00),
(213, '7', NULL, NULL, 2000.00, '2016-09-24', 646.00),
(214, '9', NULL, NULL, 1500.00, '2016-09-24', 500.00),
(215, '10', NULL, NULL, 5000.00, '2016-09-25', 0.00),
(216, '10', 20000.00, 153.00, NULL, '2016-10-01', 40153.00),
(217, '1234', 10000.00, 0.00, NULL, '2016-10-01', 20000.00),
(218, '7', 1323.00, 0.00, NULL, '2016-10-01', 2646.00),
(219, '9', 1000.00, 0.00, NULL, '2016-10-01', 2000.00),
(220, '1', NULL, 0.00, NULL, '2016-10-01', 9392.00),
(221, '12', NULL, 0.00, NULL, '2016-10-01', 234234.00),
(222, '2', NULL, 0.00, NULL, '2016-10-01', 40000.00),
(223, '3', NULL, 0.00, NULL, '2016-10-01', 2000.00),
(224, '4', NULL, 0.00, NULL, '2016-10-01', 200000.00),
(225, '5', NULL, 0.00, NULL, '2016-10-01', 200.00),
(226, '6', NULL, 0.00, NULL, '2016-10-01', 1000.00);

-- --------------------------------------------------------

--
-- Table structure for table `tbl_purchase`
--

CREATE TABLE `tbl_purchase` (
  `invoice_id` int(10) NOT NULL,
  `date` date NOT NULL,
  `did` varchar(255) NOT NULL,
  `dname` varchar(255) NOT NULL,
  `particular` varchar(255) NOT NULL,
  `purchase_total` double(10,2) NOT NULL,
  `discount` double(10,2) NOT NULL,
  `vat` double(10,2) NOT NULL,
  `payment` double(10,2) NOT NULL,
  `credit` double(10,2) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `tbl_purchase`
--

INSERT INTO `tbl_purchase` (`invoice_id`, `date`, `did`, `dname`, `particular`, `purchase_total`, `discount`, `vat`, `payment`, `credit`) VALUES
(32, '2016-08-25', '2432', 'dgd', 'Cash', 1800.00, 0.00, 0.00, 543.00, 0.00),
(213, '2016-08-25', 'd1', 'saf', 'Cash', 1000.00, 0.00, 0.00, 500.00, 0.00);

-- --------------------------------------------------------

--
-- Table structure for table `tbl_purchase_details`
--

CREATE TABLE `tbl_purchase_details` (
  `invoice_id` int(10) NOT NULL,
  `item_code` varchar(255) NOT NULL,
  `item_name` varchar(255) NOT NULL,
  `quantity` double(10,2) NOT NULL,
  `cost_price` double(10,2) NOT NULL,
  `amount` double(10,2) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `tbl_purchase_details`
--

INSERT INTO `tbl_purchase_details` (`invoice_id`, `item_code`, `item_name`, `quantity`, `cost_price`, `amount`) VALUES
(32, 'c1', 'coke', 23.00, 50.00, 1150.00);

-- --------------------------------------------------------

--
-- Table structure for table `tbl_resturant_info`
--

CREATE TABLE `tbl_resturant_info` (
  `name` varchar(255) NOT NULL,
  `location` varchar(255) NOT NULL,
  `phone_no` int(15) NOT NULL,
  `pan_no` int(20) NOT NULL,
  `vat` double(10,2) NOT NULL,
  `service_charge` double(10,2) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `tbl_resturant_info`
--

INSERT INTO `tbl_resturant_info` (`name`, `location`, `phone_no`, `pan_no`, `vat`, `service_charge`) VALUES
('The Chef''s Bakery', 'Jawalakhel, Kathmandu', 166332211, 99998888, 13.00, 10.00);

-- --------------------------------------------------------

--
-- Table structure for table `tbl_sales`
--

CREATE TABLE `tbl_sales` (
  `bill_id` bigint(20) NOT NULL,
  `name` varchar(255) NOT NULL,
  `date` date NOT NULL,
  `total_sales_amount` double(10,2) NOT NULL,
  `discount` double(10,2) NOT NULL,
  `vat` double(10,2) NOT NULL,
  `service_charge` double(10,2) NOT NULL,
  `grand_total_amount` double(10,2) NOT NULL,
  `rounded_amount` double(10,2) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `tbl_sales`
--

INSERT INTO `tbl_sales` (`bill_id`, `name`, `date`, `total_sales_amount`, `discount`, `vat`, `service_charge`, `grand_total_amount`, `rounded_amount`) VALUES
(2, 'Table 1', '2016-08-10', 50.00, 0.00, 6.50, 5.00, 60.00, 1.50),
(3, 'Table 2', '2016-08-10', 50.00, 0.00, 6.50, 5.00, 60.00, 1.50),
(4, 'Table 1', '2016-08-11', 150.00, 0.00, 19.50, 15.00, 185.00, -0.50),
(5, 'Table 1', '2016-08-11', 50.00, 0.00, 6.50, 5.00, 60.00, 1.50),
(6, 'Table 1', '2016-08-11', 50.00, 0.00, 6.50, 5.00, 60.00, 1.50),
(7, 'Table 2', '2016-08-11', 150.00, 0.00, 19.50, 15.00, 185.00, -0.50),
(8, 'Table 1', '2016-08-11', 50.00, 0.00, 6.50, 5.00, 60.00, 1.50),
(9, 'Table 1', '2016-08-11', 50.00, 0.00, 6.50, 5.00, 60.00, 1.50),
(10, 'Table 2', '2016-08-11', 100.00, 0.00, 13.00, 10.00, 125.00, -2.00),
(11, 'Table 1', '2016-08-11', 95.00, 0.00, 12.35, 9.50, 115.00, 1.85),
(12, 'Table 1', '2016-08-12', 50.00, 0.00, 6.50, 5.00, 60.00, 1.50),
(13, 'Table 2', '2016-08-16', 50.00, 0.00, 6.50, 5.00, 60.00, 1.50),
(14, 'Table 2', '2016-08-16', 50.00, 0.00, 6.50, 5.00, 60.00, 1.50),
(15, 'Table 2', '2016-08-16', 50.00, 0.00, 6.50, 5.00, 60.00, 1.50),
(16, 'Table 2', '2016-08-16', 50.00, 0.00, 6.50, 5.00, 60.00, 1.50),
(17, 'Table 4', '2016-08-16', 50.00, 0.00, 6.50, 5.00, 60.00, 1.50),
(18, 'Table 2', '2016-08-16', 50.00, 0.00, 6.50, 5.00, 60.00, 1.50),
(19, 'Table 2', '2016-08-16', 150.00, 0.00, 19.50, 15.00, 185.00, -0.50),
(20, 'Table 2', '2016-08-16', 50.00, 0.00, 6.50, 5.00, 60.00, 1.50),
(21, 'Table 2', '2016-08-16', 50.00, 0.00, 6.50, 5.00, 60.00, 1.50),
(22, 'Table 1', '2016-08-16', 150.00, 0.00, 19.50, 15.00, 185.00, -0.50),
(23, 'Table 1', '2016-08-16', 50.00, 0.00, 6.50, 5.00, 60.00, 1.50),
(24, 'Table 3', '2016-08-16', 50.00, 0.00, 6.50, 5.00, 60.00, 1.50),
(25, 'Table 2', '2016-08-16', 50.00, 0.00, 6.50, 5.00, 60.00, 1.50),
(26, 'Table 2', '2016-08-16', 100.00, 0.00, 13.00, 10.00, 125.00, -2.00),
(27, 'Table 1', '2016-08-16', 50.00, 0.00, 6.50, 5.00, 60.00, 1.50),
(28, 'Table 1', '2016-08-16', 50.00, 0.00, 6.50, 5.00, 60.00, 1.50),
(29, 'Table 2', '2016-08-16', 50.00, 0.00, 6.50, 5.00, 60.00, 1.50),
(30, 'Table 1', '2016-08-16', 50.00, 0.00, 6.50, 5.00, 60.00, 1.50),
(31, 'Table 1', '2016-08-16', 100.00, 0.00, 13.00, 10.00, 125.00, -2.00),
(32, 'Table 1', '2016-08-16', 100.00, 0.00, 13.00, 10.00, 125.00, -2.00),
(33, 'Table 1', '2016-08-16', 100.00, 0.00, 13.00, 10.00, 125.00, -2.00),
(34, 'Table 1', '2016-08-16', 100.00, 0.00, 13.00, 10.00, 125.00, -2.00),
(35, 'Table 1', '2016-08-16', 150.00, 0.00, 19.50, 15.00, 185.00, -0.50),
(36, 'Table 2', '2016-08-16', 150.00, 0.00, 19.50, 15.00, 185.00, -0.50),
(37, 'Table 1', '2016-08-16', 90.00, 0.00, 11.70, 9.00, 110.00, 0.70),
(38, 'Table 2', '2016-08-16', 50.00, 0.00, 6.50, 5.00, 60.00, 1.50),
(39, 'Table 2', '2016-08-16', 50.00, 0.00, 6.50, 5.00, 60.00, 1.50),
(40, 'Table 1', '2016-08-24', 100.00, 0.00, 13.00, 10.00, 125.00, -2.00),
(41, 'Table 2', '2016-08-25', 50.00, 0.00, 6.50, 5.00, 60.00, 1.50),
(42, 'Table 1', '2016-08-25', 95.00, 0.00, 12.35, 9.50, 115.00, 1.85),
(43, 'Table 2', '2016-08-25', 50.00, 0.00, 6.50, 5.00, 60.00, 1.50),
(44, 'Table 2', '2016-08-25', 50.00, 0.00, 6.50, 5.00, 60.00, 1.50),
(45, 'Table 1', '2016-08-25', 45.00, 0.00, 5.85, 4.50, 55.00, 0.35),
(46, 'Table 1', '2016-09-06', 50.00, 0.00, 6.50, 5.00, 60.00, 1.50),
(47, 'Table 10', '2016-09-12', 50.00, 0.00, 6.50, 5.00, 60.00, 1.50),
(48, 'Table 1', '2016-09-12', 250.00, 0.00, 32.50, 25.00, 310.00, -2.50),
(49, 'Table 2', '2016-09-12', 50.00, 0.00, 6.50, 5.00, 60.00, 1.50),
(50, 'Table 1', '2016-09-17', 150.00, 0.00, 19.50, 15.00, 185.00, -0.50),
(51, 'Table 1', '2016-09-17', 225.00, 25.00, 29.25, 22.50, 275.00, 1.75),
(52, 'Table 2', '2016-09-17', 125.00, 25.00, 16.25, 12.50, 155.00, -1.25),
(53, 'Table 3', '2016-09-17', 200.00, 0.00, 26.00, 20.00, 245.00, 1.00),
(54, 'Table 2', '2016-09-17', 150.00, 0.00, 19.50, 15.00, 185.00, -0.50),
(55, 'Table 1', '2016-09-18', 50.00, 0.00, 6.50, 5.00, 60.00, 1.50),
(56, 'Table 1', '2016-09-18', 150.00, 0.00, 19.50, 15.00, 185.00, -0.50),
(57, 'Table 1', '2016-09-18', 150.00, 0.00, 19.50, 15.00, 185.00, -0.50),
(58, 'Table 1', '2016-09-22', 50.00, 0.00, 6.50, 5.00, 60.00, 1.50),
(59, 'Table 1', '2016-09-23', 150.00, 0.00, 19.50, 15.00, 185.00, -0.50),
(60, 'Table 2', '2016-09-23', 50.00, 0.00, 6.50, 5.00, 60.00, 1.50),
(61, 'Table 3', '2016-09-23', 400.00, 0.00, 52.00, 40.00, 490.00, 2.00),
(62, 'Table 5', '2016-09-23', 150.00, 0.00, 19.50, 15.00, 185.00, -0.50),
(63, 'Table 1', '2016-09-23', 500.00, 0.00, 65.00, 50.00, 615.00, 0.00),
(64, 'Table 1', '2016-09-23', 600.00, 0.00, 78.00, 60.00, 740.00, -2.00),
(65, 'Table 1', '2016-09-23', 390.00, 0.00, 50.70, 39.00, 480.00, -0.30),
(66, 'Table 1', '2016-09-23', 250.00, 0.00, 32.50, 25.00, 310.00, -2.50),
(67, 'Table 2', '2016-09-23', 190.00, 10.00, 24.70, 19.00, 235.00, -1.30),
(68, 'Table 3', '2016-09-23', 190.00, 10.00, 24.70, 19.00, 235.00, -1.30),
(69, 'Table 2', '2016-09-23', 710.00, 40.00, 92.30, 71.00, 875.00, -1.70),
(70, 'Table 1', '2016-09-23', 285.00, 15.00, 37.05, 28.50, 350.00, 0.55),
(71, 'Table 1', '2016-09-23', 235.00, 15.00, 30.55, 23.50, 290.00, -0.95),
(72, 'Table 1', '2016-09-23', 285.00, 15.00, 37.05, 28.50, 350.00, 0.55),
(73, 'Table 2', '2016-09-23', 270.00, 30.00, 35.10, 27.00, 330.00, 2.10),
(74, 'Table 5', '2016-09-23', 200.00, 0.00, 26.00, 20.00, 245.00, 1.00),
(75, 'Table 2', '2016-09-23', 135.00, 15.00, 17.55, 13.50, 165.00, 1.05);

-- --------------------------------------------------------

--
-- Table structure for table `tbl_sales_details`
--

CREATE TABLE `tbl_sales_details` (
  `bill_id` bigint(20) NOT NULL,
  `item_name` varchar(255) NOT NULL,
  `quantity` double(10,2) NOT NULL,
  `price` double(10,2) NOT NULL,
  `amount` double(10,2) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `tbl_sales_details`
--

INSERT INTO `tbl_sales_details` (`bill_id`, `item_name`, `quantity`, `price`, `amount`) VALUES
(2, 'fanta', 1.00, 50.00, 50.00),
(3, 'fanta', 1.00, 50.00, 50.00),
(4, 'fanta', 3.00, 50.00, 150.00),
(5, 'fanta', 1.00, 50.00, 50.00),
(6, 'fanta', 1.00, 50.00, 50.00),
(7, 'coke', 1.00, 50.00, 50.00),
(7, 'fanta', 1.00, 50.00, 50.00),
(7, 'fanta', 1.00, 50.00, 50.00),
(8, 'fanta', 1.00, 50.00, 50.00),
(9, 'fanta', 1.00, 50.00, 50.00),
(10, 'fanta', 2.00, 50.00, 100.00),
(11, 'sprite', 1.00, 45.00, 45.00),
(11, 'fanta', 1.00, 50.00, 50.00),
(12, 'fanta', 1.00, 50.00, 50.00),
(13, 'fanta', 1.00, 50.00, 50.00),
(14, 'fanta', 1.00, 50.00, 50.00),
(15, 'coke', 1.00, 50.00, 50.00),
(16, 'fanta', 1.00, 50.00, 50.00),
(16, 'coke', 1.00, 50.00, 50.00),
(17, 'coke', 1.00, 50.00, 50.00),
(18, 'coke', 1.00, 50.00, 50.00),
(19, 'coke', 3.00, 50.00, 150.00),
(20, 'coke', 1.00, 50.00, 50.00),
(21, 'coke', 1.00, 50.00, 50.00),
(22, 'coke', 3.00, 50.00, 150.00),
(23, 'fanta', 1.00, 50.00, 50.00),
(24, 'coke', 1.00, 50.00, 50.00),
(25, 'fanta', 1.00, 50.00, 50.00),
(26, 'fanta', 2.00, 50.00, 100.00),
(27, 'fanta', 1.00, 50.00, 50.00),
(28, 'coke', 1.00, 50.00, 50.00),
(29, 'coke', 1.00, 50.00, 50.00),
(30, 'coke', 1.00, 50.00, 50.00),
(31, 'coke', 2.00, 50.00, 100.00),
(32, 'coke', 2.00, 50.00, 100.00),
(33, 'coke', 2.00, 50.00, 100.00),
(34, 'fanta', 2.00, 50.00, 100.00),
(35, 'coke', 3.00, 50.00, 150.00),
(36, 'fanta', 3.00, 50.00, 150.00),
(37, 'sprite', 2.00, 45.00, 90.00),
(38, 'fanta', 1.00, 50.00, 50.00),
(39, 'fanta', 1.00, 50.00, 50.00),
(40, 'coke', 2.00, 50.00, 100.00),
(41, 'coke', 1.00, 50.00, 50.00),
(42, 'coke', 1.00, 50.00, 50.00),
(42, 'sprite', 1.00, 45.00, 45.00),
(43, 'coke', 1.00, 50.00, 50.00),
(44, 'coke', 1.00, 50.00, 50.00),
(45, 'sprite', 1.00, 45.00, 45.00),
(46, 'coke', 1.00, 50.00, 50.00),
(47, 'fanta', 1.00, 50.00, 50.00),
(48, 'fanta', 2.00, 50.00, 100.00),
(48, 'fanta', 2.00, 50.00, 100.00),
(48, 'coke', 1.00, 50.00, 50.00),
(49, 'coke', 1.00, 50.00, 50.00),
(50, 'coke', 1.00, 50.00, 50.00),
(50, 'fanta', 2.00, 50.00, 100.00),
(51, 'fanta', 1.00, 50.00, 50.00),
(51, 'coke', 50.00, 4.00, 200.00),
(52, 'coke', 50.00, 3.00, 150.00),
(53, 'coke', 50.00, 4.00, 200.00),
(54, 'coke', 50.00, 3.00, 150.00),
(55, 'coke', 50.00, 1.00, 50.00),
(56, 'coke', 50.00, 3.00, 150.00),
(57, 'coke', 50.00, 3.00, 150.00),
(58, 'fanta', 50.00, 1.00, 50.00),
(59, 'fanta', 50.00, 3.00, 150.00),
(60, 'coke', 50.00, 1.00, 50.00),
(61, 'fanta', 50.00, 3.00, 150.00),
(61, 'coke', 50.00, 5.00, 250.00),
(62, 'fanta', 50.00, 1.00, 50.00),
(62, 'fanta', 2.00, 50.00, 100.00),
(63, 'fanta', 50.00, 2.00, 100.00),
(63, 'fanta', 50.00, 5.00, 250.00),
(63, 'fanta', 50.00, 3.00, 150.00),
(64, 'fanta', 2.00, 50.00, 100.00),
(64, 'coke', 10.00, 50.00, 500.00),
(65, 'fanta', 3.00, 50.00, 150.00),
(65, 'c momo', 2.00, 120.00, 240.00),
(66, 'fanta', 1.00, 50.00, 50.00),
(66, 'coke', 2.00, 50.00, 100.00),
(66, 'fanta', 1.00, 50.00, 50.00),
(66, 'fanta', 1.00, 50.00, 50.00),
(67, 'coke', 2.00, 50.00, 100.00),
(67, 'fanta', 2.00, 50.00, 100.00),
(68, 'fanta', 2.00, 50.00, 100.00),
(68, 'coke', 2.00, 50.00, 100.00),
(69, 'coke', 5.00, 50.00, 250.00),
(69, 'fanta', 10.00, 50.00, 500.00),
(70, 'fanta', 1.00, 50.00, 50.00),
(70, 'coke', 5.00, 50.00, 250.00),
(71, 'fanta', 5.00, 50.00, 250.00),
(72, 'fanta', 6.00, 50.00, 300.00),
(73, 'fanta', 2.00, 50.00, 100.00),
(73, 'coke', 4.00, 50.00, 200.00),
(74, 'fanta', 4.00, 50.00, 200.00),
(75, 'fanta', 2.00, 50.00, 100.00),
(75, 'coke', 1.00, 50.00, 50.00);

-- --------------------------------------------------------

--
-- Table structure for table `tbl_sales_temp`
--

CREATE TABLE `tbl_sales_temp` (
  `sn` double(7,2) NOT NULL,
  `item_code` varchar(255) NOT NULL,
  `item_name` varchar(255) NOT NULL,
  `quantity` double(8,2) NOT NULL,
  `price` double(8,2) NOT NULL,
  `amount` double(10,2) NOT NULL,
  `token_id` int(10) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `tbl_sales_temp`
--

INSERT INTO `tbl_sales_temp` (`sn`, `item_code`, `item_name`, `quantity`, `price`, `amount`, `token_id`) VALUES
(2.01, 'c1', 'coke', 2.00, 50.00, 100.00, 2);

-- --------------------------------------------------------

--
-- Table structure for table `tbl_saving`
--

CREATE TABLE `tbl_saving` (
  `date` date NOT NULL,
  `transaction_id` varchar(255) NOT NULL,
  `financial_institute_name` varchar(255) NOT NULL,
  `particular` varchar(255) NOT NULL,
  `saving` double(10,2) NOT NULL,
  `withdrawl` double(10,2) NOT NULL,
  `total` double(10,2) NOT NULL,
  `grand_total` double(10,2) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `tbl_saving`
--

INSERT INTO `tbl_saving` (`date`, `transaction_id`, `financial_institute_name`, `particular`, `saving`, `withdrawl`, `total`, `grand_total`) VALUES
('2016-08-25', '21', 'dasfv', 'Saving', 400.00, 0.00, 400.00, 400.00),
('2016-08-25', '', 'dasg', 'Withdrawal', 0.00, 500.00, -500.00, -100.00),
('2016-09-07', '', 'Gamcha bachat', 'Saving', 200.00, 0.00, 200.00, 100.00),
('2016-09-07', '', 'Chhari bachat', 'Saving', 500.00, 0.00, 500.00, 600.00),
('2016-09-07', '', 'Gamcha bachat', 'Withdrawal', 0.00, 300.00, -300.00, 300.00),
('2016-09-23', '12345', 'Gamcha bachat', 'Saving', 500.00, 0.00, 500.00, 400.00);

-- --------------------------------------------------------

--
-- Table structure for table `tbl_subassets`
--

CREATE TABLE `tbl_subassets` (
  `id` varchar(255) NOT NULL,
  `name` varchar(255) NOT NULL,
  `quantity` int(10) NOT NULL,
  `amount` double(10,2) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Table structure for table `tbl_subassets_details`
--

CREATE TABLE `tbl_subassets_details` (
  `id` varchar(255) NOT NULL,
  `name` varchar(255) NOT NULL,
  `date` date NOT NULL,
  `cost_price` double(10,2) NOT NULL,
  `quantity` int(10) NOT NULL,
  `total_amount` double(10,2) NOT NULL,
  `remaining_quantity` int(10) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `tbl_subassets_details`
--

INSERT INTO `tbl_subassets_details` (`id`, `name`, `date`, `cost_price`, `quantity`, `total_amount`, `remaining_quantity`) VALUES
('fur2', 'table', '2016-08-25', 400.00, 3, 1200.00, 3),
('G1', 'wine glass', '2016-08-25', 40.00, 15, 600.00, 15),
('G1', 'water glass', '2016-08-25', 25.00, 15, 375.00, 15),
('G1', 'beer glass', '2016-08-25', 25.00, 15, 375.00, 15),
('G1', 'beer glass', '2016-08-25', 25.00, 12, 300.00, 12),
('G1', 'beer glass', '2016-08-25', 25.00, 8, 200.00, 8),
('G1', 'beer glass', '2016-08-25', 25.00, 9, 225.00, 9),
('G1', 'beer glass', '2016-08-25', 25.00, 5, 125.00, 5),
('G1', 'beer glass', '2016-08-25', 25.00, 12, 300.00, 12),
('fur3', 'kitchen fridge', '2016-08-25', 40000.00, 1, 40000.00, 1),
('Fur8', 'Bed', '2016-08-25', 12000.00, 2, 24000.00, 2),
('fur5', 'home ac', '2016-08-25', 12000.00, 2, 24000.00, 2);

-- --------------------------------------------------------

--
-- Table structure for table `tbl_subcategory`
--

CREATE TABLE `tbl_subcategory` (
  `cat_id` varchar(255) NOT NULL,
  `subcat_id` varchar(255) NOT NULL,
  `subcategory_name` varchar(255) NOT NULL,
  `items_num` int(10) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `tbl_subcategory`
--

INSERT INTO `tbl_subcategory` (`cat_id`, `subcat_id`, `subcategory_name`, `items_num`) VALUES
('001', '25', 'hotlemon', 0),
('002', 'cake', 'cake', 5),
('001', 'cd', 'cold drinks', 3),
('001', 'hd', 'hot drinks', 5),
('002', 'mo', 'momo', 3);

-- --------------------------------------------------------

--
-- Table structure for table `tbl_user`
--

CREATE TABLE `tbl_user` (
  `id` varchar(255) NOT NULL,
  `username` varchar(255) NOT NULL,
  `password` varchar(255) NOT NULL,
  `privileges` varchar(255) NOT NULL,
  `date_modified` date NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `tbl_user`
--

INSERT INTO `tbl_user` (`id`, `username`, `password`, `privileges`, `date_modified`) VALUES
('001', 'admin', 'admin', 'Admin', '2016-08-25'),
('002', 'user', 'user', 'User', '2016-08-25');

-- --------------------------------------------------------

--
-- Table structure for table `tbl_wastage`
--

CREATE TABLE `tbl_wastage` (
  `id` varchar(255) NOT NULL,
  `name` varchar(255) NOT NULL,
  `date` date NOT NULL,
  `particular` varchar(255) NOT NULL,
  `cost_price` double(10,2) NOT NULL,
  `quantity` double(10,2) NOT NULL,
  `total_amount` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Indexes for dumped tables
--

--
-- Indexes for table `tbl_assets`
--
ALTER TABLE `tbl_assets`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `tbl_cashinhand_details`
--
ALTER TABLE `tbl_cashinhand_details`
  ADD PRIMARY KEY (`sn`);

--
-- Indexes for table `tbl_category`
--
ALTER TABLE `tbl_category`
  ADD PRIMARY KEY (`cat_id`);

--
-- Indexes for table `tbl_credit_purchase`
--
ALTER TABLE `tbl_credit_purchase`
  ADD PRIMARY KEY (`did`);

--
-- Indexes for table `tbl_credit_sales`
--
ALTER TABLE `tbl_credit_sales`
  ADD PRIMARY KEY (`cid`);

--
-- Indexes for table `tbl_credit_sales_subdetails`
--
ALTER TABLE `tbl_credit_sales_subdetails`
  ADD PRIMARY KEY (`sn`);

--
-- Indexes for table `tbl_distributer`
--
ALTER TABLE `tbl_distributer`
  ADD PRIMARY KEY (`did`);

--
-- Indexes for table `tbl_employee_details`
--
ALTER TABLE `tbl_employee_details`
  ADD PRIMARY KEY (`eid`),
  ADD KEY `Eid` (`eid`),
  ADD KEY `Eid_2` (`eid`),
  ADD KEY `Eid_3` (`eid`),
  ADD KEY `Eid_4` (`eid`);

--
-- Indexes for table `tbl_employee_expenses`
--
ALTER TABLE `tbl_employee_expenses`
  ADD PRIMARY KEY (`eid`);

--
-- Indexes for table `tbl_employee_expenses_details`
--
ALTER TABLE `tbl_employee_expenses_details`
  ADD PRIMARY KEY (`sn`),
  ADD KEY `eid` (`eid`);

--
-- Indexes for table `tbl_items_mp`
--
ALTER TABLE `tbl_items_mp`
  ADD PRIMARY KEY (`item_code`);

--
-- Indexes for table `tbl_mp`
--
ALTER TABLE `tbl_mp`
  ADD PRIMARY KEY (`item_code`);

--
-- Indexes for table `tbl_payroll`
--
ALTER TABLE `tbl_payroll`
  ADD PRIMARY KEY (`sn`),
  ADD KEY `date` (`date`);

--
-- Indexes for table `tbl_purchase`
--
ALTER TABLE `tbl_purchase`
  ADD PRIMARY KEY (`invoice_id`);

--
-- Indexes for table `tbl_purchase_details`
--
ALTER TABLE `tbl_purchase_details`
  ADD KEY `invoice_id` (`invoice_id`);

--
-- Indexes for table `tbl_sales`
--
ALTER TABLE `tbl_sales`
  ADD PRIMARY KEY (`bill_id`);

--
-- Indexes for table `tbl_sales_details`
--
ALTER TABLE `tbl_sales_details`
  ADD KEY `bill_id` (`bill_id`),
  ADD KEY `bill_id_2` (`bill_id`);

--
-- Indexes for table `tbl_sales_temp`
--
ALTER TABLE `tbl_sales_temp`
  ADD PRIMARY KEY (`sn`);

--
-- Indexes for table `tbl_subassets_details`
--
ALTER TABLE `tbl_subassets_details`
  ADD KEY `id` (`id`),
  ADD KEY `id_2` (`id`);

--
-- Indexes for table `tbl_subcategory`
--
ALTER TABLE `tbl_subcategory`
  ADD PRIMARY KEY (`subcat_id`),
  ADD KEY `cat_id` (`cat_id`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `tbl_cashinhand_details`
--
ALTER TABLE `tbl_cashinhand_details`
  MODIFY `sn` bigint(20) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=3;
--
-- AUTO_INCREMENT for table `tbl_credit_sales_subdetails`
--
ALTER TABLE `tbl_credit_sales_subdetails`
  MODIFY `sn` bigint(20) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=81;
--
-- AUTO_INCREMENT for table `tbl_employee_expenses_details`
--
ALTER TABLE `tbl_employee_expenses_details`
  MODIFY `sn` bigint(20) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=7;
--
-- AUTO_INCREMENT for table `tbl_payroll`
--
ALTER TABLE `tbl_payroll`
  MODIFY `sn` int(10) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=227;
--
-- AUTO_INCREMENT for table `tbl_sales_temp`
--
ALTER TABLE `tbl_sales_temp`
  MODIFY `sn` double(7,2) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=3;
--
-- Constraints for dumped tables
--

--
-- Constraints for table `tbl_employee_expenses_details`
--
ALTER TABLE `tbl_employee_expenses_details`
  ADD CONSTRAINT `empexp_expdet_fk` FOREIGN KEY (`eid`) REFERENCES `tbl_employee_expenses` (`eid`);

--
-- Constraints for table `tbl_purchase_details`
--
ALTER TABLE `tbl_purchase_details`
  ADD CONSTRAINT `purchase_purchasedetails_fk` FOREIGN KEY (`invoice_id`) REFERENCES `tbl_purchase` (`invoice_id`) ON DELETE CASCADE ON UPDATE NO ACTION;

--
-- Constraints for table `tbl_sales_details`
--
ALTER TABLE `tbl_sales_details`
  ADD CONSTRAINT `sales_salesdetail_fk` FOREIGN KEY (`bill_id`) REFERENCES `tbl_sales` (`bill_id`) ON DELETE CASCADE ON UPDATE NO ACTION;

--
-- Constraints for table `tbl_subcategory`
--
ALTER TABLE `tbl_subcategory`
  ADD CONSTRAINT `cat_fk` FOREIGN KEY (`cat_id`) REFERENCES `tbl_category` (`cat_id`) ON DELETE NO ACTION ON UPDATE NO ACTION;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
