package pro.lj.roomer.ui.adapters

import android.animation.AnimatorInflater
import android.animation.AnimatorSet
import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Log
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.animation.doOnEnd
import androidx.core.view.updateLayoutParams
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.storage.FirebaseStorage
import pro.lj.roomer.R
import pro.lj.roomer.data.Item
import pro.lj.roomer.databinding.ItemPreviewBinding
import kotlin.coroutines.coroutineContext


class HomeAdapter :RecyclerView.Adapter<HomeAdapter.ItemViewHolder>() {

    private var onItemLongClickListener : ((View, String) -> Unit) ?= null

    fun setOnItemLongClickListener(listener: (View, String) -> Unit){
        onItemLongClickListener = listener
    }


    private var onItemClickListener : ((Item) -> Unit) ?= null

    fun setOnItemClickListener(listener: (Item) -> Unit){
        onItemClickListener = listener
    }

    inner class ItemViewHolder(val binding: ItemPreviewBinding): RecyclerView.ViewHolder(binding.root)

    private val differCallback = object : DiffUtil.ItemCallback<Item>(){
        override fun areItemsTheSame(oldItem: Item, newItem: Item): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: Item, newItem: Item): Boolean {
            return oldItem.name == newItem.name
        }

    }

    val differ = AsyncListDiffer(this, differCallback)


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val binding = ItemPreviewBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ItemViewHolder(binding)
    }

    override fun getItemCount(): Int {
//        return 2
        return differ.currentList.size
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        val item = differ.currentList[position]
        val storageRef = FirebaseStorage.getInstance()
        with(holder){
            var visibleView: View = binding.card
            var inVisibleView: View = binding.txt

            if(position%2 == 0){
                binding.card.updateLayoutParams<ConstraintLayout.LayoutParams> { verticalBias = 0.0f }
            }
            else{
                binding.card.updateLayoutParams<ConstraintLayout.LayoutParams> { verticalBias = 1.0f }
            }

            itemView.setOnLongClickListener {
                onItemLongClickListener?.let { it(binding.cardView, "hey") }
                true
            }



            itemView.setOnClickListener {
//                flipCard(it.context,inVisibleView,visibleView)
//                val temp = visibleView
//                visibleView = inVisibleView
//                inVisibleView = temp
                onItemClickListener?.let { it(item) }
            }
            binding.apply {
                tvName.text = item.name
                tvPrice.text = "₹" + item.price.toString()
                ratingBar.rating = item.stars
            }
            val httpsRef = storageRef.getReferenceFromUrl(item.imageUri)
            val ONE_MEGABYTE: Long = 1024 * 1024
            httpsRef.getBytes(ONE_MEGABYTE).addOnSuccessListener {
                val bmp = BitmapFactory.decodeByteArray(it, 0, it.size)
                binding.ivProduct.setImageBitmap(Bitmap.createScaledBitmap(bmp, binding.ivProduct.width, binding.ivProduct.height, false));
            }.addOnFailureListener {
                Log.d("TABY",it.localizedMessage)
            }
        }



}

    fun flipCard(context: Context, visibleView: View, inVisibleView: View) {
        try {
            visibleView.visibility = View.VISIBLE
            val flipOutAnimatorSet =
                    AnimatorInflater.loadAnimator(
                            context,
                            R.animator.flip_out
                    ) as AnimatorSet
            flipOutAnimatorSet.setTarget(inVisibleView)
            val flipInAnimationSet =
                    AnimatorInflater.loadAnimator(
                            context,
                            R.animator.flip_in
                    ) as AnimatorSet
            flipInAnimationSet.setTarget(visibleView)
            flipOutAnimatorSet.start()
            flipInAnimationSet.start()
            flipInAnimationSet.doOnEnd {
                inVisibleView.visibility = View.GONE
            }
        } catch (e: Exception) {
            // logHandledException(e)
            Log.d("LJS",e.localizedMessage)
        }
    }

}