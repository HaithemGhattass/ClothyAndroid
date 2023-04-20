package com.clothy.clothyandroid.entities

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.clothy.clothyandroid.R
import com.clothy.clothyandroid.services.MatchResponse
import com.clothy.clothyandroid.services.MatchService
import com.clothy.clothyandroid.services.OutfitResponse.Outfit
import com.clothy.clothyandroid.services.OutfitService
import com.clothy.clothyandroid.services.RetrofitClient
import com.mindorks.placeholderview.SwipePlaceHolderView
import com.mindorks.placeholderview.annotations.Layout
import com.mindorks.placeholderview.annotations.Resolve
import com.mindorks.placeholderview.annotations.View
import com.mindorks.placeholderview.annotations.swipe.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

@SuppressLint("NonConstantResourceId")
@Layout(R.layout.adapter_tinder_card)
class TinderCard(
    private val mContext: Context,
    private val mProfile: Outfit,
    private val mSwipeView: SwipePlaceHolderView
) {
    @SuppressLint("NonConstantResourceId")
    @View(R.id.profileImageView)
    private val profileImageView: ImageView? = null

    @SuppressLint("NonConstantResourceId")
    @View(R.id.nameAgeTxt)
    private val nameAgeTxt: TextView? = null

    @SuppressLint("NonConstantResourceId")
    @View(R.id.locationNameTxt)
    private val locationNameTxt: TextView? = null
    @SuppressLint("SetTextI18n")
    @Resolve
    private fun onResolved() {
        Glide.with(mContext).load(mProfile.photo).into(profileImageView!!)
        nameAgeTxt!!.text = mProfile.type + " " + mProfile.taille
        locationNameTxt!!.text = mProfile.category.toString()
    }

    @SwipeOut
    private fun onSwipedOut() {
        Log.d("EVENT", "onSwipedOut")
        mSwipeView.addView(this)
    }

    @SwipeCancelState
    private fun onSwipeCancelState() {
        Log.d("EVENT", "onSwipeCancelState")
    }

    @SwipeIn
    private fun onSwipeIn() {
        Log.d("EVENT", "onSwipedIn")
        Log.e("match",mProfile.userID.toString())

        val idReciver = mProfile.userID.toString()
        match(idReciver)



    }

    @SwipeInState
    private fun onSwipeInState() {
        Log.d("EVENT", "onSwipeInState")
    }

    @SwipeOutState
    private fun onSwipeOutState() {
        Log.d("EVENT", "onSwipeOutState")
    }
    private fun match (IdReciver:String) {
        val retro = RetrofitClient().getInstance().create(MatchService::class.java)
        retro.match(IdReciver).enqueue(object : Callback<MatchResponse> {
            override fun onResponse(call: Call<MatchResponse>, response: Response<MatchResponse>) {

                if (response.isSuccessful) {
                    val user = response.body()
                    if (user != null) {
                        Log.e("match", user.match?.Etat.toString())
                        Log.e("match", user.match?.IdSession.toString())

                    }

                } else {
                    Log.e("Error", "error")
                }
            }

            override fun onFailure(call: Call<MatchResponse>, t: Throwable) {
                // Handle failure
            }
        })
    }
}