package ua.kiev.prog;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Message {
	private String msgStatus;
	private Date date = new Date();
	private String from;
	private String to;
	private String text;
	private String chatroom;

	public Message(String from, String to, String text, String chatroom) {
		this.from = from;
		this.to = to;
		this.text = text;
		this.chatroom = chatroom;
		if((!to.equals("")) && chatroom.equals("")) {
			msgStatus = "PRIVATE: ";
		} else if(!chatroom.equals("")) {
			msgStatus = "CHATROOM (" + chatroom + "): ";
			} else {
			msgStatus = "";
		}
	}

	public String toJSON() {
		Gson gson = new GsonBuilder().create();
		return gson.toJson(this);
	}
	
	public static Message fromJSON(String s) {
		Gson gson = new GsonBuilder().create();
		return gson.fromJson(s, Message.class);
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;

		Message message = (Message) o;

		if (!msgStatus.equals(message.msgStatus)) return false;
		if (!date.equals(message.date)) return false;
		if (!from.equals(message.from)) return false;
		if (!to.equals(message.to)) return false;
		if (!text.equals(message.text)) return false;
		return chatroom.equals(message.chatroom);

	}

	@Override
	public int hashCode() {
		int result = msgStatus.hashCode();
		result = 31 * result + date.hashCode();
		result = 31 * result + from.hashCode();
		result = 31 * result + to.hashCode();
		result = 31 * result + text.hashCode();
		result = 31 * result + chatroom.hashCode();
		return result;
	}

	@Override
	public String toString() {
		return new StringBuilder().append(msgStatus).append("[").append(new SimpleDateFormat("dd.MM.yyyy HH:mm:ss").format(date))
				.append(", From: ").append(from).append(", To: ").append(to)
				.append("] ").append(text)
                .toString();
	}

	public int send(String url) throws IOException {
		URL obj = new URL(url);
		HttpURLConnection conn = (HttpURLConnection) obj.openConnection();
		
		conn.setRequestMethod("POST");
		conn.setDoOutput(true);
	
		OutputStream os = conn.getOutputStream();
		try {
			String json = toJSON();
			os.write(json.getBytes(StandardCharsets.UTF_8));
			return conn.getResponseCode();
		} finally {
			os.close();
		}
	}
	
	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public String getFrom() {
		return from;
	}

	public void setFrom(String from) {
		this.from = from;
	}

	public String getTo() {
		return to;
	}

	public void setTo(String to) {
		this.to = to;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public String getChatroom() {
		return chatroom;
	}
}
