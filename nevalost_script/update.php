<?php 

   require("Pursuit.class.php");
	
   $pursuit  = new Pursuit();
   $response = array();

   if (isset($_POST["longitude"]) && isset($_POST["latitude"])  && isset($_POST["cookie"])) {

	$longitude = filter_var($_POST["longitude"],FILTER_SANITIZE_FULL_SPECIAL_CHARS);
	$latitude = filter_var($_POST["latitude"],FILTER_SANITIZE_FULL_SPECIAL_CHARS);
	$cookie = filter_var($_POST["cookie"],FILTER_SANITIZE_FULL_SPECIAL_CHARS); 

	if(!empty($_POST["longitude"]) && !empty($_POST["latitude"])  && !empty($_POST["cookie"])){

		$lon = floatval($longitude);
		$lat = floatval($latitude);

		$pursuit->cookie = $cookie;
		$found_pursuit = $pursuit->Search();

		if(!empty($found_pursuit)){

			foreach($found_pursuit as $o_pursuit){

	   			 $pursuit->id = $o_pursuit["id"];
	   			 $pursuit->longitude = $lon;
	   			 $pursuit->latitude = $lat;

				if ($pursuit->Save() !== null) {
					$response["statut"] = array("succes"=>"true");

					header('Content-Type: application/json;charset=utf-8');
					echo json_encode($response, JSON_FORCE_OBJECT | JSON_PRETTY_PRINT);
				} else {
					$response["statut"] = array("succes"=>"false","error"=>"sql update error");
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
   function d($v, $t = "") 
   {
      echo '<pre>';
      echo '<h1>' . $t. '</h1>';
      var_dump($v);
      echo '</pre>';
   }

?>