package agrixilla.in.shop_screens;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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
import agrixilla.in.utils.NumberTextWatcherForThousand;
import agrixilla.in.webservices.RestClient;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class DetailProductOrderPage extends AppCompatActivity{

    ImageView back_btn;
    TextView product_name;
    ImageView product_image;
    String StrProductNameSelected;
    LinearLayout ll_parent_packaged_product;
    TextView tv_total_amount,tv_discount_rate,tv_discount_amount,tv_amt_after_discount,tv_cgst_per,tv_cgst_amount,tv_sgst_per,tv_sgst_amount,tv_nettotal_amount;
    String basic_price,gst_percent,discount_percent;
    Button btn_checkout;
    View rowView;
    int object_row_no =1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_order_page);

        init();
    }

    private void init() {

        if (getIntent() != null) {

            Intent i = getIntent();
            StrProductNameSelected = i.getStringExtra("product_name");

        }

        back_btn = (ImageView)findViewById(R.id.back_btn);
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

             product_name = (TextView)findViewById(R.id.product_name);
             product_image = (ImageView)findViewById(R.id.product_image);

            product_name.setText(StrProductNameSelected);

        ll_parent_packaged_product = (LinearLayout)findViewById(R.id.ll_parent_packaged_product);
        tv_total_amount= (TextView)findViewById(R.id.tv_total_amount);
        tv_discount_rate= (TextView)findViewById(R.id.tv_discount_rate);
        tv_discount_amount= (TextView)findViewById(R.id.tv_discount_amount);
        tv_amt_after_discount = (TextView)findViewById(R.id.tv_amt_after_discount);
        tv_cgst_per= (TextView)findViewById(R.id.tv_cgst_per);
        tv_cgst_amount= (TextView)findViewById(R.id.tv_cgst_amount);
        tv_sgst_per= (TextView)findViewById(R.id.tv_sgst_per);
        tv_sgst_amount= (TextView)findViewById(R.id.tv_sgst_amount);
        tv_nettotal_amount= (TextView)findViewById(R.id.tv_nettotal_amount);

        btn_checkout = (Button)findViewById(R.id.btn_checkout);

        getPackagedProductDetails();



    }

    @Override
    public void onBackPressed() {
        Intent i = new Intent(getApplicationContext(),NewDashboard.class);
        i.putExtra("viewpager_position", 1);
        startActivity(i);
        overridePendingTransition(R.animator.left_right,R.animator.right_left);
        finish();


    }

    private void getPackagedProductDetails() {

        object_row_no  =1;
        String URL_check_pan_status = RestClient.ROOT_URL + "getPackagedProduct.php?product="+CommonMethods.UrlFormatString(StrProductNameSelected);
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
                                    for(int i = 0;i< product_list.length();i++){
                                        JSONObject jsonObject = product_list.getJSONObject(i);
                                        String package_product = jsonObject.getString("package_product");
                                        basic_price = jsonObject.getString("basic_price");
                                        String mrp = jsonObject.getString("mrp");
                                        gst_percent = jsonObject.getString("gst_percent");
                                        final String quantity = jsonObject.getString("quantity");
                                        String nos = jsonObject.getString("nos");
                                        discount_percent = jsonObject.getString("discount");


                                        /*productlist =new ArrayList<ProductModel>();
                                        productlist.add(new ProductModel(Product_id, Products_name.trim(),ProductCategory));

                                        productsAdapter=new ProductsAdapter(getActivity(), productlist);
                                        listview_products.setAdapter(productsAdapter);


                                        listview_products.setTextFilterEnabled(true);
                                        setupSearchView();*/


                                        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                                        rowView = inflater.inflate(R.layout.row_package_product_card, null);

                                        final TextView row_package_item_name = (TextView) rowView.findViewById(R.id.row_package_item_name);
                                        final EditText row_package_no_of_cases = (EditText) rowView.findViewById(R.id.row_package_no_of_cases);

                                        final TextView row_package_item_rate = (TextView) rowView.findViewById(R.id.row_package_item_rate);
                                        final TextView row_package_item_amount = (TextView) rowView.findViewById(R.id.row_package_item_amount);
                                        final  TextView row_package_item_quantity = (TextView) rowView.findViewById(R.id.row_package_item_quantity);
                                        final TextView row_package_no_of_nos = (TextView)rowView.findViewById(R.id.row_package_no_of_nos);


                                        row_package_item_quantity.setText(quantity);

                                        row_package_item_name.setText(package_product.trim() + "\n ("+quantity+" L - "+nos+" Nos.) ");
                                        row_package_no_of_cases.setText("0");
                                        row_package_item_rate.setText(CommonMethods.DecimalNumberDisplayFormattingWithComma(basic_price));
                                        row_package_item_amount.setText("0");
                                        row_package_no_of_nos.setText(nos);
                                        row_package_no_of_cases.addTextChangedListener(new TextWatcher() {
                                            @Override
                                            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                                            }

                                            @Override
                                            public void onTextChanged(CharSequence s, int start, int before, int count) {

                                            }

                                            @Override
                                            public void afterTextChanged(Editable s) {
                                                if(s.toString()!=null && !s.toString().equalsIgnoreCase("")){
                                                    double amount = Double.valueOf(s.toString())* Double.valueOf(NumberTextWatcherForThousand.trimCommaOfString(row_package_no_of_nos.getText().toString())) * Double.valueOf(NumberTextWatcherForThousand.trimCommaOfString(row_package_item_rate.getText().toString()));
                                                    Log.d("mul",s.toString()+"*"+Double.valueOf(NumberTextWatcherForThousand.trimCommaOfString(row_package_no_of_nos.getText().toString()))+"*"+Double.valueOf(NumberTextWatcherForThousand.trimCommaOfString(row_package_item_rate.getText().toString())));
                                                    row_package_item_amount.setText(CommonMethods.DecimalNumberDisplayFormattingWithComma(String.format("%.1f", amount)));
                                                    calculateTotal();
                                                }
                                            }
                                        });


                                       /* rowView.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                Intent i = new Intent(getActivity(),DetailProductOrderPage.class);
                                                i.putExtra("product_name", row_item_name.getText().toString().trim());
                                                startActivity(i);
                                                overridePendingTransition(R.animator.move_left,R.animator.move_right);
                                            }
                                        });
*/
                                        //row_product_id.setText(Product_id);
                                        rowView.setId(object_row_no);
                                        row_package_item_amount.setId(100+object_row_no);
                                        ll_parent_packaged_product.addView(rowView);

                                        object_row_no++;




                                    }

                                    calculateTotal();



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

    private void calculateTotal() {
        Log.d("TOtal View",""+object_row_no);
        double total_amount = 0;


        for(int k = 1; k<object_row_no;k++) {
            View view = rowView.getRootView();

            EditText edt_no_of_case = (EditText) view.findViewById(R.id.row_package_no_of_cases);
            TextView tv_amount = (TextView) view.findViewById(R.id.row_package_item_amount);
            TextView tv_quantity = (TextView) view.findViewById(R.id.row_package_item_quantity);
            TextView tv_basic_rate = (TextView) view.findViewById(R.id.row_package_item_rate);


            if (edt_no_of_case.getText().toString() != null && edt_no_of_case.getText().toString().equalsIgnoreCase("")) {
                double amount = 0;
                tv_amount.setText("0");
                edt_no_of_case.setText("0");

            } /*else if (edt_no_of_case.getText().toString() != null && !edt_no_of_case.getText().toString().equalsIgnoreCase("") && !edt_no_of_case.getText().toString().equalsIgnoreCase("0")) {
                {
                    double amount = Double.valueOf(edt_no_of_case.getText().toString()) * Double.valueOf(NumberTextWatcherForThousand.trimCommaOfString(tv_quantity.getText().toString())) * Double.valueOf(NumberTextWatcherForThousand.trimCommaOfString(tv_basic_rate.getText().toString()));
                    Log.d("mul", edt_no_of_case.getText().toString() + "*" + Double.valueOf(NumberTextWatcherForThousand.trimCommaOfString(tv_quantity.getText().toString())) + "*" + Double.valueOf(NumberTextWatcherForThousand.trimCommaOfString(tv_basic_rate.getText().toString())));
                    tv_amount.setText(CommonMethods.DecimalNumberDisplayFormattingWithComma(String.format("%.1f", amount)));

                }
            }*/
        }

        for(int m = 1 ; m<object_row_no;m++){
            //View v = ll_parent_packaged_product.getChildAt(m);

            View view1 = rowView.getRootView();
            TextView tv_amount = (TextView) view1.findViewById(100+m);
            double db_amount_values = Double.valueOf(NumberTextWatcherForThousand.trimCommaOfString(tv_amount.getText().toString()));

            Log.d("EachAmount",tv_amount.getText().toString()+" ---- "+db_amount_values);
            total_amount = total_amount + db_amount_values;
        }

        Log.d("Total Amount",""+total_amount);
       // double basic_rate_amount = Double.valueOf(basic_price);
        double gst_per = Double.valueOf(gst_percent);
        double discount_per = Double.valueOf(discount_percent);
        double cgst_per = gst_per/2;
        double sgst_per = gst_per/2;

        double discount_amount = ((total_amount*discount_per)/100);

        double total_amount_after_dis = total_amount - discount_amount;

        double cgst_amount = ((cgst_per*total_amount_after_dis)/100);
        double sgst_amount = ((sgst_per*total_amount_after_dis)/100);

        double net_total = total_amount_after_dis + sgst_amount + cgst_amount;

        tv_total_amount.setText(CommonMethods.NumberDisplayFormattingWithComma(String.format("%.0f", total_amount)));
        tv_discount_rate.setText("Discount@ 10%");
        tv_cgst_per.setText("CGST@ "+String.format("%.1f", cgst_per) + " %");
        tv_sgst_per.setText("SGST@ "+String.format("%.1f", sgst_per) + " %");

        tv_discount_amount.setText(CommonMethods.NumberDisplayFormattingWithComma(String.format("%.0f", discount_amount)));
        tv_amt_after_discount.setText(CommonMethods.NumberDisplayFormattingWithComma(String.format("%.0f", total_amount_after_dis)));

        tv_cgst_amount.setText(CommonMethods.NumberDisplayFormattingWithComma(String.format("%.0f", cgst_amount)));
        tv_sgst_amount.setText(CommonMethods.NumberDisplayFormattingWithComma(String.format("%.0f", sgst_amount)));
        tv_nettotal_amount.setText(CommonMethods.NumberDisplayFormattingWithComma(String.format("%.0f", net_total)));


    }
}
