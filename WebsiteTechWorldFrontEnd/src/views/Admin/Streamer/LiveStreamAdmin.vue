<template>
  <div class="container py-4">
    <div class="row">
      <!-- Video + Start Live -->
      <div class="col-lg-8 mb-4">
        <div class="bg-dark rounded-4 d-flex align-items-center justify-content-center" style="height: 400px;">
          <video ref="localVideoRef" autoplay playsinline muted style="width: 100%; height: 100%;"></video>
        </div>
        <div class="text-center mt-3">
          <button v-if="!isStreaming" class="btn btn-danger px-4 py-2 fw-bold" @click="startStreaming">
            🚀 Bắt đầu Live
          </button>
          <button v-else class="btn btn-secondary px-4 py-2 fw-bold" @click="stopStreaming">
            ⏹ Dừng Live
          </button>
        </div>
      </div>

      <!-- Danh sách sản phẩm -->
      <div class="col-lg-4 mb-4">
        <div class="card shadow-sm">
          <div class="card-header bg-white fw-bold">
            📦 Sản phẩm đang bán
          </div>
          <div class="card-body">
            <input type="text" class="form-control mb-3" v-model="searchQuery" placeholder="🔍 Tìm sản phẩm..." />
            <select class="form-select mb-3" v-model="state.selectedProduct" @change="sendSelectedProduct">
              <option disabled value="">Chọn sản phẩm để giới thiệu</option>
              <option v-for="p in state.products" :key="p.id" :value="p.id">
                {{ p.maSanPham }} - {{ p.tenSanPham }}
              </option>
            </select>
            <div class="list-group" style="max-height: 250px; overflow-y: auto;">
              <div v-for="(p, index) in state.products" :key="p.id"
                class="list-group-item d-flex justify-content-between align-items-center">
                <span>{{ index + 1 }}</span>
                <span>{{ p.maSanPham }}</span>
                <span>{{ p.tenSanPham }}</span>
                <span class="text-danger fw-bold">{{ p.giaBan }}₫</span>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>

    <!-- Chat box -->
    <div class="card shadow-sm">
      <div class="card-header bg-white fw-bold">
        💬 Chat với khán giả
      </div>
      <div class="card-body" style="max-height: 250px; overflow-y: auto; background-color: #f9fafb;" ref="chatContainer">
        <div v-for="(msg, i) in state.chatMessages" :key="i" class="mb-2">
          <div :class="msg.user === 'Streamer' ? 'text-end' : 'text-start'">
            <div class="p-2 rounded shadow-sm d-inline-block"
              :class="msg.user === 'Streamer' ? 'msg-streamer' : 'msg-viewer'" style="margin: 5px 0;">
              <strong>{{ msg.user }}</strong><br />
              {{ msg.content }}
            </div>
          </div>
        </div>
      </div>
      <div class="card-footer d-flex">
        <input type="text" class="form-control me-2" v-model="state.chatInput" placeholder="Nhập phản hồi..."
          @keyup.enter="sendReply" />
        <button class="btn btn-success" @click="sendReply">Gửi</button>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted, onUnmounted, watch, nextTick } from "vue";
import SockJS from "sockjs-client";
import Stomp from "stompjs";
import { loadSanPham } from "@/Service/Adminservice/Products/ProductAdminService";
import { useToast } from "vue-toastification";

const toast = useToast();
const localVideoRef = ref(null);
const searchQuery = ref('');
const isStreaming = ref(false);
const chatContainer = ref(null);

const state = reactive({
  selectedProduct: null,
  products: [],
  chatMessages: [],
  chatInput: "",
  isLive: isStreaming,
});

let stompClient = null;
let localStream = null;
const streamId = "123";
const streamerId = "streamer_" + Date.now(); // Tạo ID động cho streamer
const peerConnections = new Map();
const pendingCandidates = {};

const connectStreamerWebSocket = () => {
  const socket = new SockJS("http://localhost:8080/ws");
  stompClient = Stomp.over(socket);
  stompClient.connect({}, () => {
    // Xử lý tín hiệu WebRTC
    stompClient.subscribe(`/topic/signal/${streamId}`, async (msg) => {
      const signal = JSON.parse(msg.body);
      const viewerId = signal.viewerId;

      if (!viewerId) return; // Bỏ qua nếu không có viewerId

      if (signal.type === "answer") {
        const pc = peerConnections.get(viewerId);
        if (pc) {
          await pc.setRemoteDescription({ type: "answer", sdp: signal.sdp });
          // Xử lý các ICE candidate đang chờ
          if (pendingCandidates[viewerId]) {
            for (const candidate of pendingCandidates[viewerId]) {
              await pc.addIceCandidate(candidate).catch(err => console.error("Thêm ICE thất bại:", err));
            }
            delete pendingCandidates[viewerId];
          }
        }
      } else if (signal.type === "candidate") {
        const pc = peerConnections.get(viewerId);
        if (pc && pc.remoteDescription) {
          await pc.addIceCandidate(signal.candidate).catch(err => console.error("Thêm ICE thất bại:", err));
        } else {
          if (!pendingCandidates[viewerId]) pendingCandidates[viewerId] = [];
          pendingCandidates[viewerId].push(signal.candidate);
        }
      }
    });

    // Xử lý khi người xem mới tham gia
    stompClient.subscribe(`/topic/join/${streamId}`, async (msg) => {
      const { viewerId } = JSON.parse(msg.body);
      if (!viewerId || !localStream) return;

      const pc = new RTCPeerConnection();
      peerConnections.set(viewerId, pc);
      localStream.getTracks().forEach((track) => pc.addTrack(track, localStream));
      pc.onicecandidate = (event) => {
        if (event.candidate) {
          stompClient.send(`/app/signal/${streamId}`, {}, JSON.stringify({
            type: "candidate",
            candidate: event.candidate,
            viewerId,
            streamerId,
          }));
        }
      };
      const offer = await pc.createOffer();
      await pc.setLocalDescription(offer);
      stompClient.send(`/app/signal/${streamId}`, {}, JSON.stringify({
        type: "offer",
        sdp: offer.sdp,
        viewerId,
        streamerId,
      }));
    });

    // Xử lý tin nhắn chat
    stompClient.subscribe(`/topic/chat/${streamId}`, (msg) => {
      const incoming = JSON.parse(msg.body);
      if (!state.chatMessages.some(m => m.id === incoming.id)) {
        state.chatMessages.push(incoming);
        if (state.chatMessages.length > 100) { // Giới hạn số lượng tin nhắn
          state.chatMessages.shift();
        }
      }
    });
  }, (err) => {
    console.error("WebSocket connection failed:", err);
    toast.error("Không thể kết nối WebSocket");
  });
};

const startStreaming = async () => {
  try {
    localStream = await navigator.mediaDevices.getUserMedia({ video: true, audio: true });
    localVideoRef.value.srcObject = localStream;
    isStreaming.value = true;
    state.isLive = true;

    // Thông báo bắt đầu stream
    if (stompClient && stompClient.connected) {
      stompClient.send(`/app/stream/start/${streamId}`, {}, JSON.stringify({ streamerId }));
    }
  } catch (err) {
    console.error("Lỗi streamer:", err);
    toast.error("Không thể mở camera/micro");
  }
};

const stopStreaming = () => {
  try {
    if (localStream) {
      localStream.getTracks().forEach(track => track.stop());
      localStream = null;
    }
    if (localVideoRef.value) {
      localVideoRef.value.srcObject = null;
    }
    peerConnections.forEach(pc => pc.close());
    peerConnections.clear();
    pendingCandidates.value = {};

    if (stompClient && stompClient.connected) {
      stompClient.send(`/app/stream/stop/${streamId}`, {}, JSON.stringify({ type: "end", streamerId }));
    }

    isStreaming.value = false;
    state.isLive = false;
  } catch (err) {
    console.error("Lỗi khi dừng live:", err);
    toast.error("Không thể dừng live");
  }
};

const sendReply = () => {
  if (!state.chatInput.trim() || !stompClient || !stompClient.connected) return;
  const message = {
    id: Date.now(),
    user: "Streamer",
    content: state.chatInput
  };
  state.chatMessages.push(message);
  stompClient.send(`/app/chat/${streamId}`, {}, JSON.stringify(message));
  state.chatInput = "";
};

const sendSelectedProduct = () => {
  if (!state.selectedProduct || !stompClient || !stompClient.connected) return;
  const product = state.products.find(p => p.id === state.selectedProduct);
  if (product) {
    stompClient.send(`/app/product/${streamId}`, {}, JSON.stringify({
      type: "product",
      product: { id: product.id, maSanPham: product.maSanPham, tenSanPham: product.tenSanPham, giaBan: product.giaBan }
    }));
  }
};

const loadProducts = async () => {
  try {
    const response = await loadSanPham(searchQuery.value, 0, 7);
    state.products = response.data.content;
  } catch (error) {
    console.error("Lỗi khi load danh sách sản phẩm:", error);
    toast.error("Lỗi khi load danh sách sản phẩm");
  }
};

watch(searchQuery, () => {
  loadProducts();
});

watch(() => state.chatMessages.length, async () => {
  await nextTick();
  if (chatContainer.value) {
    chatContainer.value.scrollTop = chatContainer.value.scrollHeight;
  }
});

onMounted(() => {
  connectStreamerWebSocket();
  loadProducts();
});

onUnmounted(() => {
  stopStreaming();
  if (stompClient) stompClient.disconnect();
});
</script>

<style scoped>
.msg-streamer {
  background-color: #28a745 !important;
  color: #fff;
}
.msg-viewer {
  background-color: #fff;
  color: #000;
}
</style>