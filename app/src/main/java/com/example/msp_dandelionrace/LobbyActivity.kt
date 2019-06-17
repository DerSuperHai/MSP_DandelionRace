package com.example.msp_dandelionrace

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.TextView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_game.*
import kotlinx.android.synthetic.main.activity_lobby.*

class LobbyActivity : AppCompatActivity() {

    var db = FirebaseFirestore.getInstance().collection("Games")
    lateinit var tv_lobby_gameName: TextView
    private var mAuth: FirebaseAuth? = null
    private lateinit var gameName:String
    private lateinit var hostMail:String


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_lobby)

        gameName = intent.getStringExtra("name")
        hostMail = intent.getStringExtra("hostMail")
        tv_lobby_gameName = findViewById<TextView>(R.id.tv_lobby_gameName)
        tv_lobby_gameName.setText(gameName)

        mAuth = FirebaseAuth.getInstance();
        if(mAuth?.currentUser?.email!=hostMail) {
            db.get().addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    for (document in task.result) {
                        if (document.get("hostMail") == hostMail && document.get("name") == gameName) {
                            db.document(document.id).update("guest", mAuth?.currentUser?.email)
                        }
                    }
                } else {
                    println("Error getting documents. " + task.exception)
                }
            }
        }
    }

    fun leaveGame(view : View){
        db.get().addOnCompleteListener { task ->
            if (task.isSuccessful) {
                for (document in task.result) {
                    if(document.get("hostMail")==hostMail && document.get("name")==gameName) {
                        db.document(document.id).update("guest", "")
                    }
                }
            } else {
                println("Error getting documents. "+ task.exception)
            }
        }
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
    }

}
