package elf.enums;

import elf.parser.IncorrectFileSignature;

/**
 * Code automatically generated using Extended Enum Preprocessor
 */
public interface SymbolVisibility {
    int value();

    enum List implements SymbolVisibility {
        DEFAULT(0),
        INTERNAL(1),
        HIDDEN(2),
        PROTECTED(3),
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


    static SymbolVisibility of(int value) {
        
            for (SymbolVisibility it : SymbolVisibility.List.values()) {
                if (it.value() == value) {
                    return it;
                }
            }
            throw new IncorrectFileSignature("Incorrect value " + value + " for SymbolVisibility");
        
    }
}