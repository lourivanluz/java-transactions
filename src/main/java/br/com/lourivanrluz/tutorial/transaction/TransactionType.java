package br.com.lourivanrluz.tutorial.transaction;

public enum TransactionType {
    PaymentinFull(1), PaymentinInstallments(2);

    private int value;

    private TransactionType(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
