package ua.kiev.prog;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

public class Main {
	public static void main(String[] args) {
		Scanner scanner = new Scanner(System.in);
		String login = null;
		try {
			ThreadLogin tl = new ThreadLogin();
			tl.start();
			tl.join();
			if(tl.isSuccess()) {
				System.out.println("*************************************************************************************************");
				System.out.println("*************************************************************************************************");
				System.out.println("CHAT GUIDE:");
				System.out.println("Enter \"#private\" to write private massage.");
				System.out.println("Enter \"#chatroom\" to write massage in exist chatroom, or to create new chatroom and write massage to it.");
				System.out.println("Enter \"#getlistusers\" to get list of all registered users.");
				System.out.println("Enter \"#checkstatus\" to check current status of selected user.");
				System.out.println("Enter \"#exit\" to disconnect and leave chat.");
				System.out.println("Have a nice chat!");
				System.out.println("*************************************************************************************************");
				System.out.println("*************************************************************************************************");
				System.out.println();
				login = tl.getLogin();
				Thread th = new Thread(new GetThread(login));
				th.setDaemon(true);
				th.start();

				System.out.println("Enter your message: ");

				while (true) {
					String to = "";
					String chatroom = "";
					String text = scanner.nextLine();
					String resp = "";
					if (text.equals("#private")) {
						System.out.println("To whom: ");
						to = scanner.nextLine();
						resp = getAnswer("/info?login=" + to + "&type=isexist");
						System.out.println(resp);
						if (to.isEmpty()) {
							break;
						} else if (resp.startsWith("Such user not registered!")) {
							continue;
						} else {
							System.out.println("Your message to " + to + ":");
							text = scanner.nextLine();
						}
					} else if (text.equals("#chatroom")) {
						System.out.println("Chatroom: ");
						to = chatroom = scanner.nextLine();
						if (to.isEmpty()) {
							break;
						}
						resp = getAnswer("/addchat?login=" + login + "&chatroom=" + chatroom + "&new=0");
						if(resp.startsWith("Chatroom \"" + chatroom + "\" is absent")) {
							System.out.println(resp);
							text = scanner.nextLine();
							if(text.equals("Y")) {
								resp = getAnswer("/addchat?login=" + login + "&chatroom=" + chatroom + "&new=1");
								System.out.println(resp);
								System.out.println("Your message in chatroom " + to + ":");
								text = scanner.nextLine();
							} else {
								continue;
							}
						} else if (resp.startsWith("ok")) {
							System.out.println("Your message in chatroom " + to + ":");
							text = scanner.nextLine();
						}

					} else if (text.isEmpty()) {
						break;
					} else if (text.equals("#getlistusers")) {
						resp = getAnswer("/info?login=" + to + "&type=list");
						System.out.println(resp);
						continue;
					} else if (text.equals("#checkstatus")) {
						System.out.println("For whom: ");
						to = scanner.nextLine();
						resp = getAnswer("/info?login=" + to + "&type=isexist");
						if(resp.startsWith("Such user not registered")) {
							System.out.println(resp);
							continue;
						} else {
							resp = getAnswer("/info?login=" + to + "&type=user");
							System.out.println(resp);
							continue;
						}
					} else if (text.equals("#exit")) {
						break;
					}

					Message m = new Message(login, to, text, chatroom);
					int res = m.send(Utils.getURL() + "/add");


					if (res != 200) { // 200 OK
						System.out.println("HTTP error occured: " + res);
						return;
					}
				}

			}
		} catch (IOException ex) {
			ex.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		} finally {
			if(login!=null) {
				getAnswer("/info?login=" + login + "&type=changestatus");
			}
			scanner.close();
		}
	}

	public static String getAnswer(String get) {
		URL url = null;
		HttpURLConnection http = null;
		InputStream is = null;
		String result = null;
		try {
			url = new URL(Utils.getURL() + get);
			http = (HttpURLConnection) url.openConnection();
			is = http.getInputStream();
			byte[] buf = new byte[10000];
			is.read(buf);
			result = new String(buf, StandardCharsets.UTF_8);
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			http.disconnect();
		}
		return result;
	}
}
