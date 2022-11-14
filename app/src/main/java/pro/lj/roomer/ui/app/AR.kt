package pro.lj.roomer.ui.app

import android.app.Activity
import android.app.ActivityManager
import android.content.Context
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.PersistableBundle
import android.util.Log
import android.view.MotionEvent
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.ar.core.Anchor
import com.google.ar.core.HitResult
import com.google.ar.core.Plane
import com.google.ar.sceneform.AnchorNode
import com.google.ar.sceneform.rendering.ModelRenderable
import com.google.ar.sceneform.rendering.Renderable
import com.google.ar.sceneform.ux.ArFragment
import com.google.ar.sceneform.ux.TransformableNode
import com.gorisse.thomas.sceneform.light.LightEstimationConfig
import com.gorisse.thomas.sceneform.lightEstimationConfig
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import pro.lj.roomer.R
import pro.lj.roomer.data.Item
import pro.lj.roomer.databinding.ArScreenBinding

class AR : AppCompatActivity() {
    private lateinit var binding: ArScreenBinding
    private var model: Renderable? = null
    private lateinit var modelUri: String
    private lateinit var arFragment: ArFragment
    private val arSceneView get() = arFragment.arSceneView


    private val scene get() = arSceneView.scene

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ArScreenBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        val intent = this.intent
        val bundle = intent.getBundleExtra("item")
        val item: Item = bundle?.getSerializable("item") as Item
        if (!checkIsSupportedDeviceOrFinish(this)) {
            return;
        }




        arFragment = (supportFragmentManager.findFragmentById(R.id.ux_fragment) as ArFragment).apply {
            setOnSessionConfigurationListener { session, config ->
                instructionsController = null
            }

            setOnTapArPlaneListener(::onTapPlane)
        }


        CoroutineScope(Dispatchers.Main).launch {

            val ids = "https://firebasestorage.googleapis.com/v0/b/roomer-ca0e9.appspot.com/o/sofa.glb?alt=media&token=24488821-c4c4-4ff2-a622-6803f681c0f5"
            modelUri = item.modelUri
            loadModels(item)
        }

    }

    private fun loadModels(tree: Item) {
        try{
            ModelRenderable.builder()
                .setSource(applicationContext, Uri.parse(tree.modelUri))
                .setIsFilamentGltf(true)
                .build()
                .thenAccept {
                    model = it
                }

        }
        catch (e:Exception){
            //Toast.makeText(this,e.localizedMessage.toString(),Toast.LENGTH_SHORT).show()
        }

    }

    private fun onTapPlane(hitResult: HitResult, plane: Plane, motionEvent: MotionEvent) {
//        if(scene.children.size >= 1){
//            return
//        }
        if(modelUri == "") {
            Toast.makeText(applicationContext, "AR Coming soon...", Toast.LENGTH_SHORT).show()
            return
        }
        if (model == null) {
            Toast.makeText(applicationContext, "Loading...", Toast.LENGTH_SHORT).show()
            return
        }

        // Create the Anchor.
        arSceneView.lightEstimationConfig = LightEstimationConfig.AMBIENT_INTENSITY

        scene.addChild(AnchorNode(hitResult.createAnchor()).apply {
            // Create the transformable model and add it to the anchor.
            val transformableNode = TransformableNode(arFragment.transformationSystem)
            transformableNode.scaleController.sensitivity = 0f
            addChild(
                transformableNode
                    .apply {
                renderable = model
                renderableInstance?.animate(true)?.start()
            })
        })
    }


    fun checkIsSupportedDeviceOrFinish(activity: Activity): Boolean {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N) {
            Log.e("TAG", "Sceneform requires Android N or later")
            Toast.makeText(activity, "Sceneform requires Android N or later", Toast.LENGTH_LONG)
                .show()
            activity.finish()
            return false
        }
        val openGlVersionString =
            (activity.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager)
                .deviceConfigurationInfo
                .glEsVersion

        return true
    }
}