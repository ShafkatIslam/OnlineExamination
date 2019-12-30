<?php

 if($_SERVER['REQUEST_METHOD']=='GET'){

 $mid= $_GET['Mid'];
 //$get_text= $_GET['text'];

 require_once('init.php');

 #$sql = "SELECT * FROM vaccine  WHERE cell = '$usercell' and ndate >= NOW() and ndate < NOW() + INTERVAL 24 MONTH";
 $sql = "SELECT * FROM marks WHERE mid = '".$mid."'";

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
		                "Course_Name"=>$res['course'],
		            	"Marks"=>$res['mcq_marks']

		));
	}

 echo json_encode(array("result"=>$result));

 mysqli_close($con);

 }
 ?>
