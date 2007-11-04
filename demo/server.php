<pre><?php

ini_set('display_errors',0);

require('setup.php');
require_once("Morcego/Server.php");
require_once("Morcego/Graph/XML.php");


$graph = new Morcego_Graph_XML("templates/data");
$server = new Morcego_Server($graph);

?>