package com.alexis.morison.nasaimages.library.models

import java.io.Serializable

data class Library(
    val title: String,
    val href: String,
    val nasa_id: String,
    val description: String,
    val date_created: String,
    val keywords: List<String>,
    val center: String,
    val query: String,
) : Serializable