package ayds.nene.movieinfo.home.model

import ayds.nene.movieinfo.home.model.repository.OmdbRepositoryImpl
import ayds.nene.movieinfo.home.model.repository.external.omdb.OmdbAPI
import ayds.nene.movieinfo.home.model.repository.external.omdb.OmdbResponseToOmdbMovieResolverImpl
import ayds.nene.movieinfo.home.model.repository.external.omdb.OmdbService
import ayds.nene.movieinfo.home.model.repository.local.sqldb.SqlDBImpl
import ayds.nene.movieinfo.home.model.repository.local.sqldb.SqlQueriesImpl
import retrofit2.Retrofit
import retrofit2.converter.scalars.ScalarsConverterFactory

object HomeModelModule {

    private val retrofit = Retrofit.Builder()
        .baseUrl("http://www.omdbapi.com/")
        .addConverterFactory(ScalarsConverterFactory.create())
        .build()

    private fun getOmdbAPI(): OmdbAPI = retrofit.create(OmdbAPI::class.java)


    private val repository = OmdbRepositoryImpl(
        SqlDBImpl(SqlQueriesImpl()), OmdbService(getOmdbAPI(),
        OmdbResponseToOmdbMovieResolverImpl()
    ))

    val homeModel: HomeModel = HomeModelImpl(repository)
}