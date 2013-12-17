package pl.mjedynak.akka.message;

public final class Calculate {

    private final String value;

    public Calculate(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
