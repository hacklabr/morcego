<?php

require("XML/Parser/Simple.php");

class Morcego_Graph extends XML_Parser_Simple {

    // Directory where all xml and html will be stored

    // Hash containing list of nodes in this graph, indexed by nodeId
    var $nodes;

    // Array containing list of links contained in this graph
    var $links;

    // Hash to keep track of links already stored
    var $linkTrack;

    // Current node being searched
    var $currentNodeId;

    // Queue of neighbour nodes to loaded
    var $nodeQueue;

    // Current node or link being processed, to agregate its properties
    var $currentElementProperties;

    function Morcego_Graph($dataDir) {

	$this->dataDir = preg_replace("/\/?$/", "/", $dataDir);

	$this->XML_Parser_Simple();
    }

    // Gets a subgraph containing nodes that distance less than $depth links from nodeId
    function getSubGraph($nodeId, $depth) {

	$this->nodes = array();
	$this->links = array();
	$this->linkTrack = array();
	$this->currentElementProperties = array();

	$queue = array($nodeId);

	for ($i = 0; $i < $depth && sizeof($queue) > 0; $i++) {
	    $this->nodeQueue = array();
	    foreach ($queue as $nodeId) {
		$this->loadNode($nodeId);
	    }
	    $queue = $this->nodeQueue;
	}

	return array('nodes' => $this->nodes,
		     'links' => $this->links);
    }

    function loadNode($nodeId) {
	if (!isset($this->nodes[$nodeId])) {
	    $this->currentNodeId = $nodeId;
	    $this->setInputFile($this->dataDir . $nodeId . ".xml");
	    $this->parse();
	    $this->currentNodeId = null;
	}
    }

    // This is to handle elements inside nodes and links, that will be treated as properties
    function handleElement($name, $attribs, $data) {
	if ($name == 'NODE') {
	    $this->handleNode($attribs);
	} elseif ($name == 'LINK') {
	    $this->handleLink($attribs);
	} elseif ($this->getCurrentDepth() == 2) {
	    $this->currentElementProperties[$name] = $data;
	}	    
    }

    function handleNode($attribs) {
	$node = array_merge($this->currentElementProperties, $attribs);
	
	if (isset($node['ID'])) {
	    $nodeId = $node['ID'];
	    unset($node['ID']);
	} else {
	    $nodeId = $this->currentNodeId;
	}

	$this->nodes[$nodeId] =& $node;
	
	$this->currentElementProperties = array();
    }

    function handleLink($attribs) {
	$link = array_merge($this->currentElementProperties, $attribs);

	if (!isset($link['FROM'])) {
	    $link['FROM'] = $this->currentNodeId;
	}
	if (!isset($link['TO'])) {
	    $link['TO'] = $this->currentNodeId;
	}

	if ($link['FROM'] == $link['TO']) {
	    // Ignore loops
	    return;
	}


	if ($link['FROM'] == $this->currentNodeId) {
	    $neighbour = $link['TO'];
	} elseif ($link['TO'] == $this->currentNodeId) {
	    $neighbour = $link['FROM'];
	}

	if (isset($neighbour) && !isset($this->nodes[$neighbour])) {
	    $this->nodeQueue[] = $neighbour;
	}

	
	if (!isset($this->linkTrack[$link['FROM']])) {
	    $this->linkTrack[$link['FROM']] = array();
	}

	if (!isset($this->linkTrack[$link['FROM']][$link['TO']])) {
	    $this->links[] =& $link;
	    $this->linkTrack[$link['FROM']][$link['TO']] = 1;
	}

	$this->currentElementProperties = array();
	
    }
    
}

?>