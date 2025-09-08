# 📱 WebsiteTechWorld – Nền tảng bán điện thoại trực tuyến

WebsiteTechWorld là hệ thống thương mại điện tử chuyên cung cấp các sản phẩm điện thoại thông minh, được xây dựng với kiến trúc hiện đại gồm **Backend (Spring Boot)** và **Frontend (Vue.js)**. Hệ thống hỗ trợ ba vai trò chính: **Admin**, **Nhân viên**, và **Khách hàng**, với đầy đủ chức năng quản lý sản phẩm, đơn hàng, người dùng và trải nghiệm mua sắm trực tuyến.

---

## 📑 Mục lục

- [🎯 Tính năng](#tính-năng)
- [🏗️ Kiến trúc hệ thống](#kiến-trúc-hệ-thống)
- [⚙️ Cài đặt và chạy dự án](#cài-đặt-và-chạy-dự-án)
- [🛠️ Công nghệ sử dụng](#công-nghệ-sử-dụng)
- [📁 Cấu trúc thư mục](#cấu-trúc-thư-mục)
- [👥 Thành viên nhóm](#thành-viên-nhóm)

---

## 🎯 Tính năng

### 👨‍💼 Admin
- Quản lý danh mục và sản phẩm
- Quản lý tài khoản người dùng và phân quyền
- Theo dõi và xử lý đơn hàng
- Thống kê doanh thu, sản phẩm bán chạy

### 👷 Nhân viên
- Xử lý đơn hàng và cập nhật trạng thái
- Quản lý kho hàng và tồn kho
- Hỗ trợ khách hàng qua hệ thống chat hoặc email

### 🧑 Khách hàng
- Đăng ký, đăng nhập và quản lý tài khoản cá nhân
- Duyệt và tìm kiếm sản phẩm
- Thêm sản phẩm vào giỏ hàng, thanh toán trực tuyến
- Theo dõi trạng thái đơn hàng và lịch sử mua hàng

---

## 🏗️ Kiến trúc hệ thống

```plaintext
Client (Vue.js) ↔ RESTful API (Spring Boot) ↔ Database (SQL Server)
