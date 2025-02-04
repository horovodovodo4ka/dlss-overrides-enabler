import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.Divider
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material3.ButtonDefaults
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.WindowPosition
import androidx.compose.ui.window.application
import androidx.compose.ui.window.rememberWindowState
import java.nio.file.Paths

fun main() = application {
    val icon = painterResource("drawable/icon.png")
    val windowState = rememberWindowState(width = 300.dp, height = 150.dp, position = WindowPosition.Aligned(Alignment.Center))

    Window(
        onCloseRequest = ::exitApplication,
        title = "DLSS Overrides Enabler",
        resizable = false,
        state = windowState,
        icon = icon
    ) {
        MaterialTheme {
            App()
        }
    }
}

@Composable
@Preview
fun App() {
    val appData = System.getenv("AppData")
    val path = Paths.get(appData, "..", "Local", "NVIDIA Corporation", "NVIDIA app", "NvBackend", "ApplicationStorage.json")

    var isReadonly by remember { mutableStateOf(false) }
    var statusMessage by remember { mutableStateOf("Waiting for actions...") }
    var showRebootDialog by remember { mutableStateOf(false) }
    var showHelp by remember { mutableStateOf(false) }

    if (showRebootDialog) {
        RebootDialogWindow(
            onConfirm = {
                rebootSystem { status -> statusMessage = status }
                showRebootDialog = false
            },
            onDismiss = {
                showRebootDialog = false
            }
        )
    }

    if(showHelp) {
        HelpWindow { showHelp = false }
    }

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(12.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Button(
                onClick = {
                    runCatching {
                        unlockFile(path)
                        statusMessage = "File unlocked successfully."
                    }.onFailure {
                        statusMessage = "Failed to unlock file: ${it.message}"
                    }
                },
                enabled = isReadonly
            ) {
                Text("Unlock")
            }

            Button(
                onClick = {
                    runCatching {
                        if (isReadonly)
                            unlockFile(path)

                        modifyOverrideFlag(path)

                        lockFile(path)
                        showRebootDialog = true

                        statusMessage = "Overrides patched and file locked."
                    }.onFailure {
                        statusMessage = "Failed to patch file: ${it.message}"
                    }
                }
            ) {
                Text("Patch & Lock")
            }
        }

        Spacer(modifier = Modifier.fillMaxHeight().weight(1f, false))

        Row(
            modifier = Modifier.border(1.dp, Color.Black).padding(horizontal = 5.dp).fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {

            Text(statusMessage, fontSize = 12.sp, maxLines = 1, modifier = Modifier.weight(1f))

            Spacer(Modifier.width(12.dp))

            Button(
                modifier = Modifier.height(20.dp).wrapContentWidth(),
                contentPadding = PaddingValues(
                    start = 6.dp, end = 6.dp, top = 2.dp, bottom = 2.dp
                ),
                onClick = { showHelp = true }
            ) {
                Text("Help", fontSize = 12.sp)
            }
        }

    }

    LaunchedEffect(Unit) {
        watchFileChanges(path)
            .collect {
                isReadonly = it
            }
    }
}
