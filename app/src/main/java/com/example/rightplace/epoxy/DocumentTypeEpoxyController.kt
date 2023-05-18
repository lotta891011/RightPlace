package com.example.rightplace.epoxy

import com.airbnb.epoxy.EpoxyController
import com.example.rightplace.R
import com.example.rightplace.model.DocumentType
import com.example.rightplace.model.DocumentTypeInterface
import com.example.rightplace.databinding.ModelDocumentTypeBinding
import com.example.rightplace.databinding.ModelEmptyStateBinding

class DocumentTypeEpoxyController(
    private val documentTypeInterface: DocumentTypeInterface
): EpoxyController() {

    var documentTypeList = ArrayList<DocumentType>()
        set(value) {
            field = value
            requestModelBuild()
        }
    override fun buildModels() {

        if(documentTypeList.isEmpty()){
            EmptyStateEpoxyModel().id("empty_state").addTo(this)
            return
        }
        documentTypeList.sortedBy { it.Name } .forEach { documentType ->
            DocumentTypeEpoxyModel(documentType, documentTypeInterface).id(documentType.id).addTo(this)
        }

    }
    data class DocumentTypeEpoxyModel(
        val documentType: DocumentType,
        val documentTypeInterface: DocumentTypeInterface
    ): ViewBindingKotlinModel<ModelDocumentTypeBinding>(R.layout.model_document_type){

        override fun ModelDocumentTypeBinding.bind(){
            nameTextView.text = documentType.Name
            descriptionTextView.text = documentType.Description
            deleteButton.setOnClickListener {
                documentTypeInterface.onDelete(documentType)

            }

            root.setOnClickListener {
                documentTypeInterface.onItemSelected(documentType)
            }
        }

    }
    class EmptyStateEpoxyModel: ViewBindingKotlinModel<ModelEmptyStateBinding>(R.layout.model_empty_state){
        override fun ModelEmptyStateBinding.bind(){
        //nothing at the moment
        }
    }

}