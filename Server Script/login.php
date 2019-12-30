<?php

if($_SERVER['REQUEST_METHOD']=='POST'){
//Getting values
$usercell = $_POST['Cell'];
$password1 = $_POST['Pass'];
$status = 'No';
$status1 = 'Yes';

$password = md5($password1); //encrypted password for security

//importing init.php script
require_once('init.php');

//creating sql $sql_query

$sql = "SELECT * FROM teacher WHERE cell = '$usercell' AND password= '$password' AND status= '$status1'"; //check the encrypted password with user login password
//$sql = "SELECT a.M_Cell, a.M_Pass, b.BF_Cell, b.B_Pass FROM mother a, baby b WHERE (M_Cell = '$usercell' AND M_Pass= '$password') OR (BF_Cell = '$usercell' AND B_Pass= '$password')" ;
$sql1 = "SELECT * FROM student WHERE cell = '$usercell' AND password= '$password' AND status= '$status1'";

$sql2 = "SELECT * FROM teacher WHERE cell = '$usercell' AND password= '$password' AND status= '$status'"; //check the encrypted password with user login password

$sql3 = "SELECT * FROM student WHERE cell = '$usercell' AND password= '$password' AND status= '$status'"; //check the encrypted password with user login password
//executing $sql_query
$result = mysqli_query($con,$sql);
$count = mysqli_num_rows($result);
$result1 = mysqli_query($con,$sql1);
$count1 = mysqli_num_rows($result1);
$result2 = mysqli_query($con,$sql2);
$count2 = mysqli_num_rows($result2);
$result3 = mysqli_query($con,$sql3);
$count3 = mysqli_num_rows($result3);

//fetching result
$check = mysqli_fetch_array($result);
$check1 = mysqli_fetch_array($result1);
$check2 = mysqli_fetch_array($result2);
$check3 = mysqli_fetch_array($result3);
//echo $sql."|";
//echo $check;

if(isset($check)){

//displaying Successful
echo "success";

}
else if(isset($check1)){

//displaying Successful
echo "success1";

}
else if(isset($check2)){

//displaying Successful
echo "fail";

}
else if(isset($check3)){

//displaying Successful
echo "fail1";

}
else {
  //displaying failure
  echo "failure";
  //echo mysqli_error($con);
  mysqli_close($con);
}
}

?>