package com.example.listfirebase.predefinedlook

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DeleteForever
import androidx.compose.material.icons.filled.Logout
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.Job


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppBarView(
    title: String,
    onDeleteNavClicked: () -> Unit = {},
    onMenuNavClicked: () -> Unit = {},
    onLogoutClicked: () -> Unit ={}
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Icon(
            imageVector = Icons.Filled.Menu,
            contentDescription = null,
            modifier = Modifier.clickable {
                onMenuNavClicked()
            },
            tint = Color.Black
        )
        Icon(
            imageVector = Icons.Filled.DeleteForever,
            contentDescription = "Delete",
            tint = Color.Black,
            modifier = Modifier
                .size(24.dp)
                .clip(CircleShape)
                .clickable {
                    onDeleteNavClicked()
                },
        )
        Icon(
            imageVector = Icons.Filled.Logout,
            contentDescription = "Logout",
            tint = Color.Black,
            modifier = Modifier
                .size(24.dp)
                .clip(CircleShape)
                .clickable {
                    onLogoutClicked()
                }
        )
    }

}