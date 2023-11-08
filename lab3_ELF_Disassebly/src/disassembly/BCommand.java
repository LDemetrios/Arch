package disassembly;

final class BCommand extends Command {
    public final int imm;
    public final int rs2;
    public final int rs1;
    public final int funct3;
    public final int opcode;

    public BCommand(int code, int address) {
        super(code, address);
        int imm12 = slice(code, 31, 31);
        int imm10_5 = slice(code, 30, 25);
        rs2 = slice(code, 24, 20);
        rs1 = slice(code, 19, 15);
        funct3 = slice(code, 14, 12);
        int imm4_1 = slice(code, 11, 8);
        int imm11 = slice(code, 7, 7);
        opcode = slice(code, 6, 0);
        imm = Command.extendHighest(imm12 << 12 | imm11 << 11 | imm10_5 << 5 | imm4_1 << 1, 12);
    }

    public String name() {
        return switch (funct3 << 7 | opcode) {
            case 0b0001100011 -> "beq";
            case 0b0011100011 -> "bne";
            case 0b1001100011 -> "blt";
            case 0b1011100011 -> "bge";
            case 0b1101100011 -> "bltu";
            case 0b1111100011 -> "bgeu";
            default -> UNKNOWN_INSTRUCTION;
        };
    }

    @Override
    public boolean links() {
        return !name().equals(UNKNOWN_INSTRUCTION);
    }

    @Override
    public int link() {
        return address + imm;
    }

    @Override
    public String args() {
        return (links() ? "%s, %s, %x" : "%s, %s, %d").formatted(REGISTER_MNEMONIC[rs1], REGISTER_MNEMONIC[rs2], links() ? link() : imm);
    }
}
