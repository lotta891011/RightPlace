package com.example.rightplace.database.epoxy

import com.airbnb.epoxy.EpoxyController
import com.example.rightplace.R
import com.example.rightplace.model.Document
import com.example.rightplace.model.DocumentInterface
import com.example.rightplace.databinding.ModelDocumentBinding
import com.example.rightplace.databinding.ModelEmptyStateBinding

class DocumentEpoxyController(
    private val documentInterface: DocumentInterface
): EpoxyController() {

    var documentList = ArrayList<Document>()
        set(value) {
            field = value
            requestModelBuild()
        }
    override fun buildModels() {

        if(documentList.isEmpty()){
            EmptyStateEpoxyModel().id("empty_sate").addTo(this)
            return
        }
        // dodawanie posoriwanych obiektów do widoku iterując
        documentList.sortedByDescending { it.id } .forEach { document ->
            DocumentEpoxyModel(document, documentInterface).id(document.id).addTo(this)
        }

    }
    data class DocumentEpoxyModel(
        val document: Document,
        val documentInterface: DocumentInterface
    ): ViewBindingKotlinModel<ModelDocumentBinding>(R.layout.model_document){

        override fun ModelDocumentBinding.bind(){
            nameTextView.text = document.Name
            descriptionTextView.text = document.Description
            deleteButton.setOnClickListener {
                documentInterface.onDelete(document)
            }

            root.setOnClickListener {
                documentInterface.onItemSelected(document)
            }
        }

    }
    class EmptyStateEpoxyModel(): ViewBindingKotlinModel<ModelEmptyStateBinding>(R.layout.model_empty_state){
        override fun ModelEmptyStateBinding.bind(){
        //nothing at the moment
        }
    }

}