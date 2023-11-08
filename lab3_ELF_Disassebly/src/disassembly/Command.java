package disassembly;

@SuppressWarnings("unused")
public abstract sealed class Command permits BCommand, Command.Named, ICommand, JCommand, RCommand, SCommand, UCommand, LabelCommandStub {
    protected static final String UNKNOWN_INSTRUCTION = "unknown_instruction";
    public final int code;
    public final int address;

    public Command(int code, int address) {
        this.code = code;
        this.address = address;
    }

    public final String hexCode() {
        String res = Integer.toUnsignedString(code, 16);
        return "0".repeat(8 - res.length()) + res;
    }

    protected static int slice(int i, int from, int to) {
        return (i >>> to) & (1 << from - to + 1) - 1;
    }

    protected static final String[] REGISTER_MNEMONIC = {"zero", "ra", "sp", "gp", "tp", "t0", "t1", "t2", "s0", "s1", "a0", "a1", "a2", "a3", "a4", "a5", "a6", "a7", "s2", "s3", "s4", "s5", "s6", "s7", "s8", "s9", "s10", "s11", "t3", "t4", "t5", "t6"};

    protected static String toHex(int i) {
        return (i < 0 ? "-" : "") + "0x" + Integer.toUnsignedString(Math.abs(i), 16);
    }


    protected static int extendHighest(int i, int highest) {
        return (i >> highest & 1) == 0 ? i : -1 >> highest << highest | i;
    }

    public abstract String name();

    public String args() {
        return "";
    }

    public boolean links() {
        return false;
    }

    public int link() {
        return 0;
    }

    private static String pad(String s, int toLen) {
        return s + (s.length() < toLen ? " ".repeat(toLen - s.length()) : "");
    }

    public String toString(String withLabel) {
        return (withLabel == null ? "   %05x:\t%08x\t%s\t%s\n" : "   %05x\t%08x\t%s\t%s <%s>\n")
                .formatted(address, code, pad(name(), 7), args(), withLabel);
    }

    static final class Named extends Command {
        private final String name;

        public Named(int code, int address, String name) {
            super(code, address);
            this.name = name;
        }

        @Override
        public String name() {
            return name;
        }
    }

    @Override
    public String toString() {
        return toString(null);
    }

    public static Command of(int code, int address) {
        return switch (slice(code, 6, 2)) {
            case 0b01101 -> new UCommand(code, address); //LUI
            case 0b00101 -> new UCommand(code, address); //AUIPC
            case 0b11011 -> new JCommand(code, address); //JAL
            case 0b11001 -> new ICommand(code, address); //JALR
            case 0b11000 -> new BCommand(code, address);
            case 0b00000 -> new ICommand(code, address);
            case 0b01000 -> new SCommand(code, address);
            case 0b00100 -> new ICommand(code, address);
            case 0b01100 -> new RCommand(code, address);
            case 0b11100 -> switch (code) {
                case 0x00000073 -> new Named(code, address, "ecall");
                case 0x00100073 -> new Named(code, address, "ebreak");
                default -> new Named(code, address, UNKNOWN_INSTRUCTION);
            };
            default -> new Named(code, address, UNKNOWN_INSTRUCTION);
        };
    }
}
