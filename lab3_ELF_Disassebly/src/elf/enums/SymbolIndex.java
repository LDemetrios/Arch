package elf.enums;

public interface SymbolIndex {
    int value();

    boolean isSpecific();

    enum List implements SymbolIndex {
        UNDEF(0),
        BEFORE(65280),
        AFTER(65281),
        ABS(65521),
        COMMON(65522),
        ;
        final int value;

        List(int value) {
            this.value = value;
        }

        @Override
        public int value() {
            return value;
        }

        @Override
        public boolean isSpecific() {
            return true;
        }
    }

    record ProcSpecific(int value) implements SymbolIndex {
        @Override
        public String toString() {
            return "ProcSpecific(" + value + ")";
        }

        @Override
        public boolean isSpecific() {
            return true;
        }
    }

    record Reserved1(int value) implements SymbolIndex {
        @Override
        public String toString() {
            return "Reserved1(" + value + ")";
        }

        @Override
        public boolean isSpecific() {
            return true;
        }
    }

    record Reserved2(int value) implements SymbolIndex {
        @Override
        public String toString() {
            return "Reserved2(" + value + ")";
        }

        @Override
        public boolean isSpecific() {
            return true;
        }
    }

    record Usual(int value) implements SymbolIndex {
        @Override
        public String toString() {
            return String.valueOf(value);
        }

        @Override
        public boolean isSpecific() {
            return false;
        }
    }

    static SymbolIndex of(int value) {
        if (65282 <= value && value <= 65311) {
            return new ProcSpecific(value);
        } else if (65280 <= value && value <= 65520) {
            return new Reserved1(value);
        } else if (65523 <= value && value <= 65535) {
            return new Reserved2(value);
        } else {
            for (SymbolIndex it : SymbolIndex.List.values()) {
                if (it.value() == value) {
                    return it;
                }
            }
            return new Usual(value);
        }
    }
}