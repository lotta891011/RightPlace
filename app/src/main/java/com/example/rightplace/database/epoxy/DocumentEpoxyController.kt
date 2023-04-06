package com.example.rightplace.database.epoxy

import com.airbnb.epoxy.EpoxyController
import com.example.rightplace.R
import com.example.rightplace.database.model.Document
import com.example.rightplace.database.model.DocumentInterface
import com.example.rightplace.databinding.ModelDocumentBinding

class DocumentEpoxyController(
    private val documentInterface: DocumentInterface
): EpoxyController() {
    var isLoading: Boolean = true
        set(value){
            field = value
            if(field){
                requestModelBuild()
            }
        }
    var documentList = ArrayList<Document>()
        set(value) {
            field = value
            isLoading = false
            requestModelBuild()
        }
    override fun buildModels() {
        if(isLoading){
            return
        }
        if(documentList.isEmpty()){
            return
        }
        documentList.forEach { document ->
            DocumentEpoxyModel(document, documentInterface).id(document.id)
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
        }

    }
}