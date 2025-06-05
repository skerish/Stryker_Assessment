package com.example.strykerassignmentapp.ui.navigation

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.strykerassignmentapp.feature.SearchScreenViewModel
import com.example.strykerassignmentapp.network.NetworkViewModel
import com.example.strykerassignmentapp.ui.search.QuestionDetailScreen
import com.example.strykerassignmentapp.ui.search.SearchScreen
import com.example.strykerassignmentapp.network.NetworkState
import com.example.strykerassignmentapp.utils.toFormattedDate
import kotlinx.coroutines.delay
import org.koin.androidx.compose.koinViewModel

@Composable()
fun Navigation(){
    val navController = rememberNavController()

    val context = LocalContext.current
    val searchViewModel: SearchScreenViewModel = koinViewModel()
    val networkViewModel: NetworkViewModel = koinViewModel()

    LaunchedEffect(networkViewModel.bannerState) {
        if (networkViewModel.bannerState == NetworkState.Connected){
            delay(300L)
            searchViewModel.retryOnNetworkReconnect()
        }
    }

    Scaffold(
        topBar = {
            when (networkViewModel.bannerState) {
                NetworkState.Connected -> Banner("Connected", Color.Green)
                NetworkState.Disconnected -> Banner("No internet connection", Color.Red)
                else -> {}
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = "search",
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(route = "search"){
                SearchScreen(
                    viewModel = searchViewModel,
                    onItemClick = { question ->
                        val titleEncoded = Uri.encode(question.title)
                        val authorEncoded = Uri.encode(question.owner.displayName)
                        val dateEncoded = Uri.encode(question.creationDate.toFormattedDate())
                        val linkEncoded = Uri.encode(question.link)
                        navController.navigate(
                            "questionDetails?title=$titleEncoded&author=$authorEncoded&askedOnDate=$dateEncoded&link=$linkEncoded"
                        )
                    },
                    modifier = Modifier.fillMaxSize()
                )
            }

            composable(
                route = "questionDetails?title={title}&author={author}&askedOnDate={askedOnDate}&link={link}",
                arguments = listOf(
                    navArgument("title") { type = NavType.StringType },
                    navArgument("author") { type = NavType.StringType },
                    navArgument("askedOnDate") { type = NavType.StringType },
                    navArgument("link") { type = NavType.StringType }
                )
            ){
                val args = it.arguments!!
                val title = args.getString("title")!!
                val author = args.getString("author")!!
                val askedOnDate = args.getString("askedOnDate")!!
                val link = args.getString("link")!!

                QuestionDetailScreen(
                    title = title,
                    author = author,
                    askedOnDate = askedOnDate,
                    onOpenInBrowser = {
                        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(link))
                        context.startActivity(intent)
                    },
                    onBackPressed = { navController.popBackStack() },
                    modifier = Modifier.fillMaxSize()
                )
            }
        }
    }
}

@Composable
fun Banner(message: String, background: Color) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(background)
            .padding(8.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(text = message, color = Color.White)
    }
}