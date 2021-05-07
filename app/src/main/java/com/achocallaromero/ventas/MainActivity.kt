package com.achocallaromero.ventas

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Button
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.achocallaromero.ventas.recycler.Producto
import com.achocallaromero.ventas.recycler.RecyclerAdapter
import okhttp3.*
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import java.io.IOException

class MainActivity : AppCompatActivity() {
    lateinit var boton: Button

    // recycler
    lateinit var productos: ArrayList<Producto>
    lateinit var linearLayoutManager: LinearLayoutManager
    lateinit var adapter: RecyclerAdapter
    lateinit var recyclerView: RecyclerView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        configurarRecycler()


        // Loguearse
        val intent = Intent(this, Login::class.java)
        // startActivity(intent)
        startActivityForResult(intent, 123)




    }

    fun obtenerProductos(url: String){
        funcionGET(url, object: Callback{
            override fun onFailure(call: Call, e: IOException) {
                // Cuando Falla el Servidor
                println("Error")
            }

            override fun onResponse(call: Call, response: Response) {
                // Funciona cuando el Servidor Responde Correctamente
                var responseData = response.body?.string()
                runOnUiThread {
                    try {
                        var json = JSONObject(responseData)
                        println("Productos Obtenidos COrrectamente")
                        println(json["data"])
                        var array: JSONArray = json["data"] as JSONArray
                        var i=0
                        while (i < array.length()){
                            var prod = array.getJSONObject(i)
                            productos.add(
                                    Producto(
                                            prod["codigo"].toString(),
                                            prod["nombre"].toString(),
                                            prod["descripcion"].toString(),
                                            prod["precio"].toString().toDouble(),
                                            prod["url_imagen"].toString(),
                                            prod["like"].toString().toInt(),
                                            prod["dislike"].toString().toInt()

                                    )
                            )
                            i = i +1
                        }
                        adapter.notifyDataSetChanged()

                    } catch (e: JSONException){
                        e.printStackTrace()
                    }
                }
            }

        })
    }

    fun funcionGET(url: String, callback: Callback): Call {
        val request = Request.Builder()
                .url(url)
                .addHeader("Content-Type","application/json")
                .addHeader("Authorization","Bearer "+GlobalVar.token)
                .build()
        var client = OkHttpClient()
        val call = client.newCall(request)
        call.enqueue(callback)
        return call
    }

    fun configurarRecycler(){
        productos = ArrayList()
        //productos.add( Producto("01","prod1", "Descripcion",2.5,"url",20,3) )
        //productos.add( Producto("02","prod2", "Descripcion2",4.5,"url",40,13) )
        //productos.add( Producto("03","prod3", "Descripcion2",3.5,"url",30,5) )

        recyclerView = findViewById(R.id.recyclerView)
        linearLayoutManager = LinearLayoutManager(this)
        recyclerView.layoutManager = linearLayoutManager
        adapter = RecyclerAdapter(this, productos)
        recyclerView.adapter = adapter

        

        adapter.notifyDataSetChanged()

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.menu_main, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when( item.itemId ){
            R.id.login -> {
                val intent = Intent(this, Login::class.java)
                // startActivity(intent)
                startActivityForResult(intent, 123)
                return true
            }
            R.id.logout -> {

                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if( requestCode == 123 ){
            if( resultCode == Activity.RESULT_OK ){
                println("MAINACTIVITY - LOGIN CORRECTO")
                println("TOKEN DESDE MAIN: "+ GlobalVar.token)

                obtenerProductos("http://200.90.150.34:38000/api/productos")
            } else{
                println("MAINACTIVITY - LOGIN FALLIDO")
            }
        }
    }




}