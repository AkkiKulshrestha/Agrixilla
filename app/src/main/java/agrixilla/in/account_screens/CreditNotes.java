package agrixilla.in.account_screens;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import agrixilla.in.R;
import agrixilla.in.activities.NewDashboard;
import agrixilla.in.utils.UtilitySharedPreferences;

public class CreditNotes  extends AppCompatActivity{

    ImageView back_btn;

    ProgressDialog myDialog;
    String StrPan="",StrCustomerId="";
    LinearLayout ll_parent_credit_notes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_credit_notes_page);

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
        til_text.setText("CREDIT NOTES");


        StrPan = UtilitySharedPreferences.getPrefs(getApplicationContext(),"ClientPan");
        StrCustomerId = UtilitySharedPreferences.getPrefs(getApplicationContext(),"ClientCode");

        ll_parent_credit_notes = (LinearLayout)findViewById(R.id.ll_parent_credit_notes);




    }



    /*private void fetchBillDetailForUser() {


        String URL_user_info = ROOT_URL + "credit_note_list.php?CustomerId="+StrCustomerId;
        try {
            Log.d("URL_USerInfo",URL_user_info);

            ConnectionDetector cd = new ConnectionDetector(getApplicationContext());
            boolean isInternetPresent = cd.isConnectingToInternet();
            if (isInternetPresent) {

                final StringRequest stringRequest = new StringRequest(Request.Method.GET, URL_user_info,
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
                                        //StrCustomerId = jsonObject.getString("customer_id");


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

    }*/



    @Override
    public void onBackPressed() {
        Intent i = new Intent(getApplicationContext(),NewDashboard.class);
        i.putExtra("viewpager_position", 2);
        startActivity(i);
        overridePendingTransition(R.animator.left_right,R.animator.right_left);
        finish();
    }
}