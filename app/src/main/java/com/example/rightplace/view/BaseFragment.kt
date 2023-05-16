package com.example.rightplace

import android.os.Bundle
import android.util.Log
import androidx.annotation.IdRes
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.NavController
import androidx.navigation.NavDirections
import com.example.rightplace.architecture.DocumentTypeViewModel
import com.example.rightplace.architecture.DocumentViewModel
import com.example.rightplace.architecture.SpaceViewModel
import com.example.rightplace.database.AppDatabase

abstract class BaseFragment : Fragment(){
    protected val mainActivity: MainActivity
        get() = activity as MainActivity

    protected val appDatabase: AppDatabase
        get() = AppDatabase.getDatabase(requireActivity())

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

    fun safeNavigate(
        @IdRes currentDestinationId: Int,
        navDirections: NavDirections
    ) {
        if (currentDestinationId == mainActivity.navController.currentDestination?.id) {
            mainActivity.navController.navigate(navDirections)
        }
    }

    fun safeNavigate(direction: NavDirections) {
//        Log.d(clickTag, "Click happened")
        mainActivity.navController.currentDestination?.getAction(direction.actionId)?.run {
//            Log.d(clickTag, "Click Propagated")
            mainActivity.navController.navigate(direction)
        }
    }
}