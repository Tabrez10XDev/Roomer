package pro.lj.roomer.viewmodel

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Log
import android.view.View
import androidx.appcompat.widget.AppCompatImageView
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import pro.lj.roomer.data.Item
import pro.lj.roomer.data.User
import pro.lj.roomer.repositories.MainRepository
import pro.lj.roomer.util.Resource
import retrofit2.http.Url
import javax.inject.Inject

//@HiltViewModel
class MainViewModel(
    private val repository: MainRepository,
) : ViewModel() {

    private var auth: FirebaseAuth = FirebaseAuth.getInstance()
    private var fireStore : FirebaseFirestore = FirebaseFirestore.getInstance()
    private var storageRef : FirebaseStorage = FirebaseStorage.getInstance()


    val mapData : MutableLiveData<Resource<Url>> = MutableLiveData()
    private val _producutList : MutableLiveData<Resource<List<Item>>> = MutableLiveData()
    val producutList = _producutList

//    private val _filteredList : MutableLiveData<Resource<List<Item>>> = MutableLiveData()
//    val filteredList = _filteredList


    private val _user : MutableLiveData<Resource<User>> = MutableLiveData()
    val user = _user


    init {

        fetchProducts()
    }



    fun fetchProducts(){
        fireStore.collection("products")
                .get()
                .addOnSuccessListener { result ->
                    val itemList : MutableList<Item> = arrayListOf()
                    for (document in result) {

                        itemList.add(


                                document.toObject(Item::class.java)
                        )

                        _producutList.postValue(Resource.success(itemList))
//                        homeAdapter.differ.submitList(itemList)
                        Log.d("TABY", "${document.id} => ${document.data}")
                    }
                }
                .addOnFailureListener { exception ->
                    _producutList.postValue(Resource.error("Error getting documents", null))
                    Log.w("TABY", "Error getting documents.", exception)
                }
    }

    fun fetchUser(){
        fireStore.collection("users")
                .document(auth.uid!!)
                .get()
                .addOnSuccessListener { result ->
                    val doc =  result.toObject(User::class.java)
                        _user.postValue(Resource.success(doc))
//                        homeAdapter.differ.submitList(itemList)
                        Log.d("TABY", "${result.id} => ${result.data}")

                }
                .addOnFailureListener { exception ->
                    _user.postValue(Resource.error("Error getting document", null))
                    Log.w("TABY", "Error getting documents.", exception)
                }
    }

//    private fun insertMapData(healthData: Resource<HealthResponse>){
//        when(healthData.status){
//            Status.LOADING -> {
//                // NO OP
//            }
//            Status.SUCCESS ->{
//                Log.d("LJ",healthData.data.toString())
//                healthData.data?.let { insertHealthDataIntoDb(it.data) }
//            }
//            Status.ERROR -> {
//                Log.d("LJ","Error")
//
//                // TODO Show Error message
//            }
//        }
//    }





    fun fetchMapData(){
        viewModelScope.launch {
            mapData.postValue(Resource.loading(null))
            try {
                val response = repository.fetchMapData()
                mapData.postValue(response)
                Log.d("LJ","hiiiikkkkkkkk"+response.data)

            }
            catch (e : Exception){
                Log.d("LJ","hiiii"+e.localizedMessage)
                mapData.postValue(Resource.error(e.localizedMessage,null))
                //TODO
            }

        }
    }

    fun fetchImage(imageUri : String, view : AppCompatImageView){
        val httpsRef = storageRef.getReferenceFromUrl(imageUri)
        val ONE_MEGABYTE: Long = 1024 * 1024
        httpsRef.getBytes(ONE_MEGABYTE).addOnSuccessListener {
            val bmp = BitmapFactory.decodeByteArray(it, 0, it.size)
            view.setImageBitmap(Bitmap.createScaledBitmap(bmp, view.width, view.height, false));
        }.addOnFailureListener {
            Log.d("TABY",it.localizedMessage)
        }
    }


}