package com.example.desafiokotlin

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.widget.Button
import android.widget.DatePicker
import android.widget.EditText
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import java.text.SimpleDateFormat
import java.util.*


class CriarPessoaActivity : AppCompatActivity() {


    //elementos da interface do usuário
    private var etNome: EditText? = null
    private var etCpf: EditText? = null
    private var etDtNascimento : DatePicker? = null
    private var botaoCriarConta : Button?= null


    //referencias ao banco de dados
    /*
    private var mDatabaseReference : DatabaseReference? = null
    private var mDatabase : FirebaseDatabase ?= null
    private var mAuth : FirebaseAuth ?= null
    */

    private var db = Firebase.firestore

    private var tag = "CriarPessoaActivity"

    //variáveis globais
    private var nome : String ?= null
    private var cpf : String ?= null
    private var dtNascimento: DatePicker ?= null

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_criar_pessoa)

        initialize()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun initialize(){
        //pegando as informações pelo ID da interface

        etNome = findViewById<EditText>(R.id.nome)
        etCpf = findViewById<EditText>(R.id.cpf)
        etDtNascimento = findViewById<DatePicker>(R.id.dtNascimento)
        botaoCriarConta = findViewById<Button>(R.id.btn_register_account)


        //ação que acontecerá quando o botão ser clicado
        botaoCriarConta!!.setOnClickListener{ createNewAccount() }

    }


    @RequiresApi(Build.VERSION_CODES.O)
    private fun createNewAccount() {
        nome = etNome!!.getText().toString()
        cpf = etCpf!!.getText().toString()
        dtNascimento = etDtNascimento


        if (TextUtils.isEmpty(nome) || TextUtils.isEmpty(cpf)){
            Toast.makeText(this, "Entre com mais detalhes!", Toast.LENGTH_SHORT).show()
        }else{

            //converter a data recebida pelo DatePicker para o formato timestamp
            val calendar: Calendar = Calendar.getInstance()
            calendar.set(dtNascimento!!.year, dtNascimento!!.month, dtNascimento!!.dayOfMonth+1)

            val sdf = SimpleDateFormat("dd-MM-yyyy")
            val formatedDate: String = sdf.format(calendar.getTime())
            val date: Date = sdf.parse(formatedDate)

            val user = hashMapOf<String, Any?>(
                    "nome" to nome,
                    "cpf" to cpf,
                    "dtNascimento" to date
            )

            db.collection("pessoas")
                    .add(user)
                    .addOnSuccessListener {documentReference->
                        Log.d(tag, "DocumentSnapshot added with ID: ${documentReference.id}\"")
                    }
                    .addOnFailureListener{e->
                        Log.w(tag, "Error adding document", e)
                    }

            Toast.makeText(this, "Informações preenchidas com sucesso!", Toast.LENGTH_SHORT).show()
            updateUserInfoAndUI()
        }

    }

    private fun updateUserInfoAndUI(){
        //redireciona pra tela principal
        val intent = Intent(this@CriarPessoaActivity, MainActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        startActivity(intent)
    }
}