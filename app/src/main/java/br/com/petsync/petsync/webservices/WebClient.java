package br.com.petsync.petsync.webservices;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

/**
 * Created by Virgínia on 11/07/2016.
 */
public class WebClient {

    private String resposta = null;

    public String getJsonFromUrl(String urlString) {

        String retorno = null;
        InputStream inputStream;
        try {

            URL url = new URL(urlString);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();

            connection.setRequestProperty("Accept", "application/json");
            connection.connect();

            int responseCode = connection.getResponseCode();


            if(responseCode < HttpURLConnection.HTTP_BAD_REQUEST) {
                inputStream = connection.getInputStream();

            } else {
                inputStream = connection.getErrorStream();
            }
            String contentEncoding = connection.getContentEncoding();
            retorno = converterInputStreamToString(inputStream);
            inputStream.close();
            connection.disconnect();

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return retorno;
    }

    private static String converterInputStreamToString(InputStream inputStream){
        StringBuffer buffer = new StringBuffer();
        try{
            BufferedReader bReader = new BufferedReader(new InputStreamReader(inputStream, "utf-8"), 8);

            String line = null;
            while ((line = bReader.readLine()) != null) {
                buffer.append(line + "\n");
            }
            inputStream.close();
        }catch(IOException e){
            e.printStackTrace();
        }

        return buffer.toString();
    }

    public String post (String json, String urlString) {
        try {
            URL url = new URL(urlString);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestProperty("Content-type", "application/json");
            connection.setRequestProperty("Accept", "application/json");

            //Escrevendo na saida padrão
            connection.setDoOutput(true); //queremos fazer um post

            PrintStream output = new PrintStream(connection.getOutputStream());
            output.println(json);

            connection.connect();

            this.resposta = converterInputStreamToString(connection.getInputStream());

            //codigo do response.
            int responseCode = connection.getResponseCode();

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return this.resposta;
    }

}
