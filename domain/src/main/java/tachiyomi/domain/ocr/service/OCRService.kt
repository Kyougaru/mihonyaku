package tachiyomi.domain.ocr.service

import tachiyomi.domain.ocr.model.ImageInput
import tachiyomi.domain.ocr.model.TextBlock

fun interface OCRService {
    suspend fun getTextBlocks(image: ImageInput): List<TextBlock>
}
