package agrixilla.in.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import agrixilla.in.R;
import agrixilla.in.ShopFregments.BioPesticideFragment;
import agrixilla.in.ShopFregments.FungicideFragment;
import agrixilla.in.ShopFregments.HerbicideFragment;
import agrixilla.in.ShopFregments.InsecticidesFragment;
import agrixilla.in.ShopFregments.PGRFragment;
import agrixilla.in.shop_screens.ShopListPage;
import com.stepstone.stepper.StepperLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

public class ShopFragment extends  Fragment  {

    //LinearLayout ll_parent_product;
    View rootView;
    Context mContext;

    LinearLayout ll_parent_product;


    // private static ArrayList<ProductModel> ProductModelArrayList;
    private final String[] SHOP_PAGE_TITLES = new String[] {
            "PGR",
            "Insecticide",
            "Fungicide",
            "Herbicide",
            "Bio-Pesticide"
    };

    // The fragments that are used as the individual pages
    private final Fragment[] SHOP_PAGES = new Fragment[] {
            new PGRFragment(),
            new InsecticidesFragment(),
            new FungicideFragment(),
            new HerbicideFragment(),
            new BioPesticideFragment()

    };

    Fragment mFragement;
    StepperLayout stepperLayout_Products;

    // The ViewPager is responsible for sliding pages (fragments) in and out upon user input
   // private ViewPager viewpager_shop;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView  = inflater.inflate(R.layout.activity_shop_dashboard, container, false);


        init();
        return rootView;
    }



    private void init() {
        mContext = getContext();


        ll_parent_product = (LinearLayout)rootView.findViewById(R.id.ll_parent_product);
        fetchShopItemsList();

    }


    public String loadJSONFromShopItemJson() {
        String json = null;
        try {
            InputStream is = getActivity().getAssets().open("shop_items.json");
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, StandardCharsets.UTF_8);
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
        return json;
    }

    private void fetchShopItemsList() {

        //Get state json value from assets folder
        try {
            JSONObject obj = new JSONObject(loadJSONFromShopItemJson());
            JSONArray m_jArry = obj.getJSONArray("shop_item");

            for (int i = 0; i < m_jArry.length(); i++) {

                JSONObject jo_inside = m_jArry.getJSONObject(i);

                String item = jo_inside.getString("item");


                LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                final View rowView = inflater.inflate(R.layout.row_list_card, null);

                final TextView row_item_name = (TextView) rowView.findViewById(R.id.row_item_name);

                row_item_name.setText(item.toUpperCase());
                ll_parent_product.addView(rowView);

                rowView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                            Intent i = new Intent(getActivity(),ShopListPage.class);
                            i.putExtra("product", row_item_name.getText().toString().trim());
                            startActivity(i);
                            getActivity().overridePendingTransition(R.animator.move_left,R.animator.move_right);

                    }
                });

            }



        } catch (JSONException e) {
            e.printStackTrace();
        }
    }





}
