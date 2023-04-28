package com.example.rightplace

import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.NavDirections
import com.example.rightplace.architecture.AppViewModel
import com.example.rightplace.database.AppDatabase

abstract class BaseFragment : Fragment(){
    protected val mainActivity: MainActivity
        get() = activity as MainActivity

    protected val appDatabase: AppDatabase
        get() = AppDatabase.getDatabase(requireActivity())

    protected val sharedViewModel: AppViewModel by activityViewModels()

    protected fun navigateUp(){
        mainActivity.navController.navigateUp()
    }

    protected fun navigateViaGraph(actionId: Int){
        mainActivity.navController.navigate(actionId)
    }

    protected fun navigateViaGraph(navDirections: NavDirections){
        mainActivity.navController.navigate(navDirections)
    }
}