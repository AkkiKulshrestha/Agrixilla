package agrixilla.in.investorRegistration;

import androidx.core.content.ContextCompat;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;

import agrixilla.in.R;
import agrixilla.in.adapters.StepperAdapter;
import com.stepstone.stepper.StepperLayout;

public class InvestorRegistration extends AppCompatActivity {

     StepperLayout mStepperLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_investor_registration);




        mStepperLayout = (StepperLayout) findViewById(R.id.stepperLayout);
        mStepperLayout.setAdapter(new StepperAdapter(getSupportFragmentManager(), this));

        //mStepperLayout.setTabNavigationEnabled(false);

    }

}
