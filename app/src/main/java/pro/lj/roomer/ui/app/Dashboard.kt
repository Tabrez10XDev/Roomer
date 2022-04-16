package pro.lj.roomer.ui.app

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import pro.lj.roomer.R
import pro.lj.roomer.databinding.DashboardBinding


class Dashboard : AppCompatActivity() {
    private lateinit var binding: DashboardBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DashboardBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        binding.bottomNavBar.setupWithNavController(findNavController(R.id.fragment2))

        binding.bottomNavBar.itemIconTintList = null
//    blur1.setBlur(3)
//        blur2.setBlur(3)
//        val intent = Intent(this, Home::class.java)
//        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK
//        startActivity(intent)
    }
}