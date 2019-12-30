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
$Otp = $_POST['Otp'];


//importing init.php script
require_once('init.php');

//if($retval==true)

$sql = "SELECT * FROM student WHERE cell = '$Cell' AND email= '$Email' AND otp= '$Otp'"; //check the encrypted 

$result = mysqli_query($con,$sql);
$count = mysqli_num_rows($result);

$check = mysqli_fetch_array($result);
//$result1 = mysqli_query($con,"SELECT email FROM student where email = '$Email'");
if(isset($check)){

//displaying Successful
	echo "success";

}

else {
  //displaying failure
  echo "failure";
  //echo mysqli_error($con);
  mysqli_close($con);
}
}
  ?>
