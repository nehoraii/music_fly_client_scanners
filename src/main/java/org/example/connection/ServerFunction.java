package org.example.connection;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.example.enums.ErrorsEnum;
import org.example.enums.SendStatusEnum;
import org.example.vo.SongsVO;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

public class ServerFunction {
     private String massageHead;
    public  ErrorsEnum connect(String path, SendStatusEnum status, Object user){
        Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSX").create();
        String json=gson.toJson(user);
        HttpRequest httpRequest;
        String massage;
        try {
            switch (status){
                case POST:
                    httpRequest = HttpRequest.newBuilder()
                            .uri(new URI(path))
                            .header("Content-Type", "application/json")
                            .POST(HttpRequest.BodyPublishers.ofString(json, StandardCharsets.UTF_8)).build();
                    break;
                case PUT:
                    httpRequest = HttpRequest.newBuilder()
                            .uri(new URI(path))
                            .header("Content-Type", "application/json")
                            .PUT(HttpRequest.BodyPublishers.ofString(json, StandardCharsets.UTF_8)).build();
                    break;
                case DELETE:
                    httpRequest = HttpRequest.newBuilder()
                            .uri(new URI(path))
                            .header("Content-Type", "application/json")
                            .DELETE().build();
                    break;
                case GET:
                    httpRequest = HttpRequest.newBuilder()
                            .uri(new URI(path))
                            .header("Content-Type", "application/json")
                            .GET().build();
                    break;
                default:
                    return ErrorsEnum.WRONG_CASE;
            }

            HttpClient httpClient = HttpClient.newHttpClient();
            HttpResponse<String> postResponse = httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString());
            massage = postResponse.body();
            massageHead=massage;
            return ErrorsEnum.GOOD;
        } catch (MalformedURLException e) {
            System.out.println(e);
            return ErrorsEnum.URLError;
        } catch (IOException e) {
            System.out.println(e);
            return ErrorsEnum.OpenConnectionError;
        }catch (Exception e){
            System.out.println(e);
            return ErrorsEnum.ElseError;
        }

    }
    public String getMassageHead(){
        return massageHead;
    }

}
