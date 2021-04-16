package agrixilla.in.more_screens;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.OpenableColumns;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import agrixilla.in.R;
import agrixilla.in.activities.NewDashboard;
import agrixilla.in.adapters.SalesReturnsListAdapter;
import agrixilla.in.models.SalesUploadModel;
import agrixilla.in.utils.CommonMethods;
import agrixilla.in.utils.FileUtils;
import agrixilla.in.utils.UtilitySharedPreferences;

import java.io.File;
import java.util.ArrayList;
import java.util.UUID;

public class UploadSalesReturnsActivity extends AppCompatActivity{

    ImageView back_btn;
    private final int STORAGE_PERMISSION_CODE = 23;

    ProgressDialog myDialog;
    String StrPan="",StrCustomerId="";
    TextView Tv_SelectedFileName,tv_no_data;
    RecyclerView rv_uploaded_list;
    Button button_upload,button_choose;
    private static final int FILE_SELECT_CODE = 0;
    ArrayList<SalesUploadModel> SaleReturnsList = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_sales_returns_page);

        init();
    }

    private void init() {
        back_btn = (ImageView) findViewById(R.id.back_btn);
        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(),NewDashboard.class);
                i.putExtra("viewpager_position", 3);
                startActivity(i);
                overridePendingTransition(R.animator.left_right,R.animator.right_left);
                finish();
            }
        });

        StrPan = UtilitySharedPreferences.getPrefs(getApplicationContext(),"ClientPan");
        StrCustomerId = UtilitySharedPreferences.getPrefs(getApplicationContext(),"ClientCode");


        Tv_SelectedFileName = (TextView) findViewById(R.id.Tv_SelectedFileName);

        tv_no_data = (TextView) findViewById(R.id.tv_no_data);
        rv_uploaded_list = (RecyclerView)findViewById(R.id.rv_uploaded_list);

        button_upload = (Button)findViewById(R.id.button_upload);
        button_choose = (Button)findViewById(R.id.button_choose);

        button_choose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(Build.VERSION.SDK_INT >= 23)
                {
                    requestReadPermission();

                }else{
                    fileBrowse();
                }
            }
        });

        getDocumentUploadedList();

    }

    private void getDocumentUploadedList() {

      /*  myDialog = new ProgressDialog(this);
        myDialog.setMessage("Loading Uploaded Files. Please wait ...");
        myDialog.setCancelable(false);
        myDialog.setCanceledOnTouchOutside(false);
        myDialog.show();*/
        String uniqueId = UUID.randomUUID().toString();


        SalesReturnsListAdapter uploadAdapter = new SalesReturnsListAdapter(SaleReturnsList);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        rv_uploaded_list.setLayoutManager(mLayoutManager);


        String File_link = "https://www.playaccounting.com/explanation/gj-exp/returns-outwards/attachment/journal-entry-for-return-of-merchandise-img1/";
        String file_name = "journal-entry-for-return-of-merchandise-img1";
        String file_extension = ".jpg";
        String date = "2018-06-25";


        SaleReturnsList.add(new SalesUploadModel(date,file_name,file_extension,File_link));
        rv_uploaded_list.setAdapter(uploadAdapter);
        rv_uploaded_list.setVisibility(View.VISIBLE);



       /* final String KycList_url =  RestClient.ROOT_URL + "/getUploadedSalesReturns.php?_"+uniqueId+"&CustomerId="+StrCustomerId;
        Log.d("PaymentStatus_url-->", "" + KycList_url);
        ConnectionDetector cd = new ConnectionDetector(this);
        boolean isInternetPresent = cd.isConnectingToInternet();

        if (isInternetPresent) {

            final StringRequest stringRequest = new StringRequest(Request.Method.GET, KycList_url ,
                    new com.android.volley.Response.Listener<String>() {


                        @Override
                        public void onResponse(String response) {

                            Log.d("kyc List Response-->", response);
                            try {
                                JSONArray kyc_list  = new JSONArray(response);
                                int length = kyc_list.length();
                                if(length>=1)
                                {
                                    rv_uploaded_list.setVisibility(View.VISIBLE);
                                    tv_no_data.setVisibility(View.GONE);

                                }else {
                                    tv_no_data.setVisibility(View.VISIBLE);
                                    rv_uploaded_list.setVisibility(View.GONE);
                                    myDialog.dismiss();
                                }

                                for(int k = 0;k<kyc_list.length();k++){

                                    JSONObject jsonObject = kyc_list.getJSONObject(k);


                                    SalesReturnsListAdapter uploadAdapter = new SalesReturnsListAdapter(SaleReturnsList);
                                    RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
                                    rv_uploaded_list.setLayoutManager(mLayoutManager);


                                    String File_link = jsonObject.getString("file_link");
                                    String file_name = jsonObject.getString("file_name");
                                    String file_extension = jsonObject.getString("file_extension");
                                    String date = jsonObject.getString("date");


                                    SaleReturnsList.add(new SalesUploadModel(date,file_name,file_extension,File_link));
                                    rv_uploaded_list.setAdapter(uploadAdapter);


                                }
                                myDialog.dismiss();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                        }
                    },
                    new com.android.volley.Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            error.printStackTrace();
                            CommonMethods.DisplayToast(getApplicationContext(), "Problem with connection. Please try again later.");


                        }
                    });

            int socketTimeout = 50000;//30 seconds - change to what you want
            RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
            stringRequest.setRetryPolicy(policy);
            RequestQueue requestQueue = Volley.newRequestQueue(this);
            requestQueue.add(stringRequest);
        }else{
            CommonMethods.DisplayToast(getApplicationContext(),"No Internet Connection");
        }*/
    }


    private void requestReadPermission() {
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED){
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_EXTERNAL_STORAGE)) {
                //If the user has denied the permission previously your code will come to this block
                //Here you can explain why you need this permission
                //Explain here why you need this permission
                CommonMethods.DisplayToast(this, "Wetland App Needs your Read External Storage to read your Uploads if any.");
            }

            //And finally ask for the permission
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, STORAGE_PERMISSION_CODE);
        }else{
            fileBrowse();
        }


    }

    private void fileBrowse() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("*/*");
        intent.addCategory(Intent.CATEGORY_OPENABLE);

        try {
            startActivityForResult(
                    Intent.createChooser(intent, "Select a File to Upload"),
                    FILE_SELECT_CODE);
        } catch (android.content.ActivityNotFoundException ex) {
            // Potentially direct the user to the Market with a Dialog
            Toast.makeText(this, "Please install a File Manager.",
                    Toast.LENGTH_SHORT).show();
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case FILE_SELECT_CODE:
                if (resultCode == RESULT_OK) {
                    // Get the Uri of the selected file
                    Uri uri1 = data.getData();
                    Log.d("FileUploadOnResult", "File Uri: " + uri1.toString());
                    // Get the path
                    String path = FileUtils.getPath(this, uri1);
                    Log.d("FileUploadOnResult", "File Path: " + path);

                    Uri uri = data.getData();
                    String uriString = uri.toString();
                    File myFile = new File(uriString);
                    String path1 = myFile.getAbsolutePath();
                    String displayName = null;


                    if (uriString.startsWith("content://")) {
                        Cursor cursor = null;
                        try {
                            cursor = getApplicationContext().getContentResolver().query(uri, null, null, null, null);
                            if (cursor != null && cursor.moveToFirst()) {
                                displayName = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME));
                                Tv_SelectedFileName.setText(displayName);
                            }
                        } finally {
                            cursor.close();
                        }
                    } else if (uriString.startsWith("file://")) {
                        displayName = myFile.getName();
                        Tv_SelectedFileName.setText(displayName);

                    }
                    // Get the file instance
                    // File file = new File(path);
                    // Initiate the upload
                }
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
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
        i.putExtra("viewpager_position", 3);
        startActivity(i);
        overridePendingTransition(R.animator.left_right,R.animator.right_left);
        finish();
    }
}