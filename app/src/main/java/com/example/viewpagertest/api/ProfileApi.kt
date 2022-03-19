package com.example.viewpagertest.api

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Log
import com.example.viewpagertest.models.*
import com.google.gson.JsonObject
import org.json.JSONObject
import java.io.OutputStreamWriter
import java.net.HttpURLConnection
import java.net.URL

class ProfileApi {

    companion object {

        private const val API_URL = "https://rp-easyfill.herokuapp.com"

        internal fun getProfile(token: String): Profile? {
            val url = URL("${API_URL}/user/me")

            val connection = (url.openConnection() as HttpURLConnection).apply {
                requestMethod = "GET"
                doInput = true
                setRequestProperty("Content-Type", "application/json")
                setRequestProperty("Authorization", "Bearer $token")
                setChunkedStreamingMode(0)
            }

            val reader = connection.inputStream.bufferedReader()
            val responseJson = JSONObject(reader.readText())

            val name:Name = Name(null,"[No Data]", "[No Data]", "[No Data]", "[No Data]")
            val address:Address = Address(null,"[No Data]", "[No Data]", "[No Data]", "[No Data]", "[No Data]", "[No Data]", "[No Data]", "[No Data]", "[No Data]", "[No Data]")
            val parent:Parent = Parent(null, Relation.N, Name(null,"[No Data]","[No Data]","[No Data]","[No Data]"))

            try {
                val nameJson = responseJson.getJSONObject("name")
                val addressJson = responseJson.getJSONObject("address")
                val parentJson = responseJson.getJSONObject("parent")

                if (connection.responseCode == HttpURLConnection.HTTP_OK) {
                    if(nameJson != null) {
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
            } catch (ex: Exception) {
                return Profile(
                    id = null,
                    username = responseJson.getString("username"),
                    email = responseJson.getString("email"),
                    contactNo = responseJson.getString("contact_no"),
                    dob = if(responseJson.getString("dob").equals(null)) responseJson.getString("dob") else "[NO DATA]",
                    gender = responseJson.getString("gender"),
                    password = null,
                    name = name,
                    address = address,
                    parent = parent
                )
            } finally {
                connection.disconnect()
            }
            return null
        }

        internal fun updateProfile(username: String, email: String, contactNo:String, dob:String, gender:String, token:String): Array<Any> {
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
                setRequestProperty("Authorization", "Bearer $token")
                setRequestProperty("Content-Type", "application/json")
                setRequestProperty("Accept", "application/json")
            }

            try {
                val writer = OutputStreamWriter(connection.outputStream)
                writer.write(requestJsonObject.toString())
                writer.flush()
                writer.close()

                if(connection.responseCode == HttpURLConnection.HTTP_BAD_REQUEST) {
                    val reader = connection.errorStream.bufferedReader()
                    val responseJson = JSONObject(reader.readText())

                    return arrayOf(false, responseJson.getString("error"))
                } else {
                    val reader = connection.inputStream.bufferedReader()
                    val responseJson = JSONObject(reader.readText())
                    return arrayOf(true, responseJson)
                }
            }

            catch (Ex: Exception) {
                Log.i("Profile Update Error", Ex.message.toString())
            }

            finally {
                connection.disconnect()
            }

            return arrayOf(false, "Invalid Data")
        }
        internal fun updateName(firstname: String, lastname: String, middlename:String, fullname:String, token:String): Array<Any> {
            val url = URL("${API_URL}/user/name")

            val requestJsonObject = JSONObject()
            requestJsonObject.put("firstname", firstname)
            requestJsonObject.put("lastname", lastname)
            requestJsonObject.put("middlename", middlename)
            requestJsonObject.put("fullname", fullname)

            val connection = (url.openConnection() as HttpURLConnection).apply {
                requestMethod = "PUT"
                doInput = true
                doOutput = true
                setRequestProperty("Authorization", "Bearer $token")
                setRequestProperty("Content-Type", "application/json")
                setRequestProperty("Accept", "application/json")
            }

            try {
                val writer = OutputStreamWriter(connection.outputStream)
                writer.write(requestJsonObject.toString())
                writer.flush()
                writer.close()

                if(connection.responseCode == HttpURLConnection.HTTP_BAD_REQUEST) {
                    val reader = connection.errorStream.bufferedReader()
                    val responseJson = JSONObject(reader.readText())

                    return arrayOf(false, responseJson.getString("error"))
                } else {
                    val reader = connection.inputStream.bufferedReader()
                    val responseJson = JSONObject(reader.readText())
                    return arrayOf(true, responseJson)
                }
            }

            catch (Ex: Exception) {
                Log.i("Profile Update Error", Ex.message.toString())
            }

            finally {
                connection.disconnect()
            }

            return arrayOf(false, "Invalid Data")
        }

        internal fun createProfile(username: String, email: String, contactNo:String, password:String) : Array<Any> {
            val url = URL("${API_URL}/user")

            val requestJsonObject = JSONObject()
            requestJsonObject.put("username", username)
            requestJsonObject.put("email", email)
            requestJsonObject.put("contact_no", contactNo)
            requestJsonObject.put("password", password)

            val connection = (url.openConnection() as HttpURLConnection).apply {
                requestMethod = "POST"
                doInput = true
                doOutput = true
                setRequestProperty("Content-Type", "application/json")
            }

            try {
                val writer = OutputStreamWriter(connection.outputStream)
                writer.write(requestJsonObject.toString())
                writer.flush()
                writer.close()

                if(connection.responseCode == HttpURLConnection.HTTP_BAD_REQUEST) {
                    val reader = connection.errorStream.bufferedReader()
                    val responseJson = JSONObject(reader.readText())

                    return arrayOf(false, responseJson.getString("error"))
                } else {
                    return arrayOf(true, "Account Created")
                }
            }

            catch (Ex: Exception) {
                Log.i("Profile Create Error", Ex.message.toString())
            }

            finally {
                connection.disconnect()
            }

            return arrayOf(false, "Invalid Data")
        }

        internal fun getProfileImage(token: String): Bitmap? {
            val url = URL("${API_URL}/user/avatar")

            val connection = (url.openConnection() as HttpURLConnection).apply {
                requestMethod = "GET"
                doInput = true
                setRequestProperty("Authorization", "Bearer $token")
                setRequestProperty("Content-Type", "image/png")
            }

            try {
                val reader = connection.inputStream
                return BitmapFactory.decodeStream(reader)
            } catch (Ex: Exception) {
                Log.i("Profile Fetch Error", Ex.message.toString())
            } finally {
                connection.disconnect()
            }
            return null
        }
    }
}