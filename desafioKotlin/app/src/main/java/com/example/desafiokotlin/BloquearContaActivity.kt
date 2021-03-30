package com.example.desafiokotlin

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class BloquearContaActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_bloquear_conta)
        initialize()
    }

    //elementos da interface
    private var etidConta: EditText?= null
    private var btn_bloquear: Button?= null
    private var btn_menu: Button?= null

    //banco
    private var db = Firebase.firestore
    private var tag = "BloquearContaActivity"


    //variaveis globais
    private var idConta: String ?= null


    private fun initialize(){
        etidConta = findViewById<EditText>(R.id.idConta)
        btn_bloquear = findViewById<Button>(R.id.btn_bloquear)
        btn_menu = findViewById<Button>(R.id.btn_menu)

        btn_bloquear!!.setOnClickListener{ bloquear() }
        btn_menu!!.setOnClickListener { menu() }

    }



    private fun bloquear(){
        idConta = etidConta!!.getText().toString()

        if (TextUtils.isEmpty(idConta.toString())){
            Toast.makeText(this, "Entre com mais detalhes!", Toast.LENGTH_SHORT).show()
        }else{

            val docRef = db.collection("contas").document(idConta!!)

            docRef.update("contaAtiva", false)
            Toast.makeText(this, "Conta bloqueada", Toast.LENGTH_SHORT).show()
        }

    }

    private fun menu(){
        val intent = Intent(this@BloquearContaActivity, MainActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        startActivity(intent)
    }
}