package elf.enums;

import elf.parser.IncorrectFileSignature;

/**
 * Code automatically generated using Extended Enum Preprocessor
 */
public interface OS_ABI {
    int value();

    enum List implements OS_ABI {
        NONE(0),
        HPUX(1),
        NETBSD(2),
        GNU(3),
        SOLARIS(6),
        AIX(7),
        IRIX(8),
        FREEBSD(9),
        TRU64(10),
        MODESTO(11),
        OPENBSD(12),
        OPENVMS(13),
        NSK(14),
        AROS(15),
        FENIXOS(16),
        CLOUDABI(17),
        OPENVOS(18),
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

    record ProcSpecific(int value) implements OS_ABI {
        @Override
        public String toString() {
            return "ProcSpecific(" + value + ")";
        }
    }

    static OS_ABI of(int value) {
        if (64 <= value && value <= 255) {
            return new ProcSpecific(value);
        } else {
            for (OS_ABI it : OS_ABI.List.values()) {
                if (it.value() == value) {
                    return it;
                }
            }
            throw new IncorrectFileSignature("Incorrect value " + value + " for OS_ABI");
        }
    }
}