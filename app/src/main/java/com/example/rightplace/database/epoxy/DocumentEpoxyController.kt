package com.example.rightplace.database.epoxy

import com.airbnb.epoxy.EpoxyController
import com.example.rightplace.R
import com.example.rightplace.model.Document
import com.example.rightplace.model.DocumentInterface
import com.example.rightplace.databinding.ModelDocumentBinding

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
            return
        }
        documentList.forEach { document ->
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
        }

    }
}