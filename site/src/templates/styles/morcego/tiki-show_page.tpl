{* $Header: /cvsroot/tikiwiki/tiki/templates/tiki-show_page.tpl,v 1.74.2.23 2005/06/21 19:57:40 rlpowell Exp $ *}

<iframe name="wiki" onLoad="morcegoNavigate(this);" style="display: none" id='morcego-content'></iframe>
<script language="JavaScript">
  var pageId = '{$page|escape}';
</script>
<script language="JavaScript" src="lib/js/morcego.js"></script>


<div class="morcego-float">
  <div id="morcego-wiki-title">{$page}</div>
  <div id="morcego-wiki-description">{$description}</div>
</div>
<iframe name="applet" src="tiki-wiki3d.php?page={$page|escape:"url"}" width="800" height="300" frameborder="0" scrolling="no"></iframe>

<div class="wikitext">
{if $structure eq 'y'}
<div class="tocnav">
<table>
<tr>
  <td>
    {if $prev_info and $prev_info.page_ref_id}
		<a href="tiki-index.php?page_ref_id={$prev_info.page_ref_id}"><img src="img/icons2/nav_dot_right.gif" border="0" height="11" width="8" alt="{tr}Previous page{/tr}" 
   			{if $prev_info.page_alias}
   				title='{$prev_info.page_alias}'
   			{else}
   				title='{$prev_info.pageName}'
   			{/if}/></a>{else}<img src="img/icons2/8.gif" alt="" border="0" height="1" width="8" />{/if}
	{if $parent_info}
   	<a href="tiki-index.php?page_ref_id={$parent_info.page_ref_id}"><img src="img/icons2/nav_home.gif" border="0" height="11" width="13" alt="{tr}Parent page{/tr}" 
        {if $parent_info.page_alias}
   	      title='{$parent_info.page_alias}'
        {else}
   	      title='{$parent_info.pageName}'
        {/if}/></a>{else}<img src="img/icons2/8.gif" alt="" border="0" height="1" width="8" />{/if}
   	{if $next_info and $next_info.page_ref_id}
      <a href="tiki-index.php?page_ref_id={$next_info.page_ref_id}"><img src="img/icons2/nav_dot_left.gif" height="11" width="8" border="0" alt="{tr}Next page{/tr}" 
		  {if $next_info.page_alias}
			  title='{$next_info.page_alias}'
		  {else}
			  title='{$next_info.pageName}'
		  {/if}/></a>{else}<img src="img/icons2/8.gif" alt="" border="0" height="1" width="8" />
	{/if}
	{if $home_info}
   	<a href="tiki-index.php?page_ref_id={$home_info.page_ref_id}"><img src="img/icons2/home.gif" border="0" height="16" width="16" alt="TOC" 
		  {if $home_info.page_alias}
			  title='{$home_info.page_alias}'
		  {else}
			  title='{$home_info.pageName}'
		  {/if}/></a>{/if}
  </td>
  <td>
{if $tiki_p_edit_structures and $tiki_p_edit_structures eq 'y' }
    <form action="tiki-editpage.php" method="post">
      <input type="hidden" name="current_page_id" value="{$page_info.page_ref_id}" />
      <input type="text" name="page" />

      {if $page_info and !$parent_info }
      <input type="hidden" name="add_child" value="checked" /> 
      {else}
      <input type="checkbox" name="add_child" /> {tr}Child{/tr}
      {/if}      
      <input type="submit" name="insert_into_struct" value="{tr}Add Page{/tr}" />
    </form>
{/if}
  </td>
</tr>
<tr>
  <td colspan="2">
    {section loop=$structure_path name=ix}
      {if $structure_path[ix].parent_id}&nbsp;{$site_crumb_seper}&nbsp;{/if}
	  <a href="tiki-index.php?page_ref_id={$structure_path[ix].page_ref_id}">
      {if $structure_path[ix].page_alias}
        {$structure_path[ix].page_alias}
	  {else}
        {$structure_path[ix].pageName}
	  {/if}
	  </a>
	{/section}
  </td>
</tr>
</table>
</div>
{/if}
{if $feature_wiki_ratings eq 'y'}{include file="poll.tpl"}{/if}
<div id="morcego-wiki-content">
{$parsed}
</div>
{if $pages > 1}
	<br />
	<div align="center">
		<a href="tiki-index.php?{if $page_info}page_ref_id={$page_info.page_ref_id}{else}page={$page|escape:"url"}{/if}&amp;pagenum={$first_page}"><img src="img/icons2/nav_first.gif" border="0" height="11" width="27" alt="{tr}First page{/tr}" title="{tr}First page{/tr}" /></a>

		<a href="tiki-index.php?{if $page_info}page_ref_id={$page_info.page_ref_id}{else}page={$page|escape:"url"}{/if}&amp;pagenum={$prev_page}"><img src="img/icons2/nav_dot_right.gif" border="0" height="11" width="8" alt="{tr}Previous page{/tr}" title="{tr}Previous page{/tr}" /></a>

		<small>{tr}page{/tr}:{$pagenum}/{$pages}</small>

		<a href="tiki-index.php?{if $page_info}page_ref_id={$page_info.page_ref_id}{else}page={$page|escape:"url"}{/if}&amp;pagenum={$next_page}"><img src="img/icons2/nav_dot_left.gif" border="0" height="11" width="8" alt="{tr}Next page{/tr}" title="{tr}Next page{/tr}" /></a>


		<a href="tiki-index.php?{if $page_info}page_ref_id={$page_info.page_ref_id}{else}page={$page|escape:"url"}{/if}&amp;pagenum={$last_page}"><img src="img/icons2/nav_last.gif" border="0" height="11" width="27" alt="{tr}Last page{/tr}" title="{tr}Last page{/tr}" /></a>
	</div>
{/if}
</div> {* End of main wiki page *}

{if $has_footnote eq 'y'}<div class="wikitext wikifootnote">{$footnote}</div>{/if}

{if isset($wiki_authors_style) && $wiki_authors_style eq 'business'}
<p class="editdate">
  {tr}Last edited by{/tr} {$lastUser|userlink}
  {section name=author loop=$contributors}
   {if $smarty.section.author.first}, {tr}based on work by{/tr}
   {else}
    {if !$smarty.section.author.last},
    {else} {tr}and{/tr}
    {/if}
   {/if}
   {$contributors[author]|userlink}
  {/section}.<br />                                         
  {tr}Page last modified on{/tr} {$lastModif|tiki_long_datetime}.
</p>
{elseif isset($wiki_authors_style) &&  $wiki_authors_style eq 'collaborative'}
<p class="editdate">
  {tr}Contributors to this page{/tr}: {$lastUser|userlink}
  {section name=author loop=$contributors}
   {if !$smarty.section.author.last},
   {else} {tr}and{/tr}
   {/if}
   {$contributors[author]|userlink}
  {/section}.<br />
  {tr}Page last modified on{/tr} {$lastModif|tiki_long_datetime} {tr}by{/tr} {$lastUser|userlink}.
</p>
{elseif isset($wiki_authors_style) &&  $wiki_authors_style eq 'none'}
{else}
<p class="editdate">
  {tr}Created by{/tr}: {$creator|userlink}
  {tr}last modification{/tr}: {$lastModif|tiki_long_datetime} {tr}by{/tr} {$lastUser|userlink}
</p>
{/if}

{if $wiki_feature_copyrights  eq 'y' and $wikiLicensePage}
  {if $wikiLicensePage == $page}
    {if $tiki_p_edit_copyrights eq 'y'}
      <p class="editdate">{tr}To edit the copyright notices{/tr} <a href="copyrights.php?page={$copyrightpage}">{tr}click here{/tr}</a>.</p>
    {/if}
  {else}
    <p class="editdate">{tr}The content on this page is licensed under the terms of the{/tr} <a href="tiki-index.php?page={$wikiLicensePage}&amp;copyrightpage={$page|escape:"url"}">{$wikiLicensePage}</a>.</p>
  {/if}
{/if}

{if $print_page eq 'y'}
  <div class="editdate" align="center"><p>
    {tr}The original document is available at{/tr} {$urlprefix}tiki-index.php?page={$page|escape:"url"}
  </p></div>
{/if}

{if $is_categorized eq 'y' and $feature_categories eq 'y' and $feature_categoryobjects eq 'y'}
<div class="catblock">{$display_catobjects}</div>
{/if}
