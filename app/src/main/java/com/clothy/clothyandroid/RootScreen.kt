package com.clothy.clothyandroid
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.compose.animation.core.*
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.clothy.clothyandroid.onBoarding.SignUpScreen
import com.clothy.clothyandroid.onBoarding.onBoardingScreen
import kotlinx.coroutines.delay

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


// start the animation
    Scaffold(

        floatingActionButton = {
            if(!shouldShowlogin){
                var rotation by remember { mutableStateOf(0f) }

                LaunchedEffect(Unit) {
                    while (true) {
                        delay(16) // Update the rotation every 16ms (60fps)
                        rotation += 1f // Increment the rotation by 1 degree
                    }
                }


                FloatingActionButton(
                onClick = {

                    navigationcontroller.navigate(NavigationItem.Add.route)
                },
                   modifier = Modifier.rotate(rotation),
                shape = RoundedCornerShape(50),
                backgroundColor = Color.White
            ) {
                Image(
                    painter = painterResource(R.drawable.ic_add_a_photo_white_24dp),
                    contentDescription = "Add",
                    modifier = Modifier.size(24.dp),
                    colorFilter = ColorFilter.tint(Color.Black)
                )            }}
        },
        isFloatingActionButtonDocked = true,
        floatingActionButtonPosition = FabPosition.Center,

        bottomBar = {
            if(!shouldShowlogin){
                Box(modifier = Modifier.height(56.dp)) {
                    BottomBar(navigationcontroller)
                }
            }
        }
    ){innerPadding ->
        Box(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
        ) {
        val context = LocalContext.current
        val sharedPreferences = context.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)

        val email = sharedPreferences?.getString("email", "")

        if(email != ""){
            shouldShowlogin = false
        }
       NavHost(navController = navigationcontroller , startDestination = (if (shouldShowlogin) {
           NavigationItem.Login.route

       } else {NavigationItem.Home.route}) as String
       ){
           composable(NavigationItem.Login.route){

               onBoardingScreen(getVideoUri(), navigateAction = {
                   navigationcontroller.navigate(NavigationItem.Home.route)
                   shouldShowlogin = false
                   println("taadiiiit")
               }, navController = navigationcontroller)
           }
           composable(NavigationItem.Home.route){

               CategoryScreen(navController = navigationcontroller)
           }
           composable(NavigationItem.Signup.route){
               SignUpScreen(getVideoUri(), navController = navigationcontroller)
           }

           composable(NavigationItem.Trade.route){
               Homee()

           }
           composable(NavigationItem.Profile.route){
               AccountScreen()
           }
           composable(NavigationItem.Add.route){
               MainScreen()
           }
           composable(NavigationItem.forgitpwd.route){
               ForgotPasswordScreen(getVideoUri(),navController = navigationcontroller)
           }

           composable(NavigationItem.Chat.route){
               Match()
           }
           composable(
               "Tinder/{variable}",
               arguments = listOf(navArgument("variable") { type = NavType.StringType })
           ) { backStackEntry ->
               backStackEntry.arguments?.getString("variable")?.let { variable ->
                   MyComposable(variable = variable,navController = navigationcontroller)
               }
           }
       }


       }
        Spacer(modifier = Modifier.height(56.dp))
    }
    }

sealed class NavigationItem(var route: String ,var icon: ImageVector, var title: String){
    object Signup : NavigationItem("signup", Icons.Filled.Home, "Signup")
    object forgitpwd : NavigationItem("changepwd", Icons.Filled.Home, "Changepwd")
    object Login : NavigationItem("login", Icons.Filled.Home, "Home")
    object Home : NavigationItem("home", Icons.Filled.Home, "Home")
    object Chat : NavigationItem("chat", Icons.Filled.Chat, "Chat")
    object Add : NavigationItem("add", Icons.Filled.AddAPhoto, "Add")
    object Trade : NavigationItem("trade", Icons.Filled.Apartment, "Trade")
    object Profile : NavigationItem("profile", Icons.Filled.Person, "Profile")
    object Tinder : NavigationItem("Tinder/{variable}", Icons.Filled.Person, "Tinder")




}

@Composable

fun BottomBar(navController: NavController){

    val items = listOf(
        NavigationItem.Home,
        NavigationItem.Chat,
        NavigationItem.Trade,
        NavigationItem.Profile
    )
    BottomAppBar(
        cutoutShape = RoundedCornerShape(50),
        backgroundColor = Color.White,
        contentColor = Color.White
    ) {
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentRoute = navBackStackEntry?.destination?.route
        items.forEach { item ->
            BottomNavigationItem(
                icon = {
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier
                            .size(30.dp)
                            .background(if (currentRoute == item.route) Color.LightGray else Color.Transparent, CircleShape)
                    ) {
                        Icon(
                            imageVector = item.icon,
                            contentDescription = item.title,
                            tint = if (currentRoute == item.route) Color.White else Color.Black
                        )
                    }
                },
                selectedContentColor = Color.LightGray,
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


private fun getVideoUri(): Uri {
    val rawId = R.raw.clouds
    val videoUri = "android.resource://com.clothy.clothyandroid.onBoarding/$rawId"
    return Uri.parse(videoUri)
}

