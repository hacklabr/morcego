<?php

require('setup.php');
require('xajax_core/xajax.inc.php');
require('Smarty.class.php');

$xajax = new xajax();

$xajax->registerFunction('loadNode');

$xajax->processRequest();

$smarty = new Smarty();

$xajax->configure('javascript URI', 'lib/xajax');
$smarty->assign('xajax_js', $xajax->getJavascript());

//Obtain full URL to reach this same dir
$baseUrl = preg_replace('/\/(index.php.*)?$/','', 'http://' . $_SERVER['HTTP_HOST'] . $_SERVER['REQUEST_URI']);

$smarty->assign('baseUrl', $baseUrl);
$smarty->assign('startNode', 'Welcome');

$smarty->display('index.tpl');

function loadNode($nodeId) {
    $objResponse = new xajaxResponse();

    $smarty = new Smarty();

    $smarty->template_dir = "data";

    $objResponse->assign('title', 'innerHTML', $nodeId);
    $objResponse->assign('content', 'innerHTML', $smarty->fetch("$nodeId.tpl"));

    return $objResponse;    
    
}

?>