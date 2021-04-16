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
import agrixilla.in.account_screens.AccountStatement;
import agrixilla.in.account_screens.Billing;
import agrixilla.in.account_screens.CreditNotes;
import agrixilla.in.account_screens.ManageProfile;
import agrixilla.in.account_screens.Payments;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Objects;

public class AccountFragment extends Fragment {

    View rootView;
    Context context;
    LinearLayout ll_parent_account;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView  = inflater.inflate(R.layout.activity_account_dashboard, container, false);


        init();
        return rootView;
    }

    private void init() {

        ll_parent_account = (LinearLayout)rootView.findViewById(R.id.ll_parent_account);

        fetchAccountItemsList();


    }

    private String loadJSONFromAccountItemJson() {
        String json = null;
        try {
            InputStream is = Objects.requireNonNull(getActivity()).getAssets().open("account_items.json");
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

    private void fetchAccountItemsList() {

        //Get state json value from assets folder
        try {
            JSONObject obj = new JSONObject(Objects.requireNonNull(loadJSONFromAccountItemJson()));
            JSONArray m_jArry = obj.getJSONArray("acc_items");

            for (int i = 0; i < m_jArry.length(); i++) {

                JSONObject jo_inside = m_jArry.getJSONObject(i);

                String item = jo_inside.getString("item");


                LayoutInflater inflater = (LayoutInflater) Objects.requireNonNull(getActivity()).getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                final View rowView = Objects.requireNonNull(inflater).inflate(R.layout.row_list_card, null);

                final TextView row_item_name = (TextView) rowView.findViewById(R.id.row_item_name);

                row_item_name.setText(item.toUpperCase());
                ll_parent_account.addView(rowView);

                rowView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(row_item_name.getText().toString().equalsIgnoreCase("Manage Profile"))
                        {

                            Intent i = new Intent(getActivity(),ManageProfile.class);
                            startActivity(i);
                            Objects.requireNonNull(getActivity()).overridePendingTransition(R.animator.move_left,R.animator.move_right);
                        }else  if(row_item_name.getText().toString().equalsIgnoreCase("Account Statement"))
                        {

                            Intent i = new Intent(getActivity(),AccountStatement.class);
                            startActivity(i);
                            Objects.requireNonNull(getActivity()).overridePendingTransition(R.animator.move_left,R.animator.move_right);
                        }else  if(row_item_name.getText().toString().equalsIgnoreCase("Billing"))
                        {

                            Intent i = new Intent(getActivity(),Billing.class);
                            startActivity(i);
                            Objects.requireNonNull(getActivity()).overridePendingTransition(R.animator.move_left,R.animator.move_right);
                        }else  if(row_item_name.getText().toString().equalsIgnoreCase("Payments"))
                        {

                            Intent i = new Intent(getActivity(),Payments.class);
                            startActivity(i);
                            Objects.requireNonNull(getActivity()).overridePendingTransition(R.animator.move_left,R.animator.move_right);
                        }else  if(row_item_name.getText().toString().equalsIgnoreCase("Credit Notes"))
                        {

                            Intent i = new Intent(getActivity(),CreditNotes.class);
                            startActivity(i);
                            Objects.requireNonNull(getActivity()).overridePendingTransition(R.animator.move_left,R.animator.move_right);
                        }
                    }
                });
            }



        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
