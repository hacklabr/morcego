<?php

if (strpos($_SERVER["SCRIPT_NAME"],basename(__FILE__)) !== false) {
  header("location: index.php");
  exit;
}

require('lib/xajax/xajax_core/xajax.inc.php');

$xajax = new xajax();
$xajax->registerFunction('loadMorcego');
$xajax->registerFunction('loadPage');
$xajax->processRequest();

$xajax->configure('javascript URI', 'lib/xajax');
$smarty->assign('xajax_js', $xajax->getJavascript());

function loadMorcego($page) {
    global $smarty;

    $objResponse = new xajaxResponse();

    //Obtain full URL to reach this same dir
    $baseUrl = 'http://' . $_SERVER['HTTP_HOST'] . preg_replace('/\/[^\/]+$/','', $_SERVER['REQUEST_URI']);
    $smarty->assign('baseUrl', $baseUrl);
    $smarty->assign('page', $page);

    $objResponse->assign('morcego-applet', 'innerHTML', $smarty->fetch("tiki-wiki3d.tpl"));

    return $objResponse;
}

function loadPage($page) {
    global $smarty, $tikilib, $wikilib;
    
    $objResponse = new xajaxResponse();
    
    $info = $tikilib->get_page_info($page);

    if (isset($wiki_authors_style) && $wiki_authors_style != "classic") {
	$contributors = $wikilib->get_contributors($page, $info['user']);
	$smarty->assign('contributors',$contributors);
    }
    $smarty->assign_by_ref('lastModif',$info["lastModif"]);
    $smarty->assign_by_ref('lastUser',$info["user"]);
    $smarty->assign('creator',$wikilib->get_creator($page));

    $objResponse->script('pageId = "' . preg_replace('/"/','\"', $page) . '";');
    $objResponse->assign('wiki-content', 'innerHTML', $tikilib->parse_data($info['data'], $info['is_html']));
    $objResponse->assign('editdate', 'innerHTML', $smarty->fetch("tiki-page_editdate.tpl"));

    return $objResponse;
    
}

?>