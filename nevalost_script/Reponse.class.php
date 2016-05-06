<?php

class Reponse {
    private $succes;

    public function getSucces(){
         return $this->succes;
    }

    public function setSucces($succes) {
         $this->succes = $succes;
    }

    public function jsonSerialize() {
        return $this->succes;
    }
}