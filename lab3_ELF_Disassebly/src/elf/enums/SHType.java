package elf.enums;

import elf.parser.IncorrectFileSignature;

/**
 * Code automatically generated using Extended Enum Preprocessor
 */
public interface SHType {
    int value();

    enum List implements SHType {
        NULL(0),
        PROGBITS(1),
        SYMTAB(2),
        STRTAB(3),
        RELA(4),
        HASH(5),
        DYNAMIC(6),
        NOTE(7),
        NOBITS(8),
        REL(9),
        SHLIB(10),
        DYNSYM(11),
        INIT_ARRAY(14),
        FINI_ARRAY(15),
        PREINIT_ARRAY(16),
        GROUP(17),
        SYMTAB_SHNDX(18),
        NUM(19),
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

    record OSSpecific(int value) implements SHType {
        @Override
        public String toString() {
            return "OSSpecific(" + value + ")";
        }
    }

    static SHType of(int value) {
        if (1610612736 <= value && value <= 2147483647) {
            return new OSSpecific(value);
        } else {
            for (SHType it : SHType.List.values()) {
                if (it.value() == value) {
                    return it;
                }
            }
            throw new IncorrectFileSignature("Incorrect value " + value + " for SHType");
        }
    }
}