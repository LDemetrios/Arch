package elf.objects;

import bytearrparser.BytearrParser;
import elf.enums.*;

public record Symbol(String name,
                     long value,
                     long size,
                     SymbolBinding bind,
                     SymbolType type,
                     int other,
                     SymbolVisibility visibility,
                     SymbolIndex shndx) {
    public static Symbol read(BytearrParser parser, Architecture arch, StringTable names) {
        return switch (arch) {
            case x64 -> {
                int name = parser.nextInt();
                int info = parser.nextByte();
                int other = parser.nextByte();
                int shndx = parser.nextShort();
                long value = parser.nextLong();
                long size = parser.nextLong();
                yield new Symbol(
                        names.readString(name),
                        value, size,
                        SymbolBinding.of(info >> 4),
                        SymbolType.of(info & 0xf),
                        other,
                        SymbolVisibility.of(other & 0x3),
                       SymbolIndex.of(shndx)
                );
            }
            case x32 -> {
                int name = parser.nextInt();
                int value = parser.nextInt();
                int size = parser.nextInt();
                int info = parser.nextByte();
                int other = parser.nextByte();
                int shndx = parser.nextShort();
                yield new Symbol(
                        names.readString(name),
                        value, size,
                        SymbolBinding.of(info >> 4),
                        SymbolType.of(info & 0xf),
                        other,
                        SymbolVisibility.of(other & 0x3),
                        SymbolIndex.of(shndx)
                );
            }
            default -> throw new AssertionError();
        };
    }

    @Override
    public String toString() {
        return "0x%-15X %5d %-8s %-8s %-8s %6s %s".formatted(value, size, type, bind, visibility, shndx, name);
        }
}