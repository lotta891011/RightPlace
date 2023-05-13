package com.example.rightplace

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.navArgs
import com.example.rightplace.databinding.FragmentAddSpaceBinding
import com.example.rightplace.model.Space
import java.util.UUID

class AddSpaceFragment: BaseFragment() {
    private var _binding: FragmentAddSpaceBinding? = null
    private val binding get() = _binding!!

    private val safeArgs : AddDocumentFragmentArgs by navArgs()
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
        _binding = FragmentAddSpaceBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mainActivity.supportActionBar?.title="Dodaj pokój"


        binding.saveButton.setOnClickListener {
            saveSpaceToDatabase()
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

        selectedSpace?.let { space ->
            isInEditMode = true
            binding.nameEditText.setText(space.Name)
            binding.descriptionEditText.setText(space.Description)
            binding.saveButton.text = "Zaktualizuj"
            mainActivity.supportActionBar?.title = "Zaktualizuj informacje"

        }
    }


    private fun saveSpaceToDatabase(){
        val spaceName = binding.nameEditText.text.toString().trim()
        if (spaceName.isEmpty()){
            binding.nameTextField.error = "Pole wymagane"
            return
        }
        binding.nameTextField.error = null
        val spaceDescription = binding.descriptionEditText.text.toString().trim()

        if(isInEditMode) {
            val space = selectedSpace!!.copy(
                Name = spaceName,
                Description = spaceDescription,
            )
            spaceViewModel.updateSpace(space)
            Toast.makeText(requireActivity(), "Zaktualizowano pomyślnie", Toast.LENGTH_SHORT).show()

        }
        else{
            val space = Space(
                id = UUID.randomUUID().toString(),
                Name = spaceName,
                Description = spaceDescription
            )
            spaceViewModel.insertSpace(space)
            Toast.makeText(requireActivity(), "Pozycja dodana pomyślnie", Toast.LENGTH_SHORT).show()

        }



    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}