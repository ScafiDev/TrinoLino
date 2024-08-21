package com.example.trinolino

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.example.trinolino.Classi.Users
import com.example.trinolino.Fragment.AiutoFragment
import com.example.trinolino.Fragment.AndataFragment
import com.example.trinolino.Fragment.CercaFragment
import com.example.trinolino.Fragment.DestinazioneFragment
import com.example.trinolino.Fragment.LoginFragment
import com.example.trinolino.Fragment.OccasioniFragment
import com.example.trinolino.Fragment.PartenzaFragment
import com.example.trinolino.Fragment.ProfileFragment
import com.example.trinolino.Fragment.RitornoFragment
import com.example.trinolino.Fragment.TicketsFragment
import com.example.trinolino.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity(),
    PartenzaFragment.OnImageButtonClickListener,
    DestinazioneFragment.OnImageButtonClickListener,
    LoginFragment.OnImageButtonClickListener,
    LoginFragment.OnLoginSuccessListener,
    AndataFragment.OnImageButtonClickListener{

    private lateinit var binding: ActivityMainBinding
    private var selectedPartenzaStationName: String? = null
    private var selectedDestinazioneStationName: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Impostazioni iniziali della visibilità
        binding.ritorno.visibility = View.GONE
        binding.bigliettiframe.visibility = View.GONE
        binding.fra.visibility = View.GONE

        // Testi hardcoded
        binding.partenza.text = "Partenza"
        binding.destinazione.text = "Destinazione"
        binding.andata.text = "Andata"

        // Contatore per il numero di biglietti
        var i = 0

        // Intent dai fragment di destinazione e partenza
        val value = intent.getStringExtra("selected_city")
        val value2 = intent.getStringExtra("selectedback")

        // Controlli sugli intent
        if (value != null) {
            binding.partenza.text = value
        }

        if (value2 != null) {
            binding.destinazione.text = value2
        }

        // CheckBoxRitorno
        binding.checkBox.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                binding.ritorno.visibility = View.VISIBLE
            } else {
                binding.ritorno.visibility = View.GONE
                binding.ritorno.text= "Ritorno"
            }
        }

        // Gestione dei fragment
        binding.partenza.setOnClickListener {
            binding.fra.visibility = View.VISIBLE
            if (supportFragmentManager.findFragmentByTag(PartenzaFragment.TAG) == null) {
                supportFragmentManager.beginTransaction()
                    .replace(R.id.fra, PartenzaFragment(), PartenzaFragment.TAG)
                    .addToBackStack(null)
                    .commit()
                binding.base.visibility = View.GONE
            }
        }
        binding.destinazione.setOnClickListener {
            binding.fra.visibility = View.VISIBLE
            if (supportFragmentManager.findFragmentByTag(DestinazioneFragment.TAG) == null) {
                supportFragmentManager.beginTransaction()
                    .replace(R.id.fra, DestinazioneFragment(), DestinazioneFragment.TAG)
                    .addToBackStack(null)
                    .commit()
                binding.base.visibility = View.GONE
            }
        }

        // Fragment di login
        binding.login.setOnClickListener {
            binding.fra.visibility = View.VISIBLE
            if (supportFragmentManager.findFragmentByTag(LoginFragment.TAG) == null) {
                supportFragmentManager.beginTransaction()
                    .replace(R.id.fra, LoginFragment(), LoginFragment.TAG)
                    .addToBackStack(null)
                    .commit()
                binding.base.visibility = View.GONE
            }
        }
        binding.andata.setOnClickListener {
            if (supportFragmentManager.findFragmentByTag(AndataFragment.TAG) == null) {
                supportFragmentManager.beginTransaction()
                    .replace(R.id.fra, AndataFragment(), AndataFragment.TAG)
                    .addToBackStack(null)
                    .commit()
                binding.base.visibility = View.GONE
            }
        }
        binding.ritorno.setOnClickListener{
            if(supportFragmentManager.findFragmentByTag(RitornoFragment.TAG) == null){
                supportFragmentManager.beginTransaction()
                    .replace(R.id.fra, RitornoFragment(), RitornoFragment.TAG)
                    .addToBackStack(null)
                    .commit()
                binding.base.visibility= View.GONE
            }
        }
        binding.aiuto.setOnClickListener {
            if(supportFragmentManager.findFragmentByTag(AiutoFragment.TAG)==null){
                supportFragmentManager.beginTransaction()
                    .replace(R.id.fra, AiutoFragment(), AiutoFragment.TAG)
                    .addToBackStack(null)
                    .commit()
            }

        }



        // Comportamento per i biglietti
        binding.biglietti.setOnClickListener {
            if (i % 2 == 0) {
                binding.bigliettiframe.visibility = View.VISIBLE
                binding.cercabiglietti.visibility = View.GONE
            } else {
                binding.bigliettiframe.visibility = View.GONE
                binding.cercabiglietti.visibility = View.VISIBLE
            }
            i++
        }

        // Comportamento per i pulsanti + e -
        binding.plusB.setOnClickListener {
            val currentNumber = binding.bambiniN.text.toString().toIntOrNull() ?: 0
            binding.bambiniN.setText((currentNumber + 1).toString())
        }
        binding.minusB.setOnClickListener {
            val currentNumber = binding.bambiniN.text.toString().toIntOrNull() ?: 0
            if (currentNumber > 0) {
                binding.bambiniN.setText((currentNumber - 1).toString())
            }
        }

        binding.plusA.setOnClickListener {
            val currentNumber = binding.adultiN.text.toString().toIntOrNull() ?: 0
            binding.adultiN.setText((currentNumber + 1).toString())
        }
        binding.minusA.setOnClickListener {
            val currentNumber = binding.adultiN.text.toString().toIntOrNull() ?: 0
            if (currentNumber > 0) {
                binding.adultiN.setText((currentNumber - 1).toString())
            }
        }

        binding.conferma.setOnClickListener {
            binding.bigliettiframe.visibility = View.GONE
            i++
            binding.cercabiglietti.visibility = View.VISIBLE
        }
        binding.refresh.setOnClickListener {
            binding.adultiN.setText("1");
            binding.bambiniN.setText("0");
        }

        // Gestione del comportamento della navbar
        binding.bottomNavigationView.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.navigation_home -> {
                    clearBackStack()
                    true
                }
                R.id.navigation_tickets -> {
                    clearBackStack()
                    replaceFragment(TicketsFragment(), TicketsFragment.TAG)
                    true
                }
                R.id.navigation_occasioni -> {
                    clearBackStack()
                    replaceFragment(OccasioniFragment(), OccasioniFragment.TAG)
                    true
                }
                R.id.navigation_profile -> {
                    clearBackStack()
                    replaceFragment(ProfileFragment(), ProfileFragment.TAG)
                    true
                }
                else -> false
            }
        }

        binding.cercabiglietti.setOnClickListener {
            openCercaFragment()
        }

        // Aggiunto un listener per rilevare i cambiamenti nella back stack
        supportFragmentManager.addOnBackStackChangedListener {
            handleBackStackChanged()
        }
    }

    private fun handleBackStackChanged() {
        if (supportFragmentManager.backStackEntryCount == 0) {
            // La back stack è vuota, mostra la base
            binding.base.visibility = View.VISIBLE
            binding.fra.visibility = View.GONE
        } else {
            // La back stack non è vuota, nascondi la base
            binding.base.visibility = View.GONE
            binding.fra.visibility = View.VISIBLE
        }
    }


    private fun openCercaFragment() {
        val adultTickets = binding.adultiN.text.toString().toIntOrNull() ?: 1
        val childTickets = binding.bambiniN.text.toString().toIntOrNull() ?: 0
        val startStation = getSelectedPartenzaStationName()
        val endStation = getSelectedDestinazioneStationName()
        val dateAndata = getSelectedDateAndata()
        val dateRitorno = getSelectedDateRitorno()

        val useReturnCheckbox = binding.checkBox.isChecked
        if (useReturnCheckbox && !dateRitorno.isNullOrEmpty() && !dateAndata.isNullOrEmpty()) {
            if (dateRitorno.compareTo(dateAndata) < 0) {
                Toast.makeText(this, "Viaggi nel tempo non ammessi qui (Per ora), controlla la data di andata e ritorno", Toast.LENGTH_SHORT).show()
                return
            }
        }

        val fragment = CercaFragment.newInstance(
            startStation, endStation, dateAndata, dateRitorno, adultTickets, childTickets
        )

        supportFragmentManager.beginTransaction()
            .replace(R.id.fra, fragment, CercaFragment.TAG)
            .addToBackStack(null)
            .commit()
        binding.base.visibility = View.GONE
    }

    // Funzioni per settare i campi di partenza e destinazione
    fun setPartenzaCity(city: String) {
        binding.partenza.text = city
        selectedPartenzaStationName = city
    }

    fun setDestinazioneCity(city: String) {
        binding.destinazione.text = city
        selectedDestinazioneStationName = city
    }

    fun setDataAndata(data: String) {
        binding.andata.text = data
    }

    fun setDataRitorno(data: String) {
        binding.ritorno.text = data
    }


    // Funzioni per ottenere i valori selezionati
    fun getSelectedPartenzaStationName(): String? {
        return selectedPartenzaStationName
    }

    fun getSelectedDestinazioneStationName(): String? {
        return selectedDestinazioneStationName
    }

    fun getSelectedDateAndata(): String? {
        return binding.andata.text.toString()
    }
    fun getSelectedDateRitorno():String?{
        return binding.ritorno.text.toString()
    }
    fun goToHomePage() {
        supportFragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE)
    }

    // Override per il back
    override fun onImageButtonClick() {
        supportFragmentManager.popBackStack()
    }

    override fun onLoginSuccess(user: Users) {
        // Nascondi il pulsante "Accedi" e mostra il messaggio di benvenuto
        binding.login.visibility = View.GONE
        binding.welcometextView.text = "Ciao, ${user.nome}"
        binding.welcometextView.visibility = View.VISIBLE
        binding.base.visibility = View.VISIBLE
    }

    private fun replaceFragment(fragment: Fragment, tag: String) {
        if (supportFragmentManager.findFragmentByTag(tag) == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.fra, fragment, tag)
                .addToBackStack(null)
                .commit()
            binding.base.visibility = View.GONE
        }
    }

    override fun onBackPressed() {
        if (supportFragmentManager.backStackEntryCount > 0) {
            supportFragmentManager.popBackStack()
        } else {
            super.onBackPressed()
        }
    }
    fun clearBackStack() {
        val fragmentManager = supportFragmentManager
        while (fragmentManager.backStackEntryCount > 0) {
            fragmentManager.popBackStackImmediate()
        }
    }
}

