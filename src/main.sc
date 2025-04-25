require: slotfilling/slotFilling.sc
    module = sys.zb-common

require: common.js
    module = sys.zb-common

require: functions.js
    
theme: /

    state: Start
        q!: $regex</start>
        script:
            $session.correctCount = 0;
            $session.wrongCount = 0;
        a: Hello! Let's begin by reviewing your vocabulary. Please translate the following English words into Russian.
        go!: /Translate
        
    state: Translate
        script:
            $session.currWord = getRandomWord();
            
        a: {{ $session.currWord }} 
        
    state: Compare
        intent: /check
        script:
            var input = $parseTree._input.toLowerCase().replaceAll("ё","е");
            
            log("INPUT");
            log(input);
            
            $session.isServerDown = false;
            $session.isInputCorrect = false;
            $session.translations = "";
            var meanings = getTranslations($session.currWord);
            
            if (meanings){
                log("MEANINGS_MAIN");
                log(meanings);
                var limit = Math.min(5, meanings.length);
                
                for(var i = 0; i < limit; i++){
                    var currMeaning = meanings[i];
                    if (input === currMeaning) {
                        $session.isInputCorrect = true;
                        break;
                    }
                    
                    log("TRANSLATIONS");
                    log($session.translations);
                    
                    $session.translations += "\n" + (i + 1) + ". " + currMeaning;
                }
            } else {
                $session.isServerDown = true;
            }
        if: $session.isServerDown
            go!: /NoConnection
        elseif: $session.isInputCorrect
            go!: Correct
        else: 
            go!: Wrong
            
        state: Correct
            a: Correct! Nice
            script: 
                $session.correctCount++;
            go!: /NextWord
            
        state: Wrong
            a: Wrong :( Some of the possible translations for "{{ $session.currWord }}" are: {{ $session.translations }}
            script: 
                $session.wrongCount++;
            go!: /NextWord

    state: NextWord
        script:
            $session.currWord = getRandomWord();
        a: The next word is "{{ $session.currWord }}"
        
    state: Result
        intent: /finish
        a: Correct answers: {{ $session.correctCount }}. 
            \nWrong answers: {{ $session.wrongCount }}.
            \n\nGoodbye, see you later!
        
    state: NoMatch
        event!: noMatch
        a: I don't think you typed in a word or a phrase in russian. Your responce was "{{ $request.query }}".
            \nTry typing in the translation for the word "{{ $session.currWord }}".
            
    state: NoConnection
        a: There are some server issues. Please try again later.