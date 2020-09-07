package ie.wit.fragments


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import ie.wit.R


class FloorPlanFragment : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        activity?.title = getString(R.string.floorplan_title)
        return inflater.inflate(R.layout.fragment_floorplan, container, false)
    }

    companion object {
        @JvmStatic
        fun newInstance() =
            FloorPlanFragment().apply {
                arguments = Bundle().apply { }
            }
    }
}

