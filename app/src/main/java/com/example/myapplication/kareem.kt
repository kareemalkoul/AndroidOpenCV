package com.example.myapplication

import android.graphics.*
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageProxy
import org.opencv.android.Utils
import org.opencv.core.Mat
import org.opencv.imgproc.Imgproc
import org.opencv.aruco.Aruco
import java.io.ByteArrayOutputStream


class PaletteAnalyzer(private val onColorChange: (Bitmap) -> Unit) : ImageAnalysis.Analyzer {


    override fun analyze(imageProxy: ImageProxy) {
        imageProxy.convertImageProxyToBitmap()?.let {
            var mat: Mat = Mat()
            Utils.bitmapToMat(it, mat)
            mat = aruco(mat)
            val bit: Bitmap = Bitmap.createBitmap(it)
            Utils.matToBitmap(mat, bit)

            onColorChange.invoke(bit)
        }
        imageProxy.close()
    }
}

//based on the code from https://stackoverflow.com/a/56812799
fun ImageProxy.convertImageProxyToBitmap(): Bitmap? {
    val yBuffer = planes[0].buffer // Y
    val vuBuffer = planes[2].buffer // VU

    val ySize = yBuffer.remaining()
    val vuSize = vuBuffer.remaining()

    val nv21 = ByteArray(ySize + vuSize)

    yBuffer.get(nv21, 0, ySize)
    vuBuffer.get(nv21, ySize, vuSize)

    val yuvImage = YuvImage(nv21, ImageFormat.NV21, this.width, this.height, null)
    val out = ByteArrayOutputStream()
    yuvImage.compressToJpeg(Rect(0, 0, yuvImage.width, yuvImage.height), 50, out)
    val imageBytes = out.toByteArray()
    return BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.size)
}


fun aruco(mat: Mat): Mat {
    val gray: Mat = mat.clone()
    Imgproc.cvtColor(mat, mat, Imgproc.COLOR_RGBA2RGB)

    Imgproc.cvtColor(gray, gray, Imgproc.COLOR_BGR2GRAY)
    val key = Aruco.DICT_4X4_100
    val dict = Aruco.getPredefinedDictionary(key)
    val conerns: List<Mat> = ArrayList<Mat>();
    val marker_IDs: Mat = Mat();
    Aruco.detectMarkers(gray, dict, conerns, marker_IDs);
    if (conerns.size != 0)
        Aruco.drawDetectedMarkers(mat, conerns)
    return mat;
}