-- phpMyAdmin SQL Dump
-- version 5.1.0
-- https://www.phpmyadmin.net/
--
-- Máy chủ: 127.0.0.1
-- Thời gian đã tạo: Th9 18, 2021 lúc 12:19 PM
-- Phiên bản máy phục vụ: 10.4.19-MariaDB
-- Phiên bản PHP: 8.0.6

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Cơ sở dữ liệu: `laundrydb`
--

-- --------------------------------------------------------

--
-- Cấu trúc bảng cho bảng `admin`
--

CREATE TABLE `admin` (
  `id` varchar(20) NOT NULL,
  `password` varchar(20) DEFAULT NULL,
  `role` int(11) NOT NULL,
  `employee_id` varchar(20) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Đang đổ dữ liệu cho bảng `admin`
--

INSERT INTO `admin` (`id`, `password`, `role`, `employee_id`) VALUES
('sa', '123', 2, 'E001'),
('sa1', '123', 1, 'E004'),
('sa2', '123', 0, 'E005'),
('sa3', '123', 0, 'E008'),
('sa5', '123', 1, 'E009'),
('sa6', '123', 0, 'E111'),
('sa7', '123', 0, 'E112');

-- --------------------------------------------------------

--
-- Cấu trúc bảng cho bảng `orders`
--

CREATE TABLE `orders` (
  `id` int(11) NOT NULL,
  `address` varchar(255) DEFAULT NULL,
  `created_at` datetime DEFAULT NULL,
  `user_email` varchar(255) DEFAULT NULL,
  `user_phone` varchar(50) DEFAULT NULL,
  `status` int(11) DEFAULT NULL,
  `payment` float NOT NULL DEFAULT 0,
  `shipper_id` varchar(20) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Đang đổ dữ liệu cho bảng `orders`
--

INSERT INTO `orders` (`id`, `address`, `created_at`, `user_email`, `user_phone`, `status`, `payment`, `shipper_id`) VALUES
(17, '590 CMT8,Quận 3,Tp.HCM', '2021-09-08 16:20:35', 'ijimal118@gmail.com', '0935097778', 2, 5.5, 's001'),
(19, '590 CMT8,Quận 3,Tp.HCM', '2021-09-17 14:53:54', 'ijimal118@gmail.com', '0935097778', 5, 4.4, 's005'),
(20, '590 CMT8,Quận 3,Tp.HCM', '2021-09-17 16:25:37', 'ijimal118@gmail.com', '0935097778', 0, 13.2, 'S009');

-- --------------------------------------------------------

--
-- Cấu trúc bảng cho bảng `order_detail`
--

CREATE TABLE `order_detail` (
  `id` int(11) NOT NULL,
  `order_id` int(11) DEFAULT NULL,
  `services_id` int(11) DEFAULT NULL,
  `price` int(11) DEFAULT NULL,
  `shipping_fee` int(11) DEFAULT NULL,
  `quantity` int(11) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Đang đổ dữ liệu cho bảng `order_detail`
--

INSERT INTO `order_detail` (`id`, `order_id`, `services_id`, `price`, `shipping_fee`, `quantity`) VALUES
(15, 17, 2, 4, 0, 2),
(16, 17, 20, 1, 0, 1),
(19, 19, 10, 3, 0, 1),
(20, 19, 21, 1, 0, 1),
(21, 20, 2, 4, 0, 2),
(22, 20, 1, 6, 0, 2),
(23, 20, 20, 1, 0, 1),
(24, 20, 21, 1, 0, 1);

-- --------------------------------------------------------

--
-- Cấu trúc bảng cho bảng `services`
--

CREATE TABLE `services` (
  `id` int(11) NOT NULL,
  `name` varchar(50) DEFAULT NULL,
  `price` int(11) DEFAULT NULL,
  `type` varchar(50) DEFAULT NULL,
  `image` text DEFAULT NULL,
  `description` text DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Đang đổ dữ liệu cho bảng `services`
--

INSERT INTO `services` (`id`, `name`, `price`, `type`, `image`, `description`) VALUES
(1, 'Dry Cleaning & Ironed', 3, 'Kg', 'dry-cleaning.svg', 'Delicate items and fabrics. Cleaned, ironed and put on hangers.\r\nWe dry clean or wash your items according to the care label. Clothes are ironed and returned on a hanger.'),
(2, 'Iron Only', 2, 'Kg', 'ironing.svg', 'In select locations we offer an iron only service for shirts and blouses. You wash and dry, we iron!'),
(9, 'Wash', 2, 'Kg', 'wash.svg', 'Everyday laundry as well as towels, sheets and duvet covers. Washed and tumble-dried, no ironing.'),
(10, 'Wash & Iron', 3, 'Kg', 'washiron.jpg', 'Items are washed, tumble-dried, ironed and delivered on hangers.'),
(11, 'DUVETS ', 1, 'Item', 'pillow.jpg', 'Larger items that require a different cleaning process.Includes blanket, pillow, mattress'),
(20, 'OMO', 1, 'Demand', 'omo.jpg', 'Washing liquid , We will also accept laundry detergent on request'),
(21, 'Lix', 1, 'Demand', 'Lix.jpg', ' Washing liquid, We will also accept laundry detergent on request'),
(22, 'Downy', 1, 'Demand', 'Downy-Fabric-Protect.jpg', 'Fabric softener, We will also accept washing and rinsing on request'),
(23, 'Comfort', 1, 'Demand', 'comfort.webp', 'Fabric softener, We will also accept washing and rinsing on request'),
(25, 'Aba', 1, 'Demand', 'Aba.jpg', ''),
(26, 'Omo', 1, 'Demand', 'Omo.jpg', '');

-- --------------------------------------------------------

--
-- Cấu trúc bảng cho bảng `shipper`
--

CREATE TABLE `shipper` (
  `id` varchar(20) NOT NULL,
  `password` varchar(20) DEFAULT NULL,
  `phone` varchar(50) DEFAULT NULL,
  `salary` int(11) DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL,
  `image` varchar(255) DEFAULT NULL,
  `status` int(11) NOT NULL DEFAULT 0
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Đang đổ dữ liệu cho bảng `shipper`
--

INSERT INTO `shipper` (`id`, `password`, `phone`, `salary`, `name`, `image`, `status`) VALUES
('s001', '123456', '0935097778', 5, 'Nguyễn Văn Hậu', 'Shipper1.png', 0),
('S002', '2222', '01678954698', 3, 'Lương Phúc Hải', 'Shipper2.png', 0),
('s005', '123', '0937378889', 1, 'Nguyễn Thanh Bình', 'Shipper3.png', 1),
('S009', '123', '0896494621', 2, 'Phúc Hậu', 'Shipper4.png', 1);

-- --------------------------------------------------------

--
-- Cấu trúc bảng cho bảng `tbpayslip`
--

CREATE TABLE `tbpayslip` (
  `id` varchar(20) NOT NULL,
  `name2` varchar(50) DEFAULT NULL,
  `depart3` varchar(50) DEFAULT NULL,
  `atm4` varchar(50) DEFAULT NULL,
  `salmonth5` date DEFAULT NULL,
  `coefsal7` decimal(15,2) UNSIGNED DEFAULT 0.00,
  `coefposis8` decimal(15,2) UNSIGNED DEFAULT 0.00,
  `liabfac9` decimal(15,2) UNSIGNED DEFAULT 0.00,
  `marsys10` decimal(15,2) UNSIGNED DEFAULT 0.00,
  `acworkday12` smallint(6) UNSIGNED DEFAULT 0,
  `oversal14` decimal(15,2) UNSIGNED DEFAULT 0.00,
  `superdiem15` decimal(15,2) UNSIGNED DEFAULT 0.00,
  `phonesup16` decimal(15,2) UNSIGNED DEFAULT 0.00,
  `tradeallow17` decimal(15,2) UNSIGNED DEFAULT 0.00,
  `salincrease18` decimal(15,2) UNSIGNED DEFAULT 0.00,
  `midsimeal19` decimal(15,2) UNSIGNED DEFAULT 0.00,
  `bonoustet20` decimal(15,2) UNSIGNED DEFAULT 0.00,
  `monthlyunfe25` decimal(15,2) UNSIGNED DEFAULT 0.00,
  `advances` decimal(15,2) NOT NULL DEFAULT 0.00
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Đang đổ dữ liệu cho bảng `tbpayslip`
--

INSERT INTO `tbpayslip` (`id`, `name2`, `depart3`, `atm4`, `salmonth5`, `coefsal7`, `coefposis8`, `liabfac9`, `marsys10`, `acworkday12`, `oversal14`, `superdiem15`, `phonesup16`, `tradeallow17`, `salincrease18`, `midsimeal19`, `bonoustet20`, `monthlyunfe25`, `advances`) VALUES
('E001', 'Trần Quang Thạch', 'director', 'ATM666777888', '2021-09-02', '5.00', '7.00', '0.00', '1.00', 21, '3000000.00', '1.00', '300000.00', '2000000.00', '500000.00', '800000.00', '3000000.00', '100000.00', '1000000.00'),
('E004', 'Nguyễn Triển Chiêu', 'leader', 'ATM999000888', '2021-09-10', '5.00', '5.00', '0.00', '1.00', 22, '3000000.00', '1.00', '300000.00', '2000000.00', '500000.00', '800000.00', '3000000.00', '100000.00', '2000000.00'),
('E005', 'Bùi Như ý', 'staff', 'ATM999000777', '2021-09-01', '5.00', '4.00', '0.00', '1.00', 20, '3000000.00', '1.00', '300000.00', '2000000.00', '500000.00', '800000.00', '3000000.00', '100000.00', '1000000.00'),
('E008', 'Phạm Minh Chính', 'iron', 'ATM111999888', '2021-09-10', '5.00', '0.00', '3.00', '1.00', 22, '3000000.00', '1.00', '300000.00', '2000000.00', '500000.00', '800000.00', '3000000.00', '100000.00', '0.00'),
('E009', 'Trần Văn Minh', 'chiefAccountant', 'ATM6667778889', '2021-12-09', '6.00', '6.00', '1.00', '1.00', 22, '3000000.00', '1.00', '300000.00', '2000000.00', '500000.00', '800000.00', '3000000.00', '100000.00', '0.00'),
('E111', 'Nguyễn Quyết Tâm', 'worker', 'ATM8877999', '2021-09-10', '5.00', '0.00', '1.00', '1.00', 20, '3000000.00', '1.00', '300000.00', '2000000.00', '500000.00', '800000.00', '3000000.00', '100000.00', '0.00'),
('E112', 'Nguyễn Thành Trung', 'technicians', 'ATM1119998889', '2021-09-11', '5.00', '0.00', '4.00', '1.00', 20, '3000000.00', '1.00', '300000.00', '2000000.00', '500000.00', '800000.00', '3000000.00', '100000.00', '0.00'),
('E113', 'Phan Thành Nhân', 'wash', 'ATM1111999900', '2021-09-11', '5.00', '0.00', '2.00', '1.00', 22, '3000000.00', '1.00', '300000.00', '2000000.00', '500000.00', '800000.00', '3000000.00', '100000.00', '1000000.00'),
('E114', 'Nguyễn Văn B', 'wash', 'ATM123456788', '2021-09-12', '5.00', '0.00', '2.00', '1.00', 22, '3000000.00', '1.00', '300000.00', '2000000.00', '500000.00', '800000.00', '3000000.00', '100000.00', '0.00'),
('E115', 'Nguyễn Văn A', 'iron', 'ATM123456799', '2021-09-12', '7.00', '0.00', '3.00', '1.00', 22, '3000000.00', '1.00', '300000.00', '2000000.00', '500000.00', '800000.00', '3000000.00', '100000.00', '0.00'),
('E116', 'Minh Thành Thái', 'iron', 'ATM1234569990', '2021-09-14', '5.00', '0.00', '0.00', '1.00', 20, '3000000.00', '1.00', '300000.00', '2000000.00', '500000.00', '800000.00', '3000000.00', '100000.00', '0.00');

-- --------------------------------------------------------

--
-- Cấu trúc bảng cho bảng `users`
--

CREATE TABLE `users` (
  `email` varchar(225) NOT NULL,
  `password` varchar(20) DEFAULT NULL,
  `full_name` varchar(225) DEFAULT NULL,
  `link_image` varchar(225) DEFAULT NULL,
  `phone` varchar(50) DEFAULT NULL,
  `address` varchar(225) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Đang đổ dữ liệu cho bảng `users`
--

INSERT INTO `users` (`email`, `password`, `full_name`, `link_image`, `phone`, `address`) VALUES
('gialamq@gmail.com', '789', 'Quach Gia Lam', 'user.png', '0937378889', '31 Tổng Lung , P.13 , Q.11 ,Tp.HCM'),
('ijimal118@gmail.com', '123', 'ijimal118', 'boy.jpg', '0935097778', '590 CMT8,Quận 3,Tp.HCM'),
('newsa1@gmail.com', '123', 'Nguyen van A', 'boy.jpg', '09123123123', '999 Trần Hưng Đạo'),
('newsa2@gmail.com', '123', 'Nguyễn Văn B', 'user.png', '0901229998', '888 Nguyễn Huệ, Q1'),
('newsa3@gmail.com', '123', 'Vũ Thị C', 'boy.jpg', '0913385566', '200 Hoàng Văn Thụ'),
('sa1@gmail.com', '123', 'Nguyễn Triển Chiêu', 'user.png', '0908090119', '123 Lý Thái Tổ'),
('sa2@gmail.com', '123', 'Chiển chiêu', 'boy.jpg', '0912345536', '123 Nguyen Hue'),
('sa@gmail.com', '123', 'Ngoc Thach', 'user.png', '0913805538', '701 PVC'),
('sasa@gmail.com', '123', 'Tran Van Minh', 'boy.jpg', '0912348888', '999 CMT8'),
('thach@gmail.com', '123', 'Tran Van Thach', 'emma.jpg', '0908090119', '550 CMT8');

--
-- Chỉ mục cho các bảng đã đổ
--

--
-- Chỉ mục cho bảng `admin`
--
ALTER TABLE `admin`
  ADD PRIMARY KEY (`id`),
  ADD KEY `employee_id` (`employee_id`);

--
-- Chỉ mục cho bảng `orders`
--
ALTER TABLE `orders`
  ADD PRIMARY KEY (`id`),
  ADD KEY `user_email` (`user_email`),
  ADD KEY `shipper_id` (`shipper_id`);

--
-- Chỉ mục cho bảng `order_detail`
--
ALTER TABLE `order_detail`
  ADD PRIMARY KEY (`id`),
  ADD KEY `order_id` (`order_id`),
  ADD KEY `services_id` (`services_id`);

--
-- Chỉ mục cho bảng `services`
--
ALTER TABLE `services`
  ADD PRIMARY KEY (`id`);

--
-- Chỉ mục cho bảng `shipper`
--
ALTER TABLE `shipper`
  ADD PRIMARY KEY (`id`);

--
-- Chỉ mục cho bảng `tbpayslip`
--
ALTER TABLE `tbpayslip`
  ADD PRIMARY KEY (`id`);

--
-- Chỉ mục cho bảng `users`
--
ALTER TABLE `users`
  ADD PRIMARY KEY (`email`);

--
-- AUTO_INCREMENT cho các bảng đã đổ
--

--
-- AUTO_INCREMENT cho bảng `orders`
--
ALTER TABLE `orders`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=21;

--
-- AUTO_INCREMENT cho bảng `order_detail`
--
ALTER TABLE `order_detail`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=25;

--
-- AUTO_INCREMENT cho bảng `services`
--
ALTER TABLE `services`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=27;

--
-- Các ràng buộc cho các bảng đã đổ
--

--
-- Các ràng buộc cho bảng `admin`
--
ALTER TABLE `admin`
  ADD CONSTRAINT `admin_ibfk_1` FOREIGN KEY (`employee_id`) REFERENCES `tbpayslip` (`id`);

--
-- Các ràng buộc cho bảng `orders`
--
ALTER TABLE `orders`
  ADD CONSTRAINT `orders_ibfk_1` FOREIGN KEY (`user_email`) REFERENCES `users` (`email`),
  ADD CONSTRAINT `orders_ibfk_2` FOREIGN KEY (`shipper_id`) REFERENCES `shipper` (`id`);

--
-- Các ràng buộc cho bảng `order_detail`
--
ALTER TABLE `order_detail`
  ADD CONSTRAINT `order_detail_ibfk_1` FOREIGN KEY (`order_id`) REFERENCES `orders` (`id`),
  ADD CONSTRAINT `order_detail_ibfk_2` FOREIGN KEY (`services_id`) REFERENCES `services` (`id`);
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
