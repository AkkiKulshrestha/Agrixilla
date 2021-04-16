package agrixilla.in.investorRegistration;


import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;

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
import agrixilla.in.activities.LoginActivity;
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
import java.util.Objects;

import static agrixilla.in.webservices.RestClient.ROOT_URL;


/**
 * A simple {@link Fragment} subclass.
 */
public class OtherInfoFragment extends Fragment implements BlockingStep {



   EditText edtEmailId,edt_Password,edt_Confirm_Password,Edt_GstInNo,Edt_PesticidesLicenseNo;

    ProgressDialog myDialog;
    String StrCustomerId,StrEmail,StrClientGstInNo,StrPesticidesLicenseNo,StrPassword,EmailVerified;

    Button BtnDone;
    View rootView;



    public OtherInfoFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_other_info, container, false);

       init();

        return rootView;
    }

    private void init() {

        StrCustomerId = UtilitySharedPreferences.getPrefs(getActivity(),"ClientCode");
        StrEmail = UtilitySharedPreferences.getPrefs(getActivity(),"ClientEmail");
        StrClientGstInNo = UtilitySharedPreferences.getPrefs(getActivity(),"ClientGstInNo");
        StrPesticidesLicenseNo = UtilitySharedPreferences.getPrefs(getActivity(),"ClientPesticideLicenseNo");
        EmailVerified = UtilitySharedPreferences.getPrefs(getActivity(),"is_email_verified");

        edtEmailId= (EditText) rootView.findViewById(R.id.edt_Email_Addresss);
        edt_Password= (EditText) rootView.findViewById(R.id.edt_Password);
        edt_Confirm_Password= (EditText) rootView.findViewById(R.id.edt_Confirm_Password);
        Edt_GstInNo = (EditText) rootView.findViewById(R.id.Edt_GstInNo);
        Edt_PesticidesLicenseNo = (EditText) rootView.findViewById(R.id.Edt_PesticidesLicenseNo);


        if(StrEmail!=null && !StrEmail.equalsIgnoreCase("") && !StrEmail.equalsIgnoreCase("null")){
            edtEmailId.setText(StrEmail);
        }

        if(StrClientGstInNo!=null && !StrClientGstInNo.equalsIgnoreCase("") && !StrClientGstInNo.equalsIgnoreCase("null")){
            Edt_GstInNo.setText(StrClientGstInNo);
        }
        if(StrPesticidesLicenseNo!=null && !StrPesticidesLicenseNo.equalsIgnoreCase("") && !StrPesticidesLicenseNo.equalsIgnoreCase("null")){
            Edt_PesticidesLicenseNo.setText(StrPesticidesLicenseNo);
        }

        BtnDone = (Button)rootView.findViewById(R.id.BtnDone);
        BtnDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validateFeilds()) {

                    PostApiForSavingData();


                }
            }
        });

    }
    @Override
    public void onNextClicked(final StepperLayout.OnNextClickedCallback onNextClickedCallback) {


    }



    private void PostApiForSavingData() {
        InputMethodManager imm = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(edt_Confirm_Password.getWindowToken(), 0);

        myDialog = new ProgressDialog(getActivity());
        myDialog.setCancelable(false);
        myDialog.setCanceledOnTouchOutside(false);
        myDialog.setMessage("Please Wait...");
        myDialog.show();


        StrEmail = edtEmailId.getText().toString().trim();
        StrClientGstInNo = Edt_GstInNo.getText().toString().trim();
        StrPesticidesLicenseNo = Edt_PesticidesLicenseNo.getText().toString().trim();
        StrPassword = edt_Password.getText().toString().trim();



        String URL_user_info = ROOT_URL + "save_other_details.php";
        try {
            Log.d("URL_USerInfo",URL_user_info);

            ConnectionDetector cd = new ConnectionDetector(getActivity());
            boolean isInternetPresent = cd.isConnectingToInternet();
            if (isInternetPresent) {

                final StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_user_info,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                if (myDialog != null && myDialog.isShowing()) {
                                    myDialog.dismiss();
                                }
                                try {
                                    Log.d("mainResponse", response);

                                   JSONObject jobj = new JSONObject(response);
                                    boolean status = jobj.getBoolean("status");
                                    Intent intent = new Intent(getActivity().getApplication(), LoginActivity.class);
                                    startActivity(intent);

                                } catch (JSONException e) {
                                    if (myDialog != null && myDialog.isShowing()) {
                                        myDialog.dismiss();
                                    }
                                    e.printStackTrace();
                                }

                            }
                        }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        if (myDialog != null && myDialog.isShowing()) {
                            myDialog.dismiss();
                        }
                        VolleyLog.d("volley", "Error: " + error.getMessage());
                        error.printStackTrace();

                    }
                }) {


                    @Override
                    public Map<String, String> getParams() throws AuthFailureError {
                        Map<String, String> params = new HashMap<String, String>();
                        params.put("customer_id", StrCustomerId);
                        params.put("gstnumber", StrClientGstInNo);
                        params.put("liciencenumber", StrPesticidesLicenseNo);
                        params.put("email", StrEmail);
                        params.put("password",StrPassword);


                        Log.d("ParrasInvestor", params.toString());
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

    private boolean validateFeilds() {
        boolean result = true;

        if (!MyValidator.isValidPassword(edt_Password)) {
            edt_Password.requestFocus();
            result = false;
        }
        if (!MyValidator.isValidPassword(edt_Confirm_Password)) {
            edt_Confirm_Password.requestFocus();
            result = false;
        }

        if(!edt_Password.getText().toString().trim().equals(edt_Confirm_Password.getText().toString().trim())){
            edt_Confirm_Password.setError("Password & Confirm Password Does not Match.");
            edt_Confirm_Password.requestFocus();
            result = false;
        }



        return result;
    }

    @Override
    public void onCompleteClicked(final StepperLayout.OnCompleteClickedCallback onCompleteClickedCallback) {
        Intent intent = new Intent(Objects.requireNonNull(getActivity()).getApplication(), LoginActivity.class);
        startActivity(intent);
        if (validateFeilds()) {

            PostApiForSavingData();


        }

    }

    @Override
    public void onBackClicked(StepperLayout.OnBackClickedCallback onBackClickedCallback) {

        onBackClickedCallback.goToPrevStep();
    }

    @Nullable
    @Override
    public VerificationError verifyStep() {

        if(!validateFeilds()) {
            return new VerificationError("");
        }

        return null;
    }

    @Override
    public void onSelected() {

    }

    @Override
    public void onError(@NonNull VerificationError verificationError) {

    }


}
