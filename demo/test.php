<pre><?php

require_once("Morcego/Server.php");

$graph = new Morcego_Graph("data");

print_r($graph->getSubGraph("Welcome", 3));

?>
