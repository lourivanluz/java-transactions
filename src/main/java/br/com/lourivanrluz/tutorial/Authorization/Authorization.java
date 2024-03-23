package br.com.lourivanrluz.tutorial.Authorization;

public record Authorization(
        String message) {

    public Boolean isAuthorized() {
        return message.equals("Autorizado");
    }
}
