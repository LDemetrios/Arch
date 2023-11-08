package disassembly;

final class UCommand extends Command {
    public final int imm;
    public final int rd;
    public final int opcode;

    public UCommand(int code, int address) {
        super(code, address);
        int imm31_12 = slice(code, 31, 12);
        rd = slice(code, 11, 7);
        opcode = slice(code, 6, 0);
        imm = imm31_12 << 12;
    }

    public String name() {
        return switch (opcode) {
            case 0b0110111 -> "lui";
            case 0b0010111 -> "auipc";
            default -> UNKNOWN_INSTRUCTION;
        };
    }

    @Override
    public String args() {
        return switch (opcode) {
            case 0b0110111 -> "%s, 0x%x".formatted(REGISTER_MNEMONIC[rd], imm);
            case 0b0010111 -> "%s, %d".formatted(REGISTER_MNEMONIC[rd], imm);
            default -> UNKNOWN_INSTRUCTION;
        };
    }
}
