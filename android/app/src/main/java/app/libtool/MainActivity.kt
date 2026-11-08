package app.libtool

import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent { MaterialTheme { Screen() } }
    }

    @Composable
    private fun Screen() {
        val scope = rememberCoroutineScope()
        var scanned by remember { mutableIntStateOf(0) }
        var busy by remember { mutableStateOf(false) }
        var tracks by remember { mutableStateOf<List<Track>?>(null) }
        val picker = rememberLauncherForActivityResult(ActivityResultContracts.OpenDocumentTree()) { uri: Uri? ->
            if (uri == null) return@rememberLauncherForActivityResult
            busy = true
            scope.launch {
                tracks = scanTree(this@MainActivity, uri) { scanned = it }
                busy = false
            }
        }
        val groups = remember(tracks) { tracks?.let { groupDuplicates(it) } }

        Column(Modifier.fillMaxSize().padding(16.dp).statusBarsPadding()) {
            Button(onClick = { picker.launch(null) }, enabled = !busy) { Text("Choose music folder") }
            Spacer(Modifier.height(8.dp))
            when {
                busy -> Text("Scanned $scanned files…")
                groups == null -> Text("Pick a folder to find duplicate files. Nothing is modified.")
                else -> {
                    Text("${tracks!!.size} files, ${groups.size} duplicate groups")
                    LazyColumn {
                        items(groups) { g ->
                            Column(Modifier.padding(vertical = 8.dp)) {
                                Text(g.key.replace('|', " — "), style = MaterialTheme.typography.titleSmall)
                                g.files.forEach { Text("${it.name}  (${it.size / 1024} KB)") }
                            }
                            HorizontalDivider()
                        }
                    }
                }
            }
        }
    }
}
