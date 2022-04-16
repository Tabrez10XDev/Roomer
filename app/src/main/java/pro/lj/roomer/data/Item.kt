package pro.lj.roomer.data

import android.net.Uri

data class Item(
        val name : String = "NULL",
        val stars : Float = 0.0f,
        val imageUri: String = "NULL",
        val price: Float = 0.0f,
        val modelUri : String = "NULL"
)
