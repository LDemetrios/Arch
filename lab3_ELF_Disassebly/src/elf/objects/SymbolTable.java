package elf.objects;

import bytearrparser.BytearrParser;
import elf.enums.Architecture;
import elf.enums.SHType;
import util.Utilities;

import java.util.Arrays;

public class SymbolTable extends Section {
    private final Symbol[] symbols;

    public SymbolTable(String name, SectionHeader header, BytearrParser parser, Architecture arch, StringTable symNames) {
        super(name, header, parser);
        if (type != SHType.List.SYMTAB)
            throw new IllegalArgumentException(header + " doesn't represent SymbolTable");
        parser.moveTo(Utilities.assertIsInt(offset));
        symbols = new Symbol[Utilities.assertIsInt(size / entrySize)];
        for (int i = 0; i < symbols.length; i++) {
            symbols[i] = Symbol.read(parser, arch, symNames);
        }
    }

    public Symbol[] getSymbols() {
        return Arrays.copyOf(symbols, symbols.length);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("Symbol Value              Size Type     Bind     Vis       Index Name\n");
        for (int i = 0; i < symbols.length; i++) {
            Symbol symbol = symbols[i];
            sb.append("[%4d] %s\n".formatted(i, symbol));
        }
        return sb.toString();
    }
}

