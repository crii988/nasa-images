package com.alexis.morison.nasaimages.apod.models

import java.io.Serializable

data class APOD(
    val copyright: String,
    val date: String,
    val explanation: String,
    val hdurl: String,
    val title: String,
    val url: String,
) : Serializable