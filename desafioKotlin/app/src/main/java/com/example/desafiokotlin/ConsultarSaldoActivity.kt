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

class ConsultarSaldoActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_consultar_saldo)

        initialize()
    }

    //elementos da interface
    private var etidConta: EditText?= null
    private var btn_consultar: Button?= null
    private var btn_menu: Button ?= null

    //banco
    private var db = Firebase.firestore
    private var tag = "ConsultarSaldoActivity"


    //variaveis globais
    private var idConta: String ?= null


    private fun initialize(){
        etidConta = findViewById<EditText>(R.id.idConta)
        btn_consultar = findViewById<Button>(R.id.btn_consultar)
        btn_menu = findViewById<Button>(R.id.btn_menu)

        btn_consultar!!.setOnClickListener{ saldo() }
        btn_menu!!.setOnClickListener { menu() }

    }



    private fun saldo(){
        idConta = etidConta!!.getText().toString()

        if (TextUtils.isEmpty(idConta.toString())){
            Toast.makeText(this, "Entre com mais detalhes!", Toast.LENGTH_SHORT).show()
        }else{

            val docRef = db.collection("contas").document(idConta!!)

            docRef.get()
                .addOnSuccessListener { document ->
                    if (document != null) {
                        var saldo: Double = document.get("saldo").toString().toDouble()
                        var saldoUI = findViewById<TextView>(R.id.saldoConta)

                        saldoUI.setText(saldo.toString())
                    } else {
                        Toast.makeText(this, "Não existe uma conta com esse ID!", Toast.LENGTH_SHORT).show()
                    }
                }
        }

    }

    private fun menu(){
        val intent = Intent(this@ConsultarSaldoActivity, MainActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        startActivity(intent)
    }

}