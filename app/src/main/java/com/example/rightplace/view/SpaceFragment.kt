package com.example.rightplace.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.rightplace.R
import com.example.rightplace.databinding.FragmentSpaceBinding
import com.example.rightplace.model.Space
import com.example.rightplace.model.SpaceInterface
import com.example.rightplace.epoxy.SpaceEpoxyController


class SpaceFragment : BaseFragment(), SpaceInterface {
    private var _binding: FragmentSpaceBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentSpaceBinding.inflate(inflater, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.addButton.setOnClickListener {
            navigateViaGraph(R.id.action_spaceFragment_to_addSpaceFragment)
        }

        val controller = SpaceEpoxyController(this)
        binding.epoxyRecyclerView.setController(controller)
        spaceViewModel.spaceLiveData.observe(viewLifecycleOwner){
            controller.spaceList = it as ArrayList<Space>
        }


    }

    override fun onEdit(space: Space) {
        val navDirections = SpaceFragmentDirections.actionSpaceFragmentToAddSpaceFragment(space.id)
        navigateViaGraph(navDirections)
    }

    override fun onItemSelected(space: Space) {
        val navDirections = SpaceFragmentDirections.actionSpaceFragmentToDocumentFragment(space.id)
        navigateViaGraph(navDirections)
    }
}