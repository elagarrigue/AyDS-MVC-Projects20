package ayds.cuckoo.movieinfo.main

import ayds.cuckoo.movieinfo.home.controller.HomeControllerModule
import ayds.cuckoo.movieinfo.home.view.HomeViewModule

fun main(args: Array<String>) {
    initGraph()
    HomeViewModule.homeView.openView()
}

private fun initGraph() {
    HomeControllerModule.init()
}
