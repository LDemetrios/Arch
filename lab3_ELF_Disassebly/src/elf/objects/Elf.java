package elf.objects;

import elf.parser.MetaInf;

public class Elf {
    public final MetaInf metaInf;
    public final StringTable strtab;
    public final StringTable names;
    public final SymbolTable symtab;
    public final Section text;

    public Elf(MetaInf metaInf, StringTable strtab, StringTable names, SymbolTable symtab, Section text) {
        this.metaInf = metaInf;
        this.strtab = strtab;
        this.names = names;
        this.symtab = symtab;
        this.text = text;
    }
}
