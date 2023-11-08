fun intOf(a: Byte, b: Byte, c: Byte, d: Byte) =
	a.toUByte().toInt() shl 24 or (b.toUByte().toInt() shl 16) or (c.toUByte().toInt() shl 8) or d
		.toUByte().toInt()

fun shortOf(c: Byte, d: Byte) = ((c.toInt() shl 8) or d.toInt()).toShort()

fun ByteArray.toInt() = intOf(this[0], this[1], this[2], this[3])
fun ByteArray.toShort() = shortOf(this[0], this[1])

fun Int.toBytes() =
	listOf(this shr 24, this shr 16, this shr 8, this).map(Int::toByte).toByteArray()

fun Short.toBytes() = listOf(this.toInt() shr 8, this).map(Number::toByte).toByteArray()

fun Int.toBinary(n: Int) = toString(2).run { "0".repeat(n - length) + this }
fun Byte.toHex() = toUByte().toString(16).run { "0".repeat(2 - length) + this }

