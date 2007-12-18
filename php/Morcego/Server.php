<?php

ini_set('display_errors',0);

require_once("XML/Server.php");

class Morcego_Server extends XML_RPC_Server {

    // Morcego_Graph object
    var $graph;

    // Using for string encoding in Macintoshs
    var $_macCharTable = array(
			       "C4" => "80", // Ä
			       "C5" => "81", // Å
			       "C7" => "82", // Ç
			       "C9" => "83", // É
			       "D1" => "84", // Ñ
			       "D6" => "85", // Ö
			       "DC" => "86", // Ü
			       "E1" => "87", // á
			       "E0" => "88", // à
			       "E2" => "89", // â
			       "E4" => "8A", // ä
			       "E3" => "8B", // ã
			       "E5" => "8C", // å
			       "E7" => "8D", // ç
			       "E9" => "8E", // é
			       "E8" => "8F", // è
			       "EA" => "90", // ê
			       "EB" => "91", // ë
			       "ED" => "92", // í
			       "EC" => "93", // ì
			       "EE" => "94", // î
			       "EF" => "95", // ï
			       "F1" => "96", // ñ
			       "F3" => "97", // ó
			       "F2" => "98", // ò
			       "F4" => "99", // ô
			       "F6" => "9A", // ö
			       "F5" => "9B", // õ
			       "FA" => "9C", // ú
			       "F9" => "9D", // ù
			       "FB" => "9E", // û
			       "FC" => "9F", // ü

			       "C0" => "CB", // À
			       "C3" => "CC", // Ã
			       "D5" => "CD", // Õ

			       "FF" => "D8", // ÿ

			       "C2" => "E5", // Â
			       "CA" => "E6", // Ê
			       "C1" => "E7", // Á
			       "CB" => "E8", // Ë
			       "C8" => "E9", // È
			       "CD" => "EA", // Í
			       "CE" => "EB", // Î
			       "CF" => "EC", // Ï
			       "CC" => "ED", // Ì
			       "D3" => "EE", // Ó
			       "D4" => "EF", // Ô

			       "D2" => "F1", // Ò
			       "DA" => "F2", // Ú
			       "DB" => "F3", // Û
			       "D9" => "F4", // Ù
			       );

    var $win = false;
    var $mac = false;


    function Morcego_Server($graph) {
	
	$this->graph = $graph;

	$map = array('getSubGraph' => array('function' => array($this, 'getSubGraph')),
		     'getVersion' => array('function' => array($this, 'getVersion')));

	$this->win = preg_match("/Windows/", $_SERVER['HTTP_USER_AGENT']);
	$this->mac = preg_match("/Mac OS/", $_SERVER['HTTP_USER_AGENT']);
	
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
	    $encoded[strtolower($key)] = new XML_RPC_Value($this->_recodeString($value), "string");
	}
	
	return new XML_RPC_Value($encoded, "struct");
    }

    function _recodeString($str) {
	if ($this->win) {
	    return utf8_decode($str);
	}

	if ($this->mac) {
	    $str = utf8_decode($str);

	    $new = "";
	    for ($i = 0; $i < strlen($str); $i++) {
		$c = substr($str, $i, 1);
		$isoCode = strtoupper(dechex(ord($c)));
		if (isset($this->_macCharTable[$isoCode])) {
		    $new .= chr(hexdec($this->_macCharTable[$isoCode]));
		} else {
		    $new .= $c;
		}
	    }

	    return $new;
	}

	return $str;
    }

}

?>