package com.clothy.clothyandroid

import android.content.Context
import android.graphics.Point
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.clothy.clothyandroid.entities.TinderCard
import com.clothy.clothyandroid.services.ApiService
import com.clothy.clothyandroid.services.RetrofitClient
import com.clothy.clothyandroid.services.UserResponse
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.mindorks.placeholderview.SwipeDecor
import com.mindorks.placeholderview.SwipePlaceHolderView
import com.mindorks.placeholderview.SwipeViewBuilder
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.Request
import org.json.JSONArray
import org.json.JSONException
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.IOException

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [TradeFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class TradeFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var rootLayout: View? = null
    lateinit var mSwipeView: SwipePlaceHolderView
    private var mContext: Context? = null

    fun SwipeViewFragment() {
        // Required empty public constructor
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        // Inflate the layout for this fragment
        rootLayout = inflater.inflate(R.layout.fragment_swipe_view, container, false)

        return rootLayout
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mSwipeView = view.findViewById<SwipePlaceHolderView>(R.id.swipeView)
        mContext = activity
        val bottomMargin: Int = Utils.dpToPx(100)
        val windowSize: Point = Utils.getDisplaySize(requireActivity().windowManager)
        mSwipeView.getBuilder<SwipePlaceHolderView, SwipeViewBuilder<SwipePlaceHolderView>>()
            .setDisplayViewCount(3)
            .setSwipeDecor(
                SwipeDecor()
                    .setViewWidth(windowSize.x)
                    .setViewHeight(windowSize.y - bottomMargin)
                    .setViewGravity(Gravity.TOP)
                    .setPaddingTop(20)
                    .setRelativeScale(0.01f)
                //    .setSwipeInMsgLayoutId(R.layout.tinder_swipe_in_msg_view)
                 //   .setSwipeOutMsgLayoutId(R.layout.tinder_swipe_out_msg_view)
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
                            mSwipeView.addView<Any>(TinderCard(mContext,userr , mSwipeView))
                            println(userr)
                        }
                    }
                    // Get the SharedPreferences object for your app

                }
                else{
                    Log.e("Error", "error")
                }
            }

            override fun onFailure(call: Call<List<UserResponse.User>>, t: Throwable) {
                TODO("Not yet implemented")
            }
        })
    }


    private fun animateFab(fab: FloatingActionButton) {
        fab.animate().scaleX(0.7f).setDuration(100).withEndAction {
            fab.animate().scaleX(1f).scaleY(1f)
        }
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment TradeFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            TradeFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}