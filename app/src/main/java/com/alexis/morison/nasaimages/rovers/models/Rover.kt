package com.alexis.morison.nasaimages.rovers.models

import java.io.Serializable

class Rover(
    val camera_full_name: String,
    val earth_date: String,
    val sol_date: String,
    val rover_name: String,
    val status: String,
    val img_src: String,
) : Serializable