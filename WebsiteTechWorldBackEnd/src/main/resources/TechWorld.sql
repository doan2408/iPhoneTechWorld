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
                         ngay_tao_don_hang DATE, -- new
                         loai_hoa_don NVARCHAR(50),
                         ngay_thanh_toan DATE,
                         trang_thai_thanh_toan NVARCHAR(50),
                         is_delete BIT,
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
                     chip_xu_ly NVARCHAR(50),
                     hang_san_xuat NVARCHAR(50),
                     so_nhan NVARCHAR(50),
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
                      so_imei_2 VARCHAR(70), -- new
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

-- Table nhan_vien
INSERT INTO nhan_vien (ten_nhan_vien, tai_khoan, mat_khau, email, sdt, dia_chi, trang_thai, chuc_vu, gioi_tinh, ngay_sinh)
VALUES
    (N'Nguyễn Văn An', 'nv_an', '$2a$10$mQLhyl17N446ZOSUjzzRqOTkQ9q/PAaI9omLyfs82fHeJWdpzkutu', 'an.nv@example.com', '0901234567', N'123 Đường Láng, Hà Nội', N'ENABLE', N'ADMIN', 1, '1990-05-15'),
    (N'Trần Thị Bình', 'nv_binh', '$2a$10$mQLhyl17N446ZOSUjzzRqOTkQ9q/PAaI9omLyfs82fHeJWdpzkutu', 'binh.tt@example.com', '0912345678', N'456 Nguyễn Huệ, TP.HCM', N'ENABLE', N'STAFF', 0, '1995-08-20'),
    (N'Lê Văn Cường', 'nv_cuong', '$2a$10$mQLhyl17N446ZOSUjzzRqOTkQ9q/PAaI9omLyfs82fHeJWdpzkutu', 'cuong.lv@example.com', '0923456789', N'789 Trần Phú, Đà Nẵng', N'ENABLE', N'STAFF', 1, '1992-03-10'),
    (N'Phạm Thị Duyên', 'nv_duyen', '$2a$10$mQLhyl17N446ZOSUjzzRqOTkQ9q/PAaI9omLyfs82fHeJWdpzkutu', 'duyen.pt@example.com', '0934567890', N'101 Lê Lợi, Huế', N'DISABLE', N'STAFF', 0, '1998-11-25'),
    (N'Hoàng Văn Em', 'nv_em', '$2a$10$mQLhyl17N446ZOSUjzzRqOTkQ9q/PAaI9omLyfs82fHeJWdpzkutu', 'em.hv@example.com', '0945678901', N'202 Phạm Văn Đồng, Hà Nội', N'DISABLE', N'ADMIN', 1, '1988-07-30'),
    (N'Nguyễn Thị Mai', 'nv_mai', '$2a$10$mQLhyl17N446ZOSUjzzRqOTkQ9q/PAaI9omLyfs82fHeJWdpzkutu', 'mai.nt@example.com', '0956789012', N'303 Bạch Mai, Hà Nội', N'ENABLE', N'STAFF', 0, '1993-02-14'),
    (N'Trần Văn Hưng', 'nv_hung', '$2a$10$mQLhyl17N446ZOSUjzzRqOTkQ9q/PAaI9omLyfs82fHeJWdpzkutu', 'hung.tv@example.com', '0967890123', N'555 Nguyễn Trãi, TP.HCM', N'ENABLE', N'ADMIN', 1, '1990-09-09'),
    (N'Lê Thị Hồng', 'nv_hong', '$2a$10$mQLhyl17N446ZOSUjzzRqOTkQ9q/PAaI9omLyfs82fHeJWdpzkutu', 'hong.lt@example.com', '0978901234', N'777 Lê Đại Hành, Đà Nẵng', N'ENABLE', N'STAFF', 0, '1996-12-01'),
    (N'Phạm Văn Khang', 'nv_khang', '$2a$10$mQLhyl17N446ZOSUjzzRqOTkQ9q/PAaI9omLyfs82fHeJWdpzkutu', 'khang.pv@example.com', '0989012345', N'999 Hai Bà Trưng, Huế', N'DISABLE', N'STAFF', 1, '1991-04-22'),
    (N'Hoàng Thị Lan', 'nv_lan', '$2a$10$mQLhyl17N446ZOSUjzzRqOTkQ9q/PAaI9omLyfs82fHeJWdpzkutu', 'lan.ht@example.com', '0990123456', N'111 Phạm Ngũ Lão, Hà Nội', N'ENABLE', N'ADMIN', 0, '1989-06-18'),
    (N'Nguyễn Văn Nam', 'nv_nam', '$2a$10$mQLhyl17N446ZOSUjzzRqOTkQ9q/PAaI9omLyfs82fHeJWdpzkutu', 'nam.nv@example.com', '0902345678', N'222 Lê Văn Sỹ, TP.HCM', N'ENABLE', N'STAFF', 1, '1994-03-30'),
    (N'Trần Thị Ngọc', 'nv_ngoc', '$2a$10$mQLhyl17N446ZOSUjzzRqOTkQ9q/PAaI9omLyfs82fHeJWdpzkutu', 'ngoc.tt@example.com', '0913456789', N'333 Trần Hưng Đạo, Đà Nẵng', N'DISABLE', N'STAFF', 0, '1997-07-07'),
    (N'Lê Văn Phong', 'nv_phong', '$2a$10$mQLhyl17N446ZOSUjzzRqOTkQ9q/PAaI9omLyfs82fHeJWdpzkutu', 'phong.lv@example.com', '0924567890', N'444 Lý Thường Kiệt, Huế', N'ENABLE', N'ADMIN', 1, '1990-10-10'),
    (N'Phạm Thị Quỳnh', 'nv_quynh', '$2a$10$mQLhyl17N446ZOSUjzzRqOTkQ9q/PAaI9omLyfs82fHeJWdpzkutu', 'quynh.pt@example.com', '0935678901', N'555 Nguyễn Đình Chiểu, Hà Nội', N'ENABLE', N'STAFF', 0, '1995-01-25'),
    (N'Hoàng Văn Sơn', 'nv_son', '$2a$10$mQLhyl17N446ZOSUjzzRqOTkQ9q/PAaI9omLyfs82fHeJWdpzkutu', 'son.hv@example.com', '0946789012', N'666 Lê Lai, TP.HCM', N'DISABLE', N'ADMIN', 1, '1987-11-11');

-- Table khach_hang
INSERT INTO khach_hang (ten_khach_hang, sdt, tai_khoan, mat_khau, email, ngay_sinh, gioi_tinh, anh, tong_diem, so_diem_hien_tai, hang_khach_hang, trang_thai)
VALUES
    (N'Nguyễn Thị Hoa', '0987654321', N'hoa_nt', '$2a$10$mQLhyl17N446ZOSUjzzRqOTkQ9q/PAaI9omLyfs82fHeJWdpzkutu', 'hoa.nt@example.com', '1993-04-12', 0, 'hoa.jpg', 1000.00, 500.00, N'GOLD', N'ACTIVE'),
    (N'Trần Văn Hùng', '0976543210', N'hung_tv', '$2a$10$mQLhyl17N446ZOSUjzzRqOTkQ9q/PAaI9omLyfs82fHeJWdpzkutu', 'hung.tv@example.com', '1985-09-25', 1, 'hung.jpg', 500.00, 200.00, N'SILVER', N'ACTIVE'),
    (N'Lê Thị Lan', '0965432109', N'lan_lt', '$2a$10$mQLhyl17N446ZOSUjzzRqOTkQ9q/PAaI9omLyfs82fHeJWdpzkutu', 'lan.lt@example.com', '1997-12-30', 0, 'lan.jpg', 200.00, 100.00, N'MEMBER', N'INACTIVE'),
    (N'Phạm Văn Minh', '0954321098', N'minh_pv', '$2a$10$mQLhyl17N446ZOSUjzzRqOTkQ9q/PAaI9omLyfs82fHeJWdpzkutu', 'minh.pv@example.com', '1990-06-18', 1, 'minh.jpg', 3000.00, 1500.00, N'DIAMOND', N'ACTIVE'),
    (N'Hoàng Thị Ngọc', '0943210987', N'ngoc_ht', '$2a$10$mQLhyl17N446ZOSUjzzRqOTkQ9q/PAaI9omLyfs82fHeJWdpzkutu', 'ngoc.ht@example.com', '1995-03-05', 0, 'ngoc.jpg', 800.00, 400.00, N'GOLD', N'ACTIVE'),
    (N'Nguyễn Văn Bình', '0932109876', N'binh_nv', '$2a$10$mQLhyl17N446ZOSUjzzRqOTkQ9q/PAaI9omLyfs82fHeJWdpzkutu', 'binh.nv@example.com', '1988-08-08', 1, 'binh.jpg', 1200.00, 600.00, N'GOLD', N'ACTIVE'),
    (N'Trần Thị Mai', '0921098765', N'mai_tt', '$2a$10$mQLhyl17N446ZOSUjzzRqOTkQ9q/PAaI9omLyfs82fHeJWdpzkutu', 'mai.tt@example.com', '1992-02-14', 0, 'mai.jpg', 400.00, 200.00, N'SILVER', N'INACTIVE'),
    (N'Lê Văn Nam', '0910987654', N'nam_lv', '$2a$10$mQLhyl17N446ZOSUjzzRqOTkQ9q/PAaI9omLyfs82fHeJWdpzkutu', 'nam.lv@example.com', '1990-11-11', 1, 'nam.jpg', 2500.00, 1250.00, N'DIAMOND', N'ACTIVE'),
    (N'Phạm Thị Hồng', '0909876543', N'hong_pt', '$2a$10$mQLhyl17N446ZOSUjzzRqOTkQ9q/PAaI9omLyfs82fHeJWdpzkutu', 'hong.pt@example.com', '1996-06-20', 0, 'hong.jpg', 600.00, 300.00, N'SILVER', N'ACTIVE'),
    (N'Hoàng Văn Phong', '0998765432', N'phong_hv', '$2a$10$mQLhyl17N446ZOSUjzzRqOTkQ9q/PAaI9omLyfs82fHeJWdpzkutu', 'phong.hv@example.com', '1987-03-15', 1, 'phong.jpg', 1500.00, 750.00, N'GOLD', N'ACTIVE'),
    (N'Nguyễn Thị Quỳnh', '0987654320', N'quynh_nt', '$2a$10$mQLhyl17N446ZOSUjzzRqOTkQ9q/PAaI9omLyfs82fHeJWdpzkutu', 'quynh.nt@example.com', '1994-09-09', 0, 'quynh.jpg', 300.00, 150.00, N'MEMBER', N'INACTIVE'),
    (N'Trần Văn Sơn', '0976543219', N'son_tv', '$2a$10$mQLhyl17N446ZOSUjzzRqOTkQ9q/PAaI9omLyfs82fHeJWdpzkutu', 'son.tv@example.com', '1989-12-12', 1, 'son.jpg', 2000.00, 1000.00, N'DIAMOND', N'ACTIVE'),
    (N'Lê Thị Thảo', '0965432108', N'thao_lt', '$2a$10$mQLhyl17N446ZOSUjzzRqOTkQ9q/PAaI9omLyfs82fHeJWdpzkutu', 'thao.lt@example.com', '1993-07-07', 0, 'thao.jpg', 700.00, 350.00, N'SILVER', N'ACTIVE'),
    (N'Phạm Văn Tâm', '0954321097', N'tam_pv', '$2a$10$mQLhyl17N446ZOSUjzzRqOTkQ9q/PAaI9omLyfs82fHeJWdpzkutu', 'tam.pv@example.com', '1991-04-04', 1, 'tam.jpg', 1800.00, 900.00, N'GOLD', N'ACTIVE'),
    (N'Hoàng Thị Vân', '0943210986', N'van_ht', '$2a$10$mQLhyl17N446ZOSUjzzRqOTkQ9q/PAaI9omLyfs82fHeJWdpzkutu', 'van.ht@example.com', '1995-10-10', 0, 'van.jpg', 900.00, 450.00, N'GOLD', N'ACTIVE');

-- Table phieu_giam_gia
INSERT INTO phieu_giam_gia (ten_khuyen_mai, loai_khuyen_mai, gia_tri_khuyen_mai, gia_tri_don_hang_toi_thieu, gia_tri_khuyen_mai_toi_da, ngay_bat_dau, ngay_ket_thuc, dieu_kien_ap_dung, hang_toi_thieu, so_luong, so_diem_can_de_doi, is_global, trang_thai)
VALUES
    (N'Giảm giá iPhone 10%', N'Phần trăm', 10.00, 1000000.00, 200000.00, '2025-05-01', '2025-06-01', N'Áp dụng cho đơn iPhone từ 1 triệu', N'SILVER', 100, 100.00, 1, N'ACTIVE'),
    (N'Giảm 200K iPhone', N'Cố định', 200000.00, 2000000.00, 200000.00, '2025-04-15', '2025-05-15', N'Đơn iPhone từ 2 triệu', N'GOLD', 50, 200.00, 0, N'NOT_STARTED'),
    (N'Flash Sale iPhone 15%', N'Phần trăm', 15.00, 500000.00, 300000.00, '2025-06-01', '2025-06-07', N'Flash sale iPhone cuối tuần', N'MEMBER', 200, 50.00, 1, N'NOT_STARTED'),
    (N'Giảm 500K iPhone VIP', N'Cố định', 500000.00, 5000000.00, 500000.00, '2025-03-01', '2025-04-01', N'Đơn iPhone từ 5 triệu', N'DIAMOND', 20, 500.00, 0, N'EXPIRED'),
    (N'Giảm 5% iPhone 16', N'Phần trăm', 5.00, 1000000.00, 100000.00, '2025-05-10', '2025-05-20', N'Áp dụng iPhone 16', N'GOLD', 150, 80.00, 1, N'EXPIRED'),
    (N'Giảm giá iPhone 12%', N'Phần trăm', 12.00, 1500000.00, 250000.00, '2025-06-15', '2025-07-15', N'Áp dụng cho đơn iPhone từ 1.5 triệu', N'SILVER', 120, 120.00, 1, N'NOT_STARTED'),
    (N'Giảm 300K iPhone Pro', N'Cố định', 300000.00, 3000000.00, 300000.00, '2025-05-20', '2025-06-20', N'Đơn iPhone Pro từ 3 triệu', N'GOLD', 60, 250.00, 0, N'ACTIVE'),
    (N'Flash Sale iPhone 20%', N'Phần trăm', 20.00, 800000.00, 400000.00, '2025-07-01', '2025-07-07', N'Flash sale iPhone hàng tuần', N'MEMBER', 250, 60.00, 1, N'NOT_STARTED'),
    (N'Giảm 600K iPhone VIP', N'Cố định', 600000.00, 6000000.00, 600000.00, '2025-04-01', '2025-05-01', N'Đơn iPhone từ 6 triệu', N'DIAMOND', 25, 600.00, 0, N'EXPIRED'),
    (N'Giảm 8% iPhone 15', N'Phần trăm', 8.00, 1200000.00, 150000.00, '2025-06-10', '2025-06-20', N'Áp dụng iPhone 15', N'GOLD', 180, 90.00, 1, N'NOT_STARTED'),
    (N'Giảm giá iPhone 7%', N'Phần trăm', 7.00, 1100000.00, 130000.00, '2025-05-15', '2025-06-15', N'Áp dụng cho đơn iPhone từ 1.1 triệu', N'SILVER', 110, 110.00, 1, N'ACTIVE'),
    (N'Giảm 250K iPhone', N'Cố định', 250000.00, 2500000.00, 250000.00, '2025-04-20', '2025-05-20', N'Đơn iPhone từ 2.5 triệu', N'GOLD', 55, 220.00, 0, N'ACTIVE'),
    (N'Flash Sale iPhone 10%', N'Phần trăm', 10.00, 600000.00, 200000.00, '2025-07-15', '2025-07-22', N'Flash sale iPhone hàng tuần', N'MEMBER', 220, 55.00, 1, N'NOT_STARTED'),
    (N'Giảm 700K iPhone VIP', N'Cố định', 700000.00, 7000000.00, 700000.00, '2025-03-15', '2025-04-15', N'Đơn iPhone từ 7 triệu', N'DIAMOND', 30, 700.00, 0, N'EXPIRED'),
    (N'Giảm 6% iPhone 14', N'Phần trăm', 6.00, 1300000.00, 120000.00, '2025-06-20', '2025-06-30', N'Áp dụng iPhone 14', N'GOLD', 160, 85.00, 1, N'NOT_STARTED');

-- Table hoa_don
INSERT INTO hoa_don (id_khach_hang, id_phieu_giam_gia, ten_nguoi_mua, sdt_nguoi_mua, ten_nguoi_nhan, dia_chi_giao_hang, ngay_dat_hang, trang_thai_don_hang, phi_ship, tong_tien, so_tien_giam, thanh_tien, ngay_tao_hoa_don, loai_hoa_don, ngay_thanh_toan, trang_thai_thanh_toan, ma_van_don, sdt_nguoi_nhan,is_delete)
VALUES
    (1, NULL, N'Nguyễn Văn A', '0911111111', N'Lê Thị B', N'123 ABC', GETDATE(), N'PENDING', 20000, 5400000, 0, 5420000, GETDATE(), N'ONLINE', NULL, N'PENDING', 'VD001', '0911111111',0),
    (2, 1, N'Trần Thị C', '0922222222', N'Trần Thị C', N'456 DEF', GETDATE(), N'SHIPPING', 25000, 7200000, 200000, 7030000, GETDATE(), N'POS', GETDATE(), N'PAID', 'VD002', '0922222222',0),
    (3, NULL, N'Lê Văn D', '0933333333', N'Lê Văn D', N'789 GHI', GETDATE(), N'DELIVERED', 30000, 3500000, 0, 3530000, GETDATE(), N'ONLINE', GETDATE(), N'COMPLETED', 'VD003', '0933333333',0),
    (4, 2, N'Phạm Văn E', '0944444444', N'Phạm Văn E', N'101 JKL', GETDATE(), N'PENDING', 20000, 6000000, 300000, 5720000, GETDATE(), N'ONLINE', NULL, N'PENDING', 'VD004', '0944444444',0),
    (5, 3, N'Hoàng Thị F', '0955555555', N'Hoàng Thị F', N'202 MNO', GETDATE(), N'SHIPPING', 25000, 8000000, 400000, 7625000, GETDATE(), N'POS', GETDATE(), N'PAID', 'VD005', '0955555555',0),
    (1, 4, N'Nguyễn Văn G', '0966666666', N'Nguyễn Văn G', N'303 PQR', GETDATE(), N'DELIVERED', 30000, 4500000, 500000, 4030000, GETDATE(), N'ONLINE', GETDATE(), N'COMPLETED', 'VD006', '0966666666',0),
    (2, NULL, N'Trần Thị H', '0977777777', N'Trần Thị H', N'456 STU', GETDATE(), N'PENDING', 20000, 5500000, 0, 5520000, GETDATE(), N'ONLINE', NULL, N'PENDING', 'VD007', '0977777777',0),
    (3, 5, N'Lê Văn I', '0988888888', N'Lê Văn I', N'789 VWX', GETDATE(), N'SHIPPING', 25000, 7000000, 100000, 6925000, GETDATE(), N'POS', GETDATE(), N'PAID', 'VD008', '0988888888',0),
    (4, NULL, N'Phạm Thị K', '0999999999', N'Phạm Thị K', N'101 YZA', GETDATE(), N'DELIVERED', 30000, 4000000, 0, 4030000, GETDATE(), N'ONLINE', GETDATE(), N'COMPLETED', 'VD009', '0999999999',0),
    (5, 1, N'Hoàng Văn L', '0900000000', N'Hoàng Văn L', N'202 BCD', GETDATE(), N'PENDING', 20000, 6500000, 200000, 6320000, GETDATE(), N'ONLINE', NULL, N'PENDING', 'VD010', '0900000000',0),
    (1, 2, N'Nguyễn Thị M', '0911111112', N'Nguyễn Thị M', N'303 EFG', GETDATE(), N'SHIPPING', 25000, 7500000, 300000, 7225000, GETDATE(), N'POS', GETDATE(), N'PAID', 'VD011', '0911111112',0),
    (2, 3, N'Trần Văn N', '0922222223', N'Trần Văn N', N'456 HIJ', GETDATE(), N'DELIVERED', 30000, 5000000, 400000, 4630000, GETDATE(), N'ONLINE', GETDATE(), N'COMPLETED', 'VD012', '0922222223',0),
    (3, NULL, N'Lê Thị O', '0933333334', N'Lê Thị O', N'789 KLM', GETDATE(), N'PENDING', 20000, 6000000, 0, 6020000, GETDATE(), N'ONLINE', NULL, N'PENDING', 'VD013', '0933333334',0),
    (4, 4, N'Phạm Văn P', '0944444445', N'Phạm Văn P', N'101 NOP', GETDATE(), N'SHIPPING', 25000, 8000000, 500000, 7525000, GETDATE(), N'POS', GETDATE(), N'PAID', 'VD014', '0944444445',0),
    (5, 5, N'Hoàng Thị Q', '0955555556', N'Hoàng Thị Q', N'202 QRS', GETDATE(), N'DELIVERED', 30000, 4500000, 100000, 4430000, GETDATE(), N'ONLINE', GETDATE(), N'COMPLETED', 'VD015', '0955555556',0);

-- Table lich_su_hoa_don
INSERT INTO lich_su_hoa_don (id_nhan_vien, id_hoa_don, hanh_dong, thoi_gian_thay_doi, mo_ta)
VALUES
    (1, 1, N'Tạo hóa đơn', '2025-05-02', N'Khách hàng đặt iPhone 16'),
    (2, 2, N'Thanh toán hóa đơn', '2025-04-20', N'Thanh toán iPhone 15 Pro tại cửa hàng'),
    (3, 3, N'Xác nhận đơn hàng', '2025-05-01', N'Đơn iPhone 14 chờ thanh toán'),
    (4, 4, N'Tạo hóa đơn', '2025-05-03', N'Khách hàng đặt iPhone 16 Pro'),
    (5, 5, N'Thanh toán hóa đơn', '2025-04-21', N'Thanh toán iPhone 15 tại cửa hàng'),
    (1, 6, N'Xác nhận đơn hàng', '2025-05-02', N'Đơn iPhone 13 chờ thanh toán'),
    (2, 7, N'Tạo hóa đơn', '2025-05-04', N'Khách hàng đặt iPhone 16'),
    (3, 8, N'Thanh toán hóa đơn', '2025-04-22', N'Thanh toán iPhone 15 Pro tại cửa hàng'),
    (4, 9, N'Xác nhận đơn hàng', '2025-05-03', N'Đơn iPhone 14 chờ thanh toán'),
    (5, 10, N'Tạo hóa đơn', '2025-05-05', N'Khách hàng đặt iPhone 16 Pro'),
    (1, 11, N'Thanh toán hóa đơn', '2025-04-23', N'Thanh toán iPhone 15 tại cửa hàng'),
    (2, 12, N'Xác nhận đơn hàng', '2025-05-04', N'Đơn iPhone 13 chờ thanh toán'),
    (3, 13, N'Tạo hóa đơn', '2025-05-06', N'Khách hàng đặt iPhone 16'),
    (4, 14, N'Thanh toán hóa đơn', '2025-04-24', N'Thanh toán iPhone 15 Pro tại cửa hàng'),
    (5, 15, N'Xác nhận đơn hàng', '2025-05-05', N'Đơn iPhone 14 chờ thanh toán');

-- Table khach_hang_giam_gia
INSERT INTO khach_hang_giam_gia (id_khach_hang, id_phieu_giam_gia, is_user, ngay_cap)
VALUES
    (1, 1, 1, '2025-05-01'),
    (2, 2, 0, '2025-04-15'),
    (3, 3, 0, '2025-06-01'),
    (4, 4, 1, '2025-03-01'),
    (5, 5, 1, '2025-05-10'),
    (1, 6, 1, '2025-06-15'),
    (2, 7, 0, '2025-05-20'),
    (3, 8, 0, '2025-07-01'),
    (4, 9, 1, '2025-04-01'),
    (5, 10, 1, '2025-06-10'),
    (1, 11, 1, '2025-05-15'),
    (2, 12, 0, '2025-04-20'),
    (3, 13, 0, '2025-07-15'),
    (4, 14, 1, '2025-03-15'),
    (5, 15, 1, '2025-06-20');

-- Table dia_chi
INSERT INTO dia_chi (id_khach_hang, ten_nguoi_nhan, sdt_nguoi_nhan, so_nha, ten_duong, xa_phuong, quan_huyen, tinh_thanh_pho, dia_chi_chinh)
VALUES
    (1, N'Nguyễn Thị Hoa', '0998938493', N'123', N'Đường Láng', N'Đống Đa', N'Đống Đa', N'Hà Nội', 1),
    (1, N'Nguyễn Văn Hoe', '0998938412', N'1234', N'Đường Bắc Nam', N'Ngọc Liệp', N'Quốc Oai', N'Hà Nội', 0),
    (2, N'Trần Văn Hùng', '0128473827', N'456', N'Nguyễn Huệ', N'Quận 1', N'Quận 1', N'TP.HCM', 1),
    (3, N'Lê Thị Lan', '0938462736', N'789', N'Trần Phú', N'Hai Châu', N'Hai Châu', N'Đà Nẵng', 1),
    (4, N'Phạm Văn Minh', '0293123321', N'101', N'Lê Lợi', N'Thừa Thiên', N'Thừa Thiên', N'Huế', 1),
    (5, N'Hoàng Thị Ngọc', '0998293041', N'202', N'Phạm Văn Đồng', N'Cầu Giấy', N'Cầu Giấy', N'Hà Nội', 1),
    (1, N'Nguyễn Văn Tâm', '0998938494', N'555', N'Hoàn Kiếm', N'Hoàn Kiếm', N'Hoàn Kiếm', N'Hà Nội', 0),
    (2, N'Trần Thị Mai', '0128473828', N'666', N'Lý Thường Kiệt', N'Quận 3', N'Quận 3', N'TP.HCM', 0),
    (3, N'Lê Văn Phong', '0938462737', N'888', N'Hùng Vương', N'Thanh Khê', N'Thanh Khê', N'Đà Nẵng', 0),
    (4, N'Phạm Thị Quỳnh', '0293123322', N'222', N'Nguyễn Trãi', N'Phong Điền', N'Phong Điền', N'Huế', 0),
    (5, N'Hoàng Văn Sơn', '0998293042', N'333', N'Trần Duy Hưng', N'Cầu Giấy', N'Cầu Giấy', N'Hà Nội', 0),
    (1, N'Nguyễn Thị Vân', '0998938495', N'777', N'Giải Phóng', N'Hoàng Mai', N'Hoàng Mai', N'Hà Nội', 0),
    (2, N'Trần Văn Nam', '0128473829', N'999', N'Võ Thị Sáu', N'Quận 7', N'Quận 7', N'TP.HCM', 0),
    (3, N'Lê Thị Thảo', '0938462738', N'111', N'Lê Đại Hành', N'Liên Chiểu', N'Liên Chiểu', N'Đà Nẵng', 0),
    (4, N'Phạm Văn Bình', '0293123323', N'444', N'Hai Bà Trưng', N'Phú Lộc', N'Phú Lộc', N'Huế', 0);

-- Table gio_hang
INSERT INTO gio_hang (id_khach_hang)
VALUES
    (1),
    (2),
    (3),
    (4),
    (5),
    (6),
    (7),
    (8),
    (9),
    (10),
    (11),
    (12),
    (13),
    (14),
    (15);

-- Table camera_sau
INSERT INTO camera_sau (loai_camera, do_phan_giai, khau_do, loai_zoom, che_do_chup)
VALUES
    (N'Wide', N'48MP', N'f/1.6', N'Optical 2x', N'Night Mode'),
    (N'Ultra Wide', N'12MP', N'f/2.4', N'Digital', N'Panorama'),
    (N'Telephoto', N'12MP', N'f/2.8', N'Optical 3x', N'Portrait'),
    (N'Macro', N'12MP', N'f/2.4', N'Digital', N'Macro Mode'),
    (N'Periscope', N'12MP', N'f/3.4', N'Optical 5x', N'ProRAW'),
    (N'Wide', N'48MP', N'f/1.8', N'Optical 2x', N'Night Mode'),
    (N'Ultra Wide', N'12MP', N'f/2.2', N'Digital', N'Panorama'),
    (N'Telephoto', N'12MP', N'f/3.0', N'Optical 4x', N'Portrait'),
    (N'Macro', N'12MP', N'f/2.2', N'Digital', N'Macro Mode'),
    (N'Periscope', N'12MP', N'f/3.6', N'Optical 6x', N'ProRAW'),
    (N'Wide', N'48MP', N'f/1.7', N'Optical 2x', N'Night Mode'),
    (N'Ultra Wide', N'12MP', N'f/2.3', N'Digital', N'Panorama'),
    (N'Telephoto', N'12MP', N'f/2.9', N'Optical 3x', N'Portrait'),
    (N'Macro', N'12MP', N'f/2.3', N'Digital', N'Macro Mode'),
    (N'Periscope', N'12MP', N'f/3.5', N'Optical 5x', N'ProRAW');

-- Table camera_truoc
INSERT INTO camera_truoc (loai_camera, do_phan_giai, khau_do, loai_zoom, che_do_chup)
VALUES
    (N'TrueDepth', N'12MP', N'f/1.9', N'Digital', N'Face ID'),
    (N'Wide', N'12MP', N'f/2.2', N'Digital', N'Portrait'),
    (N'Selfie', N'12MP', N'f/2.0', N'Digital', N'Animoji'),
    (N'FaceTime', N'12MP', N'f/2.2', N'Digital', N'Center Stage'),
    (N'Portrait', N'12MP', N'f/1.9', N'Digital', N'Night Mode'),
    (N'TrueDepth', N'12MP', N'f/2.0', N'Digital', N'Face ID'),
    (N'Wide', N'12MP', N'f/2.1', N'Digital', N'Portrait'),
    (N'Selfie', N'12MP', N'f/2.1', N'Digital', N'Animoji'),
    (N'FaceTime', N'12MP', N'f/2.3', N'Digital', N'Center Stage'),
    (N'Portrait', N'12MP', N'f/2.0', N'Digital', N'Night Mode'),
    (N'TrueDepth', N'12MP', N'f/1.8', N'Digital', N'Face ID'),
    (N'Wide', N'12MP', N'f/2.3', N'Digital', N'Portrait'),
    (N'Selfie', N'12MP', N'f/2.2', N'Digital', N'Animoji'),
    (N'FaceTime', N'12MP', N'f/2.1', N'Digital', N'Center Stage'),
    (N'Portrait', N'12MP', N'f/1.8', N'Digital', N'Night Mode');

-- Table cpu
INSERT INTO cpu (chip_xu_ly, hang_san_xuat, so_nhan, xung_nhip, cong_nghe_san_xuat, bo_nho_dem, tieu_thu_dien_nang, nam_ra_mat)
VALUES
    (N'Apple A17 Pro', N'Apple', N'6 nhân', N'3.46 GHz', N'3nm', N'8MB', N'15W', '2024-09-01'),
    (N'Apple A16 Bionic', N'Apple', N'6 nhân', N'3.7 GHz', N'3nm', N'12MB', N'15W', '2023-09-01'),
    (N'Apple A15 Bionic', N'Apple', N'6 nhân', N'3.2 GHz', N'4nm', N'8MB', N'15W', '2022-09-01'),
    (N'Apple A14 Bionic', N'Apple', N'6 nhân', N'3.2 GHz', N'5nm', N'8MB', N'15W', '2021-09-01'),
    (N'Apple A12 Bionic', N'Apple', N'6 nhân', N'3.0 GHz', N'5nm', N'8MB', N'15W', '2020-09-01'),
    (N'Apple A18 Pro', N'Apple', N'6 nhân', N'3.8 GHz', N'3nm', N'10MB', N'15W', '2025-09-01'),
    (N'Apple A17 Bionic', N'Apple', N'6 nhân', N'3.5 GHz', N'3nm', N'9MB', N'15W', '2024-09-01'),
    (N'Apple A16 Pro', N'Apple', N'6 nhân', N'3.6 GHz', N'3nm', N'11MB', N'15W', '2023-09-01'),
    (N'Apple A15 Pro', N'Apple', N'6 nhân', N'3.3 GHz', N'4nm', N'9MB', N'15W', '2022-09-01'),
    (N'Apple A14 Pro', N'Apple', N'6 nhân', N'3.1 GHz', N'5nm', N'7MB', N'15W', '2021-09-01'),
    (N'Apple A13 Bionic', N'Apple', N'6 nhân', N'3.0 GHz', N'5nm', N'8MB', N'15W', '2020-09-01'),
    (N'Apple A12 Pro', N'Apple', N'6 nhân', N'2.9 GHz', N'5nm', N'7MB', N'15W', '2019-09-01'),
    (N'Apple A11 Bionic', N'Apple', N'6 nhân', N'2.8 GHz', N'6nm', N'6MB', N'15W', '2018-09-01'),
    (N'Apple A10 Fusion', N'Apple', N'4 nhân', N'2.3 GHz', N'7nm', N'5MB', N'15W', '2017-09-01'),
    (N'Apple A9', N'Apple', N'2 nhân', N'1.8 GHz', N'7nm', N'4MB', N'15W', '2016-09-01');

-- Table loai
INSERT INTO loai (ten_loai)
VALUES
    (N'Thường'),
    (N'Plus'),
    (N'Pro'),
    (N'Pro Max'),
    (N'Mini'),
    (N'Ultra'),
    (N'Standard'),
    (N'Lite'),
    (N'Compact'),
    (N'Premium'),
    (N'Max'),
    (N'Elite'),
    (N'Advanced'),
    (N'Slim'),
    (N'Pro Plus');

-- Table xuat_xu
INSERT INTO xuat_xu (ma_xuat_xu, ten_quoc_gia)
VALUES
    ('VNA', N'Việt Nam'),
    ('LLA', N'Mỹ Latinh'),
    ('JPA', N'Nhật Bản'),
    ('CHA', N'Trung Quốc'),
    ('KRA', N'Hàn Quốc'),
    ('USA', N'Hoa Kỳ'),
    ('EUA', N'Châu Âu'),
    ('INA', N'Ấn Độ'),
    ('THA', N'Thái Lan'),
    ('SGA', N'Singapore'),
    ('MAA', N'Malaysia'),
    ('AUA', N'Úc'),
    ('CAA', N'Canada'),
    ('TWA', N'Đài Loan'),
    ('HKA', N'Hồng Kông');

-- Table pin
INSERT INTO pin (phien_ban, cong_suat_sac, thoi_gian_su_dung, so_lan_sac_toi_da)
VALUES
    (N'Li-Ion 4000mAh', N'25W', N'20 giờ', N'1000 lần'),
    (N'Li-Ion 4500mAh', N'30W', N'22 giờ', N'1200 lần'),
    (N'Li-Ion 3500mAh', N'20W', N'18 giờ', N'800 lần'),
    (N'Li-Ion 5000mAh', N'45W', N'25 giờ', N'1500 lần'),
    (N'Li-Ion 4200mAh', N'27W', N'21 giờ', N'1100 lần'),
    (N'Li-Ion 3800mAh', N'22W', N'19 giờ', N'900 lần'),
    (N'Li-Ion 4300mAh', N'28W', N'23 giờ', N'1300 lần'),
    (N'Li-Ion 3900mAh', N'24W', N'20 giờ', N'950 lần'),
    (N'Li-Ion 4600mAh', N'32W', N'24 giờ', N'1400 lần'),
    (N'Li-Ion 4100mAh', N'26W', N'21 giờ', N'1050 lần'),
    (N'Li-Ion 3700mAh', N'21W', N'18 giờ', N'850 lần'),
    (N'Li-Ion 4400mAh', N'29W', N'22 giờ', N'1250 lần'),
    (N'Li-Ion 4000mAh', N'23W', N'20 giờ', N'1000 lần'),
    (N'Li-Ion 4500mAh', N'31W', N'23 giờ', N'1350 lần'),
    (N'Li-Ion 3600mAh', N'20W', N'19 giờ', N'900 lần');

-- Table he_dieu_hanh
INSERT INTO he_dieu_hanh (phien_ban, nha_phat_trien, giao_dien_nguoi_dung)
VALUES
    (N'iOS 18', N'Apple', N'iOS UI'),
    (N'iOS 17', N'Apple', N'iOS UI'),
    (N'iOS 16', N'Apple', N'iOS UI'),
    (N'iOS 15', N'Apple', N'iOS UI'),
    (N'iOS 14', N'Apple', N'iOS UI'),
    (N'iOS 19', N'Apple', N'iOS UI'),
    (N'iOS 13', N'Apple', N'iOS UI'),
    (N'iOS 12', N'Apple', N'iOS UI'),
    (N'iOS 11', N'Apple', N'iOS UI'),
    (N'iOS 10', N'Apple', N'iOS UI'),
    (N'iOS 9', N'Apple', N'iOS UI'),
    (N'iOS 8', N'Apple', N'iOS UI'),
    (N'iOS 7', N'Apple', N'iOS UI'),
    (N'iOS 6', N'Apple', N'iOS UI'),
    (N'iOS 5', N'Apple', N'iOS UI');

-- Table man_hinh
INSERT INTO man_hinh (ten_man_hinh, kich_thuoc, loai_man_hinh, do_phan_giai, tan_so_quet, do_sang, chat_lieu_kinh)
VALUES
    (N'Super Retina XDR', N'6.1 inch', N'OLED', N'2532x1170', N'120Hz', N'2000 nit', N'Ceramic Shield'),
    (N'Super Retina XDR', N'6.7 inch', N'OLED', N'2796x1290', N'120Hz', N'2000 nit', N'Ceramic Shield'),
    (N'Liquid Retina', N'6.1 inch', N'LCD', N'1792x828', N'60Hz', N'625 nit', N'Ceramic Shield'),
    (N'Super Retina', N'5.4 inch', N'OLED', N'2340x1080', N'60Hz', N'1200 nit', N'Ceramic Shield'),
    (N'ProMotion XDR', N'6.7 inch', N'OLED', N'2796x1290', N'120Hz', N'2000 nit', N'Ceramic Shield'),
    (N'Super Retina XDR', N'6.5 inch', N'OLED', N'2688x1242', N'120Hz', N'1800 nit', N'Ceramic Shield'),
    (N'Liquid Retina HD', N'6.1 inch', N'LCD', N'1792x828', N'60Hz', N'700 nit', N'Ceramic Shield'),
    (N'Super Retina', N'5.8 inch', N'OLED', N'2436x1125', N'60Hz', N'1000 nit', N'Ceramic Shield'),
    (N'ProMotion XDR', N'6.1 inch', N'OLED', N'2532x1170', N'120Hz', N'2000 nit', N'Ceramic Shield'),
    (N'Super Retina XDR', N'6.7 inch', N'OLED', N'2796x1290', N'120Hz', N'2000 nit', N'Ceramic Shield'),
    (N'Liquid Retina', N'6.1 inch', N'LCD', N'1792x828', N'60Hz', N'625 nit', N'Ceramic Shield'),
    (N'Super Retina', N'5.4 inch', N'OLED', N'2340x1080', N'60Hz', N'1200 nit', N'Ceramic Shield'),
    (N'ProMotion XDR', N'6.7 inch', N'OLED', N'2796x1290', N'120Hz', N'2000 nit', N'Ceramic Shield'),
    (N'Super Retina XDR', N'6.5 inch', N'OLED', N'2688x1242', N'120Hz', N'1800 nit', N'Ceramic Shield'),
    (N'Liquid Retina HD', N'6.1 inch', N'LCD', N'1792x828', N'60Hz', N'700 nit', N'Ceramic Shield');

-- Table rom
INSERT INTO rom (dung_luong_rom, loai_rom, toc_do_doc_ghi, nha_san_xuat, nam_ra_mat)
VALUES
    (N'128GB', N'NVMe', N'3500 MB/s', N'Apple', '2023-09-01'),
    (N'256GB', N'NVMe', N'4000 MB/s', N'Apple', '2023-09-01'),
    (N'512GB', N'NVMe', N'4000 MB/s', N'Apple', '2023-09-01'),
    (N'1TB', N'NVMe', N'5000 MB/s', N'Apple', '2023-09-01'),
    (N'64GB', N'NVMe', N'3000 MB/s', N'Apple', '2021-09-01'),
    (N'128GB', N'NVMe', N'3500 MB/s', N'Apple', '2022-09-01'),
    (N'256GB', N'NVMe', N'4000 MB/s', N'Apple', '2022-09-01'),
    (N'512GB', N'NVMe', N'4000 MB/s', N'Apple', '2022-09-01'),
    (N'1TB', N'NVMe', N'5000 MB/s', N'Apple', '2022-09-01'),
    (N'64GB', N'NVMe', N'3000 MB/s', N'Apple', '2020-09-01'),
    (N'128GB', N'NVMe', N'3500 MB/s', N'Apple', '2021-09-01'),
    (N'256GB', N'NVMe', N'4000 MB/s', N'Apple', '2021-09-01'),
    (N'512GB', N'NVMe', N'4000 MB/s', N'Apple', '2021-09-01'),
    (N'1TB', N'NVMe', N'5000 MB/s', N'Apple', '2021-09-01'),
    (N'64GB', N'NVMe', N'3000 MB/s', N'Apple', '2019-09-01');

-- Table ram
INSERT INTO ram (dung_luong_ram, loai_ram, toc_do_doc_ghi, nha_san_xuat, nam_ra_mat)
VALUES
    (N'6GB', N'LPDDR5', N'6400 MB/s', N'Apple', '2023-09-01'),
    (N'8GB', N'LPDDR5', N'6400 MB/s', N'Apple', '2023-09-01'),
    (N'4GB', N'LPDDR4X', N'4266 MB/s', N'Apple', '2021-09-01'),
    (N'12GB', N'LPDDR5', N'7200 MB/s', N'Apple', '2023-09-01'),
    (N'16GB', N'LPDDR5X', N'8500 MB/s', N'Apple', '2023-09-01'),
    (N'6GB', N'LPDDR5', N'6400 MB/s', N'Apple', '2022-09-01'),
    (N'8GB', N'LPDDR5', N'6400 MB/s', N'Apple', '2022-09-01'),
    (N'4GB', N'LPDDR4X', N'4266 MB/s', N'Apple', '2020-09-01'),
    (N'12GB', N'LPDDR5', N'7200 MB/s', N'Apple', '2022-09-01'),
    (N'16GB', N'LPDDR5X', N'8500 MB/s', N'Apple', '2022-09-01'),
    (N'6GB', N'LPDDR5', N'6400 MB/s', N'Apple', '2021-09-01'),
    (N'8GB', N'LPDDR5', N'6400 MB/s', N'Apple', '2021-09-01'),
    (N'4GB', N'LPDDR4X', N'4266 MB/s', N'Apple', '2019-09-01'),
    (N'12GB', N'LPDDR5', N'7200 MB/s', N'Apple', '2021-09-01'),
    (N'16GB', N'LPDDR5X', N'8500 MB/s', N'Apple', '2021-09-01');

-- Table mau_sac
INSERT INTO mau_sac (ten_mau, hex_color)
VALUES
    (N'Đen Phantôm', '#1C2526'),
    (N'Trắng Ngọc Trai', '#F5F6F5'),
    (N'Vàng Ánh Kim', '#D4A017'),
    (N'Xanh Biển Sâu', '#0B3D91'),
    (N'Hồng Phấn', '#F8B7CD'),
    (N'Xám Không Gian', '#4A4A4A'),
    (N'Bạc Ánh Kim', '#C0C0C0'),
    (N'Xanh Lá Cây', '#2E8B57'),
    (N'Đỏ Rực Rỡ', '#FF0000'),
    (N'Tím Hoàng Gia', '#800080'),
    (N'Xanh Dương', '#1E90FF'),
    (N'Vàng Đồng', '#B8860B'),
    (N'Đen Ánh Kim', '#2F2F2F'),
    (N'Trắng Sứ', '#F5F5F5'),
    (N'Hồng Ánh Kim', '#FFB6C1');

-- Table nha_cung_cap
INSERT INTO nha_cung_cap (ten_nha_cung_cap, dia_chi, sdt, email)
VALUES
    (N'VNA Trading', N'123 Nguyễn Trãi, Hà Nội', '0901234567', 'vna@example.com'),
    (N'Thế Giới Di Động', N'456 Lê Văn Sỹ, TP.HCM', '0912345678', 'tgdd@example.com'),
    (N'FPT Shop', N'789 Trần Hưng Đạo, Đà Nẵng', '0923456789', 'fpt@example.com'),
    (N'CellphoneS', N'101 Hai Bà Trưng, Huế', '0934567890', 'cellphones@example.com'),
    (N'Hoàng Hà Mobile', N'202 Lý Thường Kiệt, Hà Nội', '0945678901', 'hoangha@example.com'),
    (N'Viettel Store', N'303 Phạm Văn Đồng, Hà Nội', '0956789012', 'viettel@example.com'),
    (N'Mobile World', N'555 Nguyễn Huệ, TP.HCM', '0967890123', 'mobileworld@example.com'),
    (N'Phong Vũ', N'777 Lê Đại Hành, Đà Nẵng', '0978901234', 'phongvu@example.com'),
    (N'Điện Máy Xanh', N'999 Hai Bà Trưng, Huế', '0989012345', 'dienmayxanh@example.com'),
    (N'TechZone', N'111 Phạm Ngũ Lão, Hà Nội', '0990123456', 'techzone@example.com'),
    (N'ShopDunk', N'222 Lê Văn Sỹ, TP.HCM', '0902345678', 'shopdunk@example.com'),
    (N'Trần Anh', N'333 Trần Hưng Đạo, Đà Nẵng', '0913456789', 'trananh@example.com'),
    (N'Nguyễn Kim', N'444 Lý Thường Kiệt, Huế', '0924567890', 'nguyenkim@example.com'),
    (N'HNam Mobile', N'555 Nguyễn Đình Chiểu, Hà Nội', '0935678901', 'hnam@example.com'),
    (N'Minh Tuấn Mobile', N'666 Lê Lai, TP.HCM', '0946789012', 'minhtuan@example.com');

-- Table san_pham
INSERT INTO san_pham (ten_san_pham, thuong_hieu, id_nha_cung_cap, trang_thai)
VALUES
    (N'iPhone 16', N'Apple', 1, 'ACTIVE'),
    (N'iPhone 16 Pro', N'Apple', 2, 'DISCONTINUED'),
    (N'iPhone 15', N'Apple', 3, 'COMING_SOON'),
    (N'iPhone 14', N'Apple', 4, 'TEMPORARILY_UNAVAILABLE'),
    (N'iPhone 13', N'Apple', 5, 'OUT_OF_STOCK'),
    (N'iPhone 16 Plus', N'Apple', 6, 'ACTIVE'),
    (N'iPhone 15 Pro', N'Apple', 7, 'DISCONTINUED'),
    (N'iPhone 14 Pro', N'Apple', 8, 'COMING_SOON'),
    (N'iPhone 13 Pro', N'Apple', 9, 'TEMPORARILY_UNAVAILABLE'),
    (N'iPhone 12', N'Apple', 10, 'OUT_OF_STOCK'),
    (N'iPhone 16 Pro Max', N'Apple', 11, 'ACTIVE'),
    (N'iPhone 15 Pro Max', N'Apple', 12, 'DISCONTINUED'),
    (N'iPhone 14 Plus', N'Apple', 13, 'COMING_SOON'),
    (N'iPhone 13 Mini', N'Apple', 14, 'TEMPORARILY_UNAVAILABLE'),
    (N'iPhone 12 Pro', N'Apple', 15, 'OUT_OF_STOCK');

-- Table san_pham_chi_tiet
INSERT INTO san_pham_chi_tiet (id_san_pham, id_mau, id_ram, id_rom, id_man_hinh, id_he_dieu_hanh, id_pin, id_cpu, id_camera_truoc, id_camera_sau, id_xuat_xu, id_loai, so_luong, gia_ban)
VALUES
    (1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 10, 20000000.00),
    (2, 2, 2, 2, 2, 1, 2, 2, 2, 2, 2, 3, 8, 25000000.00),
    (3, 3, 3, 3, 3, 2, 3, 3, 3, 3, 3, 1, 15, 15000000.00),
    (4, 4, 4, 4, 4, 3, 4, 4, 4, 4, 4, 1, 12, 12000000.00),
    (5, 5, 5, 5, 5, 4, 5, 5, 5, 5, 5, 1, 20, 10000000.00),
    (6, 6, 6, 6, 6, 1, 6, 6, 6, 6, 6, 2, 9, 22000000.00),
    (7, 7, 7, 7, 7, 2, 7, 7, 7, 7, 7, 3, 7, 27000000.00),
    (8, 8, 8, 8, 8, 3, 8, 8, 8, 8, 8, 3, 14, 18000000.00),
    (9, 9, 9, 9, 9, 4, 9, 9, 9, 9, 9, 3, 11, 13000000.00),
    (10, 10, 10, 10, 10, 5, 10, 10, 10, 10, 10, 1, 18, 11000000.00),
    (11, 11, 11, 11, 11, 1, 11, 11, 11, 11, 11, 4, 6, 30000000.00),
    (12, 12, 12, 12, 12, 2, 12, 12, 12, 12, 12, 4, 5, 28000000.00),
    (13, 13, 13, 13, 13, 3, 13, 13, 13, 13, 13, 2, 13, 16000000.00),
    (14, 14, 14, 14, 14, 4, 14, 14, 14, 14, 14, 5, 10, 14000000.00),
    (15, 15, 15, 15, 15, 5, 15, 15, 15, 15, 15, 3, 16, 12000000.00);

-- Table chi_tiet_hoa_don
INSERT INTO chi_tiet_hoa_don (id_hoa_don, id_san_pham_chi_tiet, ten_san_pham, mo_ta, so_luong, don_gia)
VALUES
    (1, 1, N'iPhone 16', N'Ip16', 1, 20000000.00),
    (2, 2, N'iPhone 16 Pro', N'Ip16Pro', 1, 25000000.00),
    (3, 3, N'iPhone 15', N'ip15', 1, 15000000.00),
    (4, 4, N'iPhone 14', N'ip14', 1, 12000000.00),
    (5, 5, N'iPhone 13', N'ip13', 1, 10000000.00),
    (6, 6, N'iPhone 16 Plus', N'ip16plus', 1, 22000000.00),
    (7, 7, N'iPhone 15 Pro', N'ip15pro', 1, 27000000.00),
    (8, 8, N'iPhone 14 Pro', N'ip14pro', 1, 18000000.00),
    (9, 9, N'iPhone 13 Pro', N'ip13pro', 1, 13000000.00),
    (10, 10, N'iPhone 12', N'ip12', 1, 11000000.00),
    (11, 11, N'iPhone 16 Pro Max', N'ip16promax', 1, 30000000.00),
    (12, 12, N'iPhone 15 Pro Max', N'ip15promax', 1, 28000000.00),
    (13, 13, N'iPhone 14 Plus', N'ip14plus', 1, 16000000.00),
    (14, 14, N'iPhone 13 Mini', N'ip13mini', 1, 14000000.00),
    (15, 15, N'iPhone 12 Pro', N'ip12pro', 1, 12000000.00);

-- Table loai_bao_hanh
INSERT INTO loai_bao_hanh (ten_loai_bao_hanh, thoi_gian_thang, mo_ta)
VALUES
    (N'Bảo hành chính hãng', 12, N'Bảo hành từ nhà sản xuất'),
    (N'Bảo hành của cửa hàng', 6, N'Bảo hành nội bộ của cửa hàng'),
    (N'Bảo hành đổi trả', 1, N'Cho phép đổi trả trong vòng 1 tháng'),
    (N'Bảo hành mở rộng', 24, N'Bảo hành mở rộng từ nhà sản xuất'),
    (N'Bảo hành VIP', 36, N'Bảo hành đặc biệt cho khách hàng VIP'),
    (N'Bảo hành 1 đổi 1', 3, N'Đổi trả 1 đổi 1 trong 3 tháng'),
    (N'Bảo hành tiêu chuẩn', 12, N'Bảo hành cơ bản từ cửa hàng'),
    (N'Bảo hành quốc tế', 12, N'Bảo hành toàn cầu từ nhà sản xuất'),
    (N'Bảo hành nhanh', 6, N'Bảo hành sửa chữa nhanh trong 6 tháng'),
    (N'Bảo hành đặc biệt', 18, N'Bảo hành đặc biệt cho sản phẩm cao cấp'),
    (N'Bảo hành cơ bản', 9, N'Bảo hành cơ bản từ nhà cung cấp'),
    (N'Bảo hành mở rộng 2 năm', 24, N'Bảo hành mở rộng thêm 2 năm'),
    (N'Bảo hành 1 đổi 1 6 tháng', 6, N'Đổi trả 1 đổi 1 trong 6 tháng'),
    (N'Bảo hành siêu tốc', 3, N'Sửa chữa nhanh trong 3 tháng'),
    (N'Bảo hành vàng', 36, N'Bảo hành cao cấp cho sản phẩm đặc biệt');

-- Table bao_hanh
INSERT INTO bao_hanh (id_khach_hang, id_san_pham_chi_tiet, ngay_bat_dau, ngay_ket_thuc, id_loai_bao_hanh, trang_thai_bao_hanh)
VALUES
    (1, 1, '2025-05-02', '2026-05-02', 1, N'UNDER_WARRANTY'),
    (2, 2, '2025-04-20', '2026-04-20', 2, N'UNDER_WARRANTY'),
    (3, 3, '2025-05-01', '2026-05-01', 1, N'UNDER_WARRANTY'),
    (4, 4, '2025-03-15', '2026-03-15', 1, N'UNDER_WARRANTY'),
    (5, 5, '2025-05-12', '2026-05-12', 3, N'UNDER_WARRANTY'),
    (6, 6, '2025-06-01', '2026-06-01', 4, N'UNDER_WARRANTY'),
    (7, 7, '2025-04-25', '2027-04-25', 5, N'UNDER_WARRANTY'),
    (8, 8, '2025-05-10', '2026-05-10', 6, N'UNDER_WARRANTY'),
    (9, 9, '2025-03-20', '2026-03-20', 7, N'UNDER_WARRANTY'),
    (10, 10, '2025-05-15', '2026-05-15', 8, N'UNDER_WARRANTY'),
    (11, 11, '2025-06-05', '2026-06-05', 9, N'UNDER_WARRANTY'),
    (12, 12, '2025-04-30', '2026-04-30', 10, N'UNDER_WARRANTY'),
    (13, 13, '2025-05-20', '2026-05-20', 11, N'UNDER_WARRANTY'),
    (14, 14, '2025-03-25', '2026-03-25', 12, N'UNDER_WARRANTY'),
    (15, 15, '2025-06-10', '2026-06-10', 13, N'UNDER_WARRANTY');

-- Table lich_su_bao_hanh
INSERT INTO lich_su_bao_hanh (id_san_pham_bao_hanh, ngay_tiep_nhan, ngay_hoan_thanh, mo_ta_loi, trang_thai)
VALUES
    (1, '2025-05-03', '2025-05-10', N'Lỗi màn hình chớp tắt', N'REPAIRED'),
    (2, '2025-04-25', '2025-04-28', N'Không sạc được pin', N'REPAIRED'),
    (3, '2025-05-02', NULL, N'Loa bị rè', N'IN_REPAIR'),
    (4, '2025-04-01', '2025-04-05', N'Lỗi phần mềm, treo máy', N'REPAIRED'),
    (5, '2025-05-13', NULL, N'Nứt màn hình do rơi vỡ', N'WARRANTY_VOID'),
    (6, '2025-06-02', '2025-06-09', N'Lỗi cảm ứng', N'REPAIRED'),
    (7, '2025-04-26', '2025-04-30', N'Pin tụt nhanh', N'REPAIRED'),
    (8, '2025-05-11', NULL, N'Camera bị mờ', N'IN_REPAIR'),
    (9, '2025-03-21', '2025-03-25', N'Lỗi kết nối Wi-Fi', N'REPAIRED'),
    (10, '2025-05-16', NULL, N'Máy nóng bất thường', N'IN_REPAIR'),
    (11, '2025-06-06', '2025-06-13', N'Lỗi nút nguồn', N'REPAIRED'),
    (12, '2025-05-01', '2025-05-05', N'Lỗi loa ngoài', N'REPAIRED'),
    (13, '2025-05-21', NULL, N'Màn hình bị ám vàng', N'IN_REPAIR'),
    (14, '2025-03-26', '2025-03-30', N'Lỗi cảm biến ánh sáng', N'REPAIRED'),
    (15, '2025-06-11', NULL, N'Lỗi sạc không dây', N'IN_REPAIR');

-- Table imei
INSERT INTO imei (id_san_pham_chi_tiet, so_imei, so_imei_2, trang_thai_imei, nha_mang)
VALUES
    (1, '123456789012345', '543210987654321', 'AVAILABLE', 'Viettel'),
    (2, '123456789012346', '543210987654322', 'AVAILABLE', 'Mobifone'),
    (3, '123456789012347', '543210987654323', 'AVAILABLE', 'Vinaphone'),
    (4, '123456789012348', '543210987654324', 'AVAILABLE', 'Viettel'),
    (5, '123456789012349', '543210987654325', 'AVAILABLE', 'Mobifone'),
    (6, '123456789012350', '543210987654326', 'AVAILABLE', 'Vinaphone'),
    (7, '123456789012351', '543210987654327', 'AVAILABLE', 'Viettel'),
    (8, '123456789012352', '543210987654328', 'AVAILABLE', 'Mobifone'),
    (9, '123456789012353', '543210987654329', 'AVAILABLE', 'Vinaphone'),
    (10, '123456789012354', '543210987654330', 'AVAILABLE', 'Viettel'),
    (11, '123456789012355', '543210987654331', 'AVAILABLE', 'Mobifone'),
    (12, '123456789012356', '543210987654332', 'AVAILABLE', 'Vinaphone'),
    (13, '123456789012357', '543210987654333', 'AVAILABLE', 'Viettel'),
    (14, '123456789012358', '543210987654334', 'AVAILABLE', 'Mobifone'),
    (15, '123456789012359', '543210987654335', 'AVAILABLE', 'Vinaphone');

-- Table hinh_anh
INSERT INTO hinh_anh (id_san_pham_chi_tiet, url, image_public_id)
VALUES
    (1, 'https://example.com/images/iphone16_black.jpg', 'iphone16_black_001'),
    (2, 'https://example.com/images/iphone16pro_white.jpg', 'iphone16pro_white_002'),
    (3, 'https://example.com/images/iphone15_gold.jpg', 'iphone15_gold_003'),
    (4, 'https://example.com/images/iphone14_blue.jpg', 'iphone14_blue_004'),
    (5, 'https://example.com/images/iphone13_pink.jpg', 'iphone13_pink_005'),
    (6, 'https://example.com/images/iphone16plus_gray.jpg', 'iphone16plus_gray_006'),
    (7, 'https://example.com/images/iphone15pro_silver.jpg', 'iphone15pro_silver_007'),
    (8, 'https://example.com/images/iphone14pro_green.jpg', 'iphone14pro_green_008'),
    (9, 'https://example.com/images/iphone13pro_red.jpg', 'iphone13pro_red_009'),
    (10, 'https://example.com/images/iphone12_purple.jpg', 'iphone12_purple_010'),
    (11, 'https://example.com/images/iphone16promax_blue.jpg', 'iphone16promax_blue_011'),
    (12, 'https://example.com/images/iphone15promax_gold.jpg', 'iphone15promax_gold_012'),
    (13, 'https://example.com/images/iphone14plus_black.jpg', 'iphone14plus_black_013'),
    (14, 'https://example.com/images/iphone13mini_white.jpg', 'iphone13mini_white_014'),
    (15, 'https://example.com/images/iphone12pro_silver.jpg', 'iphone12pro_silver_015');

-- Table imei_da_ban
INSERT INTO imei_da_ban (id_chi_tiet_hoa_don, so_imei, trang_thai)
VALUES
    (1, '123456789012345', N'SOLD'),
    (2, '123456789012346', N'SOLD'),
    (3, '123456789012347', N'SOLD'),
    (4, '123456789012348', N'SOLD'),
    (5, '123456789012349', N'SOLD'),
    (6, '123456789012350', N'SOLD'),
    (7, '123456789012351', N'SOLD'),
    (8, '123456789012352', N'SOLD'),
    (9, '123456789012353', N'SOLD'),
    (10, '123456789012354', N'SOLD'),
    (11, '123456789012355', N'SOLD'),
    (12, '123456789012356', N'SOLD'),
    (13, '123456789012357', N'SOLD'),
    (14, '123456789012358', N'SOLD'),
    (15, '123456789012359', N'SOLD');

-- Table phuong_thuc_thanh_toan
INSERT INTO phuong_thuc_thanh_toan (ten_phuong_thuc, loai_hinh_thuc)
VALUES
    (N'Thẻ tín dụng', N'Trực tuyến'),
    (N'Chuyển khoản ngân hàng', N'Trực tuyến'),
    (N'Tiền mặt', N'Tại cửa hàng'),
    (N'Ví Momo', N'Trực tuyến'),
    (N'Thanh toán khi nhận hàng', N'Tại điểm giao'),
    (N'Ví ZaloPay', N'Trực tuyến'),
    (N'Thẻ ghi nợ', N'Trực tuyến'),
    (N'PayPal', N'Trực tuyến'),
    (N'Thẻ Visa', N'Trực tuyến'),
    (N'Thẻ MasterCard', N'Trực tuyến'),
    (N'Ví ShopeePay', N'Trực tuyến'),
    (N'VNPay', N'Trực tuyến'),
    (N'Tiền mặt tại quầy', N'Tại cửa hàng'),
    (N'Thanh toán trả góp', N'Trực tuyến'),
    (N'Ví Viettel Money', N'Trực tuyến');

-- Table chi_tiet_thanh_toan
INSERT INTO chi_tiet_thanh_toan (id_hoa_don, id_phuong_thuc_thanh_toan, so_tien_thanh_toan)
VALUES
    (1, 1, 19830000.00),
    (2, 3, 14840000.00),
    (3, 2, 24535000.00),
    (4, 4, 12000000.00),
    (5, 5, 10000000.00),
    (6, 6, 22000000.00),
    (7, 7, 27000000.00),
    (8, 8, 18000000.00),
    (9, 9, 13000000.00),
    (10, 10, 11000000.00),
    (11, 11, 30000000.00),
    (12, 12, 28000000.00),
    (13, 13, 16000000.00),
    (14, 14, 14000000.00),
    (15, 15, 12000000.00);

-- Table gio_hang_chi_tiet
INSERT INTO gio_hang_chi_tiet (id_gio_hang, id_san_pham_chi_tiet, so_luong, gia, ngay_them)
VALUES
    (1, 1, 2, 20000000.00, '2025-05-05'),
    (2, 2, 1, 25000000.00, '2025-05-05'),
    (3, 3, 1, 15000000.00, '2025-05-05'),
    (4, 4, 1, 12000000.00, '2025-05-06'),
    (5, 5, 1, 10000000.00, '2025-05-06'),
    (6, 6, 2, 22000000.00, '2025-05-07'),
    (7, 7, 1, 27000000.00, '2025-05-07'),
    (8, 8, 1, 18000000.00, '2025-05-08'),
    (9, 9, 1, 13000000.00, '2025-05-08'),
    (10, 10, 1, 11000000.00, '2025-05-09'),
    (11, 11, 2, 30000000.00, '2025-05-09'),
    (12, 12, 1, 28000000.00, '2025-05-10'),
    (13, 13, 1, 16000000.00, '2025-05-10'),
    (14, 14, 1, 14000000.00, '2025-05-11'),
    (15, 15, 1, 12000000.00, '2025-05-11');

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