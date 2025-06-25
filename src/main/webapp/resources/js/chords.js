function formatChords() {
    let chordsElement = document.getElementById('form:chordsDisplay');

    if (chordsElement) {
        let rawChords = chordsElement.innerText;

        // Padrão para identificar (praticamente) todos os tipos de acordes e não considerar "acordes" no meio da letra
        // Ex:         D
        //     Que nenhuma família comece
        //                       A7
        //     Em qualquer de repente
        // Nesse caso, "Em" (acorde de mi menor) não é identificado como acorde
        const pattern = "(\\b[A-G](?:b|bb|#|##)*(?:[1-9])*(?:sus|maj|min|aug|m|M|\\+|-|dim|Maj)*[\\d\\/]*(\\(1?\\d(\\+|-)?(\\/1?\\d(\\+|-)?)?\\))?(\\/[A-G](b|bb|#|##)?)?)(M|maj|Maj)?(?=\\s|$)(?! [a-zH-Z0-9_]| [A-G][ac-z])"
        const regex = new RegExp(pattern, "g");
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