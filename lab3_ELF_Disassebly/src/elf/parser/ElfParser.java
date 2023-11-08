package elf.parser;

import bytearrparser.BytearrParser;
import elf.enums.Architecture;
import elf.enums.EMachine;
import elf.enums.EType;
import elf.enums.OS_ABI;
import elf.objects.*;
import util.Utilities;

import java.util.HashMap;
import java.util.Map;

public class ElfParser {
    private static void checkMagic(byte[] data) {
        if (data[0] != 0x7f) throw new IncorrectFileSignature("Illegal magic for Elf file (char 0)");
        if (data[1] != 0x45) throw new IncorrectFileSignature("Illegal magic for Elf file (char 1)");
        if (data[2] != 0x4c) throw new IncorrectFileSignature("Illegal magic for Elf file (char 2)");
        if (data[3] != 0x46) throw new IncorrectFileSignature("Illegal magic for Elf file (char 3)");
    }

    public static Elf parse(byte[] data) {
        checkMagic(data);
        Architecture arch = Architecture.of(data[4]);
        boolean littleEndian = (data[5] == 1);
        BytearrParser parser = new BytearrParser(data, littleEndian, arch);
        parser.moveTo(6);
        if (parser.nextByte() != 0x01) throw new IncorrectFileSignature();
        OS_ABI osabi = OS_ABI.of(parser.nextByte());
        int abiVersion = parser.nextByte();
        parser.moveTo(16);
        EType eType = EType.of(parser.nextShort());
        EMachine eMachine = EMachine.of(parser.nextShort());
        int eVersion = parser.nextInt();
        if (eVersion != 0x01) throw new IncorrectFileSignature();
        long eEntry = parser.nextWord();
        long ePhOff = parser.nextWord();
        long eShOff = parser.nextWord();
        int eFlags = parser.nextInt();
        int eEhSize = parser.nextShort();
        int ePhEntSize = parser.nextShort();
        int ePhNum = parser.nextShort();
        int eShEntSize = parser.nextShort();
        int eShNum = parser.nextShort();
        int eShStrNdx = parser.nextShort();
        MetaInf metaInf = new MetaInf(
                arch, littleEndian, osabi, abiVersion, eType, eMachine, eVersion, eEntry,
                ePhOff, eShOff, eFlags, eEhSize, ePhEntSize, ePhNum, eShEntSize, eShNum, eShStrNdx
        );
        SectionHeader[] headers = new SectionHeader[eShNum];
        for (int i = 0; i < eShNum; i++) {
            parser.moveTo(Utilities.assertIsInt(eShOff + (long) i * eShEntSize));
            headers[i] = SectionHeader.read(parser);
        }
        StringTable names = new StringTable(".shstrtab", headers[eShStrNdx], parser);
        Map<String, SectionHeader> named = new HashMap<>();
        for (SectionHeader header : headers) {
            named.put(names.readString(header.shName()), header);
        }
        // I won't implement all possible kinds of sections

        StringTable strtab = new StringTable(".strtab", named.get(".strtab"), parser);
        SymbolTable symtab = new SymbolTable(".symtab", named.get(".symtab"), parser, arch, strtab);
        Section text = new Section(".text", named.get(".text"), parser);
        return new Elf(metaInf, strtab, names, symtab, text);
    }
}
