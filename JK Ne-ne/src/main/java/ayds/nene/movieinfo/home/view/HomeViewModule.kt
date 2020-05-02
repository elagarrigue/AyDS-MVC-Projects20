package ayds.nene.movieinfo.home.view

import ayds.nene.movieinfo.home.model.HomeModelModule

object HomeViewModule {
    val homeView = MainWindow(HomeModelModule.homeModel, MovieDescriptionHelperImpl())
}