package com.example.rightplace.view.additional

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.Toast
import androidx.navigation.fragment.navArgs
import com.example.rightplace.view.BaseFragment
import com.example.rightplace.databinding.FragmentAddDocumentBinding
import com.example.rightplace.model.Document
import com.example.rightplace.model.Space
import io.github.g0dkar.qrcode.QRCode
import java.io.File
import java.util.*
import kotlin.collections.ArrayList

class AddDocumentFragment: BaseFragment() {
    private var _binding: FragmentAddDocumentBinding? = null
    private val binding get() = _binding!!

    private val safeArgs : AddSpaceFragmentArgs by navArgs()

    private val selectedSpace : Space? by lazy{
        spaceViewModel.spaceLiveData.value?.find {
            it.id == safeArgs.spaceId
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAddDocumentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mainActivity.supportActionBar?.title="Dodaj dokument do: "+selectedSpace?.Name

//

        val spinner: Spinner = binding.typesSpinner
        documentTypeViewModel.documentTypeLiveData.observe(viewLifecycleOwner){ documentTypeList ->
            val tab: ArrayList <String> = ArrayList()
            val ids: ArrayList <String> = ArrayList()
            if (documentTypeList.isEmpty()){
                Toast.makeText(requireActivity(), "Przed dodaniem dokumentu dodaj rodzaj w trzeciej zakładce", Toast.LENGTH_LONG).show()
            }
            documentTypeList.forEach {
                tab.add(it.Name.toString())
                ids.add(it.id)
            }
            val adapter : ArrayAdapter<String> = ArrayAdapter(
                requireActivity(),
                android.R.layout.simple_spinner_item,tab)

            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

            spinner.adapter = adapter

            binding.nameEditText.requestFocus()

            spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
                override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {

                    binding.saveButton.setOnClickListener {
                        saveDocumentToDatabase(ids[p2])
                    }




                }

                override fun onNothingSelected(p0: AdapterView<*>?) {
                    TODO("Not yet implemented")
                }

            }
        }




        sharedViewModel.transactionCompleteLiveData.observe(viewLifecycleOwner){complete ->
            if(complete){
                binding.nameEditText.text=null
                binding.nameEditText.requestFocus()
                binding.descriptionEditText.text=null
                sharedViewModel.transactionCompleteLiveData.postValue(false)
            }
        }


    }

    private fun saveDocumentToDatabase(id:String){
        val documentName = binding.nameEditText.text.toString().trim()
        if (documentName.isEmpty()){
            binding.nameTextField.error = "Pole wymagane"
            return
        }
        binding.nameTextField.error = null
        val documentDescription = binding.descriptionEditText.text.toString().trim()

        val document = Document(
            id = UUID.randomUUID().toString(),
            Name = documentName,
            Description = documentDescription,
            TypeId = id,
            RoomId = selectedSpace!!.id,
            Code = documentName.hashCode()
        )
        sharedViewModel.insertDocument(document)

        val f = File("/storage/self/primary/DCIM/QR/")
        if(!f.exists()){
            f.mkdir()
        }
        val qrCodeFile = File("/storage/self/primary/DCIM/QR/"+document.id+"_qrcode.png")
        val dataToEncode = document.id
        val eachQRCodeSquareSize = 15 // In Pixels!
        val qrCodeRenderer = QRCode(dataToEncode).render(eachQRCodeSquareSize)
        qrCodeFile.let {
            qrCodeFile.outputStream().use { qrCodeRenderer.writeImage(it) }

        }



        Toast.makeText(requireActivity(), "Pozycja dodana pomyślnie", Toast.LENGTH_SHORT).show()


    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}