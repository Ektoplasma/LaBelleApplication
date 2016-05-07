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

		$o_mdp = password_hash($mdp, PASSWORD_DEFAULT);

		$salt = openssl_random_pseudo_bytes(32);
		$cookie = hash('sha256', $user.$pursuitName.$o_mdp.$salt);

		$pursuit->user = $user;
		$pursuit->nom_poursuite  = $pursuitName;
		$pursuit->password = $o_mdp;
		$pursuit->longitude = "47.0";
		$pursuit->latitude = "4.0";
		$pursuit->cookie = $cookie;

		$creation = $pursuit->Create();

		$response["statut"] = array("succes"=>"true","cookie"=>$cookie);

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
