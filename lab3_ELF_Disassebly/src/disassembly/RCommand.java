package disassembly;

final class RCommand extends Command {
    public final int funct7;
    public final int rs2;
    public final int rs1;
    public final int funct3;
    public final int rd;
    public final int opcode;

    public RCommand(int code, int address) {
        super(code, address);
        funct7 = slice(code, 31, 25);
        rs2 = slice(code, 24, 20);
        rs1 = slice(code, 19, 15);
        funct3 = slice(code, 14, 12);
        rd = slice(code, 11, 7);
        opcode = slice(code, 6, 0);
    }

    public String name() {
        return switch (funct7 << 10 | funct3 << 7 | opcode) {
            case 0b00000000000110011 -> "add";
            case 0b01000000000110011 -> "sub";
            case 0b00000000010110011 -> "sll";
            case 0b00000000100110011 -> "slt";
            case 0b00000000110110011 -> "sltu";
            case 0b00000001000110011 -> "xor";
            case 0b00000001010110011 -> "srl";
            case 0b01000001010110011 -> "sra";
            case 0b00000001100110011 -> "or";
            case 0b00000001110110011 -> "and";
            case 0b00000010000110011 -> "mul";
            case 0b00000010010110011 -> "mulh";
            case 0b00000010100110011 -> "mulhsu";
            case 0b00000010110110011 -> "mulhu";
            case 0b00000011000110011 -> "div";
            case 0b00000011010110011 -> "divu";
            case 0b00000011100110011 -> "rem";
            case 0b00000011110110011 -> "remu";
            default -> UNKNOWN_INSTRUCTION;
        };
    }

    @Override
    public String args() {
        return "%s, %s, %s".formatted(REGISTER_MNEMONIC[rd], REGISTER_MNEMONIC[rs1], REGISTER_MNEMONIC[rs2]);
    }


}
