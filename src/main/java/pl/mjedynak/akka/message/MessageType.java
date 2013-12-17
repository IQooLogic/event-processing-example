package pl.mjedynak.akka.message;

public enum MessageType {
    WRITING_FINISHED,
    READING_FINISHED,
    PROCESSING_FINISHED,
    NO_RESULT,
    PROCESSING_EXCEPTION;
}
