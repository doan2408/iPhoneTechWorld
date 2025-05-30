package org.example.websitetechworld.Enum.HoaDon;

public enum LoaiHoaDon {
    ONLINE("Trực tuyến"),
    POS("Tại quầy");

    private final String displayName;

    LoaiHoaDon(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
