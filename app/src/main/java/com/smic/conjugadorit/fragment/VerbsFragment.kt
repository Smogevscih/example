package com.smic.conjugadorit.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.smic.conjugadorit.R
import com.smic.conjugadorit.SharedViewModel
import com.smic.conjugadorit.databinding.FragmentVerbsBinding
import com.smic.conjugadorit.databinding.ProgressBlockBinding
import com.smic.conjugadorit.databinding.VerbBlockBinding
import com.smic.conjugadorit.utils.show
import com.smic.domain.entities.Verb
import java.util.*

class VerbsFragment : Fragment() {
    private val sharedViewModel: SharedViewModel by lazy {
        ViewModelProvider(requireActivity())[SharedViewModel::class.java]
    }
    private val viewModel: VerbsFragmentViewModel by lazy {
        ViewModelProvider(this)[VerbsFragmentViewModel::class.java]
    }
    private var _binding: FragmentVerbsBinding? = null
    private val binding: FragmentVerbsBinding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentVerbsBinding.inflate(inflater, container, false)
        sharedViewModel.currentQuest.observe(viewLifecycleOwner){
            viewModel.getVerbs(it)
        }

        val linearLayoutManager = LinearLayoutManager(context)
        binding.verbsRecycler.apply {
            layoutManager = linearLayoutManager

            //добавление слушателя на прокрутку, реализация пагинации
            addOnScrollListener(object :
                PaginationScrollListener(linearLayoutManager) {
                override fun loadMoreItems() {
                    viewModel.getNextVerbs((adapter as VerbsAdapter))
                }

                override val possibleLoad: Boolean
                    get() = viewModel.possibleLoad

            })
        }

        viewModel.verbsLiveData.observe(viewLifecycleOwner) {
            val adapter = VerbsAdapter(it)

            binding.verbsRecycler.adapter = adapter
        }

        viewModel.resetCount()// обнуление счетчика для пагинации
        return binding.root
    }

    private fun setCurrentVerb(infinitivo: String) {
        sharedViewModel.selectedVerb(infinitivo)
        findNavController().navigate(R.id.verbFragment)
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private inner class VerbsHolder(private val binding: VerbBlockBinding) :
        RecyclerView.ViewHolder(binding.root) {


        fun bind(verb: Verb) {
            binding.verbTextView.text = verb.infinitivo
            binding.translationTextView.text =
                if (Locale.getDefault().language.uppercase() == "RU") verb.translationRU else verb.translationEN

            itemView.setOnClickListener {
                setCurrentVerb(verb.infinitivo)
            }

        }

    }

    private class ProgressHolder(private val binding: ProgressBlockBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind() = binding.progressRequest.show

    }

    inner class VerbsAdapter(_verbs: List<Verb>) :
        RecyclerView.Adapter<RecyclerView.ViewHolder>() {
        private val VERBS_HOLD = 100
        private val PROGRESS_HOLD = 200
        private var flag = true
        private val verbs: MutableList<Verb>

        init {
            if (_verbs.size < COUNT_OF_VERB) {
                flag = false
            }
            verbs = _verbs.toMutableList()
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
            return when (viewType) {
                VERBS_HOLD -> {
                    val binding = VerbBlockBinding.inflate(layoutInflater, parent, false)
                    VerbsHolder(binding)
                }
                else -> {
                    val binding = ProgressBlockBinding.inflate(layoutInflater, parent, false)
                    ProgressHolder(binding)
                }
            }
        }


        override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
            when (holder) {
                is VerbsHolder -> holder.bind(verbs[position])
                is ProgressHolder -> holder.bind()
            }

        }

        override fun getItemViewType(position: Int) =
            if (position == verbs.size)
                PROGRESS_HOLD
            else VERBS_HOLD

        fun addNextVerbs(newPartOfVerbs: List<Verb>) {
            if (newPartOfVerbs.size < COUNT_OF_VERB) {
                flag = false
            }

            if (newPartOfVerbs.isEmpty()) {
                return
            }
            val positionStart = verbs.size
            verbs.addAll(newPartOfVerbs)
            notifyItemInserted(positionStart)
        }

        override fun getItemCount(): Int = if (flag) verbs.size + 1 else verbs.size

    }

    /**
     * Слушатель для организации пагинации. Определяет виден ли последний элемент в RecyclerView.
     * Требуется реализовать один метод loadMoreItems(), для загрузки новой порции данных и
     * установить поле-флаг (possibleLoad), для исключения серии загрузки новых данных.
     */
    private abstract class PaginationScrollListener(var layoutManager: LinearLayoutManager) :
        RecyclerView.OnScrollListener() {
        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
            super.onScrolled(recyclerView, dx, dy)
            val visibleItemCount = layoutManager.childCount
            val totalItemCount = layoutManager.itemCount
            val firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition()
            if (possibleLoad) {
                if (visibleItemCount + firstVisibleItemPosition >=
                    totalItemCount && firstVisibleItemPosition >= 0
                ) {
                    loadMoreItems()
                }
            }
        }

        protected abstract fun loadMoreItems()

        abstract val possibleLoad: Boolean //for avoiding serial requests

    }

    companion object {
        @JvmStatic
        private val COUNT_OF_VERB = 50
    }
}