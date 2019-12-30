package com.example.professt.onlineexamination.Evaluated_Answer_Script;

public class Answer {

    String answerId,mid;

    public Answer(String answerId, String mid) {
        this.answerId = answerId;
        this.mid = mid;
    }

    public String getAnswerId() {
        return answerId;
    }

    public void setAnswerId(String answerId) {
        this.answerId = answerId;
    }

    public String getMid() {
        return mid;
    }

    public void setMid(String mid) {
        this.mid = mid;
    }
}
