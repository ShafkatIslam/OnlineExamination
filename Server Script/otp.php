<?php

if($_SERVER['REQUEST_METHOD']=='POST'){
//Getting values
$email = $_POST['Email'];
$otp = $_POST['Otp'];

//importing init.php script
require_once('init.php');

//creating sql $sql_query

$sql = "SELECT * FROM teacher WHERE email = '$email' AND value= '$otp'"; //check the encrypted password with user login password
//$sql = "SELECT a.M_Cell, a.M_Pass, b.BF_Cell, b.B_Pass FROM mother a, baby b WHERE (M_Cell = '$usercell' AND M_Pass= '$password') OR (BF_Cell = '$usercell' AND B_Pass= '$password')" ;
//executing $sql_query
$result = mysqli_query($con,$sql);
$count = mysqli_num_rows($result);
//$result1 = mysqli_query($con,$sql1);
//$count1 = mysqli_num_rows($result1);

//fetching result
$check = mysqli_fetch_array($result);
//$check1 = mysqli_fetch_array($result1);
//echo $sql."|";
//echo $check;

if(isset($check)){

//displaying Successful
echo "success";

}
/*else if(isset($check1)){

//displaying Successful
echo "success1";

}*/
else {
  //displaying failure
  echo "failure";
  //echo mysqli_error($con);
  mysqli_close($con);
}
}

?>
