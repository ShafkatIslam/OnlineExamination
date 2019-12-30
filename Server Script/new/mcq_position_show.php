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
 $sql = "SELECT rank, mcq_marks, course, mid FROM ( SELECT @rownum := @rownum + 1 AS rank, mcq_marks, mid, course, question_id FROM marks WHERE question_id = '".$Question_Paper_Id."' ORDER BY mcq_marks DESC) as result WHERE mid='".$mid."' and course = '".$Course_Name."'";

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
        	$position = $res[0]

		//Pushing msg and date in the blank array created
		array_push($result,array(
		                "Course_Name"=>$res['course'],
		            	"Marks"=>$res['mcq_marks'],
		            	"Rank"=>$res['rank']

		));
	}

 echo json_encode(array("result"=>$result));

 mysqli_close($con);

 }
 ?>
