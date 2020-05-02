package ayds.nene.movieinfo.home.view

import ayds.observer.Observable

interface HomeView {
    fun openView()
    val movieTitle: String
    fun onUiEvent(): Observable<UiEvent>
}