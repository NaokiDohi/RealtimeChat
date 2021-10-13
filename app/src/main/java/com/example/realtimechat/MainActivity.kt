package com.example.realtimechat

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.example.realtimechat.databinding.ActivityMainBinding
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var database: FirebaseDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        database = Firebase.database

        database.getReference("messages").addValueEventListener(
            object : ValueEventListener{
                // 初回実行時と値変更時にコール
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    for (postSnapshot in dataSnapshot.children){
                        val key = postSnapshot.key
                        val value = postSnapshot.value
                        Log.d("called value", "key=${key}, value=${value}")
                    }
                }

                // サーバー側のエラーなど、処理がうまくいかなかったときに呼ばれる
                override fun onCancelled(databaseError: DatabaseError) {
                    Log.w("messages", "onCancelled", databaseError.toException())
                }
            }
        )
    }

    override fun onResume() {
        super.onResume()
        // System.currentTimeMillis()は現在時刻をUNIXミリ秒(1970年1月1日0時から何ミリ秒経ったか)で取得するメソッド
//        database.getReference("messages").child(System.currentTimeMillis().toString()).setValue("Hello World!!")
        database.getReference("messages")
            .child(System.currentTimeMillis().toString())
            .setValue(ChatMessage("Naoki Dohi", "Hello World!!"))

        Log.d("hoge", "firebase is setting now")
    }
}

//{
//    "(略)-default-rtdb": {
//        "messages": { ← getReference("messages")
//            1630377509526: ← .child(System.currentTimeMillis().toString())
//                "こんにちは！", ← .setValue("こんにちは！")
//        }
//    }
//}

//FirebaseDatabase.getReference(String)	データベースの参照を取得する。引数を指定するとそのパスの参照を取得する
//DatabaseReference.child(String)	    引数で指定したパスの参照を取得する。まだそのパスは存在していなくてもよい
//DatabaseReference.setValue(Object)	現在のパス(参照)に引数の内容を保存する。
//                                      そのパスに値がまだ入っていない場合は新規で追加され、入っている場合は上書きされる。nullを指定すると削除扱い