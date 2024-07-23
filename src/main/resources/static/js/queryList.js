function checkAuth(comp){
    if(comp.previousElementSibling.previousElementSibling.value === 'false'){
        location.href = '/community/qna/' + comp.previousElementSibling.value;
    }
    else{
        alert('권한이 없습니다.');
    }
}