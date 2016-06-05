<?php 

   require("Pursuit.class.php");
   require("Follower.class.php");
	
   $pursuit  = new Pursuit();
   $follower  = new Follower();
   $response = array();
   $once = false;

   if (isset($_POST["user"]) && isset($_POST["pursuit"]) && isset($_POST["password"])) {

   	$user = filter_var($_POST["user"],FILTER_SANITIZE_FULL_SPECIAL_CHARS);
	$pursuitName = filter_var($_POST["pursuit"],FILTER_SANITIZE_FULL_SPECIAL_CHARS);
	$mdp = filter_var($_POST["password"],FILTER_SANITIZE_FULL_SPECIAL_CHARS);

	if(!empty($user) && !empty($pursuitName) && !empty($mdp)){

               $pursuit->nom_poursuite = $pursuitName;
               $found_pursuit = $pursuit->Search();

               if(!empty($found_pursuit)){

                  foreach($found_pursuit as $o_pursuit){

                     $pursuit->id = $o_pursuit["id"];

                     if (password_verify($mdp, $o_pursuit["password"])) {

                              $once = true;
                              
                              $salt = openssl_random_pseudo_bytes(32);
                              $cookie = hash('sha256', $user.$pursuitName.$o_pursuit["password"].$salt);

                              $follower->user = $user;
                              $follower->nom_poursuite  = $pursuitName;
                              $follower->longitude = "1.0";
                              $follower->latitude = "1.0";
                              $follower->cookie = $cookie;
                              $follower->id_creator = $o_pursuit["id"];

                              $creation = $follower->Create();

                              $response["statut"] = array("succes"=>"true","cookie"=>$cookie);

                              header('Content-Type: application/json;charset=utf-8');
                              echo json_encode($response, JSON_FORCE_OBJECT | JSON_PRETTY_PRINT);

                     } 
                  }

                  if($once === false){
                        $response["statut"] = array("succes"=>"false", "error"=>"sql password not found");
                        header('Content-Type: application/json;charset=utf-8');
                        echo json_encode($response, JSON_FORCE_OBJECT | JSON_PRETTY_PRINT); 
                  }
               }
               else{
                     $response["statut"] = array("succes"=>"false", "error"=>"sql search not found");
                     header('Content-Type: application/json;charset=utf-8');
                     echo json_encode($response, JSON_FORCE_OBJECT | JSON_PRETTY_PRINT);
               }

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