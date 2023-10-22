
// 多参数的日志函数
export function log(...args){
    const currentTime = new Date().toLocaleTimeString();
    console.log(`[${currentTime}][LOG]:`, ...args)
}
export function debug(...args){
    const currentTime = new Date().toLocaleTimeString();
    console.log(`[${currentTime}][DEBUG]:`, ...args)
}

export function info(...args){
    const currentTime = new Date().toLocaleTimeString();
    console.log(`[${currentTime}][INFO ]:`,...args)
}

export function warning(...args){
    const currentTime = new Date().toLocaleTimeString();
    console.log(`[${currentTime}][WARNING]: `,...args)
}

export function error(...args){
    const currentTime = new Date().toLocaleTimeString();
    console.log(`[${currentTime}][ERROR]:`,...args)
}