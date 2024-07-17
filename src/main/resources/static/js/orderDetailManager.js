function httpRequest(url, method, body){
    return fetch(url, {
        method: method,
        headers: {
            "Content-Type" : "application/json"
        },
        body: body
    });
}

function toCheckValueList(comps){
    let list = [];
    comps.forEach(checkBox => {
        if(checkBox.checked){
            list.push(checkBox.nextElementSibling.value);
        }
    });

    return list;
}

const allItemSelectCheckBox = document.getElementById('allItemSelectCheckBox');

if(allItemSelectCheckBox){
    allItemSelectCheckBox.addEventListener('click', () => {
        if(allItemSelectCheckBox.checked){
            Array.from(document.getElementsByClassName('itemSelectCheckBox')).forEach(checkBox => checkBox.checked = true);
        }
        else{
            Array.from(document.getElementsByClassName('itemSelectCheckBox')).forEach(checkBox => checkBox.checked = false);
        }
    });
}

const selectedStatusChangeButton = document.getElementById('selectedStatusChange-btn');

if(selectedStatusChangeButton){
    selectedStatusChangeButton.addEventListener('click', () => {

        if(toCheckValueList(Array.from(document.getElementsByClassName('itemSelectCheckBox'))).length === 0){
            alert('항목을 선택하세요.');
            return;
        }

        if(document.getElementById('statusSel').value !== '선택'){
            let body = JSON.stringify({
                paymentDetailIds : toCheckValueList(Array.from(document.getElementsByClassName('itemSelectCheckBox'))),
                paymentDetailStatus : document.getElementById('statusSel').value
            });

            httpRequest(`/admin/orderDetailManager`, 'POST', body)
            .then(response => {
                if(response.ok){
                    alert('변경되었습니다.');
                    location.reload();
                }
                else{
                    alert('error');
                }
            });
        }
        else{
            alert('상태를 선택해주세요.');
        }
    });
}

const itemStatusSelections = document.getElementsByClassName('itemStatusSel');

if(itemStatusSelections){
    Array.from(itemStatusSelections).forEach(itemStatusSel => {
        itemStatusSel.addEventListener('change', () => {
            let ids = []
            ids.push(itemStatusSel.parentElement.parentElement.parentElement.children[1].value);
            let body = JSON.stringify({
                paymentDetailIds : ids,
                paymentDetailStatus : itemStatusSel.value
            });

            httpRequest(`/admin/orderDetailManager`, 'POST', body)
            .then(response => {
                if(response.ok){
                    alert('변경되었습니다.');
                    location.reload();
                }
                else{
                    alert('error');
                }
            });
        });
    });
}

//넘버링
if(document.getElementsByClassName('number')){
    Array.from(document.getElementsByClassName('number')).forEach((comp, index) => comp.textContent = index + 1);
}

