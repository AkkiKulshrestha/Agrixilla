package agrixilla.in.investorRegistration;


import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import androidx.annotation.UiThread;
import androidx.fragment.app.Fragment;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Spinner;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import agrixilla.in.R;
import agrixilla.in.utils.CommonMethods;
import agrixilla.in.utils.ConnectionDetector;
import agrixilla.in.utils.MyValidator;
import agrixilla.in.utils.UtilitySharedPreferences;
import com.stepstone.stepper.BlockingStep;
import com.stepstone.stepper.StepperLayout;
import com.stepstone.stepper.VerificationError;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import static agrixilla.in.webservices.RestClient.ROOT_URL;


/**
 * A simple {@link Fragment} subclass.
 */
public class ContactInfoFragment extends Fragment implements BlockingStep {

    Context context;
    View rootView;
    EditText Edt_AddressPincode,Edt_Address1,Edt_Address2,Edt_Address3,Edt_Town,Edt_Territory,Edt_Region,Edt_City,Edt_State;
    String CityId,state_code;
    ProgressDialog myDialog;
    String StrAddress1,StrAddress2,StrAddress3,StrTown,StrTerritory,StrCity,StrPincode,StrState,StrCustomerId,StrRegion;

    public ContactInfoFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_contact_info, container, false);

        context = getContext();
        init();

        return rootView;
    }

    private void init() {

        StrCustomerId = UtilitySharedPreferences.getPrefs(getActivity(),"ClientCode");
        StrAddress1 = UtilitySharedPreferences.getPrefs(getActivity(),"ClientAddress1");
        StrAddress2 = UtilitySharedPreferences.getPrefs(getActivity(),"ClientAddress2");
        StrAddress3 = UtilitySharedPreferences.getPrefs(getActivity(),"ClientAddress2");
        StrPincode = UtilitySharedPreferences.getPrefs(getActivity(),"ClientPincode");
        StrTown = UtilitySharedPreferences.getPrefs(getActivity(),"town");
        StrCity =  UtilitySharedPreferences.getPrefs(getActivity(),"city");
        StrState = UtilitySharedPreferences.getPrefs(getActivity(),"state");
        StrTown = UtilitySharedPreferences.getPrefs(getActivity(),"town");
        StrTerritory = UtilitySharedPreferences.getPrefs(getActivity(),"territory");
        StrRegion = UtilitySharedPreferences.getPrefs(getActivity(),"region");

        Edt_AddressPincode = (EditText)rootView.findViewById(R.id.Edt_AddressPincode);
        Edt_Address1= (EditText)rootView.findViewById(R.id.Edt_Address1);
        Edt_Address2= (EditText)rootView.findViewById(R.id.Edt_Address2);
        Edt_Address3= (EditText)rootView.findViewById(R.id.Edt_Address3);

        Edt_Town = (EditText)rootView.findViewById(R.id.Edt_Town);
        Edt_Territory = (EditText)rootView.findViewById(R.id.Edt_Territory);
        Edt_Region = (EditText)rootView.findViewById(R.id.Edt_Region);
        Edt_City= (EditText)rootView.findViewById(R.id.Edt_City);
        Edt_State= (EditText)rootView.findViewById(R.id.Edt_State);



        if(StrAddress1!=null && !StrAddress1.equalsIgnoreCase("") && !StrAddress1.equalsIgnoreCase("null")){
            Edt_Address1.setText(StrAddress1);
        }

        if(StrAddress2!=null && !StrAddress2.equalsIgnoreCase("") && !StrAddress2.equalsIgnoreCase("null")){
            Edt_Address2.setText(StrAddress2);
        }

        if(StrAddress3!=null && !StrAddress3.equalsIgnoreCase("") && !StrAddress3.equalsIgnoreCase("null")){
            Edt_Address3.setText(StrAddress3);
        }

        if(StrCity!=null && !StrCity.equalsIgnoreCase("") && !StrCity.equalsIgnoreCase("null")){
            Edt_City.setText(StrCity);
        }

        if(StrTown!=null && !StrTown.equalsIgnoreCase("") && !StrTown.equalsIgnoreCase("null")){
            Edt_Town.setText(StrTown);
        }

        if(StrTerritory!=null && !StrTerritory.equalsIgnoreCase("") && !StrTerritory.equalsIgnoreCase("null")){
            Edt_Territory.setText(StrTerritory);
        }


        if(StrRegion!=null && !StrRegion.equalsIgnoreCase("") && !StrRegion.equalsIgnoreCase("null")){
                Edt_Region.setText(StrRegion);
        }

        if(StrPincode!=null && !StrPincode.equalsIgnoreCase("") && !StrPincode.equalsIgnoreCase("null")){
            Edt_AddressPincode.setText(StrPincode);
        }

        if(StrState!=null && !StrState.equalsIgnoreCase("") && !StrState.equalsIgnoreCase("null")){
            Edt_State.setText(StrState);
        }


        Edt_AddressPincode.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(s.length()==6){

                    String StrPincode = s.toString();
                    Log.d("StrPincode",StrPincode);
                    ApiToGetCityState(StrPincode);


                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


    }



    private int getIndex(Spinner spinner, String searchString) {

        if (searchString == null || spinner.getCount() == 0) {

            return -1; // Not found

        } else {

            for (int i = 0; i < spinner.getCount(); i++) {
                if (spinner.getItemAtPosition(i).toString().equalsIgnoreCase(searchString)) {
                    return i; // Found!
                }
            }

            return -1; // Not found
        }
    }


    private void ApiToGetCityState(String strPincode) {

        String URL_Check_Pincode = "http://www.mymfnow.com/invest/getCityStateForPincode?pincode="+strPincode;
        try {
            Log.d("URL",URL_Check_Pincode);

            ConnectionDetector cd = new ConnectionDetector(context);
            boolean isInternetPresent = cd.isConnectingToInternet();
            if (isInternetPresent) {

                StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_Check_Pincode,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                Log.d("Response",response);
                                try {
                            /*  ":"Thane","":"Maharashtra","pincode":"401107","*/
                                    JSONObject jobj = new JSONObject(response);

                                    //CityId  = jobj.getString("id");
                                    String city = jobj.getString("city");
                                    String state = jobj.getString("state");
                                    state_code  = jobj.getString("state_code");

                                    if(city!=null && !city.equalsIgnoreCase("")){
                                        Edt_City.setText(city);
                                    }else {
                                        Edt_City.setText("");
                                    }

                                    if(state!=null && !state.equalsIgnoreCase("")){
                                        Edt_State.setText(state);
                                    }else{
                                        Edt_State.setText("");
                                    }

                                } catch (Exception e) {

                                    e.printStackTrace();
                                }

                            }
                        }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        VolleyLog.d("volley", "Error: " + error.getMessage());
                        error.printStackTrace();

                    }
                });

                int socketTimeout = 50000;//30 seconds - change to what you want
                RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
                stringRequest.setRetryPolicy(policy);
                RequestQueue requestQueue = Volley.newRequestQueue(context);
                requestQueue.add(stringRequest);



            } else {
                CommonMethods.DisplayToast(context, "Please check your internet connection");
            }
        } catch (Exception e) {

            e.printStackTrace();

        }


    }

    @Nullable
    @Override
    public VerificationError verifyStep() {
        if(!validateFeilds()) {
            return new VerificationError("");
        }

        return null;
    }



    private boolean validateFeilds() {
        boolean result = true;

        if (!MyValidator.isValidField(Edt_AddressPincode)) {
            Edt_AddressPincode.requestFocus();
            result = false;
        }

        if (!MyValidator.isValidField(Edt_Address1)) {
            Edt_Address1.requestFocus();
            result = false;
        }

        if (!MyValidator.isValidField(Edt_Address2)) {
            Edt_Address2.requestFocus();
            result = false;
        }

        if (!MyValidator.isValidField(Edt_City)) {
            Edt_City.requestFocus();
            result = false;
        }

        if (!MyValidator.isValidField(Edt_State)) {
            Edt_State.requestFocus();
            result = false;
        }


        return  result;
    }

    @Override
    public void onSelected() {

    }

    @Override
    public void onError(@NonNull VerificationError verificationError) {
    }

    @Override
    public void onNextClicked(final StepperLayout.OnNextClickedCallback onNextClickedCallback) {
        onNextClickedCallback.goToNextStep();
        if (validateFeilds()) {
            ApiSaveContactDetails();
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    onNextClickedCallback.goToNextStep();
                    //onNextClickedCallback.getStepperLayout().hideProgress();
                }
            }, 200L);
        }
    }

    private void ApiSaveContactDetails() {

        InputMethodManager imm = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(Edt_State.getWindowToken(), 0);

        final String Address1 = Edt_Address1.getText().toString().trim();
        final String Address2 = Edt_Address1.getText().toString().trim();
        final String Address3 = Edt_Address1.getText().toString().trim();
        final String Pincode = Edt_AddressPincode.getText().toString().trim();
        final String City = Edt_City.getText().toString().trim();
        final String State = Edt_State.getText().toString().trim();
        final String Town = Edt_Town.getText().toString();
        final String Territorry = Edt_Territory.getText().toString();
        final String Region = Edt_Region.getText().toString();

        String URL_user_info = ROOT_URL + "save_contact_details.php";
        try {
            Log.d("URL_USerInfo",URL_user_info);

            ConnectionDetector cd = new ConnectionDetector(getActivity());
            boolean isInternetPresent = cd.isConnectingToInternet();
            if (isInternetPresent) {

                final StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_user_info,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {

                                try {
                                    Log.d("mainResponse", response);

                                    JSONObject jobj = new JSONObject(response);
                                    boolean status = jobj.getBoolean("status");


                                } catch (JSONException e) {

                                    e.printStackTrace();
                                }

                            }
                        }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {

                        VolleyLog.d("volley", "Error: " + error.getMessage());
                        error.printStackTrace();

                    }
                }) {


                    @Override
                    public Map<String, String> getParams() throws AuthFailureError {
                        Map<String, String> params = new HashMap<String, String>();
                        params.put("customer_id", StrCustomerId);
                        params.put("addr1", Address1);
                        params.put("addr2", Address2);
                        params.put("addr3", Address3);
                        params.put("pincode", Pincode);
                        params.put("town",Town);
                        params.put("territory",Territorry);
                        params.put("region",Region);
                        params.put("city", City);
                        params.put("state", State);

                        Log.d("ParrasContact", params.toString());
                        return params;

                    }
                };

                int socketTimeout = 50000; //30 seconds - change to what you want
                RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
                stringRequest.setRetryPolicy(policy);
                // RequestQueue requestQueue = Volley.newRequestQueue(this, new HurlStack(null, getSocketFactory()));
                RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
                requestQueue.add(stringRequest);

            } else {
                if(myDialog!=null && myDialog.isShowing()) {
                    myDialog.dismiss();
                }
                CommonMethods.DisplayToast(getActivity(), "Please check your internet connection");
            }
        } catch (Exception e) {

            e.printStackTrace();

        }
    }

    @Override
    public void onCompleteClicked(StepperLayout.OnCompleteClickedCallback onCompleteClickedCallback) {

    }

    @Override
    public void onBackClicked(StepperLayout.OnBackClickedCallback onBackClickedCallback) {
        onBackClickedCallback.goToPrevStep();
    }
}
