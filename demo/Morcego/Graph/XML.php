<?php

require("XML/Parser/Simple.php");
require("Morcego/Graph.php");

class Morcego_Graph_XML extends Morcego_Graph {

    function Morcego_Graph_XML($dataDir) {
	$this->parser = new Morcego_Graph_XML_parser($dataDir);

	$this->Morcego_Graph();

    }

    function getNode($nodeId) {
	return $this->parser->getNode($nodeId);
    }


}

class Morcego_Graph_XML_Parser extends XML_Parser_Simple {

    // Directory where all xml and html will be stored
    var $dataDir;

    // Hash of nodes, indexed by nodeId, eacho node is a hash of propertiles
    var $nodes;
    var $links;

    // Current node being searched
    var $currentNodeId;

    // Current node or link being processed, to agregate its properties
    var $currentElementProperties;

    function Morcego_Graph_XML_Parser($dataDir) {

	$this->dataDir = preg_replace("/\/?$/", "/", $dataDir);
	$this->XML_Parser_Simple();
    }


    function getNode($nodeId) {
	$this->nodes = array();
	$this->links = array();

	$this->currentNodeId = $nodeId;

	$this->setInputFile($this->dataDir . $nodeId . ".xml");
	$this->parse();

	return array('nodes' => $this->nodes,
		     'links' => $this->links);
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
	
	$this->_lowerCase($node);
	
	if (isset($node['id'])) {
	    $nodeId = $node['id'];
	    unset($node['id']);
	} else {
	    $nodeId = $this->currentNodeId;
	}

	$this->nodes[$nodeId] =& $node;
	
	$this->currentElementProperties = array();
    }

    function handleLink($attribs) {
	$link = array_merge($this->currentElementProperties, $attribs);

	$this->_lowerCase($link);

	if (!isset($link['from'])) {
	    $link['from'] = $this->currentNodeId;
	} 

	if (!isset($link['to'])) {
	    $link['to'] = $this->currentNodeId;
	}

	if ($link['from'] == $link['to']) {
	    // Ignore loops
	    return;
	}


	if ($link['from'] == $this->currentNodeId) {
	    $neighbour = $link['to'];
	} elseif ($link['to'] == $this->currentNodeId) {
	    $neighbour = $link['from'];
	}

	if (isset($neighbour) && !isset($this->nodes[$neighbour])) {
	    $this->nodeQueue[] = $neighbour;
	}

	
	$this->links[] =& $link;

	$this->currentElementProperties = array();
	
    }

    function _lowerCase(&$ar) {
	foreach ($ar as $k => $v) {
	    if ($k != strtolower($k)) {
		$ar[strtolower($k)] = $v;
		unset($ar[$k]);
	    }
	}
    }
    
}

?>