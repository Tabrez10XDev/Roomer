package pro.lj.roomer.ui.adapters

import android.animation.AnimatorInflater
import android.animation.AnimatorSet
import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.animation.doOnEnd
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.firebase.storage.FirebaseStorage
import pro.lj.roomer.R
import pro.lj.roomer.data.Item
import pro.lj.roomer.databinding.ItemProductCardBinding


class MeasureAdapter :RecyclerView.Adapter<MeasureAdapter.ItemViewHolder>() {

    private var onItemLongClickListener : ((View, String) -> Unit) ?= null

    fun setOnItemLongClickListener(listener: (View, String) -> Unit){
        onItemLongClickListener = listener
    }


    private var onItemClickListener : ((Item) -> Unit) ?= null

    fun setOnItemClickListener(listener: (Item) -> Unit){
        onItemClickListener = listener
    }

    inner class ItemViewHolder(val binding: ItemProductCardBinding): RecyclerView.ViewHolder(binding.root)

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
        val binding = ItemProductCardBinding.inflate(LayoutInflater.from(parent.context), parent, false)
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

            //
          //  var visibleView: View = binding.card
         //   var inVisibleView: View = binding.txt


//            itemView.setOnLongClickListener {
//                onItemLongClickListener?.let { it(binding.cardView, "hey") }
//                true
//            }



            itemView.setOnClickListener {
//                flipCard(it.context,inVisibleView,visibleView)
//                val temp = visibleView
//                visibleView = inVisibleView
//                inVisibleView = temp
                onItemClickListener?.let { it(item) }
            }



            binding.apply {
                tvItemName.text = item.name
                tvItemPrice.text = "₹"+item.price
                ratingBar.numStars = item.stars.toInt()
//                tvName.text = item.name
//                tvPrice.text = "₹" + item.price.toString()
//                ratingBar.rating = item.stars
            }
                    Glide
                        .with(itemView.context)
                        .load(item.imageUri)
                        .into(binding.ivItem)



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