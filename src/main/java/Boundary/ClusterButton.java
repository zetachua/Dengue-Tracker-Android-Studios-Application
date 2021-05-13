package Boundary;

import android.content.Context;

import Control.ClinicManager;

import org.json.JSONException;

public class ClusterButton implements AppStrategy {
    private Context clusterBtnContext;

    public ClusterButton(Context context) {
        this.clusterBtnContext = context;
    }
    ClinicManager clinicManager = new ClinicManager();

    @Override
    public void buttonClick() {
        ClusterUI clusterUI = new ClusterUI(clusterBtnContext);
        clinicManager.removeClinicObjects();
        try {
            clusterUI.displayClusterInfo();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
