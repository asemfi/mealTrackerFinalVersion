import android.annotation.SuppressLint
import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import androidx.fragment.app.DialogFragment
import com.example.diettracker.R

class CustomDialog : DialogFragment() {
    private var nutrientTextViews = arrayOfNulls<TextView>(6)
    private var nutrientUnitTextViews = arrayOfNulls<TextView>(6)
    private var nutrients = arrayOfNulls<String>(6)
    private var units = arrayOfNulls<String>(6)

    @SuppressLint("MissingInflatedId")
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        // Create a new dialog object
        val builder = AlertDialog.Builder(activity)

        // Inflate the custom layout for the dialog
        val inflater = requireActivity().layoutInflater
        val view = inflater.inflate(R.layout.dialog, null)

        // Getting nutrient name and percentage TextViews
        nutrientTextViews[0] = view.findViewById(R.id.dialog_nutrient1)
        nutrientTextViews[1] = view.findViewById(R.id.dialog_nutrient2)
        nutrientTextViews[2] = view.findViewById(R.id.dialog_nutrient3)
        nutrientTextViews[3] = view.findViewById(R.id.dialog_nutrient4)
        nutrientTextViews[4] = view.findViewById(R.id.dialog_nutrient5)
        nutrientTextViews[5] = view.findViewById(R.id.dialog_nutrient6)

        nutrientUnitTextViews[0] = view.findViewById(R.id.dialog_nutrient_unit1)
        nutrientUnitTextViews[1] = view.findViewById(R.id.dialog_nutrient_unit2)
        nutrientUnitTextViews[2] = view.findViewById(R.id.dialog_nutrient_unit3)
        nutrientUnitTextViews[3] = view.findViewById(R.id.dialog_nutrient_unit4)
        nutrientUnitTextViews[4] = view.findViewById(R.id.dialog_nutrient_unit5)
        nutrientUnitTextViews[5] = view.findViewById(R.id.dialog_nutrient_unit6)

        builder.setView(view)

        // Set the positive button for the dialog
        builder.setPositiveButton("OK") { dialog, which ->
            // Handle the OK button click event
        }

        // Create the dialog and return it
        return builder.create()
    }

    fun setReturnValue(returnValue: Array<String>) {
        nutrients = arrayOfNulls<String>(6)
        units = arrayOfNulls<String>(6)

        for (i in 4..9) {
            nutrients[i - 4] = returnValue[i]
        }
        for (i in 10..15) {
            units[i - 10] = returnValue[i]
        }

        nutrientTextViews[0]?.text = nutrients[0]
    }

    override fun onStart() {
        super.onStart()

        for (i in 0..5) {
            nutrientTextViews[i]?.text = nutrients[i]
            nutrientUnitTextViews[i]?.text = "${units[i]}%"
        }
    }
}
