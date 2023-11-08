package main;

import disassembly.RiscvDisassembler;
import elf.objects.Elf;
import elf.parser.ElfParser;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

public class Main {
    private static String convert(byte[] data) {
        StringBuilder res = new StringBuilder(".text\n");
        Elf elf = ElfParser.parse(data);
        String text = RiscvDisassembler.disassemble(
                elf.text,
                elf.metaInf.littleEndian(),
                elf.metaInf.arch(),
                elf.symtab.getSymbols()
        );
        res.append(text);
        res.append("\n.symtab\n");
        res.append(elf.symtab);
        res.append("\n");
        return res.toString();
    }

    public static void main(String[] args) {
        if (args.length < 2) error("Usage: java -jar jarname.jar inputfile outputfile");
        String inp = args[0], out = args[1];
        var inputFile = new File(inp);
        if (!inputFile.exists()) error("File " + inp + " doesn't exist");
        byte[] input = null;
        try (FileInputStream fis = new FileInputStream(inputFile)) {
            input = fis.readAllBytes();
        } catch (FileNotFoundException e) {
            error("File disappeared right out of our hands");
        } catch (IOException e) {
            error("Unknown IO Exception", e);
        }
        String result = convert(input);
        File outputFile = new File(out);
        if (outputFile.exists()) {
            Scanner sc = new Scanner(System.in);
            boolean clear = false;
            boolean overwrite = false;
            System.out.println("File " + out + " already exists. Overwrite? y/n");
            while (!clear) {
                String line = sc.nextLine();
                if (!line.isEmpty()) {
                    char first = line.charAt(0);
                    if (first == 'Y' || first == 'y') {
                        clear = true;
                        overwrite = true;
                    } else if (first == 'N' || first == 'n') {
                        clear = true;
                        overwrite = false;
                    }
                }
                if (!clear) System.out.println("The who, the what, the why, the when, the where? (c) Try again.");
            }
            if (overwrite) {
                boolean success = outputFile.delete();
                if (!success) error("Oh God, this file is undeletable! Turn and burn!");
            }
        }
        if (outputFile.exists()) {
            error("Err... I already deleted it! Where did it come from again?");
        }
        try {
            outputFile.createNewFile();
        } catch (IOException e) {
            if (!"The system cannot find the path specified".equals(e.getMessage())) error("Unknown IO exception", e);
            int lastSeparator = outputFile.getPath().lastIndexOf('\\');
            String dirs = outputFile.getPath().substring(0, lastSeparator);

            boolean successfully = new File(dirs).mkdirs();
            if (!successfully) error("Can't create the path specified");

            try {
                outputFile.createNewFile();
            } catch (IOException e1) {
                if (!"The system cannot find the path specified".equals(e1.getMessage()))
                    error("Unknown IO exception", e);
                error("Can't create the path specified");
            }
        }
        try (FileOutputStream fos = new FileOutputStream(outputFile)) {
            fos.write(result.getBytes(StandardCharsets.UTF_8));
        } catch (FileNotFoundException e) {
            error("What's going all with your files? It's been created about Plank-time ago, but now it's nowhere.");
        } catch (IOException e) {
            error("Unknown IO exception", e);
        }
    }

    private static void error(String str) {
        error(str, null);
    }

    private static void error(String str, Exception cause) {
        System.err.println(str);
        if (cause != null) cause.printStackTrace();
        System.exit(0);
    }
}
