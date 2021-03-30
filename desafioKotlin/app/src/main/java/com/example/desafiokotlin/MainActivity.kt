package com.example.desafiokotlin

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.content.Intent
import android.os.Handler
import android.widget.Button


class MainActivity : AppCompatActivity() {

    private var btnCriarUsuario: Button?= null
    private var btnCriarConta: Button ?= null
    private var btnFazerDeposito: Button?= null
    private var btnConsultarSaldo: Button ?= null
    private var btnFazerSaque: Button?= null
    private var btnBloquearConta: Button ?= null
    private var btnExtratoConta: Button?= null
    private var btnInformacoes: Button ?= null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initialize()
    }

    private fun initialize(){
        btnCriarUsuario = findViewById<Button>(R.id.btn_criarUsuario)
        btnCriarConta = findViewById<Button>(R.id.btn_criar_conta)
        btnFazerDeposito = findViewById<Button>(R.id.btn_deposito)
        btnConsultarSaldo = findViewById<Button>(R.id.btn_consultarSaldo)
        btnFazerSaque = findViewById<Button>(R.id.btn_saque)
        btnBloquearConta = findViewById<Button>(R.id.btn_bloquear)
        btnExtratoConta = findViewById<Button>(R.id.btn_extrato)
        btnInformacoes = findViewById<Button>(R.id.btn_infoConta)


        btnCriarUsuario!!.setOnClickListener{ criarPessoa() }
        btnCriarConta!!.setOnClickListener{ criarConta() }
        btnFazerDeposito!!.setOnClickListener{ depositar() }
        btnConsultarSaldo!!.setOnClickListener{ saldo() }
        btnFazerSaque!!.setOnClickListener{ sacar() }
        btnBloquearConta!!.setOnClickListener{ bloquear() }
        btnExtratoConta!!.setOnClickListener{ extrato() }
        btnInformacoes!!.setOnClickListener{ infoConta() }

    }

    fun criarPessoa(){
        val intent = Intent(this, CriarPessoaActivity::class.java)
        intent.change()

    }

    fun criarConta(){
        val intent = Intent(this, CriarContaActivity::class.java)
        intent.change()
    }

    fun depositar(){
        val intent = Intent(this, DepositarActivity::class.java)
        intent.change()
    }

    fun sacar(){
        val intent = Intent(this, SaqueActivity::class.java)
        intent.change()
    }

    fun saldo(){
        val intent = Intent(this, ConsultarSaldoActivity::class.java)
        intent.change()
    }

    fun bloquear(){
        val intent = Intent(this, BloquearContaActivity::class.java)
        intent.change()
    }

    fun infoConta(){
        val intent = Intent(this, InfoContaActivity::class.java)
        intent.change()
    }

    fun extrato(){
        val intent = Intent(this, ExtratoActivity::class.java)
        intent.change()
    }

    fun Intent.change(){
        startActivity(this)
        finish()
    }

}