package elf.objects;


import bytearrparser.BytearrParser;
import elf.enums.SHType;
import elf.enums.SectionFlags;

public record SectionHeader(
        int shName,
        SHType shType,
        SectionFlags shFlags,
        long shAddr,
        long shOffset,
        long shSize,
        int shLink,
        int shInfo,
        long shAddrAlign,
        long shEntSize
) {
    public static SectionHeader read(BytearrParser parser){
        int shName = parser.nextInt();
        SHType shType = SHType.of(parser.nextInt());
        SectionFlags shFlags = new SectionFlags(parser.nextWord());
        long shAddr = parser.nextWord();
        long shOffset = parser.nextWord();
        long shSize = parser.nextWord();
        int shLink = parser.nextInt();
        int shInfo = parser.nextInt();
        long shAddrAlign = parser.nextWord();
        long shEntSize = parser.nextWord();
        return new SectionHeader(
                shName, shType, shFlags, shAddr, shOffset,
                shSize, shLink, shInfo, shAddrAlign, shEntSize
        );
    }

    @Override
    public String toString() {
        return "SectionHeader (" +
                "name = " + shName +
                ", type = " + shType +
                ", addr = " + shAddr +
                ", offset = " + shOffset +
                ", size = " + shSize +
                ", link = " + shLink +
                ", info = " + shInfo +
                ", addrAlign = " + shAddrAlign +
                ", entSize = " + shEntSize +
                ')';
    }
}
