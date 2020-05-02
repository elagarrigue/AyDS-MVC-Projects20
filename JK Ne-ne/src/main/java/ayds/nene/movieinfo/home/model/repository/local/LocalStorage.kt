package ayds.nene.movieinfo.home.model.repository.local

import ayds.nene.movieinfo.home.model.entities.OmdbMovie

interface LocalStorage {

    fun saveMovie(term: String, movie: OmdbMovie)
    fun getMovie(term: String): OmdbMovie?
}