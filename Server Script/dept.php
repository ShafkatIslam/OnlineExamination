<?php

 if($_SERVER['REQUEST_METHOD']=='GET'){

 require_once('init.php');

 $sql = "SELECT * FROM departments";

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
		                "Dept"=>$res['short_name']

		));
	}

 echo json_encode(array("result"=>$result));

 mysqli_close($con);

 }
 ?>
