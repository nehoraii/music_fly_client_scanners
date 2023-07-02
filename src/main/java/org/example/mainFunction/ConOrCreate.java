package org.example.mainFunction;

import org.example.function.Login;
import org.example.function.User;

import java.util.Scanner;

public class ConOrCreate {
    public void ConOrCreate(){
        boolean flag=true;
        String answer;
        Scanner scanner=new Scanner(System.in);
        do {
            System.out.println("Choose either connection or creation: ");
            answer=scanner.nextLine();
            answer=answer.toLowerCase();
            switch (answer){
                case "creation":
                    User user=new User();
                    flag=user.userFront();
                    break;
                case "1":
                    Login login=new Login();
                    flag=login.loginFront();
                    break;
                case "Q":
                    flag=false;
                default:
                    System.out.println("Wrong!!!");
            }
        }while (!flag);
    }
}
