package up.ddm

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import up.ddm.data.PersonagemDAO

fun saveCharacter(personagem: Personagem, personagemDao: PersonagemDAO, coroutineScope: CoroutineScope) {
    coroutineScope.launch {
        personagemDao.insert(personagem)
    }
}