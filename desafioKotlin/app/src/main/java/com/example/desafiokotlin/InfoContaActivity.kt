package com.example.desafiokotlin

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Window
import android.view.WindowManager
import android.os.Build
import android.text.TextUtils
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import java.sql.Timestamp
import java.text.SimpleDateFormat
import java.util.*


class InfoContaActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_info_conta)

        initialize()
    }

    //elementos da interface
    private var etidConta: EditText?= null
    private var btn_consultar: Button?= null
    private var btn_menu: Button ?= null

    //banco
    private var db = Firebase.firestore
    private var tag = "InfoContaActivity"


    //variaveis globais
    private var idConta: String ?= null


    private fun initialize(){
        etidConta = findViewById<EditText>(R.id.idConta)
        btn_consultar = findViewById<Button>(R.id.btn_consultar)
        btn_menu = findViewById<Button>(R.id.btn_menu)

        btn_consultar!!.setOnClickListener{ consultar() }
        btn_menu!!.setOnClickListener { menu() }

    }



    private fun consultar(){
        idConta = etidConta!!.getText().toString()

        if (TextUtils.isEmpty(idConta.toString())){
            Toast.makeText(this, "Entre com mais detalhes!", Toast.LENGTH_SHORT).show()
        }else{

            val docRef = db.collection("contas").document(idConta!!)

            docRef.get()
                .addOnSuccessListener { document ->
                    if (document != null) {
                        var contaAtiva: Boolean = document.get("contaAtiva").toString().toBoolean()
                        var contaAtivaUI = findViewById<TextView>(R.id.contaAtiva)

                        if (contaAtiva)
                            contaAtivaUI.setText("Sim")
                        else
                            contaAtivaUI.setText("Não")

                        var saldo: Double = document.get("saldo").toString().toDouble()
                        var saldoUI = findViewById<TextView>(R.id.saldoConta)
                        saldoUI.setText(saldo.toString())

                        var idUsuario: Int = document.get("idPessoa").toString().toInt()
                        var idUsuarioUI = findViewById<TextView>(R.id.idUsuario)
                        idUsuarioUI.setText(idUsuario.toString())

                        var limiteSaque: Double = document.get("limiteDiario").toString().toDouble()
                        var limiteSaqueUI = findViewById<TextView>(R.id.limiteDiario)
                        limiteSaqueUI.setText(limiteSaque.toString())

                        var tipoConta: Int = document.get("tipoConta").toString().toInt()
                        var tipoContaUI = findViewById<TextView>(R.id.tipoConta)
                        tipoContaUI.setText(tipoConta.toString())

                        //converte o objeto timestamp do banco para uma string de dia no formato dd/mm/aaaa
                        val timestamp = document.get("dataCriacao") as com.google.firebase.Timestamp
                        val milliseconds = timestamp.seconds * 1000 + timestamp.nanoseconds / 1000000
                        val sdf = SimpleDateFormat("dd/MM/yyyy")
                        val netDate = Date(milliseconds)
                        val date = sdf.format(netDate).toString()


                        var dataUI = findViewById<TextView>(R.id.dtaCriacao)
                        dataUI.setText(date)

                    } else {
                        Toast.makeText(this, "Não existe uma conta com esse ID!", Toast.LENGTH_SHORT).show()
                    }
                }
        }

    }

    private fun menu(){
        val intent = Intent(this@InfoContaActivity, MainActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        startActivity(intent)
    }




}