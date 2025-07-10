package com.example.lidoonda

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.core.view.isVisible
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import com.example.lidoonda.databinding.FragmentLoginBinding
import com.google.android.material.navigation.NavigationView


private lateinit var drawerLayout : DrawerLayout
private lateinit var binding: FragmentLoginBinding
public var loggato : Boolean = false
lateinit var menuOpzion: Menu
lateinit var login : MenuItem
lateinit var logout : MenuItem
lateinit var profile : MenuItem
lateinit var prenotazioni : MenuItem
lateinit var signup : MenuItem
var temperatura : String = ""
var precipitazioni : String = ""
var idUtente : String = ""


class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener  {
    @SuppressLint("RestrictedApi")
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        binding = FragmentLoginBinding.inflate(layoutInflater)
        setContentView(R.layout.main_activity)
        Log.v("Ordine", "1")
        richiediMeteo()
        abilitaTool(savedInstanceState)

    }



    override fun onNavigationItemSelected(item: MenuItem): Boolean {

        when(item.itemId){
            R.id.nav_home -> replaceFragment(HomeFragment())
            R.id.nav_login -> replaceFragment(LoginFragment())
            R.id.nav_logout -> logout()
            R.id.nav_galleria -> replaceFragment(GalleriaFragment())
            R.id.nav_profile -> replaceFragment(ProfileFragment())
            R.id.nav_signup -> replaceFragment(SignupFragment())
            R.id.nav_servizi -> replaceFragment(ServiziFragment())
            R.id.nav_contatti -> replaceFragment(ContattiFragment())
            R.id.nav_prenotazioni -> replaceFragment(ListaPrenotazioniFragment())
        }

        drawerLayout.closeDrawer(GravityCompat.START)
        return true
    }

    private fun replaceFragment(fragment: Fragment){
        val transaction : FragmentTransaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.fragment_container, fragment)
        transaction.commit()
    }

    override fun onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)){
            drawerLayout.closeDrawer(GravityCompat.START)
        }else{
            onBackPressedDispatcher.onBackPressed()
        }
    }

    fun loginBottone(view: View) {
        val userV = findViewById<EditText>(R.id.inputNome).text.toString()
        var password = makeQuerySelect("password", "utenti", "email", "\"$userV\"")
        idUtente= makeQuerySelect("idutenti", "utenti", "email", "\"$userV\"")


        val pwV = findViewById<EditText>(R.id.inputPW).text.toString()
        Log.v("Login", "$pwV\n$password")
        if (pwV == password /*&& pwV != "" */){
            findViewById<EditText>(R.id.inputNome).setBackgroundResource(0)
            findViewById<EditText>(R.id.inputPW).setBackgroundResource(0)
            findViewById<ProgressBar>(R.id.progressBar).visibility = View.VISIBLE
            binding.progressBar.visibility = View.VISIBLE

            loggato = true
            findViewById<TextView>(R.id.user_name).text = userV

            replaceFragment(HomeFragment())

        }else{
            findViewById<ProgressBar>(R.id.progressBar).visibility = View.GONE
            findViewById<EditText>(R.id.inputNome).setBackgroundResource(R.drawable.bordorosso)
            findViewById<EditText>(R.id.inputPW).setBackgroundResource(R.drawable.bordorosso)
            findViewById<TextView>(R.id.textMessaggio).text = "Dati Errati"
        }
        rimuoviTastiera()
    }
    fun signupBottone(view: View){
        var nomeReg = findViewById<EditText>(R.id.nomeReg).text.toString()
        var cognomeReg = findViewById<EditText>(R.id.cognomeReg).text.toString()
        var emailReg = findViewById<EditText>(R.id.emailReg).text.toString()
        var passReg = findViewById<EditText>(R.id.passwordReg).text.toString()


        if (!emailReg.contains("@", true) or !emailReg.contains(".", true)){
            Toast.makeText(applicationContext, "Inserire email corretta", Toast.LENGTH_SHORT).show()
        }else if (passReg.length < 8){
            Toast.makeText(applicationContext, "Inserire Password corretta", Toast.LENGTH_SHORT).show()
        }else{
            var utente = User(nomeReg, cognomeReg, emailReg, passReg)
            Log.v("utente", utente.idQuery.toString())
            makeInsert("insert into utenti values('${utente.idQuery}', '${utente.name}', '${utente.cognome}', '${utente.email}', '${utente.password}')")
            rimuoviTastiera()
            Toast.makeText(applicationContext, "Utente inserito correttamente", Toast.LENGTH_SHORT).show()
        }
    }

    private fun logout(){
        loggato = false
        findViewById<TextView>(R.id.user_name).text = ""
        Toast.makeText(applicationContext, "Avvenuto logout", Toast.LENGTH_SHORT).show()
        replaceFragment(HomeFragment())
    }

     fun abilitaTool(savedInstanceState: Bundle?){
        drawerLayout= findViewById<DrawerLayout>(R.id.drawer_layout)
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.title = "Lido Onda"
        supportActionBar?.subtitle = "$temperatura C° - Precipitazioni: $precipitazioni"


        val navigationView = findViewById<NavigationView>(R.id.nav_view)
        navigationView.setNavigationItemSelectedListener(this)

        val toggle = ActionBarDrawerToggle(this, drawerLayout, toolbar,R.string.open_nav, R.string.close_nav)

        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()
        if (savedInstanceState == null){
            replaceFragment(HomeFragment())
            navigationView.setCheckedItem(R.id.nav_home)
        }
        login = navigationView.menu.findItem(R.id.nav_login)
        logout = navigationView.menu.findItem(R.id.nav_logout)
        profile = navigationView.menu.findItem(R.id.nav_profile)
        prenotazioni = navigationView.menu.findItem(R.id.nav_prenotazioni)
         signup = navigationView.menu.findItem(R.id.nav_signup)
    }
    fun showMap(view: View) {
        var intent  = Intent(this, MapsActivity::class.java)
        startActivity(intent)
    }
    fun rimuoviTastiera(){
        val view: View? = this.currentFocus

        // on below line checking if view is not null.
        if (view != null) {
            // on below line we are creating a variable
            // for input manager and initializing it.
            val inputMethodManager =
                getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager

            // on below line hiding our keyboard.
            inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
        }
    }
    fun dettagliButton(view: View){
        cambiaBottoni()
    }
    fun cambiaBottoni(){
        var buttonDettagli: Button = findViewById(R.id.rvButtonPrenotazioni)
        if (buttonDettagli.isVisible){
            findViewById<ImageView>(R.id.rvImagePrenotazioni).visibility = View.INVISIBLE
            findViewById<TextView>(R.id.rvTitlePrenotazioni).visibility = View.INVISIBLE
            buttonDettagli.visibility = View.INVISIBLE
            findViewById<Button>(R.id.rvButtonPagamento).visibility = View.VISIBLE
            findViewById<Button>(R.id.rvButtonRimuovi).visibility = View.VISIBLE
            findViewById<Button>(R.id.rvButtonAnnulla).visibility = View.VISIBLE

        }else{
            findViewById<ImageView>(R.id.rvImagePrenotazioni).visibility = View.VISIBLE
            findViewById<TextView>(R.id.rvTitlePrenotazioni).visibility = View.VISIBLE
            buttonDettagli.visibility = View.VISIBLE
            findViewById<Button>(R.id.rvButtonPagamento).visibility = View.INVISIBLE
            findViewById<Button>(R.id.rvButtonRimuovi).visibility = View.INVISIBLE
            findViewById<Button>(R.id.rvButtonAnnulla).visibility = View.INVISIBLE
        }
    }
    fun fragmentSignup(view: View){
        replaceFragment(SignupFragment())
    }
    fun fragmentLogin(view: View){
        replaceFragment(LoginFragment())
    }
    fun modificaBottone(view: View){
        var nome = findViewById<TextView>(R.id.nomeProf).text
        var cognome = findViewById<TextView>(R.id.cognomeProf).text
        var mail = findViewById<TextView>(R.id.emailProf).text
        var password = findViewById<TextView>(R.id.passwordProf).text
        makeUpdate("update utenti set nome='$nome',cognome='$cognome',email='$mail',password='$password' where idutenti='$idUtente'")
        rimuoviTastiera()
        Toast.makeText(applicationContext, "Modifica avvenuta con successo", Toast.LENGTH_SHORT).show()
    }
    fun pagamentoFragment(view: View){
        replaceFragment(PagamentoFragment())
    }
    fun inserisciPagamento(view: View){
        try {
            var numero = findViewById<TextView>(R.id.numeroPagamento).text.toString()
            var data = findViewById<TextView>(R.id.pickDate).text.toString()
            var cvv = findViewById<TextView>(R.id.cvvpick).text.toString()
            var mese = data.subSequence(0,2).toString().toInt()
            Log.v("mese" , mese.toString())
            var anno = data.subSequence(3,5).toString().toInt()
            Log.v("anno" , anno.toString())
            if (mese in 1..12 && anno >= 24){
                makeInsert("insert into pagamento values('$idUtente', '$numero', '$data', '$cvv')")
                Toast.makeText(applicationContext, "Inserimento avvenuto con successo", Toast.LENGTH_SHORT).show()
                rimuoviTastiera()
            }else{
                Toast.makeText(applicationContext, "Inserire data corretta", Toast.LENGTH_SHORT).show()
            }

        }catch (e : Exception){
            Toast.makeText(applicationContext, "Inserire valori corretti", Toast.LENGTH_SHORT).show()
        }

    }
    fun modificaPagamento(view: View) {
        try {
            var numero = findViewById<TextView>(R.id.numeroPagamento).text.toString()
            var data = findViewById<TextView>(R.id.pickDate).text.toString()
            var cvv = findViewById<TextView>(R.id.cvvpick).text.toString()
            var mese = data.subSequence(0,2).toString().toInt()
            Log.v("mese" , mese.toString())
            var anno = data.subSequence(3,5).toString().toInt()
            Log.v("anno" , anno.toString())
            if (mese in 1..12 && anno >= 24){
                makeUpdate("update pagamento set numero='$numero',data='$data',cvv='$cvv' where idutente='$idUtente'")
                replaceFragment(ProfileFragment())
                Toast.makeText(applicationContext, "Inserimento avvenuto con successo", Toast.LENGTH_SHORT).show()
            }else{
                Toast.makeText(applicationContext, "Inserire data corretta", Toast.LENGTH_SHORT).show()
            }

        }catch (e : Exception){
            Toast.makeText(applicationContext, "Inserire valori corretti", Toast.LENGTH_SHORT).show()
        }
    }
    fun rimuoviPagamento(view : View){
        makeRemove("delete from pagamento where idutente='$idUtente'")
        Toast.makeText(applicationContext,  "Metodo di pagamento rimosso con successo", Toast.LENGTH_SHORT).show()
        replaceFragment(ProfileFragment())
    }
    fun fragmentContatti(view: View){
        replaceFragment(ContattiFragment())
    }

}



