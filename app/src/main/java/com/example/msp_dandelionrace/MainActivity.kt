package com.example.msp_dandelionrace

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.ListView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore


class MainActivity : AppCompatActivity() {

    private var mAuth: FirebaseAuth? = null

    var db = FirebaseFirestore.getInstance().collection("Games")
    lateinit var listView:ListView
    lateinit var et_gameName: EditText


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        mAuth = FirebaseAuth.getInstance();
        listView = findViewById<ListView>(R.id.lv_games)
        et_gameName = findViewById<EditText>(R.id.et_gameName)
        refreshEmbedded()

    }

    fun hostGame(view : View){
        val user = FirebaseAuth.getInstance().currentUser
        if (user != null) {
            val email = user.email
            val test = GameInstance("Test", email!!)
            val dataMap = HashMap<String, Any>()
            dataMap.put("name", test.name)
            dataMap.put("hostMail", test.hostMail)

            db.add(dataMap)
                .addOnSuccessListener { documentReference ->
                    println("DocumentSnapshot added with ID: " + documentReference.id
                    )
                }
                .addOnFailureListener { e -> println("Error adding document: "+ e) }
            val intent = Intent(this, LobbyActivity::class.java)
            intent.putExtra("name", test.name)
            intent.putExtra("hostMail", test.hostMail)
            startActivity(intent)
        }
    }

    fun logout(view : View){
        mAuth?.signOut()
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
    }

    fun refresh(view : View){
        refreshEmbedded()
    }

    fun refreshEmbedded(){
        val listItems = arrayListOf<GameInstance>()

        db.get().addOnCompleteListener { task ->
            if (task.isSuccessful) {
                for (document in task.result) {
                    println(document.get("hostMail"))
                    println(document.get("name"))
                    val game = GameInstance(document.get("name").toString(), document.get("hostMail").toString())
                    listItems.add(game)
                }
                val adapter = GameAdapter(this, listItems)
                listView.adapter = adapter
            } else {
                println("Error getting documents. "+ task.exception)
            }
        }
    }

}
