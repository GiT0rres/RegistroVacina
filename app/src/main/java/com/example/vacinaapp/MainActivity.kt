package com.example.vacinaapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            val navController = rememberNavController()
            AppNavigation(navController)
        }
    }
}

@Composable
fun AppNavigation(navController: NavHostController) {

    NavHost(
        navController = navController,
        startDestination = "login"
    ) {

        composable("login") {
            LoginScreen(navController)
        }

        composable("cadastroUsuario") {
            CadastroUsuarioScreen(navController)
        }

        composable("home") {
            HomeScreen(navController)
        }

        composable(
            route = "cadastro/{vacinaId}",
            arguments = listOf(navArgument("vacinaId") { type = NavType.IntType })
        ) { backStackEntry ->
            val vacinaId = backStackEntry.arguments?.getInt("vacinaId") ?: -1
            CadastroVacinaScreen(navController = navController, vacinaId = vacinaId)
        }

        composable(
            route = "detalhes/{vacinaId}",
            arguments = listOf(navArgument("vacinaId") { type = NavType.IntType })
        ) { backStackEntry ->
            val vacinaId = backStackEntry.arguments?.getInt("vacinaId") ?: -1
            DetalhesVacinaScreen(navController = navController, vacinaId = vacinaId)
        }

        composable("perfil") {
            PerfilScreen(navController)
        }

        composable("calendario") {
            CalendarioScreen(navController)
        }
    }
}