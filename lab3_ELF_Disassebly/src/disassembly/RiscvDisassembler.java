package disassembly;

import bytearrparser.BytearrParser;
import elf.enums.Architecture;
import elf.enums.SymbolType;
import elf.objects.Section;
import elf.objects.Symbol;
import util.Utilities;

import java.util.*;

public class RiscvDisassembler {
    public static String disassemble(Section progbits, boolean littleEndian, Architecture arch, Symbol[] symbols) {
        byte[] bytes = progbits.rawContent();
        BytearrParser parser = new BytearrParser(bytes, littleEndian, arch);
        int commandsNum = bytes.length / 4; //Currently only 32-bit commands are supported
        List<Command> commands = new ArrayList<>(commandsNum);
        for (int i = 0; i < commandsNum; i++) {
            int code = parser.nextInt();
            if ((code & 3) != 3) throw new IllegalArgumentException("16-bit instructions are not supported");
            if (((code >> 2) & 7) == 7)
                throw new IllegalArgumentException("more-than-32-bit instructions are not supported");
            commands.add(Command.of(code, Utilities.assertIsInt(progbits.virtualAddress + i * 4)));
        }
        Map<Integer, String> labels = new HashMap<>();
        for (Symbol sym : symbols) {
            if (sym.type() != SymbolType.List.FUNC) continue;
            labels.put(Utilities.assertIsInt(sym.value()), sym.name());
        }
        int iter = 0;
        for (Command com : commands) {
            if (!com.links()) continue;
            int to = com.link();
            if (labels.containsKey(to)) continue;
            labels.put(to, "L" + (iter++));
        }
        labels.forEach((i, s) -> commands.add(new LabelCommandStub(0, i, s)));
        commands.sort(
                Comparator.comparingInt(c -> ((Command) c).address)
                        .thenComparing((o1, o2) -> o1 instanceof LabelCommandStub ? -1 : o2 instanceof LabelCommandStub ? 1 : 0)
        );
        StringBuilder res = new StringBuilder();
        for (Command com : commands) {
            res.append(com.toString(com.links() ? labels.get(com.link()) : null));
        }
        return res.toString();
    }
}
