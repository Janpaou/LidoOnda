package com.example.lidoonda

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity
data class User(val name: String,val cognome: String, val email: String, val password: String){
    @PrimaryKey(autoGenerate = true)
    var idQuery = makeQueryInt() + 1
}
data class Prenotazioni(val id: String,val tipo: Int, val ora: String, val data: String, val prenotato: Int)
data class Pagamento(val numero: String,val data: String, val cvv: String)

public class UserDetail(
    @SerializedName("idutenti")
    var utentiId: String?,

    @SerializedName("name")
    var name: String?,

    @SerializedName("cognome")
    var surname: String?,

    @SerializedName("email")
    var email: String?,

    @SerializedName("password")
    var password: String?
)







data class ImageData(var dataImage: Int)

data class DataClass(var dataImage: Int, var dataTitle: String, var dataButton: Int)
data class DataClassPrenotazioni(var dataImage: Int, var dataTitle: String, var dataButton: Int, var buttonAnnulla: Int, var buttonRimuovi: Int, var buttonPagamento: Int)

