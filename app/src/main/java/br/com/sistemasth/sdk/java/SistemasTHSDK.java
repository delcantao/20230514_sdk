package br.com.sistemasth.sdk.java;

import android.content.Context;
import android.os.Build;
import android.util.JsonReader;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.CompletableFuture;


public final class SistemasTHSDK {

    private CompletableFuture<Void> constructorFuture;
    private String rrrr;

    public SistemasTHSDK(String apiKey) {
        validateApiKey(apiKey);
        loadDataFromServer();
    }


    private void validateApiKey(String apiKey) {
        Log.d("SistemasTHSDK", apiKey);
        Log.d("SistemasTHSDK", "validateApiKey");
        Log.d("SistemasTHSDK", "validateApiKey");
    }

    private void loadDataFromServer() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            constructorFuture = CompletableFuture.runAsync(() -> {
                // Chamada HTTP no construtor
                // Aguarde até que a chamada seja concluída

                try {
                    URL url = new URL("https://liveness.sistemasth.com.br/api/config"); // URL da API
                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                    connection.setRequestMethod("GET");

                    // Fazer a leitura da resposta da API, se necessário
                    BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                    String response;
                    while ((response = reader.readLine()) != null) {
                        // Processar a resposta da API, se necessário
                        rrrr = response;
                    }


                    reader.close();
                    connection.disconnect();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }

            });
        }
    }

    public void startLivenessDetection(Context currentActivity) throws Exception {



        Log.d("SistemasTHSDK1", rrrr == null ? "null" : rrrr);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            constructorFuture.thenRun(() -> {
                // Aqui você pode ter certeza de que a chamada do construtor foi finalizada
                // Faça qualquer operação adicional necessária

                Log.d("SistemasTHSDK2", rrrr == null ? "null" : rrrr);

//                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//                    Intent intent = new Intent(currentActivity, LivenessActivity.class);
//                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                    currentActivity.startActivityForResult(intent, 1);
//                }

            });
        }
    }

}
