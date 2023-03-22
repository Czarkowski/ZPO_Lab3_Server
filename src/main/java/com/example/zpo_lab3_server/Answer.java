package com.example.zpo_lab3_server;

public class Answer {
    private String nick;
    private String answer;

    public String getNick() {
        return nick;
    }

    public String getAnswer() {
        return answer;
    }

    public Answer(String nick, String answer) {
        this.nick = nick;
        this.answer = answer;
    }
}
