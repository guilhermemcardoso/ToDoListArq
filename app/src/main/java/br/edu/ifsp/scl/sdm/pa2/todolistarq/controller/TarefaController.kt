package br.edu.ifsp.scl.sdm.pa2.todolistarq.controller

import android.os.AsyncTask
import androidx.room.Room
import br.edu.ifsp.scl.sdm.pa2.todolistarq.model.database.ToDoListArqDatabase
import br.edu.ifsp.scl.sdm.pa2.todolistarq.model.entity.Tarefa
import br.edu.ifsp.scl.sdm.pa2.todolistarq.view.BaseFragment.Constantes.ID_INEXISTENTE
import br.edu.ifsp.scl.sdm.pa2.todolistarq.view.TarefaFragment

class TarefaController(private val tarefaFragment: TarefaFragment) {
    private val database: ToDoListArqDatabase
    init {
        database = Room.databaseBuilder(tarefaFragment.requireContext(),
            ToDoListArqDatabase::class.java,
            ToDoListArqDatabase.Constantes.DB_NAME
        ).build()
    }

    fun inserirTarefa(tarefa: Tarefa) {
        object : AsyncTask<Tarefa, Unit, Long>() {
            override fun doInBackground(vararg params: Tarefa?): Long {
                params[0]?.let { novaTarefa ->
                    return database.getTarefaDao().inserirTarefa(novaTarefa)
                }
                return ID_INEXISTENTE
            }

            override fun onPostExecute(result: Long?) {
                super.onPostExecute(result)
                result?.let { novoId ->
                    tarefaFragment.retornaTarefa(
                        Tarefa(novoId.toInt(),
                        tarefa.nome,
                        tarefa.realizada
                        )
                    )
                }

            }
        }.execute()

    }

    fun atualizarTarefa(tarefa: Tarefa) {
        object : AsyncTask<Tarefa, Unit, Unit>() {
            override fun doInBackground(vararg params: Tarefa?) {
                params[0]?.let { tarefaEditada ->
                    database.getTarefaDao().atualizarTarefa(tarefa)
                }
            }

            override fun onPostExecute(result: Unit?) {
                super.onPostExecute(result)
                tarefaFragment.retornaTarefa(tarefa)
            }
        }.execute()

    }
}