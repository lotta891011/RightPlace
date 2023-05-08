package com.example.rightplace

import android.os.Bundle
import android.text.Layout.Directions
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModel
import androidx.navigation.NavDirections
import androidx.navigation.fragment.navArgs
import com.example.rightplace.architecture.AppViewModel
import com.example.rightplace.view.epoxy.DocumentEpoxyController
import com.example.rightplace.databinding.FragmentAddDocumentBinding
import com.example.rightplace.databinding.FragmentDocumentBinding
import com.example.rightplace.model.Document
import com.example.rightplace.model.DocumentInterface
import kotlin.reflect.KProperty


class DocumentFragment : BaseFragment(), DocumentInterface {
    private var _binding: FragmentDocumentBinding? = null
    private val binding get() = _binding!!



    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentDocumentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.addButton.setOnClickListener {
            navigateViaGraph(R.id.action_documentFragment_to_addDocumentFragment)
        }

        val controller = DocumentEpoxyController(this)
        binding.epoxyRecyclerView.setController(controller)

        sharedViewModel.documentLiveData.observe(viewLifecycleOwner){ documentList ->
            controller.documentList = documentList as ArrayList<Document>

        }
    }
    override fun onDelete(document: Document) {
        sharedViewModel.deleteDocument(document)
    }

    override fun onItemSelected(document: Document) {
        val navDirections = DocumentFragmentDirections.actionDocumentFragmentToAddDocumentFragment(document.id)
        navigateViaGraph(navDirections)
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


}
