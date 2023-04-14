package com.clothy.clothyandroid

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Point
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.WindowManager
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import com.clothy.clothyandroid.entities.TinderCard
import com.clothy.clothyandroid.services.ApiService
import com.clothy.clothyandroid.services.RetrofitClient
import com.clothy.clothyandroid.services.UserResponse
import com.clothy.clothyandroid.ui.theme.ClothyAndroidTheme
import com.mindorks.placeholderview.SwipeDecor
import com.mindorks.placeholderview.SwipePlaceHolderView
import com.mindorks.placeholderview.SwipeViewBuilder
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


@Composable
fun MyComposable() {

    TradeFragmentContent(TradeFragment())

}
@Preview
@Composable
fun show() {
    ClothyAndroidTheme() {

        MyComposable()

    }
}

@SuppressLint("ServiceCast")
@Composable
fun TradeFragmentContent(fragment: TradeFragment) {
    val context = LocalContext.current
    val mSwipeView = remember { SwipePlaceHolderView(context) }
    val bottomMargin: Int = Utils.dpToPx(100)
    val windowSize: Point = Utils.getDisplaySize(context.getSystemService(Context.WINDOW_SERVICE) as WindowManager)
    mSwipeView.getBuilder<SwipePlaceHolderView, SwipeViewBuilder<SwipePlaceHolderView>>()
        .setDisplayViewCount(3)
        .setSwipeDecor(
            SwipeDecor()
                .setViewWidth(windowSize.x)
                .setViewHeight(windowSize.y - bottomMargin)
                .setViewGravity(Gravity.TOP)
                .setPaddingTop(20)
                .setRelativeScale(0.01f)
                .setSwipeInMsgLayoutId(R.layout.tinder_swipe_in_msg_view)
               .setSwipeOutMsgLayoutId(R.layout.tinder_swipe_out_msg_view)
        )

    val retro = RetrofitClient().getInstance().create(ApiService::class.java)
    retro.getUsers().enqueue(object : Callback<List<UserResponse.User>> {
        override fun onResponse(
            call: Call<List<UserResponse.User>>,
            response: Response<List<UserResponse.User>>
        ) {

            if (response.isSuccessful) {
                val user = response.body()

                user?.get(1)?.firstname?.let { Log.e("firstname", it) }
                if (user != null) {
                    for (userr in user) {
                        userr.firstname?.let { Log.e("firstname", it) }
                        mSwipeView.addView<Any>(TinderCard(context, userr , mSwipeView))
                        println(userr)
                    }
                }
            }
            else{
                Log.e("Error", "error")
            }
        }

        override fun onFailure(call: Call<List<UserResponse.User>>, t: Throwable) {
            // Handle failure
        }
    })

    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        SwipeView(mSwipeView = mSwipeView)
    }
}

@Composable
fun SwipeView(mSwipeView: SwipePlaceHolderView) {
    AndroidView(factory = { context ->
        mSwipeView.apply {
            layoutParams = ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
            )
        }
    })
}

