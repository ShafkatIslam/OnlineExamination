<?php

if($_SERVER['REQUEST_METHOD']=='POST'){
//Getting values

$Mid = $_POST['Mid'];
$Marks = $_POST['Marks'];
$Course = $_POST['Names'];
$Verify = $_POST['MVerify'];
$Question_id = $_POST['id'];


require_once('init.php');

	//creating sql $sql_query
$sql = "insert into marks(mcq_marks,mid,course,mcq_verify,question_id) values('$Marks','$Mid','$Course','$Verify','$Question_id')";

$result = mysqli_query($con,"SELECT mid,question_id FROM marks where mid = '$Mid' and question_id ='$Question_id'");
$num_rows = mysqli_num_rows($result);

if($num_rows>0)
{
  echo "exists";
}
else
{
if(mysqli_query($con,$sql))
  {
    echo "success";
  }
  else {
    echo mysqli_error($con);
  }
  mysqli_close($con);
}

}
  ?>
