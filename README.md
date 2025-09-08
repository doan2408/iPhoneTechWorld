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

## 👥 Vai trò và nhiệm vụ từng thành viên

Dự án WebsiteTechWorld được triển khai bởi 5 thành viên, mỗi người đảm nhận một phần công việc cụ thể nhằm đảm bảo tiến độ và chất lượng sản phẩm.

---

### 🔹 Phí Duy Mạnh – Leader  
- Phụ trách bán hàng online và tại quầy  
- Xử lý giao hàng, trả hàng, bảo hành  
- Quản lý đơn hàng, tra cứu đơn hàng  
- Soạn sản phẩm, thực hiện thanh toán  
- **Lưu ý:** Là người duy nhất có quyền **pull request** và **merge vào nhánh `main`**

---

### 🔹 Nguyễn Bá Doãn   
- Xây dựng chức năng đăng ký, đăng nhập  
- Tích hợp quét mã, quẹt mã khẩu  
- Quản lý nhân viên và khách hàng  
- Phân hạng người dùng và hệ thống tích điểm  
- Thiết kế trang chủ và chức năng bảo hành (CRUD)

---

### 🔹 Nguyễn Đức Cường  
- Quản lý sản phẩm và kho hàng  
- Xử lý hóa đơn và chương trình khuyến mãi  
- Phân quyền người dùng và phân công công việc  
- Lọc và phân tích thông tin khách hàng

---

### 🔹 Lê Chí Nguyên  
- Triển khai luồng quản lý phiếu giảm giá  
- Quản lý khuyến mãi sản phẩm  
- Tích hợp chức năng live stream  
- Xây dựng tính năng yêu thích sản phẩm

---

### 🔹 Bùi Thị Minh  
- Thống kê dữ liệu người dùng  
- Quản lý thông tin khách hàng phía frontend  
- Xây dựng trang "My Account", đơn hàng và yêu thích

---

## 🏗️ Kiến trúc hệ thống

```plaintext
Client (Vue.js) ↔ RESTful API (Spring Boot) ↔ Database (SQL Server)
