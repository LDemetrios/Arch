package elf.enums;

import elf.parser.IncorrectFileSignature;

/**
 * Code automatically generated using Extended Enum Preprocessor
 */
public interface SymbolBinding {
    int value();

    enum List implements SymbolBinding {
        LOCAL(0),
        GLOBAL(1),
        WEAK(2),
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

    record ProcSpecific(int value) implements SymbolBinding {
        @Override
        public String toString() {
            return "ProcSpecific(" + value + ")";
        }
    }

    static SymbolBinding of(int value) {
        if (13 <= value && value <= 15) {
            return new ProcSpecific(value);
        } else {
            for (SymbolBinding it : SymbolBinding.List.values()) {
                if (it.value() == value) {
                    return it;
                }
            }
            throw new IncorrectFileSignature("Incorrect value " + value + " for SymbolBinding");
        }
    }
}