package com.muratipek.ak3_sharephotofirebase.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.LinearLayout
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.muratipek.ak3_sharephotofirebase.model.Post
import com.muratipek.ak3_sharephotofirebase.R
import com.muratipek.ak3_sharephotofirebase.adapter.FeedRecyclerAdapter
import com.muratipek.ak3_sharephotofirebase.databinding.ActivityFeedBinding

class FeedActivity : AppCompatActivity() {
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var database : FirebaseFirestore
    private lateinit var recyclerViewAdapter : FeedRecyclerAdapter
    private lateinit var binding : ActivityFeedBinding

    var postList = ArrayList<Post>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFeedBinding.inflate(layoutInflater)
        setContentView(binding.root)
        firebaseAuth = FirebaseAuth.getInstance()
        database = FirebaseFirestore.getInstance()

        getDatas()

        var layoutManager = LinearLayoutManager(this)
        binding.recyclerView.layoutManager = layoutManager
        recyclerViewAdapter = FeedRecyclerAdapter(postList)
        binding.recyclerView.adapter = recyclerViewAdapter
    }
    fun getDatas(){
        database.collection("Post").orderBy("date", Query.Direction.DESCENDING)
            .addSnapshotListener { snapshot, exception ->
            if(exception != null){
                Toast.makeText(this, exception.localizedMessage, Toast.LENGTH_LONG).show()
            }else{
                if(snapshot != null){
                    if(!snapshot.isEmpty){
                        val documents = snapshot.documents

                        postList.clear()
                        for(document in documents){
                            val useremail = document.get("useremail") as String
                            val usercomment = document.get("usercomment") as String
                            val imageurl = document.get("imageurl") as String

                            postList.add(Post(useremail, usercomment, imageurl))
                        }
                        recyclerViewAdapter.notifyDataSetChanged()
                    }
                }
            }
        }
    }
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.options_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.share_photo -> {
                val intent = Intent(this, PhotoShareActivity::class.java)
                startActivity(intent)
            }
            R.id.sign_out -> {
                firebaseAuth.signOut()
                val intent = Intent(this, SignInActivity::class.java)
                startActivity(intent)
                finish()
            }
            else -> {

            }
        }

        return super.onOptionsItemSelected(item)
    }
}