<?php

require_once("Morcego/Graph/Tiki.php");
require_once("lib/tikilib.php");

class Morcego_Graph_Tiki_Wiki extends Morcego_Graph_Tiki {

    var $existingColor;
    var $missingColor;

    function Morcego_Graph_Tiki_Wiki() {
	global $tikilib;

	$this->existingColor = $tikilib->get_preference("wiki_3d_existing_page_color", '#00BB88');
	$this->minssingColor = $tikilib->get_preference("wiki_3d_missing_page_color", '#00BB88');

	$this->Morcego_Graph_Tiki($tikilib);
    }

    function getNode($pageName) {
	$nodes = array();
	$links = array();


	$node = array();

	$node['type'] = "Round";

	if ($this->tiki->page_exists($pageName)) {
	    $exists = true;
	    $node['color'] = $this->existingColor;
	} else {
	    $exists = false;
	    $node['color'] = $this->missingColor;
	}

	$node['onclick'] = 'xajax_loadPage("'.preg_replace('/"/','\"',$pageName).'");';

	$nodes[$pageName] =& $node;
	
	$already = array();

	if ($exists) {
	    $query = "select `toPage` from `tiki_links` where `fromPage`=?";
	    $result = $this->query($query,array($pageName));
	    while ($row = $result->fetchRow(DB_FETCHMODE_ASSOC)) {
		$link = array('from' => $pageName,
			      'to' => $row['toPage'],
			      'type' => 'Directional');
		
		$already[$link['to']] = sizeof($links);
		
		$links[] = $link;
	    }
	}
	
	$query = "select `fromPage` from `tiki_links` where `toPage`=?";
	$result = $this->query($query,array($pageName));
    
	while ($row = $result->fetchRow(DB_FETCHMODE_ASSOC)) {
	    if (isset($already[$row['fromPage']])) {
		$links[$already[$row['fromPage']]]['type'] = 'Bidirectional';
	    } else {
		$links[] = array('from' => $row['fromPage'],
				 'to' => $pageName,
				 'type' => ( ( $exists && $this->tiki->page_exists($row['fromPage']))
					     ? 'Directional' : 'Dashed'));
		
	    }
	}
			
	return array('nodes' => $nodes,
		     'links' => $links);	
	
    }

    
}

?>