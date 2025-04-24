words.globalVar = ["apples", "oranges", "bananas"];

function getRandomWord(){
    return words[$jsapi.random(Object.keys(words).length)]
}