{foreach from=$neighbours item=neighbour}
   <span onclick="navigateTo('{$neighbour|escape}')" class="menu">{$neighbour}</span>
{/foreach}
