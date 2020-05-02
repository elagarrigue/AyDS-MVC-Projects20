package ayds.nene.movieinfo.home.controller

import ayds.nene.movieinfo.home.model.HomeModelModule.homeModel
import ayds.nene.movieinfo.home.view.HomeViewModule.homeView

object HomeControllerModule {

    fun init() {
        HomeControllerImpl(homeView, homeModel)
    }
}