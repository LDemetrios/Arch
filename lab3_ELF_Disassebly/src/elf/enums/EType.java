package elf.enums;

import elf.parser.IncorrectFileSignature;

/**
 * Code automatically generated using Extended Enum Preprocessor
 */
public interface EType {
    int value();

    enum List implements EType {
        NONE(0),
        REL(1),
        EXEC(2),
        DYN(3),
        CORE(4),
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

    record ProcSpecific(int value) implements EType {
        @Override
        public String toString() {
            return "ProcSpecific(" + value + ")";
        }
    }

    record OSSpecific(int value) implements EType {
        @Override
        public String toString() {
            return "OSSpecific(" + value + ")";
        }
    }

    static EType of(int value) {
        if (65280 <= value && value <= 65535) {
            return new ProcSpecific(value);
        } else if (65024 <= value && value <= 65279) {
            return new OSSpecific(value);
        } else {
            for (EType it : EType.List.values()) {
                if (it.value() == value) {
                    return it;
                }
            }
            throw new IncorrectFileSignature("Incorrect value " + value + " for EType");
        }
    }
}