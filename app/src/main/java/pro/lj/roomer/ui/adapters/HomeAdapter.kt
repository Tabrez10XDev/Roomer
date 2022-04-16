package pro.lj.roomer.ui.adapters

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.updateLayoutParams
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.storage.FirebaseStorage
import pro.lj.roomer.data.Item
import pro.lj.roomer.databinding.ItemPreviewBinding


class HomeAdapter :RecyclerView.Adapter<HomeAdapter.ItemViewHolder>() {

    private var onItemClickListener : ((View, String) -> Unit) ?= null

    fun setOnItemClickListener(listener: (View, String) -> Unit){
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
            if(position%2 == 0){
                binding.card.updateLayoutParams<ConstraintLayout.LayoutParams> { verticalBias = 0.0f }
            }
            else{
                binding.card.updateLayoutParams<ConstraintLayout.LayoutParams> { verticalBias = 1.0f }
            }
            itemView.setOnLongClickListener {
                onItemClickListener?.let { it(binding.cardView, "hey") }
                true
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

}