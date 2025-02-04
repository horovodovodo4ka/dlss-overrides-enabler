import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.DialogWindow
import androidx.compose.ui.window.rememberDialogState

@Composable
fun RebootDialogWindow(onConfirm: () -> Unit, onDismiss: () -> Unit) {
    DialogWindow(
        onCloseRequest = onDismiss,
        state = rememberDialogState(width = 250.dp, height = 200.dp),
        title = "System Reboot",
        resizable = false,
        content = {
            Surface {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Text("Changes will take effect after a reboot. Reboot now?")
                    Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                        Button(onClick = onConfirm) {
                            Text("Reboot")
                        }
                        Button(onClick = onDismiss) {
                            Text("Cancel")
                        }
                    }
                }
            }
        })
}
