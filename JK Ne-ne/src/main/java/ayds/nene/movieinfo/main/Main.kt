package ayds.nene.movieinfo.main

import ayds.nene.movieinfo.home.controller.HomeControllerModule
import ayds.nene.movieinfo.home.view.HomeViewModule

fun main(args: Array<String>) {
    initGraph()
    HomeViewModule.homeView.openView()
}

private fun initGraph() {
    HomeControllerModule.init()
}