package com.example.professt.onlineexamination;

public class Key {

    public static final String MAIN_URL = "https://www.tikabarta.com/mocktest";

    public static  final String SIGNUP_URL = MAIN_URL+"/api/signup.php";
    public static  final String SIGNUP_URL1 = MAIN_URL+"/api/signup1.php";
    public static  final String LOGIN_URL = MAIN_URL+"/api/login.php";
    public static  final String VIEW_URL = MAIN_URL+"/api/view.php?Cell=";
    public static  final String VIEW_URL1 = MAIN_URL+"/api/view1.php?Cell=";
    public static  final String UPDATE_URL = MAIN_URL+"/api/update.php";
    public static  final String UPDATE_URL1 = MAIN_URL+"/api/update1.php";
    public static  final String UPDATE_URL2 = MAIN_URL+"/api/update2.php";
    public static  final String UPDATE_URL3 = MAIN_URL+"/api/update3.php";
    public static  final String UPLOAD_URL = MAIN_URL+"/api/upload.php";
    public static  final String LOAD_DEPT = MAIN_URL+"/api/dept.php";
    public static  final String LOAD_QUES = MAIN_URL+"/api/question.php?No_of_questions=";
    public static  final String LOAD_QUES_INFORMATION = MAIN_URL+"/api/question_information.php?Dept=";
    public static  final String LOAD_WRITTEN_INFORMATION = MAIN_URL+"/api/written_information.php?Dept=";
    public static  final String LOAD_WRITTEN_QUES = MAIN_URL+"/api/written_question.php?Dept=";
    public static  final String LOAD_MCQ_MARKS = MAIN_URL+"/api/mcq_marks.php";
    public static  final String DUPLICATE_ANSWER_SCRIPT = MAIN_URL+"/api/duplicate_answe_script.php";
    public static  final String WRITTEN_MARK = MAIN_URL+"/api/written_marks.php?Mid=";
    public static  final String MCQ_MARK = MAIN_URL+"/api/mcq_marks_show.php?Mid=";
    public static  final String WRITTEN_SCRIPT = MAIN_URL+"/api/written_script.php?Mid=";
    public static  final String MCQ_RANK = MAIN_URL+"/api/mcq_position_show.php?Mid=";
    public static  final String WRITTEN_RANK = MAIN_URL+"/api/written_position_show.php?Mid=";
    public static  final String FORGET_PASS = MAIN_URL+"/api/forget_password.php";
    public static  final String OTP_CHECK = MAIN_URL+"/api/otp_check.php";
    public static  final String NEW_PASSWORD = MAIN_URL+"/api/new_password.php";
    public static  final String FORGETS_PASS = MAIN_URL+"/api/forgets_password.php";
    public static  final String OTPS_CHECK = MAIN_URL+"/api/otps_check.php";
    public static  final String NEWS_PASSWORD = MAIN_URL+"/api/news_password.php";

    public static final String KEY_ID = "id";
    public static final String KEY_NAME = "Name";
    public static final String KEY_PASSWORD = "Pass";
    public static final String KEY_EMAIL = "Email";
    public static final String KEY_MID = "Mid";
    public static final String KEY_CELL = "Cell";
    public static final String KEY_DEPT = "Dept";
    public static final String KEY_GNDER = "Gender";
    public static final String KEY_IMAGE = "Images";
    public static final String KEY_STATUS = "Status";
    public static final String KEY_CSTATUS = "CStatus";
    public static final String KEY_OTP = "Otp";
    public static final String KEY_VERIFY = "Verify";

    public static final String KEY_QUESTION = "Question";
    public static final String KEY_WQUESTION = "WQuestion";
    public static final String KEY_OP1 = "Op1";
    public static final String KEY_OP2 = "Op2";
    public static final String KEY_OP3 = "Op3";
    public static final String KEY_OP4 = "Op4";
    public static final String KEY_ANSWER = "Answer";
    public static final String KEY_ANSWER_ID = "AnswerID";
    public static final String KEY_TEACHERID = "TeacherId";
    public static final String KEY_NAMES = "Names";
    public static final String KEY_MCQ_MARKS = "Marks";
    public static final String KEY_MCQ_VERIFY = "MVerify";
    public static final String KEY_COURSE_ID = "Course_id";
    public static final String KEY_NO_OF_QUES = "No_of_questions";
    public static final String KEY_DATE = "Date";
    public static final String KEY_END_TIME = "End_time";
    public static final String KEY_MCQ_STATUS = "Mcq_Status";
    public static final String KEY_WRITTEN_STATUS = "Written_Status";
    public static final String KEY_WRITTEN_MARKS = "WMarks";
    public static final String KEY_COURSE_NAME = "Course_Name";
    public static final String KEY_ANSWER_SCRIPT_ID = "Answer_Script_Id";
    public static final String KEY_QUESTION_PAPER_ID = "Question_Paper_Id";
    public static final String KEY_MCQ_RANK = "Rank";
    public static final String KEY_WRITTEN_RANK = "WRank";

    public static final String SHARED_PREF_NAME = "com.example.professt.onlineexamination.userlogin"; //pcakage name+ id(any name)

    //This would be used to store the cell of current logged in user
    public static final String CELL_SHARED_PREF = "cell";
    public static final String USER_SHARED_PREF = "Username";

    //json array name.We will received data in this array
    public static final String JSON_ARRAY = "result";
}
