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
$Password = $_POST['Pass'];

$Password = md5($Password); //encrypted password for security

//importing init.php script
require_once('init.php');

$sql = "UPDATE student SET password = '$Password' where cell='$Cell' and email = '$Email'";

  if(mysqli_query($con,$sql))
  {
    echo "success";
  }
  else {
    echo mysqli_error($con);
  }
  mysqli_close($con);

	//creating sql $sql_query
//$sql = "insert into teacher(name,email,cell,mid,gender,password,path) values('$Name','$Email','$Cell','$Mid','$Gender','$Pass','$actualpath')";



}
  ?>
