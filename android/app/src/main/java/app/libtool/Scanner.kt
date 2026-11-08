package app.libtool

import android.content.Context
import android.media.MediaMetadataRetriever
import android.net.Uri
import androidx.documentfile.provider.DocumentFile
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

private val audioExt = setOf("mp3", "flac", "m4a", "aac", "ogg", "opus", "wav")

/** Walks a SAF tree and reads embedded tags. Read-only: never modifies files. */
suspend fun scanTree(
    ctx: Context,
    root: Uri,
    onProgress: (Int) -> Unit = {},
): List<Track> = withContext(Dispatchers.IO) {
    val out = mutableListOf<Track>()
    fun walk(dir: DocumentFile) {
        for (f in dir.listFiles()) {
            val name = f.name ?: continue
            if (name.startsWith(".")) continue
            if (f.isDirectory) { walk(f); continue }
            if (name.substringAfterLast('.', "").lowercase() !in audioExt) continue
            out += readTrack(ctx, f)
            onProgress(out.size)
        }
    }
    DocumentFile.fromTreeUri(ctx, root)?.let(::walk)
    out
}

private fun readTrack(ctx: Context, f: DocumentFile): Track {
    val r = MediaMetadataRetriever()
    return try {
        r.setDataSource(ctx, f.uri)
        Track(
            uri = f.uri.toString(),
            name = f.name.orEmpty(),
            size = f.length(),
            durationMs = r.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION)?.toLongOrNull() ?: 0,
            title = r.extractMetadata(MediaMetadataRetriever.METADATA_KEY_TITLE).orEmpty(),
            artist = r.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ARTIST).orEmpty(),
        )
    } catch (_: Exception) {
        Track(f.uri.toString(), f.name.orEmpty(), f.length(), 0, "", "")
    } finally {
        r.release()
    }
}
