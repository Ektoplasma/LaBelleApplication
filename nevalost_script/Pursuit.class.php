<?php
require_once("easyCRUD.class.php");

class Pursuit Extends Crud {

  # The table you want to perform the database actions on
  protected $table = 'poursuite';

  # Primary Key of the table
  protected $pk  = 'id';

}