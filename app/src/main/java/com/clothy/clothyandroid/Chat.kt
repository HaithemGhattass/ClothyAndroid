package com.clothy.clothyandroid

import android.annotation.SuppressLint
import android.graphics.PorterDuff
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.SearchView
import androidx.compose.runtime.Composable
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.clothy.clothyandroid.adapters.ProductAdapter
import com.clothy.clothyandroid.services.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


private  var adapter: ProductAdapter? = null
private lateinit var recyclerView: RecyclerView
@SuppressLint("StaticFieldLeak")
private lateinit var editSearch: SearchView
private var outfitlist = mutableListOf<OutfitResponse.Outfit>()
private var originalList = mutableListOf<OutfitResponse.Outfit>()
private var filteredList = mutableListOf<OutfitResponse.Outfit>()
@Composable
fun Homee() {

    AndroidView(
        factory = { context ->
            val view = LayoutInflater.from(context).inflate(R.layout.activity_closet, null)
            recyclerView = view.findViewById(R.id.rv)
           editSearch = view.findViewById(R.id.edtSearch)
            val retro = RetrofitClient().getInstance().create(OutfitService::class.java)
            retro.getUserOutfit().enqueue(object : Callback<List<OutfitResponse.Outfit>> {
                override fun onResponse(
                    call: Call<List<OutfitResponse.Outfit>>,
                    response: Response<List<OutfitResponse.Outfit>>
                ) {
                    if (response.isSuccessful) {
                        val outfit = response.body()
                        if (outfit != null) {
                            outfitlist.clear()
                            outfitlist.addAll(outfit)
                            Log.e("list", outfitlist.toString())
                            originalList.clear()
                            originalList =
                                outfitlist.toList() as MutableList<OutfitResponse.Outfit> //
                            setupRecyclerViewAdapter()

                            adapter?.notifyDataSetChanged()

                        }
                    } else {
                        Log.e("Error", "error")
                    }
                }

                override fun onFailure(call: Call<List<OutfitResponse.Outfit>>, t: Throwable) {
                    // Handle failure
                }
            })

            editSearch.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(name: String): Boolean = true

                override fun onQueryTextChange(name: String): Boolean {
                    val filteredList = originalList.filter { outfit ->
                        outfit.type!!.contains(name, ignoreCase = true)
                    }
                    adapter?.submitList(filteredList)
                    return true
                }
            })
            view
        },
        update = { view ->
            // Update the view here if necessary
        }
    )
}

fun setupRecyclerViewAdapter() {

    val mlayoutManager = GridLayoutManager(MyApplication.getInstance(), 2)
    recyclerView.layoutManager = mlayoutManager
    recyclerView.addItemDecoration(DividerItemDecoration(MyApplication.getInstance(), DividerItemDecoration.VERTICAL))
    adapter = ProductAdapter(outfitlist)
    recyclerView.adapter = adapter
    adapter?.submitList(outfitlist)
}
private fun searchOutfits(query: String) {
    filteredList.clear()
    if (query.isEmpty()) {
        filteredList.addAll(originalList)
    } else {
        for (outfit in originalList) {
            if (outfit.name.toLowerCase().contains(query.toLowerCase())) {
                filteredList.add(outfit)
            }
        }
    }
    adapter?.submitList(filteredList)
}