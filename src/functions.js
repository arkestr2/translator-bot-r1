words.globalVar = ["apples", "oranges", "bananas"];

funcion getRandomWord(){
    return words[$jsapi.random(Object.keys(words).length)]
}