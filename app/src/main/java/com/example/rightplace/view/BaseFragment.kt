package com.example.rightplace.view

import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.NavDirections
import com.example.rightplace.MainActivity
import com.example.rightplace.architecture.DocumentTypeViewModel
import com.example.rightplace.architecture.DocumentViewModel
import com.example.rightplace.architecture.SpaceViewModel

abstract class BaseFragment : Fragment(){
    protected val mainActivity: MainActivity
        get() = activity as MainActivity


    protected val sharedViewModel: DocumentViewModel by activityViewModels()

    protected val spaceViewModel: SpaceViewModel by activityViewModels()

    protected val documentTypeViewModel: DocumentTypeViewModel by activityViewModels()

    protected fun navigateUp(){
        mainActivity.navController.navigateUp()
    }

    protected fun navigateViaGraph(actionId: Int){
        mainActivity.navController.navigate(actionId)
    }

    protected fun navigateViaGraph(navDirections: NavDirections){
        mainActivity.navController.navigate(navDirections)
    }

    fun safeNavigate(direction: NavDirections) {
        mainActivity.navController.currentDestination?.getAction(direction.actionId)?.run {
            mainActivity.navController.navigate(direction)
        }
    }
}