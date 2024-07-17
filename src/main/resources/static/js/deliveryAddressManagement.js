function httpRequest(url, method, body){
    return fetch(url, {
        method: method,
        headers: {
            "Content-Type" : "application/json"
        },
        body: body
    });
}

const submitAddressButton = document.getElementById('submitAddress-btn');

if(submitAddressButton){
    submitAddressButton.addEventListener('click', () => {

        //입력된 정보를 이용하여 http 요청
        let body = JSON.stringify({
            addressId: document.getElementById('addressId').value,
            addressNickname: document.getElementById('addressNickname').value,
            postalCode: document.getElementById('sample6_postcode').value,
            roadNameAddress: document.getElementById('sample6_address').value,
            detailAddress: document.getElementById('sample6_detailAddress').value,
            recipientName: document.getElementById('recipientName').value,
            recipientPhone: document.getElementById('phoneTop').value + document.getElementById('phoneMiddle').value + document.getElementById('phoneBottom').value,
            deliveryRequest: document.getElementById('deliveryRequest').value,
            frontDoorSecret: document.getElementById('frontDoorSecret').value,
            deliveryBoxNum: document.getElementById('deliveryBoxNum').value,
            addressDefault: document.getElementById('defaultAddress').value
        });
        httpRequest(`/user/address/management`, 'POST', body)
        .then(response => {
            if(response.ok){
                if(document.getElementById('addressId').value === ''){
                    alert('등록되었습니다.');
                    location.replace('/user/address/list');
                }
                else{
                    alert('변경사항이 저장되었습니다.');
                    location.replace('/user/address/list');
                }
            }
            else{
                alert('error')
            }
        });
    });
}

//입력 여부 확인하여 보여줌
const deliveryRequestButton = document.getElementsByClassName('deliveryRequest-btn');

if(deliveryRequestButton){
    Array.from(deliveryRequestButton).forEach(button => {
        button.addEventListener('click', () => {
            button.children[0].setAttribute('src', '/images/icon_img/addressCheck.png');
            document.getElementById('deliveryRequest').value = button.children[1].textContent;

            Array.from(deliveryRequestButton).forEach(other => {
                if(button !== other){
                    other.children[0].setAttribute('src', '/images/icon_img/addressUncheck.png')
                }
            });

            console.log(button.nextElementSibling.id);
            if(button.nextElementSibling.id === 'deliveryBoxNumInputPlace'){
                document.getElementById('deliveryBoxNumInputPlace').classList.remove('d-hidden');
                document.getElementById('deliveryBoxNum').value = deliveryBoxNumView.value;
            }
            else{
                document.getElementById('deliveryBoxNumInputPlace').classList.add('d-hidden');
                document.getElementById('deliveryBoxNum').value = '';
            }
        });
    });
}

const deliveryBoxNumView = document.getElementById('deliveryBoxNumView');

if(deliveryBoxNumView){
    deliveryBoxNumView.addEventListener('blur', () => {
        document.getElementById('deliveryBoxNum').value = deliveryBoxNumView.value;
    });
}

const notUsedPasswordButton = document.getElementById('notUsedPassword-btn');

if(notUsedPasswordButton){
    notUsedPasswordButton.addEventListener('click', () => {
        notUsedPasswordButton.children[0].setAttribute('src', '/images/icon_img/addressCheck.png');
        document.getElementById('frontDoorSecret').value = '';
        usedPasswordButton.children[0].setAttribute('src', '/images/icon_img/addressUncheck.png');
    });
}

const usedPasswordButton = document.getElementById('usedPassword-btn');

if(usedPasswordButton){
    usedPasswordButton.addEventListener('click', () => {
        usedPasswordButton.children[0].setAttribute('src', '/images/icon_img/addressCheck.png');
        document.getElementById('frontDoorSecret').value = usedPasswordButton.children[1].value;
        notUsedPasswordButton.children[0].setAttribute('src', '/images/icon_img/addressUncheck.png');
    });

    usedPasswordButton.children[1].addEventListener('blur', () => {
        document.getElementById('frontDoorSecret').value = usedPasswordButton.children[1].value;
    });
}

const defaultAddressButton = document.getElementById('defaultAddress-btn');

if(defaultAddressButton){
    defaultAddressButton.addEventListener('click', () => {
        if(document.getElementById('defaultAddress').value === '0'){
            defaultAddressButton.children[0].setAttribute('src', '/images/icon_img/addressCheck.png');
            document.getElementById('defaultAddress').value = 1;
        }
        else{
            defaultAddressButton.children[0].setAttribute('src', '/images/icon_img/addressUncheck.png');
            document.getElementById('defaultAddress').value = 0;
        }
    });
}

const deleteAddressButton = document.getElementById('deleteAddress-btn');

if(deleteAddressButton){
    deleteAddressButton.addEventListener('click', () => {
        let body = JSON.stringify({
            addressId: document.getElementById('addressId').value
        });

        httpRequest(`/user/address/delete`, 'DELETE', body)
        .then(response => {
            if(response.ok){
                alert('배송지 정보가 삭제되었습니다.');
                location.replace('/user/address/list');
            }
            else{
                alert('error');
            }
        });
    });
}

const prevWindowButton = document.getElementById('prevWindow-btn');

if(prevWindowButton){
    prevWindowButton.addEventListener('click', () => {
        window.history.back();
    });

}

//최초 세팅
if(document.getElementById('deliveryRequest').value === '문 앞'){
    document.getElementById('deliveryRequestSel').children[1].children[0].src  = '/images/icon_img/addressCheck.png';
}
if(document.getElementById('deliveryRequest').value === '직접 받고 부재 시 문 앞'){
    document.getElementById('deliveryRequestSel').children[2].children[0].src  = '/images/icon_img/addressCheck.png';
}
if(document.getElementById('deliveryRequest').value === '경비실'){
    document.getElementById('deliveryRequestSel').children[3].children[0].src  = '/images/icon_img/addressCheck.png';
}
if(document.getElementById('deliveryRequest').value === '택배함'){
    document.getElementById('deliveryRequestSel').children[4].children[0].src  = '/images/icon_img/addressCheck.png';
    document.getElementById('deliveryBoxNumInputPlace').classList.remove('d-hidden');
}

if(document.getElementById('frontDoorSecret').value === ''){
    document.getElementById('frontDoorSecret').previousElementSibling.previousElementSibling.children[0].src = '/images/icon_img/addressCheck.png';
}
else{
    document.getElementById('frontDoorSecret').previousElementSibling.children[0].src = '/images/icon_img/addressCheck.png';
}

if(document.getElementById('defaultAddress').value === '1'){
    console.log(document.getElementById('defaultAddress').previousElementSibling.previousElementSibling);
    document.getElementById('defaultAddress').previousElementSibling.previousElementSibling.src = '/images/icon_img/addressCheck.png';
}




