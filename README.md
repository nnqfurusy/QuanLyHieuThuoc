# Hệ Thống Quản Lý Hiệu Thuốc (Pharmacy Management System)

Dự án ứng dụng Java Desktop quản lý bán thuốc, tồn kho, thông tin khách hàng, nhân viên và báo cáo doanh thu dành cho các nhà thuốc tây. Ứng dụng được thiết kế giao diện trực quan bằng **Java Swing**, tích hợp xử lý dữ liệu qua CSDL quan hệ **MySQL** thông qua bộ công cụ **XAMPP**.

---

## 1. Giới thiệu chung

Dự án được phát triển nhằm tự động hóa quy trình quản lý nhà thuốc, giảm thiểu thao tác thủ công, tính toán chính xác và phân quyền sử dụng rõ ràng giữa **Quản lý** và **Nhân viên bán hàng**.

### Các tính năng chính:
- **Đăng nhập & Phân quyền**: Đăng nhập theo mã nhân viên với hai vai trò chính: Quản lý (Admin) và Nhân viên.
- **Quản lý Thuốc & Danh mục**:
  - Quản lý danh mục thuốc: Mã thuốc, tên thuốc, dạng bào chế, hạn sử dụng, số lượng tồn kho và giá bán.
  - Tra cứu / Tìm kiếm thông tin thuốc theo mã hoặc tên thuốc.
- **Quản lý Bán hàng & Đơn hàng**:
  - Tạo đơn hàng mới, liên kết thông tin khách hàng và nhân viên lập đơn.
  - Giỏ hàng tạm thời, tự động lấy giá thuốc từ CSDL.
  - Xuất và **In hóa đơn**
- **Quản lý Khách hàng & Nhân viên**:
  - Thêm, sửa, xóa, tìm kiếm thông tin khách hàng kèm ghi chú sức khỏe.
  - Quản lý danh sách nhân viên, chức vụ, lương, mật khẩu và số điện thoại.
- **Thống kê & Báo cáo**:
  - **Báo cáo tồn kho**: Cảnh báo các mặt hàng sắp hết (số lượng tồn $\le 10$).
  - **Báo cáo doanh thu**: Thống kê tổng số đơn hàng, tổng doanh thu theo ngày/khoảng thời gian.

## 2. Yêu cầu hệ thống

### Cấu hình phần cứng tối thiểu:
- **Hệ điều hành**: Windows 8.1 / 10 / 11 (64-bit), macOS, hoặc Linux.
- **Bộ vi xử lý (CPU)**: 1.5 GHz trở lên.
- **Bộ nhớ trong (RAM)**: Tối thiểu 2 GB (Khuyên dùng 4 GB trở lên).
- **Dung lượng ổ cứng trống**: Tối thiểu 1 GB.

### Phần mềm & Thư viện yêu cầu:
1. **Môi trường Java**: JDK 8 trở lên (Khuyên dùng JDK 11 hoặc JDK 17).
2. **Cơ sở dữ liệu & Server**: **XAMPP Control Panel** (Kích hoạt Module **MySQL Database Server**).
3. **Thư viện kết nối**: Thư viện MySQL Connector/J (`mysql-connector-j-9.7.0.jar`).
4. **Môi trường phát triển (IDE)**: Apache NetBeans, IntelliJ IDEA, hoặc Eclipse.

---

## 3. Hướng dẫn chạy chương trình

### Bước 1: Khởi động CSDL trên XAMPP
1. Mở ứng dụng **XAMPP Control Panel**.
2. Nhấn nút **Start** tại dòng **MySQL** và **Apache**.
<img width="494" height="306" alt="image" src="https://github.com/user-attachments/assets/27c3648e-e14b-470c-a65a-a205938b8aaa" />

4. Chọn chức năng Tạo mới cơ sở dữ liệu và đặt tên là **ql_nhathuoc**
<img width="734" height="137" alt="image" src="https://github.com/user-attachments/assets/886ddff2-ccd4-4675-8beb-dbc0143429d1" />

5. Chọn chức năng Nhập trong cơ sở dữ liệu **ql_nhathuoc**
<img width="952" height="251" alt="image" src="https://github.com/user-attachments/assets/5c91ee77-f7a7-4de2-a24c-a01255dbf8f6" />

6. Chọn tệp cơ sở dữ liệu và chọn Nhập
<img width="952" height="253" alt="image" src="https://github.com/user-attachments/assets/74cc7370-9782-493b-ba30-395b3c24d1f3" />


### Bước 2: Clone kho lưu trữ về máy
Mở Terminal / Command Prompt và chạy lệnh:
```bash
git clone https://github.com/nnqfurusy/QuanLyHieuThuoc.git
``` 
### Bước 3: Mở Project tại IDE
Vào NetBean và chọn Java Project with Existing Sources
<img width="541" height="371" alt="Screenshot 2026-07-25 204840" src="https://github.com/user-attachments/assets/f69272d7-ddac-4953-a3c6-1c672fcaef18" />

Lựa chọn đường dẫn dẫn tới thư mục vừa tải về
<img width="539" height="373" alt="Screenshot 2026-07-25 204915" src="https://github.com/user-attachments/assets/bbadd33c-e62a-4eef-b35a-9c6cb9ffe865" />

Chọn Source Package là thư mục src trong thư mục vừa tải về và chọn Finish
<img width="541" height="395" alt="image" src="https://github.com/user-attachments/assets/239dafc3-bb29-491d-a87b-b11d26ce0c54" />

Trong thư mục Libraries của Project, chọn Add thêm Connector để kết nối tới cơ sở dữ liệu

<img width="185" height="76" alt="image" src="https://github.com/user-attachments/assets/4fd9fdc8-e302-457e-b405-4bf74db84ea2" />

### Bước 4: Chạy project
Chạy Project với tài khoản Admin:
- Username: ADMIN01
- Password: admin123
