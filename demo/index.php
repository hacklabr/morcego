<?php

require('setup.php');
require('xajax_core/xajax.inc.php');
require('Smarty.class.php');
require('Morcego/Graph/XML.php');

$startNode = "Welcome";

if (isset($_REQUEST['node'])) {
    $nodeId = $_REQUEST['node'];
} else {
    $nodeId = $startNode;
}

$graph = new Morcego_Graph_XML("templates/data");

$smarty = new Smarty();

// First assign to smarty what is needed for both ajax and static call

$smarty->assign('links', $graph->getLinks($nodeId));

// Process ajax requests

$xajax = new xajax();
$xajax->registerFunction('loadNode');

$xajax->processRequest();

// Static request, configure smarty and display content

$xajax->configure('javascript URI', 'lib/xajax');
$smarty->assign('xajax_js', $xajax->getJavascript());

//Obtain full URL to reach this same dir
$baseUrl = 'http://' . $_SERVER['HTTP_HOST'] . preg_replace('/\/[^\/]+$/','', $_SERVER['REQUEST_URI']);

$smarty->assign('baseUrl', $baseUrl);
$smarty->assign('nodeId', $nodeId);
$smarty->assign('content', $smarty->fetch("data/$nodeId.tpl"));

$smarty->display('index.tpl');


// Dinamically load node, called through xajax
function loadNode($nodeId) {
    global $graph, $smarty;

    $objResponse = new xajaxResponse();

    $objResponse->assign('title', 'innerHTML', $nodeId);
    if (file_exists($smarty->template_dir . "/data/$nodeId.tpl")) {
	$objResponse->assign('content', 'innerHTML', $smarty->fetch("data/$nodeId.tpl"));
    } else {
	$objResponse->assign('content', 'innerHTML', '');
    }

    $links = $graph->getLinks($nodeId);
    if (isset($links['from'][0])) {
	$smarty->assign('neighbours', $links['from']);
	$objResponse->assign('arrow-left', 'style.display', 'block');
	$objResponse->assign('links-from', 'innerHTML', $smarty->fetch("links.tpl"));
    } else {
	$objResponse->assign('arrow-left', 'style.display', 'none');
    }
    
    if (isset($links['to'][0])) {
	$smarty->assign('neighbours', $links['to']);
	$objResponse->assign('arrow-right', 'style.display', 'block');
	$objResponse->assign('links-to', 'innerHTML', $smarty->fetch("links.tpl"));
    } else {
	$objResponse->assign('arrow-right', 'style.display', 'none');
    }

    $objResponse->assign('links-to', 'style.display', 'none');
    $objResponse->assign('links-from', 'style.display', 'none');

    return $objResponse;    
    
}

?>