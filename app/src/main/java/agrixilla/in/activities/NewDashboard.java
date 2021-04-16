package  agrixilla.in.activities;

import android.os.Bundle;
import android.os.Handler;
import com.google.android.material.tabs.TabLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import  agrixilla.in.R;
import  agrixilla.in.utils.CommonMethods;
import agrixilla.in.utils.UtilitySharedPreferences;

/**
 * Created by Akshay on 11-06-2018.
 */

public class NewDashboard extends AppCompatActivity {

   // private final String LOG_TAG = SHOPActivity.class.getSimpleName();

    ImageView iv_back;

    // Titles of the individual pages (displayed in tabs)
    private final String[] PAGE_TITLES = new String[] {
            "HOME",
            "SHOP",
            "ACCOUNT",
            "MORE"
    };

    // The fragments that are used as the individual pages
    private final Fragment[] PAGES = new Fragment[] {
            new HomeFragment(),
            new ShopFragment(),
            new AccountFragment(),
            new OtherFragment()

    };

    final int[] ICONS = new int[]{
            R.drawable.home_icon,
            R.drawable.shop_icon,
            R.drawable.account_icon,
            R.drawable.more_icon
    };

    int position = 0;

    // The ViewPager is responsible for sliding pages (fragments) in and out upon user input
    private ViewPager viewpager_main_activity;
    String StrPan,StrCustomerId,StrAgency,StrProprietorName,StrTelephone,StrMobile;
    TextView txt_firm_name,txt_mobile_no;
    ImageView iv_notification;
    boolean doubleBackToExitPressedOnce = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_dashboard_main_activity);


        init();
    }

    private void init() {
        Bundle extras = getIntent().getExtras();
        if(extras != null) {
            position = extras.getInt("viewpager_position");
        }

        StrPan = UtilitySharedPreferences.getPrefs(getApplicationContext(),"ClientPan");
        StrCustomerId = UtilitySharedPreferences.getPrefs(getApplicationContext(),"ClientCode");
        StrAgency =  UtilitySharedPreferences.getPrefs(getApplicationContext(),"ClientFirmName");
        StrProprietorName = UtilitySharedPreferences.getPrefs(getApplicationContext(),"ClientProprietorName");
        StrTelephone =  UtilitySharedPreferences.getPrefs(getApplicationContext(),"ClientTelephone");
        StrMobile = UtilitySharedPreferences.getPrefs(getApplicationContext(),"ClientMobile");

        txt_firm_name = (TextView)findViewById(R.id.txt_firm_name);
        txt_mobile_no = (TextView)findViewById(R.id.txt_mobile_no);
        iv_notification  = (ImageView)findViewById(R.id.iv_notification);
        iv_notification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CommonMethods.DisplayToast(getApplicationContext(),"Please wait for Notification.");
            }
        });

        if(StrAgency!=null && !StrAgency.equalsIgnoreCase("") && !StrAgency.equalsIgnoreCase("null")) {
            txt_firm_name.setText(StrAgency);
        }

        if(StrMobile!=null && !StrMobile.equalsIgnoreCase("") && !StrMobile.equalsIgnoreCase("null")) {
            txt_mobile_no.setText(StrMobile);
        }

        viewpager_main_activity = (ViewPager) findViewById(R.id.viewpager_main_activity);
        viewpager_main_activity.setAdapter(new MyPagerAdapter(getSupportFragmentManager()));

        viewpager_main_activity.setCurrentItem(position);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tab_layout_main_segment);
        tabLayout.setupWithViewPager(viewpager_main_activity,true);

        tabLayout.getTabAt(0).setIcon(ICONS[0]);
        tabLayout.getTabAt(1).setIcon(ICONS[1]);
        tabLayout.getTabAt(2).setIcon(ICONS[2]);
        tabLayout.getTabAt(3).setIcon(ICONS[3]);


    }

    /* PagerAdapter for supplying the ViewPager with the pages (fragments) to display. */
    public class MyPagerAdapter extends FragmentPagerAdapter {

        public MyPagerAdapter(FragmentManager fragmentManager) {
            super(fragmentManager);
        }

        @Override
        public Fragment getItem(int position) {
            return PAGES[position];
        }

        @Override
        public int getCount() {
            return PAGES.length;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return PAGE_TITLES[position];
        }

    }

    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            return;
        }

        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, "Please click BACK again to exit", Toast.LENGTH_SHORT).show();

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                doubleBackToExitPressedOnce=false;
            }
        }, 2000);
    }
}
