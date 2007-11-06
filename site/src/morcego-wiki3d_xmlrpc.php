<?php 

// $HEADER$

require_once("Morcego/Server.php");
require_once("Morcego/Graph/Tiki/Wiki.php");

$graph = new Morcego_Graph_Tiki_Wiki();
$server = new Morcego_Server($graph);


?>
