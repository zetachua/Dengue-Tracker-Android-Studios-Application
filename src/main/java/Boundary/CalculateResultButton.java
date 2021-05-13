package Boundary;


import android.view.View;
import Control.SymptomCheckerManager;


public class CalculateResultButton implements AppStrategy {

    @Override
    public void buttonClick() {
        SymptomCheckerManager symptomManager = new SymptomCheckerManager();
        boolean potentialDengue = symptomManager.calculateResult();

        if (!potentialDengue) {
            SymptomCheckerActivity.nearbyClinicsBtn.setVisibility(View.INVISIBLE);
            String unlikelyDengue = "\nUnlikely" +
                    " case of Dengue.";
            SymptomCheckerActivity.resultTextView.setText(unlikelyDengue);
        }
        else {
            String likelyDengue = "\nPotential" +
                    " case of Dengue. Please visit a nearby Clinic.\n" ;
            SymptomCheckerActivity.resultTextView.setText(likelyDengue);
            SymptomCheckerActivity.nearbyClinicsBtn.setVisibility(View.VISIBLE);
        }

        SymptomCheckerActivity.resultTextView.setEnabled(false);
    }
}
