object Memory {
	private val data = ByteArray(MEMORY_SIZE)

	fun getLine(line: Int) = data
		.slice((line shl OFFSET_SIZE) until ((line + 1) shl OFFSET_SIZE))
		.toByteArray()

	fun setLine(line: Int, data: ByteArray) {
		val from = line shl OFFSET_SIZE
		for (i in 0 until LINE_SIZE) this.data[from + i] = data[i]
	}

	fun check(array: ByteArray) = (0 until MEMORY_SIZE).all { data[it] == array[it] }
}
