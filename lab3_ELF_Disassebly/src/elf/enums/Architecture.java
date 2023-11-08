package elf.enums;

import elf.parser.IncorrectFileSignature;

public enum Architecture {
    x32(1),
    x64(2),
    ;
    final int value;

    Architecture(int value) {
        this.value = value;
    }

    public int value() {
        return value;
    }

    public static Architecture of(int value) {
        for (Architecture it : Architecture.values()) {
            if (it.value() == value) {
                return it;
            }
        }
        throw new IncorrectFileSignature("Incorrect value " + value + " for Architecture");
    }
}


