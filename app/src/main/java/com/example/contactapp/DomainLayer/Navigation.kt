package com.example.contactapp.DomainLayer

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.contactapp.PresentationLayer.View.AddContactScreen
import com.example.contactapp.PresentationLayer.View.ContactListScreen

@Composable
fun ContactNavGraph(navController: NavHostController = rememberNavController()) {
    NavHost(navController = navController, startDestination = "contact_list") {
        composable("contact_list") { ContactListScreen(navController) }

        composable(
            route = "add_contact/{contactId}?",
            arguments = listOf(navArgument("contactId") {
                type = NavType.IntType
                defaultValue = -1
            })
        ) { backStackEntry ->
            val contactId = backStackEntry.arguments?.getInt("contactId") ?: -1
            AddContactScreen(navController, contactId)
        }
    }
}