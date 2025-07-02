//获取当前系统时间 
export function getCurDate(){
    var now=new Date();
    var year=now.getFullYear();
    var month=now.getMonth()+1;//月份从0开始，所以要加1
    var date=now.getDate();
    month=month<10?"0"+month:month; //如果月份小于10，则在前面补0
    date=date<10?"0"+date:date; //如果日期小于10，则在前面补0
    return year+"-"+month+"-"+date;
}

//想sessionStorage中存储数据
export function setSessionStorage(key, value) {
   setSessionStorage.setItem(key, JSON.stringify(value));
}

//从sessionStorage中获取数据
export function getSessionStorage(key) {
   var str= sessionStorage.getItem(key);
    if (str=''||str==null||str=='null'||str==undefined) {
        return null;
    }else{
        return JSON.parse(str);
    }
}

//从sessionStorage中删除数据
export function removeSessionStorage(key) {
    sessionStorage.removeItem(key);
}

//向localStorage存储数据
export function setLocalStorage(key, value) {
    localStorage.setItem(key, JSON.stringify(value));
}

//从localStorage获取数据
export function getLocalStorage(key) {
    var str = localStorage.getItem(key);
    if (str == '' || str == null || str == 'null' || str == undefined) {
        return null;
    } else {
        return JSON.parse(str);
    }
}   

//从localStorage中删除数据
export function removeLocalStorage(key) {
    localStorage.removeItem(key);
} 

