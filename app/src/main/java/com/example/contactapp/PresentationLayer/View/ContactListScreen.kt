package com.example.contactapp.PresentationLayer.View

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.provider.Settings
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.contactapp.DataLayer.Contact
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun ContactListScreen(
    navController: NavController,
    viewModel: ContactViewModel = hiltViewModel()
) {
    val contacts by viewModel.contacts.collectAsState()
    val context = LocalContext.current
    var showPermissionDialog by remember { mutableStateOf(false) }
    var pendingPhoneNumber by remember { mutableStateOf<String?>(null) } // Stores number for call after permission is granted

    val callLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            // If permission granted & a phone number was stored, make the call
            pendingPhoneNumber?.let { number ->
                makePhoneCall(context, number)
                pendingPhoneNumber = null
            }
        } else {
            showPermissionDialog = true // Show permission dialog if denied
        }
    }

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = { navController.navigate("add_contact/-1") },
                containerColor = Color.Blue
            ) {
                Icon(Icons.Default.Add, contentDescription = "Add Contact")
            }
        }
    ) { padding ->
        LazyColumn(modifier = Modifier.padding(padding)) {
            items(contacts) { contact ->
                ContactCard(
                    contact = contact,

                    onCallClick = {
                        if (hasCallPermission(context)) {
                            makePhoneCall(context, contact.phoneNumber)
                        } else {
                            pendingPhoneNumber = contact.phoneNumber
                            callLauncher.launch(Manifest.permission.CALL_PHONE)
                        }
                    },
                    onDeleteClick = {
                        viewModel.deleteContact(contact)
                        Toast.makeText(
                            context,
                            "${contact.name} is deleted Successfully",
                            Toast.LENGTH_SHORT
                        ).show()
                    },
                    onEditClick = { navController.navigate("add_contact/${contact.id}") }
                )
            }
        }
    }

    if (showPermissionDialog) {
        PermissionDeniedDialog(
            onDismiss = { showPermissionDialog = false },
            onSettingsClick = {
                openAppSettings(context)
                showPermissionDialog = false
            }
        )
    }
}

@Composable
fun ContactCard(
    contact: Contact,
    onCallClick: () -> Unit,
    onDeleteClick: () -> Unit,
    onEditClick: () -> Unit
) {
    var isExpanded by remember { mutableStateOf(false) }

    Card(
        modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth()
            .clickable { isExpanded = !isExpanded }, // Toggle expand on click
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Text(
                text = contact.name,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                fontFamily = FontFamily.Monospace
            )
            Text(
                text = "Email: ${contact.email}",
                fontSize = 17.sp,
                fontFamily = FontFamily.Default
            )
            Text(
                text = "Phone: ${contact.phoneNumber}",
                fontSize = 17.sp,
                fontFamily = FontFamily.Default
            )
            Text(
                text = "Added On: ${
                    SimpleDateFormat(
                        "dd/MM/yyyy HH:mm",
                        Locale.getDefault()
                    ).format(Date(contact.timestamp))
                }",
                fontSize = 17.sp,
                fontFamily = FontFamily.Default
            )

            if (isExpanded) {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp, vertical = 10.dp),
                    elevation = CardDefaults.cardElevation(10.dp),
                    colors = CardDefaults.cardColors(MaterialTheme.colorScheme.primaryContainer),
                    shape = RoundedCornerShape(10.dp)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(5.dp),
                        horizontalArrangement = Arrangement.SpaceEvenly,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        IconButton(onClick = onCallClick) {
                            Icon(Icons.Default.Call, contentDescription = "Call")
                        }
                        IconButton(onClick = onDeleteClick) {
                            Icon(Icons.Default.Delete, contentDescription = "Delete")
                        }
                        IconButton(onClick = onEditClick) {
                            Icon(Icons.Default.Edit, contentDescription = "Edit")
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun PermissionDeniedDialog(onDismiss: () -> Unit, onSettingsClick: () -> Unit) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Permission Required") },
        text = { Text("This permission is needed to make phone calls. Please enable it in Settings.") },
        confirmButton = {
            Button(onClick = onSettingsClick) {
                Text("Go to Settings")
            }
        },
        dismissButton = {
            Button(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
}

fun makePhoneCall(context: Context, phoneNumber: String) {
    val callIntent = Intent(Intent.ACTION_CALL, Uri.parse("tel:$phoneNumber"))
    try {
        context.startActivity(callIntent)
    } catch (e: SecurityException) {
        Toast.makeText(context, "Permission not granted", Toast.LENGTH_SHORT).show()
    }
}

fun hasCallPermission(context: Context): Boolean {
    return PackageManager.PERMISSION_GRANTED ==
            context.checkSelfPermission(Manifest.permission.CALL_PHONE)
}

fun openAppSettings(context: Context) {
    val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
        addCategory(Intent.CATEGORY_DEFAULT)
        data = Uri.parse("package:${context.packageName}")
        flags = Intent.FLAG_ACTIVITY_NEW_TASK

//        Intent.setData = Uri.parse("package:${context.packageName}")
    }
    context.startActivity(intent)
}