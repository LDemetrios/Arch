package elf.objects;

import bytearrparser.BytearrParser;
import elf.enums.SHType;
import elf.enums.SectionFlags;
import util.Utilities;

public class Section {
    public final String name;
    public final SHType type;
    public final long virtualAddress;
    public final int link;
    public final int info;
    public final SectionFlags flags;
    public final long addressAlignment;
    public final long entrySize;
    protected final BytearrParser parser;
    public final long offset;
    public final long size;

    public Section(String name, SectionHeader header, BytearrParser parser) {
        this.name = name;
        this.type = header.shType();
        this.virtualAddress = header.shAddr();
        this.link = header.shLink();
        this.info = header.shInfo();
        this.flags = header.shFlags();
        this.addressAlignment = header.shAddrAlign();
        this.entrySize = header.shEntSize();
        this.parser = parser;
        this.offset = header.shOffset();
        this.size = header.shSize();
    }

    public byte[] rawContent() {
        return parser.slice(Utilities.assertIsInt(offset), Utilities.assertIsInt(size));
    }
}