package disassembly;

final class JCommand extends Command {
    public final int imm;
    public final int rd;
    public final int opcode;

    public JCommand(int code, int address) {
        super(code, address);
        int imm20 = slice(code, 31, 31);
        int imm10_1 = slice(code, 30, 21);
        int imm11 = slice(code, 20, 20);
        int imm19_12 = slice(code, 19, 12);
        rd = slice(code, 11, 7);
        opcode = slice(code, 6, 0);
        imm = Command.extendHighest(imm20 << 20 | imm19_12 << 12 | imm11 << 11 | imm10_1 << 1, 20);
    }

    public String name() {
        return switch (opcode) {
            case 0b1101111 -> "jal";
            default -> UNKNOWN_INSTRUCTION;
        };
    }

    @Override
    public String args() {
        return (links() ? "%s, %x" : "%s, %d").formatted(REGISTER_MNEMONIC[rd], links() ? link() : imm);
    }

    @Override
    public boolean links() {
        return opcode == 0b1101111;
    }

    @Override
    public int link() {
        return address + imm;
    }
}
