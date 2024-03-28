package br.com.lourivanrluz.tutorial.authorization;

public record Authorization(
        String message, Boolean authorized) {

    public Boolean isAuthorized() {
        return message.equals("Autorizado");
    }
}
