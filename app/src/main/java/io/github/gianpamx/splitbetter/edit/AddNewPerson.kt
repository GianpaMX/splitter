package io.github.gianpamx.splitbetter.edit

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.unit.dp

@Composable
fun AddNewPerson(onAddNewPayerClick: (() -> Unit)? = null) {
    Row(
        modifier = Modifier
            .height(56.dp)
            .fillMaxWidth()
            .clickable {
                onAddNewPayerClick?.invoke()
            }
            .padding(horizontal = 16.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text("Add new person...", color = Color.Gray, fontStyle = FontStyle.Italic)
    }
}
