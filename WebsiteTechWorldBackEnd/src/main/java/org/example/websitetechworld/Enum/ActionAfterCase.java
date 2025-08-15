package org.example.websitetechworld.Enum;

import org.example.websitetechworld.Enum.CaseReason.CaseType;

public enum ActionAfterCase {
    RETRY("Giao lại"),
    CANCEL("Hủy bỏ"),
    HOLD("Giữ"),
    RETURN_TO_STOCK("Trả lại kho"),
    REFUND("Hoàn tiền"),
    EXCHANGE("Trao đổi");

    private final String displayName;

    ActionAfterCase(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }

    public static ActionAfterCase fromDisplayName(String displayName) {
        for (ActionAfterCase caseAfter : values()) {
            if (caseAfter.getDisplayName().equalsIgnoreCase(displayName)) {
                return caseAfter;
            }
        }
        throw new IllegalArgumentException("Loại trường hợp không hợp lệ: " + displayName);
    }
}
