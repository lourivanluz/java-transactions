package br.com.lourivanrluz.tutorial.transaction;

public enum TransactionType {
    PaymentinFull(0), PaymentinInstallments(1);

    private final Integer value;

    TransactionType(Integer value) {
        this.value = value;
    }

    public Integer getValue() {
        return this.value;
    }

    public String getType() {
        return value == 0 ? "PaymentinFull" : "PaymentinInstallments";
    }
}