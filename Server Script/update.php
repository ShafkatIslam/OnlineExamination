<?php

if($_SERVER['REQUEST_METHOD']=='POST'){
 //Getting values



 $getid =  $_POST['id'];
 $getmid =  $_POST['Mid'];
 $getname =  $_POST['Name'];
 $getcell =  $_POST['Cell'];
 $getemail =  $_POST['Email'];
 $Dept = $_POST['Dept'];
 $Image_Path = $_POST['Images'];


 //importing dbConnect.php script
 require_once('init.php');
 
 $upload_path= "uploads/$getcell.jpg";
 $actualpath = "$upload_path";


   $sql="UPDATE student SET name ='$getname' , cell ='$getcell', email = '$getemail',mid = '$getmid',dept ='$Dept',path='$actualpath' WHERE id = '$getid' ";




if(mysqli_query($con,$sql))

{

     file_put_contents($upload_path,base64_decode($Image_Path));
     echo "success";

}

 else
 {
     echo "failure";
 }

 mysqli_close($con);


}


?>
