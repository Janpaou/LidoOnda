package com.example.lidoonda

import android.util.Log
import com.google.gson.JsonArray
import com.google.gson.JsonObject
import com.google.gson.JsonParser
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import okhttp3.OkHttpClient
import okhttp3.Request
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST
import java.io.IOException
import java.lang.Exception
import java.sql.*


public interface ApiService {
    @POST("postSelect/")
    @FormUrlEncoded
    fun select(@Field("query") query: String): Call<JsonObject>

    @POST("postUpdate/")
    @FormUrlEncoded
    fun update(@Field("query") query: String): Call<JsonObject>

    @POST("postRemove/")
    @FormUrlEncoded
    fun remove(@Field("query") query: String): Call<JsonObject>

    @POST("postInsert/")
    @FormUrlEncoded
    fun insert(@Field("query") query: String): Call<JsonObject>

}



val retrofit by lazy {
    Retrofit.Builder()
        .baseUrl("http://10.0.2.2:8000/webmobile/") 
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(ApiService::class.java)
}

fun makeQuerySelect(select: String, table: String, column: String, value : String): String{
     var valori = ""
     val query = "SELECT $select From webmobile.$table where $column = $value "
     Log.v("Query", query)
     val call: Call<JsonObject> =  retrofit.select(query)

     val job = GlobalScope.launch(Dispatchers.Default){
         try{
             var oggetto: String = call.execute().body()?.get("queryset").toString()
             oggetto = oggetto.drop(1)
             oggetto = oggetto.dropLast(1)
             Log.v("DBMSObj", oggetto)
             var jsonObject = JsonParser().parse(oggetto).asJsonObject
             valori = jsonObject.get(select).toString()
             if (select != "idutenti"){
                 valori = valori.dropLast(1)
                 valori = valori.drop(1)
             }
             Log.v("DBMSVal", valori)
         }catch(e : Exception){
             Log.v("Error", e.toString())
         }

     }
    runBlocking {
        job.join()
    }
    return valori

}

fun makeInsert(query: String){
    val call: Call<JsonObject> =  retrofit.insert(query)

    val job = GlobalScope.launch(Dispatchers.Default){
        try{
            call.execute()
        }catch (e : Exception){
            Log.v("DBMS", e.toString())
        }
    }
    runBlocking {
        job.join()
    }
}
fun makeUpdate(query: String){
    val call: Call<JsonObject> =  retrofit.update(query)

    val job = GlobalScope.launch(Dispatchers.Default){
        try{
            call.execute()
        }catch (e : Exception){
            Log.v("DBMS", e.toString())
        }
    }
    runBlocking {
        job.join()
    }
}

fun makeRemove(query: String){
    val call: Call<JsonObject> =  retrofit.remove(query)

    val job = GlobalScope.launch(Dispatchers.Default){
        try{
            call.execute()
            Log.v("DBMS", call.execute().body().toString())
        }catch (e : Exception){
            Log.v("DBMS", e.toString())
        }
    }
    runBlocking {
        job.join()
    }
}

fun makeQueryPrenotazioni(select: String, table: String, column: String, value : String): JsonArray?{
    var valori : JsonArray? = null
    val query = "SELECT $select From webmobile.$table where $column = $value "
    Log.v("Query", query)
    val call: Call<JsonObject> =  retrofit.select(query)

    val job = GlobalScope.launch(Dispatchers.Default){
        try{
            var oggetto = call.execute().body()?.get("queryset").toString()
            Log.v("DBMSObj", oggetto)
            valori = JsonParser().parse(oggetto).asJsonArray


        }catch(e : Exception){
            Log.v("Error", e.toString())
        }

    }
    runBlocking {
        job.join()
    }
    return valori
}
fun makeQueryInt(): Int{
    var valori  = 0
    val query = "SELECT max(idutenti) FROM webmobile.utenti;"
    Log.v("Query", query)
    val call: Call<JsonObject> =  retrofit.select(query)

    val job = GlobalScope.launch(Dispatchers.Default){
        try{
            var oggetto = call.execute().body()?.get("queryset").toString()
            Log.v("DBMSObj", oggetto)
            oggetto = oggetto.dropLast(1)
            oggetto = oggetto.drop(1)
            var jsonObject = JsonParser().parse(oggetto).asJsonObject
            valori = jsonObject.get("max(idutenti)").asInt
            Log.v("DBMSObj", valori.toString())

        }catch(e : Exception){
            Log.v("Error", e.toString())
        }

    }
    runBlocking {
        job.join()
    }
    return valori
}
fun makeQueryPagamento(query : String) : String{
    var chiamata = ""
    val call: Call<JsonObject> =  retrofit.select(query)

    val job = GlobalScope.launch(Dispatchers.Default){
        try{
            chiamata = call.execute().body()?.get("queryset").toString()
            Log.v("Chiamata", chiamata)
        }catch(e : Exception){
            Log.v("Error", e.toString())
        }

    }
    runBlocking {
        job.join()
    }
    return chiamata
}
fun makeQueryValore(query : String): Int{
    var valori  = 0
    Log.v("Query", query)
    val call: Call<JsonObject> =  retrofit.select(query)

    val job = GlobalScope.launch(Dispatchers.Default){
        try{
            var oggetto = call.execute().body()?.get("queryset").toString()
            Log.v("DBMSObj", oggetto)
            oggetto = oggetto.dropLast(1)
            oggetto = oggetto.drop(1)
            var jsonObject = JsonParser().parse(oggetto).asJsonObject
            valori = jsonObject.get("prenotato").asInt
            Log.v("DBMSObj", valori.toString())

        }catch(e : Exception){
            Log.v("Error", e.toString())
        }

    }
    runBlocking {
        job.join()
    }
    return valori
}

fun richiediMeteo(){
    val client = OkHttpClient()
    val request = Request.Builder()
        .url("https://api.open-meteo.com/v1/forecast?latitude=38.132&longitude=13.3356&current=temperature_2m,precipitation&timezone=Europe%2FBerlin&forecast_days=1")
        .get()
        .build()
    val job = GlobalScope.launch(Dispatchers.Default) {
        try {
            var oggetto = client.newCall(request).execute().body
            var response : okhttp3.Response = client.newCall(request).execute()
            var lista = StringBuilder(response.body!!.string())
            var meteo = lista.split("\":")
            var temp = meteo[meteo.size -2].split(",")
            var piog = meteo[meteo.size -1].split("}")
            temperatura = temp[0]
            precipitazioni = piog[0]
            Log.v("Meteo", oggetto.toString())
            Log.v("Meteo", response.body!!.string())
        } catch (e: Exception) {
            Log.v("Meteo", e.toString())
        }
    }
    runBlocking {
        job.join()
    }
}

