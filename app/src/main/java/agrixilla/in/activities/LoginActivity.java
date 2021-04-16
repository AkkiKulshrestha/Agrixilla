package agrixilla.in.activities;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.provider.Settings;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
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
import agrixilla.in.investorRegistration.InvestorRegistration;
import agrixilla.in.utils.CommonMethods;
import agrixilla.in.utils.ConnectionDetector;
import agrixilla.in.utils.MyValidator;
import agrixilla.in.utils.UtilitySharedPreferences;


import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import static agrixilla.in.webservices.RestClient.ROOT_URL;


public class LoginActivity extends AppCompatActivity implements View.OnClickListener{

    String StrDeviceUniqueId;
    String client_id,client_name,client_pan,client_mobile,client_email,active,bse_active,kyc;
    String email_verified,mobile_verified;

    EditText EdtUserId,EdtPassword;
    Button  btn_sign_in;
    TextView Tv_Register;
    ProgressDialog myDialog;
    int PanStatusCode;

    private String StrIMEI1 = "";
    private String StrIMEI2 = "";
    private String StrIMEI = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        init();
    }

    @SuppressLint("MissingPermission")

    private void init() {

        StrDeviceUniqueId = getDeviceID();

        EdtUserId = (EditText)findViewById(R.id.EdtUserId);
        EdtPassword = (EditText)findViewById(R.id.edt_Password);

        btn_sign_in = (Button) findViewById(R.id.btn_sign_in);
        btn_sign_in.setOnClickListener(this);

        Tv_Register = (TextView)findViewById(R.id.Tv_Register);
        Tv_Register.setOnClickListener(this);
    }

    @SuppressLint("HardwareIds")
    public String getDeviceID() {
        String m_szUniqueID = "";
        TelephonyManager telMgr = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
        int simState = telMgr != null ? telMgr.getSimState() : 0;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            int simCount = telMgr != null ? telMgr.getPhoneCount() : 0;
            if (simCount == 2) {
                if (checkSelfPermission(Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    Activity#requestPermissions
                    return null;
                }

                TelephonyManager telephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
                if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.READ_PHONE_STATE) == PackageManager.PERMISSION_GRANTED) {

                    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.Q) {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                            StrIMEI1 = telephonyManager != null ? telephonyManager.getImei(0) : "";
                            StrIMEI2 = telephonyManager != null ? telephonyManager.getImei(1) : "";
                        }
                    } else {
                        StrIMEI = Settings.Secure.getString(getContentResolver(),
                                Settings.Secure.ANDROID_ID);
                    }
                }
            } else {
                StrIMEI = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);
            }
            // 2 compute DEVICE ID
            if (StrIMEI != null && !StrIMEI.equalsIgnoreCase("") && !StrIMEI.equalsIgnoreCase("null")) {
                m_szUniqueID = StrIMEI;
            } else if (StrIMEI1 != null && !StrIMEI1.equalsIgnoreCase("") && !StrIMEI1.equalsIgnoreCase("null")) {
                m_szUniqueID = StrIMEI1;
            } else if (StrIMEI2 != null && !StrIMEI2.equalsIgnoreCase("") && !StrIMEI2.equalsIgnoreCase("null")) {
                m_szUniqueID = StrIMEI2;
            } else {
                final String m_szDevIDShort = "35"
                        + // we make this look like a valid IMEI
                        Build.BOARD.length() % 10 + Build.BRAND.length() % 10
                        + Build.CPU_ABI.length() % 10 + Build.DEVICE.length() % 10
                        + Build.DISPLAY.length() % 10 + Build.HOST.length() % 10
                        + Build.ID.length() % 10 + Build.MANUFACTURER.length() % 10
                        + Build.MODEL.length() % 10 + Build.PRODUCT.length() % 10
                        + Build.TAGS.length() % 10 + Build.TYPE.length() % 10
                        + Build.USER.length() % 10; // 13 digits

                final WifiManager wm = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);
                final String m_szWLANMAC = wm != null ? wm.getConnectionInfo().getMacAddress() : "";
                // 5 Bluetooth MAC address android.permission.BLUETOOTH required
                BluetoothAdapter m_BluetoothAdapter = null; // Local Bluetooth adapter
                m_BluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
                final String m_szBTMAC = m_BluetoothAdapter.getAddress();
                System.out.println("m_szBTMAC " + m_szBTMAC);

                if (m_szWLANMAC != null && !m_szWLANMAC.equalsIgnoreCase("") && !m_szWLANMAC.equalsIgnoreCase("null")) {
                    m_szUniqueID = m_szWLANMAC;
                } else if (m_szBTMAC != null && !m_szBTMAC.equalsIgnoreCase("") && !m_szBTMAC.equalsIgnoreCase("null")) {
                    m_szUniqueID = m_szBTMAC;
                }

            }
        } else {
            if (telMgr != null) {
                StrIMEI = telMgr.getDeviceId();
                m_szUniqueID = StrIMEI;
            }

        }
        Log.i("--DeviceID--", m_szUniqueID);
        Log.d("DeviceIdCheck", "DeviceId that generated MPreferenceActivity:" + m_szUniqueID);
        return m_szUniqueID;
    }


    @Override
    public void onClick(View view) {
       int id = view.getId();

       if (id == R.id.btn_sign_in) {
           Intent i = new Intent(getApplicationContext(),NewDashboard.class);
           i.putExtra("viewpager_position", 0);
           startActivity(i);
           overridePendingTransition(R.animator.move_left,R.animator.move_right);
           finish();

          /* if (isValidFeild()) {


               fetchUserDetails();
           }*/
       }else if(id == R.id.Tv_Register){
           startActivity(new Intent(getApplicationContext(),InvestorRegistration.class));
           overridePendingTransition(R.animator.move_left,R.animator.move_right);
           finish();
       }



    }

    private void fetchUserDetails() {

        myDialog = new ProgressDialog(LoginActivity.this);
        myDialog.setMessage("Please wait...");
        myDialog.setCancelable(false);
        myDialog.setCanceledOnTouchOutside(false);
        myDialog.show();

        final String StrUserId = EdtUserId.getText().toString();
        final String StrPassword = EdtPassword.getText().toString();

        String URL_login = ROOT_URL+"/login.php";
        try {
            Log.d("URL_Login",URL_login);

            ConnectionDetector cd = new ConnectionDetector(this);
            boolean isInternetPresent = cd.isConnectingToInternet();
            if (isInternetPresent) {

                final StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_login,
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
                                    String data = jobj.getString("data");
                                    if (status) {
                                        JSONObject jsonObject = new JSONObject(data);
                                        client_id = jsonObject.getString("customer_id");
                                        String client_firmname = jsonObject.getString("firm_name");
                                        String client_propritor_name = jsonObject.getString("propritor_name");
                                        client_pan = jsonObject.getString("pan");
                                        client_mobile = jsonObject.getString("mobile");
                                        client_email = jsonObject.getString("email");
                                        //active = jsonObject.getString("active");
                                        String address1 = jsonObject.getString("address1");
                                        String address2 = jsonObject.getString("address2");
                                        String address3 = jsonObject.getString("address3");
                                        String pincode = jsonObject.getString("pincode");

                                        String pesticide_license_no = jsonObject.getString("pesticide_license_no");
                                        String gst_in_no = jsonObject.getString("gst_in_no");
                                        String telephone_no = jsonObject.getString("telephone_no");

                                        UtilitySharedPreferences.setPrefs(getApplicationContext(),"ClientCode",client_id);
                                        UtilitySharedPreferences.setPrefs(getApplicationContext(),"ClientFirmName",client_firmname);
                                        UtilitySharedPreferences.setPrefs(getApplicationContext(),"ClientProprietorName",client_propritor_name);
                                        UtilitySharedPreferences.setPrefs(getApplicationContext(),"ClientPan",client_pan);
                                        UtilitySharedPreferences.setPrefs(getApplicationContext(),"ClientMobile",client_mobile);
                                        UtilitySharedPreferences.setPrefs(getApplicationContext(),"ClientEmail",client_email);
                                        UtilitySharedPreferences.setPrefs(getApplicationContext(),"ClientAddress1",address1);
                                        UtilitySharedPreferences.setPrefs(getApplicationContext(),"ClientAddress2",address2);
                                        UtilitySharedPreferences.setPrefs(getApplicationContext(),"ClientAddress2",address3);
                                        UtilitySharedPreferences.setPrefs(getApplicationContext(),"ClientPincode",pincode);
                                        UtilitySharedPreferences.setPrefs(getApplicationContext(),"ClientPesticideLicenseNo",pesticide_license_no);
                                        UtilitySharedPreferences.setPrefs(getApplicationContext(),"ClientGstInNo",gst_in_no);
                                        UtilitySharedPreferences.setPrefs(getApplicationContext(),"ClientTelephone",telephone_no);

                                        if(myDialog!=null && myDialog.isShowing()){
                                            myDialog.dismiss();
                                        }

                                            Intent i = new Intent(getApplicationContext(),NewDashboard.class);
                                            i.putExtra("viewpager_position", 0);
                                            startActivity(i);
                                            overridePendingTransition(R.animator.move_left,R.animator.move_right);
                                            finish();


                                }else {
                                        CommonMethods.DisplayToast(getApplicationContext(),"Please check the credentials and try again.");
                                    }

                            }catch (Exception e ){
                                    e.printStackTrace();
                                    if(myDialog!=null && myDialog.isShowing()){
                                        myDialog.dismiss();
                                    }
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
                        params.put("MobileNo", StrUserId);
                        params.put("Password", StrPassword);


                        Log.d("ParrasLogin", params.toString());
                        return params;

                    }
                };

                int socketTimeout = 50000; //30 seconds - change to what you want
                RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
                stringRequest.setRetryPolicy(policy);
                // RequestQueue requestQueue = Volley.newRequestQueue(this, new HurlStack(null, getSocketFactory()));
                RequestQueue requestQueue = Volley.newRequestQueue(this);
                requestQueue.add(stringRequest);

            } else {
                if(myDialog!=null && myDialog.isShowing()) {
                    myDialog.dismiss();
                }
                CommonMethods.DisplayToast(this, "Please check your internet connection");
            }
        } catch (Exception e) {

            e.printStackTrace();

        }


    }


    private boolean isValidFeild() {
        boolean result = true;

        if (!MyValidator.isValidMobile(EdtUserId)) {
            EdtUserId.requestFocus();
            result = false;
        }

        if (!MyValidator.isValidField(EdtPassword)) {
            EdtPassword.requestFocus();
            result = false;
        }
        return  result;
    }

}
