package org.example.websitetechworld.Enum;

import org.example.websitetechworld.Enum.CaseReason.CaseType;

public enum ActionAfterCase {
    FAILED_DELIVERY("Giao hàng không thành công"),
    RETURN("Đã xác nhận");

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
