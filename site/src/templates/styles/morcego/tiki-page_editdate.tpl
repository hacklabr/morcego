{if isset($wiki_authors_style) && $wiki_authors_style eq 'business'}
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
  {tr}Page last modified on{/tr} {$lastModif|tiki_short_datetime}.
{elseif isset($wiki_authors_style) &&  $wiki_authors_style eq 'collaborative'}
  {tr}Contributors to this page{/tr}: {$lastUser|userlink}
  {section name=author loop=$contributors}
   {if !$smarty.section.author.last},
   {else} {tr}and{/tr}
   {/if}
   {$contributors[author]|userlink}
  {/section}.<br />
  {tr}Page last modified on{/tr} {$lastModif|tiki_short_datetime} {tr}by{/tr} {$lastUser|userlink}.
{elseif isset($wiki_authors_style) &&  $wiki_authors_style eq 'none'}
{else}
  {tr}Created by{/tr}: {$creator|userlink}
  {tr}last modification{/tr}: {$lastModif|tiki_short_datetime} {tr}by{/tr} {$lastUser|userlink}
{/if}