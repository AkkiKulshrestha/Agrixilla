package agrixilla.in.ShopFregments;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import agrixilla.in.R;
import agrixilla.in.shop_screens.DetailProductOrderPage;
import agrixilla.in.utils.CommonMethods;
import agrixilla.in.utils.UtilitySharedPreferences;
import com.stepstone.stepper.Step;
import com.stepstone.stepper.VerificationError;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class PGRFragment  extends Fragment implements Step {

    RecyclerView Rv_PGR;
    LinearLayout ll_parent_product;
    View rootView;
    Context context;

    //int object_no_portfolio =1 ;
    public PGRFragment() {
        // Required empty public constructor
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView  = inflater.inflate(R.layout.fragment_pgr, container, false);
        //CommonMethods.DisplayToast(getContext(),"I,m on Create");


        return rootView;
    }



    private void getPGRProductList() {
        CommonMethods.DisplayToast(getContext(),"I,m in getProduct List");
        context = getContext();

        String response = UtilitySharedPreferences.getPrefs(context,"PGR_PRODUCT_LIST");

        ll_parent_product = (LinearLayout)rootView.findViewById(R.id.ll_parent_product);

        try {
        JSONArray product_list = new JSONArray(response);
        for(int i = 0;i< product_list.length();i++){
            JSONObject jsonObject = product_list.getJSONObject(i);

                    //String Product_id = String.valueOf(i);
                    String Products_name = jsonObject.getString("product");


                LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                final View rowView = inflater.inflate(R.layout.row_list_card, null);

                final TextView row_item_name = (TextView) rowView.findViewById(R.id.row_item_name);

                row_item_name.setText(Products_name.trim());

                rowView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent i = new Intent(getActivity(),DetailProductOrderPage.class);
                        i.putExtra("product_name", row_item_name.getText().toString().trim());
                        startActivity(i);
                        getActivity().overridePendingTransition(R.animator.move_left,R.animator.move_right);
                    }
                });

                //row_product_id.setText(Product_id);

                ll_parent_product.addView(rowView);


        }



    } catch (JSONException e) {

        e.printStackTrace();
    }

      /*  String URL_check_pan_status = RestClient.ROOT_URL + "getPgrProducts.php";
        try {
            Log.d("URL",URL_check_pan_status);

            ConnectionDetector cd = new ConnectionDetector(getActivity());
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

                                        //String Product_id = String.valueOf(i);
                                        String Products_name = jsonObject.getString("product");

                                        LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                                        View rowView = inflater.inflate(R.layout.row_product_list, null);

                                        TextView row_product_name = (TextView) rowView.findViewById(R.id.row_product_name);
                                        TextView row_product_id  = (TextView) rowView.findViewById(R.id.row_product_id);

                                        row_product_name.setText(Products_name.trim());
                                        //row_product_id.setText(Product_id);

                                        ll_parent_product.addView(rowView);


                                        //object_no_portfolio++;


                                       *//* ProductModel product_model = new ProductModel();
                                        product_model.setProduct_id(Product_id);
                                        product_model.setProduct_name(Products);
                                        //product_model.setProduct_image(ProductImageContent);

                                        ProductModelArrayList.add(product_model);

*//*




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
                RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
                requestQueue.add(stringRequest);


            } else {

                CommonMethods.DisplayToast(getActivity(), "Please check your internet connection");
            }
        } catch (Exception e) {

            e.printStackTrace();

        }
*/

    }

   /* @Override
    public void onNextClicked(StepperLayout.OnNextClickedCallback callback) {
            callback.goToNextStep();
    }

    @Override
    public void onCompleteClicked(StepperLayout.OnCompleteClickedCallback callback) {

    }

    @Override
    public void onBackClicked(StepperLayout.OnBackClickedCallback callback) {

    }*/

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
