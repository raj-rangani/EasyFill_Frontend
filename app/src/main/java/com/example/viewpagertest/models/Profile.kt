package com.example.viewpagertest.models

data class Profile(
    var id:Int?,
    var username: String,
    var password: String?,
    var email: String,
    var contactNo: String,
    var dob: String,
    var gender: String,
    var name: Name,
    var address: Address,
    var parent: Parent
)
