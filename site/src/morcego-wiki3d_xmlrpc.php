<?php 

// $HEADER$
ini_set('include_path', ini_get('include_path') . ':./lib/pear');

require_once("Morcego/Server.php");
require_once("Morcego/Graph/Tiki/Wiki.php");

$graph = new Morcego_Graph_Tiki_Wiki();
$server = new Morcego_Server($graph);


?>
