<html>
  {$xajax_js}
  <head>
    <link rel="stylesheet" type="text/css" href="style.css" />
  </head>
  <body>
    <div id="all">
      <div id="title">{$startNode}</div>
      <div id="morcego">
          <applet ID="morcegoApplet" archive="morcego-0.6.0-dev.jar" code="br.arca.morcego.Morcego" width="100%" height="100%" MAYSCRIPT>
            <param name="serverUrl" value="{$baseUrl}/server.php">
            <param name="startNode" value="{$startNode}">
            <param name="showArcaLogo" value="true">

          </applet>
      </div>
      <div id="content">{$content}</div>
    </div>


  </body>
</html>