<?php

 if($_SERVER['REQUEST_METHOD']=='GET'){
 
 $get_no_of_questions= $_GET['No_of_questions'];
 $c_id= $_GET['cid'];
 $id= $_GET['id'];

 require_once('init.php');

 //$sql = "SELECT * FROM departments";
 $sql = "SELECT mcq.question, mcq.op1, mcq.op2, mcq.op3, mcq.op4, mcq.answer, mcq.teacher_id, courses.name, mcq_informations.no_of_questions , mcq_informations.end_time, mcq_informations.date 
         FROM mcq 
		 INNER JOIN courses ON mcq.course_id=courses.id 
		 INNER JOIN mcq_informations ON courses.id=mcq_informations.course_id 
		 WHERE courses.id = '".$c_id."' AND mcq_informations.id = '".$id."' 
		 ORDER BY RAND() 
		 LIMIT $get_no_of_questions";

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
		                "Question"=>$res['question'],
		                "Op1"=>$res['op1'],
		                "Op2"=>$res['op2'],
		                "Op3"=>$res['op3'],
		                "Op4"=>$res['op4'],
		                "Answer"=>$res['answer'],
		                "TeacherId"=>$res['teacher_id'],
		                "Names"=>$res['name'],
		                "End_time"=>$res['end_time'],
		                "Date"=>$res['date']

		));
	}

 echo json_encode(array("result"=>$result));

 mysqli_close($con);

 }
 ?>
