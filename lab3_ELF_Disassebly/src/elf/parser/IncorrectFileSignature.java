package elf.parser;

public final class IncorrectFileSignature extends IllegalArgumentException {
    public IncorrectFileSignature() {
    }

    public IncorrectFileSignature(String s) {
        super(s);
    }

    public IncorrectFileSignature(String message, Throwable cause) {
        super(message, cause);
    }

    public IncorrectFileSignature(Throwable cause) {
        super(cause);
    }
}
