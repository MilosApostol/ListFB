package com.example.listfirebase.predefinedlook

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.TextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp


@Composable
fun TextFieldLookPassword(
    text: String,
    label: String,
    onTextChange: (String) -> Unit,
    leadingIcon: ImageVector? = null,
    trailingIconStart: ImageVector? = null,
    trailingIconEnd: ImageVector? = null,
    onClick: () -> Unit,
    visible: Boolean
) {
    TextField(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White),
        textStyle = TextStyle(
            color = Color.Black,
            fontWeight = FontWeight.Bold
        ),
        value = text,
        onValueChange = { onTextChange(it) },
        label = {
            Text(
                text = label,
                color = Color.Black,
                fontWeight = FontWeight.Bold
            )
        },
        visualTransformation = if (visible) VisualTransformation.None
        else PasswordVisualTransformation(),
        keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Done),
        leadingIcon = {
            if (leadingIcon != null) {
                Icon(
                    imageVector = leadingIcon,
                    contentDescription = null,
                    modifier = Modifier.size(18.dp),
                    tint = Color.Black
                )
            }
        },
        trailingIcon = {
            if (trailingIconEnd != null) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    IconButton(
                        onClick = { onClick() },
                    ) {
                        Icon(
                            imageVector = if (visible) trailingIconEnd
                            else Icons.Default.Visibility,
                            contentDescription = "Toggle password visibility",
                            tint = Color.Black
                        )
                    }
                }
            }
        })
}


@Composable
fun TextFieldLookEmail(
    onTextChange: (String) -> Unit,
    text: String = "",
    label: String = "",
    leadingIcon: ImageVector? = null,
    trailingIcon: ImageVector? = null,
) {
    TextField(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White),
        textStyle = TextStyle(
            color = Color.Black,
            fontWeight = FontWeight.Bold
        ),
        value = text,
        onValueChange = { onTextChange(it) },
        label = {
            Text(
                text = label,
                color = Color.Black,
                fontWeight = FontWeight.Bold
            )
        },
        leadingIcon = {
            if (leadingIcon != null) {
                Icon(
                    imageVector = leadingIcon,
                    contentDescription = null,
                    modifier = Modifier.size(18.dp),
                    tint = Color.Black
                )
            }
        })
}

