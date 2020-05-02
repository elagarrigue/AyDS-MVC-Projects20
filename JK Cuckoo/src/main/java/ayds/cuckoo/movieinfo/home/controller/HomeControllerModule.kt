package ayds.cuckoo.movieinfo.home.controller

import ayds.cuckoo.movieinfo.home.model.HomeModelModule.homeModel
import ayds.cuckoo.movieinfo.home.view.HomeViewModule.homeView

object HomeControllerModule {

    fun init() {
        HomeControllerImpl(homeView, homeModel)
    }
}