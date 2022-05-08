package pro.lj.roomer.data

import android.graphics.Bitmap
import java.io.Serializable


data class Item(
        val name : String = "NULL",
        val stars : Float = 0.0f,
        val imageUri: String = "NULL",
        val price: String = "0",
        val modelUri : String = "NULL"
) : Serializable




