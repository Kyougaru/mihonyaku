package eu.kanade.presentation.reader

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import tachiyomi.domain.ocr.model.TextBlock

@Composable
fun OCRTextSelectDialog(
    onDismissRequest: () -> Unit,
    textBlocks: List<TextBlock>,
    onBlockClick: (String) -> Unit
) {
    if (textBlocks.isEmpty()) return

    val density = LocalDensity.current

    Box(
        modifier = Modifier
            .fillMaxSize()
            .pointerInput(Unit) {
                detectTapGestures { offset ->
                    val tappedBlock = textBlocks.firstOrNull { block ->
                        val rect = block.boundingBox
                        offset.x in rect.left.toFloat()..rect.right.toFloat() &&
                            offset.y in rect.top.toFloat()..rect.bottom.toFloat()
                    }

                    if (tappedBlock == null) {
                        onDismissRequest()
                    }
                }
            }
    ) {
        textBlocks.forEach { block ->
            val rect = block.boundingBox

            val leftDp = with(density) { rect.left.toDp() }
            val topDp = with(density) { rect.top.toDp() }
            val widthDp = with(density) { rect.width.toDp() }
            val heightDp = with(density) { rect.height.toDp() }

            Box(modifier = Modifier
                .offset(
                    x = leftDp,
                    y = topDp
                )
                .size(
                    width = widthDp,
                    height = heightDp
                )
                .border(2.dp, Color.Red)
                .clickable { onBlockClick(block.text) }
            )
        }
    }
}
