package bytearrparser;

import elf.enums.Architecture;

public class BytearrParser {
    private final byte[] data;
    private final boolean littleEndian;
    private final Architecture arch;
    private int currentPos = 0;

    public BytearrParser(byte[] data, boolean littleEndian, Architecture arch) {
        this.data = data;
        this.littleEndian = littleEndian;
        this.arch = arch;
    }

    public void moveTo(int newPos) {
        if (newPos < 0) throw new IllegalArgumentException();
        checkOpen();
        currentPos = newPos;
    }

    private int byteFrom(int pos) {
        return ((int) data[pos]) & 0xff;
    }

    public int shortFrom(int pos) {
        return (byteFrom(pos) << (littleEndian ? 0 : 8)) | (byteFrom(pos + 1) << (littleEndian ? 8 : 0));
    }

    public int intFrom(int pos) {
        return (shortFrom(pos) << (littleEndian ? 0 : 16)) | (shortFrom(pos + 2) << (littleEndian ? 16 : 0));
    }

    public long longFrom(int pos) {
        return ((long) intFrom(pos) & 0xffff_ffffL) << (littleEndian ? 0 : 32) |
                ((long) intFrom(pos + 4) & 0xffff_ffffL) << (littleEndian ? 32 : 0);
    }

    public int nextByte() {
        checkOpen();
        return byteFrom(currentPos++);
    }

    public int nextShort() {
        checkOpen();
        return shortFrom((currentPos += 2) - 2);
    }

    public int nextInt() {
        checkOpen();
        return intFrom((currentPos += 4) - 4);
    }

    public long nextLong() {
        checkOpen();
        return longFrom((currentPos += 8) - 8);
    }

    public long nextWord() {
        checkOpen();
        return switch (arch) {
            case x32 -> nextInt();
            case x64 -> nextLong();
            default -> throw new AssertionError();
        };
    }

    public long wordFrom(int pos) {
        return switch (arch) {
            case x32 -> intFrom(pos);
            case x64 -> longFrom(pos);
            default -> throw new AssertionError();
        };
    }

    public String nullTerminatedStringFrom(int pos) {
        StringBuilder sb = new StringBuilder();
        char c = (char) byteFrom(pos++);
        while (c != '\u0000') {
            sb.append(c);
            c = (char) byteFrom(pos++);
        }
        return sb.toString();
    }

    public void close() {
        currentPos = -1;
    }

    private void checkOpen() {
        if (currentPos == -1) {
            throw new IllegalStateException();
        }
    }

    public byte[] slice(int offset, int size) {
        byte[] res = new byte[size];
        System.arraycopy(data, offset, res, 0, size);
        return res;
    }
}
