package org.example;

public class OkResponse {
    private boolean ok;
    private Result result;

    public OkResponse() {
    }

    public OkResponse(boolean ok, Result result) {
        this.ok = ok;
        this.result = result;
    }

    public boolean isOk() {
        return ok;
    }

    public void setOk(boolean ok) {
        this.ok = ok;
    }

    public Result getResult() {
        return result;
    }

    public void setResult(Result result) {
        this.result = result;
    }


}

class Result {
    private int message_id;
    private From from;
    private Chat chat;
    private long date;
    private String text;
    private SomeClass[] entities;

    public Result() {
    }

    public Result(int message_id, From from, Chat chat, long date, String text, SomeClass[] entities) {
        this.message_id = message_id;
        this.from = from;
        this.chat = chat;
        this.date = date;
        this.text = text;
        this.entities = entities;
    }

    public int getMessage_id() {
        return message_id;
    }

    public void setMessage_id(int message_id) {
        this.message_id = message_id;
    }

    public From getFrom() {
        return from;
    }

    public void setFrom(From from) {
        this.from = from;
    }

    public Chat getChat() {
        return chat;
    }

    public void setChat(Chat chat) {
        this.chat = chat;
    }

    public long getDate() {
        return date;
    }

    public void setDate(long date) {
        this.date = date;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public SomeClass[] getEntities() {
        return entities;
    }

    public void setEntities(SomeClass[] entities) {
        this.entities = entities;
    }
}

class From {
    private long id;
    private boolean is_bot;
    private String first_name;
    private String username;

    public From() {
    }

    public From(long id, boolean is_bot, String first_name, String username) {
        this.id = id;
        this.is_bot = is_bot;
        this.first_name = first_name;
        this.username = username;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public boolean isIs_bot() {
        return is_bot;
    }

    public void setIs_bot(boolean is_bot) {
        this.is_bot = is_bot;
    }

    public String getFirst_name() {
        return first_name;
    }

    public void setFirst_name(String first_name) {
        this.first_name = first_name;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}

class Chat {
    private long id;
    private String first_name;
    private String username;
    private String type;

    public Chat() {
    }

    public Chat(long id, String first_name, String username, String type) {
        this.id = id;
        this.first_name = first_name;
        this.username = username;
        this.type = type;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getFirst_name() {
        return first_name;
    }

    public void setFirst_name(String first_name) {
        this.first_name = first_name;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}

class SomeClass {
    private int offset;
    private int length;
    private String type;

    public SomeClass() {
    }

    public SomeClass(int offset, int length, String type) {
        this.offset = offset;
        this.length = length;
        this.type = type;
    }

    public int getOffset() {
        return offset;
    }

    public void setOffset(int offset) {
        this.offset = offset;
    }

    public int getLength() {
        return length;
    }

    public void setLength(int length) {
        this.length = length;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
