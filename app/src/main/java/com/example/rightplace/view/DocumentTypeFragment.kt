package com.example.rightplace

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.navArgs
import com.example.rightplace.view.epoxy.DocumentEpoxyController
import com.example.rightplace.databinding.FragmentDocumentBinding
import com.example.rightplace.model.*
import com.example.rightplace.view.epoxy.DocumentTypeEpoxyController


class DocumentTypeFragment : BaseFragment(), DocumentTypeInterface {
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
//        mainActivity.supportActionBar?.title="Dokumenty w: "+selectedSpace?.Name


        binding.addButton.setOnClickListener {
            navigateViaGraph(R.id.action_documentTypeFragment_to_addDocumentTypeFragment)
        }

        val controller = DocumentTypeEpoxyController(this)
        binding.epoxyRecyclerView.setController(controller)

        documentTypeViewModel.documentTypeLiveData.observe(viewLifecycleOwner){ documentTypeList ->
            controller.documentTypeList = documentTypeList as ArrayList<DocumentType>

        }
    }
    override fun onDelete(documentType: DocumentType) {
        documentTypeViewModel.deleteDocumentType(documentType)
        Toast.makeText(requireActivity(), "Pozycja usunięta pomyślnie", Toast.LENGTH_SHORT).show()

    }

    override fun onItemSelected(documentType: DocumentType) {
        val navDirections = DocumentTypeFragmentDirections.actionDocumentTypeFragmentToAddDocumentTypeFragment(documentType.id)
        navigateViaGraph(navDirections)
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


}
