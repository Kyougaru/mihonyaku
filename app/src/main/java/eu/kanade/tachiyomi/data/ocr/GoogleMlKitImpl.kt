package eu.kanade.tachiyomi.data.ocr

import android.graphics.BitmapFactory
import android.graphics.Rect
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.text.TextRecognition
import com.google.mlkit.vision.text.japanese.JapaneseTextRecognizerOptions
import tachiyomi.domain.ocr.model.ImageInput
import tachiyomi.domain.ocr.model.Rectangle
import tachiyomi.domain.ocr.model.TextBlock
import tachiyomi.domain.ocr.service.OCRService
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class GoogleMlKitImpl: OCRService {
    override suspend fun getTextBlocks(image: ImageInput): List<TextBlock> {
        return suspendCoroutine { continuation ->
            val bitmap = BitmapFactory.decodeByteArray(image.data, 0, image.data.size)
            val inputImage = InputImage.fromBitmap(bitmap, 0)
            val textRecognizer = TextRecognition.getClient(JapaneseTextRecognizerOptions.Builder().build())
            textRecognizer.process(inputImage)
                .addOnSuccessListener { result ->
                    val blocks = result.textBlocks.filter { it.boundingBox != null }.map {
                        TextBlock(
                            text = it.text,
                            boundingBox = it.boundingBox!!.toDomain()
                        )
                    }
                    continuation.resume(blocks)
                }
                .addOnFailureListener { e ->
                    continuation.resumeWith(Result.failure(e))
                }
        }
    }

    private fun Rect.toDomain(): Rectangle {
        return Rectangle(
            left = this.left,
            top = this.top,
            right = this.right,
            bottom = this.bottom
        )
    }
}
