package ru.krizhanovsky.aston.model;

public enum TransactionType {
    TRANSFER("Перевод"),
    DEPOSIT("Внесение"),
    WITHDRAWAL("Снятие"),
    PAYMENT("Платёж");

    private final String description;

    TransactionType(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
