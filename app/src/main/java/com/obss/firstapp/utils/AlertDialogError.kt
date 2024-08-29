package com.obss.firstapp.utils

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp
import com.obss.firstapp.R

@Suppress("ktlint:standard:function-naming")
@Composable
fun AlertDialogError(
    onDismissRequest: () -> Unit,
    onClose: () -> Unit,
    dialogTitle: String,
    dialogText: String,
    icon: Painter,
) {
    AlertDialog(
        icon = {
            Icon(icon, contentDescription = EXAMPLE_ICON_DESC)
        },
        title = {
            Text(
                text = dialogTitle,
                color = Color.Black,
                fontSize = 20.sp,
                fontFamily = FontFamily(Font(R.font.ubuntu_bold)),
            )
        },
        text = {
            Text(
                text = dialogText,
                color = Color.Black,
                fontSize = 16.sp,
                fontFamily = FontFamily(Font(R.font.ubuntu_light)),
                textAlign = TextAlign.Center,
            )
        },
        onDismissRequest = {
            onDismissRequest()
        },
        confirmButton = {
            TextButton(
                onClick = {
                    onClose()
                },
            ) {
                Text(CLOSE)
            }
        },
    )
}

private const val EXAMPLE_ICON_DESC = "Example Icon"
private const val CLOSE = "Close"
