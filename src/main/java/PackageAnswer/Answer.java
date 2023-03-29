package PackageAnswer;

import java.io.Serializable;

public class Answer implements Serializable {

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
