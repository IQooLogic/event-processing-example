package pl.mjedynak.akka.message;

public final class Result {

    private final String value;

    public Result(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
