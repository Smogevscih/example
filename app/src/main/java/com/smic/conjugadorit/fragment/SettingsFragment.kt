package com.smic.conjugadorit.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CompoundButton
import androidx.appcompat.widget.SwitchCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.smic.conjugadorit.databinding.FragmentSettingsBinding
import com.smic.conjugadorit.databinding.TenseBlockBinding


class SettingsFragment : Fragment() {

    private val viewModel: SettingsFragmentViewModel by lazy {
        ViewModelProvider(this)[SettingsFragmentViewModel::class.java]
    }
    private var _binding: FragmentSettingsBinding? = null
    private val binding: FragmentSettingsBinding get() = _binding!!
    private lateinit var switchList: List<SwitchCompat>
    private var pronombresSettingsMap: MutableMap<String, Boolean>? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSettingsBinding.inflate(inflater, container, false)

        switchList = with(binding) {
            listOf(
                yoSwitch,
                tuSwitch,
                elSwitch,
                nosotrosSwitch,
                vosotrosSwitch,
                ellosSwitch
            )
        }

        viewModel.settingsLiveData.observe(viewLifecycleOwner) { settings ->
            settings?.let {
                pronombresSettingsMap = it.pronombresSettings
                switchList.forEach { switch->
                    switch.isChecked = settings.pronombresSettings[switch.text.toString()] ?: true
                    switch.jumpDrawablesToCurrentState()
                }

                binding.tensesRecyclerView.apply {
                    layoutManager = LinearLayoutManager(context)
                    adapter = TenseAdapter(settings.tensesSettings)
                }

            }

        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val changeListener = CompoundButton.OnCheckedChangeListener { switch, value ->
            pronombresSettingsMap?.set(switch.text.toString(), value)
        }

        switchList.forEach { it.setOnCheckedChangeListener(changeListener) }
    }

    override fun onPause() {
        super.onPause()
        viewModel.updateSettings()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private inner class TenseAdapter(private val list: MutableList<Pair<String, Boolean>>) :
        RecyclerView.Adapter<TenseHolder>(), TenseCallback {


        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TenseHolder {
            val binding = TenseBlockBinding.inflate(layoutInflater, parent, false)
            return TenseHolder(binding, this)
        }

        override fun onBindViewHolder(holder: TenseHolder, position: Int) {
            holder.bind(list[position])

        }

        override fun getItemCount(): Int = list.size
        override fun checked(tenseName: String, isChecked: Boolean, adapterPosition: Int) {
            list[adapterPosition] = list[adapterPosition].copy(second = isChecked)

        }
    }

    private class TenseHolder(
        private val binding: TenseBlockBinding,
        private val callback: TenseCallback
    ) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(pair: Pair<String, Boolean>) {
            with(binding) {
                tenseSwitch.text = pair.first
                tenseSwitch.isChecked = pair.second
                tenseSwitch.setOnCheckedChangeListener { _, b ->
                    callback.checked(pair.first, b, adapterPosition)
                }
            }
        }
    }

    interface TenseCallback {
        fun checked(tenseName: String, isChecked: Boolean, adapterPosition: Int)
    }
}