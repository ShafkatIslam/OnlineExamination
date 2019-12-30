<?php

 if($_SERVER['REQUEST_METHOD']=='GET'){
 
 $department = $_GET['Dept'];

 require_once('init.php');

 $sql = "SELECT question_papers.*,departments.short_name
         FROM question_papers
		 INNER JOIN departments ON question_papers.department_id=departments.id
		 WHERE departments.short_name = '".$department."'";

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
		                "Written_Status"=>$res['written_status']

		));
	}

 echo json_encode(array("result"=>$result));

 mysqli_close($con);

 }
 ?>
