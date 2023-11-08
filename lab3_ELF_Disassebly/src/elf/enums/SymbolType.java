package elf.enums;

import elf.parser.IncorrectFileSignature;

/**
 * Code automatically generated using Extended Enum Preprocessor
 */
public interface SymbolType {
    int value();

    enum List implements SymbolType {
        NOTYPE(0),
        OBJECT(1),
        FUNC(2),
        SECTION(3),
        FILE(4),
        ;
        final int value;

        List(int value) {
            this.value = value;
        }

        @Override
        public int value() {
            return value;
        }
    }

    record ProcSpecific(int value) implements SymbolType {
        @Override
        public String toString() {
            return "ProcSpecific(" + value + ")";
        }
    }

    static SymbolType of(int value) {
        if (13 <= value && value <= 15) {
            return new ProcSpecific(value);
        } else {
            for (SymbolType it : SymbolType.List.values()) {
                if (it.value() == value) {
                    return it;
                }
            }
            throw new IncorrectFileSignature("Incorrect value " + value + " for SymbolType");
        }
    }
}