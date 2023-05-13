package com.example.rightplace.view.epoxy

import com.airbnb.epoxy.EpoxyController
import com.example.rightplace.R
import com.example.rightplace.model.Space
import com.example.rightplace.model.SpaceInterface
import com.example.rightplace.databinding.ModelSpaceBinding
import com.example.rightplace.databinding.ModelEmptyStateBinding

class SpaceEpoxyController(
    private val spaceInterface: SpaceInterface
): EpoxyController() {

    var spaceList = ArrayList<Space>()
        set(value) {
            field = value
            requestModelBuild()
        }

    override fun buildModels() {

        if(spaceList.isEmpty()){
            EmptyStateEpoxyModel().id("test").addTo(this)
            return
        }
        // dodawanie posoriwanych obiektów do widoku iterując
        spaceList.sortedBy { it.Name } .forEach { space ->
            SpaceEpoxyModel(space, spaceInterface).id(space.id).addTo(this)
        }

    }
    data class SpaceEpoxyModel(
        val space: Space,
        val spaceInterface: SpaceInterface
    ): ViewBindingKotlinModel<ModelSpaceBinding>(R.layout.model_space){

        override fun ModelSpaceBinding.bind(){
            nameTextView.text = space.Name
            descriptionTextView.text = space.Description
            editButton.setOnClickListener {
                spaceInterface.onEdit(space)
//                spaceInterface.onDelete(space)
            }

            root.setOnClickListener {
                spaceInterface.onItemSelected(space)
            }
        }

    }
    class EmptyStateEpoxyModel(): ViewBindingKotlinModel<ModelEmptyStateBinding>(R.layout.model_empty_state){
        override fun ModelEmptyStateBinding.bind(){
            //nothing at the moment
        }
    }

}