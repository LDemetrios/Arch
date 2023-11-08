package elf.parser;

import elf.enums.PType;


public record ProgramHeader(
		PType pType,
		int pFlags,
		long pOffset,
		long pVaddr,
		long pPaddr,
		long pFilesz,
		long pMemsz,
		long pAlign
) {

}