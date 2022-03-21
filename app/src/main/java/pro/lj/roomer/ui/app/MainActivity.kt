package pro.lj.roomer.ui.app

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import pro.lj.roomer.R
import pro.lj.roomer.ui.fragments.Home


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.landing_page)
//    blur1.setBlur(3)
//        blur2.setBlur(3)
//        val intent = Intent(this, Home::class.java)
//        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK
//        startActivity(intent)
    }
}