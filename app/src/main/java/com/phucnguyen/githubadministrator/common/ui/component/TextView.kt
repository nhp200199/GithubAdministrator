package com.phucnguyen.githubadministrator.common.ui.component

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.clickable
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextDecoration

@Composable
fun ClickableUrlText(
    text: String,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current

    Text(
        text = text,
        style = TextStyle(
            color = Color.Blue,
            textDecoration = TextDecoration.Underline
        ),
        modifier = modifier
            .clickable {
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(text))
                context.startActivity(intent)
            }
        ,
    )
}