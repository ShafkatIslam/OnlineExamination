<?php

if($_SERVER['REQUEST_METHOD']=='POST'){
 //Getting values



 $getid =  $_POST['id'];
 $getmid =  $_POST['Mid'];
 $getname =  $_POST['Name'];
 $getcell =  $_POST['Cell'];
 $getemail =  $_POST['Email'];
 $Dept = $_POST['Dept'];


 //importing dbConnect.php script
 require_once('init.php');


   $sql="UPDATE student SET name ='$getname' , cell ='$getcell', email = '$getemail',mid = '$getmid',dept ='$Dept' WHERE id = '$getid' ";




if(mysqli_query($con,$sql))

{

     echo "success";

}

 else
 {
     echo "failure";
 }

 mysqli_close($con);


}


?>
