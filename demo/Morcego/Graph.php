<?php

class Morcego_Graph {

    function Morcego_Graph() {
    }

    // Abstract, must be implemented by superclasses. Returns same structure of a getSubgraph, but no depth
    function getNode($nodeId) {
	return array();
    }

    // Gets a subgraph containing nodes that distance less than $depth links from nodeId
    function getSubgraph($nodeId, $depth) {

	// Hash of nodes, indexed by nodeId, eacho node is a hash of propertiles
	$nodes = array();

	// Array of links, each one being a hash of properties containing at least "to" and "from" indexes
	$links = array();

	// Hash to keep track of links already stored
	$linkTrack = array();

	// Queues of neighbour nodes to loaded
	$queue = array($nodeId);
	$localQueue = array();

	for ($i = 0; $i < $depth && sizeof($queue) > 0; $i++) {
	    $localQueue = array();
	    foreach ($queue as $nodeId) {
		$subgraph = $this->getNode($nodeId);
		
		if (isset($subgraph['nodes'])) {
		    foreach ($subgraph['nodes'] as $id => $node) {
			if (!isset($nodes[$id])) {
			    $nodes[$id] = $node;
			}
		    }
		}

		if (isset($subgraph['links'])) {
		    foreach ($subgraph['links'] as $link) {
			if (!isset($linkTrack[$link['from']])) {
			    $linkTrack[$link['from']] = array();
			}
			if (!isset($linkTrack[$link['from']][$link['to']])) {
			    $linkTrack[$link['from']][$link['to']] = 1;
			    $links[] = $link;

			    if ($link['from'] == $nodeId) {
				$neighbour = $link['to'];
			    } elseif ($link['to'] == $nodeId) {
				$neighbour = $link['from'];
			    }
			    
			    if (isset($neighbour) && !isset($nodes[$neighbour]) && !in_array($neighbour, $localQueue)) {
				$localQueue[] = $neighbour;
			    }

			}
		    }
		}
	    }
	    $queue = $localQueue;
	}

	return array('nodes' => $nodes,
		     'links' => $links);
    }

    // Returns a hash containing "to" and "from", each one being an array of neighbours
    function getLinks($nodeId) {

	$graph = $this->getNode($nodeId);

	$links = array('to' => array(),
		       'from' => array());

	foreach ($graph['links'] as $link) {
	    if ($link['to'] == $nodeId) {
		$links['from'][] = $link['from'];
	    } elseif ($link['from'] == $nodeId) {
		$links['to'][] = $link['to'];
	    }
	}

	return $links;
    }


}

?>