create database TechWorld
go
use TechWorld
go

create table nhan_vien (
id_nhan_vien int identity(1,1) primary key,
ma_nhan_vien as (
'NV' + case 
		   when id_nhan_vien <10 then '00' + cast (id_nhan_vien as varchar)
		   when id_nhan_vien <100 then '0' + cast (id_nhan_vien as varchar)
		   else cast (id_nhan_vien as varchar)
		   end
		   ) persisted,
ten_nhan_vien nvarchar(50),
tai_khoan varchar(50),
mat_khau VARCHAR(255), --password123, password456
email varchar(100),
sdt varchar(10),
dia_chi nvarchar(100),
trang_thai nvarchar(50), -- ENABLE , DISABLE
chuc_vu nvarchar(50), --STAFF, ADMIN
gioi_tinh bit, --0: nữ, 1: nam
nam_sinh date
)

create table khach_hang (
id_khach_hang int identity(1,1) primary key,
ma_khach_hang as (
'KH' + case 
		   when id_khach_hang <10 then '00' + cast (id_khach_hang as varchar)
		   when id_khach_hang <100 then '0' + cast (id_khach_hang as varchar)
		   else cast (id_khach_hang as varchar)
		   end
		   ) persisted,
ten_khach_hang nvarchar(50),
sdt varchar(10),
tai_khoan nvarchar(50),
mat_khau VARCHAR(255);,
email nvarchar(100),
ngay_sinh date,
gioi_tinh bit,  --0: nữ, 1: nam
anh nvarchar(200),
tong_diem decimal(10,2), --1.1 hàng năm set lại hạng 1 lần (hạng, tổng điểm, điểm hiện tại)
so_diem_hien_tai decimal(10,2), --1.1 hàng năm set lại hạng 1 lần (hạng, tổng điểm, điểm hiện tại)
hang_khach_hang nvarchar(50),-- MEMBER, SILVER, GOLD, DIAMOND,  1.1 hàng năm set lại hạng 1 lần (hạng, tổng điểm, điểm hiện tại)
trang_thai nvarchar(50) --ACTIVE, INACTIVE
)

create table phieu_giam_gia (
id_phieu_giam_gia int identity(1,1) primary key,
ma_giam_gia as  (
'VC' + case 
		   when id_phieu_giam_gia <10 then '00' + cast (id_phieu_giam_gia as varchar)
		   when id_phieu_giam_gia <100 then '0' + cast (id_phieu_giam_gia as varchar)
		   else cast (id_phieu_giam_gia as varchar)
		   end
		   ) persisted,
ten_khuyen_mai nvarchar(50),
loai_khuyen_mai nvarchar(50),
gia_tri_khuyen_mai decimal(10,2),
gia_tri_don_hang_toi_thieu decimal(10,2),
gia_tri_khuyen_mai_toi_da decimal(10,2),
ngay_bat_dau date,
ngay_ket_thuc date,
dieu_kien_ap_dung nvarchar(100),-- mô tả
hang_toi_thieu nvarchar(50),
so_luong int,
so_diem_can_de_doi decimal(10,2),
is_global bit,-- 0: gioi han, 1:  tat ca deu co the su dung
trang_thai nvarchar(50)  --NOT STARTED, ACTIVE, EXPIRED
)

create table hoa_don (
id_hoa_don int identity(1,1) primary key,
ma_hoa_don as (
'HD' + case 
		   when id_hoa_don <10 then '00' + cast (id_hoa_don as varchar)
		   when id_hoa_don <100 then '0' + cast (id_hoa_don as varchar)
		   else cast (id_hoa_don as varchar)
		   end
		   ) persisted,
id_khach_hang int references khach_hang(id_khach_hang),
id_phieu_giam_gia int references phieu_giam_gia(id_phieu_giam_gia),
ten_nguoi_nhan nvarchar(50),
dia_chi nvarchar(100),
sdt nvarchar(10),
phi_ship decimal(10,2),
tong_tien decimal(10,2),
so_tien_giam decimal(10,2),
thanh_tien decimal(10,2),
ngay_tao date,
loai_hoa_don nvarchar(50), -- online, point of sale (POS)
ngay_thanh_toan date,
trang_thai_thanh_toan nvarchar(50) -- PENDING, PAID, CANCELLED,REFUNDED,COMPLETED
)

create table lich_su_hoa_don (
id_lich_su_hoa_don int identity(1,1) primary key,
id_nhan_vien int,
id_hoa_don int,
hanh_dong nvarchar(100),
thoi_gian_thay_doi date,
mo_ta nvarchar(100),
foreign key (id_nhan_vien) references nhan_vien(id_nhan_vien),
foreign key (id_hoa_don) references hoa_don(id_hoa_don) ON DELETE CASCADE,
)


create table khach_hang_giam_gia (
	id_khach_hang_giam_gia int identity(1,1) primary key ,
	id_khach_hang int references khach_hang(id_khach_hang) ON DELETE CASCADE,
	id_phieu_giam_gia int references phieu_giam_gia(id_phieu_giam_gia) ON DELETE CASCADE,
	is_user bit, --0: chưa dùng, 1: đã dùng
	ngay_cap date
)

create table dia_chi (
id_dia_chi int identity(1,1) primary key,
id_khach_hang int,
ten_nguoi_nhan nvarchar(50),
so_nha nvarchar(50),
ten_duong nvarchar(50),
xa_phuong nvarchar(50),
quan_huyen nvarchar(50),
tinh_thanh_pho nvarchar(50),
dia_chi_chinh bit, --1: địa chỉ chính, 0: các địa chỉ phụ
foreign key (id_khach_hang) references khach_hang(id_khach_hang) ON DELETE CASCADE
)

CREATE TABLE gio_hang (
    id_gio_hang INT IDENTITY(1,1) PRIMARY KEY,
    id_khach_hang INT UNIQUE, -- mỗi khách chỉ có 1 giỏ
    FOREIGN KEY (id_khach_hang) REFERENCES khach_hang(id_khach_hang) ON DELETE CASCADE
);



create table camera_sau ( 
id_camera_sau int identity (1,1) primary key,
loai_camera nvarchar(100),
do_phan_giai nvarchar (50),
khau_do nvarchar(50),
loai_zoom nvarchar(50),
che_do_chup nvarchar(50)
)

create table camera_truoc ( 
id_camera_truoc int identity (1,1) primary key,
loai_camera nvarchar(100),
do_phan_giai nvarchar (50),
khau_do nvarchar(50),
loai_zoom nvarchar(50),
che_do_chup nvarchar(50)
)

create table cpu (
id_cpu int identity(1,1) primary key,
hang_san_xuat nvarchar(50),
so_nhan nvarchar(50),
so_luong int,
xung_nhip nvarchar(50),
cong_nghe_san_xuat nvarchar(50),
bo_nho_dem nvarchar (50),
tieu_thu_dien_nang nvarchar(50),
nam_ra_mat date
)

create table loai (
id_loai int identity(1,1) primary key,
ten_loai nvarchar(50) --thuong, plus, pro, promax
)

create table xuat_xu (
id_xuat_xu int identity(1,1) primary key,
ma_xuat_xu NVARCHAR(10), -- VN/A LL/A
ten_quoc_gia nvarchar(50)
)


create table pin (
id_pin int identity(1,1) primary key,
phien_ban nvarchar(50),
cong_suat_sac nvarchar(50),
thoi_gian_su_dung nvarchar(50),
so_lan_sac_toi_da nvarchar(50)
)


create table he_dieu_hanh (
id_he_dieu_hanh int identity(1,1) primary key,
phien_ban nvarchar(50),
nha_phat_trien nvarchar(50),
giao_dien_nguoi_dung nvarchar(50)
)

create table man_hinh (
id_man_hinh int identity(1,1) primary key,
ten_man_hinh nvarchar(50),
kich_thuoc nvarchar(50),
loai_man_hinh nvarchar(50),
do_phan_giai nvarchar(50),
tan_so_quet nvarchar(50), --60hz, 120hz
do_sang nvarchar(50), --nit
chat_lieu_kinh nvarchar(50)
)


create table rom(
id_rom int identity(1,1) primary key,
dung_luong_rom nvarchar(50),
loai_rom nvarchar(50),
toc_do_doc_ghi nvarchar(50),
nha_san_xuat nvarchar(50),
nam_ra_mat date
)


create table ram (
id_ram int identity(1,1) primary key,
dung_luong_ram nvarchar(50),
loai_ram nvarchar(50),
toc_do_doc_ghi nvarchar(50),
nha_san_xuat nvarchar(50),
nam_ra_mat date
)


create table mau_sac (
id_mau_sac int identity(1,1) primary key,
ten_mau nvarchar(50)
)


create table nha_cung_cap ( 
id_nha_cung_cap int identity(1,1) primary key,
ten_nha_cung_cap nvarchar(50),
dia_chi nvarchar(50),
sdt varchar(10),
email varchar(50)
)

create table san_pham ( 
id_san_pham int identity(1,1) primary key,
ma_san_pham as (
'SP' + case
		when id_san_pham <10 then '00' + cast (id_san_pham as varchar)
		   when id_san_pham <100 then '0' + cast (id_san_pham as varchar)
		   else cast (id_san_pham as varchar)
		   end
		   ) persisted,
ten_san_pham nvarchar(50),
thuong_hieu nvarchar(50),
so_luong_ton_kho int,
id_nha_cung_cap int references nha_cung_cap(id_nha_cung_cap)
)


create table san_pham_chi_tiet ( 
	id_san_pham_chi_tiet int identity(1,1) primary key,
	ma_san_pham_chi_tiet as (
		  'SPCT' + case
		   when id_san_pham_chi_tiet <10 then '00' + cast (id_san_pham_chi_tiet as varchar)
		   when id_san_pham_chi_tiet <100 then '0' + cast (id_san_pham_chi_tiet as varchar)
		   else cast (id_san_pham_chi_tiet as varchar)
		   end
		   ) persisted,
	id_san_pham int references san_pham(id_san_pham) ON DELETE CASCADE,
	id_mau int references mau_sac(id_mau_sac),
	id_ram int references ram(id_ram),
	id_rom int references rom(id_rom),
	id_man_hinh int references man_hinh(id_man_hinh),
	id_he_dieu_hanh int references he_dieu_hanh(id_he_dieu_hanh),
	id_pin int references pin(id_pin),
	id_cpu int references cpu(id_cpu),
	id_camera_truoc int references camera_truoc(id_camera_truoc),
	id_camera_sau int references camera_sau(id_camera_sau),
	id_xuat_xu int references xuat_xu(id_xuat_xu),
	id_loai int references loai(id_loai),
	so_luong int,
	gia_ban decimal(10,2)
)

create table loai_bao_hanh (
    id_loai_bao_hanh int identity primary key, -- tat ca loai bao hanh deu mien phi
    ten_loai_bao_hanh nvarchar(100),
	thoi_gian_thang INT,   
    mo_ta NVARCHAR(255)
);
create table bao_hanh (
	id_bao_hanh int identity(1,1) primary key,
	id_khach_hang int,
	id_san_pham_chi_tiet int references san_pham_chi_tiet(id_san_pham_chi_tiet),
	ngay_bat_dau date,
	ngay_ket_thuc date,
	id_loai_bao_hanh int references loai_bao_hanh(id_loai_bao_hanh),	
	trang_thai_bao_hanh nvarchar(50) -- UNDER WARRANTY (Đang bảo hành),  WARRANTY EXPIRED  (Hết bảo hành),  , WARRANTY VOID: Hết hiệu lực bảo hành. do khách hàng sửa chữa gây hư hại trước khi mang đến bảo hành
);

CREATE TABLE lich_su_bao_hanh (
    id INT IDENTITY(1,1) PRIMARY KEY,
    id_san_pham_bao_hanh INT REFERENCES bao_hanh(id_bao_hanh) ON DELETE CASCADE,
    ngay_tiep_nhan DATE,
    ngay_hoan_thanh DATE,
    mo_ta_loi NVARCHAR(255),
    trang_thai NVARCHAR(50) -- IN REPAIR, REPAIRED, WARRANTY VOID, ...
);

create table imei (
id_imei int identity(1,1) primary key,
id_san_pham_chi_tiet int references san_pham_chi_tiet(id_san_pham_chi_tiet) ON DELETE CASCADE,
so_imei varchar(70),
trang_thai_imei nvarchar(50),  -- AVAILABLE (Có sẵn) , RESERVED (Đã đặt trước), SOLD (Đã bán),RETURNED (Đã trả lại), REFURBISHED (Tân trang),  BLACKLISTED ( Đã bị khóa )
nha_mang nvarchar(50)
)

create table hinh_anh (
id_hinh_anh int identity(1,1) primary key,
id_san_pham_chi_tiet int references san_pham_chi_tiet(id_san_pham_chi_tiet) ON DELETE CASCADE,
url varchar(100)
)


create table gio_hang_chi_tiet (
	id_gio_hang_chi_tiet int identity(1,1) primary key,
	id_gio_hang int,
	id_san_pham_chi_tiet int,
	so_luong int,
	gia decimal(10,2),
	ngay_them DATE DEFAULT CAST(GETDATE() AS DATE),
	foreign key (id_gio_hang) references gio_hang(id_gio_hang) ON DELETE CASCADE,
	foreign key (id_san_pham_chi_tiet) references san_pham_chi_tiet(id_san_pham_chi_tiet) 
)

create table chi_tiet_hoa_don (
	id_chi_tiet_hoa_don int identity(1,1) primary key,
	id_hoa_don int references hoa_don(id_hoa_don) ON DELETE CASCADE,
	id_san_pham_chi_tiet int references san_pham_chi_tiet(id_san_pham_chi_tiet),
	ma_chi_tiet_hoa_don as (
	'CTHD' + case 
			   when id_chi_tiet_hoa_don <10 then '00' + cast (id_chi_tiet_hoa_don as varchar)
			   when id_chi_tiet_hoa_don <100 then '0' + cast (id_chi_tiet_hoa_don as varchar)
			   else cast (id_chi_tiet_hoa_don as varchar)
			   end
			   ) persisted,
	ten_san_pham nvarchar(50),
	mo_ta nvarchar(100),
	so_luong int,
	don_gia decimal(10,2)
)

create table imei_da_ban (
	id_imei_da_ban int identity(1,1) primary key,
	id_hoa_don_chi_tiet int references chi_tiet_hoa_don(id_chi_tiet_hoa_don) ON DELETE CASCADE,
	so_imei varchar(70),
	trang_thai nvarchar(50)	    -- SOLD
)


create table giao_hang (
id_giao_hang int identity(1,1) primary key,
id_khach_hang int references khach_hang(id_khach_hang),
id_hoa_don int references hoa_don(id_hoa_don) ON DELETE CASCADE,
ma_giao_hang as (
'GH' + case 
		   when id_giao_hang <10 then '00' + cast (id_giao_hang as varchar)
		   when id_giao_hang <100 then '0' + cast (id_giao_hang as varchar)
		   else cast (id_giao_hang as varchar)
		   end
		   ) persisted,
ngay_dat_hang date,
tong_gia_tri_don_hang decimal(10,2),
dia_chi_giao_hang nvarchar(50),
trang_thai_don_hang nvarchar(50) -- PENDING, PACKED, SHIPPING, DELIVERED,FAILED,RETURNED	
)


create table chi_tiet_giao_hang (
id_chi_tiet_giao_hang int identity(1,1) primary key,
id_giao_hang int references giao_hang(id_giao_hang) ON DELETE CASCADE,
id_san_pham_chi_tiet int references san_pham_chi_tiet(id_san_pham_chi_tiet),
ma_chi_tiet_giao_hang as (
'CTGH' + case 
		   when id_chi_tiet_giao_hang <10 then '00' + cast (id_chi_tiet_giao_hang as varchar)
		   when id_chi_tiet_giao_hang <100 then '0' + cast (id_chi_tiet_giao_hang as varchar)
		   else cast (id_chi_tiet_giao_hang as varchar)
		   end
		   ) persisted,
so_luong int,
don_gia decimal(10,2)
)

create table phuong_thuc_thanh_toan (
id_phuong_thuc_thanh_toan int identity (1,1) primary key,
ten_phuong_thuc nvarchar(50),
loai_hinh_thuc nvarchar(50)
)

create table chi_tiet_thanh_toan (
id_chi_tiet_thanh_toan int identity(1,1) primary key,
id_hoa_don int references hoa_don(id_hoa_don) ON DELETE CASCADE,
id_phuong_thuc_thanh_toan int references phuong_thuc_thanh_toan(id_phuong_thuc_thanh_toan),
so_tien_thanh_toan decimal(10,2)
)



-- Bảng nhan_vien
INSERT INTO nhan_vien (ten_nhan_vien, tai_khoan, mat_khau, email, sdt, dia_chi, trang_thai, chuc_vu, gioi_tinh, nam_sinh)
VALUES 
    (N'Nguyễn Văn An', 'nv_an', '$2a$10$OprA7imS.gORyxtQ3KkfqOLuEXK2.NjHcTRRFpPfnScJQpaokqSFC', 'an.nv@example.com', '0901234567', N'123 Đường Láng, Hà Nội', N'ENABLE', N'ADMIN', 1, '1990-05-15'),
    (N'Trần Thị Bình', 'nv_binh', '$2a$10$G9o3ggyK9QDDeDOuXeKGhu9eHlFNNQng.WZHRNg.dYwwTIsdVHGP2', 'binh.tt@example.com', '0912345678', N'456 Nguyễn Huệ, TP.HCM', N'ENABLE', N'STAFF', 0, '1995-08-20'),
    (N'Lê Văn Cường', 'nv_cuong', 'password789', 'cuong.lv@example.com', '0923456789', N'789 Trần Phú, Đà Nẵng', N'ENABLE', N'STAFF', 1, '1992-03-10'),
    (N'Phạm Thị Duyên', 'nv_duyen', 'password101', 'duyen.pt@example.com', '0934567890', N'101 Lê Lợi, Huế', N'DISABLE', N'STAFF', 0, '1998-11-25'),
    (N'Hoàng Văn Em', 'nv_em', 'password202', 'em.hv@example.com', '0945678901', N'202 Phạm Văn Đồng, Hà Nội', N'DISABLE', N'ADMIN', 1, '1988-07-30');

-- Bảng khach_hang
INSERT INTO khach_hang (ten_khach_hang, sdt, tai_khoan, mat_khau, email, ngay_sinh, gioi_tinh, anh, tong_diem, so_diem_hien_tai, hang_khach_hang, trang_thai)
VALUES 
    (N'Nguyễn Thị Hoa', '0987654321', N'hoa_nt', '$2a$10$OprA7imS.gORyxtQ3KkfqOLuEXK2.NjHcTRRFpPfnScJQpaokqSFC', 'hoa.nt@example.com', '1993-04-12', 0, 'hoa.jpg', 1000.00, 500.00, N'GOLD', N'ACTIVE'),
    (N'Trần Văn Hùng', '0976543210', N'hung_tv', '$2a$10$G9o3ggyK9QDDeDOuXeKGhu9eHlFNNQng.WZHRNg.dYwwTIsdVHGP2', 'hung.tv@example.com', '1985-09-25', 1, 'hung.jpg', 500.00, 200.00, N'SILVER', N'ACTIVE'),
    (N'Lê Thị Lan', '0965432109', N'lan_lt', 'pass789', 'lan.lt@example.com', '1997-12-30', 0, 'lan.jpg', 200.00, 100.00, N'MEMBER', N'INACTIVE'),
    (N'Phạm Văn Minh', '0954321098', N'minh_pv', 'pass101', 'minh.pv@example.com', '1990-06-18', 1, 'minh.jpg', 3000.00, 1500.00, N'DIAMOND', N'ACTIVE'),
    (N'Hoàng Thị Ngọc', '0943210987', N'ngoc_ht', 'pass202', 'ngoc.ht@example.com', '1995-03-05', 0, 'ngoc.jpg', 800.00, 400.00, N'GOLD', N'ACTIVE');

-- Bảng phieu_giam_gia
INSERT INTO phieu_giam_gia (ten_khuyen_mai, loai_khuyen_mai, gia_tri_khuyen_mai, gia_tri_don_hang_toi_thieu, gia_tri_khuyen_mai_toi_da, ngay_bat_dau, ngay_ket_thuc, dieu_kien_ap_dung, hang_toi_thieu, so_luong, so_diem_can_de_doi, is_global, trang_thai)
VALUES 
    (N'Giảm giá iPhone 10%', N'Phần trăm', 10.00, 1000000.00, 200000.00, '2025-05-01', '2025-06-01', N'Áp dụng cho đơn iPhone từ 1 triệu', N'SILVER', 100, 100.00, 1, N'ACTIVE'),
    (N'Giảm 200K iPhone', N'Cố định', 200000.00, 2000000.00, 200000.00, '2025-04-15', '2025-05-15', N'Đơn iPhone từ 2 triệu', N'GOLD', 50, 200.00, 0, N'NOT_STARTED'),
    (N'Flash Sale iPhone 15%', N'Phần trăm', 15.00, 500000.00, 300000.00, '2025-06-01', '2025-06-07', N'Flash sale iPhone cuối tuần', N'MEMBER', 200, 50.00, 1, N'NOT_STARTED'),
    (N'Giảm 500K iPhone VIP', N'Cố định', 500000.00, 5000000.00, 500000.00, '2025-03-01', '2025-04-01', N'Đơn iPhone từ 5 triệu', N'DIAMOND', 20, 500.00, 0, N'EXPIRED'),
    (N'Giảm 5% iPhone 16', N'Phần trăm', 5.00, 1000000.00, 100000.00, '2025-05-10', '2025-05-20', N'Áp dụng iPhone 16', N'GOLD', 150, 80.00, 1, N'EXPIRED');

-- Bảng hoa_don
INSERT INTO hoa_don (id_khach_hang, id_phieu_giam_gia, ten_nguoi_nhan, dia_chi, sdt, phi_ship, tong_tien, so_tien_giam, thanh_tien, ngay_tao, loai_hoa_don, ngay_thanh_toan, trang_thai_thanh_toan)
VALUES 
    (1, 1, N'Nguyễn Thị Hoa', N'123 Đường Láng, Hà Nội', '0987654321', 30000.00, 20000000.00, 200000.00, 19830000.00, '2025-05-02', N'ONLINE', '2025-05-02', 'PENDING'),
    (2, 2, N'Trần Văn Hùng', N'456 Nguyễn Huệ, TP.HCM', '0976543210', 40000.00, 15000000.00, 200000.00, 14840000.00, '2025-04-20', N'POS', '2025-04-20', 'PENDING'),
    (3, NULL, N'Lê Thị Lan', N'789 Trần Phú, Đà Nẵng', '0965432109', 25000.00, 10000000.00, 0.00, 10025000.00, '2025-05-01', N'ONLINE', '2025-05-01', 'PAID'),
    (4, 4, N'Phạm Văn Minh', N'101 Lê Lợi, Huế', '0954321098', 35000.00, 25000000.00, 500000.00, 24535000.00, '2025-03-15', N'POS', '2025-03-15', 'PAID'),
    (5, 5, N'Hoàng Thị Ngọc', N'202 Phạm Văn Đồng, Hà Nội', '0943210987', 30000.00, 18000000.00, 90000.00, 17940000.00, '2025-05-12', N'ONLINE', '2025-05-12', 'PAID');

-- Bảng lich_su_hoa_don
INSERT INTO lich_su_hoa_don (id_nhan_vien, id_hoa_don, hanh_dong, thoi_gian_thay_doi, mo_ta)
VALUES 
    (1, 1, N'Tạo hóa đơn', '2025-05-02', N'Khách hàng đặt iPhone 16'),
    (2, 2, N'Thanh toán hóa đơn', '2025-04-20', N'Thanh toán iPhone 15 Pro tại cửa hàng'),
    (3, 3, N'Xác nhận đơn hàng', '2025-05-01', N'Đơn iPhone 14 chờ thanh toán'),
    (4, 4, N'Hủy giảm giá', '2025-03-15', N'Khách hàng đổi ý khi mua iPhone 13'),
    (5, 5, N'Cập nhật địa chỉ', '2025-05-12', N'Đổi địa chỉ giao iPhone 12');

-- Bảng khach_hang_giam_gia
INSERT INTO khach_hang_giam_gia (id_khach_hang, id_phieu_giam_gia, is_user, ngay_cap)
VALUES 
    (1, 1, 1, '2025-05-01'),
    (2, 2, 0, '2025-04-15'),
    (3, 3, 0, '2025-06-01'),
    (4, 4, 1, '2025-03-01'),
    (5, 5, 1, '2025-05-10');

-- Bảng dia_chi
INSERT INTO dia_chi (id_khach_hang, ten_nguoi_nhan, so_nha, ten_duong, xa_phuong, quan_huyen, tinh_thanh_pho, dia_chi_chinh)
VALUES 
    (1, N'Nguyễn Thị Hoa', N'123', N'Đường Láng', N'Đống Đa', N'Đống Đa', N'Hà Nội', 1),
	(1, N'Nguyễn Văn Hoe', N'1234', N'Đường Bắc Nam', N'Ngọc Liệp', N'Quốc Oai', N'Hà Nội', 0),
    (2, N'Trần Văn Hùng', N'456', N'Nguyễn Huệ', N'Quận 1', N'Quận 1', N'TP.HCM', 1),
    (3, N'Lê Thị Lan', N'789', N'Trần Phú', N'Hai Châu', N'Hai Châu', N'Đà Nẵng', 1),
    (4, N'Phạm Văn Minh', N'101', N'Lê Lợi', N'Thừa Thiên', N'Thừa Thiên', N'Huế', 1),
    (5, N'Hoàng Thị Ngọc', N'202', N'Phạm Văn Đồng', N'Cầu Giấy', N'Cầu Giấy', N'Hà Nội', 1);

-- Bảng gio_hang
INSERT INTO gio_hang (id_khach_hang)
VALUES 
    (1),
    (2),
    (3),
    (4),
    (5);

-- Bảng camera_sau
INSERT INTO camera_sau (loai_camera, do_phan_giai, khau_do, loai_zoom, che_do_chup)
VALUES 
    (N'Wide', N'48MP', N'f/1.6', N'Optical 2x', N'Night Mode'),
    (N'Ultra Wide', N'12MP', N'f/2.4', N'Digital', N'Panorama'),
    (N'Telephoto', N'12MP', N'f/2.8', N'Optical 3x', N'Portrait'),
    (N'Macro', N'12MP', N'f/2.4', N'Digital', N'Macro Mode'),
    (N'Periscope', N'12MP', N'f/3.4', N'Optical 5x', N'ProRAW');

-- Bảng camera_truoc
INSERT INTO camera_truoc (loai_camera, do_phan_giai, khau_do, loai_zoom, che_do_chup)
VALUES 
    (N'TrueDepth', N'12MP', N'f/1.9', N'Digital', N'Face ID'),
    (N'Wide', N'12MP', N'f/2.2', N'Digital', N'Portrait'),
    (N'Selfie', N'12MP', N'f/2.0', N'Digital', N'Animoji'),
    (N'FaceTime', N'12MP', N'f/2.2', N'Digital', N'Center Stage'),
    (N'Portrait', N'12MP', N'f/1.9', N'Digital', N'Night Mode');

-- Bảng cpu
INSERT INTO cpu (hang_san_xuat, so_nhan, so_luong, xung_nhip, cong_nghe_san_xuat, bo_nho_dem, tieu_thu_dien_nang, nam_ra_mat)
VALUES 
    (N'Apple', N'6 nhân', 6, N'3.46 GHz', N'3nm', N'8MB', N'15W', '2024-09-01'), -- A18
    (N'Apple', N'6 nhân', 6, N'3.7 GHz', N'3nm', N'12MB', N'15W', '2023-09-01'), -- A17 Pro
    (N'Apple', N'6 nhân', 6, N'3.2 GHz', N'4nm', N'8MB', N'15W', '2022-09-01'), -- A16 Bionic
    (N'Apple', N'6 nhân', 6, N'3.2 GHz', N'5nm', N'8MB', N'15W', '2021-09-01'), -- A15 Bionic
    (N'Apple', N'6 nhân', 6, N'3.0 GHz', N'5nm', N'8MB', N'15W', '2020-09-01'); -- A14 Bionic

-- Bảng loai
INSERT INTO loai (ten_loai)
VALUES 
    (N'Thường'),
    (N'Plus'),
    (N'Pro'),
    (N'Pro Max'),
    (N'Mini');

-- Bảng xuat_xu
INSERT INTO xuat_xu (ma_xuat_xu, ten_quoc_gia)
VALUES 
    ('VNA', N'Việt Nam'),
    ('LLA', N'Mỹ Latinh'),
    ('JPA', N'Nhật Bản'),
    ('CHA', N'Trung Quốc'),
    ('KRA', N'Hàn Quốc');

-- Bảng pin
INSERT INTO pin (phien_ban, cong_suat_sac, thoi_gian_su_dung, so_lan_sac_toi_da)
VALUES 
    (N'Li-Ion 4000mAh', N'25W', N'20 giờ', N'1000 lần'),
    (N'Li-Ion 4500mAh', N'30W', N'22 giờ', N'1200 lần'),
    (N'Li-Ion 3500mAh', N'20W', N'18 giờ', N'800 lần'),
    (N'Li-Ion 5000mAh', N'45W', N'25 giờ', N'1500 lần'),
    (N'Li-Ion 4200mAh', N'27W', N'21 giờ', N'1100 lần');

-- Bảng he_dieu_hanh
INSERT INTO he_dieu_hanh (phien_ban, nha_phat_trien, giao_dien_nguoi_dung)
VALUES 
    (N'iOS 18', N'Apple', N'iOS UI'),
    (N'iOS 17', N'Apple', N'iOS UI'),
    (N'iOS 16', N'Apple', N'iOS UI'),
    (N'iOS 15', N'Apple', N'iOS UI'),
    (N'iOS 14', N'Apple', N'iOS UI');

-- Bảng man_hinh
INSERT INTO man_hinh (ten_man_hinh, kich_thuoc, loai_man_hinh, do_phan_giai, tan_so_quet, do_sang, chat_lieu_kinh)
VALUES 
    (N'Super Retina XDR', N'6.1 inch', N'OLED', N'2532x1170', N'120Hz', N'2000 nit', N'Ceramic Shield'),
    (N'Super Retina XDR', N'6.7 inch', N'OLED', N'2796x1290', N'120Hz', N'2000 nit', N'Ceramic Shield'),
    (N'Liquid Retina', N'6.1 inch', N'LCD', N'1792x828', N'60Hz', N'625 nit', N'Ceramic Shield'),
    (N'Super Retina', N'5.4 inch', N'OLED', N'2340x1080', N'60Hz', N'1200 nit', N'Ceramic Shield'),
    (N'ProMotion XDR', N'6.7 inch', N'OLED', N'2796x1290', N'120Hz', N'2000 nit', N'Ceramic Shield');

-- Bảng rom
INSERT INTO rom (dung_luong_rom, loai_rom, toc_do_doc_ghi, nha_san_xuat, nam_ra_mat)
VALUES 
    (N'128GB', N'NVMe', N'3500 MB/s', N'Apple', '2023-09-01'),
    (N'256GB', N'NVMe', N'4000 MB/s', N'Apple', '2023-09-01'),
    (N'512GB', N'NVMe', N'4000 MB/s', N'Apple', '2023-09-01'),
    (N'1TB', N'NVMe', N'5000 MB/s', N'Apple', '2023-09-01'),
    (N'64GB', N'NVMe', N'3000 MB/s', N'Apple', '2021-09-01');

-- Bảng ram
INSERT INTO ram (dung_luong_ram, loai_ram, toc_do_doc_ghi, nha_san_xuat, nam_ra_mat)
VALUES 
    (N'6GB', N'LPDDR5', N'6400 MB/s', N'Apple', '2023-09-01'),
    (N'8GB', N'LPDDR5', N'6400 MB/s', N'Apple', '2023-09-01'),
    (N'4GB', N'LPDDR4X', N'4266 MB/s', N'Apple', '2021-09-01'),
    (N'12GB', N'LPDDR5', N'7200 MB/s', N'Apple', '2023-09-01'),
    (N'16GB', N'LPDDR5X', N'8500 MB/s', N'Apple', '2023-09-01');

-- Bảng mau_sac
INSERT INTO mau_sac (ten_mau)
VALUES 
    (N'Đen Phantôm'),
    (N'Trắng Ngọc Trai'),
    (N'Vàng Ánh Kim'),
    (N'Xanh Biển Sâu'),
    (N'Hồng Phấn');

-- Bảng nha_cung_cap
INSERT INTO nha_cung_cap (ten_nha_cung_cap, dia_chi, sdt, email)
VALUES 
    (N'VNA Trading', N'123 Nguyễn Trãi, Hà Nội', '0901234567', 'vna@example.com'),
    (N'Thế Giới Di Động', N'456 Lê Văn Sỹ, TP.HCM', '0912345678', 'tgdd@example.com'),
    (N'FPT Shop', N'789 Trần Hưng Đạo, Đà Nẵng', '0923456789', 'fpt@example.com'),
    (N'CellphoneS', N'101 Hai Bà Trưng, Huế', '0934567890', 'cellphones@example.com'),
    (N'Hoàng Hà Mobile', N'202 Lý Thường Kiệt, Hà Nội', '0945678901', 'hoangha@example.com');

-- Bảng san_pham
INSERT INTO san_pham (ten_san_pham, thuong_hieu, so_luong_ton_kho, id_nha_cung_cap)
VALUES 
    (N'iPhone 16', N'Apple', 50, 1),
    (N'iPhone 16 Pro', N'Apple', 30, 2),
    (N'iPhone 15', N'Apple', 20, 3),
    (N'iPhone 14', N'Apple', 40, 4),
    (N'iPhone 13', N'Apple', 25, 5);

-- Bảng san_pham_chi_tiet
INSERT INTO san_pham_chi_tiet (id_san_pham, id_mau, id_ram, id_rom, id_man_hinh, id_he_dieu_hanh, id_pin, id_cpu, id_camera_truoc, id_camera_sau, id_xuat_xu, id_loai, so_luong, gia_ban)
VALUES 
    (1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 10, 20000000.00), -- iPhone 16, VNA
    (2, 2, 2, 2, 2, 1, 2, 2, 2, 2, 2, 3, 8, 25000000.00), -- iPhone 16 Pro, LLA
    (3, 3, 3, 3, 3, 2, 3, 3, 3, 3, 3, 1, 15, 15000000.00), -- iPhone 15, JPA
    (4, 4, 4, 4, 4, 3, 4, 4, 4, 4, 4, 1, 12, 12000000.00), -- iPhone 14, CHA
    (5, 5, 5, 5, 5, 4, 5, 5, 5, 5, 5, 1, 20, 10000000.00); -- iPhone 13, KRA

	--bảng loai_bao_hanh
INSERT INTO loai_bao_hanh (ten_loai_bao_hanh, thoi_gian_thang, mo_ta)
VALUES 
    (N'Bảo hành chính hãng', 12, N'Bảo hành từ nhà sản xuất'),
    (N'Bảo hành của cửa hàng', 6, N'Bảo hành nội bộ của cửa hàng'),
    (N'Bảo hành đổi trả', 1, N'Cho phép đổi trả trong vòng 1 tháng');

-- Bảng bao_hanh
INSERT INTO bao_hanh (id_khach_hang, id_san_pham_chi_tiet, ngay_bat_dau, ngay_ket_thuc, id_loai_bao_hanh, trang_thai_bao_hanh)
VALUES 
    (1, 1, '2025-05-02', '2026-05-02', 1, N'UNDER_WARRANTY'),
    (2, 2, '2025-04-20', '2026-04-20', 2, N'UNDER_WARRANTY'),
    (3, 3, '2025-05-01', '2026-05-01', 1, N'UNDER_WARRANTY'),
    (4, 4, '2025-03-15', '2026-03-15', 1, N'UNDER_WARRANTY'),
    (5, 5, '2025-05-12', '2026-05-12', 3, N'UNDER_WARRANTY');

INSERT INTO lich_su_bao_hanh (id_san_pham_bao_hanh, ngay_tiep_nhan, ngay_hoan_thanh, mo_ta_loi, trang_thai)
VALUES
    (1, '2025-05-03', '2025-05-10', N'Lỗi màn hình chớp tắt', N'REPAIRED'),
    (2, '2025-04-25', '2025-04-28', N'Không sạc được pin', N'REPAIRED'),
    (3, '2025-05-02', NULL, N'Loa bị rè', N'IN_REPAIR'),
    (4, '2025-04-01', '2025-04-05', N'Lỗi phần mềm, treo máy', N'REPAIRED'),
    (5, '2025-05-13', NULL, N'Nứt màn hình do rơi vỡ', N'WARRANTY_VOID');

-- Bảng imei
INSERT INTO imei (id_san_pham_chi_tiet, so_imei, trang_thai_imei, nha_mang)
VALUES 
    (1, '123456789012345', N'AVAILABLE', N'Viettel'),
    (2, '123456789012346', N'AVAILABLE', N'Mobifone'),
    (3, '123456789012347', N'AVAILABLE', N'Vinaphone'),
    (4, '123456789012348', N'AVAILABLE', N'Viettel'),
    (5, '123456789012349', N'AVAILABLE', N'Mobifone');

-- Bảng hinh_anh
INSERT INTO hinh_anh (id_san_pham_chi_tiet, url)
VALUES 
    (1, 'https://example.com/iphone16_black.jpg'),
    (2, 'https://example.com/iphone16pro_white.jpg'),
    (3, 'https://example.com/iphone15_gold.jpg'),
    (4, 'https://example.com/iphone14_blue.jpg'),
    (5, 'https://example.com/iphone13_pink.jpg');

-- Bảng gio_hang_chi_tiet
INSERT INTO gio_hang_chi_tiet (id_gio_hang, id_san_pham_chi_tiet, so_luong, gia, ngay_them)
VALUES 
    -- Giỏ hàng 1: Thêm iPhone 16
    (1, 1, 2, 20000000.00, '2025-05-05'), -- 2 chiếc iPhone 16, giá 20 triệu
    -- Giỏ hàng 2: Thêm iPhone 16 Pro và iPhone 15
    (2, 2, 1, 25000000.00, '2025-05-05'), -- 1 chiếc iPhone 16 Pro, giá 25 triệu
    (2, 3, 1, 15000000.00, '2025-05-05'); -- 1 chiếc iPhone 15, giá 15 triệu

-- Bảng chi_tiet_hoa_don
INSERT INTO chi_tiet_hoa_don (id_hoa_don, id_san_pham_chi_tiet, ten_san_pham, mo_ta, so_luong, don_gia)
VALUES 
    (1, 1, N'iPhone 16', N'128GB, Đen Phantôm, VNA', 1, 20000000.00),
    (2, 2, N'iPhone 16 Pro', N'256GB, Trắng Ngọc Trai, LLA', 1, 25000000.00),
    (3, 3, N'iPhone 15', N'128GB, Vàng Ánh Kim, JPA', 2, 15000000.00),
    (4, 4, N'iPhone 14', N'64GB, Xanh Biển Sâu, CHA', 1, 12000000.00),
    (5, 5, N'iPhone 13', N'64GB, Hồng Phấn, KRA', 3, 10000000.00);

-- Bảng imei_da_ban
INSERT INTO imei_da_ban (id_hoa_don_chi_tiet, so_imei, trang_thai)
VALUES 
    (1, '123456789012345', N'SOLD'),
    (2, '123456789012346', N'SOLD'),
    (3, '123456789012347', N'SOLD'),
    (4, '123456789012348', N'SOLD'),
    (5, '123456789012349', N'SOLD');

-- Bảng giao_hang
INSERT INTO giao_hang (id_khach_hang, id_hoa_don, ngay_dat_hang, tong_gia_tri_don_hang, dia_chi_giao_hang, trang_thai_don_hang)
VALUES 
    (1, 1, '2025-05-02', 19830000.00, N'123 Đường Láng, Hà Nội', N'PENDING'),
    (3, 3, '2025-05-01', 10025000.00, N'789 Trần Phú, Đà Nẵng', N'SHIPPING'),
    (5, 5, '2025-05-12', 17940000.00, N'202 Phạm Văn Đồng, Hà Nội', N'PACKED');

-- Bảng chi_tiet_giao_hang
INSERT INTO chi_tiet_giao_hang (id_giao_hang, id_san_pham_chi_tiet, so_luong, don_gia)
VALUES 
    (1, 1, 1, 20000000.00),
    (2, 3, 2, 15000000.00),
    (3, 5, 3, 10000000.00);

-- Bảng phuong_thuc_thanh_toan
INSERT INTO phuong_thuc_thanh_toan (ten_phuong_thuc, loai_hinh_thuc)
VALUES 
    (N'Thẻ tín dụng', N'Trực tuyến'),
    (N'Chuyển khoản ngân hàng', N'Trực tuyến'),
    (N'Tiền mặt', N'Tại cửa hàng'),
    (N'Ví Momo', N'Trực tuyến'),
    (N'Thanh toán khi nhận hàng', N'Tại điểm giao');

-- Bảng chi_tiet_thanh_toan
INSERT INTO chi_tiet_thanh_toan (id_hoa_don, id_phuong_thuc_thanh_toan, so_tien_thanh_toan)
VALUES 
    (1, 1, 19830000.00),
    (2, 3, 14840000.00),
    (3, 4, 0.00),
    (4, 2, 24535000.00),
    (5, 5, 17940000.00);

-- 1. nhan_vien
SELECT * FROM nhan_vien;

-- 2. khach_hang
SELECT * FROM khach_hang;

-- 3. phieu_giam_gia
SELECT * FROM phieu_giam_gia;

-- 4. hoa_don
SELECT * FROM hoa_don;

-- 5. lich_su_hoa_don
SELECT * FROM lich_su_hoa_don;

-- 6. khach_hang_giam_gia
SELECT * FROM khach_hang_giam_gia;

-- 7. dia_chi
SELECT * FROM dia_chi;

-- 8. gio_hang
SELECT * FROM gio_hang;

-- 9. camera_sau
SELECT * FROM camera_sau;

-- 10. camera_truoc
SELECT * FROM camera_truoc;

-- 11. cpu
SELECT * FROM cpu;

-- 12. loai
SELECT * FROM loai;

-- 13. xuat_xu
SELECT * FROM xuat_xu;

-- 14. pin
SELECT * FROM pin;

-- 15. he_dieu_hanh
SELECT * FROM he_dieu_hanh;

-- 16. man_hinh
SELECT * FROM man_hinh;

-- 17. rom
SELECT * FROM rom;

-- 18. ram
SELECT * FROM ram;

-- 19. mau_sac
SELECT * FROM mau_sac;

-- 20. nha_cung_cap
SELECT * FROM nha_cung_cap;

-- 21. san_pham
SELECT * FROM san_pham;

-- 22. san_pham_chi_tiet
SELECT * FROM san_pham_chi_tiet;

-- 23. bao_hanh
SELECT * FROM bao_hanh;

-- 24. imei
SELECT * FROM imei;

-- 25. hinh_anh
SELECT * FROM hinh_anh;

-- 26. gio_hang_chi_tiet
SELECT * FROM gio_hang_chi_tiet;

-- 27. chi_tiet_hoa_don
SELECT * FROM chi_tiet_hoa_don;

-- 28. imei_da_ban
SELECT * FROM imei_da_ban;

-- 29. giao_hang
SELECT * FROM giao_hang;

-- 30. chi_tiet_giao_hang
SELECT * FROM chi_tiet_giao_hang;

-- 31. phuong_thuc_thanh_toan
SELECT * FROM phuong_thuc_thanh_toan;

-- 32. chi_tiet_thanh_toan
SELECT * FROM chi_tiet_thanh_toan;

SELECT * FROM loai_bao_hanh

SELECT * FROM lich_su_bao_hanh