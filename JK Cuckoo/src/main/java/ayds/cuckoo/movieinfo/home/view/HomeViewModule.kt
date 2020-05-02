package ayds.cuckoo.movieinfo.home.view

import ayds.cuckoo.movieinfo.home.model.HomeModelModule

object HomeViewModule {
    val homeView = MainWindow(HomeModelModule.homeModel, MovieDescriptionHelperImpl())
}