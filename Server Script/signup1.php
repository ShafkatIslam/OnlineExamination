<?php

if($_SERVER['REQUEST_METHOD']=='POST'){
//Getting values
$Name = $_POST['Name'];
$Email = $_POST['Email'];
$Cell = $_POST['Cell'];
$Mid = $_POST['Mid'];
$Dept = $_POST['Dept'];
$Gender = $_POST['Gender'];
$Pass = $_POST['Pass'];
$Image_Path = $_POST['Images'];
$Status = $_POST['Status'];

$Pass = md5($Pass); //encrypted password for security

/*
$a = rand(100,999999);
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

 $upload_path= "uploads/$Cell.jpg";
 $actualpath = "$upload_path";

	//creating sql $sql_query
$sql = "insert into student(name,email,cell,mid,dept,gender,password,path,status) values('$Name','$Email','$Cell','$Mid','$Dept','$Gender','$Pass','$actualpath','$Status')";

$result = mysqli_query($con,"SELECT cell FROM student where cell = '$Cell'");
$result1 = mysqli_query($con,"SELECT mid FROM student where mid = '$Mid'");

$num_rows = mysqli_num_rows($result);
$num_rows1 = mysqli_num_rows($result1);
if($num_rows>0)
{
  echo "exists";
}
else if($num_rows1>0)
{
  echo "existss";
}
else {
  if(mysqli_query($con,$sql))
  {
  	file_put_contents($upload_path,base64_decode($Image_Path));
    echo "success";
  }
  else {
    echo mysqli_error($con);
  }
  mysqli_close($con);
}

}
  ?>
