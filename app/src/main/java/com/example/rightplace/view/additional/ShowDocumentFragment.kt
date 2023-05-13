package com.example.rightplace

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.navArgs
import com.example.rightplace.databinding.FragmentShowDocumentBinding
import com.example.rightplace.model.Document

class ShowDocumentFragment: BaseFragment() {
    private var _binding: FragmentShowDocumentBinding? = null
    private val binding get() = _binding!!

    private val safeArgs : ShowDocumentFragmentArgs by navArgs()
    private val selectedDocument : Document? by lazy{
        sharedViewModel.documentLiveData.value?.find {
            it.id == safeArgs.documentId
        }

    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentShowDocumentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mainActivity.supportActionBar?.title="Podgląd i edycja dokumentu"

        binding.saveButton.setOnClickListener {
            saveDocumentToDatabase()
        }
        sharedViewModel.transactionCompleteLiveData.observe(viewLifecycleOwner){complete ->
            if(complete){
                    return@observe
            }
        }
        binding.nameEditText.requestFocus()

        selectedDocument?.let {document ->
            binding.nameEditText.setText(document.Name)
            binding.descriptionEditText.setText(document.Description)
        }
    }


    private fun saveDocumentToDatabase(){
        val documentName = binding.nameEditText.text.toString().trim()
        if (documentName.isEmpty()){
            binding.nameTextField.error = "Pole wymagane"
            return
        }
        binding.nameTextField.error = null
        val documentDescription = binding.descriptionEditText.text.toString().trim()

        val document = selectedDocument!!.copy(
            Name = documentName,
            Description = documentDescription,
        )
        sharedViewModel.updateDocument(document)
        Toast.makeText(requireActivity(), "Zaktualizowano pomyślnie", Toast.LENGTH_SHORT).show()


    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}