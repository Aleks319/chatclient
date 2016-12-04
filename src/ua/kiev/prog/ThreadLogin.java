package ua.kiev.prog;

import java.util.Scanner;

public class ThreadLogin extends Thread{
    private boolean success;
    private String login;

    public ThreadLogin() {
        success = true;
    }

    @Override
    public void run() {
        Scanner scanner = new Scanner(System.in);

            String req = "/get?login=";

            System.out.println("Enter your login: ");
            login = scanner.nextLine();

            System.out.println("Enter your password: ");
            String password = scanner.nextLine();

            while (true) {
                String strBuf = Main.getAnswer(req + login + "&password=" + password);
                System.out.println(strBuf);
                if (strBuf.startsWith("Wrong pass")) {
                    req = "/get?login=";
                    System.out.println("Enter your password again or enter \"#exit\" to disconnect: ");
                    password = scanner.nextLine();
                    if (password.equals("#exit")) {
                        success = false;
                        break;
                    } else {
                        continue;
                    }
                } else if (strBuf.startsWith("This login is absent.")) {
                    if (scanner.nextLine().equals("Y")) {
                        System.out.println("Enter your password: ");
                        password = scanner.nextLine();
                        req = "/new?login=";
                        continue;
                    } else {
                        success = false;
                        break;
                    }

                } else {
                    success = true;
                    break;
                }
            }

    }

    public boolean isSuccess() {
        return success;
    }

    public String getLogin() {
        return login;
    }
}
