package disassembly;

final class ICommand extends Command {
    public final int imm;
    public final int rs1;
    public final int funct3;
    public final int rd;
    public final int opcode;

    public ICommand(int code, int address) {
        super(code, address);
        imm = extendHighest(slice(code, 31, 20), 11); //imm11_0
        rs1 = slice(code, 19, 15);
        funct3 = slice(code, 14, 12);
        rd = slice(code, 11, 7);
        opcode = slice(code, 6, 0);
    }

    public String name() {
        return switch (funct3 << 7 | opcode) {
            case 0b0001100111 -> "jalr";
            case 0b0000000011 -> "lb";
            case 0b0010000011 -> "lh";
            case 0b0100000011 -> "lw";
            case 0b1000000011 -> "lbu";
            case 0b1010000011 -> "lhu";
            case 0b0000010011 -> "addi";
            case 0b0100010011 -> "slti";
            case 0b0110010011 -> "sltiu";
            case 0b1000010011 -> "xori";
            case 0b1100010011 -> "ori";
            case 0b1110010011 -> "andi";
            case 0b0010010011 -> "slli";
            case 0b1010010011 -> slice(imm, 10, 10) == 1 ? "srai" : "srli";
            default -> UNKNOWN_INSTRUCTION;
        };
    }

    @Override
    public String args() {
        String name = name();
        return switch (name) {
            case "jalr", "lb", "lh", "lw", "lbu", "lhu" -> "%s, %d(%s)".formatted(REGISTER_MNEMONIC[rd], imm, REGISTER_MNEMONIC[rs1]);
            case "slli", "srai", "srli" -> "%s, %s, 0x%x".formatted(REGISTER_MNEMONIC[rd], REGISTER_MNEMONIC[rs1], slice(imm, 4, 0));
            default -> "%s, %s, %d".formatted(REGISTER_MNEMONIC[rd], REGISTER_MNEMONIC[rs1], imm);
        };
    }
}
