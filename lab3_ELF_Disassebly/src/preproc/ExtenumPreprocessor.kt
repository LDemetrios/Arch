package preproc

import java.io.File

fun main() {
	val w =
		"[\u0009\u000a\u000b\u000c\u000d\u001c\u001d\u001e\u001f\u0020\u1680\u2000\u2001\u2002\u2003\u2004\u2005\u2006\u2008\u2009\u200a\u2028\u2029\u205f\u3000]"
	val nameRegex = "[A-Za-z][A-Za-z_0-9]*"
	val number = "-?[0-9]*|-?0x[A-Fa-f0-9]*|-?0b[01]*"
	val beginPattern = Regex("$w*($nameRegex)$w+values$w*\\{$w*")
	val valuePattern = Regex("$w*($nameRegex)$w*:$w*($number)$w*")
	val rangePattern = Regex("$w*($nameRegex)$w*:$w*($number)$w*\\.\\.\\.?($number)$w*")
	val endPattern = Regex("$w*}$w*")

	val files = File("src")
		.allSubfiles()
		.filter { it.extension == "extenum" }

	for (file in files) {
		val location = file.invariantSeparatorsPath.split("/").drop(1).dropLast(1)
		val content = file.readLines()
		var name = ""
		var values = mutableListOf<Pair<String, Int>>()
		var ranges = mutableListOf<Triple<String, Int, Int>>()
		var state = 0 //0 - '}' passed, 1 - name passed, content expected
		val structures =
			mutableListOf<Triple<String, List<Pair<String, Int>>, List<Triple<String, Int, Int>>>>()
		for (line in content) {
			if (line.matches(Regex("$w*"))) continue
			if (state == 0) {
				name =
					beginPattern.matchEntire(line)?.groups?.get(1)?.value ?: throw AssertionError()
				state = 1
			} else {
				if (endPattern.matches(line)) {
					state = 0
					structures.add(Triple(name, values, ranges))
					values = mutableListOf()
					ranges = mutableListOf()
				} else if (valuePattern.matches(line)) {
					val (full, a, b) = valuePattern.matchEntire(line)!!.groups.map { it!!.value }
					values.add(a to b.toInt0())
				} else if (rangePattern.matches(line)) {
					val (full, a, b, c) = rangePattern.matchEntire(line)!!.groups.map { it!!.value }
					ranges.add(Triple(a, b.toInt0(), c.toInt0()))
				}
			}
		}

		for (structure in structures) {
			val content = constuct(location, structure.first, structure.second, structure.third)
			val f = File("src/" + location.joinToString("/") + "/" + structure.first + ".java")
			if (f.exists()) f.delete()
			f.createNewFile()
			f.writeText(content)
		}
	}
}

fun String.toInt0() = when {
	startsWith("0x") -> substring(2).toUInt(16).toInt()
	startsWith("0b") -> substring(2).toUInt(2).toInt()
	else             -> toInt()
}


fun constuct(
	location: List<String>,
	name: String,
	values: List<Pair<String, Int>>,
	ranges: List<Triple<String, Int, Int>>
): String {
	return """
package ${location.joinToString(".")};

import elf.parser.IncorrectFileSignature;

/**
 * Code automatically generated using Extended Enum Preprocessor
 */
public interface $name {
    int value();

    enum List implements $name {
${values.joinToString("\n") { "        ${it.first}(${it.second})," }}
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
${
		ranges.joinToString("\n") {
			"""
    record ${it.first}(int value) implements $name {
        @Override
        public String toString() {
            return "${it.first}(" + value + ")";
        }
    }"""
		}
	}

    static $name of(int value) {
        ${
		ranges.joinToString(" else ") {
			"""if (${it.second} <= value && value <= ${it.third}) {
            return new ${it.first}(value);
        }"""
		}
	}${if(ranges.isEmpty()) "" else " else {"}
            for ($name it : $name.List.values()) {
                if (it.value() == value) {
                    return it;
                }
            }
            throw new IncorrectFileSignature("Incorrect value " + value + " for $name");
        ${if(ranges.isEmpty()) "" else "}"}
    }
}
	""".trimIndent()
}

private fun File.allSubfiles() =
	setOf(this).whileChanges {
		it.flatMapTo(mutableSetOf()) {
			if (it.isDirectory) it.listFiles()!!.toList() else listOf(it)
		}
	}

private fun <T> T.whileChanges(func: (T) -> T): T {
	var prev = this
	var res = func(this)
	while (res != prev) {
		prev = res
		res = func(res)
	}
	return res
}