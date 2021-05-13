package Control;

import Boundary.SymptomCheckerActivity;

import java.util.ArrayList;

public class SymptomCheckerManager {

    public static float calculate(ArrayList<String> s) {
        float total=0;
        float severe = 30;
        float moderate = 20;
        float mild = 10;
        for (String a:s){
            if (a == "Fever") total += severe;
            if (a == "Severe Headache") total += severe;
            if (a == "Skin Rash") total += moderate;
            if (a == "Mild Bleeding") total += mild;
            if (a == "Nausea/Vomiting") total += mild;
            if (a == "Joint/Muscle Ache") total += moderate;
        }
        return total;
    }

    public boolean calculateResult() {
        boolean potentialDengue=false;

        float percentageResult = SymptomCheckerManager.calculate(SymptomCheckerActivity.result);

        if (percentageResult >= 50) potentialDengue=true;

        return potentialDengue;
    }


}
