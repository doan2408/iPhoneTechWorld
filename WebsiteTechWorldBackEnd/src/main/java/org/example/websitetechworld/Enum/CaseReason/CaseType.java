package org.example.websitetechworld.Enum.CaseReason;


public enum CaseType {
    FAILED_DELIVERY("Giao hàng không thành công"),
    RETURN("Đã xác nhận");

    private final String displayName;

    CaseType(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }

    public static CaseType fromDisplayName(String displayName) {
        for (CaseType type : values()) {
            if (type.getDisplayName().equalsIgnoreCase(displayName)) {
                return type;
            }
        }
        throw new IllegalArgumentException("Loại trường hợp không hợp lệ: " + displayName);
    }

}
