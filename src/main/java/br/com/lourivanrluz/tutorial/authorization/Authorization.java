package br.com.lourivanrluz.tutorial.authorization;

public record Authorization(
        String message) {

    public Boolean isAuthorized() {
        return message.equals("Autorizado");
    }
}
