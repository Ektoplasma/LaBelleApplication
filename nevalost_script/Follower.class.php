<?php
require_once("easyCRUD.class.php");

class Follower Extends Crud {

  # The table you want to perform the database actions on
  protected $table = 'follower';

  # Primary Key of the table
  protected $pk  = 'id';

}