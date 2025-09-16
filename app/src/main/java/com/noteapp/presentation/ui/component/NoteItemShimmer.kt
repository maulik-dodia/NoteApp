package com.noteapp.presentation.ui.component

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Card
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.noteapp.util.NoteConstant.FLOAT_ONE
import com.noteapp.util.shimmerEffect

@Composable
fun NoteItemShimmer() {
    Card(modifier = Modifier
        .fillMaxWidth()
        .padding(all = 8.dp)
    ) {
        Row(modifier = Modifier
            .fillMaxWidth()
            .padding(all = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(weight = FLOAT_ONE)) {
                Box(modifier = Modifier
                    .fillMaxWidth()
                    .height(height = 25.dp)
                    .shimmerEffect()
                )
                Spacer(modifier = Modifier.height(height = 4.dp))
                Box(modifier = Modifier
                    .width(width = 250.dp)
                    .height(height = 20.dp)
                    .shimmerEffect()
                )
                Spacer(modifier = Modifier.height(height = 8.dp))
                Box(modifier = Modifier
                    .width(width = 175.dp)
                    .height(height = 15.dp)
                    .shimmerEffect()
                )
            }
            IconButton(
                onClick = { }
            ) {
                Box(modifier = Modifier
                    .fillMaxSize()
                    .shimmerEffect()
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewNoteItemShimmer() {
    NoteItemShimmer()
}