<template>
  <div class="container py-4">
    <div class="row">
      <!-- Video + Start/Stop -->
      <div class="col-lg-8 mb-4">
        <div class="bg-dark rounded-4 d-flex align-items-center justify-content-center" style="height: 400px;">
          <video ref="localVideoRef" autoplay playsinline muted style="width: 100%; height: 100%;"></video>
        </div>
        <div class="text-center mt-3">
          <button v-if="!isStreaming" class="btn btn-danger px-4 py-2 fw-bold" @click="startStreaming">🚀 Bắt đầu Live</button>
          <button v-else class="btn btn-secondary px-4 py-2 fw-bold" @click="stopStreaming">⏹ Dừng Live</button>
        </div>
      </div>

      <!-- Sản phẩm -->
      <div class="col-lg-4 mb-4" v-if="isStreaming">
        <div class="card shadow-sm">
          <div class="card-header bg-white fw-bold">📦 Sản phẩm đang bán</div>
          <div class="card-body">
            <input type="text" class="form-control mb-3" v-model="searchQuery" placeholder="🔍 Tìm sản phẩm..." />
            <select class="form-select mb-3" v-model="state.selectedProduct" @change="sendSelectedProduct">
              <option disabled value="">Chọn sản phẩm</option>
              <option v-for="p in listProduct" :key="p.id" :value="p.id">{{ p.maSanPham }} - {{ p.tenSanPham }}</option>
            </select>
            <div class="list-group" style="max-height: 250px; overflow-y: auto;">
              <div v-for="(p,index) in listProduct" :key="p.id" class="list-group-item d-flex justify-content-between">
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

    <!-- Chat -->
    <div class="card shadow-sm" v-if="isStreaming">
      <div class="card-header bg-white fw-bold">💬 Chat với khán giả</div>
      <div class="card-body" style="max-height: 250px; overflow-y:auto;" ref="chatContainer">
        <div v-for="(msg,i) in state.chatMessages" :key="i" class="mb-2">
          <div :class="msg.user==='Streamer' && msg.idNguoiGui === streamerId ? 'text-end':'text-start'">
            <div class="p-2 rounded shadow-sm d-inline-block"
                 :class="msg.user==='Streamer' && msg.idNguoiGui === streamerId ? 'bg-success text-white':'bg-light text-dark'" style="margin:5px 0;">
              <strong>{{ msg.tenNguoiGui }}</strong><br/>
              {{ msg.content }}
            </div>
          </div>
        </div>
      </div>
      <div class="card-footer d-flex">
        <input type="text" class="form-control me-2" v-model="state.chatInput" placeholder="Nhập tin nhắn..." @keyup.enter="sendChat" />
        <button class="btn btn-success" @click="sendChat">Gửi</button>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted, nextTick, onUnmounted, watch } from "vue";
import SockJS from "sockjs-client";
import Stomp from "stompjs";
import axios from "axios";

const user = ref(null);

const localVideoRef = ref(null);
const chatContainer = ref(null);
const searchQuery = ref('');
const listProduct = ref([]);
const isStreaming = ref(false);

const state = reactive({
  selectedProduct: null,
  chatMessages: [],
  chatInput: ""
});

let stompClient = null;
let localStream = null;
let streamId = null;
let streamerId = null;
let streamerName = null;
const peerConnections = new Map();
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

const fetchStreamId = async () => {
  const res = await axios.post("http://localhost:8080/live-stream/create");
  streamId = res.data.streamId;
};

const connectWebSocket = () => {
  const socket = new SockJS("http://localhost:8080/ws");
  stompClient = Stomp.over(socket);

  stompClient.connect({}, () => {
    stompClient.subscribe(`/topic/chat/${streamId}`, msg => {
      const incoming = JSON.parse(msg.body);
      if (!state.chatMessages.some(m => m.id === incoming.id)) state.chatMessages.push(incoming);
      nextTick(() => chatContainer.value && (chatContainer.value.scrollTop = chatContainer.value.scrollHeight));
    });

    stompClient.subscribe(`/topic/signal/${streamId}`, async (msg) => {
      const signal = JSON.parse(msg.body);

      if (signal.type === "offer") {
        if (peerConnections.has(signal.viewerId)) {
          peerConnections.get(signal.viewerId).close();
          peerConnections.delete(signal.viewerId);
        }

        const pc = new RTCPeerConnection({ iceServers: [{ urls: "stun:stun.l.google.com:19302" }] });
        localStream.getTracks().forEach(track => pc.addTrack(track, localStream));

        pc.onicecandidate = (e) => {
          if (e.candidate) {
            stompClient.send(`/app/signal/${streamId}`, {}, JSON.stringify({
              type: "candidate",
              candidate: e.candidate,
              viewerId: signal.viewerId
            }));
          }
        };

        await pc.setRemoteDescription({ type: "offer", sdp: signal.sdp });
        const answer = await pc.createAnswer();
        await pc.setLocalDescription(answer);

        stompClient.send(`/app/signal/${streamId}`, {}, JSON.stringify({
          type: "answer",
          sdp: answer.sdp,
          viewerId: signal.viewerId
        }));

        peerConnections.set(signal.viewerId, pc);
      }

      else if (signal.type === "candidate" && peerConnections.has(signal.viewerId)) {
        const pc = peerConnections.get(signal.viewerId);
        if (pc.remoteDescription) {
          try { await pc.addIceCandidate(signal.candidate); } catch (e) { console.error(e); }
        } else {
          (pendingCandidates[signal.viewerId] ||= []).push(signal.candidate);
        }
      }
    });
  });
};

const startStreaming = async () => {
  await fetchStreamId();
  localStream = await navigator.mediaDevices.getUserMedia({ video: true, audio: true });
  localVideoRef.value.srcObject = localStream;
  await connectWebSocket();
  isStreaming.value = true;
};

const stopStreaming = async () => {
  if (localStream) localStream.getTracks().forEach(t => t.stop());
  peerConnections.forEach((pc) => pc.close());
  peerConnections.clear();
  await axios.post("http://localhost:8080/live-stream/stop");
  state.selectedProduct = null;
  state.chatMessages = [];
  state.chatInput = '';
  isStreaming.value = false;
};

const sendSelectedProduct = () => {
  const product = listProduct.value.find(p => p.id === state.selectedProduct);
  if (!product || !stompClient) return;
  stompClient.send(`/app/product/${streamId}`, {}, JSON.stringify({ type: 'product', product }));
};

const sendChat = () => {
  if (!state.chatInput.trim() || !stompClient) return;
  const message = { id: Date.now(), user: 'Streamer', idNguoiGui: streamerId, tenNguoiGui: streamerName, content: state.chatInput };
  state.chatMessages.push(message);
  stompClient.send(`/app/chat/${streamId}`, {}, JSON.stringify(message));
  state.chatInput = '';
};

const checkUser = () => {
  user.value = JSON.parse(localStorage.getItem("user")) || null;
  streamerId = user.value?.id || Date.now();
  streamerName = user.value?.fullName || 'Người bán';
};

watch(searchQuery, fetchProducts);

onMounted(() => {
  checkUser();
  fetchProducts();
});

onUnmounted(() => {
  stopStreaming();
  if (stompClient) stompClient.disconnect();
});
</script>