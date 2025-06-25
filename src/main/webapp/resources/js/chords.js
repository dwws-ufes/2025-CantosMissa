function formatChords() {
    let chordsElement = document.getElementById('form:chordsDisplay');

    if (chordsElement) {
        let rawChords = chordsElement.innerText;

        const notes = "[A-G]",
            accentuations = "(?:b|bb)",
            chords = "*(?:#|##|sus|maj|min|aug|m|M|\\+|-|dim)",
            withh = "*[\\d\\/]*",
            numbers = "*(?:[1-9])",
            outsideChords = "(?=\\s|$)(?! \\w)",
            pattern = "("+"\\b(" + notes + accentuations + numbers + chords + withh + '(?:' + notes + accentuations + chords + withh + ')*)'+outsideChords+")",
            regex = new RegExp(pattern, "g");

        let lines = rawChords.split('\n');
        let formatted = [];

        lines.forEach(line => {
            if(line.match(regex)){
                formatted.push(line.replace(regex, "<b>$1</b>")+"<br/>")
            } else {
                formatted.push(line+"<br/>");
            }
        })
        chordsElement.innerHTML = formatted.join("");
    }
}

window.onload = formatChords;