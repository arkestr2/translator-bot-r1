require: slotfilling/slotFilling.sc
  module = sys.zb-common

require: common.js
    module = sys.zb-common

require: functions.js
    
theme: /

    state: Start
        q!: $regex</start>
        a: Hello! Let's begin by reviewing your vocabulary. Please translate the following English words into Russian.
        go!: /Translate
        
    state: Translate
        script:
            $reactions.answer(getRandomWord());
            
        a: {{ $session.currWord }} 

    state: Correct
        a: Correct! Nice
        
    state: Wrong
        script:
            getTranslation()
        a: Wrong :( Some of the possible translations for "{{ word_to_translate }}" are:"

    state: NextWord
        a: NextWord
        
    state: Result
        a: Correct answers: {{ correct_count }}. Wrong answers: {{ wrong_count }}. Goodbye, see you later!
        
    state: NoMatch
        event!: noMatch
        a: Я не понял. Вы сказали: {{$request.query}}