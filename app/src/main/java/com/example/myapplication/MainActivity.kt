package com.example.myapplication

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.myapplication.navigation.NavGraph
import com.example.myapplication.screens.Product
import com.example.myapplication.theme.MyApplicationTheme
import com.example.myapplication.viewmodel.CartViewModel
import com.example.myapplication.viewmodel.UserViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            MyApplicationTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    // Instanciamos ViewModels (AndroidViewModel)
                    val userViewModel: UserViewModel = viewModel()
                    val cartViewModel: CartViewModel = viewModel()

                    // Lista de productos con URI de recursos como String
                    val productsList = listOf(
                        Product(
                            1,
                            "Polera Y2K",
                            12000.0,
                            "android.resource://${packageName}/drawable/producto1"
                        ),
                        Product(
                            2,
                            "Polerón Y2KP",
                            23990.0,
                            "android.resource://${packageName}/drawable/producto2"
                        ),
                        Product(
                            3,
                            "Pantalón Y2K",
                            32000.0,
                            "android.resource://${packageName}/drawable/producto3"
                        ),
                        Product(
                            4,
                            "Gorro Reflectante",
                            9990.0,
                            "android.resource://${packageName}/drawable/producto4"
                        ),
                        Product(
                            5,
                            "Pantalón 2 Y2K",
                            14990.0,
                            "android.resource://${packageName}/drawable/producto5"
                        ),
                        Product(
                            6,
                            "Pantalón 3 Y2K",
                            45990.0,
                            "android.resource://${packageName}/drawable/producto6"
                        ),
                        Product(
                            7,
                            "Pantalón 4 Y2K",
                            59990.0,
                            "android.resource://${packageName}/drawable/producto7"
                        )
                    )

                    // Llamamos a NavGraph pasando ViewModels y lista de productos
                    NavGraph(
                        userViewModel = userViewModel,
                        cartViewModel = cartViewModel,
                        productsList = productsList
                    )
                }
            }
        }
    }
}
