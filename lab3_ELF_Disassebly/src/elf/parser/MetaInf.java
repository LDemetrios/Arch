package elf.parser;

import elf.enums.*;

public record MetaInf(
        Architecture arch,
        boolean littleEndian,
        OS_ABI osabi,
        int abiVersion,
        EType eType,
        EMachine eMachine,
        int eVersion,
        long eEntry,
        long ePhOff,
        long eShOff,
        int eFlags,
        int eEhSize,
        int ePhEntSize,
        int ePhNum,
        int eShEntSize,
        int eShNum,
        int eShStrNdx
) {

}

