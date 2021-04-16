package agrixilla.in.account_screens;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

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
import agrixilla.in.activities.NewDashboard;
import agrixilla.in.utils.CommonMethods;
import agrixilla.in.utils.ConnectionDetector;
import agrixilla.in.utils.MyValidator;
import agrixilla.in.utils.UtilitySharedPreferences;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import static agrixilla.in.webservices.RestClient.ROOT_URL;

public class ManageProfile extends AppCompatActivity {

    ImageView back_btn;
    EditText Edt_Proprietor_Name,Edt_Agency_Name,Edt_AlternateNo,EdtEmailId,Edt_AddressPincode,Edt_Address1,Edt_Address2,Edt_Address3,Edt_City,Edt_State;
    String CityId,state_code;
    ProgressDialog myDialog;
    String StrPan="",StrAddress1="",StrAddress2="",StrAddress3="",StrTown="",StrCity="",StrPincode="",StrState="",StrCustomerId="",StrEmail="",StrAgency="",StrProprietorName="",StrTelephone="",StrMobile="";
    Button btn_update;
    TextView tv_user_id_text;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_profile_page);

        init();
    }

    private void init() {
        back_btn = (ImageView) findViewById(R.id.back_btn);
        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(),NewDashboard.class);
                i.putExtra("viewpager_position", 2);
                startActivity(i);
                overridePendingTransition(R.animator.left_right,R.animator.right_left);
                finish();
            }
        });

        TextView til_text = (TextView)findViewById(R.id.til_text);
        til_text.setText("PROFILE DETAILS");


        StrPan = UtilitySharedPreferences.getPrefs(getApplicationContext(),"ClientPan");
        StrCustomerId = UtilitySharedPreferences.getPrefs(getApplicationContext(),"ClientCode");
        StrAddress1 = UtilitySharedPreferences.getPrefs(getApplicationContext(),"ClientAddress1");
        StrAddress2 = UtilitySharedPreferences.getPrefs(getApplicationContext(),"ClientAddress2");
        StrAddress3 = UtilitySharedPreferences.getPrefs(getApplicationContext(),"ClientAddress3");
        StrPincode = UtilitySharedPreferences.getPrefs(getApplicationContext(),"ClientPincode");
        StrTown = UtilitySharedPreferences.getPrefs(getApplicationContext(),"town");
        StrCity =  UtilitySharedPreferences.getPrefs(getApplicationContext(),"city");
        StrState = UtilitySharedPreferences.getPrefs(getApplicationContext(),"state");
        StrEmail = UtilitySharedPreferences.getPrefs(getApplicationContext(),"email");
        StrAgency =  UtilitySharedPreferences.getPrefs(getApplicationContext(),"ClientFirmName");
        StrProprietorName = UtilitySharedPreferences.getPrefs(getApplicationContext(),"ClientProprietorName");
        StrTelephone =  UtilitySharedPreferences.getPrefs(getApplicationContext(),"ClientTelephone");
        StrMobile = UtilitySharedPreferences.getPrefs(getApplicationContext(),"ClientMobile");

        
        tv_user_id_text = (TextView)findViewById(R.id.tv_user_id_text);
        Edt_Proprietor_Name = (EditText)findViewById(R.id.edt_Proprietor_Name);
        Edt_Agency_Name = (EditText)findViewById(R.id.edt_Agency_Name);
        Edt_AlternateNo = (EditText)findViewById(R.id.Edt_AlternateNo);
        EdtEmailId= (EditText) findViewById(R.id.edt_Email_Addresss);
        Edt_AddressPincode = (EditText)findViewById(R.id.Edt_AddressPincode);
        Edt_Address1= (EditText)findViewById(R.id.Edt_Address1);
        Edt_Address2= (EditText)findViewById(R.id.Edt_Address2);
        Edt_Address3= (EditText)findViewById(R.id.Edt_Address3);
        Edt_City= (EditText)findViewById(R.id.Edt_City);
        Edt_State= (EditText)findViewById(R.id.Edt_State);

        btn_update = (Button)findViewById(R.id.btn_update);
        
        
        if(StrAgency!=null && !StrAgency.equalsIgnoreCase("") && !StrAgency.equalsIgnoreCase("null")){
            Edt_Agency_Name.setText(StrAgency);
        }

        if(StrProprietorName!=null && !StrProprietorName.equalsIgnoreCase("") && !StrProprietorName.equalsIgnoreCase("null") ){
            Edt_Proprietor_Name.setText(StrProprietorName);
        }


        if(StrTelephone!=null && !StrTelephone.equalsIgnoreCase("") && !StrTelephone.equalsIgnoreCase("null") && !StrTelephone.equalsIgnoreCase("0")){
            Edt_AlternateNo.setText(StrTelephone);
        }

        if(StrEmail!=null && !StrEmail.equalsIgnoreCase("") && !StrEmail.equalsIgnoreCase("null")){
            EdtEmailId.setText(StrEmail);
        }


        if(StrAddress1!=null && !StrAddress1.equalsIgnoreCase("") && !StrAddress1.equalsIgnoreCase("null")){
            Edt_Address1.setText(StrAddress1);
        }

        if(StrAddress2!=null && !StrAddress2.equalsIgnoreCase("") && !StrAddress2.equalsIgnoreCase("null")){
            Edt_Address2.setText(StrAddress2);
        }

        if(StrAddress3!=null && !StrAddress3.equalsIgnoreCase("") && !StrAddress3.equalsIgnoreCase("null")){
            Edt_Address3.setText(StrAddress3);
        }

        if(StrPincode!=null && !StrPincode.equalsIgnoreCase("") && !StrPincode.equalsIgnoreCase("null")){
            Edt_AddressPincode.setText(StrPincode);
        }



        if(StrCity!=null && !StrCity.equalsIgnoreCase("") && !StrCity.equalsIgnoreCase("null")){
            Edt_City.setText(StrCity);
        }

        if(StrState!=null && !StrState.equalsIgnoreCase("") && !StrState.equalsIgnoreCase("null")){
            Edt_State.setText(StrState);
        }

        if(StrMobile!=null && !StrMobile.equalsIgnoreCase("") && !StrMobile.equalsIgnoreCase("null")){
            tv_user_id_text.setText("USER ID - "+StrMobile);
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

        btn_update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(IsValidated()){
                    ApiSaveDetails();
                }
            }
        });
        if(StrPan!=null && !StrPan.equalsIgnoreCase("") && !StrPan.equalsIgnoreCase("null")){
            fetchDataForPAN();
        }
        
    }



    private void fetchDataForPAN() {
        

        String URL_user_info = ROOT_URL + "customer_details.php";
        try {
            Log.d("URL_USerInfo",URL_user_info);

            ConnectionDetector cd = new ConnectionDetector(getApplicationContext());
            boolean isInternetPresent = cd.isConnectingToInternet();
            if (isInternetPresent) {

                final StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_user_info,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                // Log.d("mainResponse", response);

                                try {

                                    JSONObject jObj = new JSONObject(response);

                                    boolean status = jObj.getBoolean("status");
                                    String data = jObj.getString("data");
                                    if(status){

                                        JSONObject jsonObject = new JSONObject(data);
                                        StrCustomerId = jsonObject.getString("customer_id");
                                        StrAgency = jsonObject.getString("firm_name");
                                        StrProprietorName = jsonObject.getString("propritor_name");
                                        StrTelephone = jsonObject.getString("telephone_no");
                                        //String StrCustomerStatus = jsonObject.getString("customer_status");
                                        StrMobile = jsonObject.getString("mobile");
                                        //MobileVerified = jsonObject.getString("is_mobile_verified");

                                        StrAddress1 = jsonObject.getString("address1");
                                        StrAddress2 = jsonObject.getString("address2");
                                        StrAddress3 = jsonObject.getString("address3");
                                        StrPincode = jsonObject.getString("pincode");
                                        StrTown = jsonObject.getString("town");
                                        String territory = jsonObject.getString("territory");
                                        StrCity = jsonObject.getString("city");
                                        StrState = jsonObject.getString("state");

                                        String gst_in_no = jsonObject.getString("gst_in_no");
                                        String pesticide_license_no = jsonObject.getString("pesticide_license_no");
                                        StrEmail = jsonObject.getString("email");
                                        //String is_email_verified = jsonObject.getString("is_email_verified");


                                        if(StrAgency!=null && !StrAgency.equalsIgnoreCase("") && !StrAgency.equalsIgnoreCase("null")){
                                            Edt_Agency_Name.setText(StrAgency);
                                        }

                                        if(StrProprietorName!=null && !StrProprietorName.equalsIgnoreCase("") && !StrProprietorName.equalsIgnoreCase("null") ){
                                            Edt_Proprietor_Name.setText(StrProprietorName);
                                        }


                                        if(StrTelephone!=null && !StrTelephone.equalsIgnoreCase("") && !StrTelephone.equalsIgnoreCase("null") && !StrTelephone.equalsIgnoreCase("0")){
                                            Edt_AlternateNo.setText(StrTelephone);
                                        }

                                        if(StrEmail!=null && !StrEmail.equalsIgnoreCase("") && !StrEmail.equalsIgnoreCase("null")){
                                            EdtEmailId.setText(StrEmail);
                                        }


                                        if(StrAddress1!=null && !StrAddress1.equalsIgnoreCase("") && !StrAddress1.equalsIgnoreCase("null")){
                                            Edt_Address1.setText(StrAddress1);
                                        }

                                        if(StrAddress2!=null && !StrAddress2.equalsIgnoreCase("") && !StrAddress2.equalsIgnoreCase("null")){
                                            Edt_Address2.setText(StrAddress2);
                                        }

                                        if(StrAddress3!=null && !StrAddress3.equalsIgnoreCase("") && !StrAddress3.equalsIgnoreCase("null")){
                                            Edt_Address3.setText(StrAddress3);
                                        }

                                        if(StrPincode!=null && !StrPincode.equalsIgnoreCase("") && !StrPincode.equalsIgnoreCase("null")){
                                            Edt_AddressPincode.setText(StrPincode);
                                        }



                                        if(StrCity!=null && !StrCity.equalsIgnoreCase("") && !StrCity.equalsIgnoreCase("null")){
                                            Edt_City.setText(StrCity);
                                        }

                                        if(StrState!=null && !StrState.equalsIgnoreCase("") && !StrState.equalsIgnoreCase("null")){
                                            Edt_State.setText(StrState);
                                        }

                                        if(StrMobile!=null && !StrMobile.equalsIgnoreCase("") && !StrMobile.equalsIgnoreCase("null")){
                                            tv_user_id_text.setText("USER ID - "+StrMobile);
                                        }

                                    }


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
                        params.put("pancard", StrPan);

                        Log.d("ParrasRoboLumpsum", params.toString());
                        return params;

                    }
                };

                int socketTimeout = 50000; //30 seconds - change to what you want
                RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
                stringRequest.setRetryPolicy(policy);
                // RequestQueue requestQueue = Volley.newRequestQueue(this, new HurlStack(null, getSocketFactory()));
                RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
                requestQueue.add(stringRequest);

            } else {
                if(myDialog!=null && myDialog.isShowing()) {
                    myDialog.dismiss();
                }
                CommonMethods.DisplayToast(getApplicationContext(), "Please check your internet connection");
            }
        } catch (Exception e) {

            e.printStackTrace();

        }

    }


    private boolean IsValidated() {

        boolean result = true;

        if (!MyValidator.isValidField(Edt_Agency_Name)) {
            Edt_Agency_Name.requestFocus();
            result = false;
        }

        if (!MyValidator.isValidName(Edt_Proprietor_Name)) {
            Edt_Proprietor_Name.requestFocus();
            result = false;
        }

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

    private void ApiSaveDetails() {

        InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(Edt_State.getWindowToken(), 0);

        StrAddress1 = Edt_Address1.getText().toString().trim();
        StrAddress2 = Edt_Address2.getText().toString().trim();
        StrPincode = Edt_AddressPincode.getText().toString().trim();
        StrCity = Edt_City.getText().toString().trim();
        StrState = Edt_State.getText().toString().trim();
        StrProprietorName = Edt_Proprietor_Name.getText().toString();
        StrAgency = Edt_Agency_Name.getText().toString();

        if(Edt_Address3.getText().toString()!=null && !Edt_Address3.getText().toString().equalsIgnoreCase("")){
            StrAddress3 = Edt_Address3.getText().toString();

        }else {
            StrAddress3 = "";
        }

        if(Edt_AlternateNo.getText().toString()!=null && !Edt_AlternateNo.getText().toString().equalsIgnoreCase("")){
            StrTelephone = Edt_AlternateNo.getText().toString();

        }else {
            StrTelephone = "";
        }

        if(EdtEmailId.getText().toString()!=null && !EdtEmailId.getText().toString().equalsIgnoreCase("")){
            StrEmail = EdtEmailId.getText().toString();

        }else {
            StrEmail = "";
        }


        String URL_user_info = ROOT_URL + "update_profile_info.php";
        try {
            Log.d("URL_USerInfo",URL_user_info);

            ConnectionDetector cd = new ConnectionDetector(getApplicationContext());
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
                        params.put("UserId", StrCustomerId);
                        params.put("Address1", StrAddress1);
                        params.put("Address2", StrAddress2);
                        params.put("Address3", StrAddress3);
                        params.put("Pincode", StrPincode);
                        params.put("FirmName",StrAgency);
                        params.put("PropritorName",StrProprietorName);
                        params.put("Email",StrEmail);
                        params.put("AlternateNo",StrTelephone);
                        params.put("City", StrCity);
                        params.put("State", StrState);

                        Log.d("ParrasContact", params.toString());
                        return params;

                    }
                };

                int socketTimeout = 50000; //30 seconds - change to what you want
                RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
                stringRequest.setRetryPolicy(policy);
                // RequestQueue requestQueue = Volley.newRequestQueue(this, new HurlStack(null, getSocketFactory()));
                RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
                requestQueue.add(stringRequest);

            } else {
                if(myDialog!=null && myDialog.isShowing()) {
                    myDialog.dismiss();
                }
                CommonMethods.DisplayToast(getApplicationContext(), "Please check your internet connection");
            }
        } catch (Exception e) {

            e.printStackTrace();

        }
    }


    private void ApiToGetCityState(String strPincode) {

        String URL_Check_Pincode = "http://www.mymfnow.com/invest/getCityStateForPincode?pincode="+strPincode;
        try {
            Log.d("URL",URL_Check_Pincode);

            ConnectionDetector cd = new ConnectionDetector(getApplicationContext());
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
                RequestQueue requestQueue = Volley.newRequestQueue(this);
                requestQueue.add(stringRequest);



            } else {
                CommonMethods.DisplayToast(this, "Please check your internet connection");
            }
        } catch (Exception e) {

            e.printStackTrace();

        }


    }


    @Override
    public void onBackPressed() {
        Intent i = new Intent(getApplicationContext(),NewDashboard.class);
        i.putExtra("viewpager_position", 2);
        startActivity(i);
        overridePendingTransition(R.animator.left_right,R.animator.right_left);
        finish();
    }
}
