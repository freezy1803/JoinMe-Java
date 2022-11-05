package de.freezy.joinme.main;


import com.google.common.net.MediaType;
import okhttp3.*;

import java.io.IOException;

public class CheckLizenz {


    public static String check(String var) throws IOException {


        OkHttpClient client = new OkHttpClient().newBuilder().build();
        RequestBody body = new MultipartBody.Builder().setType(MultipartBody.FORM)
                .addFormDataPart("user", Lizenz.user)
                .addFormDataPart("plugin", Lizenz.pluginName)
                .build();
        Request request = new Request.Builder()
                .url("https://api.frostmc.de/v1/getlicense")
                .method("POST", body)
                .addHeader("X-Auth-Token", Lizenz.key)
                .build();

        Response response = client.newCall(request).execute();


        String resStr = response.body().string();

        //resStr to JSON
        com.google.gson.JsonParser parser = new com.google.gson.JsonParser();

        com.google.gson.JsonObject json = parser.parse(resStr).getAsJsonObject();



        return json.get(var).getAsString();

    }





}
