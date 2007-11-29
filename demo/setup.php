<?php

ini_set('include_path','./lib:./lib/xajax:./lib/Smarty/libs:'.ini_get('include_path'));

checkEnvironment();

function checkEnvironment() {

    $missing = array();
    $files = array("Morcego/Graph/XML.php",
		   "Morcego/Server.php",
		   // Note that ${VERSION} will be substituted by correct version by package.sh
		   "morcego-${VERSION}.jar");

    foreach ($files as $file) {
	if (!file_exists($file)) {
	    $missing[] = $file;
	}
    }

    $thisDir = dirname(__FILE__);

    if (sizeof($missing) > 0) {
	echo "The following files are missing:<br /><ul>";
	foreach ($missing as $file) {
	    echo "<li>$file</li>";
	}
	echo "</ul><br/>They can be found in the binary package you downloaded, either at root or php/ dir.<br />";
	echo "Copy then to $thisDir and reload this page.";
	exit;
    }

    @$fp = fopen("templates_c/test.txt", "w");
    if (!$fp) {
	$user = getWebUser();
	echo "Your web server does not have permission to write in templates_c dir.<br />";
	if (!isWindows()) {
	    echo "You can fix this by doing one of the following commands:<br /><ul>";
	    echo "<li>chown $user $thisDir/templates_c</li>";
	    echo "<li>chmod 777 $thisDir/templates_c</li>";
	    echo "</ul>";
	} else {
	    echo "Sorry, we don't have information on how to fix this in windows. If you do so, ";
	    echo "contact the developers at morcego-dev@arca.ime.usp.br so that we put some help here.";
	}
	exit;
    }
}

function getWebUser() {
    $wwwuser = '';

    if (isWindows()) {
	$wwwuser = 'SYSTEM';
    }
    
    if (function_exists('posix_getuid')) {
	// group is being checked in case we need it later

	$user = @posix_getpwuid(@posix_getuid());
	$group = @posix_getpwuid(@posix_getgid());
	$wwwuser = $user ? $user['name'] : false;
	$wwwgroup = $group ? $group['name'] : false;
    }
    
    if (!$wwwuser) {
	$wwwuser = 'nobody (or the user account the web server is running under)';
    }
    
    if (!$wwwgroup) {
	$wwwgroup = 'nobody (or the group account the web server is running under)';
    }

    return $wwwuser;
}

function isWindows() {
    return strtoupper(substr(PHP_OS, 0, 3)) == 'WIN';
}

?>