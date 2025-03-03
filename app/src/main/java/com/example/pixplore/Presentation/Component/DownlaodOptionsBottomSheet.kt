package com.example.pixplore.Presentation.Component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.modifier.modifierLocalConsumer
import androidx.compose.ui.text.input.KeyboardType.Companion.Text
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun downloadOptionsBottomSheet(
    modifier: Modifier = Modifier,
    isOpen:Boolean,
    sheetState: SheetState,
    onDismissRequest: ()->Unit,
    onOptionClick: (ImageDownloadingOptions)->Unit,
    options:List<ImageDownloadingOptions> = ImageDownloadingOptions.entries
){
    if(isOpen){
        ModalBottomSheet(
            sheetState  = sheetState,
            onDismissRequest = {onDismissRequest()}
        ) {
            options.forEach{option->
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { onOptionClick(option) }
                        .padding(16.dp),
            contentAlignment = Alignment.Center
                ){
                    Text(text = option.label,style = MaterialTheme.typography.bodyLarge)
                }
            }
        }
    }

}

// create enum class
enum class ImageDownloadingOptions(val label : String){
    SMALL(label = "Download Small Size"),
    MEDIUM(label = "Download Medium Size"),
    LARGE(label = "Download Large Size")
}