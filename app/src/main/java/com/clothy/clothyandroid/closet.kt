package com.clothy.clothyandroid

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.core.os.bundleOf
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.clothy.clothyandroid.adapters.MessageAdapter
import com.clothy.clothyandroid.adapters.ProductAdapter
import com.clothy.clothyandroid.services.OutfitResponse
import com.clothy.clothyandroid.services.OutfitService
import com.clothy.clothyandroid.services.RetrofitClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.ArrayList

class Closet : AppCompatActivity() {
    var outfitlist = ArrayList<OutfitResponse.Outfit>()
    private lateinit var adapter: ProductAdapter
    private lateinit var recyclerView: RecyclerView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_closet)
        recyclerView = findViewById(R.id.rv)
        recyclerView.layoutManager = GridLayoutManager(this, 2)
        adapter= ProductAdapter(outfitlist)
        recyclerView.adapter = adapter
        val retro = RetrofitClient().getInstance().create(OutfitService::class.java)
        retro.getUserOutfit().enqueue(object : Callback<List<OutfitResponse.Outfit>> {
            override fun onResponse(
                call: Call<List<OutfitResponse.Outfit>>,
                response: Response<List<OutfitResponse.Outfit>>
            ) {
                if (response.isSuccessful) {
                    val outfit = response.body()

                    //  user?.get(0)?.type?.let { Log.e("type", it) }
                    if (outfit != null) {
                        outfitlist.clear() // Clear the existing list


                            outfitlist.addAll(outfit)
                            println(outfit)

                            setupRecyclerViewAdapter()
                            adapter.notifyDataSetChanged()

                    }
                }
                else{
                    Log.e("Error", "error")
                }
            }

            override fun onFailure(call: Call<List<OutfitResponse.Outfit>>, t: Throwable) {
                // Handle failure
            }
        })
        recyclerView = findViewById(R.id.rv)
        outfitlist = ArrayList()
        val layoutParams = recyclerView.layoutParams
        recyclerView.layoutParams = layoutParams
        recyclerView.layoutManager = GridLayoutManager(this, 2)
        adapter = ProductAdapter(outfitlist)
        recyclerView.adapter = adapter
        adapter.submitList(outfitlist)
    }
    private fun setupRecyclerViewAdapter() {
        recyclerView = findViewById(R.id.rv)
        recyclerView.layoutManager = GridLayoutManager(this, 2)
        adapter = ProductAdapter(outfitlist)
        recyclerView.adapter = adapter
        adapter.submitList(outfitlist)

    }

}
