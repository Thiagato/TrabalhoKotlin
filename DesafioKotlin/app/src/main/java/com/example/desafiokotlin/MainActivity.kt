package com.example.desafiokotlin

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.*
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.desafiokotlin.Models.Jogador
import com.example.desafiokotlin.Controls.CustomTextField

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            NavHostApp()
        }
    }
}

class JogadorViewModel : ViewModel() {
    var jogadores by mutableStateOf(mutableListOf<Jogador>())
        private set

    fun addJogador(nome: String, level: Double, bonusEquipamento: Double, modificadores: Double) {
        val poder = level + bonusEquipamento + modificadores
        jogadores.add(Jogador(nome, level, poder, bonusEquipamento, modificadores))
    }
}

@Composable
fun NavHostApp() {
    val navController = rememberNavController()
    val jogadorViewModel: JogadorViewModel = viewModel()

    NavHost(navController, startDestination = "cadastroJogador") {
        composable("cadastroJogador") {
            CadastroJogadorScreen(navController, jogadorViewModel)
        }
        composable("listaJogadores") {
            ListaJogadoresScreen(navController, jogadorViewModel)
        }
        composable("detalhesJogador/{index}") { backStackEntry ->
            val index = backStackEntry.arguments?.getString("index")?.toInt() ?: 0
            DetalhesJogadorScreen(index, navController, jogadorViewModel)
        }
    }
}

@Composable
fun CadastroJogadorScreen(navController: NavController, jogadorViewModel: JogadorViewModel) {
    var nome by remember { mutableStateOf("") }
    var level by remember { mutableStateOf(0.0) }
    var bonusEquipamento by remember { mutableStateOf(0.0) }
    var modificadores by remember { mutableStateOf(0.0) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {
        CustomTextField(valor = nome, valorMudar = { nome = it }, label = "Nome do jogador")

        Row {
            Button(onClick = { if (level > 0) level-- }) { Text(text = "-") }
            Text(text = "Level $level")
            Button(onClick = { if (level < 10) level++ }) { Text(text = "+") }
        }

        Row {
            Button(onClick = { if (bonusEquipamento > 0) bonusEquipamento-- }) { Text(text = "-") }
            Text(text = "Equipamento $bonusEquipamento")
            Button(onClick = { if (bonusEquipamento < 40) bonusEquipamento++ }) { Text(text = "+") }
        }

        Row {
            Button(onClick = { if (modificadores > -5) modificadores-- }) { Text(text = "-") }
            Text(text = "Modificadores $modificadores")
            Button(onClick = { if (modificadores < 10) modificadores++ }) { Text(text = "+") }
        }

        Button(onClick = {
            if (jogadorViewModel.jogadores.size < 6) {
                jogadorViewModel.addJogador(nome, level, bonusEquipamento, modificadores)
                nome = ""
                level = 0.0
                bonusEquipamento = 0.0
                modificadores = 0.0
            }
        }) {
            Text(text = "Salvar")
        }

        Button(onClick = { navController.navigate("listaJogadores") }) {
            Text(text = "Ver Jogadores")
        }
    }
}

@Composable
fun ListaJogadoresScreen(navController: NavController, jogadorViewModel: JogadorViewModel) {
    LazyColumn {
        items(jogadorViewModel.jogadores) { jogador ->
            Button(onClick = { navController.navigate("detalhesJogador/${jogadorViewModel.jogadores.indexOf(jogador)}") }) {
                Text(
                    text = "${jogador.nome} - Poder: ${jogador.poder}",
                    modifier = Modifier.padding(8.dp)
                )
            }
        }
    }
}

@Composable
fun DetalhesJogadorScreen(index: Int, navController: NavController, jogadorViewModel: JogadorViewModel) {
    val jogador = jogadorViewModel.jogadores[index]

    var nome by remember { mutableStateOf(jogador.nome) }
    var level by remember { mutableStateOf(jogador.level) }
    var bonusEquipamento by remember { mutableStateOf(jogador.bonusEquipamento) }
    var modificadores by remember { mutableStateOf(jogador.modificadores) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Top
    ) {
        CustomTextField(valor = nome, valorMudar = { nome = it }, label = "Nome do jogador")

        val poder = level + bonusEquipamento + modificadores

        Text(text = "Poder $poder")

        Row {
            Button(onClick = { if (level > 0) level-- }) { Text(text = "-") }
            Text(text = "Level $level")
            Button(onClick = { if (level < 10) level++ }) { Text(text = "+") }
        }

        Row {
            Button(onClick = { if (bonusEquipamento > 0) bonusEquipamento-- }) { Text(text = "-") }
            Text(text = "Equipamento $bonusEquipamento")
            Button(onClick = { if (bonusEquipamento < 40) bonusEquipamento++ }) { Text(text = "+") }
        }

        Row {
            Button(onClick = { if (modificadores > -5) modificadores-- }) { Text(text = "-") }
            Text(text = "Modificadores $modificadores")
            Button(onClick = { if (modificadores < 10) modificadores++ }) { Text(text = "+") }
        }

        Button(onClick = {
            jogadorViewModel.jogadores[index] = Jogador(nome, level, poder, bonusEquipamento, modificadores)
            navController.popBackStack()
        }) {
            Text(text = "Salvar")
        }
    }
}

@Preview(showBackground = true)
@Composable
fun previewPrincipal() {
    NavHostApp()
}
