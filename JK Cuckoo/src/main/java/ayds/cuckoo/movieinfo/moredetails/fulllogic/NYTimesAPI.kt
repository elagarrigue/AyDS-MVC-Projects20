package ayds.cuckoo.movieinfo.moredetails.fulllogic

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface NYTimesAPI {

    @GET("reviews/search.json?api-key=fFnIAXXz8s8aJ4dB8CVOJl0Um2P96Zyx")
    fun getTerm(@Query("query") term: String): Call<String>
}