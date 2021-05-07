package com.achocallaromero.ventas;

import android.content.Context;
import android.net.Uri;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class ConstantsFunctionsJava {

    public static Call post(Context context, String url, String json, Callback callback) {
        long connectTimeout = 0;
        long readTimeout = 300;
        long writeTimeout = 300;

        // ------------------------------------------------------------------------------------
        OkHttpClient client = new OkHttpClient.Builder()
                //.connectTimeout(connectTimeout, TimeUnit.MILLISECONDS)
                //.readTimeout(readTimeout, TimeUnit.SECONDS)
                //.writeTimeout(writeTimeout, TimeUnit.SECONDS)
                .addInterceptor(new Interceptor() {
                    @NotNull
                    @Override
                    public Response intercept(@NotNull Chain chain) throws IOException {
                        Request newRequest = chain.request().newBuilder()
                                //.addHeader("username", GlobalVar.Companion.getUsername())
                                //.addHeader("Authorization", GlobalVar.Companion.getJwt().substring(4) )
                                .build();
                        return chain.proceed(newRequest);

                    }
                })
                .build();
        // ------------------------------------------------------------------------------------

        MediaType JSON = MediaType.parse("application/json; charset=utf-8");
        //OkHttpClient client = new OkHttpClient();

        RequestBody body = RequestBody.create(json, JSON);
        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .build();
        Call call = client.newCall(request);
        call.enqueue(callback);
        return call;
    }

    public static Call postMultple(Context context, String url, JSONArray params, ArrayList<File> files, ArrayList<String> fileParams, Callback callback) {
        long readTimeout = 0;

        // ------------------------------------------------------------------------------------
        OkHttpClient client = new OkHttpClient.Builder()
                //.readTimeout(readTimeout, TimeUnit.MILLISECONDS)
                .build();

        MultipartBody.Builder multipartBuilder = new MultipartBody.Builder()
                .setType(MultipartBody.FORM);

        try{
            for(int i=0; i<params.length(); i++){
                JSONObject itemArray = params.getJSONObject(i);
                multipartBuilder.addFormDataPart(itemArray.getString("key"), itemArray.getString("value") );
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        for(int j=0; j<files.size(); j++){
            multipartBuilder.addFormDataPart(fileParams.get(j),files.get(j).getName(), RequestBody.create(files.get(j),MediaType.parse(files.get(j).getPath())) );
        }

        RequestBody requestBody = multipartBuilder.build();

        Request request = new Request.Builder()
                //.addHeader("username", GlobalVar.Companion.getUsername())
                //.addHeader("Authorization", GlobalVar.Companion.getJwt().substring(4) )
                .url(url)
                .post(requestBody)
                .build();
        Call call = client.newCall(request);
        call.enqueue(callback);
        return call;
    }


    /*public static Call post(Context context, String url, String json, Callback callback) {
        try {
            long connectTimeout = 0;
            long readTimeout = 300;
            long writeTimeout = 300;

            // ------------------------------------------------------------------------------------
            final TrustManager[] trustAllCerts = new TrustManager[]{
                    new X509TrustManager() {

                        @Override
                        public void checkClientTrusted(java.security.cert.X509Certificate[] chain,
                                                       String authType) throws
                                CertificateException {
                        }

                        @Override
                        public void checkServerTrusted(java.security.cert.X509Certificate[] chain,
                                                       String authType) throws
                                CertificateException {
                        }

                        @Override
                        public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                            return new java.security.cert.X509Certificate[]{};
                        }
                    }
            };

            // final SSLContext sslContext = SSLContext.getInstance("TLSv1.2");
            final SSLContext sslContext = SSLContext.getInstance("SSL");
            sslContext.init(null, trustAllCerts, new java.security.SecureRandom());

            final SSLSocketFactory sslSocketFactory = sslContext.getSocketFactory();
            // ------------------------------------------------------------------------------------
            OkHttpClient client = new OkHttpClient.Builder()
                    //.connectTimeout(connectTimeout, TimeUnit.MILLISECONDS)
                    //.readTimeout(readTimeout, TimeUnit.SECONDS)
                    //.writeTimeout(writeTimeout, TimeUnit.SECONDS)
                    .sslSocketFactory(sslSocketFactory, (X509TrustManager) trustAllCerts[0])
                    .hostnameVerifier(new HostnameVerifier() {
                        @Override
                        public boolean verify(String hostname, SSLSession session) {
                            return true;
                        }
                    })
                    .addInterceptor(new Interceptor() {
                        @NotNull
                        @Override
                        public Response intercept(@NotNull Chain chain) throws IOException {
                            Request newRequest = chain.request().newBuilder()
                                    .addHeader("username", GlobalVar.Companion.getUsername())
                                    .addHeader("Authorization", GlobalVar.Companion.getJwt().substring(4) )
                                    .build();
                            return chain.proceed(newRequest);

                        }
                    })
                    .build();
            // ------------------------------------------------------------------------------------

            MediaType JSON = MediaType.parse("application/json; charset=utf-8");
            //OkHttpClient client = new OkHttpClient();

            RequestBody body = RequestBody.create(json, JSON);
            Request request = new Request.Builder()
                    .url(url)
                    .post(body)
                    .build();
            Call call = client.newCall(request);
            call.enqueue(callback);
            return call;
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        } catch (KeyManagementException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static Call postMultple(Context context, String url, JSONArray params, ArrayList<File> files, ArrayList<String> fileParams, Callback callback) {
        try {
            long readTimeout = 0;
            // ------------------------------------------------------------------------------------
            final TrustManager[] trustAllCerts = new TrustManager[]{
                    new X509TrustManager() {

                        @Override
                        public void checkClientTrusted(java.security.cert.X509Certificate[] chain,
                                                       String authType) throws
                                CertificateException {
                        }

                        @Override
                        public void checkServerTrusted(java.security.cert.X509Certificate[] chain,
                                                       String authType) throws
                                CertificateException {
                        }

                        @Override
                        public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                            return new java.security.cert.X509Certificate[]{};
                        }
                    }
            };

            // final SSLContext sslContext = SSLContext.getInstance("TLSv1.2");
            final SSLContext sslContext = SSLContext.getInstance("SSL");
            sslContext.init(null, trustAllCerts, new java.security.SecureRandom());

            final SSLSocketFactory sslSocketFactory = sslContext.getSocketFactory();
            // ------------------------------------------------------------------------------------
            OkHttpClient client = new OkHttpClient.Builder()
                    //.readTimeout(readTimeout, TimeUnit.MILLISECONDS)
                    .sslSocketFactory(sslSocketFactory, (X509TrustManager) trustAllCerts[0])
                    .hostnameVerifier(new HostnameVerifier() {
                        @Override
                        public boolean verify(String hostname, SSLSession session) {
                            return true;
                        }
                    })
                    .build();

            MultipartBody.Builder multipartBuilder = new MultipartBody.Builder()
                    .setType(MultipartBody.FORM);

            try{
                for(int i=0; i<params.length(); i++){
                    JSONObject itemArray = params.getJSONObject(i);
                    multipartBuilder.addFormDataPart(itemArray.getString("key"), itemArray.getString("value") );
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

            for(int j=0; j<files.size(); j++){
                multipartBuilder.addFormDataPart(fileParams.get(j),files.get(j).getName(), RequestBody.create(files.get(j),MediaType.parse(files.get(j).getPath())) );
            }

            RequestBody requestBody = multipartBuilder.build();

            Request request = new Request.Builder()
                    .addHeader("username", GlobalVar.Companion.getUsername())
                    .addHeader("Authorization", GlobalVar.Companion.getJwt().substring(4) )
                    .url(url)
                    .post(requestBody)
                    .build();
            Call call = client.newCall(request);
            call.enqueue(callback);
            return call;
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        } catch (KeyManagementException e) {
            e.printStackTrace();
        }
        return null;
    }*/

    /*public static SSLSocketFactory generateSsl(Context context, String url, String json, Callback callback) {
        try {
            // ------------------------------------------------------------------------------------
            final TrustManager[] trustAllCerts = new TrustManager[]{
                    new X509TrustManager() {

                        @Override
                        public void checkClientTrusted(java.security.cert.X509Certificate[] chain,
                                                       String authType) throws
                                CertificateException {
                        }

                        @Override
                        public void checkServerTrusted(java.security.cert.X509Certificate[] chain,
                                                       String authType) throws
                                CertificateException {
                        }

                        @Override
                        public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                            return new java.security.cert.X509Certificate[]{};
                        }
                    }
            };

            // final SSLContext sslContext = SSLContext.getInstance("TLSv1.2");
            final SSLContext sslContext = SSLContext.getInstance("SSL");
            sslContext.init(null, trustAllCerts, new java.security.SecureRandom());

            //final SSLSocketFactory sslSocketFactory = sslContext.getSocketFactory();
            // ------------------------------------------------------------------------------------
            return sslContext.getSocketFactory();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        } catch (KeyManagementException e) {
            e.printStackTrace();
        }
        return null;
    }*/
}
