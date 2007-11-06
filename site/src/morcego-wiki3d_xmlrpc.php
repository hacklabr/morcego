<?php 

// $HEADER$

require_once("Morcego/Server.php");
require_once("Morcego/Graph/Tikiwiki/Wiki.php");

$graph = new Morcego_Graph_Tikiwiki_Wiki();
$server = new Morcego_Server($graph);


/*

include_once("lib/init/initlib.php");
require_once('db/tiki-db.php');
require_once('lib/tikilib.php');
require_once('lib/userslib.php');
require_once("XML/Server.php");
require_once("lib/wiki/wikilib.php");


$map = array (
	      "getSubGraph" => array( "function" => "getSubGraph" ),
	      "getVersion" => array( "function" => "getVersion" )
	      );

$server = new XML_RPC_Server( $map );

function getVersion($params) {
    return new XML_RPC_Response(new XML_RPC_Value(2, "int"));
}

function getSubGraph($params) {
    global $wikilib, $dbTiki;

    $nodeName = $params->getParam(0); $nodeName = $nodeName->scalarVal();
    $depth = $params->getParam(1); $depth = $depth->scalarVal();

    if (!$wikilib->page_exists($nodeName) && $wikilib->page_exists(utf8_encode($nodeName))) {
	$nodeName = utf8_encode($nodeName);
    }

    $response = array();
    $nodes = array();
    $links = array();

    $passed = array($nodeName => true);
    $queue = array($nodeName);
    $i = 0;

    $tikilib = new TikiLib($dbTiki);
    $existing_color = $tikilib->get_preference("wiki_3d_existing_page_color", '#00BB88');
    $missing_color = $tikilib->get_preference("wiki_3d_missing_page_color", '#FF6666');

    while ($i <= $depth && sizeof($queue) > 0) {
	$nextQueue = array();
	foreach ($queue as $nodeName) {

	    foreach (wiki_get_subgraph($nodeName) as $link) {
		$links[] = $link;
		$neighbour = $link['to'] == $nodeName ? $link['from'] : $link['to'];
		if (!isset($passed[$neighbour])) {
		    $nextQueue[] = $neighbour;
		    $passed[$neighbour] = true;
		}
	    }
	    
	    $node = array();

	    $base_url = 'http://' . $_SERVER['HTTP_HOST'] . $_SERVER['REQUEST_URI'];
	    $base_url = preg_replace('/\/morcego-wiki3d_xmlrpc.php.*$/','',$base_url);

	    if ($wikilib->page_exists($nodeName)) {
		$color = $existing_color;
		$actionUrl = "${base_url}/morcego-wikipage.php?page=${nodeName}";
	    } else {
		$color = $missing_color;
		$actionUrl = "${base_url}/tiki-editpage.php?page=${nodeName}";
	    }

	    //$node['neighbours'] = new XML_RPC_Value($neighbours, "array");
	    if (!empty($color)) {
		$node['color'] = new XML_RPC_Value($color, "string");
	    }
	    $node['actionUrl'] = new XML_RPC_Value($actionUrl, "string");

	    //$node['description']  = new XML_RPC_Value(" ** $nodeName ** ", "string");

	    $nodes[$nodeName] = new XML_RPC_Value($node, "struct");

	}
	$i++;
	$queue = $nextQueue;
    }

    $response['nodes'] = new XML_RPC_Value($nodes, "struct");
    
    $passed = array();
    foreach ($links as $link) {
	$from = $link['from'];
	$to = $link['to'];
	if (!isset($passed[$to])) {
	    $passed[$to] = array();
	}
	if (!isset($passed[$from])) {
	    $passed[$from] = array();
	}

	if (!isset($passed[$to][$from])) {
	    $link = array('from' => new XML_RPC_Value($from, 'string'),
			  'to' => new XML_RPC_Value($to, 'string'),
			  'type' => new XML_RPC_Value($link['type'],'string'),
			  'description' => $link['description']);



	    $links[] = new XML_RPC_Value($link, 'struct');

	    $passed[$to][$from] = 1;
	    $passed[$from][$to] = 1;
	}
    }

    $response['links'] = new XML_RPC_Value($links, 'array');

    return new XML_RPC_Response(new XML_RPC_Value($response, "struct"));
}

//TODO move this to wikilib

// Returns a data structure for links of a page, in format of Morcego
// This is used by wiki3d, to make the graph
function wiki_get_subgraph($page) {

    global $wikilib;
    
    $links = array();
    $already = array();
    
    $query = "select `toPage` from `tiki_links` where `fromPage`=?";
    $result = $wikilib->query($query,array($page));
    while ($row = $result->fetchRow(DB_FETCHMODE_ASSOC)) {
	$link = array(
		      'from' => $page,
		      'to' => $row['toPage'],
		      'type' => $wikilib->page_exists($row['toPage']) ? 'Directional' : 'Dashed',
		      );
	$links[] = $link;
	$already[$row['toPage']] = sizeof($links);
    }
    
    $query = "select `fromPage` from `tiki_links` where `toPage`=?";
    $result = $wikilib->query($query,array($page));
    while ($row = $result->fetchRow(DB_FETCHMODE_ASSOC)) {
	if (!isset($already[$row['toPage']])) {
	    $link = array(
			  'to' => $page,
			  'from' => $row['fromPage'],
			  'type' => 'Directional',
			  );
	    $links[] = $link;
	} else {
	    $links[$already[$row['toPage']]]['type'] = 'Solid';
	}
    }
    
    return $links;
    
}
*/

?>
