package br.edu.ifsp.scl.sdm.pa2.todolistarq.controller

import androidx.room.Room
import br.edu.ifsp.scl.sdm.pa2.todolistarq.model.database.ToDoListArqDatabase
import br.edu.ifsp.scl.sdm.pa2.todolistarq.model.entity.Tarefa
import br.edu.ifsp.scl.sdm.pa2.todolistarq.view.TarefaFragment
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class TarefaController(private val tarefaFragment: TarefaFragment) {
    private val database: ToDoListArqDatabase
    init {
        database = Room.databaseBuilder(tarefaFragment.requireContext(),
            ToDoListArqDatabase::class.java,
            ToDoListArqDatabase.Constantes.DB_NAME
        ).build()
    }

    fun atualizarTarefa(tarefa: Tarefa) {

        GlobalScope.launch {
            database.getTarefaDao().atualizarTarefa(tarefa)
            tarefaFragment.retornarTarefa(tarefa)
        }
    }

    fun inserirTarefa(tarefa: Tarefa) {
        GlobalScope.launch {
            val id = database.getTarefaDao().inserirTarefa(tarefa)
            tarefaFragment.retornarTarefa(
                Tarefa(id.toInt(),
                    tarefa.nome,
                    tarefa.realizada
                )
            )
        }
    }
}