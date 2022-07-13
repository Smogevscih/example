package com.smic.conjugadorit.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.smic.conjugadorit.SharedViewModel
import com.smic.conjugadorit.databinding.ConjugadoBlockBinding
import com.smic.conjugadorit.databinding.FragmentVerbBinding
import com.smic.conjugadorit.databinding.PairBlockBinding
import com.smic.domain.entities.Conjugado
import com.smic.domain.utils.FactoryPronoun
import java.util.*


class VerbFragment : Fragment() {
    private val sharedViewModel: SharedViewModel by lazy {
        ViewModelProvider(requireActivity())[SharedViewModel::class.java]
    }
    private val viewModel: VerbFragmentViewModel by lazy {
        ViewModelProvider(this)[VerbFragmentViewModel::class.java]
    }
    private var _binding: FragmentVerbBinding? = null
    private val binding: FragmentVerbBinding get() = _binding!!

    private val settings get() = viewModel.settingsLiveData.value!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentVerbBinding.inflate(inflater, container, false)

        binding.verbRecyclerView.apply {
            layoutManager = LinearLayoutManager(context)
        }

        val verb_infinitivo = sharedViewModel.selectedVerb
        viewModel.setSelectedVerb(verb_infinitivo)

        viewModel.verbLiveData.observe(viewLifecycleOwner) {
            binding.infinitiveTextView.text = it.infinitivo
            binding.translationTextView.text =
                if (Locale.getDefault().language.uppercase() == "RU") it.translationRU else it.translationEN
            val adapter = VerbAdapter(it.listConjugado)
            binding.verbRecyclerView.adapter = adapter
        }

        return binding.root
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private inner class VerbAdapter(private val listConjugado: List<Conjugado>) :
        RecyclerView.Adapter<VerbHolder>() {

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VerbHolder {
            val binding = ConjugadoBlockBinding.inflate(layoutInflater, parent, false)
            return VerbHolder(binding)
        }

        override fun onBindViewHolder(holder: VerbHolder, position: Int) {
            holder.bind(listConjugado[position])
        }

        override fun getItemCount(): Int = listConjugado.size

    }

    private inner class VerbHolder(private val binding: ConjugadoBlockBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(conjugado: Conjugado) {
            with(binding) {
                verbTextView.text = conjugado.nombre

                //возвращает упорядоченный список Пар (местоимение,спряжение)
                val conjugadoList =
                    FactoryPronoun.toOrderList(conjugado.mapConjugado, settings.pronombresSettings)

                conjugadoRecyclerView.apply {
                    layoutManager = LinearLayoutManager(context)
                    adapter = ConjugadoAdapter(conjugadoList)
                }
            }
        }
    }

    private inner class ConjugadoAdapter(private val listConjugado: List<Pair<String, String>>) :
        RecyclerView.Adapter<ConjugadoHolder>() {

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ConjugadoHolder {
            val binding = PairBlockBinding.inflate(layoutInflater, parent, false)
            return ConjugadoHolder(binding)
        }

        override fun onBindViewHolder(holder: ConjugadoHolder, position: Int) {
            holder.bind(listConjugado[position])
        }

        override fun getItemCount(): Int = listConjugado.size

    }

    private class ConjugadoHolder(private val binding: PairBlockBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(pair: Pair<String, String>) {
            with(binding) {
                pronomNameTextView.text = pair.first
                conjTextView.text = pair.second
            }
        }

    }


}