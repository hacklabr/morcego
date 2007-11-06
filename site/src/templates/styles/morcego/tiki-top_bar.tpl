<div id="logo">
  <a href="index.php"><img src="img/logo.jpg" border="0"/></a>
  <h1>Morcego</h1>
  <h2>3D Network Browser</h2>
</div>

<div id="login">
  {if $user}
      {tr}logged as{/tr}: {$user} - 
      <a class="linkmodule" href="tiki-logout.php">{tr}Logout{/tr}</a>
  {else}
      You're not logged in. 
      <a href="tiki-login_scr.php">{tr}Log in{/tr}</a>
      {tr}or{/tr}
      <a href="tiki-register.php">{tr}Sign up{/tr}</a>
  {/if}
</div>

{phplayers id=42 type="horiz"}
