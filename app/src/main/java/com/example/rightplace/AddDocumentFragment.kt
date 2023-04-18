package com.example.rightplace

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.rightplace.databinding.FragmentAddDocumentBinding
import com.example.rightplace.model.Document
import java.util.UUID

class AddDocumentFragment: BaseFragment() {
    private var _binding: FragmentAddDocumentBinding? = null
    private val binding get() = _binding!!

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

        binding.saveButton.setOnClickListener {
            saveDocumentToDatabase()
        }
        sharedViewModel.transactionCompleteLiveData.observe(viewLifecycleOwner){complete ->
            if(complete){
                Toast.makeText(requireActivity(), "Pozycja dodana pomyślnie", Toast.LENGTH_SHORT).show()
                binding.nameEditText.text=null
                binding.nameEditText.requestFocus()
                binding.descriptionEditText.text=null
                sharedViewModel.transactionCompleteLiveData.postValue(false)
            }
        }
        binding.nameEditText.requestFocus()
    }

    private fun saveDocumentToDatabase(){
        val documentName = binding.nameEditText.text.toString().trim()
        if (documentName.isEmpty()){
            binding.nameTextField.error = "Pole wymagane"
            return
        }
        binding.nameTextField.error = null
        val documentDescription = binding.descriptionEditText.text.toString().trim()


        val document = Document(
            id = UUID.randomUUID().hashCode(),
            Name = documentName,
            Description = documentDescription,
            TypeId = 1, //todo
            RoomId = 1, //todo
            Code = documentName.hashCode()
        )
        sharedViewModel.insertDocument(document)

    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}