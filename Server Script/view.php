<?php

 if($_SERVER['REQUEST_METHOD']=='GET'){

 $get_cell= $_GET['Cell'];

 require_once('init.php');

 $sql = "SELECT * FROM student WHERE cell= '".$get_cell."'";

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
		                "id"=>$res['id'],
		                "Name"=>$res['name'],
		            	"Email"=>$res['email'],
		            	"Cell"=>$res['cell'],
                  "Mid"=>$res['mid'],
                  "Dept"=>$res['dept'],
                  "Images"=>$res['path']

		));
	}

 echo json_encode(array("result"=>$result));

 mysqli_close($con);

 }
 ?>