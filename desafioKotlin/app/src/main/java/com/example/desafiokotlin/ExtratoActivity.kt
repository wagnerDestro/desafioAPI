package com.example.desafiokotlin

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.ExpandableListView
import android.widget.Toast
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import java.text.SimpleDateFormat
import java.util.*

class ExtratoActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_extrato)
        initialize()
    }

    //elementos da interface
    private var etidConta: EditText?= null
    private var btn_extrato: Button?= null
    private var btn_menu: Button?= null

    //banco
    private var db = Firebase.firestore
    private var tag = "ExtratoActivity"


    //variaveis globais
    private var idConta: String ?= null


    private fun initialize(){
        etidConta = findViewById<EditText>(R.id.idConta)
        btn_extrato = findViewById<Button>(R.id.btn_extrato)
        btn_menu = findViewById<Button>(R.id.btn_menu)

        btn_extrato!!.setOnClickListener{ extrato() }
        btn_menu!!.setOnClickListener { menu() }

    }



    private fun extrato(){
        idConta = etidConta!!.getText().toString()

        if (TextUtils.isEmpty(idConta.toString())){
            Toast.makeText(this, "Entre com mais detalhes!", Toast.LENGTH_SHORT).show()
        }else{

            db.collection("transacoes")
                .whereEqualTo("idConta", idConta!!).get()
                .addOnSuccessListener{
                    documents->
                    for(document in documents){
                        val timestamp = document.get("dataTransacao") as com.google.firebase.Timestamp
                        val milliseconds = timestamp.seconds * 1000 + timestamp.nanoseconds / 1000000
                        val sdf = SimpleDateFormat("dd/MM/yyyy")
                        val netDate = Date(milliseconds)
                        val date = sdf.format(netDate).toString()

                        //val registro = listOf(document.id, document.getString("idConta"), document.getDouble("valor"), date)


                        Log.d(tag, "${document.id} => " +
                                "ID da Conta: ${document.getString("idConta")}\n" +
                                "Valor: ${document.getDouble("valor")}\n" +
                                "Data transação: $date")
                    }
                }

        }

    }

    private fun menu(){
        val intent = Intent(this@ExtratoActivity, MainActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        startActivity(intent)
    }
}