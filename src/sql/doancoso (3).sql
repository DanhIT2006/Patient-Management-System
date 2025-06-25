-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1
-- Generation Time: Jun 25, 2025 at 04:23 PM
-- Server version: 10.4.32-MariaDB
-- PHP Version: 8.2.12

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `doancoso`
--

-- --------------------------------------------------------

--
-- Table structure for table `bac_si`
--

CREATE TABLE `bac_si` (
  `mabacsi` varchar(10) NOT NULL,
  `hoten` varchar(100) DEFAULT NULL,
  `gioitinh` varchar(10) DEFAULT NULL,
  `ngaysinh` date DEFAULT NULL,
  `sdt` varchar(15) DEFAULT NULL,
  `diachi` varchar(255) DEFAULT NULL,
  `cccd` varchar(50) DEFAULT NULL,
  `nghe_nghiep` varchar(50) DEFAULT NULL,
  `khoa` varchar(50) DEFAULT NULL,
  `email` varchar(50) DEFAULT NULL,
  `trinh_do` varchar(50) DEFAULT NULL,
  `avatar` varchar(255) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `bac_si`
--

INSERT INTO `bac_si` (`mabacsi`, `hoten`, `gioitinh`, `ngaysinh`, `sdt`, `diachi`, `cccd`, `nghe_nghiep`, `khoa`, `email`, `trinh_do`, `avatar`) VALUES
('BS001', 'Tran Thi B', 'Nu', '1985-05-10', '2', 'HCM', '055026783513', 'Bác sĩ ngoại khoa', '', '', '', '0'),
('BS002', 'Trần Thị Bích', NULL, '2025-05-02', '0921205231', 'HCM', '0987654321', 'Thạc sĩ', 'Đa khoa', 'ttb12@gmail.com', 'Cao học', '');

-- --------------------------------------------------------

--
-- Table structure for table `benhnhan`
--

CREATE TABLE `benhnhan` (
  `mabenhnhan` varchar(50) NOT NULL,
  `hoten` varchar(100) NOT NULL,
  `gioitinh` varchar(10) DEFAULT NULL,
  `ngaysinh` date DEFAULT NULL,
  `sdt` varchar(20) DEFAULT NULL,
  `diachi` text DEFAULT NULL,
  `cccd` varchar(20) DEFAULT NULL,
  `ngayvaovien` date DEFAULT NULL,
  `ngayravien` date DEFAULT NULL,
  `nghe_nghiep` varchar(100) DEFAULT NULL,
  `avatar` varchar(255) DEFAULT NULL,
  `khoa` varchar(50) DEFAULT NULL,
  `benh` varchar(50) DEFAULT NULL,
  `yeu_cau` varchar(50) DEFAULT NULL,
  `thuoc` varchar(50) DEFAULT NULL,
  `vien_phi` varchar(50) DEFAULT NULL,
  `phong` varchar(50) DEFAULT NULL,
  `email` varchar(50) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `benhnhan`
--

INSERT INTO `benhnhan` (`mabenhnhan`, `hoten`, `gioitinh`, `ngaysinh`, `sdt`, `diachi`, `cccd`, `ngayvaovien`, `ngayravien`, `nghe_nghiep`, `avatar`, `khoa`, `benh`, `yeu_cau`, `thuoc`, `vien_phi`, `phong`, `email`) VALUES
('001234', 'Nguyễn Văn A', 'Nam', '1990-03-15', '1', '123 Đường Láng, Đống Đa, Hà Nội', '123456789012', '2025-05-20', '2025-05-22', 'Kỹ sư', 'src/Imgs/avatars/avatar_001234_1748368118871.jpg', '', NULL, NULL, NULL, NULL, NULL, NULL),
('BN002', 'Phan Thanh Khang', 'Nam', '2006-10-30', '3', 'Gia Lai', '055026783541', NULL, NULL, NULL, 'src/Imgs/avatars/avatar_BN002_1748422236803.jpg', '', NULL, NULL, NULL, NULL, NULL, NULL),
('BN006', 'Võ Thành Danh', 'Nam', '2025-05-05', '0921205231', 'Đắk Lắk', '066206011772', '2025-05-06', '2025-05-08', 'Sinh viên', '', 'Dạ dày', 'Đau ruột thừa', 'Tận tình', '', '1600000', '245', '10a5.thanhdanh206@gmail.com'),
('BN007', 'Phan Thanh Khang', 'Nam', '2006-10-30', '0987654321', 'Quảng Nam', '09876543212344', '2025-05-21', '2025-05-23', 'Sinh viên', 'src/Imgs/avatars/avatar_BN007_1748585429541.jpg', 'Thần kinh', 'Khùng', '', '', '8000000', '345', 'khangpt.24it@vku.udn.vn');

-- --------------------------------------------------------

--
-- Table structure for table `danhgia`
--

CREATE TABLE `danhgia` (
  `ma_feedback` int(11) NOT NULL,
  `ma_benh_nhan` varchar(50) DEFAULT NULL,
  `rating` int(11) DEFAULT NULL,
  `comment` text DEFAULT NULL,
  `hailong` enum('Có','Không') DEFAULT NULL,
  `lydo` text DEFAULT NULL,
  `trainghiem` text DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `danhgia`
--

INSERT INTO `danhgia` (`ma_feedback`, `ma_benh_nhan`, `rating`, `comment`, `hailong`, `lydo`, `trainghiem`) VALUES
(1, '001234', 5, 'cdcd', 'Có', 'dcdc', 'cđ'),
(2, '001234', 4, 'br', 'Có', 'dcdc', 'cđ'),
(3, '001234', 3, 'viettttt', 'Có', 'lý do', 'trai nghiem'),
(4, '001234', 1, 'phat beo', 'Có', 'bbb', 'vvv');

-- --------------------------------------------------------

--
-- Table structure for table `ho_so_benh_an`
--

CREATE TABLE `ho_so_benh_an` (
  `ma_ho_so` int(11) NOT NULL,
  `ma_benh_nhan` varchar(50) DEFAULT NULL,
  `ly_do_vao_vien` text DEFAULT NULL,
  `tom_tat_benh_ly` text DEFAULT NULL,
  `tien_su_benh` text DEFAULT NULL,
  `ket_qua_xet_nghiem` text DEFAULT NULL,
  `noi_khoa` varchar(100) DEFAULT NULL,
  `phau_thuat` varchar(100) DEFAULT NULL,
  `tinh_trang_ra_vien` varchar(100) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `ho_so_benh_an`
--

INSERT INTO `ho_so_benh_an` (`ma_ho_so`, `ma_benh_nhan`, `ly_do_vao_vien`, `tom_tat_benh_ly`, `tien_su_benh`, `ket_qua_xet_nghiem`, `noi_khoa`, `phau_thuat`, `tinh_trang_ra_vien`) VALUES
(2, '001234', 'Đau bụng cấp', 'Viêm ruột thừa', 'Không', 'CRP tăng, bạch cầu cao', 'Có', 'Chưa phẫu thuật', 'Đang điều trị'),
(4, 'BN002', 'Đau bụng', 'VIêm dạ dày', 'Tiểu đường', 'N/A', 'Có', 'Không', 'Đang điều trị');

-- --------------------------------------------------------

--
-- Table structure for table `lichhen`
--

CREATE TABLE `lichhen` (
  `ma_lich_hen` int(11) NOT NULL,
  `ma_benh_nhan` varchar(50) DEFAULT NULL,
  `ngay` date DEFAULT NULL,
  `ten_kham` varchar(100) DEFAULT NULL,
  `bac_si` varchar(100) DEFAULT NULL,
  `phong` varchar(50) DEFAULT NULL,
  `toa` varchar(50) DEFAULT NULL,
  `trang_thai` varchar(50) DEFAULT NULL,
  `ma_bac_si` varchar(10) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `lichhen`
--

INSERT INTO `lichhen` (`ma_lich_hen`, `ma_benh_nhan`, `ngay`, `ten_kham`, `bac_si`, `phong`, `toa`, `trang_thai`, `ma_bac_si`) VALUES
(1, '001234', '2025-05-23', 'Nội soi', 'Dr. Nguyễn Văn B', 'Phòng 101', 'A', 'Hoàn thành', NULL),
(2, '001234', '2025-05-24', 'Chụp X-Quang', 'Dr. Trần Thị C', 'Phòng 102', 'A', 'Đang chờ', NULL),
(3, '001234', '2025-05-25', 'Siêu âm', 'Dr. Lê Văn D', 'Phòng 103', 'A', 'Hủy', NULL),
(4, '001234', '2025-05-26', 'Xét nghiệm nước tiểu', 'Dr.Đỗ Văn Được', 'Phòng 104', 'B', 'Chưa khám', NULL),
(5, '001234', '2025-05-27', 'Xét nghiệm máu', 'Dr.Trần Công Đức', 'Phòng 105', 'B', 'Chưa khám', NULL),
(6, '001234', '2025-05-26', 'Đo điện tim', 'Dr.Trương Văn Lực', 'Phòng 106', 'B', 'Đã khám', NULL),
(7, 'BN007', '2025-05-25', 'Xét nghiệm máu', 'Tran Thi B', '103', 'A', 'Chưa khám', 'BS001'),
(8, 'BN007', '2025-12-30', 'bbb', 'Tran Thi B', '432', 'A', 'Chưa khám', 'BS001'),
(9, 'BN007', '2025-06-26', 'bbb', 'Tran Thi B', '111', 'A', 'Chưa khám', 'BS001'),
(10, 'BN007', '2025-06-12', 'ccccc', 'Tran Thi B', '311', 'A', 'Chưa khám', 'BS001');

-- --------------------------------------------------------

--
-- Table structure for table `lichkham`
--

CREATE TABLE `lichkham` (
  `ma_lich_kham` int(11) NOT NULL,
  `ngay` date DEFAULT NULL,
  `kham` varchar(50) DEFAULT NULL,
  `sdt` varchar(20) DEFAULT NULL,
  `benhnhan` varchar(50) DEFAULT NULL,
  `phong` varchar(25) DEFAULT NULL,
  `toa` varchar(10) DEFAULT NULL,
  `trangthai` varchar(30) DEFAULT NULL,
  `ma_benh_nhan` varchar(10) DEFAULT NULL,
  `ma_bac_si` varchar(10) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `lichkham`
--

INSERT INTO `lichkham` (`ma_lich_kham`, `ngay`, `kham`, `sdt`, `benhnhan`, `phong`, `toa`, `trangthai`, `ma_benh_nhan`, `ma_bac_si`) VALUES
(1, '2025-06-01', 'Chụp CT', NULL, 'Nguyễn Văn A', 'Phòng 105', 'A', 'Chưa khám', '001234', 'BS001'),
(2, '2025-06-12', 'ccccc', '', 'Phan Thanh Khang', '311', 'A', 'Chưa khám', 'BN007', 'BS001');

-- --------------------------------------------------------

--
-- Table structure for table `phieukham`
--

CREATE TABLE `phieukham` (
  `ma_phieu_kham` int(11) NOT NULL,
  `ma_benh_nhan` varchar(50) DEFAULT NULL,
  `ma_bac_si` varchar(50) DEFAULT NULL,
  `bac_si_phu_trach` varchar(100) DEFAULT NULL,
  `yeu_cau_kham` text DEFAULT NULL,
  `doi_tuong` varchar(50) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Table structure for table `tai_khoan`
--

CREATE TABLE `tai_khoan` (
  `sdt` varchar(20) NOT NULL,
  `matkhau` varchar(255) NOT NULL,
  `email` varchar(100) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `tai_khoan`
--

INSERT INTO `tai_khoan` (`sdt`, `matkhau`, `email`) VALUES
('0921205231', 'QrvrhH3', '10a5.thanhdanh206@gmail.com'),
('0987654321', '1', 'khangpt.24it@vku.udn.vn'),
('1', '2', 'nguyenvanA@gmail.com'),
('3', 'B7RrkyNh', 'kakaff2006@gmail.com');

-- --------------------------------------------------------

--
-- Table structure for table `tai_khoan_bpk`
--

CREATE TABLE `tai_khoan_bpk` (
  `sdt` varchar(30) NOT NULL,
  `matkhau` varchar(30) NOT NULL,
  `hoten` varchar(30) DEFAULT NULL,
  `gioitinh` varchar(30) DEFAULT NULL,
  `ngaysinh` date DEFAULT NULL,
  `diachi` varchar(30) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `tai_khoan_bpk`
--

INSERT INTO `tai_khoan_bpk` (`sdt`, `matkhau`, `hoten`, `gioitinh`, `ngaysinh`, `diachi`) VALUES
('1', '1', NULL, NULL, NULL, NULL);

-- --------------------------------------------------------

--
-- Table structure for table `tai_khoan_bs`
--

CREATE TABLE `tai_khoan_bs` (
  `sdt` varchar(50) NOT NULL,
  `matkhau` varchar(50) NOT NULL,
  `email` varchar(50) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `tai_khoan_bs`
--

INSERT INTO `tai_khoan_bs` (`sdt`, `matkhau`, `email`) VALUES
('0921205231', 'Danh@11', 'ttb12@gmail.com'),
('2', '2', 'ttt@gmail.com');

--
-- Indexes for dumped tables
--

--
-- Indexes for table `bac_si`
--
ALTER TABLE `bac_si`
  ADD PRIMARY KEY (`mabacsi`),
  ADD KEY `bac_si_ibfk_1` (`sdt`);

--
-- Indexes for table `benhnhan`
--
ALTER TABLE `benhnhan`
  ADD PRIMARY KEY (`mabenhnhan`),
  ADD KEY `sdt` (`sdt`);

--
-- Indexes for table `danhgia`
--
ALTER TABLE `danhgia`
  ADD PRIMARY KEY (`ma_feedback`),
  ADD KEY `ma_benh_nhan` (`ma_benh_nhan`);

--
-- Indexes for table `ho_so_benh_an`
--
ALTER TABLE `ho_so_benh_an`
  ADD PRIMARY KEY (`ma_ho_so`),
  ADD KEY `ma_benh_nhan` (`ma_benh_nhan`);

--
-- Indexes for table `lichhen`
--
ALTER TABLE `lichhen`
  ADD PRIMARY KEY (`ma_lich_hen`),
  ADD KEY `ma_benh_nhan` (`ma_benh_nhan`),
  ADD KEY `fk_ma_bac_si` (`ma_bac_si`);

--
-- Indexes for table `lichkham`
--
ALTER TABLE `lichkham`
  ADD PRIMARY KEY (`ma_lich_kham`),
  ADD KEY `fkk_ma_bac_si` (`ma_bac_si`),
  ADD KEY `fkk_ma_benh_nhan` (`ma_benh_nhan`);

--
-- Indexes for table `phieukham`
--
ALTER TABLE `phieukham`
  ADD PRIMARY KEY (`ma_phieu_kham`),
  ADD KEY `ma_benh_nhan` (`ma_benh_nhan`);

--
-- Indexes for table `tai_khoan`
--
ALTER TABLE `tai_khoan`
  ADD PRIMARY KEY (`sdt`);

--
-- Indexes for table `tai_khoan_bpk`
--
ALTER TABLE `tai_khoan_bpk`
  ADD PRIMARY KEY (`sdt`);

--
-- Indexes for table `tai_khoan_bs`
--
ALTER TABLE `tai_khoan_bs`
  ADD PRIMARY KEY (`sdt`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `danhgia`
--
ALTER TABLE `danhgia`
  MODIFY `ma_feedback` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=5;

--
-- AUTO_INCREMENT for table `ho_so_benh_an`
--
ALTER TABLE `ho_so_benh_an`
  MODIFY `ma_ho_so` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=5;

--
-- AUTO_INCREMENT for table `lichhen`
--
ALTER TABLE `lichhen`
  MODIFY `ma_lich_hen` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=11;

--
-- AUTO_INCREMENT for table `lichkham`
--
ALTER TABLE `lichkham`
  MODIFY `ma_lich_kham` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=3;

--
-- AUTO_INCREMENT for table `phieukham`
--
ALTER TABLE `phieukham`
  MODIFY `ma_phieu_kham` int(11) NOT NULL AUTO_INCREMENT;

--
-- Constraints for dumped tables
--

--
-- Constraints for table `bac_si`
--
ALTER TABLE `bac_si`
  ADD CONSTRAINT `bac_si_ibfk_1` FOREIGN KEY (`sdt`) REFERENCES `tai_khoan_bs` (`sdt`) ON DELETE CASCADE ON UPDATE CASCADE;

--
-- Constraints for table `benhnhan`
--
ALTER TABLE `benhnhan`
  ADD CONSTRAINT `benhnhan_ibfk_1` FOREIGN KEY (`sdt`) REFERENCES `tai_khoan` (`sdt`);

--
-- Constraints for table `danhgia`
--
ALTER TABLE `danhgia`
  ADD CONSTRAINT `danhgia_ibfk_1` FOREIGN KEY (`ma_benh_nhan`) REFERENCES `benhnhan` (`mabenhnhan`);

--
-- Constraints for table `ho_so_benh_an`
--
ALTER TABLE `ho_so_benh_an`
  ADD CONSTRAINT `ho_so_benh_an_ibfk_1` FOREIGN KEY (`ma_benh_nhan`) REFERENCES `benhnhan` (`mabenhnhan`);

--
-- Constraints for table `lichhen`
--
ALTER TABLE `lichhen`
  ADD CONSTRAINT `fk_ma_bac_si` FOREIGN KEY (`ma_bac_si`) REFERENCES `bac_si` (`mabacsi`),
  ADD CONSTRAINT `lichhen_ibfk_1` FOREIGN KEY (`ma_benh_nhan`) REFERENCES `benhnhan` (`mabenhnhan`);

--
-- Constraints for table `lichkham`
--
ALTER TABLE `lichkham`
  ADD CONSTRAINT `fkk_ma_bac_si` FOREIGN KEY (`ma_bac_si`) REFERENCES `bac_si` (`mabacsi`),
  ADD CONSTRAINT `fkk_ma_benh_nhan` FOREIGN KEY (`ma_benh_nhan`) REFERENCES `benhnhan` (`mabenhnhan`);

--
-- Constraints for table `phieukham`
--
ALTER TABLE `phieukham`
  ADD CONSTRAINT `phieukham_ibfk_1` FOREIGN KEY (`ma_benh_nhan`) REFERENCES `benhnhan` (`mabenhnhan`);
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
