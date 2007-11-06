{* $Header: /cvsroot/tikiwiki/tiki/templates/tiki-show_page.tpl,v 1.74.2.23 2005/06/21 19:57:40 rlpowell Exp $ *}

<script language="JavaScript">
  var pageId = '{$page|escape}';
</script>
<script language="JavaScript" src="lib/js/prototype.js"></script>
<script language="JavaScript" src="lib/js/morcego.js"></script>
<script language="JavaScript">pageId = "{$page|escape}";</script>

{$xajax_js}

<div id="morcego-top">
  <div id="morcego-wiki-title">{$page}</div>
  <div id="morcego-wiki-description">{$description}</div>
</div>

<div id="morcego-content-window">
  <div id="morcego-applet" ></div>

  <div id="wiki-content" class="wikitext">{$parsed}</div>

</div> {* morcego-content-window *}

{if $has_footnote eq 'y'}<div class="wikitext wikifootnote">{$footnote}</div>{/if}

<p id="editdate">{include file="tiki-page_editdate.tpl"}</p>

{include file="tiki-page_bar.tpl" }

{if $is_categorized eq 'y' and $feature_categories eq 'y' and $feature_categoryobjects eq 'y'}
<div class="catblock">{$display_catobjects}</div>
{/if}

{if $feature_bot_bar eq 'y'}
<div id="tiki-bot" class="menubar">
  {include file="tiki-bot_bar.tpl"}
</div>
{/if}
