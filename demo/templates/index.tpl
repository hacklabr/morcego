<html>

  <head>

    <link rel="stylesheet" type="text/css" href="style.css" />
    <link rel="icon" href="img/favicon.png" />

    {$xajax_js}

    <script language="JavaScript" src="lib/prototype.js" />

  </head>

  <body>
    <div id="all">

      <div id="title">{$nodeId}</div>

      <div id="morcego">
        <form name="appletForm">
	  <applet ID="morcegoApplet" archive="morcego-0.6.0-dev.jar" code="br.arca.morcego.Morcego" width="100%" height="100%" MAYSCRIPT>
            <param name="serverUrl" value="{$baseUrl}/server.php">
            <param name="startNode" value="{$nodeId}">
            <param name="showArcaLogo" value="true">

          </applet>
	</form>
      </div>

      <div id="right-colum">
	  <div id="navigation">

            <img id="arrow-left" src="img/larrow.gif" onclick="$('links-from').show()" {if !$links.from[0]}style="display: none"{/if}/>
	    <img id="arrow-right" src="img/rarrow.gif"  onclick="$('links-to').show()" {if !$links.to[0]}style="display: none"{/if}/>

          </div>

 	  <div id="links-from" style="display: none" onclick="$('links-from').hide()">
	    {include file="links.tpl" neighbours=$links.from}
          </div>
 	  <div id="links-to" class="links" style="display: none" onclick="$('links-to').hide()">
	      {include file="links.tpl" neighbours=$links.to}
          </div>


	  <div id="content">{$content}</div>

      </div>



    </div>
  </body>

  <script language="JavaScript" src="morcego.js" />

</html>