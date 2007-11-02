<html>
  {$xajax_js}
  <head>
    <link rel="stylesheet" type="text/css" href="style.css" />
  </head>
  <body>
    <div id="all">
      <div id="title">{$startNode}</div>
      <div id="morcego">
          <applet ID="morcegoApplet" archive="morcego-0.6.0-dev.jar" code="br.arca.morcego.Morcego" width="350" height="350" MAYSCRIPT>
            <param name="serverUrl" value="{$baseUrl}/server.php">
            <param name="startNode" value="{$startNode}">
	    <param name="width" value="350">
	    <param name="height" value="350">

          </applet>
      </div>
      <div id="content"></div>
    </div>


  </body>
</html>