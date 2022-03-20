package com.example.viewpagertest.api

import android.util.Log
import org.json.JSONObject
import java.io.OutputStreamWriter
import java.net.HttpURLConnection
import java.net.URL

class LoginApi {
    companion object {
        private const val API_URL = "https://rp-easyfill.herokuapp.com"

        internal fun login(username: String, password: String) : Array<Any> {
            val url = URL("$API_URL/user/login")

            val requestJsonObject = JSONObject()
            requestJsonObject.put("username", username)
            requestJsonObject.put("password", password)

            val connection = (url.openConnection() as HttpURLConnection).apply {
                requestMethod = "POST"
                doOutput = true
                doInput = true
                setRequestProperty("Content-Type", "application/json")
            }

            try {
                val writer = OutputStreamWriter(connection.outputStream)
                writer.write(requestJsonObject.toString())
                writer.flush()
                writer.close()

                val reader = connection.inputStream.bufferedReader()
                val responseJson = JSONObject(reader.readText())

                return arrayOf(connection.responseCode == HttpURLConnection.HTTP_OK, responseJson.getString("token"))
            }

            catch (Ex: Exception) {
                Log.i("Profile Update Error", Ex.message.toString())
            }

            finally {
                connection.disconnect()
            }

            return arrayOf(false)
        }

        internal fun logout(token: String) : Boolean{
            val url = URL("$API_URL/user/logout")

            val connection = (url.openConnection() as HttpURLConnection).apply {
                requestMethod = "POST"
                setRequestProperty("Authorization", "Bearer $token")
                doOutput = true
                doInput = true
                setRequestProperty("Content-Type", "application/json")
            }

            return connection.responseCode == HttpURLConnection.HTTP_OK
        }
    }
}