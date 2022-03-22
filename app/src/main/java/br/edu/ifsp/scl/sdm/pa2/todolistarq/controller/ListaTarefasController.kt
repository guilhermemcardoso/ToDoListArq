package br.edu.ifsp.scl.sdm.pa2.todolistarq.controller

import androidx.room.Room
import br.edu.ifsp.scl.sdm.pa2.todolistarq.model.database.ToDoListArqDatabase
import br.edu.ifsp.scl.sdm.pa2.todolistarq.model.entity.Tarefa
import br.edu.ifsp.scl.sdm.pa2.todolistarq.view.ListaTarefasFragment
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ListaTarefasController(private val listaTarefasFragment: ListaTarefasFragment) {
    private val database: ToDoListArqDatabase
    init {
        database = Room.databaseBuilder(listaTarefasFragment.requireContext(),
            ToDoListArqDatabase::class.java,
            ToDoListArqDatabase.Constantes.DB_NAME
        ).build()
    }

    private val escopoCorrotinas = CoroutineScope(Dispatchers.IO)

    fun buscarTarefas() {
        escopoCorrotinas.launch {
            val tarefas = database.getTarefaDao().recuperarTarefas()
            val listaTarefas = mutableListOf<Tarefa>()
            tarefas.forEach { tarefa ->
                listaTarefas.add(tarefa)
            }
            listaTarefasFragment.atualizarListaTarefas(listaTarefas)
        }
    }

    fun removerTarefa(tarefa: Tarefa) {
        escopoCorrotinas.launch {
            database.getTarefaDao().removerTarefa(tarefa)
        }
    }
}