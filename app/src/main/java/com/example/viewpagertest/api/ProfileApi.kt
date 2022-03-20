package com.example.viewpagertest.api

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Log
import com.example.viewpagertest.models.*
import com.google.gson.JsonObject
import org.json.JSONArray
import org.json.JSONObject
import java.io.*
import java.net.HttpURLConnection
import java.net.URL

class ProfileApi {

    companion object {

        private const val API_URL = "https://rp-easyfill.herokuapp.com"

        internal fun getProfile(token: String): Profile? {
            val url = URL("${API_URL}/user/me")

            val name:Name = Name(null,"[No Data]", "[No Data]", "[No Data]", "[No Data]")
            val address:Address = Address(null,"[No Data]", "[No Data]", "[No Data]", "[No Data]", "[No Data]", "[No Data]", "[No Data]", "[No Data]", "[No Data]", "000000")
            val parent:Parent = Parent(null, Relation.N, Name(null,"[No Data]","[No Data]","[No Data]","[No Data]"))

            try {
                addAddress(address,token)
                addName(name, token)
                addParent(parent,token)
            } catch (Ex: Exception) {

            }

            val connection = (url.openConnection() as HttpURLConnection).apply {
                requestMethod = "GET"
                doInput = true
                setRequestProperty("Content-Type", "application/json")
                setRequestProperty("Authorization", "Bearer $token")
                setChunkedStreamingMode(0)
            }

            val reader = connection.inputStream.bufferedReader()
            val responseJson = JSONObject(reader.readText())


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
            }

            try {
                val writer = OutputStreamWriter(connection.outputStream)
                writer.write(requestJsonObject.toString())
                writer.flush()
                writer.close()

                if(connection.responseCode == HttpURLConnection.HTTP_BAD_REQUEST) {
                    val reader = connection.errorStream.bufferedReader()
                    val responseJson = reader.readText()

                    return arrayOf(false, responseJson)
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

        internal fun updateAddress(street:String, area:String, locality:String, house:String, post:String, state:String, district:String, subDistrict: String, city:String, pincode:String, token:String): Array<Any> {
            val url = URL("${API_URL}/user/address")

            val requestJsonObject = JSONObject()
            requestJsonObject.put("street_line_1", street)
            requestJsonObject.put("area", area)
            requestJsonObject.put("locality", locality)
            requestJsonObject.put("house_no", house)
            requestJsonObject.put("post_office", post)
            requestJsonObject.put("state", state)
            requestJsonObject.put("district", district)
            requestJsonObject.put("sub_district", subDistrict)
            requestJsonObject.put("city", city)
            requestJsonObject.put("pincode", pincode)

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

        internal fun updateParent(relation: String, firstname: String, lastname: String, middlename:String, fullname:String, token:String): Array<Any> {
            val url = URL("${API_URL}/user/parent")

            val requestJsonObject = JSONObject()
            var parentRelation:String = "N"
            parentRelation = when(relation) {
                Relation.H.relation -> "H"
                Relation.W.relation -> "W"
                Relation.G.relation -> "G"
                Relation.F.relation -> "F"
                Relation.M.relation -> "M"
                else -> "N"
            }
            requestJsonObject.put("relation",parentRelation)

            val nameRequestObject = JSONObject()
            nameRequestObject.put("firstname", firstname)
            nameRequestObject.put("lastname", lastname)
            nameRequestObject.put("middlename", middlename)
            nameRequestObject.put("fullname", fullname)

            requestJsonObject.put("name", nameRequestObject)

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
                    return arrayOf(false, "Invalid Data")
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
                setChunkedStreamingMode(1)
            }

            try {
                val reader = connection.inputStream
                val bytes = reader.readBytes()
                return BitmapFactory.decodeByteArray(bytes, 0, bytes.size)
            } catch (Ex: Exception) {
                Log.i("Profile Fetch Error", Ex.message.toString())
            } finally {
                connection.disconnect()
            }
            return null
        }

        internal fun setProfileImage(bitmap:ByteArrayOutputStream, token: String) : Boolean {
            val url = URL("${API_URL}/user/avatar")

            val connection = (url.openConnection() as HttpURLConnection).apply {
                requestMethod = "POST"
                doOutput = true
                doInput = true
                setRequestProperty("Authorization", "Bearer $token")
            }

            try {
                val byteArray = bitmap.toByteArray()
                val writer = connection.outputStream
                writer.write(byteArray, 0, byteArray.size)
                writer.flush()
                writer.close()

                return connection.responseCode == HttpURLConnection.HTTP_CREATED

            } catch (Ex: Exception) {
                Log.i("Profile Fetch Error", Ex.message.toString())
            } finally {
                connection.disconnect()
            }
            return false
        }

        internal fun deleteProfile(token:String): Boolean {
            val url = URL("${API_URL}/user/me")

            val connection = (url.openConnection() as HttpURLConnection).apply {
                requestMethod = "DELETE"
                setRequestProperty("Authorization", "Bearer $token")
                doInput = true
                doOutput = true
            }

            return connection.responseCode == HttpURLConnection.HTTP_NO_CONTENT
        }

        private fun addAddress(address: Address, token: String) : Array<Any> {
            val url = URL("${API_URL}/user/address")

            val requestJsonObject = JSONObject()
            requestJsonObject.put("street_line_1", address.street_line_1)
            requestJsonObject.put("area", address.area)
            requestJsonObject.put("locality", address.locality)
            requestJsonObject.put("house_no", address.houseNo)
            requestJsonObject.put("post_office", address.postOffice)
            requestJsonObject.put("state", address.state)
            requestJsonObject.put("district", address.district)
            requestJsonObject.put("sub_district", address.subDistrict)
            requestJsonObject.put("city", address.city)
            requestJsonObject.put("pincode", address.pincode)

            val connection = (url.openConnection() as HttpURLConnection).apply {
                requestMethod = "POST"
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

        private fun addParent(parent: Parent, token:String): Array<Any> {
            val url = URL("${API_URL}/user/parent")

            val requestJsonObject = JSONObject()
            var parentRelation:String = "N"
            parentRelation = when(parent.relation.relation) {
                Relation.H.relation -> "H"
                Relation.W.relation -> "W"
                Relation.G.relation -> "G"
                Relation.F.relation -> "F"
                Relation.M.relation -> "M"
                else -> "N"
            }
            requestJsonObject.put("relation",parentRelation)

            val nameRequestObject = JSONObject()
            nameRequestObject.put("firstname", parent.name.firstname)
            nameRequestObject.put("lastname", parent.name.lastname)
            nameRequestObject.put("middlename", parent.name.middlename)
            nameRequestObject.put("fullname", parent.name.fullname)

            requestJsonObject.put("name", nameRequestObject)

            val connection = (url.openConnection() as HttpURLConnection).apply {
                requestMethod = "POST"
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
                    return arrayOf(false, "Invalid Data")
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

        private fun addName(name: Name, token:String): Array<Any> {
            val url = URL("${API_URL}/user/name")

            val requestJsonObject = JSONObject()
            requestJsonObject.put("firstname", name.firstname)
            requestJsonObject.put("lastname", name.lastname)
            requestJsonObject.put("middlename", name.middlename)
            requestJsonObject.put("fullname", name.fullname)

            val connection = (url.openConnection() as HttpURLConnection).apply {
                requestMethod = "POST"
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

    }
}