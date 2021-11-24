package com.example.android.politicalpreparedness.election

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.android.politicalpreparedness.R
import com.example.android.politicalpreparedness.database.ElectionDatabase
import com.example.android.politicalpreparedness.databinding.FragmentVoterInfoBinding

class VoterInfoFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val database = ElectionDatabase.getInstance(requireContext())
        val viewModel =
            ViewModelProvider(this, VoterInfoViewModelFactory(database.electionDao)).get(
                VoterInfoViewModel::class.java
            )

        val binding = FragmentVoterInfoBinding.inflate(inflater)
        binding.lifecycleOwner = this

        val args = VoterInfoFragmentArgs.fromBundle(requireArguments())
        viewModel.loadVoterInfo(args.argElectionId, args.argDivision)
        binding.viewModel = viewModel

        viewModel.targetUrl.observe(viewLifecycleOwner, Observer {
            it?.let {
                loadUrl(it)
                viewModel.doneNavigation()
            }
        })

        viewModel.saveButtonState.observe(viewLifecycleOwner, Observer { state ->
            if (state) {
                binding.saveButton.text = getString(R.string.unfollow_election)
            } else {
                binding.saveButton.text = getString(R.string.follow_election)
            }
        })

        return binding.root

    }

    private fun loadUrl(url: String) {
        val uri = Uri.parse(url)
        val intent = Intent(Intent.ACTION_VIEW, uri)
        requireContext().startActivity(intent)
    }

}