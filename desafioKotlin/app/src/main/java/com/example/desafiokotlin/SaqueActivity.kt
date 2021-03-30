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
import java.util.*

class SaqueActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_saque)

        initialize()
    }

    //elementos da interface
    private var etidConta: EditText?= null
    private var etValorSaque: EditText?= null
    private var btn_sacar: Button?= null

    //banco
    private var db = Firebase.firestore
    private var tag = "SaqueActivity"


    //variaveis globais
    private var idConta: String ?= null
    private var ValorSaque: Double ?= null
    private var saldoAtual: Double ?= null


    private fun initialize(){
        etidConta = findViewById<EditText>(R.id.idConta)
        etValorSaque = findViewById<EditText>(R.id.valorSaque)
        btn_sacar = findViewById<Button>(R.id.btn_sacar)

        btn_sacar!!.setOnClickListener{ sacar() }

    }

    private fun sacar(){
        idConta = etidConta!!.getText().toString()
        ValorSaque = etValorSaque!!.getText().toString().toDouble()

        if (TextUtils.isEmpty(idConta.toString()) || TextUtils.isEmpty(ValorSaque.toString())){
            Toast.makeText(this, "Entre com mais detalhes!", Toast.LENGTH_SHORT).show()
        }else{

            val docRef = db.collection("contas").document(idConta!!)

            docRef.get().addOnSuccessListener{document->
                if(document != null && document.get("contaAtiva").toString().toBoolean() && document.get("saldo").toString().toDouble() >= ValorSaque!! && ValorSaque!! <= document.get("limiteDiario").toString().toDouble()){
                    db.runTransaction { transaction ->
                        val snapshot = transaction.get(docRef)
                        transaction.update(docRef,"saldo",snapshot.getDouble("saldo")!! - ValorSaque!!)
                    }.addOnSuccessListener {
                        val date = Date()
                        val cal = Calendar.getInstance()
                        cal.time = date

                        val transacao = hashMapOf<String, Any?>(
                            "idConta" to idConta,
                            "valor" to ValorSaque,
                            "dataTransacao" to cal.time
                        )

                        db.collection("transacoes")
                            .add(transacao)
                            .addOnSuccessListener {documentReference->
                                Log.d(tag, "DocumentSnapshot added with ID: ${documentReference.id}\"")
                            }
                            .addOnFailureListener{e->
                                Log.w(tag, "Error adding document", e)
                            }

                        Toast.makeText(this, "Saque realizado com sucesso", Toast.LENGTH_SHORT).show()
                        updateUi()
                    }
                  }else{
                    if (!document.get("contaAtiva").toString().toBoolean()){
                        Toast.makeText(this, "A conta está desativada.", Toast.LENGTH_SHORT).show()
                    }
                    if (document.get("saldo").toString().toDouble() < ValorSaque!!){
                        Toast.makeText(this, "Saldo insuficiente.", Toast.LENGTH_SHORT).show()
                    }

                    if (ValorSaque!! > document.get("limiteDiario").toString().toDouble()){
                        Toast.makeText(this, "Valor informado maior que o limite de saque diário.", Toast.LENGTH_SHORT).show()
                    }
                }

            }

        }

    }

    private fun updateUi(){
        val intent = Intent(this@SaqueActivity, MainActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        startActivity(intent)
    }
}