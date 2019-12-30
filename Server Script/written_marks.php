<?php

 if($_SERVER['REQUEST_METHOD']=='GET'){

 $mid= $_GET['Mid'];
 //$get_text= $_GET['text'];

 require_once('init.php');

 #$sql = "SELECT * FROM vaccine  WHERE cell = '$usercell' and ndate >= NOW() and ndate < NOW() + INTERVAL 24 MONTH";
 $sql = "SELECT written_exam.*,question_papers.course_id,courses.name
         FROM written_exam
		 INNER JOIN question_papers ON question_papers.question_paper_id=written_exam.question_id
		 INNER JOIN courses ON courses.id=question_papers.course_id
		 WHERE written_exam.student_id = '".$mid."'";

/*
if(strlen($get_text)>0)
{
  $sql = "SELECT * FROM contacts WHERE name LIKE '%$get_text%' AND user_cell='".$usercell."' ORDER BY name ASC";
}
*/
 $r = mysqli_query($con,$sql);

// $res = mysqli_fetch_array($r);

 $result = array();


while($res = mysqli_fetch_array($r))
        {

		//Pushing msg and date in the blank array created
		array_push($result,array(
		                "Course_Name"=>$res['name'],
		            	"WMarks"=>$res['mark']

		));
	}

 echo json_encode(array("result"=>$result));

 mysqli_close($con);

 }
 ?>
