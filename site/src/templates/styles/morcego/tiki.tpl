{include file="header.tpl"}

{if $feature_bidi eq 'y'}<table dir="rtl" ><tr><td>{/if}

{* Index we display a wiki page here *}

<div id="tudo">

 {if $feature_top_bar eq 'y'} 
 <div id="tiki-top" class="tiki-top menubar">
    {include file="tiki-top_bar.tpl"} 
 </div> 
 {/if}


      {if $feature_left_column eq 'y' && $user}
      <div id="tiki-left">
      {section name=homeix loop=$left_modules}
      {$left_modules[homeix].data}
      {/section}
      </div>
      {/if}

      <div class="content">
        {include file=$mid}
      </div>

      {if $feature_right_column eq 'y' && sizeof($right_modules) > 0}
      <div id="tiki-right">
      {section name=homeix loop=$right_modules}
      {$right_modules[homeix].data}
      {/section}      
      </div>
      {/if}


</div>

{if $feature_bidi eq 'y'}</td></tr></table>{/if}

{include file="footer.tpl"}
