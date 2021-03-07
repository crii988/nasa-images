package com.alexis.morison.nasaimages.rovers.fragments

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.transition.TransitionManager
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.view.get
import androidx.fragment.app.FragmentActivity
import com.alexis.morison.nasaimages.R
import com.alexis.morison.nasaimages.library.fragments.LibraryFragment
import com.alexis.morison.nasaimages.rovers.models.RoverQuery
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.slider.Slider
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import java.util.*

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
    private lateinit var inputSol: TextInputEditText
    private lateinit var inputSolLayout: TextInputLayout
    private lateinit var datePickerEarth: DatePicker

    private lateinit var btnSearch: Button
    private lateinit var root: CoordinatorLayout

    private lateinit var toolbar: MaterialToolbar

    private val itemsRovers = listOf("Perseverance", "Curiosity", "Opportunity", "Spirit")
    private var adapterRovers: ArrayAdapter<String>? = null
    private var roverSelected = ""

    private var cameras: List<String>? = null
    private var adapterCameras: ArrayAdapter<String>? = null
    private var cameraSelected = ""
    private var sol = 0
    private var date = ""
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
        inputSol = v.findViewById(R.id.input_sol)
        inputSolLayout = v.findViewById(R.id.input_sol_layout)
        datePickerEarth = v.findViewById(R.id.date_picker_earth)

        btnSearch = v.findViewById(R.id.btn_search_rover)
        root = v.findViewById(R.id.root_rover_form)

        toolbar = activity!!.findViewById(R.id.toolbar_id_container)
        toolbar.title = resources.getString(R.string.rovers_title)
    }

    private fun setData() {

        adapterRovers = ArrayAdapter(requireContext(), R.layout.list_item, itemsRovers)
        (textFieldRover.editText as? AutoCompleteTextView)?.setAdapter(adapterRovers)

        inputSol.setText("0")
        date = "${datePickerEarth.year}-${datePickerEarth.month + 1}-${datePickerEarth.dayOfMonth}"
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

        radioFilter.setOnCheckedChangeListener { _, b ->

            if (b) layoutFilter.visibility = View.VISIBLE
        }

        radioLatest.setOnCheckedChangeListener { _, b ->

            if (b) layoutFilter.visibility = View.GONE
        }

        checkCamera.setOnCheckedChangeListener { _, b ->

            textFieldCamera.isEnabled = b
        }

        radioSol.setOnCheckedChangeListener { _, b ->

            if (b) {

                sliderSol.visibility = View.VISIBLE
                inputSolLayout.visibility = View.VISIBLE
                datePickerEarth.visibility = View.GONE

                TransitionManager.beginDelayedTransition(root)
            }
        }

        radioEarth.setOnCheckedChangeListener { _, b ->

            if (b) {

                sliderSol.visibility = View.GONE
                inputSolLayout.visibility = View.GONE
                datePickerEarth.visibility = View.VISIBLE

                TransitionManager.beginDelayedTransition(root)
            }
        }

        dropdownCamera.setOnItemClickListener { _, _, i, _ ->

            cameraSelected = adapterCameras!!.getItem(i).toString()
            textFieldCamera.isEnabled = false
            textFieldCamera.isEnabled = true
        }

        sliderSol.addOnChangeListener { _, value, _ ->

            sol = value.toInt()
            inputSol.setText(sol.toString())
        }

        inputSol.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) { }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

                try {
                    sol = p0.toString().toInt()
                    if (sol < 5000) sliderSol.value = sol.toFloat()

                    inputSol.setSelection(inputSol.length())
                }
                catch (e: Exception) {

                    Log.d("asdasd", e.message.toString())
                }
            }

            override fun afterTextChanged(p0: Editable?) { }
        })

        datePickerEarth.setOnDateChangedListener { _, i, i2, i3 ->

            date = "$i-$i2-$i3"
        }

        btnSearch.setOnClickListener {

            search()
        }
    }

    private fun search() {

        val roverQuery = RoverQuery(
                roverSelected,
                radioLatest.isChecked,
                radioFilter.isChecked,
                checkCamera.isChecked,
                cameraSelected,
                sol.toString(),
                date
        )

        val fm = context as FragmentActivity

        val fragmentTransaction = fm.supportFragmentManager.beginTransaction()

        fragmentTransaction.replace(R.id.fragmentContainer, RoverFragment.newInstance(roverQuery))

        fragmentTransaction.addToBackStack(null)
        fragmentTransaction.commit()
    }
}