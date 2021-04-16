package agrixilla.in.activities;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.smarteist.autoimageslider.IndicatorAnimations;
import com.smarteist.autoimageslider.IndicatorView.draw.controller.DrawController;
import com.smarteist.autoimageslider.SliderAnimations;
import com.smarteist.autoimageslider.SliderView;

import java.util.ArrayList;
import java.util.List;

import agrixilla.in.R;
import agrixilla.in.adapters.ImageSliderAdapter;
import agrixilla.in.models.SliderItem;

public class HomeFragment extends Fragment {

    private static final Integer[] IMAGES = {R.drawable.slider_image1, R.drawable.slider_image2, R.drawable.slider_image3, R.drawable.slider_image4, R.drawable.slider_image5};
    private static ViewPager mPager;
    private static final int currentPage = 0;
    private static final int NUM_PAGES = 0;
    View rootView;
    Context context;
    TextView Tv_DisplayGolu;
    SliderView sliderView;
    ImageSliderAdapter adapter;
    private ArrayList<Integer> ImagesArray = new ArrayList<Integer>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.activity_home_fragment, container, false);


        init();
        return rootView;
    }

    private void init() {
        mPager = (ViewPager) rootView.findViewById(R.id.image_pager);
        sliderView = (SliderView) rootView.findViewById(R.id.imageSlider);

        sliderView.setIndicatorAnimation(IndicatorAnimations.THIN_WORM); //set indicator animation by using SliderLayout.IndicatorAnimations. :WORM or THIN_WORM or COLOR or DROP or FILL or NONE or SCALE or SCALE_DOWN or SLIDE and SWAP!!
        sliderView.setSliderTransformAnimation(SliderAnimations.SIMPLETRANSFORMATION);
        sliderView.setAutoCycleDirection(SliderView.AUTO_CYCLE_DIRECTION_RIGHT);
        sliderView.setIndicatorSelectedColor(Color.GREEN);
        sliderView.setIndicatorUnselectedColor(Color.GRAY);
        sliderView.setScrollTimeInSec(3);
        sliderView.setAutoCycle(true);
        sliderView.startAutoCycle();

        sliderView.setOnIndicatorClickListener(new DrawController.ClickListener() {
            @Override
            public void onIndicatorClicked(int position) {
                sliderView.setCurrentPagePosition(position);
            }
        });
        adapter = new ImageSliderAdapter(context);
        List<SliderItem> sliderItemList = new ArrayList<>();

        ImagesArray = new ArrayList<>();
        for (int i = 0; i < IMAGES.length; i++) {

            SliderItem sliderItem = new SliderItem();

            Log.d("In Home Fragment - ", "---> " + IMAGES[i]);
            sliderItem.setDrawableImage(IMAGES[i]);
            sliderItemList.add(sliderItem);
            adapter.addItem(sliderItem);


        }
        //adapter.renewItems(sliderItemList);

        sliderView.setSliderAdapter(adapter);

    }

    public void renewItems(View view) {
        List<SliderItem> sliderItemList = new ArrayList<>();
        //dummy data
        for (int i = 0; i < IMAGES.length; i++) {
            SliderItem sliderItem = new SliderItem();
            sliderItem.setDrawableImage(IMAGES[i]);
            sliderItemList.add(sliderItem);
            adapter.addItem(sliderItem);
        }
        adapter.renewItems(sliderItemList);
    }


    public void removeLastItem(View view) {
        if (adapter.getCount() - 1 >= 0)
            adapter.deleteItem(adapter.getCount() - 1);
    }

    public void addNewItem(View view) {
        List<SliderItem> sliderItemList = new ArrayList<>();

        for (int i = 0; i < IMAGES.length; i++) {
            SliderItem sliderItem = new SliderItem();
            sliderItem.setDrawableImage(IMAGES[i]);
            sliderItemList.add(sliderItem);
            adapter.addItem(sliderItem);
        }

    }

}
