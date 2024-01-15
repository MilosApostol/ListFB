package com.example.listfirebase.predefinedlook

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.listfirebase.data.firebasedata.listfirebase.ListEntity

@Composable
fun ListItems(
    list: ListEntity = ListEntity(listName = "aaaaaaaaaaaaaaa"),
    modifier: Modifier = Modifier,
    onTextClick: () -> Unit = {},
    onDeleteClick: () -> Unit = {},
    onDotsClick: () -> Unit = {},
) {
    Card(
        modifier
            .fillMaxWidth()
            .background(Color.White)
            .clickable { }
            .padding(8.dp), elevation = 8.dp, shape = RoundedCornerShape(16.dp)

    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Box(
                modifier = Modifier
                    .weight(1f)
                    .padding(end = 8.dp)
                    .clickable {
                        onTextClick()
                    }, contentAlignment = Alignment.CenterStart
            ) {
                Text(
                    text = list.listName,
                    color = Color.Black,
                    style = MaterialTheme.typography.bodyMedium,
                )
            }
            Icon(
                imageVector = Icons.Default.MoreVert,
                contentDescription = "More options",  // Add a description
                tint = Color.Black,
                modifier = Modifier.clickable {
                    onDotsClick()
                })
        }
    }
}