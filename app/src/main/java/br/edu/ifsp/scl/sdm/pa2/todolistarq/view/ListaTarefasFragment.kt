package br.edu.ifsp.scl.sdm.pa2.todolistarq.view

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.commit
import androidx.fragment.app.setFragmentResultListener
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import br.edu.ifsp.scl.sdm.pa2.todolistarq.R
import br.edu.ifsp.scl.sdm.pa2.todolistarq.databinding.FragmentListaTarefasBinding
import br.edu.ifsp.scl.sdm.pa2.todolistarq.model.entity.Tarefa
import br.edu.ifsp.scl.sdm.pa2.todolistarq.view.BaseFragment.Constantes.ACAO_TAREFA_EXTRA
import br.edu.ifsp.scl.sdm.pa2.todolistarq.view.BaseFragment.Constantes.CONSULTA
import br.edu.ifsp.scl.sdm.pa2.todolistarq.view.BaseFragment.Constantes.TAREFA_EXTRA
import br.edu.ifsp.scl.sdm.pa2.todolistarq.view.BaseFragment.Constantes.TAREFA_REQUEST_KEY
import br.edu.ifsp.scl.sdm.pa2.todolistarq.view.adapter.OnTarefaClickListener
import br.edu.ifsp.scl.sdm.pa2.todolistarq.view.adapter.TarefasAdapter
import br.edu.ifsp.scl.sdm.pa2.todolistarq.viewmodel.TarefaViewModel

class ListaTarefasFragment: BaseFragment(), OnTarefaClickListener {
    private lateinit var fragmentListaTarefasBinding: FragmentListaTarefasBinding
    private lateinit var tarefasList: MutableList<Tarefa>
    private lateinit var tarefasAdapter: TarefasAdapter
    private lateinit var tarefaViewModel: TarefaViewModel

    companion object {
        val ACTION_BUSCAR_TAREFAS = "ACTION_BUSCAR_TAREFAS"

        val EXTRA_BUSCAR_TAREFAS = "EXTRA_BUSCAR_TAREFAS"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Instanciando ViewModel
        tarefaViewModel = ViewModelProvider
            .AndroidViewModelFactory(requireActivity().application)
            .create(TarefaViewModel::class.java)

        // Buscar tarefas no banco de dados
        tarefasList = mutableListOf()

        setFragmentResultListener(TAREFA_REQUEST_KEY) { chave, resultados ->
            val tarefaExtra = resultados.getParcelable<Tarefa>(TAREFA_EXTRA)
            // Adiciona ou atualiza uma tarefa da lista
            if (tarefaExtra != null) {
                tarefaViewModel.buscarTarefas()
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        fragmentListaTarefasBinding = FragmentListaTarefasBinding.inflate(inflater, container, false)

        tarefasAdapter = TarefasAdapter(this, tarefasList)
        val tarefasLayoutManager = LinearLayoutManager(activity)
        fragmentListaTarefasBinding.tarefasRv.adapter = tarefasAdapter
        fragmentListaTarefasBinding.tarefasRv.layoutManager = tarefasLayoutManager
        tarefaViewModel.buscarTarefas()

        return fragmentListaTarefasBinding.root
    }

    override fun onTarefaClick(posicao: Int) {
        // Abre TarefaFragment para consulta de tarefa
        val tarefa = tarefasList[posicao]
        abreTarefaFragment(tarefa, true)
    }

    override fun onContextItemSelected(item: MenuItem): Boolean {
        val posicao = tarefasAdapter.posicao
        return when (item.itemId) {
            R.id.editarTarefaMi -> {
                // Abre TarefaFragment para edi????o de tarefa
                abreTarefaFragment(tarefasList[posicao], false)
                true
            }
            R.id.removerTarefaMi -> {
                // Remove do banco de dados
                tarefaViewModel.removerTarefa(tarefasList[posicao])

                // Remove da lista de tarefas
                tarefasList.removeAt(posicao)
                tarefasAdapter.notifyDataSetChanged()
                true
            }
            else -> false
        }
    }

    private fun abreTarefaFragment(tarefa: Tarefa, consulta: Boolean) {
        // Preparando tarefa para enviar para o TarefaFragment
        val argumentos = Bundle().also { bundle ->
            bundle.putParcelable(TAREFA_EXTRA, tarefa)
            if (consulta) {
                bundle.putInt(ACAO_TAREFA_EXTRA, CONSULTA)
            }
        }
        val tarefaFragment = TarefaFragment()
        tarefaFragment.arguments = argumentos

        activity?.supportFragmentManager?.commit {
            setReorderingAllowed(true)
            addToBackStack("TarefaFragment")
            replace(R.id.principalFcv, tarefaFragment)
        }
    }

    override fun atualizarListaTarefas(listaTarefas: MutableList<Tarefa>) {
        tarefasList.clear()
        tarefasList.addAll(listaTarefas)
        tarefasAdapter.notifyDataSetChanged()
    }

    override fun retornarTarefa(tarefa: Tarefa) {
        // Nao se aplica
    }

    private val receiveBuscarTarefasBr: BroadcastReceiver by lazy {
        object: BroadcastReceiver() {
            override fun onReceive(context: Context?, intent: Intent?) {
                val bundle = intent?.extras
                val lista = bundle?.getParcelableArray(EXTRA_BUSCAR_TAREFAS)
                val listaTarefas: MutableList<Tarefa> = mutableListOf()
                lista?.forEach { item ->
                    listaTarefas.add(item as Tarefa)
                }

                atualizarListaTarefas(listaTarefas)
            }
        }
    }

    override fun onStart() {
        super.onStart()
        requireActivity().registerReceiver(receiveBuscarTarefasBr, IntentFilter(
            ACTION_BUSCAR_TAREFAS))
    }
}