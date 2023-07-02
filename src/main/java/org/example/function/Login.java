package org.example.function;

import org.example.connection.ServerFunction;
import org.example.enums.ErrorsEnum;
import org.example.enums.SendStatusEnum;
import org.example.vo.LoginVo;
import org.example.vo.UserVo;

import java.util.Scanner;

public class Login {
    private String addPath="http://localhost:8081/Login/save";
    private ServerFunction server=new ServerFunction();
    private ErrorsEnum addLogin(String userName,String password){
        User userFunction=new User();
        UserVo userVo;
        userVo=userFunction.getUserByUserName(userName);
        if(userVo==null){
            return ErrorsEnum.USER_NOT_FOUND;
        }
        LoginVo loginVo=new LoginVo();
        loginVo.setPass(password);
        loginVo.setUserId(userVo.getId());
        ErrorsEnum e;
        e=server.connect(addPath, SendStatusEnum.POST,loginVo);
        return e;
    }
    public boolean loginFront(){
        Scanner scanner=new Scanner(System.in);
        String userName,password;
        System.out.println("Enter your user name");
        userName=scanner.nextLine();
        System.out.println("Enter your password");
        password=scanner.nextLine();
        ErrorsEnum e;
        e=addLogin(userName,password);
        if (e!=ErrorsEnum.GOOD){
            System.out.println(e);
            return false;
        }
        return true;
    }
}
