let majorKeys = ['A', 'Bb', 'B', 'C', 'Db', 'D', 'Eb', 'E', 'F', 'F#', 'G', 'Ab']
let majorKeysEquivalents = ['A', 'A#', 'B', 'C', 'C#', 'D', 'D#', 'E', 'F', 'Gb', 'G', 'G#']
let minorKeys = ['Am', 'Bbm', 'Bm', 'Cm', 'C#m', 'Dm', 'D#m', 'Em', 'Fm', 'F#m', 'Gm', 'G#m']
let minorKeysEquivalents = ['Am', 'A#m', 'Bm', 'Cm', 'Dbm', 'Dm', 'Ebm', 'Em', 'Fm', 'Gbm', 'Gm', 'Abm']
let naturalNotes = ['A', 'B', 'C', 'D', 'E', 'F', 'G']
let naturalNotesMinor = ['Am', 'Bm', 'Cm', 'Dm', 'Em', 'Fm', 'Gm']

let majorKey = true;
let keyListCurrentIndex = 0;

// Verifica se o tom da música é maior ou menor
function setKeyMinorOrMajor(){
    const keyElement = document.getElementById('form:keyLabel');
    if(keyElement.innerText.endsWith('m')){
        majorKey = false;
    }

    if(majorKey)
        keyListCurrentIndex = majorKeys.indexOf(keyElement.innerText);
    else
        keyListCurrentIndex = minorKeys.indexOf(keyElement.innerText);
}

// Adiciona formatação e classe específica aos acordes da cifra
function formatChords() {
    setKeyMinorOrMajor();

    const chordsElement = document.getElementById('form:chordsDisplay');

    if (chordsElement) {
        let rawChords = chordsElement.innerText;

        // Padrão para identificar (praticamente) todos os tipos de acordes e não considerar "acordes" no meio da letra
        // Ex:         D
        //     Que nenhuma família comece
        //                       A7
        //     Em qualquer de repente
        // Nesse caso, "Em" (acorde de mi menor) não será identificado como acorde
        // const pattern = "(\\b[A-G](?:b|bb|#|##)*(?:[1-9])*((?:sus|maj|min|aug|m|M|\\+|-|dim|Maj)?[\\d\\/]*)*(\\(((?:b|#)?1?\\d(\\+|-)?(\\/)?)*\\))?(\\/[A-G](b|bb|#|##)?)?)(M|maj|Maj)?(?=\\s|$)(?! [a-zH-Z0-9_]| [A-G][ac-z])";

        // Pattern considerando white space ou nulo no final de cada acorde:
        const pattern = "(\\b[A-G](?:b|bb|#|##)*(?:[1-9])*((?:sus|maj|min|aug|m|M|\\+|-|dim|Maj)?[\\d\\/]*)*(\\(((?:b|#)?1?\\d(\\+|-)?(\\/)?)*\\))?(\\/[A-G](b|bb|#|##)?)?)(M|maj|Maj)?(?=\\s|$)(?! [a-zH-Z0-9_]| [A-G][ac-ln-z])(\\s|\\n|\\b|$|\\0)";
        const regex = new RegExp(pattern, "g");
        let lines = rawChords.split('\n');
        let formatted = [];

        lines.forEach(line => {
            if(line.match(regex)){
                formatted.push(line.replace(regex, "<span class=\"chord\">$1 </span>")+"<br/>")
            } else {
                formatted.push(line+"<br/>");
            }
        })
        chordsElement.innerHTML = formatted.join("");
    }
}

function transpose(semitones){
    keyListCurrentIndex += semitones;
    if(keyListCurrentIndex < 0){
        keyListCurrentIndex = 12 + keyListCurrentIndex;
    } else if(keyListCurrentIndex > 11){
        keyListCurrentIndex = keyListCurrentIndex - 12;
    }

    const keyElement = document.getElementById('form:keyLabel');
    if(majorKey)
        keyElement.innerText = majorKeys[keyListCurrentIndex];
    else
        keyElement.innerText = minorKeys[keyListCurrentIndex];

    transposeChords(semitones);
}

function transposeChords(semitones){
    document.querySelectorAll('.chord').forEach(el => {
        const original = el.innerText;
        el.innerText = transposeChord(original, semitones);
    });
}

function transposeChord(chord, semitones) {
    // Pega apenas a root note do acorde e verifica se é menor. Além disso, pega a bass note, se houver
    let rootNote = chord.charAt(0);
    let bassNote = '';
    let rootNoteOk = false;
    let bassNotePresent = false;
    for(let i = 1; i < chord.length; i++){
        // Verifica se o acorde é bemol, sustenido ou menor
        if(!rootNoteOk) {
            if (chord.charAt(i) === 'm') {
                rootNote += chord.charAt(i);
                rootNoteOk = true;
            } else if(chord.charAt(i) === 'b' || chord.charAt(i) === '#'){
                rootNote += chord.charAt(i);
            } else
                rootNoteOk = true;
        }

        if(rootNoteOk) { // Verifica se o acorde tem baixo em outra nota
            if (chord.charAt(i) === '/') {
                bassNotePresent = true;
            } else if (bassNotePresent && (chord.charAt(i) >= 'A' && chord.charAt(i) <= 'G')) {
                bassNote += chord.charAt(i);
                if ((i + 1) < chord.length && (chord.charAt(i + 1) === 'b' || chord.charAt(i + 1) === '#'))
                    bassNote += chord.charAt(i + 1);
                break;
            } else
                bassNotePresent = false;
        }
    }

    // =============== Fazer a transposição ===============
    let rootNoteEquivalent = false;
    let bassNoteEquivalent = false;
    let rootNoteIndex = 0;
    let bassNoteIndex = 0;

    // Se encontrou bass note
    if(bassNotePresent) {
        bassNoteIndex = majorKeys.indexOf(bassNote);
        if(bassNoteIndex === -1) {
            bassNoteIndex = majorKeysEquivalents.indexOf(bassNote);
            bassNoteEquivalent = true;
        }

        bassNoteIndex += semitones;
        if(bassNoteIndex < 0){
            bassNoteIndex = 12 + bassNoteIndex;
        } else if(bassNoteIndex > 11){
            bassNoteIndex = bassNoteIndex - 12;
        }

        if(bassNoteEquivalent){
            chord = chord.replace(bassNote, majorKeysEquivalents.at(bassNoteIndex));
        } else{
            chord = chord.replace(bassNote, majorKeys.at(bassNoteIndex));
        }
    }

    if(rootNote.endsWith('m')){
        rootNoteIndex = minorKeys.indexOf(rootNote);
        if(rootNoteIndex === -1)
            rootNoteIndex = minorKeysEquivalents.indexOf(rootNote);
            rootNoteEquivalent = true;
    } else{
        rootNoteIndex = majorKeys.indexOf(rootNote);
        if(rootNoteIndex === -1) {
            rootNoteIndex = majorKeysEquivalents.indexOf(rootNote);
            rootNoteEquivalent = true;
        }
    }

    let rootNoteNewIndex = rootNoteIndex + semitones;
    if(rootNoteNewIndex < 0){
        rootNoteNewIndex = 12 + rootNoteNewIndex;
    } else if(rootNoteNewIndex > 11){
        rootNoteNewIndex = rootNoteNewIndex - 12;
    }

    let whitespaceRemoved = false;
    if(rootNote.endsWith('m')){
        if(rootNoteEquivalent){
            chord = chord.replace(rootNote, minorKeysEquivalents.at(rootNoteNewIndex));
        } else{
            chord = chord.replace(rootNote, minorKeys.at(rootNoteNewIndex));
        }

        // Se a root note pós transpose é bemol/sustenido, remove o white space após o acorde
        if(naturalNotesMinor.indexOf(minorKeys.at(rootNoteNewIndex)) === -1) {
            chord = chord.replace(' ', '');
            whitespaceRemoved = true;
        }
        else if(!chord.endsWith(' '))
            chord += ' ';
    } else {
        if (rootNoteEquivalent) {
            chord = chord.replace(rootNote, majorKeysEquivalents.at(rootNoteNewIndex));
        } else {
            chord = chord.replace(rootNote, majorKeys.at(rootNoteNewIndex));
        }
        // Se a root note pós transpose é bemol/sustenido, remove o white space após o acorde
        if (naturalNotes.indexOf(majorKeys.at(rootNoteNewIndex)) === -1){
            chord = chord.replace(' ', '');
            whitespaceRemoved = true;
        }
        else if(!chord.endsWith(' '))
            chord += ' ';
    }

    if(bassNotePresent && !whitespaceRemoved && naturalNotes.indexOf(majorKeys.at(bassNoteIndex)) === -1){
        chord = chord.replace(' ', '');
    }

    // console.log("root note: " + rootNote + "    bass note: "+ bassNote +"    chord transposed: "+chord+"teste");
    return chord;
}

function transposeTo(key){
    let semitones = 0;
    if(majorKey)
        semitones = majorKeys.indexOf(key) - keyListCurrentIndex;
    else
        semitones = minorKeys.indexOf(key) - keyListCurrentIndex;

    if(semitones !== 0)
        transpose(semitones);
}

window.onload = formatChords;