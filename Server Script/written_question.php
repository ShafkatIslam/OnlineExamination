<?php

 if($_SERVER['REQUEST_METHOD']=='GET'){

 $dept= $_GET['Dept'];

 require_once('init.php');

 //$sql = "SELECT * FROM student WHERE cell= '".$get_cell."'";
 $sql = "SELECT question_papers.question_paper_id, departments.short_name, question_papers.exam_end, question_papers.exam_date, question_papers.status
         FROM question_papers
         INNER JOIN departments ON question_papers.department_id=departments.id WHERE departments.short_name= '".$dept."'";

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
		                "WQuestion"=>$res['question_paper_id'],
		                "Dept"=>$res['short_name'],
		                "Date"=>$res['exam_date'],
		                "End_time"=>$res['exam_end']
		                //"Written_Status"=>$res['status']
		               
		));
	}

 echo json_encode(array("result"=>$result));

 mysqli_close($con);

 }
 ?>