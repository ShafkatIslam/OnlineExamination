<?php

if($_SERVER['REQUEST_METHOD']=='POST'){
//Getting values
$name = $_POST['name'];
$mid = $_POST['Mid'];
$answerId = $_POST['AnswerID'];

//$Pass = md5($Pass); //encrypted password for security

/*$a = rand(100,999999);
$to = $Email;
$subject = "Your Registration Varification Code";
$message = $a;
$header = "From:hackingpro20161@gmail.com \r\n";
$header = "Cc:hackingpro20161@gmail.com \r\n";
$header .= "MIME-Version: 1.0\r\n";
$header .= "Content-type: text/html\r\n";
         
$retval = mail ($to,$subject,$message,$header);
*/

//importing init.php script
require_once('init.php');

	//creating sql $sql_query
//$sql = "insert into teacher(name,email,cell,mid,gender,password,path) values('$Name','$Email','$Cell','$Mid','$Gender','$Pass','$actualpath')";

$result = mysqli_query($con,"SELECT answer_script_id FROM written_exam where answer_script_id = '$answerId'");

$num_rows = mysqli_num_rows($result);
if($num_rows>0)
{
  echo "exists";
}
else {
  
  echo "success";
  mysqli_close($con);
}

}
  ?>
