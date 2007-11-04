<?php

require_once("XML/Server.php");

class Morcego_Server extends XML_RPC_Server {

    // Morcego_Graph object
    var $graph;

    function Morcego_Server($graph) {
	
	$this->graph = $graph;

	$map = array('getSubGraph' => array('function' => array($this, 'getSubGraph')),
		     'getVersion' => array('function' => array($this, 'getVersion')));

	$this->XML_RPC_Server($map);
    }

    function getVersion() {
	return new XML_RPC_Response(new XML_RPC_Value(2, "int"));
    }

    function getSubGraph($params) {
	$nodeId = $params->getParam(0); $nodeId = $nodeId->scalarVal();
	$depth = $params->getParam(1); $depth = $depth->scalarVal();

	$encoded = $this->_encodeGraph($this->graph->getSubGraph($nodeId, $depth));

	return new XML_RPC_Response($encoded);
    }

    function _encodeGraph($graph) {
	$encodedNodes = array();
	$encodedLinks = array();
	
	foreach ($graph['links'] as $link) {
	    $encodedLinks[] = $this->_encodeElement($link);
	}

	foreach ($graph['nodes'] as $nodeId => $node) {
	    $encodedNodes[$nodeId] = $this->_encodeElement($node);
	}

	return new XML_RPC_Value(array('nodes' => new XML_RPC_Value($encodedNodes, "struct"),
				       'links' => new XML_RPC_Value($encodedLinks, "array")),
				 'struct');
    }

    function _encodeElement($structure) {
	$encoded = array();

	foreach ($structure as $key => $value) {
	    $encoded[strtolower($key)] = new XML_RPC_Value($value, "string");
	}
	
	return new XML_RPC_Value($encoded, "struct");
    }

}

?>