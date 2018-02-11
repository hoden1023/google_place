package com.example.icecream.new_job.util;

import com.example.icecream.new_job.entity.PlaceInfo;
import com.example.icecream.new_job.entity.PlaceResult;
import com.google.android.gms.maps.model.LatLng;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by IceCream on 2/10/2018.
 */

public class JsonParser {
    public static PlaceResult getListPlace(String json) {
        PlaceResult placeResult = new PlaceResult();
        try {
            JSONObject jsonObject = new JSONObject(json);
            if (jsonObject.has("next_page_token")) {
                placeResult.setPagetoken(jsonObject.getString("next_page_token"));
            }
            if (jsonObject.has("results")) {
                JSONArray arrayResult = jsonObject.getJSONArray("results");
                ArrayList<PlaceInfo> lstPlace = new ArrayList<>();
                for (int i = 0; i < arrayResult.length(); i++) {
                    PlaceInfo placeInfo = new PlaceInfo();
                    JSONObject object = arrayResult.getJSONObject(i);
                    if (object.has("name")) {
                        placeInfo.setName(object.getString("name"));
                    }
                    if (object.has("vicinity")) {
                        placeInfo.setVicinity(object.getString("vicinity"));
                    }
                    if (object.has("geometry")) {
                        JSONObject geometry = object.getJSONObject("geometry");
                        if (geometry.has("location")) {
                            JSONObject location = geometry.getJSONObject("location");
                            if (location.has("lat")) {
                                placeInfo.setLat(location.getDouble("lat"));
                            }

                            if (location.has("lng")) {
                                placeInfo.setLng(location.getDouble("lng"));
                            }
                        }
                    }
                    lstPlace.add(placeInfo);
                }
                placeResult.setListResult(lstPlace);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return placeResult;

    }

    public static ArrayList<LatLng> getListStep(String json)
    {
        ArrayList<LatLng> latLngs = new ArrayList<>();
        try {
            JSONObject resultObject = new JSONObject(json);
            if (resultObject.has("routes"))
            {
                JSONArray routesArray = resultObject.getJSONArray("routes");
                if (routesArray.length()>0)
                {
                    JSONObject jsonObject = routesArray.getJSONObject(0);
                    if (jsonObject.has("legs"))
                    {
                        JSONArray legsArray = jsonObject.getJSONArray("legs");
                        if (legsArray.length()>0)
                        {
                            JSONObject object = legsArray.getJSONObject(0);
                            if (object.has("steps"))
                            {
                                JSONArray stepsArray =object.getJSONArray("steps");
                                for (int i=0;i<stepsArray.length();i++)
                                {
                                    JSONObject object1 = stepsArray.getJSONObject(i);
                                    if (object1.has("polyline"))
                                    {
                                        JSONObject polyline = object1.getJSONObject("polyline");
                                        if (polyline.has("points")){
                                            String points=polyline.getString("points");
                                            latLngs.addAll(decodePoly(points));
                                        }

                                    }
                                }
                            }
                        }
                    }
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return latLngs;
    }

    public static ArrayList<LatLng> decodePoly(String encoded) {

        ArrayList<LatLng> poly = new ArrayList<>();
        int index = 0, len = encoded.length();
        int lat = 0, lng = 0;

        while (index < len) {
            int b, shift = 0, result = 0;
            do {
                b = encoded.charAt(index++) - 63;
                result |= (b & 0x1f) << shift;
                shift += 5;
            } while (b >= 0x20);
            int dlat = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
            lat += dlat;

            shift = 0;
            result = 0;
            do {
                b = encoded.charAt(index++) - 63;
                result |= (b & 0x1f) << shift;
                shift += 5;
            } while (b >= 0x20);
            int dlng = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
            lng += dlng;

            LatLng p = new LatLng((((double) lat / 1E5)),
                    (((double) lng / 1E5)));
            poly.add(p);
        }

        return poly;
    }

}
