CREATE DATABASE TechWorld;
GO
USE TechWorld;
GO

CREATE TABLE nhan_vien (
                           id_nhan_vien INT IDENTITY(1,1) PRIMARY KEY,
                           ma_nhan_vien AS (
        'NV' + CASE
            WHEN id_nhan_vien < 10 THEN '00' + CAST(id_nhan_vien AS VARCHAR)
            WHEN id_nhan_vien < 100 THEN '0' + CAST(id_nhan_vien AS VARCHAR)
            ELSE CAST(id_nhan_vien AS VARCHAR)
        END
    ) PERSISTED,
                           ten_nhan_vien NVARCHAR(50),
                           tai_khoan VARCHAR(50),
                           mat_khau VARCHAR(255),
                           email VARCHAR(100),
                           sdt VARCHAR(10),
                           dia_chi NVARCHAR(100),
                           trang_thai NVARCHAR(50),
                           chuc_vu NVARCHAR(50),
                           gioi_tinh BIT,
                           ngay_sinh DATE
);

CREATE TABLE khach_hang (
                            id_khach_hang INT IDENTITY(1,1) PRIMARY KEY,
                            ma_khach_hang AS (
        'KH' + CASE
            WHEN id_khach_hang < 10 THEN '00' + CAST(id_khach_hang AS VARCHAR)
            WHEN id_khach_hang < 100 THEN '0' + CAST(id_khach_hang AS VARCHAR)
            ELSE CAST(id_khach_hang AS VARCHAR)
        END
    ) PERSISTED,
                            ten_khach_hang NVARCHAR(50),
                            sdt VARCHAR(10),
                            tai_khoan NVARCHAR(50),
                            mat_khau NVARCHAR(255),
                            email NVARCHAR(100),
                            ngay_sinh DATE,
                            gioi_tinh BIT,
                            anh NVARCHAR(200),
                            tong_diem DECIMAL(10,2),
                            so_diem_hien_tai DECIMAL(10,2),
                            hang_khach_hang NVARCHAR(50),
                            trang_thai NVARCHAR(50)
);

CREATE TABLE phieu_giam_gia (
                                id_phieu_giam_gia INT IDENTITY(1,1) PRIMARY KEY,
                                ma_giam_gia AS (
        'VC' + CASE
            WHEN id_phieu_giam_gia < 10 THEN '00' + CAST(id_phieu_giam_gia AS VARCHAR)
            WHEN id_phieu_giam_gia < 100 THEN '0' + CAST(id_phieu_giam_gia AS VARCHAR)
            ELSE CAST(id_phieu_giam_gia AS VARCHAR)
        END
    ) PERSISTED,
                                ten_khuyen_mai NVARCHAR(50),
                                loai_khuyen_mai NVARCHAR(50),
                                gia_tri_khuyen_mai DECIMAL(10,2),
                                gia_tri_don_hang_toi_thieu DECIMAL(10,2),
                                gia_tri_khuyen_mai_toi_da DECIMAL(10,2),
                                ngay_bat_dau DATE,
                                ngay_ket_thuc DATE,
                                dieu_kien_ap_dung NVARCHAR(100),
                                hang_toi_thieu NVARCHAR(50),
                                so_luong INT,
                                so_diem_can_de_doi DECIMAL(10,2),
                                is_global BIT,
                                trang_thai NVARCHAR(50)
);

CREATE TABLE hoa_don (
                         id_hoa_don INT IDENTITY(1,1) PRIMARY KEY,

                         ma_hoa_don AS (
        'HD' + CASE
            WHEN id_hoa_don < 10 THEN '00' + CAST(id_hoa_don AS VARCHAR)
            WHEN id_hoa_don < 100 THEN '0' + CAST(id_hoa_don AS VARCHAR)
            ELSE CAST(id_hoa_don AS VARCHAR)
        END
    ) PERSISTED,

                         ma_van_don NVARCHAR(20) NULL,

                         id_khach_hang INT REFERENCES khach_hang(id_khach_hang),
                         id_phieu_giam_gia INT REFERENCES phieu_giam_gia(id_phieu_giam_gia),

                         ten_nguoi_mua NVARCHAR(50),
                         sdt_nguoi_mua NVARCHAR(10),
                         ten_nguoi_nhan NVARCHAR(50),
                         sdt_nguoi_nhan NVARCHAR(10),
                         dia_chi_giao_hang NVARCHAR(100),
                         ngay_dat_hang DATE,

                         phi_ship DECIMAL(10,2),
                         tong_tien DECIMAL(10,2),
                         so_tien_giam DECIMAL(10,2),
                         thanh_tien DECIMAL(10,2),
                         ngay_tao_hoa_don DATE,
                         loai_hoa_don NVARCHAR(50),
                         ngay_thanh_toan DATE,
                         trang_thai_thanh_toan NVARCHAR(50),
                         trang_thai_don_hang NVARCHAR(50)
);

CREATE TABLE lich_su_hoa_don (
                                 id_lich_su_hoa_don INT IDENTITY(1,1) PRIMARY KEY,
                                 id_nhan_vien INT,
                                 id_hoa_don INT,
                                 hanh_dong NVARCHAR(100),
                                 thoi_gian_thay_doi DATE,
                                 mo_ta NVARCHAR(100),
                                 FOREIGN KEY (id_nhan_vien) REFERENCES nhan_vien(id_nhan_vien),
                                 FOREIGN KEY (id_hoa_Don) REFERENCES hoa_don(id_hoa_don) ON DELETE CASCADE
);

CREATE TABLE khach_hang_giam_gia (
                                     id_khach_hang_giam_gia INT IDENTITY(1,1) PRIMARY KEY,
                                     id_khach_hang INT REFERENCES khach_hang(id_khach_hang) ON DELETE CASCADE,
                                     id_phieu_giam_gia INT REFERENCES phieu_giam_gia(id_phieu_giam_gia) ON DELETE CASCADE,
                                     is_user BIT,
                                     ngay_cap DATE
);

CREATE TABLE dia_chi (
                         id_dia_chi INT IDENTITY(1,1) PRIMARY KEY,
                         id_khach_hang INT,
                         ten_nguoi_nhan NVARCHAR(50),
                         sdt_nguoi_nhan VARCHAR(10),
                         so_nha NVARCHAR(50),
                         ten_duong NVARCHAR(50),
                         xa_phuong NVARCHAR(50),
                         quan_huyen NVARCHAR(50),
                         tinh_thanh_pho NVARCHAR(50),
                         dia_chi_chinh BIT,
                         FOREIGN KEY (id_khach_hang) REFERENCES khach_hang(id_khach_hang) ON DELETE CASCADE
);

CREATE TABLE gio_hang (
                          id_gio_hang INT IDENTITY(1,1) PRIMARY KEY,
                          id_khach_hang INT UNIQUE,
                          FOREIGN KEY (id_khach_hang) REFERENCES khach_hang(id_khach_hang) ON DELETE CASCADE
);

CREATE TABLE camera_sau (
                            id_camera_sau INT IDENTITY(1,1) PRIMARY KEY,
                            loai_camera NVARCHAR(100),
                            do_phan_giai NVARCHAR(50),
                            khau_do NVARCHAR(50),
                            loai_zoom NVARCHAR(50),
                            che_do_chup NVARCHAR(50)
);

CREATE TABLE camera_truoc (
                              id_camera_truoc INT IDENTITY(1,1) PRIMARY KEY,
                              loai_camera NVARCHAR(100),
                              do_phan_giai NVARCHAR(50),
                              khau_do NVARCHAR(50),
                              loai_zoom NVARCHAR(50),
                              che_do_chup NVARCHAR(50)
);

CREATE TABLE cpu (
                     id_cpu INT IDENTITY(1,1) PRIMARY KEY,
                     hang_san_xuat NVARCHAR(50),
                     so_nhan NVARCHAR(50),
                     so_luong INT,
                     xung_nhip NVARCHAR(50),
                     cong_nghe_san_xuat NVARCHAR(50),
                     bo_nho_dem NVARCHAR(50),
                     tieu_thu_dien_nang NVARCHAR(50),
                     nam_ra_mat DATE
);

CREATE TABLE loai (
                      id_loai INT IDENTITY(1,1) PRIMARY KEY,
                      ten_loai NVARCHAR(50)
);

CREATE TABLE xuat_xu (
                         id_xuat_xu INT IDENTITY(1,1) PRIMARY KEY,
                         ma_xuat_xu NVARCHAR(10),
                         ten_quoc_gia NVARCHAR(50)
);

CREATE TABLE pin (
                     id_pin INT IDENTITY(1,1) PRIMARY KEY,
                     phien_ban NVARCHAR(50),
                     cong_suat_sac NVARCHAR(50),
                     thoi_gian_su_dung NVARCHAR(50),
                     so_lan_sac_toi_da NVARCHAR(50)
);

CREATE TABLE he_dieu_hanh (
                              id_he_dieu_hanh INT IDENTITY(1,1) PRIMARY KEY,
                              phien_ban NVARCHAR(50),
                              nha_phat_trien NVARCHAR(50),
                              giao_dien_nguoi_dung NVARCHAR(50)
);

CREATE TABLE man_hinh (
                          id_man_hinh INT IDENTITY(1,1) PRIMARY KEY,
                          ten_man_hinh NVARCHAR(50),
                          kich_thuoc NVARCHAR(50),
                          loai_man_hinh NVARCHAR(50),
                          do_phan_giai NVARCHAR(50),
                          tan_so_quet NVARCHAR(50),
                          do_sang NVARCHAR(50),
                          chat_lieu_kinh NVARCHAR(50)
);

CREATE TABLE rom (
                     id_rom INT IDENTITY(1,1) PRIMARY KEY,
                     dung_luong_rom NVARCHAR(50),
                     loai_rom NVARCHAR(50),
                     toc_do_doc_ghi NVARCHAR(50),
                     nha_san_xuat NVARCHAR(50),
                     nam_ra_mat DATE
);

CREATE TABLE ram (
                     id_ram INT IDENTITY(1,1) PRIMARY KEY,
                     dung_luong_ram NVARCHAR(50),
                     loai_ram NVARCHAR(50),
                     toc_do_doc_ghi NVARCHAR(50),
                     nha_san_xuat NVARCHAR(50),
                     nam_ra_mat DATE
);

CREATE TABLE mau_sac (
                         id_mau_sac INT IDENTITY(1,1) PRIMARY KEY,
                         ten_mau NVARCHAR(50),
                         hex_color NVARCHAR(7)
);

CREATE TABLE nha_cung_cap (
                              id_nha_cung_cap INT IDENTITY(1,1) PRIMARY KEY,
                              ten_nha_cung_cap NVARCHAR(50),
                              dia_chi NVARCHAR(50),
                              sdt VARCHAR(10),
                              email VARCHAR(50)
);

CREATE TABLE san_pham (
                          id_san_pham INT IDENTITY(1,1) PRIMARY KEY,
                          ma_san_pham AS (
        'SP' + CASE
            WHEN id_san_pham < 10 THEN '00' + CAST(id_san_pham AS VARCHAR)
            WHEN id_san_pham < 100 THEN '0' + CAST(id_san_pham AS VARCHAR)
            ELSE CAST(id_san_pham AS VARCHAR)
        END
    ) PERSISTED,
                          ten_san_pham NVARCHAR(50),
                          thuong_hieu NVARCHAR(50),
                          trang_thai VARCHAR(50),
                          id_nha_cung_cap INT REFERENCES nha_cung_cap(id_nha_cung_cap)
);

CREATE TABLE san_pham_chi_tiet (
                                   id_san_pham_chi_tiet INT IDENTITY(1,1) PRIMARY KEY,
                                   ma_san_pham_chi_tiet AS (
        'SPCT' + CASE
            WHEN id_san_pham_chi_tiet < 10 THEN '00' + CAST(id_san_pham_chi_tiet AS VARCHAR)
            WHEN id_san_pham_chi_tiet < 100 THEN '0' + CAST(id_san_pham_chi_tiet AS VARCHAR)
            ELSE CAST(id_san_pham_chi_tiet AS VARCHAR)
        END
    ) PERSISTED,
                                   id_san_pham INT REFERENCES san_pham(id_san_pham) ON DELETE CASCADE,
                                   id_mau INT REFERENCES mau_sac(id_mau_sac),
                                   id_ram INT REFERENCES ram(id_ram),
                                   id_rom INT REFERENCES rom(id_rom),
                                   id_man_hinh INT REFERENCES man_hinh(id_man_hinh),
                                   id_he_dieu_hanh INT REFERENCES he_dieu_hanh(id_he_dieu_hanh),
                                   id_pin INT REFERENCES pin(id_pin),
                                   id_cpu INT REFERENCES cpu(id_cpu),
                                   id_camera_truoc INT REFERENCES camera_truoc(id_camera_truoc),
                                   id_camera_sau INT REFERENCES camera_sau(id_camera_sau),
                                   id_xuat_xu INT REFERENCES xuat_xu(id_xuat_xu),
                                   id_loai INT REFERENCES loai(id_loai),
                                   so_luong INT,
                                   gia_ban DECIMAL(10,2)
);

CREATE TABLE loai_bao_hanh (
                               id_loai_bao_hanh INT IDENTITY PRIMARY KEY,
                               ten_loai_bao_hanh NVARCHAR(100),
                               thoi_gian_thang INT,
                               mo_ta NVARCHAR(255)
);

CREATE TABLE bao_hanh (
                          id_bao_hanh INT IDENTITY(1,1) PRIMARY KEY,
                          id_khach_hang INT,
                          id_san_pham_chi_tiet INT REFERENCES san_pham_chi_tiet(id_san_pham_chi_tiet),
                          ngay_bat_dau DATE,
                          ngay_ket_thuc DATE,
                          id_loai_bao_hanh INT REFERENCES loai_bao_hanh(id_loai_bao_hanh),
                          trang_thai_bao_hanh NVARCHAR(50)
);

CREATE TABLE lich_su_bao_hanh (
                                  id INT IDENTITY(1,1) PRIMARY KEY,
                                  id_san_pham_bao_hanh INT REFERENCES bao_hanh(id_bao_hanh) ON DELETE CASCADE,
                                  ngay_tiep_nhan DATE,
                                  ngay_hoan_thanh DATE,
                                  mo_ta_loi NVARCHAR(255),
                                  trang_thai NVARCHAR(50)
);

CREATE TABLE imei (
                      id_imei INT IDENTITY(1,1) PRIMARY KEY,
                      id_san_pham_chi_tiet INT REFERENCES san_pham_chi_tiet(id_san_pham_chi_tiet) ON DELETE CASCADE,
                      so_imei VARCHAR(70),
                      trang_thai_imei NVARCHAR(50),
                      nha_mang NVARCHAR(50)
);

CREATE TABLE hinh_anh (
                          id_hinh_anh INT IDENTITY(1,1) PRIMARY KEY,
                          id_san_pham_chi_tiet INT REFERENCES san_pham_chi_tiet(id_san_pham_chi_tiet) ON DELETE CASCADE,
                          url VARCHAR(255),
                          image_public_id VARCHAR(100)
);

CREATE TABLE gio_hang_chi_tiet (
                                   id_gio_hang_chi_tiet INT IDENTITY(1,1) PRIMARY KEY,
                                   id_gio_hang INT,
                                   id_san_pham_chi_tiet INT,
                                   so_luong INT,
                                   gia DECIMAL(10,2),
                                   ngay_them DATE DEFAULT CAST(GETDATE() AS DATE),
                                   FOREIGN KEY (id_gio_hang) REFERENCES gio_hang(id_gio_hang) ON DELETE CASCADE,
                                   FOREIGN KEY (id_san_pham_chi_tiet) REFERENCES san_pham_chi_tiet(id_san_pham_chi_tiet)
);

CREATE TABLE chi_tiet_hoa_don (
                                  id_chi_tiet_hoa_don INT IDENTITY(1,1) PRIMARY KEY,
                                  id_hoa_don INT REFERENCES hoa_don(id_hoa_don) ON DELETE CASCADE,
                                  id_san_pham_chi_tiet INT REFERENCES san_pham_chi_tiet(id_san_pham_chi_tiet),

                                  ma_chi_tiet_hoa_don AS (
        'CTHD' + CASE
            WHEN id_chi_tiet_hoa_don < 10 THEN '00' + CAST(id_chi_tiet_hoa_don AS VARCHAR)
            WHEN id_chi_tiet_hoa_don < 100 THEN '0' + CAST(id_chi_tiet_hoa_don AS VARCHAR)
            ELSE CAST(id_chi_tiet_hoa_don AS VARCHAR)
        END
    ) PERSISTED,

                                  ten_san_pham NVARCHAR(50),
                                  mo_ta NVARCHAR(100),
                                  so_luong INT,
                                  don_gia DECIMAL(10,2)
);

CREATE TABLE imei_da_ban (
                             id_imei_da_ban INT IDENTITY(1,1) PRIMARY KEY,
                             id_chi_tiet_hoa_don INT REFERENCES chi_tiet_hoa_don (id_chi_tiet_hoa_don ) ON DELETE CASCADE,
                             so_imei VARCHAR(70),
                             trang_thai NVARCHAR(50)
);

CREATE TABLE phuong_thuc_thanh_toan (
                                        id_phuong_thuc_thanh_toan INT IDENTITY(1,1) PRIMARY KEY,
                                        ten_phuong_thuc NVARCHAR(50),
                                        loai_hinh_thuc NVARCHAR(50)
);

CREATE TABLE chi_tiet_thanh_toan (
                                     id_chi_tiet_thanh_toan INT IDENTITY(1,1) PRIMARY KEY,
                                     id_hoa_don INT REFERENCES hoa_don (id_hoa_don ) ON DELETE CASCADE,
                                     id_phuong_thuc_thanh_toan INT REFERENCES phuong_thuc_thanh_toan(id_phuong_thuc_thanh_toan),
                                     so_tien_thanh_toan DECIMAL(10,2)
);

INSERT INTO nhan_vien (ten_nhan_vien, tai_khoan, mat_khau, email, sdt, dia_chi, trang_thai, chuc_vu, gioi_tinh, ngay_sinh)
VALUES
    (N'Nguyễn Văn An', 'nv_an', '$2a$10$mQLhyl17N446ZOSUjzzRqOTkQ9q/PAaI9omLyfs82fHeJWdpzkutu', 'an.nv@example.com', '0901234567', N'123 Đường Láng, Hà Nội', N'ENABLE', N'ADMIN', 1, '1990-05-15'),
    (N'Trần Thị Bình', 'nv_binh', '$2a$10$mQLhyl17N446ZOSUjzzRqOTkQ9q/PAaI9omLyfs82fHeJWdpzkutu', 'binh.tt@example.com', '0912345678', N'456 Nguyễn Huệ, TP.HCM', N'ENABLE', N'STAFF', 0, '1995-08-20'),
    (N'Lê Văn Cường', 'nv_cuong', '$2a$10$mQLhyl17N446ZOSUjzzRqOTkQ9q/PAaI9omLyfs82fHeJWdpzkutu', 'cuong.lv@example.com', '0923456789', N'789 Trần Phú, Đà Nẵng', N'ENABLE', N'STAFF', 1, '1992-03-10'),
    (N'Phạm Thị Duyên', 'nv_duyen', '$2a$10$mQLhyl17N446ZOSUjzzRqOTkQ9q/PAaI9omLyfs82fHeJWdpzkutu', 'duyen.pt@example.com', '0934567890', N'101 Lê Lợi, Huế', N'DISABLE', N'STAFF', 0, '1998-11-25'),
    (N'Hoàng Văn Em', 'nv_em', '$2a$10$mQLhyl17N446ZOSUjzzRqOTkQ9q/PAaI9omLyfs82fHeJWdpzkutu', 'em.hv@example.com', '0945678901', N'202 Phạm Văn Đồng, Hà Nội', N'DISABLE', N'ADMIN', 1, '1988-07-30');

INSERT INTO khach_hang (ten_khach_hang, sdt, tai_khoan, mat_khau, email, ngay_sinh, gioi_tinh, anh, tong_diem, so_diem_hien_tai, hang_khach_hang, trang_thai)
VALUES
    (N'Nguyễn Thị Hoa', '0987654321', N'hoa_nt', '$2a$10$mQLhyl17N446ZOSUjzzRqOTkQ9q/PAaI9omLyfs82fHeJWdpzkutu', 'hoa.nt@example.com', '1993-04-12', 0, 'hoa.jpg', 1000.00, 500.00, N'GOLD', N'ACTIVE'),
    (N'Trần Văn Hùng', '0976543210', N'hung_tv', '$2a$10$mQLhyl17N446ZOSUjzzRqOTkQ9q/PAaI9omLyfs82fHeJWdpzkutu', 'hung.tv@example.com', '1985-09-25', 1, 'hung.jpg', 500.00, 200.00, N'SILVER', N'ACTIVE'),
    (N'Lê Thị Lan', '0965432109', N'lan_lt', '$2a$10$mQLhyl17N446ZOSUjzzRqOTkQ9q/PAaI9omLyfs82fHeJWdpzkutu', 'lan.lt@example.com', '1997-12-30', 0, 'lan.jpg', 200.00, 100.00, N'MEMBER', N'INACTIVE'),
    (N'Phạm Văn Minh', '0954321098', N'minh_pv', '$2a$10$mQLhyl17N446ZOSUjzzRqOTkQ9q/PAaI9omLyfs82fHeJWdpzkutu', 'minh.pv@example.com', '1990-06-18', 1, 'minh.jpg', 3000.00, 1500.00, N'DIAMOND', N'ACTIVE'),
    (N'Hoàng Thị Ngọc', '0943210987', N'ngoc_ht', '$2a$10$mQLhyl17N446ZOSUjzzRqOTkQ9q/PAaI9omLyfs82fHeJWdpzkutu', 'ngoc.ht@example.com', '1995-03-05', 0, 'ngoc.jpg', 800.00, 400.00, N'GOLD', N'ACTIVE');

INSERT INTO phieu_giam_gia (ten_khuyen_mai, loai_khuyen_mai, gia_tri_khuyen_mai, gia_tri_don_hang_toi_thieu, gia_tri_khuyen_mai_toi_da, ngay_bat_dau, ngay_ket_thuc, dieu_kien_ap_dung, hang_toi_thieu, so_luong, so_diem_can_de_doi, is_global, trang_thai)
VALUES
    (N'Giảm giá iPhone 10%', N'Phần trăm', 10.00, 1000000.00, 200000.00, '2025-05-01', '2025-06-01', N'Áp dụng cho đơn iPhone từ 1 triệu', N'SILVER', 100, 100.00, 1, N'ACTIVE'),
    (N'Giảm 200K iPhone', N'Cố định', 200000.00, 2000000.00, 200000.00, '2025-04-15', '2025-05-15', N'Đơn iPhone từ 2 triệu', N'GOLD', 50, 200.00, 0, N'NOT_STARTED'),
    (N'Flash Sale iPhone 15%', N'Phần trăm', 15.00, 500000.00, 300000.00, '2025-06-01', '2025-06-07', N'Flash sale iPhone cuối tuần', N'MEMBER', 200, 50.00, 1, N'NOT_STARTED'),
    (N'Giảm 500K iPhone VIP', N'Cố định', 500000.00, 5000000.00, 500000.00, '2025-03-01', '2025-04-01', N'Đơn iPhone từ 5 triệu', N'DIAMOND', 20, 500.00, 0, N'EXPIRED'),
    (N'Giảm 5% iPhone 16', N'Phần trăm', 5.00, 1000000.00, 100000.00, '2025-05-10', '2025-05-20', N'Áp dụng iPhone 16', N'GOLD', 150, 80.00, 1, N'EXPIRED');

INSERT INTO hoa_don  (
    id_khach_hang, id_phieu_giam_gia,
    ten_nguoi_mua, sdt_nguoi_mua,
    ten_nguoi_nhan, dia_chi_giao_hang, ngay_dat_hang, trang_thai_don_hang,
    phi_ship, tong_tien, so_tien_giam, thanh_tien,
    ngay_tao_hoa_don, loai_hoa_don , ngay_thanh_toan, trang_thai_thanh_toan,ma_van_don,sdt_nguoi_nhan
) VALUES
      (1, NULL, N'Nguyễn Văn A', '0911111111', N'Lê Thị B', N'123 ABC', GETDATE(), N'PENDING', 20000, 5400000, 0, 5420000, GETDATE(), N'ONLINE', NULL, N'PENDING','VD001','0911111111'),
      (2, 1, N'Trần Thị C', '0922222222', N'Trần Thị C', N'456 DEF', GETDATE(), N'SHIPPING', 25000, 7200000, 200000, 7030000, GETDATE(), N'POS', GETDATE(), N'PAID','VD001','0922222222'),
      (3, NULL, N'Lê Văn D', '0933333333', N'Lê Văn D', N'789 GHI', GETDATE(), N'DELIVERED', 30000, 3500000, 0, 3530000, GETDATE(), N'ONLINE', GETDATE(), N'COMPLETED','VD001','0933333333');


INSERT INTO lich_su_hoa_don (id_nhan_vien, id_hoa_don , hanh_dong, thoi_gian_thay_doi, mo_ta)
VALUES
    (1, 1, N'Tạo hóa đơn', '2025-05-02', N'Khách hàng đặt iPhone 16'),
    (2, 2, N'Thanh toán hóa đơn', '2025-04-20', N'Thanh toán iPhone 15 Pro tại cửa hàng'),
    (3, 3, N'Xác nhận đơn hàng', '2025-05-01', N'Đơn iPhone 14 chờ thanh toán')

    INSERT INTO khach_hang_giam_gia (id_khach_hang, id_phieu_giam_gia, is_user, ngay_cap)
VALUES
    (1, 1, 1, '2025-05-01'),
    (2, 2, 0, '2025-04-15'),
    (3, 3, 0, '2025-06-01'),
    (4, 4, 1, '2025-03-01'),
    (5, 5, 1, '2025-05-10');

INSERT INTO dia_chi (id_khach_hang, ten_nguoi_nhan, sdt_nguoi_nhan, so_nha, ten_duong, xa_phuong, quan_huyen, tinh_thanh_pho, dia_chi_chinh)
VALUES
    (1, N'Nguyễn Thị Hoa', '0998938493', N'123', N'Đường Láng', N'Đống Đa', N'Đống Đa', N'Hà Nội', 1),
    (1, N'Nguyễn Văn Hoe', '0998938412', N'1234', N'Đường Bắc Nam', N'Ngọc Liệp', N'Quốc Oai', N'Hà Nội', 0),
    (2, N'Trần Văn Hùng', '0128473827', N'456', N'Nguyễn Huệ', N'Quận 1', N'Quận 1', N'TP.HCM', 1),
    (3, N'Lê Thị Lan', '0938462736', N'789', N'Trần Phú', N'Hai Châu', N'Hai Châu', N'Đà Nẵng', 1),
    (4, N'Phạm Văn Minh', '0293123321', N'101', N'Lê Lợi', N'Thừa Thiên', N'Thừa Thiên', N'Huế', 1),
    (5, N'Hoàng Thị Ngọc', '0998293041', N'202', N'Phạm Văn Đồng', N'Cầu Giấy', N'Cầu Giấy', N'Hà Nội', 1);

INSERT INTO gio_hang (id_khach_hang)
VALUES
    (1),
    (2),
    (3),
    (4),
    (5);

INSERT INTO camera_sau (loai_camera, do_phan_giai, khau_do, loai_zoom, che_do_chup)
VALUES
    (N'Wide', N'48MP', N'f/1.6', N'Optical 2x', N'Night Mode'),
    (N'Ultra Wide', N'12MP', N'f/2.4', N'Digital', N'Panorama'),
    (N'Telephoto', N'12MP', N'f/2.8', N'Optical 3x', N'Portrait'),
    (N'Macro', N'12MP', N'f/2.4', N'Digital', N'Macro Mode'),
    (N'Periscope', N'12MP', N'f/3.4', N'Optical 5x', N'ProRAW');

INSERT INTO camera_truoc (loai_camera, do_phan_giai, khau_do, loai_zoom, che_do_chup)
VALUES
    (N'TrueDepth', N'12MP', N'f/1.9', N'Digital', N'Face ID'),
    (N'Wide', N'12MP', N'f/2.2', N'Digital', N'Portrait'),
    (N'Selfie', N'12MP', N'f/2.0', N'Digital', N'Animoji'),
    (N'FaceTime', N'12MP', N'f/2.2', N'Digital', N'Center Stage'),
    (N'Portrait', N'12MP', N'f/1.9', N'Digital', N'Night Mode');

INSERT INTO cpu (hang_san_xuat, so_nhan, so_luong, xung_nhip, cong_nghe_san_xuat, bo_nho_dem, tieu_thu_dien_nang, nam_ra_mat)
VALUES
    (N'Apple', N'6 nhân', 6, N'3.46 GHz', N'3nm', N'8MB', N'15W', '2024-09-01'),
    (N'Apple', N'6 nhân', 6, N'3.7 GHz', N'3nm', N'12MB', N'15W', '2023-09-01'),
    (N'Apple', N'6 nhân', 6, N'3.2 GHz', N'4nm', N'8MB', N'15W', '2022-09-01'),
    (N'Apple', N'6 nhân', 6, N'3.2 GHz', N'5nm', N'8MB', N'15W', '2021-09-01'),
    (N'Apple', N'6 nhân', 6, N'3.0 GHz', N'5nm', N'8MB', N'15W', '2020-09-01');

INSERT INTO loai (ten_loai)
VALUES
    (N'Thường'),
    (N'Plus'),
    (N'Pro'),
    (N'Pro Max'),
    (N'Mini');

INSERT INTO xuat_xu (ma_xuat_xu, ten_quoc_gia)
VALUES
    ('VNA', N'Việt Nam'),
    ('LLA', N'Mỹ Latinh'),
    ('JPA', N'Nhật Bản'),
    ('CHA', N'Trung Quốc'),
    ('KRA', N'Hàn Quốc');

INSERT INTO pin (phien_ban, cong_suat_sac, thoi_gian_su_dung, so_lan_sac_toi_da)
VALUES
    (N'Li-Ion 4000mAh', N'25W', N'20 giờ', N'1000 lần'),
    (N'Li-Ion 4500mAh', N'30W', N'22 giờ', N'1200 lần'),
    (N'Li-Ion 3500mAh', N'20W', N'18 giờ', N'800 lần'),
    (N'Li-Ion 5000mAh', N'45W', N'25 giờ', N'1500 lần'),
    (N'Li-Ion 4200mAh', N'27W', N'21 giờ', N'1100 lần');

INSERT INTO he_dieu_hanh (phien_ban, nha_phat_trien, giao_dien_nguoi_dung)
VALUES
    (N'iOS 18', N'Apple', N'iOS UI'),
    (N'iOS 17', N'Apple', N'iOS UI'),
    (N'iOS 16', N'Apple', N'iOS UI'),
    (N'iOS 15', N'Apple', N'iOS UI'),
    (N'iOS 14', N'Apple', N'iOS UI');

INSERT INTO man_hinh (ten_man_hinh, kich_thuoc, loai_man_hinh, do_phan_giai, tan_so_quet, do_sang, chat_lieu_kinh)
VALUES
    (N'Super Retina XDR', N'6.1 inch', N'OLED', N'2532x1170', N'120Hz', N'2000 nit', N'Ceramic Shield'),
    (N'Super Retina XDR', N'6.7 inch', N'OLED', N'2796x1290', N'120Hz', N'2000 nit', N'Ceramic Shield'),
    (N'Liquid Retina', N'6.1 inch', N'LCD', N'1792x828', N'60Hz', N'625 nit', N'Ceramic Shield'),
    (N'Super Retina', N'5.4 inch', N'OLED', N'2340x1080', N'60Hz', N'1200 nit', N'Ceramic Shield'),
    (N'ProMotion XDR', N'6.7 inch', N'OLED', N'2796x1290', N'120Hz', N'2000 nit', N'Ceramic Shield');

INSERT INTO rom (dung_luong_rom, loai_rom, toc_do_doc_ghi, nha_san_xuat, nam_ra_mat)
VALUES
    (N'128GB', N'NVMe', N'3500 MB/s', N'Apple', '2023-09-01'),
    (N'256GB', N'NVMe', N'4000 MB/s', N'Apple', '2023-09-01'),
    (N'512GB', N'NVMe', N'4000 MB/s', N'Apple', '2023-09-01'),
    (N'1TB', N'NVMe', N'5000 MB/s', N'Apple', '2023-09-01'),
    (N'64GB', N'NVMe', N'3000 MB/s', N'Apple', '2021-09-01');

INSERT INTO ram (dung_luong_ram, loai_ram, toc_do_doc_ghi, nha_san_xuat, nam_ra_mat)
VALUES
    (N'6GB', N'LPDDR5', N'6400 MB/s', N'Apple', '2023-09-01'),
    (N'8GB', N'LPDDR5', N'6400 MB/s', N'Apple', '2023-09-01'),
    (N'4GB', N'LPDDR4X', N'4266 MB/s', N'Apple', '2021-09-01'),
    (N'12GB', N'LPDDR5', N'7200 MB/s', N'Apple', '2023-09-01'),
    (N'16GB', N'LPDDR5X', N'8500 MB/s', N'Apple', '2023-09-01');

INSERT INTO mau_sac (ten_mau, hex_color)
VALUES
    (N'Đen Phantôm', '#1C2526'),
    (N'Trắng Ngọc Trai', '#F5F6F5'),
    (N'Vàng Ánh Kim', '#D4A017'),
    (N'Xanh Biển Sâu', '#0B3D91'),
    (N'Hồng Phấn', '#F8B7CD');

INSERT INTO nha_cung_cap (ten_nha_cung_cap, dia_chi, sdt, email)
VALUES
    (N'VNA Trading', N'123 Nguyễn Trãi, Hà Nội', '0901234567', 'vna@example.com'),
    (N'Thế Giới Di Động', N'456 Lê Văn Sỹ, TP.HCM', '0912345678', 'tgdd@example.com'),
    (N'FPT Shop', N'789 Trần Hưng Đạo, Đà Nẵng', '0923456789', 'fpt@example.com'),
    (N'CellphoneS', N'101 Hai Bà Trưng, Huế', '0934567890', 'cellphones@example.com'),
    (N'Hoàng Hà Mobile', N'202 Lý Thường Kiệt, Hà Nội', '0945678901', 'hoangha@example.com');

INSERT INTO san_pham (ten_san_pham, thuong_hieu, id_nha_cung_cap, trang_thai)
VALUES
    (N'iPhone 16', N'Apple', 1, 'ACTIVE'),
    (N'iPhone 16 Pro', N'Apple', 2, 'DISCONTINUED'),
    (N'iPhone 15', N'Apple', 3, 'COMING_SOON'),
    (N'iPhone 14', N'Apple', 4, 'TEMPORARILY_UNAVAILABLE'),
    (N'iPhone 13', N'Apple', 5, 'OUT_OF_STOCK');

INSERT INTO san_pham_chi_tiet (id_san_pham, id_mau, id_ram, id_rom, id_man_hinh, id_he_dieu_hanh, id_pin, id_cpu, id_camera_truoc, id_camera_sau, id_xuat_xu, id_loai, so_luong, gia_ban)
VALUES
    (1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 10, 20000000.00),
    (2, 2, 2, 2, 2, 1, 2, 2, 2, 2, 2, 3, 8, 25000000.00),
    (3, 3, 3, 3, 3, 2, 3, 3, 3, 3, 3, 1, 15, 15000000.00),
    (4, 4, 4, 4, 4, 3, 4, 4, 4, 4, 4, 1, 12, 12000000.00),
    (5, 5, 5, 5, 5, 4, 5, 5, 5, 5, 5, 1, 20, 10000000.00);

INSERT INTO chi_tiet_hoa_don (id_hoa_don , id_san_pham_chi_tiet,ten_san_pham, mo_ta, so_luong, don_gia) VALUES
                                                                                                            (1, 1, N'iPhone 16', N'Ip16', 1, 5000000),
                                                                                                            (1, 2, N'iPhone 16 Pro', N'Ip16Pro', 2, 150000),
                                                                                                            (1, 3, N'iPhone 15', N'ip15', 1, 350000);
INSERT INTO loai_bao_hanh (ten_loai_bao_hanh, thoi_gian_thang, mo_ta)
VALUES
    (N'Bảo hành chính hãng', 12, N'Bảo hành từ nhà sản xuất'),
    (N'Bảo hành của cửa hàng', 6, N'Bảo hành nội bộ của cửa hàng'),
    (N'Bảo hành đổi trả', 1, N'Cho phép đổi trả trong vòng 1 tháng');

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

INSERT INTO imei (id_san_pham_chi_tiet, so_imei, trang_thai_imei, nha_mang)
VALUES
    (1, '123456789012345', N'AVAILABLE', N'Viettel'),
    (2, '123456789012346', N'AVAILABLE', N'Mobifone'),
    (3, '123456789012347', N'AVAILABLE', N'Vinaphone'),
    (4, '123456789012348', N'AVAILABLE', N'Viettel'),
    (5, '123456789012349', N'AVAILABLE', N'Mobifone');

INSERT INTO hinh_anh (id_san_pham_chi_tiet, url, image_public_id)
VALUES
    (1, 'https://res.cloudinary.com/dzs764s5c/image/upload/v1746884362/11pro-xanh_dpkrnp.webp', '11pro_xanh'),
    (2, 'https://res.cloudinary.com/dzs764s5c/image/upload/v1746884362/11pro-xanh_dpkrnp.webp', '11pro_xanh'),
    (3, 'https://res.cloudinary.com/dzs764s5c/image/upload/v1746884362/11pro-xanh_dpkrnp.webp', '11pro_xanh'),
    (4, 'https://res.cloudinary.com/dzs764s5c/image/upload/v1746884362/11pro-xanh_dpkrnp.webp', '11pro_xanh'),
    (5, 'https://res.cloudinary.com/dzs764s5c/image/upload/v1746884362/iphone11-tr%E1%BA%AFng_ovvdt8.webp', '11 thường_trắng');

INSERT INTO gio_hang_chi_tiet (id_gio_hang, id_san_pham_chi_tiet, so_luong, gia, ngay_them)
VALUES
    (1, 1, 2, 20000000.00, '2025-05-05'),
    (2, 2, 1, 25000000.00, '2025-05-05'),
    (2, 3, 1, 15000000.00, '2025-05-05');


INSERT INTO imei_da_ban (id_chi_tiet_hoa_don , so_imei, trang_thai)
VALUES
    (1, '123456789012345', N'SOLD'),
    (2, '123456789012346', N'SOLD'),
    (3, '123456789012347', N'SOLD')


    INSERT INTO phuong_thuc_thanh_toan (ten_phuong_thuc, loai_hinh_thuc)
VALUES
    (N'Thẻ tín dụng', N'Trực tuyến'),
    (N'Chuyển khoản ngân hàng', N'Trực tuyến'),
    (N'Tiền mặt', N'Tại cửa hàng'),
    (N'Ví Momo', N'Trực tuyến'),
    (N'Thanh toán khi nhận hàng', N'Tại điểm giao');

INSERT INTO chi_tiet_thanh_toan (id_hoa_don , id_phuong_thuc_thanh_toan, so_tien_thanh_toan)
VALUES
    (1, 1, 19830000.00),
    (2, 3, 14840000.00),
    (3, 2, 24535000.00)

-- 1. nhan_vien
SELECT * FROM nhan_vien;

-- 2. khach_hang
SELECT * FROM khach_hang;

-- 3. phieu_giam_gia
SELECT * FROM phieu_giam_gia;

-- 4. hoa_don
SELECT * FROM hoa_don ;

-- 5. lich_su_hoa_don
SELECT * FROM lich_su_hoa_don ;

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
SELECT * FROM chi_tiet_hoa_don ;

-- 28. imei_da_ban
SELECT * FROM imei_da_ban;

-- 31. phuong_thuc_thanh_toan
SELECT * FROM phuong_thuc_thanh_toan;

-- 32. chi_tiet_thanh_toan
SELECT * FROM chi_tiet_thanh_toan;

--33. loai_bao_hanh
SELECT * FROM loai_bao_hanh

--34. lich_su_bao_hanh
SELECT * FROM lich_su_bao_hanh