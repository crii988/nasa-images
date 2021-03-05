package com.alexis.morison.nasaimages.rovers.fragments

import android.os.Bundle
import android.transition.TransitionManager
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.view.get
import com.alexis.morison.nasaimages.R
import com.google.android.material.slider.Slider
import com.google.android.material.textfield.TextInputLayout

class RoverFormFragment : Fragment() {

    private lateinit var dropdownRover: AutoCompleteTextView
    private lateinit var textFieldRover: TextInputLayout

    private lateinit var dropdownCamera: AutoCompleteTextView
    private lateinit var textFieldCamera: TextInputLayout

    private lateinit var radioLatest: RadioButton
    private lateinit var radioFilter: RadioButton
    private lateinit var radioSol: RadioButton
    private lateinit var radioEarth: RadioButton

    private lateinit var layoutFilter: LinearLayout

    private lateinit var checkCamera: CheckBox

    private lateinit var sliderSol: Slider
    private lateinit var datePickerEarth: DatePicker

    private lateinit var btnSearch: Button
    private lateinit var root: CoordinatorLayout

    private val apiKey = "XdRrmURyk5bW91jnAyoHbaAngJrF8vKIiQiZI6AV"

    private val itemsRovers = listOf("Perseverance", "Curiosity", "Opportunity", "Spirit")
    private var adapterRovers: ArrayAdapter<String>? = null
    private var roverSelected = ""

    private var cameras: List<String>? = null
    private var adapterCameras: ArrayAdapter<String>? = null
    private var cameraSelected = ""
    private val perseveranceDict = hashMapOf<String, String>()
    private val curiosityDict = hashMapOf<String, String>()
    private val opportunityDict = hashMapOf<String, String>()
    private val spiritDict = hashMapOf<String, String>()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        perseveranceDict["Rover Up-Look Camera"] = "EDL_RUCAM"
        perseveranceDict["Rover Down-Look Camera"] = "EDL_RDCAM"
        perseveranceDict["Descent Stage Down-Look Camera"] = "EDL_DDCAM"
        perseveranceDict["Parachute Up-Look Camera A"] = "EDL_PUCAM1"
        perseveranceDict["Parachute Up-Look Camera B"] = "EDL_PUCAM2"
        perseveranceDict["Navigation Camera - Left"] = "NAVCAM_LEFT"
        perseveranceDict["Navigation Camera - Right"] = "NAVCAM_RIGHT"
        perseveranceDict["Mast Camera Zoom - Right"] = "MCZ_RIGHT"
        perseveranceDict["Mast Camera Zoom - Left"] = "MCZ_LEFT"
        perseveranceDict["Front Hazard Avoidance Camera - Left"] = "FRONT_HAZCAM_LEFT_A"
        perseveranceDict["Front Hazard Avoidance Camera - Right"] = "FRONT_HAZCAM_RIGHT_A"
        perseveranceDict["Rear Hazard Avoidance Camera - Left"] = "REAR_HAZCAM_LEFT"
        perseveranceDict["Rear Hazard Avoidance Camera - Right"] = "REAR_HAZCAM_RIGHT"

        curiosityDict["Front Hazard Avoidance Camera"] = "FHAZ"
        curiosityDict["Rear Hazard Avoidance Camera"] = "RHAZ"
        curiosityDict["Mast Camera"] = "MAST"
        curiosityDict["Chemistry and Camera Complex"] = "CHEMCAM"
        curiosityDict["Mars Hand Lens Imager"] = "MAHLI"
        curiosityDict["Mars Descent Imager"] = "MARDI"
        curiosityDict["Navigation Camera"] = "NAVCAM"

        opportunityDict["Front Hazard Avoidance Camera"] = "FHAZ"
        opportunityDict["Rear Hazard Avoidance Camera"] = "RHAZ"
        opportunityDict["Navigation Camera"] = "NAVCAM"
        opportunityDict["Panoramic Camera"] = "PANCAM"
        opportunityDict["Miniature Thermal Emission Spectrometer (Mini-TES)"] = "MINITES"

        spiritDict["Front Hazard Avoidance Camera"] = "FHAZ"
        spiritDict["Rear Hazard Avoidance Camera"] = "RHAZ"
        spiritDict["Navigation Camera"] = "NAVCAM"
        spiritDict["Panoramic Camera"] = "PANCAM"
        spiritDict["Miniature Thermal Emission Spectrometer (Mini-TES)"] = "MINITES"
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        val v = inflater.inflate(R.layout.fragment_rover_form, container, false)

        setViews(v)

        setData()

        setListeners()

        return v
    }

    private fun setViews(v: View) {

        textFieldRover = v.findViewById(R.id.dropdown_menu_rover)
        dropdownRover = v.findViewById(R.id.autocomplete_rover)

        textFieldCamera = v.findViewById(R.id.dropdown_menu_camera)
        dropdownCamera = v.findViewById(R.id.autocomplete_camera)

        radioLatest = v.findViewById(R.id.radio_button_latest)
        radioFilter = v.findViewById(R.id.radio_button_filter)
        radioSol = v.findViewById(R.id.radio_button_sol)
        radioEarth = v.findViewById(R.id.radio_button_earth)

        layoutFilter = v.findViewById(R.id.layout_filtering)

        checkCamera = v.findViewById(R.id.check_box_camera)

        sliderSol = v.findViewById(R.id.slider_sol)
        datePickerEarth = v.findViewById(R.id.date_picker_earth)

        btnSearch = v.findViewById(R.id.btn_search_rover)
        root = v.findViewById(R.id.root_rover_form)
    }

    private fun setData() {

        adapterRovers = ArrayAdapter(requireContext(), R.layout.list_item, itemsRovers)
        (textFieldRover.editText as? AutoCompleteTextView)?.setAdapter(adapterRovers)


    }

    private fun setListeners() {

        dropdownRover.setOnItemClickListener { _, _, i, _ ->

            roverSelected = adapterRovers!!.getItem(i).toString()
            textFieldRover.isEnabled = false
            textFieldRover.isEnabled = true

            when (roverSelected) {

                "Perseverance" -> {
                    cameras = perseveranceDict.toList().map { it.first }

                    adapterCameras = ArrayAdapter(requireContext(), R.layout.list_item, cameras!!)
                    (textFieldCamera.editText as? AutoCompleteTextView)?.setAdapter(adapterCameras)

                    dropdownCamera.setText("")
                }
                "Curiosity" -> {
                    cameras = curiosityDict.toList().map { it.first }

                    adapterCameras = ArrayAdapter(requireContext(), R.layout.list_item, cameras!!)
                    (textFieldCamera.editText as? AutoCompleteTextView)?.setAdapter(adapterCameras)

                    dropdownCamera.setText("")
                }
                "Opportunity" -> {
                    cameras = opportunityDict.toList().map { it.first }

                    adapterCameras = ArrayAdapter(requireContext(), R.layout.list_item, cameras!!)
                    (textFieldCamera.editText as? AutoCompleteTextView)?.setAdapter(adapterCameras)

                    dropdownCamera.setText("")
                }
                else -> {
                    cameras = spiritDict.toList().map { it.first }

                    adapterCameras = ArrayAdapter(requireContext(), R.layout.list_item, cameras!!)
                    (textFieldCamera.editText as? AutoCompleteTextView)?.setAdapter(adapterCameras)

                    dropdownCamera.setText("")
                }
            }
        }

        radioFilter.setOnClickListener {

            if (radioFilter.isChecked) layoutFilter.visibility = View.VISIBLE
        }

        radioLatest.setOnClickListener {

            if (radioLatest.isChecked) layoutFilter.visibility = View.GONE
        }

        checkCamera.setOnClickListener {

            textFieldCamera.isEnabled = checkCamera.isChecked
        }

        radioSol.setOnClickListener {

            if (radioSol.isChecked) {

                sliderSol.visibility = View.VISIBLE
                datePickerEarth.visibility = View.GONE

                TransitionManager.beginDelayedTransition(root)
            }
        }

        radioEarth.setOnClickListener {

            if (radioEarth.isChecked) {

                sliderSol.visibility = View.GONE
                datePickerEarth.visibility = View.VISIBLE

                TransitionManager.beginDelayedTransition(root)
            }
        }

        dropdownCamera.setOnItemClickListener { _, _, i, _ ->

            cameraSelected = adapterCameras!!.getItem(i).toString()
            textFieldCamera.isEnabled = false
            textFieldCamera.isEnabled = true
        }

        btnSearch.setOnClickListener {

            Log.d("asd", roverSelected)
            Log.d("asd", "latest photos " + radioLatest.isChecked.toString())
            Log.d("asd", "filer photos " + radioFilter.isChecked.toString())
            Log.d("asd", "filer by camera " + checkCamera.isChecked.toString())
            Log.d("asd", cameraSelected)
            Log.d("asd", "martian sol " + radioSol.isChecked.toString())
            Log.d("asd", "earth date " + radioEarth.isChecked.toString())
            Log.d("asd", sliderSol.value.toString())
            Log.d("asd", "${datePickerEarth.year}-${datePickerEarth.month + 1}-${datePickerEarth.dayOfMonth}")
        }
    }
}