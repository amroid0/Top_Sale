package com.aelzohry.topsaleqatar.helper

import com.google.android.gms.maps.model.LatLng


object Constants {

    const val BASE_GOOGLE_API_URL = "https://maps.googleapis.com/maps/api/"

    //    const val MAP_API_KEY = "AIzaSyBfxQvogOA3VXhik1abMCGy98oPn_yDn98"
    const val MAP_API_KEY = "AIzaSyDA9AYnlXQ5ysrifNFT6MnaVSYl-rJQWcI"

    const val API_BASE_URL = "https://superback.topsale.qa/api/"
   const val FILE_BASE_URL = "https://superback.topsale.qa/"
    const val SOCKET_URL = "https://wss.topsale.qa" // DEFAULT


    //    const val SOCKET_URL = "https://dorosona.herokuapp.com/"
//    const val SOCKET_URL = "http://ws.topsale.qa"
//    const val SOCKET_URL = "https://testws.topsale.qa"
//    const val SOCKET_URL = "http://dorosona.herokuapp.com"
//    const val SOCKET_URL = "https://dorosona.herokuapp.com"
    const val SOCKET_PORT = 5000

    const val PASS_LENGTH = 6
    const val CACHE_SIZE = 10 * 1024 * 1024 //10MB
    const val CACHE_MAX_AGE = 5 //5 seconds
    const val CACHE_MAX_STALE = 60 * 60 * 24 * 7 //1 week

    const val REQUEST_FINE_LOCATION_PERMISSION = 765
    //const val API_BASE_URL = "https://superbacktest.topsale.qa/api/"
    //const val FILE_BASE_URL = "https://superbacktest.topsale.qa/"
//    const val SOCKET_URL = "http://ws.topsale.qa"
//    const val SOCKET_URL = "http://superbacktest.topsale.qa"
//    const val SOCKET_PORT = 5001

    //    const val API_BASE_URL = "http://10.0.2.2:3001/api/"
//    const val FILE_BASE_URL = "http://10.0.2.2:3001/"
//    const val SOCKET_URL = "http://10.0.2.2:3001"
//    const val SOCKET_PORT = 5001
    const val NETWORKING_LOG = "networking"
    const val HELP_URL = "https://google.com" // FIXME:- change help link

    //const val ONESIGNAL_APP_ID = "e2d7e8f4-2b86-4e4f-87eb-9a9da42775df" // for test
   const val ONESIGNAL_APP_ID = "3a8f7e9f-200d-40b7-a181-276881cba63b" // for production

    const val FROM_HOME = 1
    const val FROM_CATEGORY = 2
    const val FROM_OTHERS = 3

    //    val QATAT_LOCATION: LatLng = LatLng(25.279471, 51.166071)
    val QATAT_LOCATION: LatLng = LatLng(25.23843022879583, 51.36004127562046)
}