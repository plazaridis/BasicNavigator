package com.threenitas.basicnavigator;


import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocompleteFragment;
import com.google.android.gms.location.places.ui.PlaceSelectionListener;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import io.realm.Realm;
import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;


/**
 * A simple {@link Fragment} subclass.
 */
public class MapFragment extends Fragment implements OnMapReadyCallback {

    private GoogleMap mGoogleMap;
    private MapView mMapView;
    private View mView;
    private Marker mMarkerFrom, mMarkerTo;
    private Place mPlaceFrom, mPlaceTo;
    private Polyline mPolyline;
    private Button dirButton;
    private Realm realm = Realm.getDefaultInstance();

    private PlaceAutocompleteFragment placeAutocompleteFragmentFrom, placeAutocompleteFragmentTo;

    public MapFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        mView = inflater.inflate(R.layout.fragment_map, container, false);
        dirButton = (Button) mView.findViewById(R.id.dirButton);
        dirButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mMarkerFrom != null && mMarkerTo != null) {

                    if (mPolyline != null) {
                        mPolyline.remove();
                    }

                    writeDB(mPlaceFrom.getLatLng().latitude,mPlaceFrom.getLatLng().longitude,mPlaceFrom.getAddress().toString(),mPlaceFrom.getName().toString(),mPlaceTo.getLatLng().latitude,mPlaceTo.getLatLng().longitude,mPlaceTo.getAddress().toString(),mPlaceTo.getName().toString());
                    boundsCalcAndZoom(mMarkerFrom, mMarkerTo);
                    drawLine(mPlaceFrom.getLatLng().latitude, mPlaceFrom.getLatLng().longitude, mPlaceTo.getLatLng().latitude, mPlaceTo.getLatLng().longitude);

                }
            }
        });
        return mView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        placeAutocompleteFragmentFrom = (PlaceAutocompleteFragment) getActivity().getFragmentManager().findFragmentById(R.id.place_acf_from);
        placeAutocompleteFragmentFrom.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(Place place) {
                final LatLng latLng = place.getLatLng();
                if (mMarkerFrom != null) {
                    mMarkerFrom.remove();
                    if (mPolyline != null) {
                        mPolyline.remove();
                    }
                }
                mMarkerFrom = mGoogleMap.addMarker(new MarkerOptions().position(latLng).title(place.getName().toString()));
                boundsCalcAndZoom(mMarkerFrom, null);
                mPlaceFrom = place;
            }

            @Override
            public void onError(Status status) {

            }
        });

        placeAutocompleteFragmentTo = (PlaceAutocompleteFragment) getActivity().getFragmentManager().findFragmentById(R.id.place_acf_to);
        placeAutocompleteFragmentTo.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(Place place) {
                final LatLng latLng = place.getLatLng();
                if (mMarkerTo != null) {
                    mMarkerTo.remove();
                    if (mPolyline != null) {
                        mPolyline.remove();
                    }
                }
                mMarkerTo = mGoogleMap.addMarker(new MarkerOptions().position(latLng).title(place.getName().toString()));
                boundsCalcAndZoom(null, mMarkerTo);
                mPlaceTo = place;
            }

            @Override
            public void onError(Status status) {

            }
        });

        mMapView = (MapView) mView.findViewById(R.id.map);
        if (mMapView != null) {
            mMapView.onCreate(null);
            mMapView.onResume();
            mMapView.getMapAsync(this);
        }

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        MapsInitializer.initialize(getContext());

        mGoogleMap = googleMap;
        mGoogleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);

    }

    public void writeDB (final double latFrom, final double lngFrom, final String addressFrom, final String nameFrom, final double latTo, final double lngTo, final String addressTo, final String nameTo) {
        realm.executeTransactionAsync(new Realm.Transaction() {
            @Override
            public void execute(Realm bgRealm) {
                LocInfo locInfo = bgRealm.createObject(LocInfo.class);
                locInfo.setLatFrom(latFrom);
                locInfo.setLngFrom(lngFrom);
                locInfo.setAddressFrom(addressFrom);
                locInfo.setNameFrom(nameFrom);
                locInfo.setLatTo(latTo);
                locInfo.setLngTo(lngTo);
                locInfo.setAddressTo(addressTo);
                locInfo.setNamePlaceTo(nameTo);
            }
        }, new Realm.Transaction.OnSuccess() {
            @Override
            public void onSuccess() {
                // Transaction was a success.
            }
        }, new Realm.Transaction.OnError() {
            @Override
            public void onError(Throwable error) {
                // Transaction failed and was automatically canceled.
            }
        });
    }

    public void drawLine (double latFrom, double lngFrom, double latTo, double lngTo) {

        String base_url = "http://maps.googleapis.com/";

        Gson gson = new GsonBuilder()
                .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
                .create();

        RestAdapter restAdapter = new RestAdapter.Builder()
                .setEndpoint(base_url)
                .setLogLevel(RestAdapter.LogLevel.FULL)
                .build();

        MyApiRequestInterface reqinterface = restAdapter.create(MyApiRequestInterface.class);

        reqinterface.getJson(latFrom + "," + lngFrom, latTo + "," + lngTo, new Callback<DirectionResults>() {


            @Override
            public void success(DirectionResults directionResults, Response response) {
                ArrayList<LatLng> routelist = new ArrayList<LatLng>();
                if(directionResults.getRoutes().size()>0){
                    ArrayList<LatLng> decodelist;
                    Route routeA = directionResults.getRoutes().get(0);
                    if(routeA.getLegs().size()>0){
                        List<Steps> steps= routeA.getLegs().get(0).getSteps();
                        Steps step;
                        Location location;
                        String polyline;
                        for(int i=0 ; i<steps.size();i++){
                            step = steps.get(i);
                            location = step.getStart_location();
                            routelist.add(new LatLng(location.getLat(), location.getLng()));
                            polyline = step.getPolyline().getPoints();
                            decodelist = RouteDecode.decodePoly(polyline);
                            routelist.addAll(decodelist);
                            location =step.getEnd_location();
                            routelist.add(new LatLng(location.getLat() ,location.getLng()));
                        }
                    }
                }
                if(routelist.size()>0){
                    PolylineOptions rectLine = new PolylineOptions().width(10).color(
                            Color.RED);

                    for (int i = 0; i < routelist.size(); i++) {
                        rectLine.add(routelist.get(i));
                    }
                    // Adding route on the map
                    mPolyline = mGoogleMap.addPolyline(rectLine);

                }
            }

            @Override
            public void failure(RetrofitError retrofitError) {
                System.out.println("Failure, retrofitError" + retrofitError);
            }
        });

    }

    public void boundsCalcAndZoom (Marker From, Marker To) {
        LatLngBounds.Builder builder = new LatLngBounds.Builder();
        if (From != null) {
            builder.include(From.getPosition());
        }
        if (To != null) {
            builder.include(To.getPosition());
        }
        LatLngBounds bounds = builder.build();
        CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bounds, 100);
        mGoogleMap.moveCamera(cu);
        mGoogleMap.animateCamera(cu);
    }

}
