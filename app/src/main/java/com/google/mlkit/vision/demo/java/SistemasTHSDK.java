package com.google.mlkit.vision.demo.java;

import android.content.Intent;


public class SistemasTHSDK {

    public SistemasTHSDK(String ApiKey) {
        loadDataFromServer();
    }
    private void loadDataFromServer() {

    }
    public void startLivenessDetection(android.content.Context ctx) throws Exception {

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            Intent intent = new Intent(ctx, LivenessActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            ctx.startActivity(intent);
        } else {
            throw new Exception("Version should be above " + android.os.Build.VERSION_CODES.LOLLIPOP);

        }


    }

}
