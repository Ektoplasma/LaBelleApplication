<?php 

   require("Pursuit.class.php");
	
   $pursuit  = new Pursuit();
   $response = array();

   if (isset($_POST["cookie"])) {

	$cookie = filter_var($_POST["cookie"],FILTER_SANITIZE_FULL_SPECIAL_CHARS); 

	if(!empty($_POST["cookie"])){

		$pursuit->cookie = $cookie;
		$found_pursuit = $pursuit->Search();

		if(!empty($found_pursuit)){

			foreach($found_pursuit as $o_pursuit){

	   			 $pursuit->id = $o_pursuit["id"];
	   			 $deleted = $pursuit->Delete();

				if ($deleted !== null) {
					$response["statut"] = array("succes"=>"true");

					header('Content-Type: application/json;charset=utf-8');
					echo json_encode($response, JSON_FORCE_OBJECT | JSON_PRETTY_PRINT);
				} else {
					$response["statut"] = array("succes"=>"false","error"=>"sql delete error");
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