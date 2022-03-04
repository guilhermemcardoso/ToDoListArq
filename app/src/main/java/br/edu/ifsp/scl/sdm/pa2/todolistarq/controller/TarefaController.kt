package br.edu.ifsp.scl.sdm.pa2.todolistarq.controller

import android.os.AsyncTask
import androidx.room.Room
import br.edu.ifsp.scl.sdm.pa2.todolistarq.model.database.ToDoListArqDatabase
import br.edu.ifsp.scl.sdm.pa2.todolistarq.model.entity.Tarefa
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
        object : AsyncTask<Unit, Unit, Long>() {
            override fun doInBackground(vararg p0: Unit?): Long {
                return database.getTarefaDao().inserirTarefa(tarefa)
            }

            override fun onPostExecute(result: Long?) {
                super.onPostExecute(result)
            }
        }.execute()

    }

    fun atualizarTarefa(tarefa: Tarefa) {
        object : AsyncTask<Unit, Unit, Unit>() {
            override fun doInBackground(vararg p0: Unit?): Unit {
                return database.getTarefaDao().atualizarTarefa(tarefa)
            }

            override fun onPostExecute(result: Unit?) {
                super.onPostExecute(result)
            }
        }.execute()

    }
}