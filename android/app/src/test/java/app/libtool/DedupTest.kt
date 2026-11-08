package app.libtool

import org.junit.Assert.assertEquals
import org.junit.Test

class DedupTest {
    private fun t(n: String, title: String, artist: String, d: Long, size: Long = 100) =
        Track(n, n, size, d, title, artist)

    @Test fun remasterSuffixAndFeaturingCollapse() {
        val g = groupDuplicates(listOf(
            t("a", "Song (2011 Remaster)", "Band feat. Guest", 200_000),
            t("b", "Song", "Band", 201_000),
        ))
        assertEquals(1, g.size)
    }

    @Test fun differentDurationsStaySeparate() {
        assertEquals(0, groupDuplicates(listOf(t("a", "Song", "Band", 200_000), t("b", "Song", "Band", 260_000))).size)
    }
}
