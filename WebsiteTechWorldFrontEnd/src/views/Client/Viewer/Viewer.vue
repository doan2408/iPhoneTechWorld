<template>
  <div class="container-fluid py-4 bg-light min-vh-100">
    <div class="row g-4">
      <!-- Video + Chat -->
      <div class="col-lg-8 d-flex flex-column">
        <!-- Video -->
        <div class="bg-dark rounded-4 d-flex align-items-center justify-content-center" style="height: 500px;">
          <video v-if="state.isLive" ref="remoteVideoRef" autoplay playsinline
            style="width: 100%; height: 100%; background-color:black;">
          </video>
          <span v-else class="text-white fs-4">🔴 Live đã kết thúc</span>
        </div>

        <!-- Chat -->
        <div class="card shadow-lg rounded-4 flex-grow-1 mt-4" style="height: 300px;">
          <div class="card-header bg-white border-bottom fw-semibold text-secondary">
            💬 Chat trực tiếp
          </div>
          <div class="card-body overflow-auto p-3" ref="chatContainer">
            <div v-for="(msg, i) in state.chatMessages" :key="i" class="d-flex mb-2"
              :class="msg.user === 'Viewer' ? 'justify-content-end' : 'justify-content-start'">
              <div class="px-3 py-2 rounded-4" :class="msg.user === 'Viewer'
                ? 'bg-primary text-white'
                : 'bg-light text-dark border'" style="max-width: 70%;">
                <p class="mb-1 fw-bold small">{{ msg.user }}</p>
                <p class="mb-0 small">{{ msg.content }}</p>
              </div>
            </div>
          </div>
          <div class="card-footer d-flex align-items-center gap-2">
            <input v-if="state.isLoggedIn" v-model="state.chatInput" type="text" class="form-control"
              placeholder="Nhập tin nhắn..." @keyup.enter="sendComment" />
            <button v-if="state.isLoggedIn" class="btn btn-primary" @click="sendComment">
              Gửi
            </button>
            <p v-else class="text-muted small mb-0">Đăng nhập để chat</p>
          </div>
        </div>
      </div>

      <!-- Sản phẩm -->
      <div class="col-lg-4">
        <div class="card shadow-lg rounded-4">
          <div class="card-header bg-white fw-semibold text-secondary">
            🎁 Sản phẩm đang live
          </div>
          <div class="card-body">
            <div v-for="product in state.products" :key="product.id"
              class="card mb-3 border-0 shadow-sm">
              <div class="card-body d-flex justify-content-between align-items-center">
                <div>
                  <p class="fw-medium mb-1">{{ product.tenSanPham }}</p>
                  <p class="text-muted small mb-0">{{ product.giaBan }}₫</p>
                </div>
                <button class="btn btn-sm btn-primary" @click="buyProduct(product.id)"
                  :disabled="!state.isLoggedIn">
                  Mua ngay
                </button>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted, onUnmounted, watch, nextTick } from "vue";
import SockJS from "sockjs-client";
import Stomp from "stompjs";
import { ElMessage } from "element-plus";
import { v4 as uuidv4 } from 'uuid';

const remoteVideoRef = ref(null);
const chatContainer = ref(null);

const state = reactive({
  products: [],
  chatMessages: [],
  chatInput: "",
  isLoggedIn: true,
  isLive: false,
});

let stompClient = null;
let pc = null;
const streamId = "123";
const viewerId = uuidv4();
const pendingCandidates = {};

const connectViewerWebSocket = () => {
  const socket = new SockJS("http://localhost:8080/ws");
  stompClient = Stomp.over(socket);
  stompClient.connect({}, () => {
    // Thông báo người xem tham gia
    stompClient.send(`/app/join/${streamId}`, {}, JSON.stringify({ viewerId }));

    // Xử lý tín hiệu WebRTC
    stompClient.subscribe(`/topic/signal/${streamId}`, async (msg) => {
      const signal = JSON.parse(msg.body);
      if (signal.viewerId !== viewerId) return;

      if (signal.type === "offer") {
        pc = new RTCPeerConnection();
        pc.ontrack = (event) => {
          state.isLive = true;
          remoteVideoRef.value.srcObject = event.streams[0];
          event.streams[0].getTracks().forEach(track => {
            track.onended = () => {
              cleanupStream();
              state.isLive = false;
              ElMessage.warning("🔴 Live đã kết thúc");
            };
          });
        };
        pc.onicecandidate = (event) => {
          if (event.candidate) {
            stompClient.send(`/app/signal/${streamId}`, {}, JSON.stringify({
              type: "candidate",
              candidate: event.candidate,
              viewerId,
            }));
          }
        };
        await pc.setRemoteDescription({ type: "offer", sdp: signal.sdp });
        const answer = await pc.createAnswer();
        await pc.setLocalDescription(answer);
        stompClient.send(`/app/signal/${streamId}`, {}, JSON.stringify({
          type: "answer",
          sdp: answer.sdp,
          viewerId,
        }));
        if (pendingCandidates[viewerId]) {
          for (const candidate of pendingCandidates[viewerId]) {
            await pc.addIceCandidate(candidate).catch(err => console.error("Thêm ICE thất bại:", err));
          }
          delete pendingCandidates[viewerId];
        }
      } else if (signal.type === "candidate") {
        if (pc && pc.remoteDescription) {
          await pc.addIceCandidate(signal.candidate).catch(err => console.error("Thêm ICE thất bại:", err));
        } else {
          if (!pendingCandidates[viewerId]) pendingCandidates[viewerId] = [];
          pendingCandidates[viewerId].push(signal.candidate);
        }
      } else if (signal.type === "end") {
        cleanupStream();
        state.isLive = false;
        ElMessage.warning("🔴 Live đã kết thúc");
      }
    });

    // Xử lý tin nhắn chat
    stompClient.subscribe(`/topic/chat/${streamId}`, (msg) => {
      const incoming = JSON.parse(msg.body);
      if (!state.chatMessages.some(m => m.id === incoming.id)) {
        state.chatMessages.push(incoming);
        if (state.chatMessages.length > 100) {
          state.chatMessages.shift();
        }
      }
    });

    // Nhận sản phẩm được chọn từ Streamer
    stompClient.subscribe(`/topic/product/${streamId}`, (msg) => {
      const { product } = JSON.parse(msg.body);
      state.products = [product]; // Chỉ hiển thị sản phẩm đang được giới thiệu
    });
  }, (err) => {
    console.error("WebSocket connection failed:", err);
    ElMessage.error("Không thể kết nối WebSocket");
  });
};

const sendComment = () => {
  if (!state.chatInput.trim() || !stompClient || !stompClient.connected) return;
  const message = {
    id: Date.now(),
    user: "Viewer",
    content: state.chatInput
  };
  state.chatMessages.push(message);
  stompClient.send(`/app/chat/${streamId}`, {}, JSON.stringify(message));
  state.chatInput = "";
};

const cleanupStream = () => {
  if (remoteVideoRef.value && remoteVideoRef.value.srcObject) {
    remoteVideoRef.value.srcObject.getTracks().forEach(track => track.stop());
    remoteVideoRef.value.srcObject = null;
  }
  if (pc) {
    pc.close();
    pc = null;
  }
  pendingCandidates[viewerId] = [];
  state.isLive = false;
};

const buyProduct = (id) => {
  ElMessage.success(`Bạn đã mua sản phẩm #${id}`);
};

watch(() => state.chatMessages.length, async () => {
  await nextTick();
  if (chatContainer.value) {
    chatContainer.value.scrollTop = chatContainer.value.scrollHeight;
  }
});

onMounted(() => {
  connectViewerWebSocket();
});

onUnmounted(() => {
  cleanupStream();
  if (stompClient) stompClient.disconnect();
});
</script>