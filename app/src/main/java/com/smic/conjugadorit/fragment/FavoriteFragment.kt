package com.smic.conjugadorit.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.smic.conjugadorit.R
import com.smic.conjugadorit.SharedViewModel
import com.smic.conjugadorit.databinding.FragmentFavoriteBinding
import com.smic.conjugadorit.databinding.ItemBinding
import com.smic.conjugadorit.ui.FlowLayout
import com.smic.conjugadorit.ui.FlowLayoutAdapter


class FavoriteFragment : Fragment() {
    private val sharedViewModel: SharedViewModel by lazy {
        ViewModelProvider(requireActivity())[SharedViewModel::class.java]
    }

    private val viewModel: FavoriteFragmentViewModel by lazy {
        ViewModelProvider(this)[FavoriteFragmentViewModel::class.java]
    }

    private var _binding: FragmentFavoriteBinding? = null
    private val binding: FragmentFavoriteBinding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFavoriteBinding.inflate(inflater, container, false)

        //получение списка популярных глаголов
        val listStr = resources.getStringArray(R.array.popular_verbs).toList()

        val listener = object : FlowLayout.OnItemClickListener {
            override fun onItemClick(view: View, position: Int, parent: FlowLayout) {
                val infinitivo = view.findViewById<TextView>(R.id.tv_title).text
                sharedViewModel.selectedVerb(infinitivo.toString())
                viewModel.addToFavoriteVerbs(infinitivo.toString())
                findNavController().navigate(R.id.verbFragment)
            }
        }

        binding.popularVerbsFlowLayout.setOnItemClickListener(listener)
        binding.myVerbsFlowLayout.setOnItemClickListener(listener)

        binding.popularVerbsFlowLayout.setAdatper(
            object : FlowLayoutAdapter<String>(listStr) {
                override fun getView(flowLayout: FlowLayout, position: Int, data: Any): View {
                    val itemBinding = ItemBinding.inflate(layoutInflater, flowLayout, false)
                    itemBinding.tvTitle.text = data.toString()
                    return itemBinding.root
                }
            })


        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.favoriteVerbsLiveData.observe(viewLifecycleOwner) {
            binding.myVerbsFlowLayout.setAdatper(
                object : FlowLayoutAdapter<String>(it) {
                    override fun getView(flowLayout: FlowLayout, position: Int, data: Any): View {
                        val itemBinding = ItemBinding.inflate(layoutInflater, flowLayout, false)
                        itemBinding.tvTitle.text = data.toString()
                        return itemBinding.root
                    }
                })
        }

    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}