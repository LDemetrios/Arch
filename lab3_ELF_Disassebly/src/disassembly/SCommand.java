package disassembly;

final class SCommand extends Command {
    public final int imm;
    public final int rs2;
    public final int rs1;
    public final int funct3;
    public final int opcode;

    public SCommand(int code, int address) {
        super(code, address);
        int imm11_5 = slice(code, 31, 25);
        rs2 = slice(code, 24, 20);
        rs1 = slice(code, 19, 15);
        funct3 = slice(code, 14, 12);
        int imm4_0 = slice(code, 11, 7);
        opcode = slice(code, 6, 0);
        imm = extendHighest(imm11_5 << 5 | imm4_0, 11);
    }

    public String name() {
        return switch (funct3 << 7 | opcode) {
            case 0b0000100011 -> "sb";
            case 0b0010100011 -> "sh";
            case 0b0100100011 -> "sw";
            default -> UNKNOWN_INSTRUCTION;
        };
    }

    @Override
    public String args() {
        return "%s, %d(%s)".formatted(REGISTER_MNEMONIC[rs2], imm, REGISTER_MNEMONIC[rs1]);
    }
}
