<script setup>

</script>

<template>
    <div class="container mt-4">
        <main>
            <section class="cart-table">
                <div class="cart-header">
                    <div class="check-box">
                        <input type="checkbox">
                    </div>
                    <div class="san-pham">Sản Phẩm</div>
                    <div class="don-gia">Đơn Giá</div>
                    <div class="so-luong">Số Lượng</div>
                    <div class="so-tien">Số Tiền</div>
                    <div class="thao-tac">Thao Tác</div>
                </div>
                <div class="cart-item">
                    <div class="check-box">
                        <input type="checkbox">
                    </div>
                    <div class="san-pham">
                        <img src="https://res.cloudinary.com/dzs764s5c/image/upload/v1746884362/11pro-xanh_dpkrnp.webp"
                            alt="Khuyên tai" class="item-img">
                        <div class="item-info">
                            <div class="item-title"></div>
                        </div>
                    </div>
                    <div class="don-gia">
                        <div class="item-price">
                            <span class="old-price">₫19.000</span>
                            <span class="new-price">₫14.900</span>
                        </div>
                    </div>
                    <div class="so-luong">
                        <div class="item-qty">
                            <button class="qty-btn">-</button>
                            <input type="text" value="4" class="qty-input">
                            <button class="qty-btn">+</button>
                        </div>
                    </div>
                    <div class="so-tien">
                        <div class="item-total">₫59.600</div>
                    </div>
                    <div class="thao-tac">
                        <div class="item-action">
                            <button class="delete-btn">Xóa</button>
                            <a href="#">Tìm sản phẩm tương tự</a>
                        </div>
                    </div>
                </div>
                <div class="voucher-row">
                    <span>Thêm Shop Voucher</span>
                </div>
                <div class="shipping-row">
                    <span>Giảm ₫300.000 phí vận chuyển đơn tối thiểu ₫0; Giảm ₫500.000 phí vận chuyển đơn tối thiểu
                        ₫500.000 <a href="#">Tìm hiểu thêm</a></span>
                </div>
            </section>
            <section class="footer">
                <div class="select-all">
                    <input type="checkbox"> Chọn Tất Cả (17)
                    <button class="delete-btn">Xóa</button>
                </div>
                <div class="checkout">
                    <span>Tổng cộng (0 Sản phẩm): <b>₫0</b></span>
                    <button class="checkout-btn">Mua Hàng</button>
                </div>
            </section>
        </main>
    </div>

</template>

<style>
body {
    font-family: Arial, sans-serif;
    background: #fafafa;
    margin: 0;
    padding: 0;
}

.cart-table {
    background: #fff;
    margin: 30px auto;
    width: 100%;
    border-radius: 6px;
    box-shadow: 0 2px 8px #eee;
    padding: 20px;
}

.cart-header,
.cart-item,
.voucher-row,
.shipping-row {
    display: flex;
    align-items: center;
    padding: 10px 0;
}

.cart-item {
    border-top: 1px solid #eee;
    border-bottom: 1px solid #eee;
}

.item-img {
    width: 60px;
    height: 60px;
    object-fit: cover;
}

.check-box {
    width: 3%;
}

.san-pham {
    width: 42%;
    display: flex;
}

.don-gia,
.so-tien {
    width: 10%;
}

.so-luong {
    width: 15%;
}

.thao-tac {
    width: 20%;
}

.item-info {
    flex: 2;
}

.item-title {
    font-size: 1em;
    font-weight: bold;
}

.item-type {
    color: #888;
    font-size: 0.9em;
}

.item-price {
    flex: 1;
    display: flex;
    flex-direction: column;
}

.old-price {
    text-decoration: line-through;
    color: #aaa;
    font-size: 0.9em;
}

.new-price {
    color: #ee4d2d;
    font-weight: bold;
}

.item-qty {
    flex: 1;
    display: flex;
    align-items: center;
}

.qty-btn {
    width: 28px;
    height: 28px;
    border: 1px solid #ccc;
    background: #fff;
    cursor: pointer;
}

.qty-input {
    width: 40px;
    text-align: center;
    margin: 0 5px;
}

.item-total {
    flex: 1;
    color: #ee4d2d;
    font-weight: bold;
}

.item-action {
    flex: 1;
    display: flex;
    flex-direction: column;
    align-items: flex-start;
}

.delete-btn {
    background: none;
    color: #ee4d2d;
    border: none;
    cursor: pointer;
    margin-bottom: 5px;
}

.voucher-row,
.shipping-row {
    color: #0088ff;
    font-size: 0.95em;
    padding-left: 40px;
}

.footer {
    display: flex;
    justify-content: space-between;
    align-items: center;
    background: #fff;
    padding: 20px 40px;
    border-top: 1px solid #eee;
    margin-top: 30px;
}

.select-all {
    display: flex;
    align-items: center;
    gap: 15px;
}

.save-btn {
    color: #ee4d2d;
    cursor: pointer;
}

.checkout {
    display: flex;
    align-items: center;
    gap: 20px;
}

.checkout-btn {
    background: #ee4d2d;
    color: #fff;
    border: none;
    padding: 12px 32px;
    border-radius: 2px;
    font-size: 1.1em;
    cursor: pointer;
}
</style>