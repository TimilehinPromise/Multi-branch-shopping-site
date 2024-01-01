package com.valuemart.shop.persistence.entity;

public enum PaymentStatus {

    CREATED, ONGOING, ABANDONED, FAILED(true), SUCCESS(true);

    private boolean terminal;

    private PaymentStatus(boolean terminal) {
        this.terminal = terminal;
    }

    private PaymentStatus() {
        this(false);
    }

    public boolean isTerminal() {
        return terminal;
    }

    public boolean isOngoing() {
        return (this.equals(ONGOING) || this.equals(ABANDONED));
    }
}
