<?php

if($_SERVER['REQUEST_METHOD']=='POST'){
//Getting values
/*$Name = $_POST['Name'];
$Email = $_POST['Email'];
$Cell = $_POST['Cell'];
$Mid = $_POST['Mid'];
$Gender = $_POST['Gender'];
$Pass = $_POST['Pass'];
$Image_Path = $_POST['Images']; */


$Email = $_POST['Email'];
$Cell = $_POST['Cell'];


$a = rand(100,999999);
$to = $Email;
$subject = "Your Registration Varification Code";
$message = $a;
$header = "From:nusrumr@gmail.com \r\n";
$header = "Cc:tanzeem.cesc@gmail.com \r\n";
$header .= "MIME-Version: 1.0\r\n";
$header .= "Content-type: text/html\r\n";
         
$retval = mail ($to,$subject,$message,$header);


//importing init.php script
require_once('init.php');

if($retval==true)
{

$sql = "UPDATE teacher SET otp = '$a' where cell='$Cell' and email = '$Email'";

$result = mysqli_query($con,"SELECT cell,email FROM teacher where cell = '$Cell' and email = '$Email'");
//$result1 = mysqli_query($con,"SELECT email FROM student where email = '$Email'");

$num_rows = mysqli_num_rows($result);
//$num_rows1 = mysqli_num_rows($result1);
if($num_rows==0)
{
  echo "exists";
}
/*else if($num_rows1==0)
{
  echo "existss";
}*/
else {
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

	//creating sql $sql_query
//$sql = "insert into teacher(name,email,cell,mid,gender,password,path) values('$Name','$Email','$Cell','$Mid','$Gender','$Pass','$actualpath')";



}
  ?>
