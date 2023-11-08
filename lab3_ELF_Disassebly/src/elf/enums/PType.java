package elf.enums;

import elf.parser.IncorrectFileSignature;

/**
 * Code automatically generated using Extended Enum Preprocessor
 */
public interface PType {
    int value();

    enum List implements PType {
        NULL(0),
        LOAD(1),
        DYNAMIC(2),
        INTERP(3),
        NOTE(4),
        SHLIB(5),
        PHDR(6),
        TLS(7),
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

    record OSSpecific(int value) implements PType {
        @Override
        public String toString() {
            return "OSSpecific(" + value + ")";
        }
    }

    record ProcSpecific(int value) implements PType {
        @Override
        public String toString() {
            return "ProcSpecific(" + value + ")";
        }
    }

    static PType of(int value) {
        if (1610612736 <= value && value <= 1879048191) {
            return new OSSpecific(value);
        } else if (1879048192 <= value && value <= 2147483647) {
            return new ProcSpecific(value);
        } else {
            for (PType it : PType.List.values()) {
                if (it.value() == value) {
                    return it;
                }
            }
            throw new IncorrectFileSignature("Incorrect value " + value + " for PType");
        }
    }
}