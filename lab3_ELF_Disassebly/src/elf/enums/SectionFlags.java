package elf.enums;

public class SectionFlags {
    private static final long SHF_WRITE = 0x1;
    private static final long SHF_ALLOC = 0x2;
    private static final long SHF_EXECINSTR = 0x4;
    private static final long SHF_MERGE = 0x10;
    private static final long SHF_STRINGS = 0x20;
    private static final long SHF_INFO_LINK = 0x40;
    private static final long SHF_LINK_ORDER = 0x80;
    private static final long SHF_OS_NONCONFORMING = 0x100;
    private static final long SHF_GROUP = 0x200;
    private static final long SHF_TLS = 0x400;
    private static final long SHF_MASKOS = 0x0FF00000;
    private static final long SHF_MASKPROC = 0xF0000000;
    private static final long SHF_ORDERED = 0x4000000;
    private static final long SHF_EXCLUDE = 0x8000000;

    public final long value;
    public final boolean writable; //Writable
    public final boolean allocates; //Occupies memory during execution
    public final boolean executable; //Executable
    public final boolean mergable; //Might be merged
    public final boolean strings; //Contains null-terminated strings
    public final boolean infoLinkPresent; //'sh_info' contains SHT index
    public final boolean preserveOrderAfterCombining; //Preserve order after combining
    public final boolean osSpecificHandlingRequired; //Non-standard OS specific handling required
    public final boolean memberOfGroup; //Section is member of a group
    public final boolean threadLocal; //Section hold thread-local data
    public final long osSpecific; //OS-specific
    public final long procSpecific; //Processor-specific
    public final boolean specialOrderingRequired; //Special ordering requirement (Solaris)
    public final boolean excludedUnlessReferencedOrAllocated; //Section is excluded unless referenced or allocated (Solaris)

    public SectionFlags(long value) {
        this.value = value;
        writable = (value & SHF_WRITE) != 0;
        allocates = (value & SHF_ALLOC) != 0;
        executable = (value & SHF_EXECINSTR) != 0;
        mergable = (value & SHF_MERGE) != 0;
        strings = (value & SHF_STRINGS) != 0;
        infoLinkPresent = (value & SHF_INFO_LINK) != 0;
        preserveOrderAfterCombining = (value & SHF_LINK_ORDER) != 0;
        osSpecificHandlingRequired = (value & SHF_OS_NONCONFORMING) != 0;
        memberOfGroup = (value & SHF_GROUP) != 0;
        threadLocal = (value & SHF_TLS) != 0;
        osSpecific = value & SHF_MASKOS;
        procSpecific = value & SHF_MASKPROC;
        specialOrderingRequired = (value & SHF_ORDERED) != 0;
        excludedUnlessReferencedOrAllocated = (value & SHF_EXCLUDE) != 0;
    }

    @Override
    public String toString() {
        return "SectionFlags {\n" +
                "value = " + value + "\n" +
                (writable ? "V" : "X") + " writable\n" +
                (allocates ? "V" : "X") + " allocates\n" +
                (executable ? "V" : "X") + " executable\n" +
                (mergable ? "V" : "X") + " mergable\n" +
                (strings ? "V" : "X") + " strings\n" +
                (infoLinkPresent ? "V" : "X") + " info link present\n" +
                (preserveOrderAfterCombining ? "V" : "X") + " preserve order after combining\n" +
                (osSpecificHandlingRequired ? "V" : "X") + " os specific handling required\n" +
                (memberOfGroup ? "V" : "X") + " member of group\n" +
                (threadLocal ? "V" : "X") + " thread local\n" +
                (osSpecific) + " os specific\n" +
                (procSpecific) + " proc specific\n" +
                (specialOrderingRequired ? "V" : "X") + " special ordering required\n" +
                (excludedUnlessReferencedOrAllocated ? "V" : "X") + " excluded unless referenced or allocated\n" +
                '}';
    }
}
