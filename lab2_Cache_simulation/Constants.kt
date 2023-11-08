@file:JvmName("ConstantsKt")

const val MEMORY_SIZE = 256 * 1024
const val CACHE_SIZE = 2048
const val LINE_SIZE = 16
const val CACHE_TAG_SIZE = 8

val OFFSET_SIZE = LINE_SIZE.countTrailingZeroBits()
val CACHE_ADDR_SIZE = MEMORY_SIZE.countTrailingZeroBits()
val CACHE_SET_SIZE = CACHE_ADDR_SIZE - OFFSET_SIZE - CACHE_TAG_SIZE // 6
val CACHE_SETS_COUNT = 1 shl CACHE_SET_SIZE // 64

fun parseAddr(address: Int) = listOf(
	(address and 0b111111110000000000) shr 10,
	(address and 0b000000001111110000) shr 4,
	(address and 0b000000000000001111),
)