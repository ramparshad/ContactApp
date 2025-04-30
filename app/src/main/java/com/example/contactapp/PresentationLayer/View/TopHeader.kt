package com.example.contactapp.PresentationLayer.View

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.contactapp.R

@Composable
fun TopHeader() {
    Row (modifier = Modifier.fillMaxWidth().padding(horizontal = 10.dp),
        horizontalArrangement = Arrangement.SpaceBetween){
        Text(text = "Contact App", fontWeight = FontWeight.Bold, fontSize = 20.sp)

        Image(
            painter = painterResource(id = R.drawable.recent_delete),contentDescription = null,
            contentScale = ContentScale.Crop
        )
    }
}
