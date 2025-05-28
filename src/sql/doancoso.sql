CREATE DATABASE IF NOT EXISTS doancoso;
USE doancoso;
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

  `nghe_nghiep` varchar(100) DEFAULT NULL 

) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci; 

 

-- -------------------------------------------------------- 

 

-- 

-- Table structure for table `danhgia` 

-- 

 

CREATE TABLE `danhgia` ( 

  `ma_feedback` int(11) NOT NULL, 

  `ma_benh_nhan` varchar(50) DEFAULT NULL, 

  `rating` int(11) DEFAULT NULL, 

  `comment` text DEFAULT NULL, 

  `hailong` tinyint(1) DEFAULT NULL, 

  `lydo` text DEFAULT NULL, 

  `trainghiem` text DEFAULT NULL 

) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci; 

 

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

  `trang_thai` varchar(50) DEFAULT NULL 

) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci; 

 

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

('1', '1', 'nguyenvanA@gmail.com'); 

 

-- 

-- Indexes for dumped tables 

-- 

 

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

  ADD KEY `ma_benh_nhan` (`ma_benh_nhan`); 

 

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

-- AUTO_INCREMENT for dumped tables 

-- 

 

-- 

-- AUTO_INCREMENT for table `danhgia` 

-- 

ALTER TABLE `danhgia` 

  MODIFY `ma_feedback` int(11) NOT NULL AUTO_INCREMENT; 

 

-- 

-- AUTO_INCREMENT for table `ho_so_benh_an` 

-- 

ALTER TABLE `ho_so_benh_an` 

  MODIFY `ma_ho_so` int(11) NOT NULL AUTO_INCREMENT; 

 

-- 

-- AUTO_INCREMENT for table `lichhen` 

-- 

ALTER TABLE `lichhen` 

  MODIFY `ma_lich_hen` int(11) NOT NULL AUTO_INCREMENT; 

 

-- 

-- AUTO_INCREMENT for table `phieukham` 

-- 

ALTER TABLE `phieukham` 

  MODIFY `ma_phieu_kham` int(11) NOT NULL AUTO_INCREMENT; 

 

-- 

-- Constraints for dumped tables 

-- 

 

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

  ADD CONSTRAINT `lichhen_ibfk_1` FOREIGN KEY (`ma_benh_nhan`) REFERENCES `benhnhan` (`mabenhnhan`); 

 

-- 

-- Constraints for table `phieukham` 

-- 

ALTER TABLE `phieukham` 

  ADD CONSTRAINT `phieukham_ibfk_1` FOREIGN KEY (`ma_benh_nhan`) REFERENCES `benhnhan` (`mabenhnhan`); 

COMMIT; 

 