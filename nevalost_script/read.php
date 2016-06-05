<?php 

   require("Pursuit.class.php");
   require("Follower.class.php");

   $pursuit  = new Pursuit();
   $follower  = new Follower();
   $response = array();

   if (isset($_POST["cookie"])) {

	$cookie = filter_var($_POST["cookie"],FILTER_SANITIZE_FULL_SPECIAL_CHARS); 

	if(!empty($_POST["cookie"])){

		$follower->cookie = $cookie;
		$found_follower = $follower->Search();

		if(!empty($found_follower)){

			foreach($found_follower as $o_follower){

	   			 $follower->id = $o_follower["id"];

	   			 $pursuit->id = $o_follower["id_creator"];
	   			 $pursuit->find();

				if ($pursuit !== null ) {
					$lon = $pursuit->longitude;
				 	$lat = $pursuit->latitude;

					$response["statut"] = array("succes"=>"true", "lon"=>$lon, "lat"=>$lat);

					header('Content-Type: application/json;charset=utf-8');
					echo json_encode($response, JSON_FORCE_OBJECT | JSON_PRETTY_PRINT);
				} else {
					$response["statut"] = array("succes"=>"false","error"=>"sql read error");
					header('Content-Type: application/json;charset=utf-8');
					echo json_encode($response, JSON_FORCE_OBJECT | JSON_PRETTY_PRINT);
				}
			}

		}
		else{
			$response["statut"] = array("succes"=>"false","error"=>"sql search error");
			header('Content-Type: application/json;charset=utf-8');
			echo json_encode($response, JSON_FORCE_OBJECT | JSON_PRETTY_PRINT);	
		}

	}
	else {
		$response["statut"] = array("succes"=>"false","error"=>"empty error");
		header('Content-Type: application/json;charset=utf-8');
		echo json_encode($response, JSON_FORCE_OBJECT | JSON_PRETTY_PRINT);
	}

   } else {
	$response["statut"] = array("succes"=>"false","error"=>"isset error");
	header('Content-Type: application/json;charset=utf-8');
	echo json_encode($response, JSON_FORCE_OBJECT | JSON_PRETTY_PRINT);
   }