var pageId;

Element.observe(window, 'load', function(e) {
    $('morcego-applet').absolutize();
    var appletHeight = Position.positionedOffset($('editdate'))[1] - $('tiki-top').getHeight() - $('morcego-top').getHeight();
    var contentHeight = appletHeight - $('editdate').getHeight();
    $('morcego-applet').style.height = appletHeight + "px";
    $('wiki-content').style.height = contentHeight + "px";

    xajax_loadMorcego(pageId);
});

function navigateTo(page) {
    xajax_loadPage(page);
    document.morcegoApplet.navigateTo(page);
}

function editPage() {
    document.location.href = "tiki-editpage.php?page="+escape(pageId);
}

function pageHistory() {
    document.location.href = "tiki-pagehistory.php?source=0&page="+escape(pageId);
}

function removePage() {
    document.location.href = "tiki-removepage.php?version=last&page="+escape(pageId);
}

function renamePage() {
    document.location.href = "tiki-rename_page.php?page="+escape(pageId);
}

