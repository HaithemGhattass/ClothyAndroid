package com.clothy.clothyandroid.adapters

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Color
import android.graphics.Typeface
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.clothy.clothyandroid.MyApplication
import com.clothy.clothyandroid.R
import com.clothy.clothyandroid.outfit_detail
import com.clothy.clothyandroid.services.OutfitResponse
import com.clothy.clothyandroid.services.RetrofitClient
import org.w3c.dom.Text

class ProductAdapter (private val outfitlist:List<OutfitResponse.Outfit>):  ListAdapter<OutfitResponse.Outfit, ProductAdapter.ViewHolder>(DiffCallback) {

    companion object DiffCallback : DiffUtil.ItemCallback<OutfitResponse.Outfit>() {
        override fun areItemsTheSame(a: OutfitResponse.Outfit, b: OutfitResponse.Outfit)    = a.idd == b.idd
        @SuppressLint("DiffUtilEquals")
        override fun areContentsTheSame(a: OutfitResponse.Outfit, b: OutfitResponse.Outfit) = a == b

    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val root = view
        val imgPhoto     : ImageView = view.findViewById(R.id.imgProduct)
        val txtName      : TextView = view.findViewById(R.id.txtProductName)
        val txtPrice     : TextView = view.findViewById(R.id.txtProductPrice)
        val outfitcolor : ImageView = view.findViewById(R.id.outfit_color)
//        val txtQuantity  : TextView = view.findViewById(R.id.txtQuantity)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater
            .from(parent.context)
            .inflate(R.layout.item_product, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val product = outfitlist[position]

//        holder.txtId.text   = friend.id
        holder.txtName.text = product.type
        holder.txtName.setTypeface(holder.txtName.typeface, Typeface.BOLD_ITALIC)
        holder.txtName.setTextColor(Color.parseColor("#333333"));
        holder.txtPrice.text  = product.taille
        holder.txtPrice.setTextColor(Color.parseColor("#333333"));
        holder.outfitcolor.setBackgroundColor(Color.parseColor(product.couleur))

//        holder.txtQuantity.text = product.productQuan.toString()

        // TODO: Photo (blob to bitmap)a
        Glide.with(MyApplication.getInstance())
            .load(RetrofitClient().BASE_URLL+product.photo)
            .transform(CenterCrop(), RoundedCorners(50))
            .into(holder.imgPhoto)
        holder.itemView.setOnClickListener {
            val intent = Intent(holder.itemView.context, outfit_detail::class.java)
            intent.putExtra("ITEM_ID", product.idd.toString())
            Log.e("type",product.idd.toString())

            holder.itemView.context.startActivity(intent)
        }


    }
}