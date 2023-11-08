package elf.objects;

import bytearrparser.BytearrParser;
import elf.enums.SHType;
import util.Utilities;

public class StringTable extends Section {
    public StringTable(String name, SectionHeader header, BytearrParser parser) {
        super(name, header, parser);
        if (type != SHType.List.STRTAB) throw new IllegalArgumentException(header + " doesn't specify StringTable");
    }

    public String readString(int offset) {
        return parser.nullTerminatedStringFrom(Utilities.assertIsInt(this.offset + offset));
    }

    @Override
    public String toString() {
        return "index " + offset + ": " + new String(rawContent());
    }
}
