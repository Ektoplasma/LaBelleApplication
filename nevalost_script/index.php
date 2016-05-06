<?php 

   require("Pursuit.class.php");
   /*require("Reponse.class.php");*/
	
   $pursuit  = new Pursuit();
   $response = array();

   if (isset($_POST["user"]) && isset($_POST["pursuit"]) && isset($_POST["password"])) {

   	$user = filter_var($_POST["user"],FILTER_SANITIZE_FULL_SPECIAL_CHARS);
	$pursuitName = filter_var($_POST["pursuit"],FILTER_SANITIZE_FULL_SPECIAL_CHARS);
	$mdp = filter_var($_POST["password"],FILTER_SANITIZE_FULL_SPECIAL_CHARS);

	if(!empty($user) && !empty($pursuitName) && !empty($mdp)){
		$pursuit->user = $user;
		$pursuit->nom_poursuite  = $pursuitName;
		$pursuit->password = password_hash($password, PASSWORD_DEFAULT);
		$pursuit->longitude = "47.0";
		$pursuit->latitude = "4.0";
		$creation = $pursuit->Create(); 

		$response["statut"] = array("succes"=>"true");
		header('Content-Type: application/json;charset=utf-8');
		echo json_encode($response, JSON_FORCE_OBJECT | JSON_PRETTY_PRINT);
	}
	else {
		$response["statut"] = array("succes"=>"false");
		header('Content-Type: application/json;charset=utf-8');
		echo json_encode($response, JSON_FORCE_OBJECT | JSON_PRETTY_PRINT);
	}

   } else {
	$response["statut"] = array("succes"=>"false");
	header('Content-Type: application/json;charset=utf-8');
	echo json_encode($response, JSON_FORCE_OBJECT | JSON_PRETTY_PRINT);
   }
