package com.achocallaromero.ventas

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONException
import org.json.JSONObject
import java.io.IOException


class Login : AppCompatActivity() {
    lateinit var correo: EditText
    lateinit var password: EditText
    lateinit var error: EditText
    lateinit var login: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        correo = findViewById(R.id.correo)
        password = findViewById(R.id.password)
        //error = findViewById(R.id.error)
        login = findViewById(R.id.login)

        login.setOnClickListener {
            // probrarGet("https://jsonplaceholder.typicode.com/posts/1")
            ingresar( correo.text.toString(), password.text.toString() )
        }
    }

    fun ingresar(email: String,password: String){
        val url = "http://200.90.150.34:38000/api/login"
        try {
            var json = JSONObject()
            json.put("email",email)
            json.put("password",password)
            funcionPost(url, json.toString(), object: Callback{
                override fun onFailure(call: Call, e: IOException) {
                    println("Error")
                    e.printStackTrace()
                }

                override fun onResponse(call: Call, response: Response) {
                    var responseData = response.body?.string()
                    runOnUiThread {
                        try {
                            var json = JSONObject(responseData)
                            println(json)
                            if(json["res"] as Boolean){
                                println("Logueado correctamente, Guardar TOKEN")
                                println(json["token"])

                                GlobalVar.token = json["token"] as String

                                val intent = Intent()
                                setResult(Activity.RESULT_OK, intent)
                                finish()

                            } else{
                                println("Correo y Password Incorrecto")
                                val intent = Intent()
                                setResult(Activity.RESULT_CANCELED, intent)
                                finish()
                            }
                        } catch (e: JSONException){
                            e.printStackTrace()
                        }
                    }
                }

            })
        } catch (e: JSONException){
            e.printStackTrace()
        }
    }

    fun probrarGet(url: String){
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
                        println("Servidor Respondio COrrectamente")
                        println(json)
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
                .build()
        var client = OkHttpClient()
        val call = client.newCall(request)
        call.enqueue(callback)
        return call
    }

    fun funcionPost(url: String, json: String, callback: Callback): Call {
        val JSON = "application/json; charset=utf-8".toMediaType()

        val body = json.toRequestBody(JSON)
        //val body: RequestBody = create(JSON, json)
        val request: Request = Request.Builder()
                .url(url)
                .post(body)
                .build()

        val client = OkHttpClient()

        val call = client.newCall(request)
        call.enqueue(callback)
        return call

    }
}