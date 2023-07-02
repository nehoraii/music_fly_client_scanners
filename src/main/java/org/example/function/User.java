package org.example.function;

import com.google.gson.Gson;
import org.example.connection.ServerFunction;
import org.example.enums.ErrorsEnum;
import org.example.enums.SendStatusEnum;
import org.example.vo.UserVo;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Scanner;

public class User {
    private ServerFunction server=new ServerFunction();
    private static long userId;
    private String addPath="http://localhost:8081/User/save";
    private String getPath="http://localhost:8081/User/getUser";
    private ErrorsEnum creatUser(String name,String secName,String email,String phone,String userName,String birthDay){
        UserVo userVo=new UserVo();
        userVo.setUserName(userName);
        userVo.setPhone(phone);
        userVo.setEmail(email);
        userVo.setName(name);
        userVo.setSecName(secName);
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date date;
        try {
            date = dateFormat.parse(birthDay);
        } catch (ParseException e) {
            return ErrorsEnum.DATE_NOT_GOOD;
        }
        userVo.setBirthDay(date);
        ErrorsEnum e;
        e=server.connect(addPath, SendStatusEnum.POST,userVo);
        return e;
    }
    public boolean userFront(){
        Scanner scanner=new Scanner(System.in);
        String name,secName,phone,email,birthDay,userName;
        System.out.println("Enter your name: ");
        name=scanner.nextLine();
        System.out.println("Enter your second name: ");
        secName=scanner.nextLine();
        System.out.println("Enter your phone number: ");
        phone=scanner.nextLine();
        System.out.println("Enter your email: ");
        email=scanner.nextLine();
        System.out.println("Enter your birthday: ");
        birthDay=scanner.nextLine();
        System.out.println("Enter your user name: ");
        userName=scanner.nextLine();
        ErrorsEnum e;
        e=creatUser(name,secName,email,phone,userName,birthDay);
        if (e!=ErrorsEnum.GOOD){
            System.out.println(e);
            return false;
        }
        return true;
    }
    public UserVo getUserByUserName(String userName){
        UserVo userVo=new UserVo();
        userVo.setUserName(userName);
        server.connect(getPath,SendStatusEnum.POST,userVo);
        String answer;
        answer=server.getMassageHead();
        Gson gson=new Gson();
        userVo=gson.fromJson(answer,UserVo.class);
        userId=userVo.getId();
        return userVo;
    }
    public static long getUserId(){
        return userId;
    }
}
