package com.example.professt.onlineexamination;

public class Information {

    String mid,dept,question_id,uid,course_id,no_of_questions,department,mcq_status,written_status;

    private static Information information= new Information();

    public Information() {
    }

    public static Information getInstance( ) {
        return information;
    }

    public String getMid() {
        return mid;
    }

    public void setMid(String mid) {
        this.mid = mid;
    }

    public String getDept() {
        return dept;
    }

    public void setDept(String dept) {
        this.dept = dept;
    }

    public String getQuestion_id() {
        return question_id;
    }

    public void setQuestion_id(String question_id) {
        this.question_id = question_id;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getCourse_id() {
        return course_id;
    }

    public void setCourse_id(String course_id) {
        this.course_id = course_id;
    }

    public String getNo_of_questions() {
        return no_of_questions;
    }

    public void setNo_of_questions(String no_of_questions) {
        this.no_of_questions = no_of_questions;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getMcq_status() {
        return mcq_status;
    }

    public void setMcq_status(String mcq_status) {
        this.mcq_status = mcq_status;
    }

    public String getWritten_status() {
        return written_status;
    }

    public void setWritten_status(String written_status) {
        this.written_status = written_status;
    }
}
