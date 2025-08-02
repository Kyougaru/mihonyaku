package tachiyomi.domain.ocr.model

data class TextBlock(
    val text: String,
    val boundingBox: Rectangle
)
