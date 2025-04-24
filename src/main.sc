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
            $session.gamno = getRandomWord();
            
        a: {{ $session.gamno }} 

    state: NoMatch
        event!: noMatch
        a: Я не понял. Вы сказали: {{$request.query}}

    state: Match
        event!: match
        a: {{$context.intent.answer}}