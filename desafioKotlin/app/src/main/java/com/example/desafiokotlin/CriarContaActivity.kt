package com.example.desafiokotlin

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.Toast
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import java.util.*

class CriarContaActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_criar_conta)

        initialize()
    }

    //elementos da interface
    private var etidPessoa: EditText ?= null
    private var etsaldo: EditText ?= null
    private var etlimiteDiario: EditText ?= null
    private var etcontaAtiva: CheckBox ?= null
    private var ettipoConta: EditText ?= null
    private var btn_criar_conta: Button?= null

    //banco
    private var db = Firebase.firestore
    private var tag = "CriarContaActivity"


    //variáveis globais
    private var idPessoa: Int ?= null
    private var saldo: Double ?= null
    private var limiteDiario: Double ?= null
    private var contaAtiva: Boolean ?= null
    private var tipoConta: Int ?= null


    private fun initialize(){
        etidPessoa = findViewById<EditText>(R.id.id_pessoa)
        etsaldo = findViewById<EditText>(R.id.saldo)
        etlimiteDiario = findViewById<EditText>(R.id.limiteSaqueDiario)
        etcontaAtiva = findViewById<CheckBox>(R.id.ativo)
        ettipoConta = findViewById<EditText>(R.id.tipoConta)

        btn_criar_conta = findViewById<Button>(R.id.btn_criar_conta)
        btn_criar_conta!!.setOnClickListener{ criarConta() }

    }


    private fun criarConta(){
        idPessoa = etidPessoa!!.getText().toString().toInt()
        saldo = etsaldo!!.getText().toString().toDouble()
        limiteDiario = etlimiteDiario!!.getText().toString().toDouble()
        contaAtiva = etcontaAtiva!!.isChecked()
        tipoConta = ettipoConta!!.getText().toString().toInt()

        if (TextUtils.isEmpty(idPessoa.toString()) || TextUtils.isEmpty(saldo.toString()) || TextUtils.isEmpty(limiteDiario.toString()) || TextUtils.isEmpty(contaAtiva.toString()) || TextUtils.isEmpty(tipoConta.toString())){
            Toast.makeText(this, "Entre com mais detalhes!", Toast.LENGTH_SHORT).show()
        }else{

            val date = Date()
            val cal = Calendar.getInstance()
            cal.time = date

            val conta = hashMapOf<String, Any?>(
                    "idPessoa" to idPessoa,
                    "saldo" to saldo,
                    "limiteDiario" to limiteDiario,
                    "contaAtiva" to contaAtiva,
                    "tipoConta" to tipoConta,
                    "dataCriacao" to cal.time
            )

            db.collection("contas")
                    .add(conta)
                    .addOnSuccessListener {documentReference->
                        Log.d(tag, "DocumentSnapshot added with ID: ${documentReference.id}\"")
                    }
                    .addOnFailureListener{e->
                        Log.w(tag, "Error adding document", e)
                    }


            Toast.makeText(this, "Informações preenchidas com sucesso!", Toast.LENGTH_SHORT).show()
            updateUi()

        }
    }

    private fun updateUi(){
        val intent = Intent(this@CriarContaActivity, MainActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        startActivity(intent)
    }



}