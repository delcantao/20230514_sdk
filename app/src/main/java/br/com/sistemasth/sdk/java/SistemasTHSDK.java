package br.com.sistemasth.sdk.java;

import android.content.Context;
import android.os.Build;
import android.util.Log;

import androidx.annotation.RequiresApi;

import com.google.android.gms.common.annotation.KeepName;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.atomic.AtomicReference;

import br.com.sistemasth.sdk.BuildConfig;
import br.com.sistemasth.sdk.java.th.Config;

@KeepName
@RequiresApi(Build.VERSION_CODES.LOLLIPOP)
public final class SistemasTHSDK {

    private CompletableFuture<Config> configConstructorFuture;
    private CompletableFuture<Config> configLivenessStart;
    private String rrrr;

    public SistemasTHSDK(String apiKey) {
        validateApiKey(apiKey);
        loadDataFromServer();
    }

    public SistemasTHSDK(String client, String user, String password) {
        validateUserAndPassword(client, user, password);
        loadDataFromServer();
    }

    private void validateUserAndPassword(String client, String user, String password) {
        if (client == null || client.isEmpty()) {
            throw new IllegalArgumentException("Client não pode ser nula ou vazia");
        }
        if (user == null || user.isEmpty()) {
            throw new IllegalArgumentException("User não pode ser nula ou vazia");
        }
        if (password == null || password.isEmpty()) {
            throw new IllegalArgumentException("Password não pode ser nula ou vazia");
        }
    }


    private void validateApiKey(String apiKey) {
        if (apiKey == null || apiKey.isEmpty()) {
            throw new IllegalArgumentException("API Key não pode ser nula ou vazia");
        }


//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
//            constructorFuture = CompletableFuture.runAsync(() -> {
//                // Chamada HTTP no construtor
//                // Aguarde até que a chamada seja concluída
//
//                try {
//                    URL url = new URL("https://liveness.sistemasth.com.br/api/config"); // URL da API
//                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
//                    connection.setRequestMethod("POST");
//                    connection.setRequestProperty("Content-Type", "application/json"); // Definir o tipo de conteúdo como JSON
//
//                    // Criar o objeto JSON que será enviado no corpo da requisição
//                    JSONObject jsonBody = new JSONObject();
//                    jsonBody.put("key1", "value1");
//                    jsonBody.put("key2", "value2");
//                    // Adicionar mais campos conforme necessário
//
//                    // Obter o JSON como uma string
//                    String requestBody = jsonBody.toString();
//
//                    // Enviar os dados JSON no corpo da requisição
//                    connection.setDoOutput(true);
//                    OutputStream outputStream = connection.getOutputStream();
//                    BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
//                    writer.write(requestBody);
//                    writer.flush();
//                    writer.close();
//                    outputStream.close();
//
//                    // Fazer a leitura da resposta da API, se necessário
//                    BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
//                    String response;
//                    while ((response = reader.readLine()) != null) {
//                        // Processar a resposta da API, se necessário
//                        rrrr = response;
//                    }
//
//                    reader.close();
//                    connection.disconnect();
//                } catch (IOException e) {
//                    throw new RuntimeException(e);
//                } catch (JSONException e) {
//                    throw new RuntimeException(e);
//                }
//
//            });
//        }

    }

    private void loadDataFromServer() {

        AtomicReference<Config> cfgReference = new AtomicReference<>();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            configConstructorFuture = CompletableFuture.runAsync(() -> {
                // Chamada HTTP no construtor
                // Aguarde até que a chamada seja concluída
                Config cfg = new Config();
                try {
                    URL url = new URL("https://liveness.sistemasth.com.br/api/config"); // URL da API
                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                    connection.setRequestMethod("GET");
                    connection.setRequestProperty("x-api-key", BuildConfig.API_KEY);

                    // Check the response code
                    int responseCode = connection.getResponseCode();
                    if (responseCode == HttpURLConnection.HTTP_OK) {
                        // Read the response
                        BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                        StringBuilder response = new StringBuilder();
                        String line;
                        while ((line = reader.readLine()) != null) {
                            response.append(line);
                        }
                        reader.close();

                        // Parse the JSON response
                        String jsonResponse = response.toString();
                        JSONObject jsonObject = new JSONObject(jsonResponse);

                        // Access the JSON data as needed
                        cfg.nosePointSize = (float) jsonObject.getDouble("nosePointSize");
                        cfg.nosePointRadius = (float) jsonObject.getDouble("nosePointRadius");
                        cfg.nosePointColor = jsonObject.getString("nosePointHexColor");
                        cfg.boxColor = jsonObject.getString("boxHexColor");
                        cfg.urlApi = jsonObject.getString("urlApi");
                        cfg.cnhProbabilityPercent = (float) jsonObject.getDouble("cnhProbabilityPercent");


                    } else {
                        // Handle the unsuccessful response
                        throw new RuntimeException("HTTP request failed with response code: " + responseCode);
                    }

                    connection.disconnect();

                } catch (IOException | JSONException e) {
                    throw new RuntimeException(e);
                }

            }).thenApply(ignore -> cfgReference.get());
        }
    }

    @KeepName
    public void startLivenessDetection(Context currentActivity) throws Exception {

        Log.d("SistemasTHSDK1", "Começar o liveness detection" + (rrrr == null ? "null" : rrrr));

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            configConstructorFuture.thenRun(() -> {
                Config cfg = configConstructorFuture.join();

                // Aqui você pode ter certeza de que a chamada do construtor foi finalizada
                // Faça qualquer operação adicional necessária


                Log.d("SistemasTHSDK2", cfg.urlApi);
                Log.d("SistemasTHSDK2", cfg.boxColor);
                Log.d("SistemasTHSDK2", cfg.nosePointColor);
                Log.d("SistemasTHSDK2", "cnhProbabilityPercent: " + cfg.cnhProbabilityPercent);
                Log.d("SistemasTHSDK2", "nosePointSize: " + cfg.nosePointSize);
                Log.d("SistemasTHSDK2", "nosePointRadius: " + cfg.nosePointRadius);

//                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//                    Intent intent = new Intent(currentActivity, LivenessActivity.class);
//                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                    currentActivity.startActivityForResult(intent, 1);
//                }

            }).thenRun(() -> {

            });
        }
    }


    private void startVerify() {
        if (startVerifyDone != 0) return;
        if (imageHeight == 0 || imageWidth == 0) return;
        setTextInstructions(_context.getResources().getString(R.string.wait_for_the_white_nose_point));

        startVerifyDone = 1;

        try {
            URL url = new URL(baseUrl + "us/challenge/start");
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setRequestProperty("model", Build.MODEL);
            connection.setRequestProperty("OSVersion", Build.VERSION.RELEASE);
            connection.setRequestProperty("ID", Settings.Secure.ANDROID_ID);
            connection.setDoOutput(true);

            JSONObject body = new JSONObject();
            body.put("userId", "9286228c-5b45-4b73-9284-c44d877f0a81");
            body.put("imageWidth", imageWidth);
            body.put("imageHeight", imageHeight);

            OutputStreamWriter writer = new OutputStreamWriter(connection.getOutputStream());
            writer.write(body.toString());
            writer.flush();

            int responseCode = connection.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                StringBuilder response = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    response.append(line);
                }
                reader.close();

                JSONObject jsonObject = new JSONObject(response.toString());
                requestId = jsonObject.getString("id");
                String userId = jsonObject.getString("userId");
                token = jsonObject.getString("token");
                xNose = jsonObject.getInt("noseLeft");
                yNose = jsonObject.getInt("noseTop");
                heightBox = jsonObject.getInt("areaHeight");
                widthBox = jsonObject.getInt("areaWidth");
                xBox = jsonObject.getInt("areaLeft");
                yBox = jsonObject.getInt("areaTop");

                startVerifyDone = 2;
                setTextInstructions(_context.getResources().getString(R.string.wait_for_the_white_nose_point));
            } else {
                Log.d(TAG, "StartVerify: HTTP request failed with response code: " + responseCode);
            }

            connection.disconnect();
        } catch (IOException | JSONException e) {
            e.printStackTrace();
            Log.d(TAG, "StartVerify: Exception: " + e.getMessage());
        }
    }


    @KeepName
    public void startDocumentCapture(Context currentActivity) {
        Log.d("SistemasTHSDK", "startOcrDetection");
    }

    @KeepName
    public void performFacematch(String base64Doc, String base64Selfie) {
        Log.d("SistemasTHSDK", "startFaceMatch");
    }

}
