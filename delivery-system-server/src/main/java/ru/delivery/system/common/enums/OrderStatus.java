package ru.delivery.system.common.enums;

public enum OrderStatus {
    NEW,
    IN_PROGRESS,
    DONE,
    CANCELED;

    public boolean isEqual(String status) {
        return this.name().equalsIgnoreCase(status);
    }
}
