package com.example.viewpagertest.models

import com.example.viewpagertest.models.Form
import com.example.viewpagertest.models.FormField

data class FieldSpecifier(
    var id: Int?,
    var form: Form?,
    var field: FormField?,
    var xaxis: Float?,
    var yaxis: Float?,
    var fieldData: String,
)
