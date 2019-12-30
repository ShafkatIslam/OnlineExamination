<?php

 if($_SERVER['REQUEST_METHOD']=='GET'){

 $mid= $_GET['Mid'];
 $Question_Paper_Id= $_GET['Question_Paper_Id'];
 $Course_Name= $_GET['Course_Name'];
 //$get_text= $_GET['text'];

 require_once('init.php');

 #$sql = "SELECT * FROM vaccine  WHERE cell = '$usercell' and ndate >= NOW() and ndate < NOW() + INTERVAL 24 MONTH";
 #$sql = "SELECT * FROM marks WHERE question_id = '".$Question_Paper_Id."' ORDER BY mcq_marks DESC";
 $sql1 = "SET @rownum := 0";
 #$sql = "SELECT rank, mark, name, student_id FROM ( SELECT @rownum := @rownum + 1 AS rank, mark, student_id, question_id, question_papers.course_id,courses.name FROM marks INNER JOIN question_papers ON question_papers.question_paper_id=written_exam.question_id INNER JOIN courses ON courses.id=question_papers.course_id WHERE question_id = '".$Question_Paper_Id."' ORDER BY mcq_marks DESC) as result WHERE student_id='".$mid."' and name = '".$Course_Name."'";

 $sql = "SELECT rank, mark, name, student_id FROM ( SELECT @rownum := @rownum + 1 AS rank, written_exam.mark, written_exam.student_id, written_exam.question_id, question_papers.course_id,courses.name FROM written_exam INNER JOIN question_papers ON question_papers.question_paper_id=written_exam.question_id INNER JOIN courses ON courses.id=question_papers.course_id WHERE written_exam.question_id = '".$Question_Paper_Id."' ORDER BY written_exam.mark DESC) as result WHERE student_id='".$mid."' and name = '".$Course_Name."'";



/*
if(strlen($get_text)>0)
{
  $sql = "SELECT * FROM contacts WHERE name LIKE '%$get_text%' AND user_cell='".$usercell."' ORDER BY name ASC";
}
*/
 mysqli_query($con,$sql1);
 $r = mysqli_query($con,$sql);

// $res = mysqli_fetch_array($r);

 $result = array();


while($res = mysqli_fetch_array($r))
        {

		//Pushing msg and date in the blank array created
		array_push($result,array(
		                "Course_Name"=>$res['name'],
		            	"WMarks"=>$res['mark'],
		            	"WRank"=>$res[0]['rank']

		));
	}

 echo json_encode(array("result"=>$result));

 mysqli_close($con);

 }
 ?>