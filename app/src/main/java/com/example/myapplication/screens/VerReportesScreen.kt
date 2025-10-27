package com.example.myapplication.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.myapplication.viewmodel.CartViewModel

@Composable
fun VerReportesScreen(navController: NavController, cartViewModel: CartViewModel) {
    val reports by cartViewModel.getReports().collectAsState(initial = listOf<String>())

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Text("Reportes de Compras", style = MaterialTheme.typography.headlineMedium)
        Spacer(modifier = Modifier.height(16.dp))

        LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            items(reports) { report ->
                Card(modifier = Modifier.fillMaxWidth().padding(4.dp)) {
                    Text(report, modifier = Modifier.padding(8.dp))
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = { navController.navigateUp() }) {
            Text("Volver")
        }
    }
}
