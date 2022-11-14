package pro.lj.roomer.ui.app

import android.app.Activity
import android.app.ActivityManager
import android.app.AlertDialog
import android.content.Context
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.Constraints
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.core.view.updateLayoutParams
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.ar.core.Anchor
import com.google.ar.core.HitResult
import com.google.ar.core.Plane
import com.google.ar.core.Pose
import com.google.ar.sceneform.AnchorNode
import com.google.ar.sceneform.FrameTime
import com.google.ar.sceneform.Node
import com.google.ar.sceneform.Scene
import com.google.ar.sceneform.math.Vector3
import com.google.ar.sceneform.rendering.*
import com.google.ar.sceneform.ux.ArFragment
import com.google.ar.sceneform.ux.TransformableNode
import com.google.firebase.firestore.FirebaseFirestore
import pro.lj.roomer.R
import pro.lj.roomer.data.Item
import pro.lj.roomer.databinding.MeasureArScreenBinding
import pro.lj.roomer.ui.adapters.MeasureAdapter
import pro.lj.roomer.util.Resource
import kotlin.math.pow
import kotlin.math.sqrt

class MeasureAR : AppCompatActivity(), Scene.OnUpdateListener {



    private var fireStore : FirebaseFirestore = FirebaseFirestore.getInstance()
    var distanceMeterFinal: String ?= null
    private var cubeRenderable: ModelRenderable? = null
    private var distanceCardViewRenderable: ViewRenderable? = null
    private lateinit var measureAdapter: MeasureAdapter

    private val placedAnchors = ArrayList<Anchor>()
    private val placedAnchorNodes = ArrayList<AnchorNode>()
    private val midAnchors: MutableMap<String, Anchor> = mutableMapOf()
    private val midAnchorNodes: MutableMap<String, AnchorNode> = mutableMapOf()
    private val fromGroundNodes = ArrayList<List<Node>>()

    private lateinit var initCM: String


    //

    private lateinit var binding: MeasureArScreenBinding
    private lateinit var arFragment: ArFragment
    private val arSceneView get() = arFragment.arSceneView


    private val scene get() = arSceneView.scene

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = MeasureArScreenBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        if (!checkIsSupportedDeviceOrFinish(this)) {
            return;
        }



        initRenderable()

        arFragment = (supportFragmentManager.findFragmentById(R.id.measure_fragment) as ArFragment).apply {
            setOnSessionConfigurationListener { session, config ->
                instructionsController = null
            }



            setOnTapArPlaneListener(::onTapPlane)
        }

        setupMeasureRecyclerView()

        binding.btnSearch.setOnClickListener {
            binding.bottomSheet.updateLayoutParams<ConstraintLayout.LayoutParams> {
                height = 1300
            }

            binding.apply {
                tvRecommended.visibility = View.VISIBLE
                tvTip.visibility = View.VISIBLE
            }

            distanceMeterFinal?.let { fetchProductsUnder(it.toFloat()) }
        }



    }

    fun fetchProductsUnder(len: Float){
        fireStore.collection("products")
            .get()
            .addOnSuccessListener { result ->
                val itemList : MutableList<Item> = arrayListOf()
                for (document in result) {

                    val item = document.toObject(Item::class.java)
                    if(item.length <= len){
                        itemList.add(item)
                    }


                }
                measureAdapter.differ.submitList(itemList)
            }
            .addOnFailureListener { exception ->
                Toast.makeText(this,"Unknown Error",Toast.LENGTH_SHORT).show()
            }
    }

    private fun setupMeasureRecyclerView(){
        val horizontalManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        measureAdapter =
            MeasureAdapter()
        binding.measureRV.apply {
            adapter = measureAdapter
            layoutManager = horizontalManager
        }
    }

    private fun initRenderable() {
        MaterialFactory.makeTransparentWithColor(
            this,
            Color(Color.RED))
            .thenAccept { material: Material? ->
                cubeRenderable = ShapeFactory.makeSphere(
                    0.02f,
                    Vector3.zero(),
                    material)
                cubeRenderable!!.setShadowCaster(false)
                cubeRenderable!!.setShadowReceiver(false)
            }
            .exceptionally {
                val builder = AlertDialog.Builder(this)
                builder.setMessage(it.message).setTitle("Error")
                val dialog = builder.create()
                dialog.show()
                return@exceptionally null
            }




    }


    private fun clearAllAnchors(){
        placedAnchors.clear()
        for (anchorNode in placedAnchorNodes){
            arFragment!!.arSceneView.scene.removeChild(anchorNode)
            anchorNode.isEnabled = false
            anchorNode.anchor!!.detach()
            anchorNode.setParent(null)
        }
        placedAnchorNodes.clear()
        midAnchors.clear()
        for ((k,anchorNode) in midAnchorNodes){
            arFragment!!.arSceneView.scene.removeChild(anchorNode)
            anchorNode.isEnabled = false
            anchorNode.anchor!!.detach()
            anchorNode.setParent(null)
        }
        midAnchorNodes.clear()

        fromGroundNodes.clear()
    }

    private fun onTapPlane(hitResult: HitResult, plane: Plane, motionEvent: MotionEvent) {
        Log.d("Allan", cubeRenderable.toString() + " heyyy")

        if (cubeRenderable == null) return
                tapDistanceOf2Points(hitResult)
    }


    private fun tapDistanceOf2Points(hitResult: HitResult){
        if (placedAnchorNodes.size == 0){
            placeAnchor(hitResult, cubeRenderable!!)
        }
        else if (placedAnchorNodes.size == 1){
            placeAnchor(hitResult, cubeRenderable!!)

            val midPosition = floatArrayOf(
                (placedAnchorNodes[0].worldPosition.x + placedAnchorNodes[1].worldPosition.x) / 2,
                (placedAnchorNodes[0].worldPosition.y + placedAnchorNodes[1].worldPosition.y) / 2,
                (placedAnchorNodes[0].worldPosition.z + placedAnchorNodes[1].worldPosition.z) / 2)
            val quaternion = floatArrayOf(0.0f,0.0f,0.0f,0.0f)
            val pose = Pose(midPosition, quaternion)

            placeMidAnchor(pose, distanceCardViewRenderable!!)
        }
        else {
            clearAllAnchors()
            placeAnchor(hitResult, cubeRenderable!!)
        }
    }

    private fun placeMidAnchor(pose: Pose,
                               renderable: Renderable,
                               between: Array<Int> = arrayOf(0,1)){
        val midKey = "${between[0]}_${between[1]}"
        val anchor = arFragment!!.arSceneView.session!!.createAnchor(pose)
        midAnchors.put(midKey, anchor)

        val anchorNode = AnchorNode(anchor).apply {
            isSmoothed = true
            setParent(arFragment!!.arSceneView.scene)
        }
        midAnchorNodes.put(midKey, anchorNode)

        val node = TransformableNode(arFragment!!.transformationSystem)
            .apply{
                this.rotationController.isEnabled = false
                this.scaleController.isEnabled = false
                this.translationController.isEnabled = true
                this.renderable = renderable
                setParent(anchorNode)
            }
        arFragment!!.arSceneView.scene.addOnUpdateListener(this)
        arFragment!!.arSceneView.scene.addChild(anchorNode)
    }

    private fun placeAnchor(hitResult: HitResult,
                            renderable: Renderable){
        val anchor = hitResult.createAnchor()
        placedAnchors.add(anchor)

        val anchorNode = AnchorNode(anchor).apply {
            isSmoothed = true
            setParent(arFragment!!.arSceneView.scene)
        }
        placedAnchorNodes.add(anchorNode)

        val node = TransformableNode(arFragment!!.transformationSystem)
            .apply{
                this.rotationController.isEnabled = false
                this.scaleController.isEnabled = false
                this.translationController.isEnabled = true
                this.renderable = renderable
                setParent(anchorNode)
            }

        arFragment!!.arSceneView.scene.addOnUpdateListener(this)
        arFragment!!.arSceneView.scene.addChild(anchorNode)
        node.select()
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

    override fun onUpdate(frameTime: FrameTime?) {
        measureDistanceOf2Points()
    }

    private fun measureDistanceOf2Points(){
        if (placedAnchorNodes.size == 2) {
            val distanceMeter = calculateDistance(
                placedAnchorNodes[0].worldPosition,
                placedAnchorNodes[1].worldPosition)
            measureDistanceOf2Points(distanceMeter)
        }
    }

//    private fun changeUnit(distanceMeter: Float, unit: String): Float{
//        return when(unit){
//            "cm" -> distanceMeter * 100
//            "mm" -> distanceMeter * 1000
//            else -> distanceMeter
//        }
//    }
//
//    private fun makeDistanceTextWithCM(distanceMeter: Float): String{
//        val distanceCM = changeUnit(distanceMeter, "cm")
//        val distanceCMFloor = "%.2f".format(distanceCM)
//        return "${distanceCMFloor} cm"
//    }

    private fun measureDistanceOf2Points(distanceMeter: Float){
    //    val distanceTextCM = makeDistanceTextWithCM(distanceMeter)
        val distanceMFloor = "%.2f".format(distanceMeter)
        distanceMeterFinal = distanceMFloor
        binding.tvMeasure.text = distanceMFloor.toString() + "m"
    }

    private fun calculateDistance(x: Float, y: Float, z: Float): Float{
        return sqrt(x.pow(2) + y.pow(2) + z.pow(2))
    }



    private fun calculateDistance(objectPose0: Vector3, objectPose1: Vector3): Float{
        return calculateDistance(
            objectPose0.x - objectPose1.x,
            objectPose0.y - objectPose1.y,
            objectPose0.z - objectPose1.z
        )
    }
}
