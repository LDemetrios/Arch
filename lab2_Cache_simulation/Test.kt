import java.util.*

operator fun ByteArray.get(rng: IntRange) = slice(rng).toByteArray()
operator fun ByteArray.set(rng: IntRange, byteArray: ByteArray) =
	rng.withIndex().forEach { (j, i) ->
		this[i] = byteArray[j]
	}

fun test() {
	val local = ByteArray(MEMORY_SIZE)
	val rnd = Random()
	for (i in 0 until 1000000000) {
		when (rnd.nextInt(8)) {
			0 -> Unit // NOP - ignore
			1 -> { //READ8
				val addr = rnd.nextInt(MEMORY_SIZE)
				val read = Cache.read8(addr)
				if (local[addr] != read) println("Expected byte ${local[addr]}, got $read")
			}

			2 -> { //READ16
				val addr = rnd.nextInt(MEMORY_SIZE / 2) * 2
				val read = Cache.read16(addr)
				val actual = local[addr..(addr + 1)].toShort()
				if (actual != read) println("Expected short ${actual}, got $read")
			}

			3 -> { //READ32
				val addr = rnd.nextInt(MEMORY_SIZE / 4) * 4
				val read = Cache.read32(addr)
				val actual = local[addr..(addr + 3)].toInt()
				if (actual != read) println("Expected int ${actual}, got $read")
			}

			4 -> { //INVALIDATE
				val addr = rnd.nextInt(MEMORY_SIZE)
				Cache.invalidate(addr)
			}

			5 -> { //WRITE8
				val addr = rnd.nextInt(MEMORY_SIZE)
				val value = rnd.nextInt().toByte()
				Cache.write8(addr, value)
				local[addr] = value
			}

			6 -> { //WRITE16
				val addr = rnd.nextInt(MEMORY_SIZE / 2) * 2
				val value = rnd.nextInt().toShort()
				Cache.write16(addr, value)
				local[addr..(addr + 1)] = value.toBytes()
			}

			7 -> { //WRITE32
				val addr = rnd.nextInt(MEMORY_SIZE / 4) * 4
				val value = rnd.nextInt()
				Cache.write32(addr, value)
				local[addr..(addr + 3)] = value.toBytes()
			}
		}
	}
	Cache.invalidateAll()
	println(Memory.check(local))
}