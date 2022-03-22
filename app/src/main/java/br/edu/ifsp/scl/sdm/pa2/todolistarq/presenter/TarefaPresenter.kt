package br.edu.ifsp.scl.sdm.pa2.todolistarq.presenter

import androidx.fragment.app.Fragment
import androidx.room.Room
import br.edu.ifsp.scl.sdm.pa2.todolistarq.model.database.ToDoListArqDatabase
import br.edu.ifsp.scl.sdm.pa2.todolistarq.model.entity.Tarefa
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class TarefaPresenter(private val tarefaView: TarefaView) {

    private val database: ToDoListArqDatabase
    init {
        database = Room.databaseBuilder(
            (tarefaView as Fragment).requireContext(),
            ToDoListArqDatabase::class.java,
            ToDoListArqDatabase.Constantes.DB_NAME
        ).build()
    }

    fun atualizarTarefa(tarefa: Tarefa) {

        GlobalScope.launch {
            database.getTarefaDao().atualizarTarefa(tarefa)
            tarefaView.retornarTarefa(tarefa)
        }
    }

    fun inserirTarefa(tarefa: Tarefa) {
        GlobalScope.launch {
            val id = database.getTarefaDao().inserirTarefa(tarefa)
            tarefaView.retornarTarefa(
                Tarefa(id.toInt(),
                    tarefa.nome,
                    tarefa.realizada
                )
            )
        }
    }

    fun buscarTarefas() {
        GlobalScope.launch {
            val tarefas = database.getTarefaDao().recuperarTarefas()
            val listaTarefas = mutableListOf<Tarefa>()
            tarefas.forEach { tarefa ->
                listaTarefas.add(tarefa)
            }
            tarefaView.atualizarListaTarefas(listaTarefas)
        }
    }

    fun removerTarefa(tarefa: Tarefa) {
        GlobalScope.launch {
            database.getTarefaDao().removerTarefa(tarefa)
        }
    }

    interface TarefaView {
        fun atualizarListaTarefas(listaTarefas: MutableList<Tarefa>)
        fun retornarTarefa(tarefa: Tarefa)
    }
}