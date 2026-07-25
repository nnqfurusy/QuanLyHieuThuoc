-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Máy chủ: 127.0.0.1
-- Thời gian đã tạo: Th7 24, 2026 lúc 08:04 AM
-- Phiên bản máy phục vụ: 10.4.32-MariaDB
-- Phiên bản PHP: 8.2.12

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Cơ sở dữ liệu: `ql_nhathuoc`
--

-- --------------------------------------------------------

--
-- Cấu trúc bảng cho bảng `don_hang`
--

CREATE TABLE `don_hang` (
  `MaDH` varchar(40) NOT NULL,
  `NgayLap` datetime DEFAULT NULL,
  `TrangThai` varchar(60) DEFAULT NULL,
  `TongTien` decimal(18,2) DEFAULT NULL,
  `MaNV` varchar(40) DEFAULT NULL,
  `MaKH` varchar(40) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

--
-- Đang đổ dữ liệu cho bảng `don_hang`
--

INSERT INTO `don_hang` (`MaDH`, `NgayLap`, `TrangThai`, `TongTien`, `MaNV`, `MaKH`) VALUES
('DH01', '2024-01-05 09:15:00', 'Hoàn thành', 150000.00, 'NV01', 'KH01'),
('DH02', '2024-01-08 10:20:00', 'Hoàn thành', 180000.00, 'NV02', 'KH02'),
('DH03', '2024-01-10 14:05:00', 'Đang xử lý', 230000.00, 'NV03', 'KH03'),
('DH04', '2024-01-12 16:40:00', 'Hoàn thành', 95000.00, 'NV04', 'KH04'),
('DH05', '2024-01-15 11:00:00', 'Hủy', 0.00, 'NV05', 'KH05'),
('DH06', '2024-01-20 13:20:00', 'Hoàn thành', 76000.00, 'NV01', 'KH06'),
('DH07', '2024-01-22 15:10:00', 'Hoàn thành', 188000.00, 'NV02', 'KH07'),
('DH08', '2024-01-25 10:45:00', 'Đang xử lý', 220000.00, 'NV03', 'KH08'),
('DH09', '2024-01-27 17:30:00', 'Hoàn thành', 99000.00, 'NV04', 'KH09'),
('DH10', '2024-02-01 08:50:00', 'Hoàn thành', 175000.00, 'NV05', 'KH10'),
('DH11', '2024-02-05 09:40:00', 'Hoàn thành', 125000.00, 'NV01', 'KH11'),
('DH12', '2024-02-10 14:00:00', 'Đang xử lý', 220000.00, 'NV02', 'KH12'),
('DH13', '2024-02-15 16:40:00', 'Hoàn thành', 99000.00, 'NV03', 'KH13'),
('DH14', '2024-02-18 13:30:00', 'Hoàn thành', 155000.00, 'NV04', 'KH14'),
('DH15', '2024-02-20 15:10:00', 'Hủy', 0.00, 'NV05', 'KH15'),
('DH16', '2024-02-25 11:25:00', 'Hoàn thành', 122000.00, 'NV01', 'KH16'),
('DH17', '2024-03-01 09:15:00', 'Hoàn thành', 88000.00, 'NV02', 'KH17'),
('DH18', '2024-03-05 14:30:00', 'Đang xử lý', 210000.00, 'NV03', 'KH18'),
('DH19', '2024-03-07 16:00:00', 'Hoàn thành', 135000.00, 'NV04', 'KH19'),
('DH20', '2024-03-10 10:20:00', 'Hoàn thành', 168000.00, 'NV05', 'KH20'),
('DH21', '2026-07-23 00:00:00', 'Chưa thanh toán', 0.00, 'NV01', 'KH01');

-- --------------------------------------------------------

--
-- Cấu trúc bảng cho bảng `don_hang_thuoc`
--

CREATE TABLE `don_hang_thuoc` (
  `MaDH` varchar(40) NOT NULL,
  `MaThuoc` varchar(40) NOT NULL,
  `SoLuong` int(11) DEFAULT NULL,
  `Gia` decimal(18,2) DEFAULT NULL,
  `LieuDung` varchar(100) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

--
-- Đang đổ dữ liệu cho bảng `don_hang_thuoc`
--

INSERT INTO `don_hang_thuoc` (`MaDH`, `MaThuoc`, `SoLuong`, `Gia`, `LieuDung`) VALUES
('DH01', 'T03', 2, 8500.00, 'Sau ăn'),
('DH01', 'T04', 1, 12000.00, 'Sau ăn'),
('DH01', 'T06', 1, 9000.00, 'Sau ăn'),
('DH02', 'T01', 1, 2500.00, ''),
('DH02', 'T02', 3, 4500.00, 'Sau ăn'),
('DH03', 'T03', 2, 8500.00, '2 lần/ngày'),
('DH04', 'T04', 1, 12000.00, 'Sáng 1 viên'),
('DH05', 'T05', 1, 2500.00, 'Bổ sung điện giải'),
('DH06', 'T06', 1, 9000.00, 'Sau ăn'),
('DH07', 'T07', 1, 3000.00, 'Khi tiêu chảy'),
('DH08', 'T08', 2, 18000.00, 'Ngày 1 lần'),
('DH09', 'T09', 1, 12000.00, 'Sau ăn'),
('DH10', 'T10', 1, 28000.00, 'Buổi sáng'),
('DH11', 'T11', 1, 30000.00, 'Khi dị ứng'),
('DH12', 'T12', 2, 6500.00, 'Khi cần'),
('DH13', 'T13', 1, 22000.00, 'Sát khuẩn'),
('DH14', 'T01', 4, 25000.00, 'Sau ăn'),
('DH14', 'T14', 2, 4000.00, 'Sáng – tối'),
('DH15', 'T15', 1, 35000.00, 'Ngày 1 lần'),
('DH16', 'T16', 1, 9000.00, 'Khi ho'),
('DH17', 'T17', 2, 18000.00, 'Sau ăn'),
('DH18', 'T18', 1, 8000.00, 'Tối uống'),
('DH19', 'T19', 1, 5500.00, 'Khi tiêu chảy'),
('DH20', 'T20', 1, 88000.00, '2 lần/ngày'),
('DH21', 'T01', 2, 2500.00, '2 lần/ngày'),
('DH21', 'T02', 2, 2500.00, 'Sau ăn');

-- --------------------------------------------------------

--
-- Cấu trúc bảng cho bảng `hoa_don`
--

CREATE TABLE `hoa_don` (
  `MaHD` varchar(40) NOT NULL,
  `NgayLap` datetime DEFAULT NULL,
  `HinhThucThanhToan` varchar(100) DEFAULT NULL,
  `TongTienThanhToan` decimal(18,2) DEFAULT NULL,
  `MaDH` varchar(40) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

--
-- Đang đổ dữ liệu cho bảng `hoa_don`
--

INSERT INTO `hoa_don` (`MaHD`, `NgayLap`, `HinhThucThanhToan`, `TongTienThanhToan`, `MaDH`) VALUES
('HD01', '2024-01-05 09:45:00', 'Tiền mặt', 150000.00, 'DH01'),
('HD02', '2024-01-08 10:45:00', 'Chuyển khoản', 180000.00, 'DH02'),
('HD03', '2024-01-10 14:30:00', 'Tiền mặt', 230000.00, 'DH03'),
('HD04', '2024-01-12 17:00:00', 'Tiền mặt', 95000.00, 'DH04'),
('HD05', '2024-01-15 11:30:00', 'Tiền mặt', 0.00, 'DH05'),
('HD06', '2024-01-20 13:40:00', 'Tiền mặt', 76000.00, 'DH06'),
('HD07', '2024-01-22 15:45:00', 'Chuyển khoản', 188000.00, 'DH07'),
('HD08', '2024-01-25 11:10:00', 'Tiền mặt', 220000.00, 'DH08'),
('HD09', '2024-01-27 17:50:00', 'Tiền mặt', 99000.00, 'DH09'),
('HD10', '2024-02-01 09:10:00', 'Chuyển khoản', 175000.00, 'DH10'),
('HD11', '2024-02-05 10:20:00', 'Tiền mặt', 125000.00, 'DH11'),
('HD12', '2024-02-10 15:00:00', 'Tiền mặt', 220000.00, 'DH12'),
('HD13', '2024-02-15 17:00:00', 'Tiền mặt', 99000.00, 'DH13'),
('HD14', '2024-02-18 14:00:00', 'Chuyển khoản', 155000.00, 'DH14'),
('HD15', '2024-02-20 15:40:00', 'Tiền mặt', 0.00, 'DH15'),
('HD16', '2024-02-25 11:45:00', 'Chuyển khoản', 122000.00, 'DH16'),
('HD17', '2024-03-01 09:40:00', 'Tiền mặt', 88000.00, 'DH17'),
('HD18', '2024-03-05 15:20:00', 'Chuyển khoản', 210000.00, 'DH18'),
('HD19', '2024-03-07 16:20:00', 'Tiền mặt', 135000.00, 'DH19'),
('HD20', '2024-03-10 10:40:00', 'Tiền mặt', 168000.00, 'DH20');

-- --------------------------------------------------------

--
-- Cấu trúc bảng cho bảng `khach_hang`
--

CREATE TABLE `khach_hang` (
  `MaKH` varchar(40) NOT NULL,
  `HoTen` varchar(100) DEFAULT NULL,
  `NgaySinh` date DEFAULT NULL,
  `GioiTinh` varchar(10) DEFAULT NULL,
  `DiaChi` varchar(200) DEFAULT NULL,
  `SDT` varchar(15) DEFAULT NULL,
  `GhiChuSucKhoe` text DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

--
-- Đang đổ dữ liệu cho bảng `khach_hang`
--

INSERT INTO `khach_hang` (`MaKH`, `HoTen`, `NgaySinh`, `GioiTinh`, `DiaChi`, `SDT`, `GhiChuSucKhoe`) VALUES
('KH01', 'Nguyễn Văn A', '1990-01-01', 'Nam', '12 Láng Hạ, Hà Nội', '0911000001', 'Huyết áp cao'),
('KH02', 'Trần Thị B', '1995-05-05', 'Nữ', '25 CMT8, TP.HCM', '0911000002', NULL),
('KH03', 'Lê Văn C', '1988-03-20', 'Nam', '8 Lạch Tray, Hải Phòng', '0911000003', 'Dị ứng thời tiết'),
('KH04', 'Hoàng Yến', '2000-06-15', 'Nữ', '55 Điện Biên Phủ, Đà Nẵng', '0911000004', NULL),
('KH05', 'Phạm Minh D', '1985-02-10', 'Nam', '10 Trần Hưng Đạo, Cần Thơ', '0911000005', NULL),
('KH06', 'Võ Nhật E', '1999-07-09', 'Nam', '35 Nguyễn Huệ, TP.HCM', '0911000006', 'Tiểu đường'),
('KH07', 'Đỗ Mai F', '1993-10-12', 'Nữ', '78 Trần Thái Tông, Hà Nội', '0911000007', NULL),
('KH08', 'Bùi Văn G', '1980-12-11', 'Nam', '23 Thanh Bình, Hải Dương', '0911000008', 'Đau dạ dày'),
('KH09', 'Lý Mỹ H', '1997-09-30', 'Nữ', '55 Nguyễn Chí Thanh, Hà Nội', '0911000009', NULL),
('KH10', 'Ngô Hồng I', '1992-04-01', 'Nữ', '90 CMT8, Quận 3, TP.HCM', '0911000010', NULL),
('KH11', 'Dương Văn J', '1987-08-08', 'Nam', '11 Võ Văn Kiệt, TP.HCM', '0911000011', NULL),
('KH12', 'Phan Mỹ K', '1991-02-17', 'Nữ', '22 Tây Sơn, Hà Nội', '0911000012', NULL),
('KH13', 'Huỳnh Văn L', '1986-11-22', 'Nam', '33 Đại Lộ Bình Dương', '0911000013', 'Gout'),
('KH14', 'Đinh Mỹ M', '1999-01-29', 'Nữ', '10 Nguyễn Văn Cừ, Nghệ An', '0911000014', NULL),
('KH15', 'Nguyễn Văn N', '1994-05-19', 'Nam', '12 Điện Biên Phủ, TP.HCM', '0911000015', NULL),
('KH16', 'Trịnh Thu O', '1990-08-07', 'Nữ', '155 QL1A, Long An', '0911000016', NULL),
('KH17', 'Tạ Quốc P', '1983-02-24', 'Nam', '86 Hoàng Quốc Việt, Hà Nội', '0911000017', 'Thấp khớp'),
('KH18', 'Phùng Mỹ Q', '1995-06-16', 'Nữ', '45 Lê Quang Định, TP.HCM', '0911000018', NULL),
('KH19', 'Hoàng Đình R', '1989-03-13', 'Nam', '19 Nguyễn Văn Đậu, TP.HCM', '0911000019', 'Mất ngủ'),
('KH20', 'Lâm Thu S', '2001-09-05', 'Nữ', '90 Trưng Nữ Vương, Đà Nẵng', '0911000020', NULL),
('KH21', 'Tạ Đình Thủy', '2001-09-05', 'Nam', '90 Trưng Nữ Vương, Đà Nẵng', '0911000022', '');

-- --------------------------------------------------------

--
-- Cấu trúc bảng cho bảng `nhan_vien`
--

CREATE TABLE `nhan_vien` (
  `MaNV` varchar(40) NOT NULL,
  `HoTen` varchar(100) DEFAULT NULL,
  `GioiTinh` varchar(10) DEFAULT NULL,
  `Luong` decimal(18,2) DEFAULT NULL,
  `MatKhau` varchar(255) DEFAULT NULL,
  `SDT` varchar(15) DEFAULT NULL,
  `VaiTro` varchar(30) NOT NULL DEFAULT 'NhanVien'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

--
-- Đang đổ dữ liệu cho bảng `nhan_vien`
--

INSERT INTO `nhan_vien` (`MaNV`, `HoTen`, `GioiTinh`, `Luong`, `MatKhau`, `SDT`, `VaiTro`) VALUES
('ADMIN01', 'Tài Khoản Quản Lý', 'Nam', 30000000.00, 'admin123', '0999999999', 'QuanLy'),
('NV01', 'Nguyễn Minh Quân', 'Nam', 15000000.00, 'pass1', '0903300001', 'NhanVien'),
('NV02', 'Lê Thị Mai', 'Nữ', 16000000.00, 'pass2', '0903300002', 'NhanVien'),
('NV03', 'Trần Cao Huy', 'Nam', 13500000.00, 'pass3', '0903300003', 'NhanVien'),
('NV04', 'Phạm Mỹ Linh', 'Nữ', 11000000.00, 'pass4', '0903300004', 'NhanVien'),
('NV05', 'Hoàng Thế Anh', 'Nam', 14000000.00, 'pass5', '0903300005', 'NhanVien'),
('NV06', 'Đỗ Hoài Phương', 'Nữ', 12500000.00, 'pass6', '0903300006', 'NhanVien'),
('NV07', 'Vũ Quốc Cường', 'Nam', 16000000.00, 'pass7', '0903300007', 'NhanVien'),
('NV08', 'Bùi Bích Phương', 'Nữ', 11000000.00, 'pass8', '0903300008', 'NhanVien'),
('NV09', 'Đặng Hoàng Nam', 'Nam', 18000000.00, 'pass9', '0903300009', 'NhanVien'),
('NV10', 'Ngô Hải Yến', 'Nữ', 16000000.00, 'pass10', '0903300010', 'NhanVien'),
('NV11', 'Lương Gia Bảo', 'Nam', 20000000.00, 'pass11', '0903300011', 'NhanVien'),
('NV12', 'Nguyễn Thảo Nhi', 'Nữ', 13000000.00, 'pass12', '0903300012', 'NhanVien'),
('NV13', 'Trịnh Quang Khải', 'Nam', 14500000.00, 'pass13', '0903300013', 'NhanVien'),
('NV14', 'Trần Thu Hà', 'Nữ', 10500000.00, 'pass14', '0903300014', 'NhanVien'),
('NV15', 'Ngô Văn Tài', 'Nam', 8500000.00, 'pass15', '0903300015', 'NhanVien'),
('NV16', 'Nguyễn Văn An', 'Nam', 15000000.00, '123456', '0988123456', 'NhanVien');

-- --------------------------------------------------------

--
-- Cấu trúc bảng cho bảng `nha_cung_cap`
--

CREATE TABLE `nha_cung_cap` (
  `MaNCC` varchar(40) NOT NULL,
  `TenNCC` varchar(100) DEFAULT NULL,
  `DiaChi` varchar(200) DEFAULT NULL,
  `SDT` varchar(15) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

--
-- Đang đổ dữ liệu cho bảng `nha_cung_cap`
--

INSERT INTO `nha_cung_cap` (`MaNCC`, `TenNCC`, `DiaChi`, `SDT`) VALUES
('NCC01', 'Dược Hồng Hà', '12 Láng Hạ, Ba Đình, Hà Nội', '0901111001'),
('NCC02', 'Dược Trung Ương 1', '18 Ngọc Khánh, Ba Đình, Hà Nội', '0901111002'),
('NCC03', 'Dược Thành Công', '25 Nguyễn Huệ, Quận 1, TP.HCM', '0901111003'),
('NCC04', 'Dược Việt Đức', '88 Cộng Hòa, Tân Bình, TP.HCM', '0901111004'),
('NCC05', 'Dược Hà Tây', '31 Quang Trung, Hà Đông, Hà Nội', '0901111005'),
('NCC06', 'Dược Hậu Giang', '55 Hùng Vương, TP. Vị Thanh, Hậu Giang', '0901111006'),
('NCC07', 'Dược Bình Định', '45 Trần Phú, Quy Nhơn, Bình Định', '0901111007'),
('NCC08', 'Mekophar', '297 Hồng Bàng, Quận 5, TP.HCM', '0901111008'),
('NCC09', 'Pymepharco', '02 Nguyễn Tất Thành, Tuy Hòa, Phú Yên', '0901111009'),
('NCC10', 'Imexpharm', '30 Võ Trường Toản, Cao Lãnh, Đồng Tháp', '0901111010'),
('NCC11', 'OPC Pharma', '1017 Hồng Bàng, Quận 6, TP.HCM', '0901111011'),
('NCC12', 'CPC1 Hà Nội', '356 Trường Chinh, Thanh Xuân, Hà Nội', '0901111012'),
('NCC13', 'Dược Nam Hà', '415 Trần Nhân Tông, TP. Nam Định', '0901111013'),
('NCC14', 'Danapha', '253 Duy Tân, Hải Châu, Đà Nẵng', '0901111014'),
('NCC15', 'Traphaco', '75 Yên Ninh, Ba Đình, Hà Nội', '0901111015'),
('NCC16', 'Dược SPM', '458 Kinh Dương Vương, Bình Tân, TP.HCM', '0901111016'),
('NCC17', 'STADA VN', '40 Đại Lộ Bình Dương, Thủ Dầu Một, Bình Dương', '0901111017'),
('NCC18', 'US Pharma USA', '118 Đặng Văn Bi, Thủ Đức, TP.HCM', '0901111018'),
('NCC19', 'Savipharm', 'Lô Z01-02 KCN Tân Thuận, Quận 7, TP.HCM', '0901111019'),
('NCC20', 'An Thiên Pharma', '314 Nguyễn Trọng Tuyển, Phú Nhuận, TP.HCM', '0901111020');

-- --------------------------------------------------------

--
-- Cấu trúc bảng cho bảng `nha_cung_cap_thuoc`
--

CREATE TABLE `nha_cung_cap_thuoc` (
  `MaNCC` varchar(40) NOT NULL,
  `MaThuoc` varchar(40) NOT NULL,
  `NgayCungCap` date NOT NULL,
  `SoLuong` int(11) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

--
-- Đang đổ dữ liệu cho bảng `nha_cung_cap_thuoc`
--

INSERT INTO `nha_cung_cap_thuoc` (`MaNCC`, `MaThuoc`, `NgayCungCap`, `SoLuong`) VALUES
('NCC01', 'T01', '2024-01-01', 300),
('NCC02', 'T02', '2024-01-02', 250),
('NCC03', 'T03', '2024-01-03', 200),
('NCC04', 'T04', '2024-01-04', 150),
('NCC05', 'T05', '2024-01-05', 500),
('NCC06', 'T06', '2024-01-06', 400),
('NCC07', 'T07', '2024-01-07', 450),
('NCC08', 'T08', '2024-01-08', 300),
('NCC09', 'T09', '2024-01-09', 350),
('NCC10', 'T10', '2024-01-10', 200),
('NCC11', 'T11', '2024-01-11', 150),
('NCC12', 'T12', '2024-01-12', 350),
('NCC13', 'T13', '2024-01-13', 120),
('NCC14', 'T14', '2024-01-14', 300),
('NCC15', 'T15', '2024-01-15', 260),
('NCC16', 'T16', '2024-01-16', 320),
('NCC17', 'T17', '2024-01-17', 210),
('NCC18', 'T18', '2024-01-18', 280),
('NCC19', 'T19', '2024-01-19', 400),
('NCC20', 'T20', '2024-01-20', 180);

-- --------------------------------------------------------

--
-- Cấu trúc bảng cho bảng `thuoc`
--

CREATE TABLE `thuoc` (
  `MaThuoc` varchar(40) NOT NULL,
  `TenThuoc` varchar(100) DEFAULT NULL,
  `DangBaoChe` varchar(100) DEFAULT NULL,
  `HanSuDung` date DEFAULT NULL,
  `TonKho` int(11) DEFAULT NULL,
  `GiaBan` decimal(18,2) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

--
-- Đang đổ dữ liệu cho bảng `thuoc`
--

INSERT INTO `thuoc` (`MaThuoc`, `TenThuoc`, `DangBaoChe`, `HanSuDung`, `TonKho`, `GiaBan`) VALUES
('T01', 'Paracetamol 500mg', 'Viên nén', '2026-12-31', 500, 2500.00),
('T02', 'Ibuprofen 400mg', 'Viên nén', '2027-05-20', 300, 4500.00),
('T03', 'Amoxicillin 500mg', 'Viên nang', '2026-09-15', 200, 8500.00),
('T04', 'Vitamin C 1000mg', 'Viên sủi', '2027-03-10', 150, 12000.00),
('T05', 'Oresol Bột Cân Bằng Nước', 'Gói pha', '2028-03-20', 999, 3000.00),
('T06', 'Cephalexin 500mg', 'Viên nang', '2026-11-01', 350, 9000.00),
('T07', 'Loperamide 2mg', 'Viên nén', '2028-12-30', 600, 3000.00),
('T08', 'Claritine 10mg', 'Viên nén', '2027-02-14', 150, 18000.00),
('T09', 'Panadol Extra', 'Viên nén', '2027-10-10', 420, 12000.00),
('T10', 'Berocca', 'Viên sủi', '2028-01-01', 100, 28000.00),
('T11', 'Telfast 180mg', 'Viên nén', '2026-07-01', 90, 30000.00),
('T12', 'Smecta', 'Gói', '2028-06-14', 400, 6500.00),
('T13', 'Betadine', 'Dung dịch', '2027-08-20', 50, 22000.00),
('T14', 'Cotrimoxazole', 'Viên nén', '2026-04-01', 230, 4000.00),
('T15', 'ZinC Kid', 'Siro', '2027-12-01', 140, 35000.00),
('T16', 'Acemuc', 'Gói', '2028-02-02', 300, 9000.00),
('T17', 'Cefixim 100mg', 'Viên nang', '2026-03-18', 210, 18000.00),
('T18', 'Agimacron', 'Viên nén', '2027-12-12', 310, 8000.00),
('T19', 'Kolokit', 'Gói', '2028-08-01', 480, 5500.00),
('T20', 'Prospan', 'Siro', '2026-11-22', 95, 88000.00),
('T21', 'Vitamin chin chít', 'Viên nén', '2027-01-01', 0, 25000.00);

--
-- Chỉ mục cho các bảng đã đổ
--

--
-- Chỉ mục cho bảng `don_hang`
--
ALTER TABLE `don_hang`
  ADD PRIMARY KEY (`MaDH`),
  ADD KEY `MaNV` (`MaNV`),
  ADD KEY `MaKH` (`MaKH`);

--
-- Chỉ mục cho bảng `don_hang_thuoc`
--
ALTER TABLE `don_hang_thuoc`
  ADD PRIMARY KEY (`MaDH`,`MaThuoc`),
  ADD KEY `MaThuoc` (`MaThuoc`);

--
-- Chỉ mục cho bảng `hoa_don`
--
ALTER TABLE `hoa_don`
  ADD PRIMARY KEY (`MaHD`),
  ADD UNIQUE KEY `MaDH` (`MaDH`);

--
-- Chỉ mục cho bảng `khach_hang`
--
ALTER TABLE `khach_hang`
  ADD PRIMARY KEY (`MaKH`);

--
-- Chỉ mục cho bảng `nhan_vien`
--
ALTER TABLE `nhan_vien`
  ADD PRIMARY KEY (`MaNV`),
  ADD UNIQUE KEY `SDT` (`SDT`);

--
-- Chỉ mục cho bảng `nha_cung_cap`
--
ALTER TABLE `nha_cung_cap`
  ADD PRIMARY KEY (`MaNCC`);

--
-- Chỉ mục cho bảng `nha_cung_cap_thuoc`
--
ALTER TABLE `nha_cung_cap_thuoc`
  ADD PRIMARY KEY (`MaNCC`,`MaThuoc`,`NgayCungCap`),
  ADD KEY `MaThuoc` (`MaThuoc`);

--
-- Chỉ mục cho bảng `thuoc`
--
ALTER TABLE `thuoc`
  ADD PRIMARY KEY (`MaThuoc`);

--
-- Các ràng buộc cho các bảng đã đổ
--

--
-- Các ràng buộc cho bảng `don_hang`
--
ALTER TABLE `don_hang`
  ADD CONSTRAINT `don_hang_ibfk_1` FOREIGN KEY (`MaNV`) REFERENCES `nhan_vien` (`MaNV`),
  ADD CONSTRAINT `don_hang_ibfk_2` FOREIGN KEY (`MaKH`) REFERENCES `khach_hang` (`MaKH`);

--
-- Các ràng buộc cho bảng `don_hang_thuoc`
--
ALTER TABLE `don_hang_thuoc`
  ADD CONSTRAINT `don_hang_thuoc_ibfk_1` FOREIGN KEY (`MaDH`) REFERENCES `don_hang` (`MaDH`),
  ADD CONSTRAINT `don_hang_thuoc_ibfk_2` FOREIGN KEY (`MaThuoc`) REFERENCES `thuoc` (`MaThuoc`);

--
-- Các ràng buộc cho bảng `hoa_don`
--
ALTER TABLE `hoa_don`
  ADD CONSTRAINT `hoa_don_ibfk_1` FOREIGN KEY (`MaDH`) REFERENCES `don_hang` (`MaDH`);

--
-- Các ràng buộc cho bảng `nha_cung_cap_thuoc`
--
ALTER TABLE `nha_cung_cap_thuoc`
  ADD CONSTRAINT `nha_cung_cap_thuoc_ibfk_1` FOREIGN KEY (`MaNCC`) REFERENCES `nha_cung_cap` (`MaNCC`),
  ADD CONSTRAINT `nha_cung_cap_thuoc_ibfk_2` FOREIGN KEY (`MaThuoc`) REFERENCES `thuoc` (`MaThuoc`);
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
