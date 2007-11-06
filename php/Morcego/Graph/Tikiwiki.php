<?php

require_once("tiki-setup.php");

require_once("Morcego/Graph.php");

class Morcego_Graph_Tiki extends Morcego_Graph {

    var $tiki;

    function Morcego_Graph_Tiki($tiki) {
	$this->tiki =& $tiki;
	$this->Morcego_Graph();
    }

    function query($query, $values = null, $numrows = -1, $offset = -1, $reporterrors = true ) {
	return $this->tiki->query($query, $values, $numrows, $offset, $reporterrors);
    }

    function getOne($query, $values = null, $reporterrors = true, $offset = 0) {
	return $this->tiki->getOne($query, $values, $reporterrors, $offset);
    }

    function getRow($query, $values = null, $reporterrors = true, $offset = 0) {
	$result = $this->tiki->query($query, $values, 1, $offset, $reporterrors);
	return $result->fetchRow();
    }

}

?>
