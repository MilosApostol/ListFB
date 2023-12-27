package com.example.listfirebase.predefinedlook

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.listfirebase.data.firebasedata.additemsapi.AddItemsData


@Composable
fun SearchItems(itemsData: AddItemsData, onClick: (AddItemsData) -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = { onClick(itemsData) })
            .padding(8.dp)
    ) {
        Column {
            Text(
                text = itemsData.title,
                style = MaterialTheme.typography.bodySmall,
                maxLines = 1
            )
            Text(
                text = "price: ${itemsData.price}",
                style = MaterialTheme.typography.bodySmall,
                maxLines = 1
            )
            Text(
                text = "description: ${itemsData.description}",
                style = MaterialTheme.typography.bodySmall,
                maxLines = 1
            )
        }
    }

}

@Preview
@Composable
fun search() {
    SearchItems(itemsData = AddItemsData(id = "0", "title", "tuc", "tuc"),
        onClick = {})
}