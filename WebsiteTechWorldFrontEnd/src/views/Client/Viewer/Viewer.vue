<template>
  <div class="container py-4">
    <div class="row">
      <div class="col-lg-8 mb-4">
        <div class="bg-dark rounded-4 d-flex align-items-center justify-content-center" style="height:400px;">
          <video v-show="state.isVideoLoaded" ref="remoteVideoRef" autoplay playsinline
            style="width:100%;height:100%;background-color:black;"></video>
          <button v-if="state.isLive && !state.isVideoLoaded" class="btn btn-primary btn-lg" @click="loadVideo">
            ▶️ Xem Live
          </button>
          <span v-if="!state.isLive" class="text-white fs-4">🔴 Live đã kết thúc</span>
        </div>

        <!-- Chat -->
        <div class="card shadow-lg rounded-4 flex-grow-1 mt-4" style="height:250px;" v-if="state.isLive">
          <div class="card-header bg-white border-bottom fw-semibold text-secondary">💬 Chat trực tiếp</div>
          <div class="card-body overflow-auto p-3" ref="chatContainer">
            <div v-for="(msg, i) in state.chatMessages" :key="i" class="d-flex mb-2"
              :class="msg.user === 'Viewer' && msg.idNguoiGui === viewerId ? 'justify-content-end' : 'justify-content-start'">
              <div class="px-3 py-2 rounded-4" :class="{
                'bg-primary text-white': msg.user === 'Viewer' && msg.idNguoiGui === viewerId,
                'bg-light text-dark': msg.user === 'Viewer' && msg.idNguoiGui !== viewerId,
                'bg-success text-white': msg.user === 'Streamer'   // 🌿 màu xanh lá + chữ trắng
              }">
                <p class="mb-1 fw-bold small">{{ msg.user == 'Streamer' ? 'Người bán' : msg.tenNguoiGui }}</p>
                <p class="mb-0 small">{{ msg.content }}</p>
              </div>
            </div>
          </div>
          <div class="card-footer d-flex gap-2">
            <input v-if="state.isLoggedIn" v-model="state.chatInput" type="text" class="form-control"
              placeholder="Nhập tin nhắn..." @keyup.enter="sendChat" />
            <button v-if="state.isLoggedIn" class="btn btn-primary" @click="sendChat">Gửi</button>
            <p v-else class="text-muted small mb-0">Đăng nhập để chat</p>
          </div>
        </div>
      </div>

      <div class="col-lg-4" v-if="state.isLive">
        <div class="card shadow-lg rounded-4">
          <div class="card-header bg-white fw-semibold text-secondary">🎁 Sản phẩm đang live</div>
          <div class="card-body">

            <!-- ⭐ Sản phẩm đã chọn -->
            <div v-if="state.selectedProduct" class="selected-product card border-0 shadow-sm mb-4">
              <div class="row g-0 align-items-center">
                <div class="col-4">
                  <img :src="state.selectedProduct.url" alt="product" class="img-fluid rounded-start selected-img">
                </div>
                <div class="col-8">
                  <div class="card-body py-2 px-3">
                    <h6 class="fw-semibold mb-1 text-truncate">{{ state.selectedProduct.tenSanPham }}</h6>
                    <p class="text-danger fw-bold small mb-2">{{ state.selectedProduct.giaBan }}₫</p>
                    <button class="btn btn-sm btn-success w-100 fw-semibold" :disabled="!state.isLoggedIn"
                      @click="detailProduct(state.selectedProduct.id)">
                      Xem chi tiết
                    </button>
                  </div>
                </div>
              </div>
            </div>

            <!-- 🔍 Search -->
            <input type="text" class="form-control mb-3" placeholder="Tìm sản phẩm..." v-model="searchQuery">

            <!-- 📋 Danh sách sản phẩm -->
            <div v-for="product in listProduct" :key="product.id" class="product-card card border-0 shadow-sm mb-3"
              @click="selectProduct(product)">
              <div class="row g-0 align-items-center">
                <div class="col-4">
                  <img :src="product.url" alt="product" class="img-fluid rounded-start product-img">
                </div>
                <div class="col-8">
                  <div class="card-body py-2 px-3">
                    <h6 class="fw-semibold mb-1 text-truncate">{{ product.tenSanPham }}</h6>
                    <p class="text-danger fw-bold small mb-1">{{ product.giaBan }}₫</p>
                    <button class="btn btn-sm btn-outline-primary w-100" :disabled="!state.isLoggedIn"
                      @click.stop="detailProduct(product.id)">
                      Xem chi tiết
                    </button>
                  </div>
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>

    </div>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted, nextTick, onUnmounted, watch } from "vue";
import { useRouter } from "vue-router";
import SockJS from "sockjs-client";
import Stomp from "stompjs";
import axios from "axios";

const router = useRouter();
const user = ref(null);

const remoteVideoRef = ref(null);
const chatContainer = ref(null);

const searchQuery = ref('');
const listProduct = ref([]);

const state = reactive({
  selectedProduct: null,
  chatMessages: [],
  chatInput: "",
  isLive: false,
  isVideoLoaded: false,
  isLoggedIn: false
});

let stompClient = null;
let pc = null;
let streamId = null;
let viewerId = null;
let viewerName = null;
const pendingCandidates = {};

const fetchProducts = async () => {
  try {
    const res = await axios.get("http://localhost:8080/live-san-pham", {
      params: { search: searchQuery.value, page: 0, size: 10 },
    });
    listProduct.value = res.data.content;
  } catch (err) {
    console.error("Error fetching products:", err);
  }
};

const fetchCurrentStream = async () => {
  try {
    const res = await axios.get("http://localhost:8080/live-stream/current");
    if (res.data.streamId) {
      streamId = res.data.streamId;
      state.isLive = true;
    }
  } catch (error) {
    console.error("Error fetching streamId:", error);
  }
};

const fetchHistory = async () => {
  if (!streamId) return;
  try {
    const resProd = await axios.get("http://localhost:8080/live-products", { params: { streamId } });
    if (resProd.data.length > 0) {
      state.selectedProduct = resProd.data.at(-1).product;
    }

    const resChat = await axios.get("http://localhost:8080/live-chats", { params: { streamId } });
    state.chatMessages = resChat.data;

    nextTick(() => chatContainer.value && (chatContainer.value.scrollTop = chatContainer.value.scrollHeight));
  } catch (err) {
    console.error("Error fetching history:", err);
  }
};

async function makePeerConnectionAndOffer() {
  if (pc) pc.close();

  pc = new RTCPeerConnection({ iceServers: [{ urls: "stun:stun.l.google.com:19302" }] });

  pc.ontrack = (e) => {
    nextTick(() => {
      if (remoteVideoRef.value) {
        remoteVideoRef.value.srcObject = e.streams[0];
        state.isVideoLoaded = true;
      }
    });
  };

  pc.onicecandidate = (e) => {
    if (e.candidate) {
      stompClient.send(`/app/signal/${streamId}`, {}, JSON.stringify({
        type: 'candidate',
        candidate: e.candidate,
        viewerId
      }));
    }
  };

  pc.addTransceiver('video', { direction: 'recvonly' });
  pc.addTransceiver('audio', { direction: 'recvonly' });

  const offer = await pc.createOffer();
  await pc.setLocalDescription(offer);

  stompClient.send(`/app/signal/${streamId}`, {}, JSON.stringify({
    type: 'offer',
    sdp: offer.sdp,
    viewerId
  }));
}

const connectViewerWebSocket = () => {
  const socket = new SockJS("http://localhost:8080/ws");
  stompClient = Stomp.over(socket);

  stompClient.connect({}, async () => {
    stompClient.send(`/app/join/${streamId}`, {}, JSON.stringify({ viewerId }));

    stompClient.subscribe(`/topic/chat/${streamId}`, msg => {
      const chat = JSON.parse(msg.body);
      state.chatMessages.push(chat);
      nextTick(() => chatContainer.value && (chatContainer.value.scrollTop = chatContainer.value.scrollHeight));
    });

    stompClient.subscribe(`/topic/product/${streamId}`, msg => {
      const data = JSON.parse(msg.body);
      if (data.type === "product") state.selectedProduct = data.product;
    });

    stompClient.subscribe(`/topic/stream/global`, msg => {
      const data = JSON.parse(msg.body);
      if (data.type === 'start') {
        streamId = data.streamId;
        state.isLive = true;
      }
      if (data.type === 'end') {
        state.isLive = false;
        state.isVideoLoaded = false;
        if (pc) pc.close();
        pc = null;
        streamId = null;
      }
    });

    stompClient.subscribe(`/topic/signal/${streamId}`, async msg => {
      const signal = JSON.parse(msg.body);

      if (signal.type === 'answer' && pc && !pc.currentRemoteDescription) {
        await pc.setRemoteDescription({ type: 'answer', sdp: signal.sdp });
        if (pendingCandidates[streamId]) {
          for (const c of pendingCandidates[streamId]) await pc.addIceCandidate(c);
          delete pendingCandidates[streamId];
        }
      }

      if (signal.type === 'candidate' && pc) {
        if (pc.remoteDescription) {
          try { await pc.addIceCandidate(signal.candidate); } catch (e) { console.error(e); }
        } else {
          (pendingCandidates[streamId] ||= []).push(signal.candidate);
        }
      }
    });

    await makePeerConnectionAndOffer();
  });
};

const loadVideo = () => {
  if (!streamId) return;
  if (remoteVideoRef.value) remoteVideoRef.value.srcObject = null;
  if (state.isLive) fetchHistory();
  connectViewerWebSocket();
};

const sendChat = () => {
  if (!state.chatInput.trim() || !stompClient) return;
  const message = { id: Date.now(), user: 'Viewer', idNguoiGui: viewerId, tenNguoiGui: viewerName, content: state.chatInput };
  stompClient.send(`/app/chat/${streamId}`, {}, JSON.stringify(message));
  state.chatInput = '';
};

const detailProduct = (id) => router.push(`/client/detail/${id}`);

const checkUser = () => {
  user.value = JSON.parse(localStorage.getItem("user")) || null;
  state.isLoggedIn = !!user.value?.id;
  viewerId = user.value?.id || Date.now();
  viewerName = user.value?.fullName || 'Người xem';
};

watch(searchQuery, fetchProducts);

onMounted(async () => {
  checkUser();
  await fetchCurrentStream();
  fetchProducts();
});

onUnmounted(() => {
  if (stompClient) stompClient.disconnect();
  if (pc) pc.close();
});
</script>

<style scoped>
.product-card {
  border-radius: 12px;
  transition: all 0.2s ease-in-out;
  cursor: pointer;
}

.product-card:hover {
  transform: translateY(-3px);
  box-shadow: 0 6px 16px rgba(0, 0, 0, 0.12);
}

.product-img {
  height: 70%;
  width: 70%;
  object-fit: cover;
  border-radius: 12px 0 0 12px;
}

.selected-product {
  border-radius: 12px;
  transition: all 0.2s ease-in-out;
}

.selected-product:hover {
  transform: translateY(-3px);
  box-shadow: 0 6px 16px rgba(0, 0, 0, 0.12);
}

.selected-img {
  height: 70%;
  width: 70%;
  object-fit: cover;
  border-radius: 12px 0 0 12px;
}

.selected-product .btn-success {
  background-color: #28a745;
  /* xanh lá đậm */
  border: none;
}

.selected-product .btn-success:hover {
  background-color: #218838;
  /* xanh lá đậm hơn khi hover */
}
</style>