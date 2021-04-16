package agrixilla.in.shop_screens;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

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
import agrixilla.in.webservices.RestClient;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class ShopListPage extends AppCompatActivity {

    String StrProductTypeSelected;
    ImageView back_btn;
    LinearLayout ll_parent_product;
    TextView shop_product_label;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shop_type_page);

        init();
    }

    private void init() {

        if (getIntent() != null) {

            Intent i = getIntent();
            StrProductTypeSelected = i.getStringExtra("product");

        }

        back_btn = (ImageView) findViewById(R.id.back_btn);
        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(),NewDashboard.class);
                i.putExtra("viewpager_position", 1);
                startActivity(i);
                overridePendingTransition(R.animator.left_right,R.animator.right_left);
                finish();
            }
        });

        TextView til_text = (TextView)findViewById(R.id.til_text);
        til_text.setText(StrProductTypeSelected);



        shop_product_label = (TextView)findViewById(R.id.shop_product_label);
        shop_product_label.setText(StrProductTypeSelected);

        ll_parent_product = (LinearLayout)findViewById(R.id.ll_parent_product);

        if(StrProductTypeSelected!=null && !StrProductTypeSelected.equalsIgnoreCase("")){
            if(StrProductTypeSelected.equalsIgnoreCase("PGR")){
                getPGRProductList();
            }else {
                TextView textView = new TextView(this);
                textView.setText("No List to Display.");
                ll_parent_product.addView(textView);
            }
        }



    }

    private void getPGRProductList() {

        //ll_parent_product = (LinearLayout)rootView.findViewById(R.id.ll_parent_product);

        String URL_check_pan_status = RestClient.ROOT_URL + "getPgrProducts.php";
        try {
            Log.d("URL",URL_check_pan_status);

            ConnectionDetector cd = new ConnectionDetector(getApplicationContext());
            boolean isInternetPresent = cd.isConnectingToInternet();
            if (isInternetPresent) {

                StringRequest stringRequest = new StringRequest(Request.Method.GET,
                        URL_check_pan_status,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                Log.d("dataResponse",response);
                                try {

                                    //ProductModelArrayList = new ArrayList<ProductModel>();

                                    JSONArray product_list = new JSONArray(response);
                                    //UtilitySharedPreferences.setPrefs(getActivity(),"PGR_PRODUCT_LIST",product_list.toString());

                                    for(int i = 0;i< product_list.length();i++){
                                        JSONObject jsonObject = product_list.getJSONObject(i);
                                        String Product_id = String.valueOf(i);
                                        String Products_name = jsonObject.getString("product");
                                        String ProductCategory = jsonObject.getString("category");

                                        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                                        final View rowView = inflater.inflate(R.layout.row_list_card, null);

                                        final TextView row_item_name = (TextView) rowView.findViewById(R.id.row_item_name);

                                        row_item_name.setText(Products_name.trim());

                                        rowView.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                Intent i = new Intent(getApplicationContext(),DetailProductOrderPage.class);
                                                i.putExtra("product_name", row_item_name.getText().toString().trim());
                                                startActivity(i);
                                                overridePendingTransition(R.animator.move_left,R.animator.move_right);
                                            }
                                        });


                                        ll_parent_product.addView(rowView);



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
                }) ;
                int socketTimeout = 50000;//30 seconds - change to what you want
                RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, -1, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
                stringRequest.setRetryPolicy(policy);
                // RequestQueue requestQueue = Volley.newRequestQueue(this, new HurlStack(null, getSocketFactory()));
                RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
                requestQueue.add(stringRequest);


            } else {

                CommonMethods.DisplayToast(getApplicationContext(), "Please check your internet connection");
            }
        } catch (Exception e) {

            e.printStackTrace();

        }


    }

    @Override
    public void onBackPressed() {
        Intent i = new Intent(getApplicationContext(),NewDashboard.class);
        i.putExtra("viewpager_position", 1);
        startActivity(i);
        overridePendingTransition(R.animator.left_right,R.animator.right_left);
        finish();
    }
}
