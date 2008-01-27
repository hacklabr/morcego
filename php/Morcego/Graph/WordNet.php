<?php

require_once("Morcego/Graph.php");

/*
 * Serves to applet a thesaurus-like application based on data provided by "WordNet SQL builder":
 * http://sourceforge.net/projects/wnsqlbuilder/.
 *
 * Needs a global mysql connection to be up with proper database selected.
 *
 */

class Morcego_Graph_WordNet extends Morcego_Graph {

    var $synsetColors = array("a" => "#0000FF",  // adjective
			      "r" => "#880077",  // adverb
			      "n" => "#FF0000",  // noum
			      "v" => "#00FF00"); // verb

    var $antonymColor = '#FF0000';

    function Morcego_Graph_WordNet() {
	$this->Morcego_Graph();
    }

    function getNode($nodeId) {
	// Node type is identified by nodeId format: synsets are identified by
	// their IDs, while words by the word itself

	$nodes = array();
	$links = array();

	if (preg_match("/^\d+$/", $nodeId)) {
	    return $this->getSynset($nodeId);
	} else {
	    return $this->getWord($nodeId);
	}
    }

    function getSynset($synsetId) {
	$query = "select s.*, c.pos from synset s, categorydef c where c.categoryid=s.categoryid and synsetid=?";
	$bindvals = array($synsetId);
	$synset = $this->getRow($query, $bindvals);
	
	if (!$synset) return array();
	
	$node = array();
	
	$node['type'] = 'Round';
	$node['title'] = '';
	$node['description'] = $synset['definition'];
	$node['color'] = $this->synsetColors[$synset['pos']];
	
	$nodes[$synsetId] = $node;
	
	// Get all words with this meaning
	$query = "select w.lemma from sense s, word w where w.wordid=s.wordid and s.synsetid=?";
	$bindvals = array($synsetId);
	$words = $this->getCol($query, $bindvals);
	
	foreach ($words as $word) {
	    $links[] = array('from' => $synsetId,
			     'to' => $word,
			     'type' => 'Solid');
	}
	
	// Get related synsets, by now only hypernyms
	$query = "select synset2id from semlinkref where synset1id=? and linkid=1";
	$bindvals = array($synsetId);
	$synsets = $this->getCol($query, $bindvals);
	
	foreach ($synsets as $peerId) {
	    $links[] = array('from' => $synsetId,
			     'to' => $peerId,
			     'type' => 'DashedDirectional',
			     'description' => 'is a type of');
	}

	return array('nodes' => $nodes,
		     'links' => $links);
	
    }

    function getWord($word) {

	$nodes = array();
	$links = array();

	$lower = strtolower($word);
	$wordId = $this->getOne("select wordid from word where lemma=?", array($lower));

	if (!$wordId) {
	    return array();
	}
	
	$casedwordId = $this->getOne("select wordid from casedword where lemma=?", array($word));

	if (!$casedwordId) $word = $lower;

	$nodes[$word] = array('type' => 'Text');
	
	// Get all meanings for this word
	$query = "select s.synsetid, se.casedwordid, c.lemma as cased from synset s, sense se left join casedword c on c.wordid=se.casedwordid where se.synsetid=s.synsetid and se.wordid=?";
	$bindvals = array($wordId);
	$senses = $this->getData($query, $bindvals);
	
	foreach ($senses as $sense) {
	    if ((!$casedwordId && !$sense['casewordId']) || $casedwordId == $sense['casedwordId']) {
		// Link to synset
		$links[] = array('from' => $sense['synsetid'],
				 'to' => $word,
				 'type' => 'Solid');

		// Get all antonyms for this word & meaning
		$query = "select w.lemma from lexlinkref l, word w where l.synset1id=? and l.word1id=? and w.wordid=l.word2id and linkid=30";
		$bindvals = array($sense['synsetid'], $wordId);
		$antonyms = $this->getCol($query, $bindvals);

		foreach ($antonyms as $ant) {
		    $links[] = array('from' => $word,
				     'to' => $ant,
				     'type' => 'Dashed',
				     'color' => $this->antonymColor);
		}

		
	    }
	    // If this is not the same case as meaning, link to the similar word instead of synset
	    // TODO avoid repetition among cased <-> noncased links
	    elseif ($sense['cased']) {
		$links[] = array('from' => $word,
				 'to' => $sense['cased'],
				 'type' => 'Solid');
	    } else {
		$links[] = array('from' => $word,
				 'to' => $lower,
				 'type' => 'Solid');
	    }
	}

	return array('nodes' => $nodes,
		     'links' => $links);
	
    }

    function getData($query, $bindvals) {

	$query = $this->_makeQuery($query, $bindvals);

	$result = mysql_query($query);

	$data = array();
	while ($row = mysql_fetch_assoc($result)) {
	    $data[] = $row;
	}

	return $data;
    }

    function getOne($query, $bindvals) {
	$query = $this->_makeQuery($query, $bindvals);

	$result = mysql_query($query);

	$row = mysql_fetch_array($result);

	if ($row) return $row[0];

	return $row;
    }

    function getRow($query, $bindvals) {
	$query = $this->_makeQuery($query, $bindvals);

	$result = mysql_query($query);

	return mysql_fetch_assoc($result);
    }

    function getCol($query, $bindvals) {
	$query = $this->_makeQuery($query, $bindvals);

	$result = mysql_query($query);

	$col = array();

	while ($row = mysql_fetch_array($result)) {
	    $col[] = $row[0];
	}
	
	return $col;
    }

    function _makeQuery($query, $bindvals) {
	$i = 0;
	
	while (($pos = strpos($query, '?')) !== false) {
	    $query = substr_replace($query, 
				    "'" . mysql_real_escape_string($bindvals[$i++]) . "'",
				    $pos, 1);
	}

	return $query;
    }

}

?>
