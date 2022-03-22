package br.edu.ifsp.scl.sdm.pa2.todolistarq.service

import android.app.Service
import android.content.Intent
import android.os.IBinder
import android.os.Parcelable
import android.util.Log
import androidx.room.Room
import br.edu.ifsp.scl.sdm.pa2.todolistarq.model.database.ToDoListArqDatabase
import br.edu.ifsp.scl.sdm.pa2.todolistarq.model.entity.Tarefa

class TarefaService : Service() {

    private lateinit var database: ToDoListArqDatabase


    companion object {
        val ACTION_INSERIR_TAREFA = "ACTION_INSERIR_TAREFA"
        val ACTION_ATUALIZAR_TAREFA = "ACTION_ATUALIZAR_TAREFA"
        val ACTION_BUSCAR_TAREFAS = "ACTION_BUSCAR_TAREFAS"
        val ACTION_REMOVER_TAREFA = "ACTION_REMOVER_TAREFA"

        val EXTRA_INSERIR_TAREFA = "EXTRA_INSERIR_TAREFA"
        val EXTRA_ATUALIZAR_TAREFA = "EXTRA_ATUALIZAR_TAREFA"
        val EXTRA_BUSCAR_TAREFAS = "EXTRA_BUSCAR_TAREFAS"
        val EXTRA_REMOVER_TAREFA = "EXTRA_REMOVER_TAREFA"
    }

    private inner class WorkerThread(private val intent: Intent): Thread() {
        override fun run() {
            val extras = intent.extras
            Log.d("TarefaService", intent.action.toString())
            when(intent.action) {
                ACTION_ATUALIZAR_TAREFA -> {
                    val tarefa = (extras?.get(EXTRA_ATUALIZAR_TAREFA) as Tarefa)
                    val retorno = atualizarTarefa(tarefa)
                    sendBroadcast(Intent(ACTION_ATUALIZAR_TAREFA).also {
                        it.putExtra(EXTRA_ATUALIZAR_TAREFA, retorno)
                    })
                }
                ACTION_INSERIR_TAREFA -> {
                    val tarefa = (extras?.get(EXTRA_INSERIR_TAREFA) as Tarefa)
                    val retorno = inserirTarefa(tarefa)
                    sendBroadcast(Intent(ACTION_INSERIR_TAREFA).also {
                        it.putExtra(EXTRA_INSERIR_TAREFA, retorno)
                    })
                }
                ACTION_REMOVER_TAREFA -> {
                    val tarefa = (extras?.get(EXTRA_REMOVER_TAREFA) as Tarefa)
                    removerTarefa(tarefa)
                    sendBroadcast(Intent(ACTION_REMOVER_TAREFA))
                }
                else -> {
                    val retorno = buscarTarefas()
                    sendBroadcast(Intent(ACTION_BUSCAR_TAREFAS).also {
                        it.putExtra(EXTRA_BUSCAR_TAREFAS, retorno as Array<out Parcelable>)
                    })
                }
            }

            onDestroy()
        }
    }

    fun atualizarTarefa(tarefa: Tarefa): Tarefa {
        database.getTarefaDao().atualizarTarefa(tarefa)
        return tarefa
    }

    fun inserirTarefa(tarefa: Tarefa): Tarefa {
        val id = database.getTarefaDao().inserirTarefa(tarefa)
        return Tarefa(id.toInt(),
                tarefa.nome,
                tarefa.realizada
            )
    }

    fun buscarTarefas(): Array<Tarefa> {
        val listaTarefas = database.getTarefaDao().recuperarTarefas()
        return listaTarefas.toTypedArray()
    }

    fun removerTarefa(tarefa: Tarefa) {
        database.getTarefaDao().removerTarefa(tarefa)
    }

    private lateinit var workerThread: WorkerThread

    override fun onBind(intent: Intent): IBinder? = null

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {


        database = Room.databaseBuilder(
            applicationContext,
            ToDoListArqDatabase::class.java,
            ToDoListArqDatabase.Constantes.DB_NAME
        ).build()

        workerThread = WorkerThread(intent!!)
        workerThread.start()

        return START_STICKY
    }

    override fun onDestroy() {
        super.onDestroy()
    }
}