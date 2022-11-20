package raman.chatSystem.model.internet

import android.content.Context
import android.net.*
import android.net.wifi.WifiManager
import android.os.Build
import android.text.format.Formatter
import android.util.Log
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.lifecycle.LiveData
import java.util.*
import kotlin.collections.HashSet


class NetworkConnectivity private constructor(context: Context) : LiveData<Boolean>()
{
    private val TAG = "Networkk"
    private lateinit var networkCallback: ConnectivityManager.NetworkCallback
    private val validNetworks : MutableSet<Network> = HashSet()
    //Get the network information provider manager
    private val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

    private var isInternetAvailable: Boolean = false

    companion object{
        private var instance: NetworkConnectivity? = null
        fun getInstance(context: Context) = synchronized(this)
        {
            if(instance == null) instance = NetworkConnectivity(context)
            instance
        }
    }

    private fun checkValidNetworks()
    {
        //It sets the boolean value to of our live data.
        postValue(validNetworks.size > 0)
    }

    override fun onActive() {
        networkCallback = createNetworkCallback()
        //Defining what data we want to observe, this case internet connection
        val networkRequest = NetworkRequest.Builder()
            .addCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
            .build()
        connectivityManager.registerNetworkCallback(networkRequest ,networkCallback)
    }

    override fun onInactive() {
        connectivityManager.unregisterNetworkCallback(networkCallback)
    }

    // Creates a callback object that is called everything network properties changes
    private fun createNetworkCallback() = object  : ConnectivityManager.NetworkCallback()
    {
        //Notifying on connection to a new network
        override fun onAvailable(network: Network)
        {
            //Set connection to yes
            isInternetAvailable = !isInternetAvailable
            //Check of the network has internet connection
            val isInternet = is_internet_on(network)
            Log.d(TAG, "OnAvailable: $network, is there internet: $isInternet")
            //If does, add it to the list of live networks for instance wi-fi and cellar
            if(isInternet)
                validNetworks.add(network)
            checkValidNetworks()
        }

        override fun onLost(network: Network)
        {
            //Set connection to no
            isInternetAvailable = !isInternetAvailable
            Log.d(TAG, "OnLost: $network")
            validNetworks.remove(network)
            checkValidNetworks()
        }
    }

    fun checkInternetConnectionManual() : Boolean
    {
        //If current API is greater than 23 we need to use the NetworkCapabilities
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
        {
            return when(get_network_type())
            {
                NetworkCapabilities.TRANSPORT_WIFI -> true
                NetworkCapabilities.TRANSPORT_CELLULAR -> true
                else -> false
            }
        }
        else // if the android version is below API 23
        {
            @Suppress("DEPRECATION") val networkInfo = connectivityManager.activeNetworkInfo ?: return false
            @Suppress("DEPRECATION") return networkInfo.isConnected
        }
    }

    fun getDeviceIpAddress() : String
    {
        if(isInternetAvailable)
        {
            //connectivityManager.activeNetwork only works from API 23 and above
            if(Build.VERSION.SDK_INT > Build.VERSION_CODES.M)
            {
                val link: LinkProperties =  connectivityManager.getLinkProperties(connectivityManager.activeNetwork) as LinkProperties
                val networkType = get_network_type()
                var ip = ""
                Log.d(TAG, link.linkAddresses.toString())
                if(networkType == NetworkCapabilities.TRANSPORT_CELLULAR)
                {
                    val address = link.linkAddresses.toString()
                    ip = address.substring(1, address.indexOf("/"))
                }
                else if(networkType == NetworkCapabilities.TRANSPORT_WIFI)
                {
                    // ################ If device is NetworkCapabilities.TRANSPORT_CELLULAR this won't work
                    val address = link.linkAddresses[1].toString()
                    ip = address.substring(0, address.indexOf("/"))
                }
                Log.d(TAG, "Device IP: $ip with API ${Build.VERSION.SDK_INT}")
                return ip
            }
            return "No IP found 0.0.0.0"
        }
        return "Waiting for connection..."
    }

    /***
     * This method gets the current device active network whether cellar or wifi and return the network object.
     * @return Network
     */
    private fun currentNetwork() :Network?
    {
        if(Build.VERSION.SDK_INT > Build.VERSION_CODES.M)
        {
            var network : Network?
            //Returns a Network object corresponding to the currently active default data network.
            if(connectivityManager.activeNetwork.also { network = it } == null)
            {
                Log.d(TAG, "No active network has been spotted, null")
                return null
            }
            return network
        }
        Log.d(TAG, "API is less than 23, activeNetwork() method isn't recognised to return a Network object, null")
        return null
    }

    /***
     * This method takes network object and return its capabilities that are stored in a NetworkCapabilities object.
     * @param Network
     * @return NetworkCapabilities
     */
    private fun get_Network_Capabilities(network: Network?): NetworkCapabilities?
    {
        if(network == null)
        {
            Log.d(TAG, "Null value instead of Network object been given, no capabilities can be found!")
            return null
        }
        return connectivityManager.getNetworkCapabilities(network)
    }

    /***
     * A function that analysis the current active network to check its type wi-fi, etc...
     * It only focuses on returning TRANSPORT_WIFI, TRANSPORT_CELLULAR, or 0 (for the rest types of in case of an error occurring
     * @return Int
     */
    fun get_network_type(): Int
    {
        //Returns a Network object corresponding to the currently active default data network.
        val network = currentNetwork() ?: return 0
        // Representation of the capabilities of an active network.
        val activeNetwork = get_Network_Capabilities(network) ?: return 0
        return when{
            // Indicates this network uses a Wi-Fi transport, or WiFi has network connectivity
            activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> NetworkCapabilities.TRANSPORT_WIFI
            //Indicates this network uses a Cellular  transport
            activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> NetworkCapabilities.TRANSPORT_CELLULAR
            //Else no internet
            else -> 0
        }
    }

    /***
     * A public function that analysis the current active network and checks if it has the NET_CAPABILITY_INTERNET which
     * if it has such capability then it returns true (There is internet) else false.
     * @return Boolean
     */
    fun is_internet_on() :Boolean
    {
        val network = currentNetwork()
        return is_internet_on(network)
    }

    /***
     * A private function takes a Network object to checks if it has the NET_CAPABILITY_INTERNET which
     * if it has such capability then it returns true (There is internet) else false.
     * @param Network
     * @return Boolean
     */
    private fun is_internet_on(network: Network?) :Boolean
    {
        val networkCapabilities = get_Network_Capabilities(network) ?: return false
        //Is internet on or not
        return networkCapabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
    }
}

/*

    if(Build.VERSION.SDK_INT > Build.VERSION_CODES.S)
    {
        address = link.linkAddresses[1].toString()
        ip = "32 " + address.substring(1, address.indexOf("/"))
    }
    else
    {
        address = link.linkAddresses[1].toString()
        ip = "<31> " + address.substring(0, address.indexOf("/"))
    }

 */