package com.example.rightplace.view.additional

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.navArgs
import com.example.rightplace.view.BaseFragment
import com.example.rightplace.R
import com.example.rightplace.databinding.FragmentAddDocumentTypeBinding
import com.example.rightplace.model.DocumentType
import java.util.UUID

class AddDocumentTypeFragment: BaseFragment() {
    private var _binding: FragmentAddDocumentTypeBinding? = null
    private val binding get() = _binding!!

    private val safeArgs : AddDocumentTypeFragmentArgs by navArgs()
    private val selectedDocumentType : DocumentType? by lazy{
        documentTypeViewModel.documentTypeLiveData.value?.find {
            it.id == safeArgs.documentTypeId
        }
    }
    private var isInEditMode : Boolean = false
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAddDocumentTypeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mainActivity.supportActionBar?.title=getString(R.string.add_room)


        binding.saveButton.setOnClickListener {
            saveDocumentTypeToDatabase()
        }
        sharedViewModel.transactionCompleteLiveData.observe(viewLifecycleOwner){complete ->
            if(complete){
                if(isInEditMode){
                    navigateUp()
                    return@observe
                }
                binding.nameEditText.text=null
                binding.nameEditText.requestFocus()
                binding.descriptionEditText.text=null
                sharedViewModel.transactionCompleteLiveData.postValue(false)
            }
        }
        binding.nameEditText.requestFocus()

        selectedDocumentType?.let { documentType ->
            isInEditMode = true
            binding.nameEditText.setText(documentType.Name)
            binding.descriptionEditText.setText(documentType.Description)
            binding.saveButton.text = getString(R.string.update)
            mainActivity.supportActionBar?.title = getString(R.string.update_info)

        }
    }


    private fun saveDocumentTypeToDatabase(){
        val documentTypeName = binding.nameEditText.text.toString().trim()
        if (documentTypeName.isEmpty()){
            binding.nameTextField.error = getString(R.string.required)
            return
        }
        binding.nameTextField.error = null
        val documentTypeDescription = binding.descriptionEditText.text.toString().trim()

        if(isInEditMode) {
            val documentType = selectedDocumentType!!.copy(
                Name = documentTypeName,
                Description = documentTypeDescription,
            )
            documentTypeViewModel.updateDocumentType(documentType)
            Toast.makeText(requireActivity(), getString(R.string.update_success), Toast.LENGTH_SHORT).show()

        }
        else{
            val documentType = DocumentType(
                id = UUID.randomUUID().toString(),
                Name = documentTypeName,
                Description = documentTypeDescription
            )
            documentTypeViewModel.insertDocumentType(documentType)
            Toast.makeText(requireActivity(), getString(R.string.add_success), Toast.LENGTH_SHORT).show()
        }
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}