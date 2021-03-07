package com.alexis.morison.nasaimages.rovers.models

import java.io.Serializable

class RoverQuery(
        val rover: String,
        val isLatest: Boolean,
        val isFilter: Boolean,
        val isFilterCamera: Boolean,
        val camera: String,
        val sol: String,
        val date: String,
) : Serializable