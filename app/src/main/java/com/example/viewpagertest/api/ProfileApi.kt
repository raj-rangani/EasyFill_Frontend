package com.example.viewpagertest.api

import android.util.Log
import com.example.viewpagertest.models.*
import org.json.JSONObject
import java.net.HttpURLConnection
import java.net.URL

class ProfileApi {

    companion object {

        private const val API_URL = "https://rp-easyfill.herokuapp.com"

        internal fun getProfile(): Profile {
            val url = URL("${API_URL}/user/me")
            val connection = (url.openConnection() as HttpURLConnection).apply {
                requestMethod = "GET"
                doInput = true
                setRequestProperty("Content-Type", "application/json")
                setRequestProperty("Authorization", "Bearer eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJpZCI6MSwidXNlcm5hbWUiOiJyYWoifQ.CVxxR7AAEWqx6WsJXluPxXvYD9CCh8QG6fUMsG3_alw")
                setChunkedStreamingMode(0)
            }
            if (connection.responseCode == HttpURLConnection.HTTP_OK) {
                val reader = connection.inputStream.bufferedReader()
                val responseJson = JSONObject(reader.readText())
                val nameJson = responseJson.getJSONObject("name")
                val addressJson = responseJson.getJSONObject("address")
                val parentJson = responseJson.getJSONObject("parent")

                val name:Name = Name(null,"", "", "", "")
                val address:Address = Address(null,"", "", "", "", "", "", "", "", "", "")
                val parent:Parent = Parent(null, Relation.N, Name(null,"","","",""))

                if(!(nameJson.equals(null))) {
                    name.firstname = nameJson.getString("firstname")
                    name.lastname = nameJson.getString("lastname")
                    name.middlename = nameJson.getString("middlename")
                    name.fullname = nameJson.getString("fullname")
                }

                if(!(addressJson.equals(null))) {
                    address.street_line_1 = addressJson.getString("street_line_1")
                    address.area = addressJson.getString("area")
                    address.locality = addressJson.getString("locality")
                    address.houseNo = addressJson.getString("house_no")
                    address.postOffice = addressJson.getString("post_office")
                    address.state = addressJson.getString("state")
                    address.district = addressJson.getString("district")
                    address.subDistrict = addressJson.getString("sub_district")
                    address.city = addressJson.getString("city")
                    address.pincode = addressJson.getString("pincode")
                }

                if(!(parentJson.equals(null))) {
                    parent.relation = Relation.valueOf(parentJson.getString("relation"))
                    val nameJson = parentJson.getJSONObject("name")
                    val parentName:Name = Name(null,"", "", "", "")

                    if(!(nameJson.equals(null))) {
                        parentName.firstname = nameJson.getString("firstname")
                        parentName.lastname = nameJson.getString("lastname")
                        parentName.middlename = nameJson.getString("middlename")
                        parentName.fullname = nameJson.getString("fullname")
                    }

                    parent.name = parentName
                }

                return Profile(
                    id = null,
                    username = responseJson.getString("username"),
                    email = responseJson.getString("email"),
                    contactNo = responseJson.getString("contact_no"),
                    dob = responseJson.getString("dob"),
                    gender = responseJson.getString("gender"),
                    password = null,
                    name = name,
                    address = address,
                    parent = parent
                )
            }

            return null!!
        }

        internal fun updateProfile(username: String, email: String, contactNo:String, dob:String, gender:String): Boolean {
            val url = URL("${API_URL}/user/me")

            val requestJsonObject = JSONObject()
            requestJsonObject.put("username", username)
            requestJsonObject.put("email", email)
            requestJsonObject.put("contact_no", contactNo)
            requestJsonObject.put("dob", dob)
            requestJsonObject.put("gender", gender)

            val connection = (url.openConnection() as HttpURLConnection).apply {
                requestMethod = "PUT"
                doInput = true
                doOutput = true
                setRequestProperty("Authorization", "Bearer eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJpZCI6MSwidXNlcm5hbWUiOiJyYWoifQ.CVxxR7AAEWqx6WsJXluPxXvYD9CCh8QG6fUMsG3_alw")
                setRequestProperty("Content-Type", "application/json")
                setChunkedStreamingMode(0)
            }

            try {
                val writer = connection.outputStream.bufferedWriter()
                writer.write(requestJsonObject.toString())
                writer.flush()

                val reader = connection.inputStream.bufferedReader()
                val responseJson = reader.readText()

                return connection.responseCode == HttpURLConnection.HTTP_CREATED
            }

            catch (Ex: Exception) {
                Log.i("Profile Update Error", Ex.message.toString())
            }

            finally {
                connection.disconnect()
            }
            return false
        }
    }
}