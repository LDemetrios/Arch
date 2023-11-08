import java.util.*

class CacheLine {
	var lastUpd = false // Uses that way = 2
	var tag: Int = 0
	var valid: Boolean = false
	var dirty: Boolean = false
	var line: ByteArray = ByteArray(LINE_SIZE)

	override fun toString(): String = if (valid) {
		(if (lastUpd) "UPD1" else "upd0") +
			" tag " + tag.toBinary(CACHE_TAG_SIZE) +
			" " + (if (dirty) "drt" else "cln") +
			" " + line.joinToString(":") { it.toHex() }.uppercase(Locale.getDefault())
	} else "INVALID"
}

class CacheSet(val set: Int) {
	private val line1 = CacheLine()
	private val line2 = CacheLine()

	private fun sorted(): Pair<CacheLine, CacheLine> {
		if (!line1.valid && !line2.valid) throw AssertionError()
		return when {
			!line1.valid  -> line2 to line2
			!line2.valid  -> line1 to line1
			line1.lastUpd -> line1 to line2
			else          -> line2 to line1
		}
	}

	fun load(tag: Int, line: ByteArray) {
		if (line1.valid && line2.valid) {
			val sorted = sorted()
			sorted.first.lastUpd = false
			writeToMemory(sorted.second)
			readFromMemory(sorted.second, tag, line)
		} else if (line1.valid) {
			readFromMemory(line2, tag, line)
			line1.lastUpd = false
		} else if (line2.valid) {
			readFromMemory(line1, tag, line)
			line2.lastUpd = false
		} else {
			readFromMemory(line1, tag, line)
			line2.lastUpd = false
		}
	}

	private fun writeToMemory(line: CacheLine) {
		if (!line.dirty) return
		maccs++
		Memory.setLine(line.tag shl CACHE_SET_SIZE or set, line.line)
		line.dirty = false
	}

	fun invalidate(tag: Int) {
		if (line1.valid && line1.tag == tag) {
			writeToMemory(line1)
			line1.valid = false
			line2.lastUpd = false
		}
		if (line2.valid && line2.tag == tag) {
			writeToMemory(line2)
			line2.valid = false
			line1.lastUpd = false
		}
	}

	private fun readFromMemory(cacheline: CacheLine, tag: Int, line: ByteArray) {
		cacheline.tag = tag
		cacheline.line = line
		cacheline.dirty = false
		cacheline.valid = true
		cacheline.lastUpd = true
	}

	fun hits(tag: Int) = (line1.valid && line1.tag == tag) ||
		(line2.valid && line2.tag == tag)

	fun lineWith(tag: Int) =
		if (line1.valid && line1.tag == tag) {
			line1.lastUpd = true
			line2.lastUpd = false
			line1
		} else if (line2.valid && line2.tag == tag) {
			line2.lastUpd = true
			line1.lastUpd = false
			line2
		} else throw java.lang.AssertionError()

	fun invalidateAll() {
		writeToMemory(line1)
		line1.valid = false
		line2.lastUpd = false
		writeToMemory(line2)
		line2.valid = false
		line1.lastUpd = false
	}
}

object Cache {
	val data = List(CACHE_SETS_COUNT) { CacheSet(it) }

	fun read8(address: Int): Byte {
		val (tag, set, off) = parseAddr(address)
		val line = accessLine(set, tag, false)
		return line[off]
	}

	fun read16(address: Int): Short {
		val (tag, set, off) = parseAddr(address)
		val line = accessLine(set, tag, false)
		return line[off..(off + 1)].toShort()
	}

	fun read32(address: Int): Int {
		val (tag, set, off) = parseAddr(address)
		val line = accessLine(set, tag, false)
		return line[off..(off + 3)].toInt()
	}

	fun write8(address: Int, b: Byte) {
		val (tag, set, off) = parseAddr(address)
		val line = accessLine(set, tag, true)
		line[off] = b
	}

	fun write16(address: Int, s: Short) {
		val (tag, set, off) = parseAddr(address)
		val line = accessLine(set, tag, true)
		line[off..(off + 1)] = s.toBytes()
	}

	fun write32(address: Int, i: Int) {
		val (tag, set, off) = parseAddr(address)
		val line = accessLine(set, tag, true)
		line[off..(off + 3)] = i.toBytes()
	}

	private fun accessLine(set: Int, tag: Int, pollute: Boolean): ByteArray {
		val block = data[set]
		if (block.hits(tag)) {
			hits++
			if (!pollute) { //Read request
				hitrs++; resps++
			} else { //Write request
				hitrs++ //No response wait needed
			}
		} else {
			misss++
			maccs++
			if (!pollute) resps++
			misses++
			block.load(tag, getBytes(tag, set))
		}
		val line = block.lineWith(tag)
		if (pollute) line.dirty = true
		return line.line
	}

	private fun getBytes(tag: Int, set: Int): ByteArray {
		return Memory.getLine(tag shl CACHE_SET_SIZE or set)
	}

	fun invalidate(address: Int) {
		val (tag, set, off) = parseAddr(address)
		data[set].invalidate(tag)
	}

	fun invalidateAll() = data.forEach(CacheSet::invalidateAll)
}
