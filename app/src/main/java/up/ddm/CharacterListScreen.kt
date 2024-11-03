package up.ddm

import androidx.activity.result.launch
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
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import up.ddm.data.PersonagemDAO

@Composable
fun CharacterListScreen(personagemDao: PersonagemDAO) {
    val characters by personagemDao.getAllPersonagens().collectAsState(initial = emptyList())
    val coroutineScope = rememberCoroutineScope()

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        items(characters) { character ->
            CharacterListItem(character, personagemDao, coroutineScope) {
                coroutineScope.launch {
                    personagemDao.delete(character)
                }
            }
            Spacer(modifier = Modifier.height(8.dp))
        }
    }
}

@Composable
fun CharacterListItem(
    character: Personagem,
    personagemDao: PersonagemDAO,
    coroutineScope: CoroutineScope,
    onDelete: () -> Unit
) {
    var forca by remember { mutableStateOf(character.forcaFinal.toString()) }
    var destreza by remember { mutableStateOf(character.destrezaFinal.toString()) }
    var constituicao by remember { mutableStateOf(character.constituicaoFinal.toString()) }
    var inteligencia by remember { mutableStateOf(character.inteligenciaFinal.toString()) }
    var sabedoria by remember { mutableStateOf(character.sabedoriaFinal.toString()) }
    var carisma by remember { mutableStateOf(character.carismaFinal.toString()) }

    Column(modifier = Modifier.fillMaxWidth()) {
        Text("Raça: ${character.raca.name}", modifier = Modifier.padding(bottom = 4.dp))

        AttributeRow("Força", forca) { newValue ->
            coroutineScope.launch {
                forca = newValue
                updateCharacterAttribute(character, personagemDao, "forca", newValue.toIntOrNull() ?: 0)
            }
        }
        AttributeRow("Destreza", destreza) { newValue ->
            coroutineScope.launch {
                destreza = newValue
                updateCharacterAttribute(character, personagemDao, "destreza", newValue.toIntOrNull() ?: 0)
            }
        }
        AttributeRow("Constituição", constituicao) { newValue ->
            coroutineScope.launch {
                constituicao = newValue
                updateCharacterAttribute(character, personagemDao, "constituicao", newValue.toIntOrNull() ?: 0)
            }
        }
        AttributeRow("Inteligência", inteligencia) { newValue ->
            coroutineScope.launch {
                inteligencia = newValue
                updateCharacterAttribute(character, personagemDao, "inteligencia", newValue.toIntOrNull() ?: 0)
            }
        }
        AttributeRow("Sabedoria", sabedoria) { newValue ->
            coroutineScope.launch {
                sabedoria = newValue
                updateCharacterAttribute(character, personagemDao, "sabedoria", newValue.toIntOrNull() ?: 0)
            }
        }
        AttributeRow("Carisma", carisma) { newValue ->
            coroutineScope.launch {
                carisma = newValue
                updateCharacterAttribute(character, personagemDao, "carisma", newValue.toIntOrNull() ?: 0)
            }
        }
        AttributeRow("Vida", character.vida.toString()) { } // Vida is read-only

        // Add a delete button
        Button(onClick = onDelete, modifier = Modifier.padding(top = 8.dp)) {
            Text("Delete")
        }
    }
}

@Composable
fun AttributeRow(label: String, value: String, onValueChange: (String) -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text("$label:")
        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            modifier = Modifier.width(60.dp)
        )
    }
}

suspend fun updateCharacterAttribute(
    character: Personagem,
    personagemDao: PersonagemDAO,
    attributeName: String,
    newValue: Int
) {
    val updatedCharacter = character.copy(
        forcaFinal = if (attributeName == "forca") newValue else character.forcaFinal,
        destrezaFinal = if (attributeName == "destreza") newValue else character.destrezaFinal,
        constituicaoFinal = if (attributeName == "constituicao") newValue else character.constituicaoFinal,
        inteligenciaFinal = if (attributeName == "inteligencia") newValue else character.inteligenciaFinal,
        sabedoriaFinal = if (attributeName == "sabedoria") newValue else character.sabedoriaFinal,
        carismaFinal = if (attributeName == "carisma") newValue else character.carismaFinal
    )
    personagemDao.update(updatedCharacter)
}