package com.example.listfirebase.predefinedlook

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.listfirebase.data.firebasedata.additemsapi.AddItemsData

@Composable
fun CustomAdd(
    customItem: AddItemsData,
    onRowClick: () -> Unit,
    onClick: () -> Unit
) {
    Column {
        Row(
            modifier = Modifier
                .padding(8.dp)
                .fillMaxWidth()
                .clickable { onRowClick() },
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = customItem.title,
                style = MaterialTheme.typography.titleSmall,
                maxLines = 1
            )
            Icon(
                imageVector = Icons.Default.Delete,
                contentDescription = null,
                modifier = Modifier.clickable { onClick() })
        }
    }
}