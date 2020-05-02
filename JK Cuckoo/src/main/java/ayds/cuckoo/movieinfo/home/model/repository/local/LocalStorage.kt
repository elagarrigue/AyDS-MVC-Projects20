package ayds.cuckoo.movieinfo.home.model.repository.local

import ayds.cuckoo.movieinfo.home.model.entities.OmdbMovie

interface LocalStorage {

    fun saveMovie(term: String, movie: OmdbMovie)
    fun getMovie(term: String): OmdbMovie?
}