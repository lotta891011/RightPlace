package com.example.rightplace

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.navArgs
import com.example.rightplace.view.epoxy.DocumentEpoxyController
import com.example.rightplace.databinding.FragmentDocumentBinding
import com.example.rightplace.model.Document
import com.example.rightplace.model.DocumentInterface
import com.example.rightplace.model.Space


class DocumentFragment : BaseFragment(), DocumentInterface {
    private var _binding: FragmentDocumentBinding? = null
    private val binding get() = _binding!!

    private val safeArgs : DocumentFragmentArgs by navArgs()
    private val selectedSpace : Space? by lazy{
        spaceViewModel.spaceLiveData.value?.find {
            it.id == safeArgs.spaceId
        }
    }

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
        mainActivity.supportActionBar?.title="Dokumenty w: "+selectedSpace?.Name

        selectedSpace?.id?.let { sharedViewModel.setFilterQuery(it) }
        binding.addButton.setOnClickListener {
            val navDirections = selectedSpace?.id?.let { it1 ->
                DocumentFragmentDirections.actionDocumentFragmentToAddDocumentFragment(
                    it1
                )
            }
            navDirections?.let { it1 -> navigateViaGraph(it1) }
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
