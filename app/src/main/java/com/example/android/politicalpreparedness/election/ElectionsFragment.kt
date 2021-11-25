package com.example.android.politicalpreparedness.election

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.BindingAdapter
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.example.android.politicalpreparedness.database.ElectionDatabase
import com.example.android.politicalpreparedness.databinding.FragmentElectionBinding
import com.example.android.politicalpreparedness.election.adapter.ElectionListAdapter
import com.example.android.politicalpreparedness.election.adapter.ElectionListener
import com.example.android.politicalpreparedness.network.models.Election

class ElectionsFragment : Fragment() {

    private val viewModel: ElectionsViewModel by lazy {
        val database = ElectionDatabase.getInstance(requireContext()).electionDao
        ViewModelProvider(this, ElectionsViewModelFactory(database)).get(
            ElectionsViewModel::class.java
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentElectionBinding.inflate(inflater)
        binding.lifecycleOwner = this

        binding.viewModel = viewModel

        val upcomingAdapter = ElectionListAdapter(ElectionListener { election ->
            viewModel.navigateToVoterInfoOf(election)
        })

        val savedAdapter = ElectionListAdapter(ElectionListener { election ->
            viewModel.navigateToVoterInfoOf(election)
        })

        with(binding) {
            upcomingList.adapter = upcomingAdapter
            savedList.adapter = savedAdapter
        }

        viewModel.navigateToVoterInfo.observe(viewLifecycleOwner, Observer { election ->
            election?.let {
                findNavController().navigate(
                    ElectionsFragmentDirections.actionElectionsFragmentToVoterInfoFragment(
                        election.id,
                        election.division
                    )
                )
                viewModel.doneNavigationOfVoterInfo()
            }
        })

        return binding.root
    }

}

