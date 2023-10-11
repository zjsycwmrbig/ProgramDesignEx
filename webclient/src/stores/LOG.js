

export function debug(message){
    const currentTime = new Date().toLocaleTimeString();
    console.log(`[${currentTime}][DEBUG]: ${message}`)
}

export function info(message){
    const currentTime = new Date().toLocaleTimeString();
    console.log(`[${currentTime}][INFO ]: ${message}`)
}

export function warning(message){
    const currentTime = new Date().toLocaleTimeString();
    console.log(`[${currentTime}][WARNING]: ${message}`)
}

export function error(message){
    const currentTime = new Date().toLocaleTimeString();
    console.log(`[${currentTime}][ERROR]: ${message}`)
}