var hits = 0
var misses = 0

const val NEXT_ITERATION = 1
const val INITIALIZATION = 1
const val SUMMATION = 1
const val INCREMENT = 1
const val COMPARISON = 1
const val MULTIPLICATION = 5
const val HIT_REQUEST_DELAY = 6
const val MISS_REQUEST_REDIRECT_DELAY = 4
const val RESPONSE_TIME = 1 // Never send read32 request
const val CONNECTION_WAIT = 1 // Never send read32 request
const val MEM_CTR = 100
const val M = 64
const val N = 60
const val K = 32 //array creation outside the function

var iters = 0
var inits = 0
var incrs = 0
var summs = 0
var comps = 0
var mults = 0
var hitrs = 0
var misss = 0
var resps = 0
var waits = 0
var maccs = 0

fun countFor(iterations: Int) {
	iters += iterations - 1
	incrs += iterations
	comps += iterations
}

fun main() = count()

fun count() {
	var pa = 0
	inits++
	var pc = M * K + 2 * K * N // compile-time constant
	inits++
	countFor(M)
	for (y in 0 until M) {
		countFor(N)
		for (x in 0 until N) {
			var pb = M * K // compile-time constant
			inits++
			var s = 0
			inits++
			countFor(K)
			for (k in 0 until K) {
				s += Cache.read8(pa + k) * Cache.read16(pb + 2 * x)
				summs++; mults++; waits++//Tactes for memory access are count in Cache
				pb += 2 * N
				summs++
			}
			Cache.write32(pc + 4 * x, s)
		}
		pa += K
		summs++
		pc += 4 * N
		summs++
	}
	println(
		"""
$iters jumps, cost: ${iters * NEXT_ITERATION}
$inits initializations, cost: ${inits * INITIALIZATION}
$incrs increments, cost: ${incrs * INCREMENT}
$summs summations, cost: ${summs * SUMMATION}
$comps comparisons, cost: ${comps * COMPARISON}
$mults multiplications, cost: ${mults * MULTIPLICATION}
$hitrs cache requests with hit, cost: ${hitrs * HIT_REQUEST_DELAY}
$misss cache requests with miss, cost: ${misss * MISS_REQUEST_REDIRECT_DELAY}
$waits delays between cache read-requests, cost: ${waits * CONNECTION_WAIT}
$resps cache response delays, cost: ${resps * RESPONSE_TIME}
$maccs memory accesses, cost: ${maccs * MEM_CTR}

summary cost: ${
			iters * NEXT_ITERATION +
				inits * INITIALIZATION +
				summs * SUMMATION +
				comps * COMPARISON +
				mults * MULTIPLICATION +
				hitrs * HIT_REQUEST_DELAY +
				misss * MISS_REQUEST_REDIRECT_DELAY +
				resps * RESPONSE_TIME +
				maccs * MEM_CTR
		}
	""".trimIndent()
	)
	println("$hits hits, $misses misses")
}


