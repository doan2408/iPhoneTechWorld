<template>
    <Transition name="slide-up">
        <div class="compare-bar" v-if="compareList && compareList.length">
            <div class="compare-container">
                <div class="compare-items">
                    <TransitionGroup name="item" tag="div" class="items-wrapper">
                        <div class="compare-item" v-for="item in compareList" :key="item.id">
                            <div class="item-image">
                                <img :src="item.hinhAnh" :alt="item.tenSanPham" />
                            </div>
                            <div class="item-info">
                                <span class="item-name">{{ item.tenSanPham }}</span>
                            </div>
                            <button class="remove-btn" @click="$emit('remove', item)"
                                :aria-label="`Remove ${item.tenSanPham} from comparison`">
                                <svg width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor"
                                    stroke-width="2">
                                    <line x1="18" y1="6" x2="6" y2="18"></line>
                                    <line x1="6" y1="6" x2="18" y2="18"></line>
                                </svg>
                            </button>
                        </div>
                    </TransitionGroup>
                </div>

                <div class="compare-actions">
                    <div class="item-count">
                        {{ compareList.length }}/{{ maxItems || 3 }} sản phẩm
                    </div>
                    <button class="compare-btn" @click="goToComparePage" :disabled="compareList.length < 2">
                        <svg width="18" height="18" viewBox="0 0 24 24" fill="none" stroke="currentColor"
                            stroke-width="2">
                            <path d="M9 11H5a2 2 0 0 0-2 2v7a2 2 0 0 0 2 2h4a2 2 0 0 0 2-2v-7a2 2 0 0 0-2-2z"></path>
                            <path d="M19 11h-4a2 2 0 0 0-2 2v7a2 2 0 0 0 2 2h4a2 2 0 0 0 2-2v-7a2 2 0 0 0-2-2z"></path>
                            <path d="M7 21V8a2 2 0 0 1 2-2h6a2 2 0 0 1 2 2v13"></path>
                        </svg>
                        So sánh ngay
                    </button>
                </div>
            </div>
        </div>
    </Transition>
</template>

<script setup>
import { useRouter } from 'vue-router'

const props = defineProps({
    compareList: {
        type: Array,
        default: () => []
    },
    maxItems: {
        type: Number,
        default: 3
    }
})

const emit = defineEmits(['remove'])
const router = useRouter()

const goToComparePage = () => {
    if (props.compareList.length < 2) return

    router.push({
        name: 'comparePage',
        query: { ids: props.compareList.map(i => i.id).join(',') }
    })
}
</script>

<style scoped>
.compare-bar {
    position: fixed;
    bottom: 0;
    left: 0;
    right: 0;
    background: linear-gradient(45deg, #003cff, #3fb5f0);
    backdrop-filter: blur(10px);
    border-top: 1px solid rgba(255, 255, 255, 0.1);
    padding: 16px 20px;
    box-shadow: 0 -8px 32px rgba(0, 0, 0, 0.15);
    z-index: 1000;
}

.compare-container {
    max-width: 1200px;
    margin: 0 auto;
    display: flex;
    align-items: center;
    gap: 20px;
}

.compare-items {
    flex: 1;
    overflow-x: auto;
    scrollbar-width: none;
    -ms-overflow-style: none;
}

.compare-items::-webkit-scrollbar {
    display: none;
}

.items-wrapper {
    display: flex;
    gap: 12px;
    min-width: fit-content;
}

.compare-item {
    display: flex;
    align-items: center;
    gap: 12px;
    background: rgba(255, 255, 255, 0.95);
    backdrop-filter: blur(10px);
    padding: 12px 16px;
    border-radius: 12px;
    box-shadow: 0 4px 16px rgba(0, 0, 0, 0.1);
    border: 1px solid rgba(255, 255, 255, 0.2);
    transition: all 0.3s cubic-bezier(0.4, 0, 0.2, 1);
    min-width: 200px;
    position: relative;
    overflow: hidden;
}

.compare-item::before {
    content: '';
    position: absolute;
    top: 0;
    left: 0;
    right: 0;
    height: 2px;
    background: linear-gradient(90deg, #667eea, #764ba2);
}

.compare-item:hover {
    transform: translateY(-2px);
    box-shadow: 0 8px 24px rgba(0, 0, 0, 0.15);
}

.item-image {
    position: relative;
    flex-shrink: 0;
}

.item-image img {
    width: 48px;
    height: 48px;
    object-fit: cover;
    border-radius: 8px;
    border: 2px solid rgba(255, 255, 255, 0.8);
    transition: transform 0.3s ease;
}

.compare-item:hover .item-image img {
    transform: scale(1.05);
}

.item-info {
    flex: 1;
    min-width: 0;
}

.item-name {
    font-size: 14px;
    font-weight: 600;
    color: #2d3748;
    display: block;
    white-space: nowrap;
    overflow: hidden;
    text-overflow: ellipsis;
    line-height: 1.4;
}

.remove-btn {
    display: flex;
    align-items: center;
    justify-content: center;
    width: 32px;
    height: 32px;
    border: none;
    background: rgba(239, 68, 68, 0.1);
    color: #ef4444;
    border-radius: 8px;
    cursor: pointer;
    transition: all 0.3s ease;
    flex-shrink: 0;
}

.remove-btn:hover {
    background: rgba(239, 68, 68, 0.2);
    transform: scale(1.1);
}

.remove-btn:active {
    transform: scale(0.95);
}

.compare-actions {
    display: flex;
    align-items: center;
    gap: 16px;
    flex-shrink: 0;
}

.item-count {
    font-size: 14px;
    color: rgba(255, 255, 255, 0.9);
    font-weight: 500;
    white-space: nowrap;
}

.compare-btn {
    display: flex;
    align-items: center;
    gap: 8px;
    background: rgba(255, 255, 255, 0.95);
    color: #667eea;
    border: none;
    padding: 12px 24px;
    border-radius: 12px;
    cursor: pointer;
    font-weight: 600;
    font-size: 15px;
    transition: all 0.3s cubic-bezier(0.4, 0, 0.2, 1);
    box-shadow: 0 4px 16px rgba(0, 0, 0, 0.1);
    white-space: nowrap;
    position: relative;
    overflow: hidden;
}

.compare-btn::before {
    content: '';
    position: absolute;
    top: 0;
    left: -100%;
    width: 100%;
    height: 100%;
    background: linear-gradient(90deg, transparent, rgba(255, 255, 255, 0.4), transparent);
    transition: left 0.5s;
}

.compare-btn:hover::before {
    left: 100%;
}

.compare-btn:hover {
    transform: translateY(-2px);
    box-shadow: 0 8px 24px rgba(0, 0, 0, 0.15);
    background: #ffffff;
}

.compare-btn:active {
    transform: translateY(0);
}

.compare-btn:disabled {
    opacity: 0.6;
    cursor: not-allowed;
    transform: none;
}

.compare-btn:disabled:hover {
    transform: none;
    box-shadow: 0 4px 16px rgba(0, 0, 0, 0.1);
}

/* Animations */
.slide-up-enter-active,
.slide-up-leave-active {
    transition: all 0.4s cubic-bezier(0.4, 0, 0.2, 1);
}

.slide-up-enter-from,
.slide-up-leave-to {
    transform: translateY(100%);
    opacity: 0;
}

.item-enter-active,
.item-leave-active {
    transition: all 0.3s cubic-bezier(0.4, 0, 0.2, 1);
}

.item-enter-from,
.item-leave-to {
    opacity: 0;
    transform: scale(0.8) translateY(20px);
}

.item-move {
    transition: transform 0.3s cubic-bezier(0.4, 0, 0.2, 1);
}

/* Responsive Design */
@media (max-width: 768px) {
    .compare-bar {
        padding: 12px 16px;
    }

    .compare-container {
        gap: 12px;
    }

    .compare-item {
        min-width: 160px;
        padding: 10px 12px;
    }

    .item-image img {
        width: 40px;
        height: 40px;
    }

    .item-name {
        font-size: 13px;
    }

    .compare-btn {
        padding: 10px 16px;
        font-size: 14px;
    }

    .item-count {
        font-size: 13px;
    }
}

@media (max-width: 480px) {
    .compare-actions {
        flex-direction: column;
        gap: 8px;
        align-items: stretch;
    }

    .compare-btn {
        width: 100%;
        justify-content: center;
    }
}
</style>