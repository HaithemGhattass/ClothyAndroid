package com.clothy.clothyandroid

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.clothy.clothyandroid.home.HomeScreen
import com.clothy.clothyandroid.onBoarding.SignUpScreen
import com.clothy.clothyandroid.onBoarding.onBoardingScreen
@SuppressLint("SuspiciousIndentation")
@Composable
fun MainScreen() {
    val context = LocalContext.current

        context.startActivity(Intent(context, Camera::class.java))


}


@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun RootScreen() {
    val navigationcontroller = rememberNavController()
    var shouldShowlogin by rememberSaveable { mutableStateOf(true) }
    Scaffold(
        bottomBar = {
            if(!shouldShowlogin){
                BottomBar(navigationcontroller)

            }
        }
    ){
        val context = LocalContext.current
        val sharedPreferences = context.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)

        val email = sharedPreferences?.getString("email", "")

        if(email != ""){
            shouldShowlogin = false
        }
       NavHost(navController = navigationcontroller , startDestination = if (shouldShowlogin) {
           NavigationItem.Login.route} else {NavigationItem.Home.route} ){
           composable(NavigationItem.Login.route){

               onBoardingScreen(navigateAction = {
                   navigationcontroller.navigate(NavigationItem.Home.route)
                   shouldShowlogin = false
                   println("taadiiiit")
               }, navController = navigationcontroller)
           }
           composable(NavigationItem.Home.route){
               HomeScreen()
           }
           composable(NavigationItem.Signup.route){
               SignUpScreen( navController = navigationcontroller)
           }

           composable(NavigationItem.Trade.route){
               MyComposable()
           }
           composable(NavigationItem.Profile.route){
               AccountScreen()
           }
           composable(NavigationItem.Add.route){
               MainScreen()
           }
           composable(NavigationItem.forgitpwd.route){
               ForgotPasswordScreen()
           }

           composable(NavigationItem.Chat.route){
               Match()
           }


       }
    }
}
sealed class NavigationItem(var route: String ,var icon: ImageVector, var title: String){
    object Signup : NavigationItem("signup", Icons.Filled.Home, "Signup")
    object forgitpwd : NavigationItem("changepwd", Icons.Filled.Home, "Changepwd")
    object Login : NavigationItem("login", Icons.Filled.Home, "Home")
    object Home : NavigationItem("home", Icons.Filled.Home, "Home")
    object Chat : NavigationItem("chat", Icons.Filled.Chat, "Chat")
    object Add : NavigationItem("add", Icons.Filled.Add, "Add")
    object Trade : NavigationItem("trade", Icons.Filled.CurrencyExchange, "Trade")
    object Profile : NavigationItem("profile", Icons.Filled.Person, "Profile")


}

@Composable
fun BottomBar(navController: NavController){
    val items = listOf(
        NavigationItem.Home,
        NavigationItem.Chat,
        NavigationItem.Add,
        NavigationItem.Trade,
        NavigationItem.Profile
    )
    BottomNavigation(
        backgroundColor = Color(33,17,52),
        contentColor = Color.White
    ) {
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentRoute = navBackStackEntry?.destination?.route
        items.forEach { item ->
            BottomNavigationItem(
                icon = { Image(
                    imageVector = item.icon,
                    contentDescription = item.title,
                    modifier = Modifier.size(30.dp),
                    colorFilter = ColorFilter.tint(
                        Color.White
                    )
                )},
                selectedContentColor = Color.White,
                unselectedContentColor = Color.White.copy(0.4f),
                alwaysShowLabel = false,
                selected = currentRoute == item.route,
                onClick = { navController.navigate(item.route){
                    navController.graph.startDestinationRoute?.let {route ->
                        popUpTo(route ){
                            saveState = true
                        }

                    }
                    launchSingleTop = true
                    restoreState = true
                } }
            )
                
            }

    }
}