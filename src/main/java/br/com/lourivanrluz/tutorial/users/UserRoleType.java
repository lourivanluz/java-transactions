package br.com.lourivanrluz.tutorial.users;

public enum UserRoleType {
    ADM("admim"), COMMUM("commum");

    private String value;

    UserRoleType(String value) {
        this.value = value;
    }

    public String getRole() {
        return value;
    }
}
