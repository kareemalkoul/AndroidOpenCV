package com.example.myapplication

import android.graphics.Bitmap
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.myapplication.ui.theme.MyApplicationTheme
import org.opencv.android.OpenCVLoader

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (!OpenCVLoader.initDebug())
            Log.e("OpenCV", "Unable to load OpenCV!");
        else
            Log.d("OpenCV", "OpenCV loaded Successfully!");
        setContent {
            MyApplicationTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    Greeting("kareem")
                }
            }
        }
    }
}


@Composable
fun Greeting(name: String) {
    var bit: Bitmap by remember {
        mutableStateOf<Bitmap>(
            Bitmap.createBitmap(
                10,
                20,
                Bitmap.Config.ARGB_8888
            )
        )
    }
    Column {
        Text(text = "kareem")
        Box(modifier = Modifier.size(1.dp)) {
            SimpleCameraPreview(PaletteAnalyzer { bitmap -> bit = bitmap })
        }
//        Box(modifier = Modifier.filleMaxSize()) {
        BitmapImage(bit)
//        }

    }

}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    MyApplicationTheme {
        Greeting("Android")
    }
}

@Composable
fun camera() {


}

@Composable
fun BitmapImage(bitmap: Bitmap) {

    println("aaa0" + (bitmap?.height ?: null))

    Image(
        bitmap = bitmap.asImageBitmap(),
        contentDescription = "some useful description",
        contentScale = ContentScale.Fit,
        modifier = Modifier
            .fillMaxSize()
            .rotate(90F)
    )

}
