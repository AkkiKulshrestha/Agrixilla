package agrixilla.in.adapters;

import android.content.Context;
import android.os.Bundle;
import androidx.annotation.IntRange;
import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;

import agrixilla.in.investorRegistration.ContactInfoFragment;
import agrixilla.in.investorRegistration.CustomerInfoRegistration;
import agrixilla.in.investorRegistration.OtherInfoFragment;
import agrixilla.in.investorRegistration.RegisterPan;
import com.stepstone.stepper.Step;
import com.stepstone.stepper.adapter.AbstractFragmentStepAdapter;
import com.stepstone.stepper.viewmodel.StepViewModel;


public class StepperAdapter extends AbstractFragmentStepAdapter {

    private static final String CURRENT_STEP_POSITION_KEY = "messageResourceId";
    public StepperAdapter(FragmentManager fm, Context context) {
        super(fm, context);
    }
    @Override
    public Step createStep(int position) {
        switch (position){

            case 0:
                final RegisterPan step0 = new RegisterPan();
                Bundle b0 = new Bundle();
                b0.putInt(CURRENT_STEP_POSITION_KEY, position);
                step0.setArguments(b0);
                return step0;



            case 1:
                final CustomerInfoRegistration step1 = new CustomerInfoRegistration();
                Bundle b1 = new Bundle();
                b1.putInt(CURRENT_STEP_POSITION_KEY, position);
                step1.setArguments(b1);
                return step1;


            case 2:
                final ContactInfoFragment step2 = new ContactInfoFragment();
                Bundle b2 = new Bundle();
                b2.putInt(CURRENT_STEP_POSITION_KEY, position);
                step2.setArguments(b2);
                return step2;

            case 3:
                final OtherInfoFragment step3 = new OtherInfoFragment();
                Bundle b3 = new Bundle();
                b3.putInt(CURRENT_STEP_POSITION_KEY, position);
                step3.setArguments(b3);
                return step3;





        }
        return null;
    }
    @Override
    public int getCount() {
        return 4;
    }
    @NonNull
    @Override
    public StepViewModel getViewModel(@IntRange(from = 0) int position) {
        //Override this method to set Step title for the Tabs, not necessary for other stepper types
        switch (position){
            case 0:
                return new StepViewModel.Builder(context)
                        .setTitle("REGISTER")
                        .setEndButtonLabel("DISTRIBUTOR DETAILS")
                        .create();

            case 1:
                return new StepViewModel.Builder(context)
                        .setTitle("DISTRIBUTOR DETAILS")
                        .setBackButtonLabel("REGISTER")
                        .setEndButtonLabel("CONTACT DETAILS")
                        .create();
            case 2:
                return new StepViewModel.Builder(context)
                        .setTitle("CONTACT DETAILS")
                        .setBackButtonLabel("DISTRIBUTOR DETAILS")
                        .setEndButtonLabel("OTHER DETAILS")
                        .create();

            case 3:
                return new StepViewModel.Builder(context)
                        .setTitle("OTHER DETAILS") //can be a CharSequence instead
                        .setBackButtonLabel("CONTACT DETAILS")
                        .setEndButtonLabel("SUBMIT")
                        .create();




        }
        return null;
    }
}
