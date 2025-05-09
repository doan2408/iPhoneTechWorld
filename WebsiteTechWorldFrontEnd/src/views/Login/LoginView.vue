<script setup>
import { ref, watch } from "vue";
import { useRouter } from "vue-router";
import { useStore } from "vuex";
import HeaderClient from "@/components/Client/Header.vue";

const tai_khoan = ref("");
const mat_khau = ref("");
const error = ref("");
const isLoading = ref(false);
const router = useRouter();
const store = useStore();

const handleLogin = async () => {
  if (!tai_khoan.value.trim() || !mat_khau.value.trim()) {
    error.value = "Vui lòng nhập tài khoản và mật khẩu";
    return;
  }
  try {
    isLoading.value = true;
    await store.dispatch("login", {
      tai_khoan: tai_khoan.value,
      mat_khau: mat_khau.value,
    });
    if (store.getters.isAdmin) {
      router.push("/admin/products");
    } else if (store.getters.isStaff) {
      router.push("/staff/products");
    } else if (store.getters.isCustomer) {
      router.push("/home");
    }
  } catch (err) {
    error.value = err.message || "Đăng nhập thất bại";
  } finally {
    isLoading.value = false;
  }
};

watch([tai_khoan, mat_khau], () => {
  error.value = "";
});
</script>

<template>
  <div class="login-container">
    <h2>Đăng nhập</h2>
    <form @submit.prevent="handleLogin">
      <div>
        <label>Tài khoản:</label>
        <input
          v-model="tai_khoan"
          type="text"
          placeholder="Nhập tài khoản"
          required
        />
      </div>
      <div>
        <label>Mật khẩu:</label>
        <input
          v-model="mat_khau"
          type="password"
          placeholder="Nhập mật khẩu"
          required
        />
      </div>
      <button type="submit" :disabled="isLoading">
        {{ isLoading ? "Đang đăng nhập..." : "Đăng nhập" }}
      </button>
      <p v-if="error" style="color: red">{{ error }}</p>
    </form>
  </div>
</template>

<style scoped>
:root {
  --primary-color: #1e90ff;
  --secondary-color: #6c757d;
  --text-color: #ffffff;
  --error-color: #ff4d4f;
  --border-radius: 12px;
  --shadow: 0 8px 24px rgba(0, 0, 0, 0.15);
  --glass-bg: rgba(255, 255, 255, 0.1);
  --glass-border: 1px solid rgba(255, 255, 255, 0.2);
}

.login-container {
  max-width: 420px;
  margin: 3rem auto;
  padding: 2.5rem;
  background: var(--glass-bg);
  backdrop-filter: blur(10px);
  border: var(--glass-border);
  border-radius: var(--border-radius);
  box-shadow: var(--shadow);
  text-align: center;
  animation: slideIn 0.6s ease-out;
}

@keyframes slideIn {
  from {
    opacity: 0;
    transform: translateY(30px);
  }
  to {
    opacity: 1;
    transform: translateY(0);
  }
}

.login-container h2 {
  color: var(--text-color);
  margin-bottom: 2rem;
  font-size: 2rem;
  font-weight: 700;
  letter-spacing: 1px;
  text-transform: uppercase;
}

form {
  display: flex;
  flex-direction: column;
  gap: 1.5rem;
}

form div {
  text-align: left;
}

label {
  display: block;
  font-size: 1rem;
  color: var(--text-color);
  margin-bottom: 0.5rem;
  font-weight: 500;
  text-shadow: 0 1px 2px rgba(0, 0, 0, 0.2);
}

input {
  width: 100%;
  padding: 0.9rem;
  background: rgba(255, 255, 255, 0.15);
  border: var(--glass-border);
  border-radius: var(--border-radius);
  font-size: 1rem;
  color: var(--text-color);
  transition: all 0.3s ease;
}

input::placeholder {
  color: rgba(255, 255, 255, 0.7);
}

input:focus {
  outline: none;
  background: rgba(255, 255, 255, 0.25);
  border-color: var(--primary-color);
  box-shadow: 0 0 8px rgba(30, 144, 255, 0.4);
}

button {
  background: linear-gradient(135deg, var(--primary-color), #00b7ff);
  color: var(--text-color);
  padding: 1rem;
  border: none;
  border-radius: var(--border-radius);
  font-size: 1.1rem;
  font-weight: 600;
  cursor: pointer;
  transition: all 0.3s ease;
  position: relative;
  overflow: hidden;
}

button::before {
  content: "";
  position: absolute;
  top: 0;
  left: -100%;
  width: 100%;
  height: 100%;
  background: linear-gradient(
    90deg,
    transparent,
    rgba(255, 255, 255, 0.3),
    transparent
  );
  transition: 0.5s;
}

button:hover::before {
  left: 100%;
}

button:hover {
  transform: translateY(-3px);
  box-shadow: 0 6px 12px rgba(30, 144, 255, 0.3);
}

button:disabled {
  background: var(--secondary-color);
  cursor: not-allowed;
  transform: none;
  box-shadow: none;
}

p[style*="color: red"] {
  color: var(--error-color);
  font-size: 0.95rem;
  margin-top: 0.8rem;
  text-align: left;
  font-weight: 500;
}

@media (max-width: 480px) {
  .login-container {
    max-width: 90%;
    margin: 1.5rem auto;
    padding: 2rem;
  }

  .login-container h2 {
    font-size: 1.6rem;
  }

  input,
  button {
    font-size: 0.95rem;
    padding: 0.8rem;
  }

  label {
    font-size: 0.9rem;
  }
}
</style>
