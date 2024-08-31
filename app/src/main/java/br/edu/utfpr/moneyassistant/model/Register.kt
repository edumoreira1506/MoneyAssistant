package br.edu.utfpr.moneyassistant.model

import java.util.Date

data class Register(
    var _id: Int,
    var type: String,
    var detail: String,
    var value: Int,
    var date: Date,
)
