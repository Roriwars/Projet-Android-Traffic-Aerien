package com.example.trafficaerien

import android.os.AsyncTask
import android.util.Log

class SearchFlightsAsyncTask(requestListener: RequestsManager.RequestListener) :
        AsyncTask<SearchDataModel, Void, String>() {

    var mRequestListener: RequestsManager.RequestListener? = null

    init {
        mRequestListener = requestListener
    }

    override fun doInBackground(vararg searchModel: SearchDataModel?): String? {
        val data = searchModel[0]
        val baseUrl: String = if (data?.isArrival!!) {
            "https://opensky-network.org/api/flights/arrival"
        } else {
            "https://opensky-network.org/api/flights/departure"
        }

        val result: String? =
                RequestsManager.get(baseUrl, getRequestParams(searchModel = searchModel[0]))

        return result
    }

    override fun onPostExecute(result: String?) {
        super.onPostExecute(result)

        if (result == null) {
            mRequestListener?.onRequestFailed()
        } else {
            Log.v("RESULT", result)
            mRequestListener?.onRequestSuccess(result)
        }
    }

    private fun getRequestParams(searchModel: SearchDataModel?): Map<String, String>? {
        val params = HashMap<String, String>()
        if (searchModel != null) {
            params["airport"] = searchModel.icao
            params["begin"] = searchModel.begin.toString()
            params["end"] = searchModel.end.toString()
        }
        return params
    }
}