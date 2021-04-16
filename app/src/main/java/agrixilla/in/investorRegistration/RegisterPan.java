package agrixilla.in.investorRegistration;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Handler;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.google.android.material.textfield.TextInputLayout;
import androidx.fragment.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
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
import agrixilla.in.utils.CommonMethods;
import agrixilla.in.utils.ConnectionDetector;
import agrixilla.in.utils.MyValidator;
import agrixilla.in.utils.UtilitySharedPreferences;
import com.stepstone.stepper.BlockingStep;
import com.stepstone.stepper.StepperLayout;
import com.stepstone.stepper.VerificationError;
import com.stfalcon.smsverifycatcher.SmsVerifyCatcher;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import static agrixilla.in.webservices.RestClient.ROOT_URL;

public class RegisterPan extends Fragment implements BlockingStep {

    View rootView;
    String StrPan, StrProprietorName, StrAgency, StrMobile, StrTelephone, StrCustomerId, MobileVerified, StrCustomerStatus;
    EditText edt_Pan_Card;
    Button BtnValidatePan;
    TextView txt_resend_mobile_otp, txt_timer;
    EditText edt_Check_Mobile_Otp, edt_Check_Email_Otp;
    boolean emailOtpVerified = false;
    boolean mobileOtpVerified = false;
    private static final String TAG = "CustomerInfoRegist";

    StepperLayout.OnNextClickedCallback onmNextClickedCallback;

    TextInputLayout etPasswordLayout, etConfirmPasswordLayout;
    ProgressDialog myDialog;
    SmsVerifyCatcher smsVerifyCatcher;
    String mobileOtp;
    String MobileOtp, EmailPin;


    public RegisterPan() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView =  inflater.inflate(R.layout.fragment_pan_registration, container, false);

        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        init();
        // CommonMethods.hiddenKeyboard(rootView,getActivity());
        return rootView;
    }

    private void init() {



        edt_Pan_Card = (EditText)rootView.findViewById(R.id.edt_Pan_Card);

        BtnValidatePan = (Button)rootView.findViewById(R.id.BtnValidatePan);
        BtnValidatePan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(isValidPAN()){
                    fetchDataForPAN();

                }
            }
        });
    }



    private void fetchDataForPAN() {
        StrPan = edt_Pan_Card.getText().toString();

        String URL_user_info = ROOT_URL + "customer_details.php";
        try {
            Log.d("URL_USerInfo",URL_user_info);

            ConnectionDetector cd = new ConnectionDetector(getActivity());
            boolean isInternetPresent = cd.isConnectingToInternet();
            if (isInternetPresent) {

                final StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_user_info,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                 Log.d("mainResponse", response);

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
                                        StrCustomerStatus = jsonObject.getString("customer_status");
                                        StrMobile = jsonObject.getString("mobile");
                                        MobileVerified = jsonObject.getString("is_mobile_verified");

                                        String address1 = jsonObject.getString("address1");
                                        String address2 = jsonObject.getString("address2");
                                        String address3 = jsonObject.getString("address3");
                                        String pincode = jsonObject.getString("pincode");
                                        String town = jsonObject.getString("town");
                                        String territory = jsonObject.getString("territory");
                                        String city = jsonObject.getString("city");
                                        String region = jsonObject.getString("region");
                                        String state = jsonObject.getString("state");

                                        String gst_in_no = jsonObject.getString("gst_in_no");
                                        String pesticide_license_no = jsonObject.getString("pesticide_license_no");
                                        String email = jsonObject.getString("email");
                                        String is_email_verified = jsonObject.getString("is_email_verified");

                                        UtilitySharedPreferences.setPrefs(getActivity(),"ClientCode",StrCustomerId);
                                        UtilitySharedPreferences.setPrefs(getActivity(),"ClientPan",StrPan);
                                        UtilitySharedPreferences.setPrefs(getActivity(),"ClientFirmName",StrAgency);
                                        UtilitySharedPreferences.setPrefs(getActivity(),"ClientProprietorName",StrProprietorName);
                                        UtilitySharedPreferences.setPrefs(getActivity(),"ClientTelephone",StrTelephone);
                                        UtilitySharedPreferences.setPrefs(getActivity(),"ClientMobile",StrMobile);
                                        UtilitySharedPreferences.setPrefs(getActivity(),"MobileVerified",MobileVerified);
                                        UtilitySharedPreferences.setPrefs(getActivity(),"StrCustomerStatus",StrCustomerStatus);


                                        UtilitySharedPreferences.setPrefs(getActivity(),"ClientAddress1",address1);
                                        UtilitySharedPreferences.setPrefs(getActivity(),"ClientAddress2",address2);
                                        UtilitySharedPreferences.setPrefs(getActivity(),"ClientAddress2",address3);
                                        UtilitySharedPreferences.setPrefs(getActivity(),"ClientPincode",pincode);
                                        UtilitySharedPreferences.setPrefs(getActivity(),"town",town);
                                        UtilitySharedPreferences.setPrefs(getActivity(),"territory",territory);
                                        UtilitySharedPreferences.setPrefs(getActivity(),"city",city);
                                        UtilitySharedPreferences.setPrefs(getActivity(),"region",region);
                                        UtilitySharedPreferences.setPrefs(getActivity(),"state",state);
                                        UtilitySharedPreferences.setPrefs(getActivity(),"ClientPesticideLicenseNo",pesticide_license_no);
                                        UtilitySharedPreferences.setPrefs(getActivity(),"ClientGstInNo",gst_in_no);
                                        UtilitySharedPreferences.setPrefs(getActivity(),"ClientEmail",email);
                                        UtilitySharedPreferences.setPrefs(getActivity(),"is_email_verified",is_email_verified);

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

    private boolean isValidPAN() {
        boolean result = true;

        if (!MyValidator.isValidPan(edt_Pan_Card)) {
            edt_Pan_Card.requestFocus();
            result = false;
        }
        return  result;
    }


    @Override
    public void onNextClicked(final StepperLayout.OnNextClickedCallback onNextClickedCallback) {
        onNextClickedCallback.goToNextStep();
        if(isValidPAN()){
            fetchDataForPAN();
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    onNextClickedCallback.goToNextStep();
                    //onNextClickedCallback.getStepperLayout().hideProgress();
                }
            }, 200L);

        }
    }

    @Override
    public void onCompleteClicked(StepperLayout.OnCompleteClickedCallback callback) {

    }

    @Override
    public void onBackClicked(StepperLayout.OnBackClickedCallback callback) {

    }

    @Nullable
    @Override
    public VerificationError verifyStep() {
        return null;
    }

    @Override
    public void onSelected() {

    }

    @Override
    public void onError(@NonNull VerificationError error) {

    }
}