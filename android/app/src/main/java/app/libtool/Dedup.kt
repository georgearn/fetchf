package app.libtool

/** One audio file's identity. Port of mornye.core.library_dedup (tags mode). */
data class Track(
    val uri: String,
    val name: String,
    val size: Long,
    val durationMs: Long,
    val title: String,
    val artist: String,
)

data class DuplicateGroup(val key: String, val files: List<Track>) {
    val reclaimableBytes: Long get() = files.sortedByDescending { it.size }.drop(1).sumOf { it.size }
}

private val nonWord = Regex("[^\\p{L}\\p{N}_]+")
private val versionNoise = Regex(
    """\s*[(\[-]?\s*(?:(?:\d{4}\s+)?re-?master(?:ed)?(?:\s+version)?(?:\s+\d{4})?|album\s+version|original\s+version|original\s+mix|bonus\s+track|single\s+version)\s*[)\]]?\s*$""",
    RegexOption.IGNORE_CASE,
)
private val artistSplit = Regex("""\s*(?:;|/|,|&|\bfeat\.?\b|\bft\.?\b|\bwith\b|\bvs\.?\b)\s*""", RegexOption.IGNORE_CASE)

fun fold(v: String): String = nonWord.replace(v.lowercase(), " ").trim()

fun normalizeTitle(v: String): String {
    var t = v.trim()
    repeat(3) {
        val s = versionNoise.replace(t, "")
        if (s == t) return@repeat
        t = s
    }
    return fold(t)
}

fun normalizeArtist(v: String): String = fold(artistSplit.split(v.trim(), 2).firstOrNull().orEmpty())

fun identityKey(t: Track): String {
    val title = normalizeTitle(t.title)
    val artist = normalizeArtist(t.artist)
    return if (title.isEmpty() || artist.isEmpty()) "" else "$artist|$title"
}

/** Same artist+title and durations within [toleranceMs] of each other. */
fun groupDuplicates(tracks: List<Track>, toleranceMs: Long = 3000): List<DuplicateGroup> {
    val out = mutableListOf<DuplicateGroup>()
    tracks.groupBy(::identityKey).filterKeys { it.isNotEmpty() }.forEach { (key, members) ->
        if (members.size < 2) return@forEach
        val sorted = members.sortedBy { it.durationMs }
        var cluster = mutableListOf(sorted.first())
        fun flush() { if (cluster.size > 1) out += DuplicateGroup(key, cluster.sortedByDescending { it.size }) }
        for (t in sorted.drop(1)) {
            if (t.durationMs - cluster.last().durationMs <= toleranceMs) cluster += t
            else { flush(); cluster = mutableListOf(t) }
        }
        flush()
    }
    return out.sortedWith(compareByDescending<DuplicateGroup> { it.reclaimableBytes }.thenBy { it.key })
}
