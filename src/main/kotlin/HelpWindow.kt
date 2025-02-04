import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.DialogWindow
import androidx.compose.ui.window.rememberDialogState
import com.mikepenz.markdown.compose.Markdown
import com.mikepenz.markdown.m3.markdownColor
import com.mikepenz.markdown.m3.markdownTypography
import com.mikepenz.markdown.model.DefaultMarkdownTypography

@Composable
fun HelpWindow(onClose: () -> Unit) {
    DialogWindow(
        onCloseRequest = onClose,
        resizable = false,
        state = rememberDialogState(width = 700.dp, height = 405.dp),
        title = "Help",
    ) {
        val help = """
            The **DLSS Overrides** settings are disabled by default for "unsupported" applications. These settings are stored in a special file called **ApplicationStorage.json**, which is used by the **Nvidia App** and **Nvidia drivers**.
            
            #### How the Program Works:
            1. **Patch & Lock** — makes changes to the file to enable the DLSS Overrides settings and sets the file to read-only. This is necessary to prevent the **Nvidia App** from reverting the settings to their original state.
            2. **Unlock** — makes the file editable. This is needed if you want to add a new application or automatically rescan games in the **Nvidia App**.
            
            #### Modification Procedure:
            - Press **Unlock** to make the file editable.
            - Make the necessary changes in the **Nvidia App**.
            - Press **Patch & Lock** again to lock in the changes.
            - You may need to reconfigure games in the **Nvidia App** after applying the changes.
            
            To apply the new settings, you will need to restart your computer, as the file is cached by the drivers.
        """.trimIndent()

        MaterialTheme {
            Box(modifier = Modifier.padding(16.dp).fillMaxSize()) {
                Markdown(help, markdownColor(), customMarkdownTypography)
            }
        }
    }
}

val customMarkdownTypography = DefaultMarkdownTypography(
    h1 = TextStyle(fontSize = 20.sp, fontWeight = FontWeight.Bold),
    h2 = TextStyle(fontSize = 18.sp, fontWeight = FontWeight.Bold),
    h3 = TextStyle(fontSize = 16.sp, fontWeight = FontWeight.Bold),
    h4 = TextStyle(fontSize = 14.sp, fontWeight = FontWeight.Bold),
    h5 = TextStyle(fontSize = 13.sp, fontWeight = FontWeight.Bold),
    h6 = TextStyle(fontSize = 12.sp, fontWeight = FontWeight.Bold),
    text = TextStyle(fontSize = 14.sp),
    code = TextStyle(fontSize = 13.sp, fontWeight = FontWeight.Medium),
    inlineCode = TextStyle(fontSize = 13.sp, background = androidx.compose.ui.graphics.Color.LightGray),
    quote = TextStyle(fontSize = 14.sp, fontStyle = androidx.compose.ui.text.font.FontStyle.Italic),
    paragraph = TextStyle(fontSize = 14.sp),
    ordered = TextStyle(fontSize = 14.sp),
    bullet = TextStyle(fontSize = 14.sp),
    list = TextStyle(fontSize = 14.sp),
    link = TextStyle(fontSize = 14.sp, color = androidx.compose.ui.graphics.Color.Blue)
)
