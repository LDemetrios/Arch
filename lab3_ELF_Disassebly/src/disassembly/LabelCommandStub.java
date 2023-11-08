package disassembly;

public final class LabelCommandStub extends Command {
    public final String label;

    public LabelCommandStub(int code, int address, String label) {
        super(code, address);
        this.label = label;
    }

    @Override
    public String name() {
        return "";
    }

    @Override
    public String toString(String withLabel) {
        return "%08x   <%s>:\n".formatted(address, label);
    }
}
