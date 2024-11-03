package up.ddm

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Snackbar
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch
import up.ddm.data.AtributosDB
import up.ddm.data.PersonagemDAO


class MainActivity : ComponentActivity() {
    private lateinit var personagemDao: PersonagemDAO

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val db = AtributosDB.getDatabase(applicationContext, lifecycleScope)
        personagemDao = db.personagemDAO()

        setContent {
            var currentScreen by remember { mutableStateOf("race_selection") }
            var selectedRace by remember { mutableStateOf<Raca?>(null) }
            var atributos by remember { mutableStateOf(Atributos()) }

            when (currentScreen) {
                "race_selection" -> {
                    RacaSelector { raca ->
                        selectedRace = raca
                        currentScreen = "attribute_selection"
                    }
                }
                "attribute_selection" -> {
                    AtributosScreen(
                        atributos = atributos,
                        racaSelecionada = selectedRace,
                        personagemDao = personagemDao,
                        onNavigateToCharacterList = { currentScreen = "character_screen" }
                    )
                }
                "character_screen" -> {
                    CharacterListScreen(personagemDao)
                }
            }
        }
    }
}



@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AtributosScreen(
    atributos: Atributos,
    racaSelecionada: Raca?,
    personagemDao: PersonagemDAO,
    onNavigateToCharacterList: () -> Unit
) {
    var pontosRestantes by remember { mutableStateOf(atributos.pontos) }
    var snackbarMessage by remember { mutableStateOf("") }
    var snackbarVisible by remember { mutableStateOf(false) }

    // Declare coroutineScope outside the onClick lambda
    val coroutineScope = rememberCoroutineScope()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // Título da página
        Text(
            text = "Criação de Personagem",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(16.dp))

        // Exibe os pontos restantes
        Text(
            text = "Pontos Restantes: $pontosRestantes",
            fontSize = 18.sp,
            fontWeight = FontWeight.Medium
        )
        Spacer(modifier = Modifier.height(16.dp))

        // Exibe a raça selecionada
        racaSelecionada?.let {
            Text(
                text = "Raça Selecionada: ${it.name}",
                fontSize = 18.sp,
                fontWeight = FontWeight.Medium
            )
        }
        Spacer(modifier = Modifier.height(16.dp))

        // Cabeçalhos da tabela
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 16.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(text = "Atributos", fontWeight = FontWeight.Bold, modifier = Modifier.weight(1f))
        }
        Spacer(modifier = Modifier.height(8.dp))

        // Inputs dos atributos
        AtributoInputRow("Força", atributos, { pontosRestantes = atributos.pontos }, snackbarMessage, { snackbarMessage = it; snackbarVisible = true }, racaSelecionada)
        Spacer(modifier = Modifier.height(8.dp))
        AtributoInputRow("Destreza", atributos, { pontosRestantes = atributos.pontos }, snackbarMessage, { snackbarMessage = it; snackbarVisible = true }, racaSelecionada)
        Spacer(modifier = Modifier.height(8.dp))
        AtributoInputRow("Constituição", atributos, { pontosRestantes = atributos.pontos }, snackbarMessage, { snackbarMessage = it; snackbarVisible = true }, racaSelecionada)
        Spacer(modifier = Modifier.height(8.dp))
        AtributoInputRow("Sabedoria", atributos, { pontosRestantes = atributos.pontos }, snackbarMessage, { snackbarMessage = it; snackbarVisible = true }, racaSelecionada)
        Spacer(modifier = Modifier.height(8.dp))
        AtributoInputRow("Inteligência", atributos, { pontosRestantes = atributos.pontos }, snackbarMessage, { snackbarMessage = it; snackbarVisible = true }, racaSelecionada)
        Spacer(modifier = Modifier.height(8.dp))
        AtributoInputRow("Carisma", atributos, { pontosRestantes = atributos.pontos }, snackbarMessage, { snackbarMessage = it; snackbarVisible = true }, racaSelecionada)

        // Display health (vida)
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = "Vida: ${atributos.vida}",
            fontSize = 18.sp,
            fontWeight = FontWeight.Medium
        )

        // Botão "Salvar Personagem"
        Button(onClick = {
            val personagem = Personagem(
                forcaFinal = atributos.calcularAtributoFinal("Força"),
                destrezaFinal = atributos.calcularAtributoFinal("Destreza"),
                constituicaoFinal = atributos.calcularAtributoFinal("Constituição"),
                sabedoriaFinal = atributos.calcularAtributoFinal("Sabedoria"),
                inteligenciaFinal = atributos.calcularAtributoFinal("Inteligência"),
                carismaFinal = atributos.calcularAtributoFinal("Carisma"),
                vida = atributos.vida,
                raca = racaSelecionada!! // Assuming racaSelecionada is non-null here
            )
            saveCharacter(personagem, personagemDao, coroutineScope) // Pass coroutineScope here
        }) {
            Text("Salvar Personagem")
        }

        // Botão "Ver Personagens"
        Button(onClick = onNavigateToCharacterList) {
            Text("Ver Personagens")
        }

        // Snackbar para mensagens de erro
        if (snackbarVisible) {
            Snackbar(
                action = {
                    TextButton(onClick = { snackbarVisible = false }) {
                        Text("Fechar")
                    }
                }
            ) {
                Text(text = snackbarMessage)
            }
        }
    }
}

@Composable
fun RacaSelector(onRacaSelected: (Raca) -> Unit) {
    val racas = Raca.values()

    LazyColumn {
        items(racas.size) { index ->
            val raca = racas[index]
            Button(
                onClick = { onRacaSelected(raca) },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(4.dp)
            ) {
                Text(text = raca.name)
            }
        }
    }
}

@Composable
fun AtributoInputRow(
    label: String,
    atributos: Atributos,
    updatePontos: () -> Unit,
    snackbarMessage: String,
    onError: (String) -> Unit,
    racaSelecionada: Raca?
) {
    var textValue by remember { mutableStateOf("") }

    // Atribui o valor atual do atributo ao textValue quando a composable é composta
    LaunchedEffect(label) {
        textValue = when (label) {
            "Força" -> atributos.forca.toString()
            "Destreza" -> atributos.destreza.toString()
            "Constituição" -> atributos.constituicao.toString()
            "Sabedoria" -> atributos.sabedoria.toString()
            "Inteligência" -> atributos.inteligencia.toString()
            "Carisma" -> atributos.carisma.toString()
            else -> ""
        }
    }

    var valorFinal by remember { mutableStateOf(atributos.calcularAtributoFinal(label)) } // State for final value

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(text = label, modifier = Modifier.weight(1f))

        OutlinedTextField(
            value = textValue,
            onValueChange = { newValue ->
                if (newValue.all { it.isDigit() }) {
                    textValue = newValue
                }
            },
            modifier = Modifier
                .weight(1f)
                .width(50.dp)
                .onFocusChanged { focusState ->
                    if (!focusState.isFocused && textValue.isNotEmpty()) {
                        try {
                            val novoValor = textValue.toInt()
                            val valorAntigo = atributos.getAtributoValor(label)

                            // Validation logic
                            if (novoValor < 8 || novoValor > 15) {
                                onError("O valor do atributo deve estar entre 8 e 15.")
                                // Revert to the old value if invalid
                                textValue = valorAntigo.toString()
                                return@onFocusChanged // Exit the onFocusChanged handler
                            }

                            // Update points only if the new value is greater than the old value
                            if (novoValor > valorAntigo) {
                                val custoAntigo = atributos.calcularCusto(valorAntigo)
                                val custoNovo = atributos.calcularCusto(novoValor)
                                val diferencaDePontos = custoNovo - custoAntigo

                                if (atributos.pontos >= diferencaDePontos) {
                                    atributos.pontos -= diferencaDePontos
                                    atributos.setAtributo(label, novoValor)
                                    updatePontos()
                                } else {
                                    onError("Pontos insuficientes para definir esse atributo.")
                                    textValue = valorAntigo.toString()
                                }
                            } else if (novoValor < valorAntigo) {
                                // If the new value is lower, refund the points
                                val custoAntigo = atributos.calcularCusto(valorAntigo)
                                val custoNovo = atributos.calcularCusto(novoValor)
                                val diferencaDePontos = custoAntigo - custoNovo

                                atributos.pontos += diferencaDePontos
                                atributos.setAtributo(label, novoValor)
                                updatePontos()
                            }

                            // Update valorFinal to trigger recomposition
                            valorFinal = atributos.calcularAtributoFinal(label)
                        } catch (e: NumberFormatException) {
                            onError("Erro: O valor inserido não é um número válido.")
                        } catch (e: IllegalArgumentException) {
                            onError(e.message ?: "Valor inválido")
                            textValue = atributos.getAtributoValor(label).toString()
                        }
                    }
                }
        )

        // Calcula e exibe o bônus racial
        val bonus = racaSelecionada?.let { atributos.aplicarBonusRacial(it, label) } ?: 0

        // Exibe o bônus racial e o valor final
        Column(horizontalAlignment = Alignment.End) {
            Text(text = "Bônus: $bonus", modifier = Modifier.padding(start = 8.dp))
            Text(text = "Valor Final: $valorFinal", modifier = Modifier.padding(start = 8.dp)) // Use valorFinal state
        }
    }
}
