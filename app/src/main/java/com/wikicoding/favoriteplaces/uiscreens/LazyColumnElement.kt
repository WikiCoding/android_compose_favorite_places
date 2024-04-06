package com.wikicoding.favoriteplaces.uiscreens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.compose.rememberImagePainter
import com.wikicoding.favoriteplaces.R
import com.wikicoding.favoriteplaces.data.PlaceElement

@Composable
fun LazyColumnElement(element: PlaceElement) {
    val url = element.photoUri
    val painter = rememberImagePainter(url)

    Card(
        elevation = CardDefaults.cardElevation(10.dp),
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                // AsyncImage taking up the left side
                AsyncImage(
                    model = url,
                    contentDescription = "imageFromWeb",
                    contentScale = ContentScale.FillHeight,
                    modifier = Modifier
                        .weight(1f)
                        .aspectRatio(1f)
                        .clip(RoundedCornerShape(8.dp)),
                    placeholder = painterResource(id = R.drawable.baseline_photo_camera_24),
                    error = painterResource(id = R.drawable.baseline_hide_image_24),
                )

                // Text next to AsyncImage
                Column(
                    modifier = Modifier
                        .padding(start = 8.dp)
                        .weight(3f)
                ) {
                    Text(text = element.name, fontSize = 18.sp)
                    Text(text = element.description, fontSize = 12.sp)
                    Text(text = element.address, fontSize = 12.sp)
                }
            }
        }
    }
}