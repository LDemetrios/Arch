package elf.enums;

import elf.parser.IncorrectFileSignature;

/**
 * Code automatically generated using Extended Enum Preprocessor
 */
public interface EMachine {
    int value();

    enum List implements EMachine {
        NONE(0),
        M32(1),
        SPARC(2),
        IAMCU(6),
        MIPS(8),
        S370(9),
        MIPS_RS3_LE(10),
        PARISC(15),
        PPC(20),
        PPC64(21),
        S390(22),
        SPU(23),
        V800(36),
        FR20(37),
        RH32(38),
        MCORE(39),
        RCE(39),
        ARM(40),
        OLD_ALPHA(41),
        SH(42),
        SPARCV9(43),
        TRICORE(44),
        ARC(45),
        H8_300(46),
        H8_300H(47),
        H8S(48),
        H8_500(49),
        IA_64(50),
        MIPS_X(51),
        COLDFIRE(52),
        MMA(54),
        PCP(55),
        NCPU(56),
        NDR1(57),
        STARCORE(58),
        ME16(59),
        ST100(60),
        TINYJ(61),
        X86_64(62),
        MCST_ELBRUS(175),
        TI_C6000(140),
        AARCH64(183),
        RISCV(243),
        BPF(247),
        ;
        final int value;

        List(int value) {
            this.value = value;
        }

        @Override
        public int value() {
            return value;
        }
    }


    static EMachine of(int value) {
        
            for (EMachine it : EMachine.List.values()) {
                if (it.value() == value) {
                    return it;
                }
            }
            throw new IncorrectFileSignature("Incorrect value " + value + " for EMachine");
        
    }
}