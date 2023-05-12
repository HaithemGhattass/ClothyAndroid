package com.clothy.clothyandroid

import android.annotation.SuppressLint
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.clothy.clothyandroid.services.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class outfit_detail : AppCompatActivity() {
    private lateinit var type :TextView
    private lateinit var XS : Button
    private lateinit var S : Button
    private lateinit var M : Button
    private lateinit var L : Button
    private lateinit var XL : Button
    private lateinit var image : ImageView
    private lateinit var catg:TextView
    private lateinit var  color : ImageView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_outfit_detail)
        type= findViewById(R.id.txtProductDetailName)
        image =findViewById(R.id.imgProductDetail)
        catg =findViewById(R.id.txtProductDetailDescription)
        color =findViewById(R.id.product_color)
        XS = findViewById(R.id.btnSizeXS)
        S = findViewById(R.id.btnSizeS)
        M =findViewById(R.id.btnSizeM)
        L= findViewById(R.id.btnSizeL)
        XL = findViewById(R.id.btnSizeXL)
        val id = intent.getStringExtra("ITEM_ID")
        val retro = RetrofitClient().getInstance().create(OutfitService::class.java)

        Log.d("ItemId", id.toString())
        retro.getSelectedOutfit(id.toString()).enqueue(object : Callback<OutfitResponse.Outfit> {
            @SuppressLint("SetTextI18n")
            override fun onResponse(
                call: Call<OutfitResponse.Outfit>,
                response: Response<OutfitResponse.Outfit>
            ) {
                if (response.isSuccessful) {
                    val user = response.body()
                    // user?.get(0)?.Etat?.let { Log.e("type", it.toString()) }
                    if (user != null) {
                     Log.e("type",user.type.toString())
                        if(user.type.toString()=="shoes")
                        {
                            Log.e("taille",user.taille.toString())

                            M.setBackgroundColor(Color.rgb(225, 155, 155))
                            M.text = user.taille.toString()
                            XS.text = (user.taille!!.toInt() - 2).toString()
                            S.text = (user.taille!!.toInt() - 1).toString()
                            L.text = (user.taille!!.toInt() + 1).toString()
                            XL.text = (user.taille!!.toInt() + 2).toString()
                        }

                        if(user.taille.toString()=="XS")
                        {
                            XS.setBackgroundColor(Color.rgb(225, 155, 155))
                        }else if (user.taille.toString()=="S"){
                            S.setBackgroundColor(Color.rgb(225, 155, 155))
                        }else if (user.taille.toString()=="M"){
                            M.setBackgroundColor(Color.rgb(225, 155, 155))
                        }else if (user.taille.toString()=="L"){
                         L.setBackgroundColor(Color.rgb(225, 155, 155))
                        }else if (user.taille.toString()=="XL"){
                           XL.setBackgroundColor(Color.rgb(225, 155, 155))
                        }
                        val Clr = user.couleur.toString()
                        color.setBackgroundColor(Color.parseColor(Clr))
                        type.text = user.type.toString()
                        catg.text =user.category.toString()
                        Glide.with(MyApplication.getInstance())
                            .load(RetrofitClient().BASE_URLL+user.photo)
                            .into(image)

                        }
                    }
                }

            override fun onFailure(call: Call<OutfitResponse.Outfit>, t: Throwable) {
                // Handle failure
                Log.e("Error", "error")
            }
        })
    }

    fun goBack(view: View) {
        finish()
    }
}