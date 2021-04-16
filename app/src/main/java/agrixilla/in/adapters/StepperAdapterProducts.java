package agrixilla.in.adapters;

import android.content.Context;
import android.os.Bundle;
import androidx.annotation.IntRange;
import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;

import agrixilla.in.ShopFregments.BioPesticideFragment;
import agrixilla.in.ShopFregments.FungicideFragment;
import agrixilla.in.ShopFregments.HerbicideFragment;
import agrixilla.in.ShopFregments.InsecticidesFragment;
import agrixilla.in.ShopFregments.PGRFragment;
import com.stepstone.stepper.Step;
import com.stepstone.stepper.adapter.AbstractFragmentStepAdapter;
import com.stepstone.stepper.viewmodel.StepViewModel;

public class StepperAdapterProducts extends AbstractFragmentStepAdapter {

    private static final String CURRENT_STEP_POSITION_KEY = "messageResourceId";
    public StepperAdapterProducts(FragmentManager fm, Context context) {
        super(fm, context);
    }
    @Override
    public Step createStep(int position) {
        switch (position){

            case 0:
                final PGRFragment step0 = new PGRFragment();
                Bundle b0 = new Bundle();
                b0.putInt(CURRENT_STEP_POSITION_KEY, position);
                step0.setArguments(b0);
                return step0;



            case 1:
                final InsecticidesFragment step1 = new InsecticidesFragment();
                Bundle b1 = new Bundle();
                b1.putInt(CURRENT_STEP_POSITION_KEY, position);
                step1.setArguments(b1);
                return step1;


            case 2:
                final FungicideFragment step2 = new FungicideFragment();
                Bundle b2 = new Bundle();
                b2.putInt(CURRENT_STEP_POSITION_KEY, position);
                step2.setArguments(b2);
                return step2;

            case 3:
                final HerbicideFragment step3 = new HerbicideFragment();
                Bundle b3 = new Bundle();
                b3.putInt(CURRENT_STEP_POSITION_KEY, position);
                step3.setArguments(b3);
                return step3;

            case 4:
                final BioPesticideFragment step4 = new BioPesticideFragment();
                Bundle b4 = new Bundle();
                b4.putInt(CURRENT_STEP_POSITION_KEY, position);
                step4.setArguments(b4);
                return step4;





        }
        return null;
    }
    @Override
    public int getCount() {
        return 5;
    }
    @NonNull
    @Override
    public StepViewModel getViewModel(@IntRange(from = 0) int position) {
        //Override this method to set Step title for the Tabs, not necessary for other stepper types
        switch (position){
            case 0:
                return new StepViewModel.Builder(context)
                        .setTitle("PGR")
                        .setEndButtonLabel("Insecticides")
                        .create();

            case 1:
                return new StepViewModel.Builder(context)
                        .setTitle("Insecticides")
                        .setBackButtonLabel("PGR")
                        .setEndButtonLabel("Fungicides")
                        .create();
            case 2:
                return new StepViewModel.Builder(context)
                        .setTitle("Fungicides")
                        .setBackButtonLabel("Insecticides")
                        .setEndButtonLabel("Herbicides")
                        .create();

            case 3:
                return new StepViewModel.Builder(context)
                        .setTitle("Herbicides") //can be a CharSequence instead
                        .setBackButtonLabel("Fungicides")
                        .setEndButtonLabel("Bio-Pesticides")
                        .create();

            case 4:
                return new StepViewModel.Builder(context)
                        .setTitle("Bio-Pesticides") //can be a CharSequence instead
                        .setBackButtonLabel("Herbicides")
                        .create();



        }
        return null;
    }
}
