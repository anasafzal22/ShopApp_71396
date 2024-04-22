package com.onlineshop.app.app.fragments

import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.onlineshop.app.app.R
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [ProfileFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class ProfileFragment : Fragment(), OnMapReadyCallback {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
    val view= inflater.inflate(R.layout.app_fragment_profile, container, false)
        val button=view.findViewById<Button>(R.id.button)
        val image=view.findViewById<ImageView>(R.id.profile_image)
        Glide.with(requireActivity()).asBitmap().load("https://thispersondoesnotexist.com/").into(image)
        val mapFragment= childFragmentManager.findFragmentById(R.id.map)
                as SupportMapFragment
        mapFragment.getMapAsync(this)
button.setOnClickListener {
    val alertDialog=AlertDialog.Builder(requireContext())
    alertDialog.setTitle("About This App")
    alertDialog.setMessage("This shopping app uses fakestore api and was created by Anas Afzal (71396). THanks! :). Credit: fakestoreapi.com, thispersondoesnotexit.com")
    alertDialog.setIcon(R.drawable.ic_baseline_shopping_cart_24)
    alertDialog.setPositiveButton("Close", DialogInterface.OnClickListener { dialogInterface, i ->  dialogInterface.dismiss()})
    alertDialog.create().show()
}
        return  view
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment ProfileFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            ProfileFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }

    override fun onMapReady(p0: GoogleMap) {

        // Add a marker in Sydney and move the camera

        // Add a marker in Sydney and move the camera
        val sydney = LatLng(-34.0, 151.0)
        p0.addMarker(
            MarkerOptions()
                .position(sydney)
                .title("Marker in Sydney")
        )
        val zoomLevel = 15.0.toFloat()
        p0.moveCamera(CameraUpdateFactory.newLatLngZoom(sydney, zoomLevel))
    }
}