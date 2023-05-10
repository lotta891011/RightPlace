package com.example.rightplace

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.navArgs
import com.example.rightplace.databinding.FragmentAddDocumentBinding
import com.example.rightplace.model.Document
import com.example.rightplace.model.Space
import java.util.UUID

class AddDocumentFragment: BaseFragment() {
    private var _binding: FragmentAddDocumentBinding? = null
    private val binding get() = _binding!!

    private val safeArgs : AddDocumentFragmentArgs by navArgs()
//    private val selectedDocument : Document? by lazy{
//        sharedViewModel.documentLiveData.value?.find {
//            it.id == safeArgs.documentId
//
//        }
//
//    }
    private val selectedSpace : Space? by lazy{
        spaceViewModel.spaceLiveData.value?.find {
            it.id == safeArgs.spaceId
        }
    }
    private var isInEditMode : Boolean = false

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentAddDocumentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mainActivity.supportActionBar?.title="Dodaj dokument do: "+selectedSpace?.Name

        binding.saveButton.setOnClickListener {
            saveDocumentToDatabase()
        }
        sharedViewModel.transactionCompleteLiveData.observe(viewLifecycleOwner){complete ->
            if(complete){
                if(isInEditMode){
                    navigateUp()
                    return@observe
                }
                Toast.makeText(requireActivity(), "Pozycja dodana pomyślnie", Toast.LENGTH_SHORT).show()
                binding.nameEditText.text=null
                binding.nameEditText.requestFocus()
                binding.descriptionEditText.text=null
                sharedViewModel.transactionCompleteLiveData.postValue(false)
            }
        }
        binding.nameEditText.requestFocus()

        // setup when in edit mode
//        selectedDocument?.let {document ->
//            isInEditMode = true
//            binding.nameEditText.setText(document.Name)
//            binding.descriptionEditText.setText(document.Description)
//            binding.saveButton.text= "Zaktualizuj"
//            mainActivity.supportActionBar?.title="Zaktualizuj informacje"
//
//
//        }
    }


    private fun saveDocumentToDatabase(){
        val documentName = binding.nameEditText.text.toString().trim()
        if (documentName.isEmpty()){
            binding.nameTextField.error = "Pole wymagane"
            return
        }
        binding.nameTextField.error = null
        val documentDescription = binding.descriptionEditText.text.toString().trim()

        if(isInEditMode){
//            val document = selectedDocument!!.copy(
//                Name = documentName,
//                Description = documentDescription,
//            )
//            sharedViewModel.updateDocument(document)


        }
        else{
            val document = Document(
                id = UUID.randomUUID().toString(),
                Name = documentName,
                Description = documentDescription,
                TypeId = "1",
                RoomId = selectedSpace!!.id,
                Code = documentName.hashCode()
            )
            sharedViewModel.insertDocument(document)
        }

    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}