package br.edu.ifsp.scl.sdm.pa2.todolistarq.viewmodel

import android.app.Application
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.room.Room
import br.edu.ifsp.scl.sdm.pa2.todolistarq.model.database.ToDoListArqDatabase
import br.edu.ifsp.scl.sdm.pa2.todolistarq.model.entity.Tarefa
import br.edu.ifsp.scl.sdm.pa2.todolistarq.service.TarefaService
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch


class TarefaViewModel(application: Application): AndroidViewModel(application) {

    companion object {
        val ACTION_INSERIR_TAREFA = "ACTION_INSERIR_TAREFA"
        val ACTION_ATUALIZAR_TAREFA = "ACTION_ATUALIZAR_TAREFA"
        val ACTION_BUSCAR_TAREFAS = "ACTION_BUSCAR_TAREFAS"
        val ACTION_REMOVER_TAREFA = "ACTION_REMOVER_TAREFA"

        val EXTRA_INSERIR_TAREFA = "EXTRA_INSERIR_TAREFA"
        val EXTRA_ATUALIZAR_TAREFA = "EXTRA_ATUALIZAR_TAREFA"
        val EXTRA_REMOVER_TAREFA = "EXTRA_REMOVER_TAREFA"
    }

    private val listaTarefasMld: MutableLiveData<MutableList<Tarefa>> = MutableLiveData()
    private val tarefaMld: MutableLiveData<Tarefa> = MutableLiveData()

    // Funções para recuperar os observáveis
    fun recuperarListaTarefas() = listaTarefasMld
    fun recuperarTarefa() = tarefaMld

    // Funções de acesso ao data source
    fun atualizarTarefa(tarefa: Tarefa) {
        val tarefaServiceIntent = Intent(ACTION_ATUALIZAR_TAREFA, Uri.EMPTY, getApplication(), TarefaService::class.java).also {
            it.putExtra(EXTRA_ATUALIZAR_TAREFA, tarefa)
        }
        getApplication<Application>().startService(tarefaServiceIntent)
    }

    fun inserirTarefa(tarefa: Tarefa) {
        val tarefaServiceIntent = Intent(ACTION_INSERIR_TAREFA, Uri.EMPTY, getApplication(), TarefaService::class.java).also {
            it.putExtra(EXTRA_INSERIR_TAREFA, tarefa)
        }
        getApplication<Application>().startService(tarefaServiceIntent)
    }

    fun buscarTarefas() {
        val tarefaServiceIntent = Intent(ACTION_BUSCAR_TAREFAS, Uri.EMPTY, getApplication(), TarefaService::class.java)
        getApplication<Application>().startService(tarefaServiceIntent)
    }

    fun removerTarefa(tarefa: Tarefa) {
        val tarefaServiceIntent = Intent(ACTION_REMOVER_TAREFA, Uri.EMPTY, getApplication(), TarefaService::class.java).also {
            it.putExtra(EXTRA_REMOVER_TAREFA, tarefa)
        }
        getApplication<Application>().startService(tarefaServiceIntent)
    }
}