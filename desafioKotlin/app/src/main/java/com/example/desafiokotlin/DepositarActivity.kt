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

class DepositarActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_depositar)

        initialize()
    }

    //elementos da interface
    private var etidConta: EditText?= null
    private var etValorDeposito: EditText?= null
    private var btn_depositar: Button?= null

    //banco
    private var db = Firebase.firestore
    private var tag = "DepositarActivity"


    //variaveis globais
    private var idConta: String ?= null
    private var valorDeposito: Double ?= null


    private fun initialize(){
        etidConta = findViewById<EditText>(R.id.idConta)
        etValorDeposito = findViewById<EditText>(R.id.valorDeposito)
        btn_depositar = findViewById<Button>(R.id.btn_depositar)

        btn_depositar!!.setOnClickListener{ depositar() }

    }

    private fun depositar(){
        idConta = etidConta!!.getText().toString()
        valorDeposito = etValorDeposito!!.getText().toString().toDouble()

        if (TextUtils.isEmpty(idConta.toString()) || TextUtils.isEmpty(valorDeposito.toString())){
            Toast.makeText(this, "Entre com mais detalhes!", Toast.LENGTH_SHORT).show()
        }else{

            val docRef = db.collection("contas").document(idConta!!)


            docRef.get().addOnSuccessListener { document->
                if (document != null && document.get("contaAtiva").toString().toBoolean()){
                    db.runTransaction{transaction  ->
                        val snapshot = transaction.get(docRef)

                        val novoSaldo = snapshot.getDouble("saldo")!! + valorDeposito!!
                        transaction.update(docRef, "saldo", novoSaldo)
                    }.addOnSuccessListener {
                        val date = Date()
                        val cal = Calendar.getInstance()
                        cal.time = date

                        val transacao = hashMapOf<String, Any?>(
                            "idConta" to idConta,
                            "valor" to valorDeposito,
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
                        updateUi()
                        Toast.makeText(this, "Depósito feito com sucesso!", Toast.LENGTH_SHORT).show()

                    }



                }else{
                    if (!document.get("contaAtiva").toString().toBoolean()){
                        Toast.makeText(this, "A conta está desativada.", Toast.LENGTH_SHORT).show()
                    }
                }
            }



        }

    }

    private fun updateUi(){
        val intent = Intent(this@DepositarActivity, MainActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        startActivity(intent)
    }
}