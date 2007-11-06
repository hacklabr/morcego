function morcegoNavigate(frame) {
    var titl = frame.contentDocument.getElementById('morcego-wiki-title');
    var desc = frame.contentDocument.getElementById('morcego-wiki-description');
    var cont = frame.contentDocument.getElementById('morcego-wiki-content');

    pageId = titl.innerHTML;
    
    document.getElementById('morcego-wiki-title').innerHTML = titl.innerHTML;
    document.getElementById('morcego-wiki-description').innerHTML = desc.innerHTML;
    document.getElementById('morcego-wiki-content').innerHTML = cont.innerHTML;
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

function navigateTo(page) {
    applet.document.appletForm.Morcego.navigateTo(page);
    wiki.document.location.href = "morcego-wikipage.php?page=" + escape(page);
}
