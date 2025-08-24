-- Query kiểm tra IMEI trùng lặp giữa 2 bảng imei và imei_da_ban
WITH All_IMEI AS (
    SELECT so_imei FROM imei
    UNION ALL
    SELECT so_imei FROM imei_da_ban
)
SELECT 
    so_imei as IMEI,
    COUNT(*) as TRUNG_LAP
FROM All_IMEI
GROUP BY so_imei
HAVING COUNT(*) > 1
ORDER BY COUNT(*) DESC;

-- 2. Kiểm tra IMEI trùng lặp giữa bảng IMEI và IMEI_DA_BAN
SELECT 
    i.so_imei,
    'IMEI & IMEI_DA_BAN' as loai_trung,
    i.id_imei,
    i.id_san_pham_chi_tiet,
    idb.id_imei_da_ban,
    idb.id_chi_tiet_hoa_don
FROM imei i
INNER JOIN imei_da_ban idb ON i.so_imei = idb.so_imei
ORDER BY i.so_imei;


-- 3. Kiểm tra IMEI không hợp lệ (không đủ 15 ký tự hoặc chứa ký tự không phải số)
SELECT 
    'IMEI' as bang,
    id_imei,
    so_imei,
    LEN(so_imei) as do_dai,
    CASE 
        WHEN LEN(so_imei) != 15 THEN 'Độ dài không hợp lệ'
        WHEN so_imei LIKE '%[^0-9]%' THEN 'Chứa ký tự không phải số'
        ELSE 'Hợp lệ'
    END as trang_thai
FROM imei
WHERE LEN(so_imei) != 15 OR so_imei LIKE '%[^0-9]%'

UNION ALL

SELECT 
    'IMEI_DA_BAN' as bang,
    id_imei_da_ban,
    so_imei,
    LEN(so_imei) as do_dai,
    CASE 
        WHEN LEN(so_imei) != 15 THEN 'Độ dài không hợp lệ'
        WHEN so_imei LIKE '%[^0-9]%' THEN 'Chứa ký tự không phải số'
        ELSE 'Hợp lệ'
    END as trang_thai
FROM imei_da_ban
WHERE LEN(so_imei) != 15 OR so_imei LIKE '%[^0-9]%'

ORDER BY bang, trang_thai;


-- kiểm tra imei có thừa hay không
SELECT 
    spct.id_san_pham_chi_tiet,
    spct.ma_san_pham_chi_tiet,
    spct.so_luong as so_luong_can_co,
    COUNT(i.so_imei) as so_imei_da_tao,
    CASE 
        WHEN COUNT(i.so_imei) = spct.so_luong THEN 'OK'
        WHEN COUNT(i.so_imei) > spct.so_luong THEN 'THỪA'
        ELSE 'THIẾU'
    END as trang_thai
FROM san_pham_chi_tiet spct
LEFT JOIN imei i ON spct.id_san_pham_chi_tiet = i.id_san_pham_chi_tiet
GROUP BY spct.id_san_pham_chi_tiet, spct.ma_san_pham_chi_tiet, spct.so_luong
ORDER BY spct.id_san_pham_chi_tiet;
