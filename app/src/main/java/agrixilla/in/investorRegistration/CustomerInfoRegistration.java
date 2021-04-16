package agrixilla.in.investorRegistration;


import android.app.ActionBar;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.os.CountDownTimer;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.google.android.material.textfield.TextInputLayout;
import androidx.fragment.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

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
import com.stfalcon.smsverifycatcher.OnSmsCatchListener;
import com.stfalcon.smsverifycatcher.SmsVerifyCatcher;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static agrixilla.in.webservices.RestClient.ROOT_URL;


/**
 * A simple {@link Fragment} subclass.
 */
public class CustomerInfoRegistration extends Fragment implements BlockingStep {

  View rootView;
    String StrPan,StrProprietorName,StrAgency,StrMobile,StrTelephone,StrCustomerId,MobileVerified,StrCustomerStatus;
    EditText edtCustomerCode,edt_Proprietor_Name,edt_Agency_Name,edt_Mobile_Number,edt_Telephone;
    Button BtnValidatePan;
    TextView txt_resend_mobile_otp,txt_timer;
    EditText edt_Check_Mobile_Otp,edt_Check_Email_Otp;
    boolean emailOtpVerified = false;
    boolean mobileOtpVerified = false;
    private static final String TAG = "CustomerInfoRegist";


    TextInputLayout etPasswordLayout,etConfirmPasswordLayout;
    ProgressDialog myDialog;
    SmsVerifyCatcher smsVerifyCatcher;
    String mobileOtp;
    String MobileOtp,EmailPin;
    Button BtnVerifyMobile;


    public CustomerInfoRegistration() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView =  inflater.inflate(R.layout.fragment_user_registration, container, false);

        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        init();
       // CommonMethods.hiddenKeyboard(rootView,getActivity());
        return rootView;
    }

  private void init() {

      StrPan = UtilitySharedPreferences.getPrefs(getActivity(),"ClientPan");
      StrCustomerId = UtilitySharedPreferences.getPrefs(getActivity(),"ClientCode");
      StrAgency =  UtilitySharedPreferences.getPrefs(getActivity(),"ClientFirmName");
      StrProprietorName = UtilitySharedPreferences.getPrefs(getActivity(),"ClientProprietorName");
      StrTelephone =  UtilitySharedPreferences.getPrefs(getActivity(),"ClientTelephone");
      StrMobile = UtilitySharedPreferences.getPrefs(getActivity(),"ClientMobile");
      MobileVerified = UtilitySharedPreferences.getPrefs(getActivity(),"MobileVerified");



      edtCustomerCode = (EditText)rootView.findViewById(R.id.edt_Customer_Id);
      edt_Proprietor_Name = (EditText)rootView.findViewById(R.id.edt_Proprietor_Name);
      edt_Agency_Name = (EditText)rootView.findViewById(R.id.edt_Agency_Name);
      edt_Mobile_Number = (EditText)rootView.findViewById(R.id.edt_Mobile_Number);
      edt_Telephone = (EditText)rootView.findViewById(R.id.Edt_Telephone);
      BtnVerifyMobile = (Button)rootView.findViewById(R.id.BtnVerifyMobile);

      if(MobileVerified!=null && !MobileVerified.equalsIgnoreCase("") && !MobileVerified.equalsIgnoreCase("null")){
          if(MobileVerified.equalsIgnoreCase("1")){
              edt_Mobile_Number.setText(StrMobile);
              BtnVerifyMobile.setVisibility(View.GONE);

          }
      }

      edtCustomerCode.setText(StrCustomerId);
      edt_Agency_Name.setText(StrAgency);
      edt_Proprietor_Name.setText(StrProprietorName);


      smsVerifyCatcher = new SmsVerifyCatcher(getActivity(), new OnSmsCatchListener<String>() {
          @Override
          public void onSmsCatch(String message) {
              mobileOtp = parseCode(message);//Parse verification code
              edt_Check_Mobile_Otp.setText(mobileOtp);
              //then you can send verification code to server
          }
      });

      BtnVerifyMobile.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View v) {
              SaveNVerify();
          }
      });

  }

    private void SaveNVerify() {
        if (IsValidFeilds()) {

            myDialog = new ProgressDialog(getActivity());
            myDialog.setCancelable(false);
            myDialog.setCanceledOnTouchOutside(false);
            myDialog.setMessage("Please Wait...");
            myDialog.show();

            InputMethodManager imm = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(edt_Telephone.getWindowToken(), 0);

            StrCustomerId = edtCustomerCode.getText().toString();
            StrProprietorName = edt_Proprietor_Name.getText().toString();
            StrMobile = edt_Mobile_Number.getText().toString();
            StrAgency = edt_Agency_Name.getText().toString().trim();
            StrTelephone = edt_Telephone.getText().toString().trim();


            String URL_save = ROOT_URL + "register.php";
            try {
                Log.d("URL_Save", URL_save);

                ConnectionDetector cd = new ConnectionDetector(getActivity());
                boolean isInternetPresent = cd.isConnectingToInternet();
                if (isInternetPresent) {

                    final StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_save,
                            new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {
                                    myDialog.dismiss();
                                    // Log.d("mainResponse", response);

                                    try {

                                        JSONObject jObj = new JSONObject(response);

                                        boolean status = jObj.getBoolean("status");
                                        String pin = jObj.getString("pin");

                                        if (status) {

                                            //CommonMethods.DisplayToast(getContext(),"Verify Email & Mobile");
                                            if (MobileVerified.equals("0")) {


                                                final Dialog dialog11 = new Dialog(getContext());
                                                dialog11.requestWindowFeature(Window.FEATURE_NO_TITLE);
                                                dialog11.setCanceledOnTouchOutside(false);
                                                dialog11.setContentView(R.layout.popup_verify_email_mobile_otp);
                                                dialog11.getWindow().getAttributes().width = ActionBar.LayoutParams.MATCH_PARENT;
                                                ImageView btnClose = (ImageView) dialog11.findViewById(R.id.btnClose);
                                                Button btnContinue = (Button) dialog11.findViewById(R.id.btnContinue);
                                                edt_Check_Mobile_Otp = (EditText) dialog11.findViewById(R.id.edt_Check_Mobile_Otp);
                                                edt_Check_Mobile_Otp.setText(pin);
                                                txt_resend_mobile_otp = (TextView) dialog11.findViewById(R.id.txt_resend_mobile_otp);
                                                txt_timer = (TextView) dialog11.findViewById(R.id.txt_timer);


                                                if (MobileVerified.equals("1")) {
                                                    edt_Check_Mobile_Otp.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.tick, 0);
                                                    txt_resend_mobile_otp.setVisibility(View.GONE);
                                                    mobileOtpVerified = true;
                                                    edt_Check_Mobile_Otp.setEnabled(false);

                                                    edt_Check_Mobile_Otp.setHint("Mobile Verified");
                                                    edt_Check_Mobile_Otp.setHintTextColor(getResources().getColor(R.color.primary_green));
                                                } else {
                                                    edt_Check_Mobile_Otp.setHint("Mobile OTP *");
                                                }


                                                dialog11.show();

                                                starCountdown();

                                                btnContinue.setOnClickListener(new View.OnClickListener() {
                                                    @Override
                                                    public void onClick(View view) {

                                                        if (mobileOtpVerified) {
                                                            dialog11.dismiss();
                                                            //onNextClicked(StepperLayout.OnNextClickedCallback nextClick.goToNextStep());

                                                        } else {
                                                            Toast.makeText(getActivity(), "Please Verify Mobile OTP.", Toast.LENGTH_SHORT).show();
                                                        }

                                                    }
                                                });

                                                btnClose.setOnClickListener(new View.OnClickListener() {
                                                    @Override
                                                    public void onClick(View view) {
                                                        dialog11.dismiss();
                                                    }
                                                });

                                                txt_resend_mobile_otp.setOnClickListener(new View.OnClickListener() {
                                                    @Override
                                                    public void onClick(View view) {

                                                        resendMobilePin();
                                                        starCountdown();

                                                    }
                                                });


                                                edt_Check_Mobile_Otp.addTextChangedListener(new TextWatcher() {
                                                    @Override
                                                    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                                                    }

                                                    @Override
                                                    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                                                        String input_text = charSequence.toString();
                                                        if (input_text.length() == 4) {

                                                            MobileOtp = edt_Check_Mobile_Otp.getText().toString().trim();

                                                            VerifyMobileOtp(MobileOtp);


                                                        }
                                                    }

                                                    @Override
                                                    public void afterTextChanged(Editable editable) {

                                                    }
                                                });


                                            }

                                        }


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
                            params.put("name", StrProprietorName);
                            params.put("agency_name", StrAgency);
                            params.put("mobileno", StrMobile);
                            params.put("telephone", StrTelephone);

                            Log.d("ParamSaveCustomer", params.toString());
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
                    if (myDialog != null && myDialog.isShowing()) {
                        myDialog.dismiss();
                    }
                    CommonMethods.DisplayToast(getActivity(), "Please check your internet connection");
                }
            } catch (Exception e) {

                e.printStackTrace();

            }
        }
    }

    @Override
    public void onNextClicked(final StepperLayout.OnNextClickedCallback onNextClickedCallback) {
        onNextClickedCallback.goToNextStep();

        if (IsValidFeilds()) {

            myDialog = new ProgressDialog(getActivity());
            myDialog.setCancelable(false);
            myDialog.setCanceledOnTouchOutside(false);
            myDialog.setMessage("Please Wait...");
            myDialog.show();

            InputMethodManager imm = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(edt_Telephone.getWindowToken(), 0);

            StrCustomerId = edtCustomerCode.getText().toString();
            StrProprietorName = edt_Proprietor_Name.getText().toString();
            StrMobile = edt_Mobile_Number.getText().toString();
            StrAgency = edt_Agency_Name.getText().toString().trim();
            StrTelephone = edt_Telephone.getText().toString().trim();



            String URL_save = ROOT_URL + "register.php";
            try {
                Log.d("URL_Save",URL_save);

                ConnectionDetector cd = new ConnectionDetector(getActivity());
                boolean isInternetPresent = cd.isConnectingToInternet();
                if (isInternetPresent) {

                    final StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_save,
                            new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {
                                    myDialog.dismiss();
                                   // Log.d("mainResponse", response);

                                    try {

                                        JSONObject jObj = new JSONObject(response);

                                        boolean status = jObj.getBoolean("status");
                                        String pin  = jObj.getString("pin");

                                        if (status) {

                                                //CommonMethods.DisplayToast(getContext(),"Verify Email & Mobile");
                                                if(MobileVerified.equals("1")){
                                                    onNextClickedCallback.goToNextStep();

                                                }else {

                                                    final Dialog dialog11 = new Dialog(getContext());
                                                    dialog11.requestWindowFeature(Window.FEATURE_NO_TITLE);
                                                    dialog11.setCanceledOnTouchOutside(false);
                                                    dialog11.setContentView(R.layout.popup_verify_email_mobile_otp);
                                                    dialog11.getWindow().getAttributes().width = ActionBar.LayoutParams.MATCH_PARENT;
                                                    ImageView btnClose = (ImageView) dialog11.findViewById(R.id.btnClose);
                                                    Button btnContinue = (Button) dialog11.findViewById(R.id.btnContinue);
                                                    edt_Check_Mobile_Otp = (EditText) dialog11.findViewById(R.id.edt_Check_Mobile_Otp);
                                                    edt_Check_Mobile_Otp.setText(pin);
                                                    txt_resend_mobile_otp = (TextView) dialog11.findViewById(R.id.txt_resend_mobile_otp);
                                                    txt_timer = (TextView) dialog11.findViewById(R.id.txt_timer);



                                                    if (MobileVerified.equals("1")) {
                                                        edt_Check_Mobile_Otp.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.tick, 0);
                                                        txt_resend_mobile_otp.setVisibility(View.GONE);
                                                        mobileOtpVerified = true;
                                                        edt_Check_Mobile_Otp.setEnabled(false);

                                                        edt_Check_Mobile_Otp.setHint("Mobile Verified");
                                                        edt_Check_Mobile_Otp.setHintTextColor(getResources().getColor(R.color.primary_green));
                                                    }else {
                                                        edt_Check_Mobile_Otp.setHint("Mobile OTP *");
                                                    }


                                                    dialog11.show();

                                                    starCountdown();

                                                    btnContinue.setOnClickListener(new View.OnClickListener() {
                                                        @Override
                                                        public void onClick(View view) {

                                                            if (mobileOtpVerified) {
                                                                dialog11.dismiss();
                                                                onNextClickedCallback.goToNextStep();
                                                            } else {
                                                                Toast.makeText(getActivity(), "Please Verify Mobile OTP.", Toast.LENGTH_SHORT).show();
                                                            }

                                                        }
                                                    });

                                                    btnClose.setOnClickListener(new View.OnClickListener() {
                                                        @Override
                                                        public void onClick(View view) {
                                                            dialog11.dismiss();
                                                        }
                                                    });

                                                    txt_resend_mobile_otp.setOnClickListener(new View.OnClickListener() {
                                                        @Override
                                                        public void onClick(View view) {

                                                            resendMobilePin();
                                                            starCountdown();

                                                        }
                                                    });




                                                    edt_Check_Mobile_Otp.addTextChangedListener(new TextWatcher() {
                                                        @Override
                                                        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                                                        }

                                                        @Override
                                                        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                                                            String input_text = charSequence.toString();
                                                            if (input_text.length() == 4) {

                                                                MobileOtp = edt_Check_Mobile_Otp.getText().toString().trim();

                                                                VerifyMobileOtp(MobileOtp);


                                                            }
                                                        }

                                                        @Override
                                                        public void afterTextChanged(Editable editable) {

                                                        }
                                                    });




                                                }

                                             }




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
                            params.put("name", StrProprietorName);
                            params.put("agency_name", StrAgency);
                            params.put("mobileno", StrMobile);
                            params.put("telephone", StrTelephone);

                            Log.d("ParamSaveCustomer", params.toString());
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
    }

    private void resendMobilePin() {

        String URL_Resend_Mobile_Pin = ROOT_URL+"/user/resendMobilePin";
        Log.d("UrlresendMobilePin",URL_Resend_Mobile_Pin);
        StringRequest strReq = new StringRequest(Request.Method.POST,URL_Resend_Mobile_Pin,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d(TAG, response);
                        myDialog.dismiss();

                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            boolean status = jsonObject.getBoolean("status");


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        VolleyLog.d(TAG, "Error: " + error.getMessage());

                    }
                }) {
            @Override
            public Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("Pan", StrPan);
                params.put("Mobile", StrMobile);
                Log.d("resendparams", params.toString());
                return params;
            }

        };
        int socketTimeout = 50000;//30 seconds - change to what you want
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        strReq.setRetryPolicy(policy);
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        requestQueue.add(strReq);

    }




    private void starCountdown() {

        CountDownTimer cT =  new CountDownTimer(100000, 1000) {

            public void onTick(long millisUntilFinished) {

                String v = String.format("%02d", millisUntilFinished/60000);
                int va = (int)( (millisUntilFinished%60000)/1000);
                txt_timer.setText("Please Wait-" +v+":"+String.format("%02d",va));
                txt_resend_mobile_otp.setEnabled(false);
            }

            public void onFinish() {

                //Toast.makeText(getActivity(), "Finish", Toast.LENGTH_SHORT).show();

                if (mobileOtpVerified) {
                    txt_resend_mobile_otp.setVisibility(View.GONE);
                }else {
                    txt_resend_mobile_otp.setVisibility(View.VISIBLE);
                }


            }
        };
        cT.start();
    }




    private void VerifyMobileOtp(final String MobileOtp) {

        String MobileVerifyUrl =  ROOT_URL+"verify_otp.php";
        Log.d("MobileVerfiyUrl",MobileVerifyUrl);
        StringRequest strReq = new StringRequest(Request.Method.POST, MobileVerifyUrl,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d(TAG, response);
                        //Toast.makeText(getActivity(), "", Toast.LENGTH_SHORT).show();

                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            boolean status = jsonObject.getBoolean("status");
                            if (status) {
                                Toast.makeText(getActivity(), "Mobile No. is Verified Successfully", Toast.LENGTH_SHORT).show();
                                edt_Check_Mobile_Otp.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.tick, 0);
                                edt_Check_Mobile_Otp.setEnabled(false);
                                mobileOtpVerified = true;
                                MobileVerified = "1";
                                UtilitySharedPreferences.setPrefs(getActivity(),"MobileVerified",MobileVerified);
                            }else {
                                Toast.makeText(getActivity(), "Invalid Otp", Toast.LENGTH_SHORT).show();
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        myDialog.dismiss();
                        VolleyLog.d(TAG, "Error: " + error.getMessage());

                    }
                }) {
            @Override
            public Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("customer_id", StrCustomerId);
                params.put("otp",MobileOtp);
                Log.d("ParamsVerifyMobile", params.toString());
                return params;
            }

        };
        int socketTimeout = 50000;//30 seconds - change to what you want
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        strReq.setRetryPolicy(policy);
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        requestQueue.add(strReq);
    }


    @Override
    public void onCompleteClicked(StepperLayout.OnCompleteClickedCallback onCompleteClickedCallback) {

    }

    @Override
    public void onBackClicked(StepperLayout.OnBackClickedCallback onBackClickedCallback) {
        onBackClickedCallback.goToPrevStep();
    }

    @Nullable
    @Override
    public VerificationError verifyStep() {

        if (!IsValidFeilds()) {
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




    @Override
    public void onStart() {
        super.onStart();
        smsVerifyCatcher.onStart();
    }

    @Override
    public void onStop() {
        super.onStop();
        smsVerifyCatcher.onStop();
    }

    private String parseCode(String message) {
        Pattern p = Pattern.compile("\\b\\d{4}\\b");
        Matcher m = p.matcher(message);
        String code = "";
        while (m.find()) {
            code = m.group(0);
        }
        return code;
    }


/*
    private void FetchDataByPAN() {

        String URL_Check_KYC_Complaint = "http://www.mymfnow.com/invest/checkPanKycStatus?pan="+StrPan+"&name="+ CommonMethods.UrlFormatString(StrName);
        //String URL_Check_KYC_Complaint = "http://www.mymfnow.com/invest/checkPanKycStatus?pan="+"AAAAA1234A"+"&name="+ CommonMethods.UrlFormatString("faiz");
        try {
            Log.d("URL",URL_Check_KYC_Complaint);

            ConnectionDetector cd = new ConnectionDetector(getContext());
            boolean isInternetPresent = cd.isConnectingToInternet();
            if (isInternetPresent) {

                StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_Check_KYC_Complaint,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                Log.d("Response",response);
                                try {

                                    JSONObject jobj = new JSONObject(response);

                                    StatusCodeKYCComplaint = jobj.getInt("status");
                                    MessageKycComplaint = jobj.getString("msg");

                                   // StatusCodeKYCComplaint = 200;


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
                RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
                requestQueue.add(stringRequest);



            } else {
                CommonMethods.DisplayToast(getActivity(), "Please check your internet connection");
            }
        } catch (Exception e) {

            e.printStackTrace();

        }


    }
*/

    private boolean IsValidFeilds() {
        boolean result = true;



        if (!MyValidator.isValidField(edtCustomerCode)) {
            edtCustomerCode.requestFocus();
            result = false;
        }

        if (!MyValidator.isValidName(edt_Proprietor_Name)) {
            edt_Proprietor_Name.requestFocus();
            result = false;
        }

        if (!MyValidator.isValidName(edt_Agency_Name)) {
            edt_Agency_Name.requestFocus();
            result = false;
        }

        if (!MyValidator.isValidMobile(edt_Mobile_Number)) {
            edt_Mobile_Number.requestFocus();
            result = false;
        }




        return  result;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        smsVerifyCatcher.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }


}
