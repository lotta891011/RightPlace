package com.example.rightplace

import android.R
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
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



        val spinner: Spinner = binding.typesSpinner
        documentTypeViewModel.documentTypeLiveData.observe(viewLifecycleOwner){ documentTypeList ->
            val tab: kotlin.collections.ArrayList <String> = ArrayList<String>()
            val ids: kotlin.collections.ArrayList <String> = ArrayList<String>()
            documentTypeList.forEach {
                tab.add(it.Name.toString())
                ids.add(it.id)
            }
            selectedDocument?.let {document ->
                binding.typeIdField.text = document.TypeId
                binding.typesSpinner.visibility=View.GONE
                binding.typeField.text = tab[ids.indexOf(document.TypeId)]
                binding.nameEditText.setText(document.Name)
                binding.descriptionEditText.setText(document.Description)

            }
            binding.changeButton.setOnClickListener {
                binding.typeTextField.visibility = View.GONE
                binding.typeField.visibility = View.GONE
                binding.typesSpinner.visibility=View.VISIBLE

            }
            val adapter : ArrayAdapter<String> = ArrayAdapter(
                requireActivity(),
                R.layout.simple_spinner_item,tab)

            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            // Apply the adapter to the spinner
            spinner.adapter = adapter

            binding.nameEditText.requestFocus()

            spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
                override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {



                    binding.saveButton.setOnClickListener {
                        saveDocumentToDatabase(ids[p2])
                        binding.typesSpinner.visibility=View.GONE
                        binding.typeField.text = tab[p2]
                        binding.typeTextField.visibility = View.VISIBLE
                        binding.typeField.visibility = View.VISIBLE

                    }

                }

                override fun onNothingSelected(p0: AdapterView<*>?) {
                    TODO("Not yet implemented")
                }

            }
        }

    }


    private fun saveDocumentToDatabase(id :String = ""){

        val documentName = binding.nameEditText.text.toString().trim()
        if (documentName.isEmpty()){
            binding.nameTextField.error = "Pole wymagane"
            return
        }
        binding.nameTextField.error = null
        val documentDescription = binding.descriptionEditText.text.toString().trim()

        val document = selectedDocument!!.copy(
            Name = documentName,
            Description = documentDescription
        )
        if (id == ""){
            document.TypeId=binding.typeIdField.text.toString()
        }
        else{
            document.TypeId=id
        }
        sharedViewModel.updateDocument(document)
        Toast.makeText(requireActivity(), "Zaktualizowano pomyślnie", Toast.LENGTH_SHORT).show()


    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}