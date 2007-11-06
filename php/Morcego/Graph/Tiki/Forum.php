<?php

require("Morcego/Graph/Tiki.php");
require("lib/commentslib.php");

class Morcego_Graph_Tiki_Forum extends Morcego_Graph_Tiki {

    // Used to calculate the level
    var $levelCache;

    var $typeMap = array("c" => "Triangle",
			 "i" => "Round",
			 "r" => "Square",
			 "n" => "Text");
    
    var $colorMap = array("","#FF0000","#FF0088","#00FF00", "#8800FF","#0000FF");

    function Morcego_Graph_Tikiwiki_Forum() {
	global $dbTiki;

	$this->levelCache = array();

	$this->Morcego_Graph_Tikiwiki(new Comments($dbTiki));
    }

    function getNode($threadId) {
	$nodes = array();
	$links = array();

	$row = $this->getRow("select * from tiki_comments where threadId=?", array($threadId));

	$node = array();
	
	if (isset($this->levelCache[$threadId])) {
	    $node['level'] = $this->levelCache[$threadId];
	} elseif (isset($this->levelCache[$row['parentId']])) {
	    $node['level'] = $this->levelCache[$row['parentId']] + 1;
	} else {
	    $node['level'] = 0;
	}

	$node['title'] = $row['title'];
	$node['color'] = $this->colorMap[$row['comment_rating']];
	$node['type'] = $this->typeMap[$row['type']];

	$nodes[$threadId] =& $node;
	
	$this->levelCache[$threadId] = $node['level'];
	$this->levelCache[$node['parentId']] = $node['level'] - 1;
	
	$title = $this->getOne("select title from tiki_comments where threadId = ?", array($threadId));
	
	$query = "select `threadId`, `title` from `tiki_comments` where `parentId`=?";
	$result = $this->query($query,array($threadId));
	while ($row = $result->fetchRow(DB_FETCHMODE_ASSOC)) {
	    $percent = 100;
	    similar_text($row['title'], $title, $percent);
	    $link = array(
			  'from' => $threadId,
			  'to' => $row['threadId'],
			  'type' => ($percent > 80) ? 'Directional' : 'Dashed',
			  );
	    $links[] = $link;
	}
	
	if ($node['parentId']) {
	    $parentTitle = $this->getOne("select title from tiki_comments where threadId=?", array($node['parentId']));

	    $percent = 100;
	    similar_text($parentTitle, $title, $percent);

	    $link = array(
			  'to' => $threadId,
			  'from' => $node['parentId'],
			  'type' => ($percent > 80) ? 'Directional' : 'Dashed',
			  );
	    $links[] = $link;
	}
	
	return array('nodes' => $nodes,
		     'links' => $links);	
	
    }

    
}

?>