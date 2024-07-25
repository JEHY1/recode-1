function httpRequest(url, method, body){
    return fetch(url, {
        method : method,
        headers : {
            "Content-Type" : "application/json"
        },
        body : body
    });
}

function toList(components){
    let list = [];
    components.forEach(component => {
        if(component.value !== ''){
            list.push(component.value);
        }
    });
    return list;
}

function isMatchingRegex(str, regexPattern) {
    const regex = new RegExp(regexPattern);

    return regex.test(str);
}

function toWon(price){
    let PriceText = '';
    price += '';

    while(price.length > 3){
        console.log(price.substring(price.length - 3, price.length));
        PriceText += ',' + price.substring(price.length - 3, price.length);
        price = price.substring(0, price.length - 3);
        console.log(price);
        console.log(PriceText);
    }
    return price + PriceText + '원';
}

const addressInfoOpenButton = document.getElementById('addressInfoOpen-btn');

if(addressInfoOpenButton){
    addressInfoOpenButton.addEventListener('click', () => {
        if(addressInfoOpenButton.parentElement.nextElementSibling.classList.contains('d-hidden')){
            addressInfoOpenButton.parentElement.nextElementSibling.classList.remove('d-hidden');
            addressInfoOpenButton.textContent = '⋀';
        }
        else{
            addressInfoOpenButton.parentElement.nextElementSibling.classList.add('d-hidden');
            addressInfoOpenButton.textContent = '⋁';
        }
    });
}

const deliveryRequestButton = document.getElementsByClassName('deliveryRequest-btn');

if(deliveryRequestButton){
    Array.from(deliveryRequestButton).forEach(button => {
        button.addEventListener('click', () => {
            console.log('click');
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
                deliveryBoxNumView.classList.remove('border-red');
                deliveryBoxNumView.classList.add('border');
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
        if(deliveryBoxNumView.value !== ''){
            deliveryBoxNumView.classList.remove('border-red');
            deliveryBoxNumView.classList.add('border');
        }

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

const getNewAddressFormButton = document.getElementById('getNewAddressForm-btn');

if(getNewAddressFormButton){
    getNewAddressFormButton.addEventListener('click', () => {
        console.log('click');
        document.getElementById('addressNickname').classList.add('d-hidden');
    });
}

//원화 변경
if(document.getElementsByClassName('price')){
    Array.from(document.getElementsByClassName('price')).forEach(comp => comp.textContent = toWon(comp.textContent));
}

const selectBar = document.getElementById('selectBar');

if(selectBar){
    selectBar.addEventListener('change', () => {
        console.log('change');
        addressInfoOpenButton.classList.remove('d-hidden');

        if(selectBar.value === '-1'){
            console.log("new form");
            addressInfoOpenButton.parentElement.nextElementSibling.classList.remove('d-hidden');
            document.getElementById('addressInfoOpen-btn').textContent = '⋀';

            document.getElementById('addressNickname').parentElement.classList.add('d-hidden');
            document.getElementById('deliveryRequestSel').classList.remove('d-hidden');
            document.getElementById('deliveryRequestSel').nextElementSibling.classList.add('d-hidden');
            document.getElementById('deliveryRequestSel').nextElementSibling.nextElementSibling.classList.remove('d-hidden');
            document.getElementById('deliveryRequestSel').nextElementSibling.nextElementSibling.nextElementSibling.classList.add('d-hidden');

            document.getElementById('addressNickname').removeAttribute('readonly');
            document.getElementById('recipientName').removeAttribute('readonly');
            document.getElementById('sample6_addressAndPostcode').setAttribute('onclick', 'sample6_execDaumPostcode()');
            document.getElementById('sample6_detailAddress').removeAttribute('readonly');
            document.getElementById('phoneTop').removeAttribute('readonly');
            document.getElementById('phoneMiddle').removeAttribute('readonly');
            document.getElementById('phoneBottom').removeAttribute('readonly');

            //배송비 설정 및 최종금액 변경
            let beforeAddress = document.getElementById('sample6_address').value;
            if(beforeAddress.includes('제주특별자치도')){
                document.getElementById('deliveryFeeInfo').textContent = toWon(parseInt(document.getElementById('deliveryFeeInfo').textContent.substring(0, document.getElementById('deliveryFeeInfo').textContent.length - 1).replace(/,/g, '')) - 5000);
                document.getElementById('deliveryFee').textContent = '+' + toWon(parseInt(document.getElementById('deliveryFee').textContent.replace(/,/g, '')) - 5000);
                document.getElementById('totalPaymentPrice').textContent = toWon(parseInt(document.getElementById('totalPaymentPrice').textContent.replace(/,/g, '')) - 5000);
                paymentButton.textContent = toWon(parseInt(document.getElementById('totalPaymentPrice').textContent.replace(/,/g, ''))) + ' 결제하기';
            }

            //새로운 배송지 입력시 기본 요청사항 기본 값을 '문 앞' 으로 설정
            document.getElementById('deliveryRequestSel').children[1].click();
            //새로운 배송지 입력시 기본 비밀번호 사용 여부를 '비밀번호 사용 안함' 으로 설정
            document.getElementById('deliveryRequestSel').nextElementSibling.nextElementSibling.children[1].click();
            //주소 view 를 비우기
            document.getElementById('addressNickname').value = '';
            document.getElementById('recipientName').value = '';
            document.getElementById('sample6_addressAndPostcode').value = '';
            document.getElementById('sample6_detailAddress').value = '';
            document.getElementById('phoneTop').value = '';
            document.getElementById('phoneMiddle').value = '';
            document.getElementById('phoneBottom').value = '';
            document.getElementById('sample6_postcode').value = '';
            document.getElementById('sample6_address').value = '';



        }
        else{
            document.getElementById('addressNickname').parentElement.classList.remove('d-hidden');
            document.getElementById('deliveryRequestSel').classList.add('d-hidden');
            document.getElementById('deliveryRequestSel').nextElementSibling.classList.remove('d-hidden');
            document.getElementById('deliveryRequestSel').nextElementSibling.nextElementSibling.classList.add('d-hidden');
            document.getElementById('deliveryRequestSel').nextElementSibling.nextElementSibling.nextElementSibling.classList.remove('d-hidden');

            //주소 불러오기시 주소 미입력 경고 삭제
            document.getElementById('recipientName').parentElement.classList.remove('border-red');
            document.getElementById('sample6_addressAndPostcode').parentElement.parentElement.parentElement.classList.remove('border-red');
            document.getElementById('phoneTop').parentElement.parentElement.classList.remove('border-red');

            document.getElementById('addressNickname').setAttribute('readonly', true);
            document.getElementById('recipientName').setAttribute('readonly', true);
            document.getElementById('sample6_addressAndPostcode').setAttribute('onclick', '');
            document.getElementById('sample6_detailAddress').setAttribute('readonly', true);
            document.getElementById('phoneTop').setAttribute('readonly', true);
            document.getElementById('phoneMiddle').setAttribute('readonly', true);
            document.getElementById('phoneBottom').setAttribute('readonly', true);

            let deliveryRequestSel = document.getElementById('deliveryRequestSel');
            deliveryRequestSel.nextElementSibling.children[1].children[0].src = '/images/icon_img/addressUncheck.png';
            deliveryRequestSel.nextElementSibling.children[2].children[0].src = '/images/icon_img/addressUncheck.png';
            deliveryRequestSel.nextElementSibling.children[3].children[0].src = '/images/icon_img/addressUncheck.png';
            deliveryRequestSel.nextElementSibling.children[4].children[0].src = '/images/icon_img/addressUncheck.png';
            deliveryRequestSel.nextElementSibling.children[5].classList.add('d-hidden');

            deliveryRequestSel.nextElementSibling.nextElementSibling.nextElementSibling.children[1].children[0].src = '/images/icon_img/addressUncheck.png';
            deliveryRequestSel.nextElementSibling.nextElementSibling.nextElementSibling.children[2].children[0].src = '/images/icon_img/addressUncheck.png';


            let body = JSON.stringify({
                addressId : selectBar.value
            });

            httpRequest(`/user/address/getAddressInfo`, 'POST', body)
            .then(response => {
                if(response.ok){
                    return response.json();
                }
                else{
                    throw Error('error1');
                }
            })
            .then(data => {
                if(document.getElementById('sample6_address').value.includes('제주특별자치도') && !data.addressRoadNameAddress.includes('제주특별자치도')){
                    document.getElementById('deliveryFeeInfo').textContent = toWon(parseInt(document.getElementById('deliveryFeeInfo').textContent.substring(0, document.getElementById('deliveryFeeInfo').textContent.length - 1).replace(/,/g, '')) - 5000);
                    document.getElementById('deliveryFee').textContent = '+' + toWon(parseInt(document.getElementById('deliveryFee').textContent.replace(/,/g, '')) - 5000);
                    document.getElementById('totalPaymentPrice').textContent = toWon(parseInt(document.getElementById('totalPaymentPrice').textContent.replace(/,/g, '')) - 5000);
                    paymentButton.textContent = toWon(parseInt(document.getElementById('totalPaymentPrice').textContent.replace(/,/g, ''))) + ' 결제하기';
                }
                else if(!document.getElementById('sample6_address').value.includes('제주특별자치도') && data.addressRoadNameAddress.includes('제주특별자치도')){
                    document.getElementById('deliveryFeeInfo').textContent = toWon(parseInt(document.getElementById('deliveryFeeInfo').textContent.substring(0, document.getElementById('deliveryFeeInfo').textContent.length - 1).replace(/,/g, '')) + 5000);
                    document.getElementById('deliveryFee').textContent = '+' + toWon(parseInt(document.getElementById('deliveryFee').textContent.replace(/,/g, '')) + 5000) + '원';
                    document.getElementById('totalPaymentPrice').textContent = toWon(parseInt(document.getElementById('totalPaymentPrice').textContent.replace(/,/g, '')) + 5000) + '원';
                    paymentButton.textContent = toWon(parseInt(document.getElementById('totalPaymentPrice').textContent.replace(/,/g, ''))) + ' 결제하기';
                }

                //받은 주소 정보 view 입력
                document.getElementById('addressNickname').value = data.addressNickname;
                document.getElementById('recipientName').value = data.addressRecipientName;
                document.getElementById('sample6_addressAndPostcode').value = data.addressRoadNameAddress + ' [' + data.addressPostalCode + ']';
                document.getElementById('sample6_detailAddress').value = data.addressDetailAddress;
                document.getElementById('phoneTop').value = data.addressRecipientPhone.substring(0, 3);
                document.getElementById('phoneMiddle').value = data.addressRecipientPhone.substring(3, 7);
                document.getElementById('phoneBottom').value = data.addressRecipientPhone.substring(7, 11);
                document.getElementById('sample6_postcode').value = data.addressPostalCode;
                document.getElementById('sample6_address').value = data.addressRoadNameAddress;

                //받은 요청사항 view 입력
                let deliveryRequest = document.getElementById('deliveryRequest');
                deliveryRequest.value = data.addressDeliveryRequest;

                document.getElementById('deliveryBoxNum').value = data.addressDeliveryBoxNum;
                if(deliveryRequest.value === '문 앞'){
                    document.getElementById('deliveryRequestSel').nextElementSibling.children[1].children[0].src ='/images/icon_img/addressCheck.png';
                }
                else if(deliveryRequest.value === '직접 받고 부재 시 문 앞'){
                   document.getElementById('deliveryRequestSel').nextElementSibling.children[2].children[0].src ='/images/icon_img/addressCheck.png';
                }
                else if(deliveryRequest.value === '경비실'){
                    document.getElementById('deliveryRequestSel').nextElementSibling.children[3].children[0].src ='/images/icon_img/addressCheck.png';
                }
                else{
                    document.getElementById('deliveryRequestSel').nextElementSibling.children[4].children[0].src ='/images/icon_img/addressCheck.png';
                    document.getElementById('deliveryRequestSel').nextElementSibling.children[5].classList.remove('d-hidden');
                    document.getElementById('deliveryBoxNumViewDot').value = data.addressDeliveryBoxNum;
                }

                //출입 비밀번호 view 입력
                document.getElementById('frontDoorSecret').value =  data.addressFrontDoorSecret;
                document.getElementById('frontDoorSecretViewDot').value = data.addressFrontDoorSecret;
                if(document.getElementById('frontDoorSecret').value !== ''){
                    document.getElementById('frontDoorSecretViewDot').previousElementSibling.src = '/images/icon_img/addressCheck.png';
                }
                else{
                    document.getElementById('frontDoorSecretViewDot').parentElement.previousElementSibling.children[0].src = '/images/icon_img/addressCheck.png';
                }

                document.getElementById('addressForm').classList.remove('border-red');
                document.getElementById('deliveryBoxNumView').classList.remove('border-red');
                document.getElementById('deliveryBoxNumView').classList.add('border');


            });



        }
    });

}

//address default 세팅
const hasDefaultAddress = document.getElementById('hasDefaultAddress');

if(hasDefaultAddress){
    //배송 요청사항 view 설정
    document.getElementById('addressNickname').parentElement.classList.remove('d-hidden');
    document.getElementById('deliveryRequestSel').classList.add('d-hidden');
    document.getElementById('deliveryRequestSel').nextElementSibling.classList.remove('d-hidden');
    document.getElementById('deliveryRequestSel').nextElementSibling.nextElementSibling.classList.add('d-hidden');
    document.getElementById('deliveryRequestSel').nextElementSibling.nextElementSibling.nextElementSibling.classList.remove('d-hidden');

    //주소 view 기본 설정
    document.getElementById('addressNickname').setAttribute('readonly', true);
    document.getElementById('recipientName').setAttribute('readonly', true);
    document.getElementById('sample6_addressAndPostcode').setAttribute('onclick', '');
    document.getElementById('sample6_detailAddress').setAttribute('readonly', true);
    document.getElementById('phoneTop').setAttribute('readonly', true);
    document.getElementById('phoneMiddle').setAttribute('readonly', true);
    document.getElementById('phoneBottom').setAttribute('readonly', true);


    //선택 이미지 기본 설정
    let deliveryRequest = document.getElementById('deliveryRequest');

    if(deliveryRequest.value === '문 앞'){
        document.getElementById('deliveryRequestSel').nextElementSibling.children[1].children[0].src ='/images/icon_img/addressCheck.png';
    }
    else if(deliveryRequest.value === '직접 받고 부재 시 문 앞'){
       document.getElementById('deliveryRequestSel').nextElementSibling.children[2].children[0].src ='/images/icon_img/addressCheck.png';
    }
    else if(deliveryRequest.value === '경비실'){
        document.getElementById('deliveryRequestSel').nextElementSibling.children[3].children[0].src ='/images/icon_img/addressCheck.png';
    }
    else{
        document.getElementById('deliveryRequestSel').nextElementSibling.children[4].children[0].src ='/images/icon_img/addressCheck.png';
        document.getElementById('deliveryRequestSel').nextElementSibling.children[5].classList.remove('d-hidden');
    }

    if(document.getElementById('frontDoorSecretViewDot').value !== ''){
        document.getElementById('frontDoorSecretViewDot').previousElementSibling.src = '/images/icon_img/addressCheck.png';
    }
    else{
        document.getElementById('frontDoorSecretViewDot').parentElement.previousElementSibling.children[0].src = '/images/icon_img/addressCheck.png';
    }



}

const cancelButtons = document.getElementsByClassName('cancel-btn');

if(cancelButtons){
    Array.from(cancelButtons).forEach(cancelButton => {
        cancelButton.addEventListener('click', () => {

            //상품 제외로 인한 결제 금액 변경

            let productPrice = 0;
            let productPriceComp;
            if(cancelButton.previousElementSibling.children[1].children[0].children[0] == null){
                let text = cancelButton.previousElementSibling.children[1].children[0].textContent
                productPrice = parseInt(text.substring(0, text.length - 1).replace(/,/g, ''));
                productPriceComp = cancelButton.previousElementSibling.children[1].children[0];
            }
            else{
                let text = cancelButton.previousElementSibling.children[1].children[0].children[0].textContent
                productPrice = parseInt(text.substring(0, text.length - 1).replace(/,/g, ''));
                productPriceComp = cancelButton.previousElementSibling.children[1].children[0].children[0];
            }

            let discount = 0;
            let prevTotalProductPrice = parseInt(document.getElementById('productTotalPrice').textContent.substring(1, document.getElementById('productTotalPrice').textContent.length - 1).replace(/,/g, ''));
            let prevTotalDiscount = parseInt(document.getElementById('discountTotal').textContent.substring(1, document.getElementById('discountTotal').textContent.length - 1).replace(/,/g, ''));

            if(productPriceComp.parentElement.nextElementSibling !== null){
                let discountPriceText = productPriceComp.parentElement.nextElementSibling.textContent;
                let discountPrice = parseInt(discountPriceText.substring(0, discountPriceText.length - 1).replace(/,/g, ''));
                discount = productPrice - discountPrice;
            }

            document.getElementById('productTotalPrice').textContent = '+' + toWon(prevTotalProductPrice - productPrice);
            document.getElementById('discountTotal').textContent = '-' + toWon(prevTotalDiscount - discount);

            let prevProductTotalPrice = parseInt(document.getElementById('productTotalPrice').textContent.substring(1, document.getElementById('productTotalPrice').textContent.length - 1).replace(/,/g, ''));

            console.log(prevProductTotalPrice);
            if((prevTotalProductPrice - productPrice) - (prevTotalDiscount - discount) < 50000 && prevProductTotalPrice >= 50000){
                let afterDeliveryFee = parseInt(document.getElementById('deliveryFee').textContent.substring(1, document.getElementById('deliveryFee').textContent.length - 1).replace(/,/g, '')) + 2500;
                document.getElementById('deliveryFee').textContent = '+' + toWon(afterDeliveryFee);
                document.getElementById('deliveryFeeInfo').textContent = toWon(afterDeliveryFee);
            }

            let afterTotalPaymentPrice = parseInt(document.getElementById('productTotalPrice').textContent.substring(1, document.getElementById('productTotalPrice').textContent.length - 1).replace(/,/g, '')) + parseInt(document.getElementById('deliveryFee').textContent.substring(1, document.getElementById('deliveryFee').textContent.length - 1).replace(/,/g, '')) - parseInt(document.getElementById('discountTotal').textContent.substring(1, document.getElementById('discountTotal').textContent.length - 1).replace(/,/g, ''));
            document.getElementById('totalPaymentPrice').textContent = toWon(afterTotalPaymentPrice);
            document.getElementById('payment-btn').textContent = toWon(afterTotalPaymentPrice) + ' 결제하기';


            cancelButton.parentElement.remove();

            if(Array.from(document.getElementsByClassName('productView')).length === 0){
                document.getElementById('totalPaymentPrice').textContent = '0원';
                document.getElementById('payment-btn').textContent = '0원 결제하기';
                document.getElementById('deliveryFeeInfo').textContent = '0원';
                document.getElementById('deliveryFee').textContent = '0원';
                wait(10)
                .then(() => {
                    alert('모든 상품을 제거하여 메인으로 이동합니다.');
                    location.replace('/');
                });
            }
        });
    });
}

const paymentSelectBar = document.getElementById('paymentSelectBar');

if(paymentSelectBar){

    let paymentInfoView = Array.from(document.getElementsByClassName('paymentInfoView'));
    paymentSelectBar.addEventListener('change', () => {
        paymentInfoView.forEach(view => view.classList.add('d-hidden'));
        paymentSelectBar.parentElement.parentElement.children[3].children[2].value = '-1';
        paymentSelectBar.parentElement.parentElement.children[3].children[5].value = '';
        paymentSelectBar.parentElement.parentElement.children[4].children[2].value = '-1';
        paymentSelectBar.parentElement.parentElement.children[4].children[5].value = '';
        paymentSelectBar.parentElement.parentElement.children[5].children[2].value = '-1';
        paymentSelectBar.parentElement.parentElement.children[5].children[5].value = '-1';
        paymentSelectBar.parentElement.parentElement.children[5].children[8].value = '';
        paymentSelectBar.parentElement.parentElement.children[6].children[2].value = '-1';
        paymentSelectBar.parentElement.parentElement.children[6].children[5].value = '';


        if(paymentSelectBar.value === '무통장입금'){
            paymentInfoView[0].classList.remove('d-hidden');
        }
        else if(paymentSelectBar.value === '계좌이체'){
            paymentInfoView[1].classList.remove('d-hidden');
        }
        else if(paymentSelectBar.value === '신용/체크 카드'){
            paymentInfoView[2].classList.remove('d-hidden');
        }
        else{
            paymentInfoView[3].classList.remove('d-hidden');
        }
    });
}

function wait(ms) {
  return new Promise(resolve => setTimeout(resolve, ms));
}


//계산서 초기값 세팅
const productPrices = document.getElementsByClassName('productPrice');

if(productPrices){
    let totalProductPrice = 0;
    let totalDiscount = 0;

    Array.from(productPrices).forEach(productPriceText => {

        let productPrice = parseInt(productPriceText.textContent.substring(0, productPriceText.textContent.length - 1).replace(/,/g, ''));
        console.log(productPrice);

        totalProductPrice += productPrice;
        console.log(totalProductPrice);

        if(productPriceText.parentElement.nextElementSibling !== null){
            let discountPriceText = productPriceText.parentElement.nextElementSibling.textContent;
            let discountPrice = parseInt(discountPriceText.substring(0, discountPriceText.length - 1).replace(/,/g, ''));
            let discount = productPrice - discountPrice;

            totalDiscount += discount;
        }
    });

    document.getElementById('productTotalPrice').textContent = '+' + toWon(totalProductPrice);
    document.getElementById('discountTotal').textContent = '-' + toWon(totalDiscount);

    let deliveryFee = 0;
    if(hasDefaultAddress){
        if(totalProductPrice - totalDiscount < 50000){
            deliveryFee += 2500;
        }
        if(document.getElementById('sample6_address').value.includes('제주특별자치도')){
            deliveryFee += 5000;
        }

        console.log(deliveryFee);

    }
    document.getElementById('deliveryFee').textContent = '+' + toWon(deliveryFee);
    document.getElementById('deliveryFeeInfo').textContent = toWon(deliveryFee);
    document.getElementById('totalPaymentPrice').textContent = toWon(totalProductPrice - totalDiscount + deliveryFee);


    let paymentPrice = totalProductPrice - totalDiscount + deliveryFee;

    document.getElementById('payment-btn').textContent = toWon(paymentPrice) + ' 결제하기';
}

function paymentFormChange(paymentPrice){
    let paymentPriceText = '';
    paymentPrice += '';

    while(paymentPrice.length > 3){
        console.log(paymentPrice.substring(paymentPrice.length - 3, paymentPrice.length));
        paymentPriceText += ',' + paymentPrice.substring(paymentPrice.length - 3, paymentPrice.length);
        paymentPrice = paymentPrice.substring(0, paymentPrice.length - 3);
        console.log(paymentPrice);
        console.log(paymentPriceText);
    }
    return paymentPrice + paymentPriceText + '원';
}

const paymentButton = document.getElementById('payment-btn');

if(paymentButton){
    paymentButton.addEventListener('click', () => {

        console.log('click');

        //결제 버튼 클릭시 주소정보 확인
        if(document.getElementById('recipientName').value === '' || document.getElementById('sample6_addressAndPostcode').value === '' || document.getElementById('sample6_detailAddress').value === '' || (document.getElementById('deliveryRequest').value === '택배함' && document.getElementById('deliveryBoxNum').value === '') || !isMatchingRegex(phoneTop.value.trim(), "^\\d{3}$") || !isMatchingRegex(phoneMiddle.value.trim(), "^\\d{4}$") || !isMatchingRegex(phoneBottom.value.trim(), "^\\d{4}$")){
            document.getElementById('addressForm').classList.remove('border-green');
            document.getElementById('addressForm').classList.add('border-red');
            window.scrollTo({
                top: 0,
                behavior: 'smooth'
            });

            if(document.getElementById('deliveryRequest').value === '택배함' && document.getElementById('deliveryBoxNumView').value === ''){
                document.getElementById('deliveryBoxNumView').classList.remove('border');
                document.getElementById('deliveryBoxNumView').classList.add('border-red');
            }

            if(document.getElementById('recipientName').value === ''){
                document.getElementById('recipientName').parentElement.classList.add('border-red');
            }

            if(document.getElementById('sample6_addressAndPostcode').value === '' || document.getElementById('sample6_detailAddress').value === ''){
                document.getElementById('sample6_addressAndPostcode').parentElement.parentElement.parentElement.classList.add('border-red');
            }


            if(!isMatchingRegex(phoneTop.value.trim(), "^\\d{3}$") || !isMatchingRegex(phoneMiddle.value.trim(), "^\\d{4}$") || !isMatchingRegex(phoneBottom.value.trim(), "^\\d{4}$")){
                phoneTop.parentElement.parentElement.classList.add('border-red');
            }

            return ;
        }

        if(document.getElementById('paymentSelectBar').value === '-1'){
            document.getElementById('paymentSelectBar').parentElement.parentElement.classList.add('border-red');
            document.getElementById('paymentSelectBar').parentElement.parentElement.classList.remove('border-green');
            return ;
        }
        else if(document.getElementById('paymentSelectBar').value === '무통장입금'){
            if(document.getElementById('paymentBank1').value === '-1'){
                document.getElementById('paymentSelectBar').parentElement.parentElement.classList.add('border-red');
                document.getElementById('paymentSelectBar').parentElement.parentElement.classList.remove('border-green');
                return ;
            }
            if(document.getElementById('paymentDepositor').value === ''){
                document.getElementById('paymentSelectBar').parentElement.parentElement.classList.add('border-red');
                document.getElementById('paymentSelectBar').parentElement.parentElement.classList.remove('border-green');
                document.getElementById('paymentDepositor').classList.add('border-red');
                document.getElementById('paymentDepositor').classList.remove('border');
                return ;
            }
        }
        else if(document.getElementById('paymentSelectBar').value === '계좌이체'){
            if(document.getElementById('paymentBank2').value === '-1'){
                document.getElementById('paymentSelectBar').parentElement.parentElement.classList.add('border-red');
                document.getElementById('paymentSelectBar').parentElement.parentElement.classList.remove('border-green');
                return ;
            }
            if(!isMatchingRegex(document.getElementById('paymentAccountNumber').value.trim(), "^\\d{14}$")){
                document.getElementById('paymentSelectBar').parentElement.parentElement.classList.add('border-red');
                document.getElementById('paymentSelectBar').parentElement.parentElement.classList.remove('border-green');
                document.getElementById('paymentAccountNumber').classList.add('border-red');
                document.getElementById('paymentAccountNumber').classList.remove('border');
                return ;
            }
        }
        else if(document.getElementById('paymentSelectBar').value === '신용/체크 카드'){
            if(document.getElementById('paymentCard').value === '-1' || document.getElementById('paymentInstallment').value === '-1'){
                document.getElementById('paymentSelectBar').parentElement.parentElement.classList.add('border-red');
                document.getElementById('paymentSelectBar').parentElement.parentElement.classList.remove('border-green');
                return ;
            }
            if(!isMatchingRegex(document.getElementById('paymentCardNumber').value.trim(), "^\\d{16}$")){
                document.getElementById('paymentSelectBar').parentElement.parentElement.classList.add('border-red');
                document.getElementById('paymentSelectBar').parentElement.parentElement.classList.remove('border-green');
                document.getElementById('paymentCardNumber').classList.add('border-red');
                document.getElementById('paymentCardNumber').classList.remove('border');
                return ;
            }
        }
        else if(document.getElementById('paymentSelectBar').value === '핸드폰 결제'){
            if(document.getElementById('paymentPhoneCompany').value === '-1'){
                document.getElementById('paymentSelectBar').parentElement.parentElement.classList.add('border-red');
                document.getElementById('paymentSelectBar').parentElement.parentElement.classList.remove('border-green');
                return ;
            }
            if(!isMatchingRegex(document.getElementById('paymentMicropaymentPhone').value.trim(), "^\\d{16}$")){
                document.getElementById('paymentSelectBar').parentElement.parentElement.classList.add('border-red');
                document.getElementById('paymentSelectBar').parentElement.parentElement.classList.remove('border-green');
                document.getElementById('paymentMicropaymentPhone').classList.add('border-red');
                document.getElementById('paymentMicropaymentPhone').classList.remove('border');
                return ;
            }
        }

        //약관 동의여부 확인
        if(!document.getElementById('allAgreementCheckBox').checked){
            console.log('sdf');
            document.getElementById('allAgreementCheckBox').parentElement.parentElement.classList.remove('border-green');
            document.getElementById('allAgreementCheckBox').parentElement.parentElement.classList.add('border-red');

            return ;
        }
        else{
            console.log('else');
        }

        if(toList(Array.from(document.getElementsByClassName('cartId'))).length === 0){
            console.log('size = 0');
            let body = JSON.stringify({
                productId : toList(Array.from(document.getElementsByClassName('productId')))[0]
            });

            httpRequest(`/user/payment/cleanList`, 'POST', body)
            .then(response => {
                if(response.ok){

                    return response.json();
                }
                else{
                    alert('error2');
                    throw new error('error');
                }
            })
            .then(data => {
                console.log(data);
                if(data.products.length !== 0){
                    let text = '';
                    data.products.forEach(product => {
                        text += product.productName + '\n';
                        document.getElementById('productId' + product.productId).parentElement.children[4].click();
                    });
                    alert('품절된 상품 있음\n' + text + '\n\n 해당 상품이 결제 명세서 및 장바구니에서 제외되었습니다.\n 결제 명세서 내역을 재확인바람니다.');
                }
                else{
                    //결제 요청
                    if(toList(Array.from(document.getElementsByClassName('productId'))).length !== 0){
                        let body = JSON.stringify({
                            paymentPrice : document.getElementById('totalPaymentPrice').textContent.substring(0, document.getElementById('totalPaymentPrice').textContent.length - 1).replace(/,/g, ''),
                            paymentType : document.getElementById('paymentSelectBar').value,
                            paymentBank : getPaymentBank(document.getElementById('paymentBank1'), document.getElementById('paymentBank2')),
                            paymentDepositor : document.getElementById('paymentDepositor').value,
                            paymentAccountNumber : document.getElementById('paymentAccountNumber').value,
                            paymentCard : document.getElementById('paymentCard').value,
                            paymentInstallment : document.getElementById('paymentInstallment').value,
                            paymentCardNumber : document.getElementById('paymentCardNumber').value,
                            paymentPhoneCompany : document.getElementById('paymentPhoneCompany').value,
                            paymentMicropaymentPhone : document.getElementById('paymentMicropaymentPhone').value,
                            paymentPostalCode : document.getElementById('sample6_postcode').value,
                            paymentAddress : document.getElementById('sample6_address').value + ', ' + document.getElementById('sample6_detailAddress').value,
                            paymentRecipientName : document.getElementById('recipientName').value,
                            paymentRecipientPhone : document.getElementById('phoneTop').value + document.getElementById('phoneMiddle').value + document.getElementById('phoneBottom').value,
                            deliveryRequest : document.getElementById('deliveryRequest').value,
                            deliveryBoxNum : document.getElementById('deliveryBoxNum').value,
                            frontDoorSecret : document.getElementById('frontDoorSecret').value,
                            deliveryFee : document.getElementById('deliveryFeeInfo').textContent.substring(0, document.getElementById('deliveryFeeInfo').textContent.length - 1).replace(/,/g, ''),
                            cartIds : toList(Array.from(document.getElementsByClassName('cartId'))),
                            productIds : toList(Array.from(document.getElementsByClassName('productId')))
                        });

                        httpRequest(`/user/payment`, 'POST', body)
                        .then(response => {
                            if(response.ok){
                                alert('상품구매가 완료되었습니다.');
                                location.replace('/');
                            }
                            else{
                                alert('error3');
                            }
                        });
                    }
                }
            });
        }
        else{
            let body = JSON.stringify({
                cartIds : toList(Array.from(document.getElementsByClassName('cartId')))
            });

            httpRequest(`/user/cart/cleanList`, 'DELETE', body)
            .then(response => {
                if(response.ok){
                    return response.json();
                }
                else{
                    alert('error4');
                    throw new error('error');
                }
            })
            .then(data => {
                console.log(data);
                if(data.products.length !== 0){
                    let text = '';
                    data.products.forEach(product => {
                        text += product.productName + '\n';
                        document.getElementById('productId' + product.productId).parentElement.children[4].click();
                    });
                    alert('품절된 상품 있음\n' + text + '\n\n 해당 상품이 결제 명세서 및 장바구니에서 제외되었습니다.\n 결제 명세서 내역을 재확인바람니다.');
                }
                else{
                    //결제요청
                    if(toList(Array.from(document.getElementsByClassName('productId'))).length !== 0){
                        let body = JSON.stringify({
                            paymentPrice : document.getElementById('totalPaymentPrice').textContent.substring(0, document.getElementById('totalPaymentPrice').textContent.length - 1).replace(/,/g, ''),
                            paymentType : document.getElementById('paymentSelectBar').value,
                            paymentBank : getPaymentBank(document.getElementById('paymentBank1'), document.getElementById('paymentBank2')),
                            paymentDepositor : document.getElementById('paymentDepositor').value,
                            paymentAccountNumber : document.getElementById('paymentAccountNumber').value,
                            paymentCard : document.getElementById('paymentCard').value,
                            paymentInstallment : document.getElementById('paymentInstallment').value,
                            paymentCardNumber : document.getElementById('paymentCardNumber').value,
                            paymentPhoneCompany : document.getElementById('paymentPhoneCompany').value,
                            paymentMicropaymentPhone : document.getElementById('paymentMicropaymentPhone').value,
                            paymentPostalCode : document.getElementById('sample6_postcode').value,
                            paymentAddress : document.getElementById('sample6_address').value + ', ' + document.getElementById('sample6_detailAddress').value,
                            paymentRecipientName : document.getElementById('recipientName').value,
                            paymentRecipientPhone : document.getElementById('phoneTop').value + document.getElementById('phoneMiddle').value + document.getElementById('phoneBottom').value,
                            deliveryRequest : document.getElementById('deliveryRequest').value,
                            deliveryBoxNum : document.getElementById('deliveryBoxNum').value,
                            frontDoorSecret : document.getElementById('frontDoorSecret').value,
                            deliveryFee : document.getElementById('deliveryFeeInfo').textContent.substring(0, document.getElementById('deliveryFeeInfo').textContent.length - 1).replace(/,/g, ''),
                            cartIds : toList(Array.from(document.getElementsByClassName('cartId'))),
                            productIds : toList(Array.from(document.getElementsByClassName('productId')))
                        });

                        httpRequest(`/user/payment`, 'POST', body)
                        .then(response => {
                            if(response.ok){
                                alert('상품구매가 완료되었습니다.');
                                location.replace('/');
                            }
                            else{
                                alert('error5');
                            }
                        });
                    }
                }
            });
        }
    });
}

function getPaymentBank(comp1, comp2){
    if(comp1.value === '' && comp2.value === ''){
        return null;
    }
    return comp1.value === '' ? comp2.value : comp1.value;
}

//쇼핑몰 이용약관 동의시 check 박스 상태 변경
const termsAndConditionsAgreementCheckBox = document.getElementById('termsAndConditionsAgreementCheckBox');

if(termsAndConditionsAgreementCheckBox){
    termsAndConditionsAgreementCheckBox.addEventListener('change', () => {
        if(termsAndConditionsAgreementCheckBox.checked && privacyAgreementCheckBox.checked){
            allAgreementCheckBox.checked = true;
        }
        else{
            allAgreementCheckBox.checked = false;
        }
    });
}

//개인정보 수집 밎 이용 동의시 check 박스 상태 변경
const privacyAgreementCheckBox = document.getElementById('privacyAgreementCheckBox');

if(privacyAgreementCheckBox){
    privacyAgreementCheckBox.addEventListener('change', () => {
        if(termsAndConditionsAgreementCheckBox.checked && privacyAgreementCheckBox.checked){
            allAgreementCheckBox.checked = true;
        }
        else{
            allAgreementCheckBox.checked = false;
        }
    });
}

//모든 약관 동의시 check박스 상태 변경
const allAgreementCheckBox = document.getElementById('allAgreementCheckBox');

if(allAgreementCheckBox){
    allAgreementCheckBox.addEventListener('click', () => {
        if(allAgreementCheckBox.checked){
            if(!termsAndConditionsAgreementCheckBox.checked){
                termsAndConditionsAgreementCheckBox.checked = true;
            }
            if(!privacyAgreementCheckBox.checked){
                privacyAgreementCheckBox.checked = true;
            }
        }
        else{
            if(termsAndConditionsAgreementCheckBox.checked){
                termsAndConditionsAgreementCheckBox.checked = false;
            }
            if(privacyAgreementCheckBox.checked){
                privacyAgreementCheckBox.checked = false;
            }
        }
    });
}

//
const addressForm = document.getElementById('addressForm');

if(addressForm){
    addressForm.addEventListener('click', () => {
        allFormBorderRewind();
        addressForm.classList.remove('border-red');
        addressForm.classList.add('border-green');
    });
}

const paymentTypeForm = document.getElementById('paymentTypeForm');

if(paymentTypeForm){
    paymentTypeForm.addEventListener('click', () => {
        allFormBorderRewind();

        document.getElementById('paymentDepositor').classList.remove('border-red');
        document.getElementById('paymentDepositor').classList.add('border');

        document.getElementById('paymentAccountNumber').classList.remove('border-red');
        document.getElementById('paymentAccountNumber').classList.add('border');

        document.getElementById('paymentCardNumber').classList.remove('border-red');
        document.getElementById('paymentCardNumber').classList.add('border');

        document.getElementById('paymentMicropaymentPhone').classList.remove('border-red');
        document.getElementById('paymentMicropaymentPhone').classList.add('border');

        paymentTypeForm.classList.remove('border-red');
        paymentTypeForm.classList.add('border-green');
    });
}

const productForm = document.getElementById('productForm');

if(productForm){
    productForm.addEventListener('click', () => {
        allFormBorderRewind();
        productForm.classList.remove('border-red');
        productForm.classList.add('border-green');
    });
}

const paymentForm = document.getElementById('paymentForm');

if(paymentForm){
    paymentForm.addEventListener('click', () => {
        allFormBorderRewind();

        paymentForm.classList.remove('border-red');
        paymentForm.classList.add('border-green');
    });
}

const agreementForm = document.getElementById('agreementForm');

if(agreementForm){
    agreementForm.addEventListener('click', () => {
        allFormBorderRewind();
        agreementForm.classList.remove('border-red');
        agreementForm.classList.add('border-green');
    });
}

function allFormBorderRewind(){
    addressForm.classList.remove('border-green');
    paymentTypeForm.classList.remove('border-green');
    productForm.classList.remove('border-green');
    paymentForm.classList.remove('border-green');
    agreementForm.classList.remove('border-green');
}

const termsAndConditionsAgreementViewButton = document.getElementById('termsAndConditionsAgreementView-btn');

if(termsAndConditionsAgreementViewButton){
    termsAndConditionsAgreementViewButton.addEventListener('click', () => {
        document.getElementById('termsAndConditionsAgreementViewButton').click();
    });
}

const privacyAgreementViewButton = document.getElementById('privacyAgreementView-btn');

if(privacyAgreementViewButton){
    privacyAgreementViewButton.addEventListener('click', () => {
        document.getElementById('privacyAgreementViewButton').click();
    });
}


function termsAndConditionsAgreement(comp){
    if(!document.getElementById('termsAndConditionsAgreementCheckBox').checked){
        document.getElementById('termsAndConditionsAgreementCheckBox').click();
    }
    comp.previousElementSibling.click();
}

function PrivacyAgreement(comp){
    if(!document.getElementById('privacyAgreementCheckBox').checked){
        document.getElementById('privacyAgreementCheckBox').click();
    }
    comp.previousElementSibling.click();
}

const PrivacyAgreementButton = document.getElementById('PrivacyAgreement-btn');

if(PrivacyAgreementButton){
    PrivacyAgreementButton.addEventListener('click', () => {
        document.getElementById('termsAndConditionsAgreementCheckBox').click();
        PrivacyAgreementButton.previousElementSibling.click();
    })
}

//카드자릿수 확인
const paymentCardNumber = document.getElementById('paymentCardNumber');

if(paymentCardNumber){
    paymentCardNumber.addEventListener('blur', () => {
        console.log(parseInt(paymentCardNumber.value.trim()));
        if(parseInt(paymentCardNumber.value.trim()).toString() === paymentCardNumber.value.trim() && paymentCardNumber.value.trim().length === 16){
            paymentCardNumber.classList.remove('border-red');
        }
        else{
            paymentCardNumber.classList.add('border-red');
        }
    });
}

//계좌번호 자리수 확인
const paymentAccountNumber = document.getElementById('paymentAccountNumber');

if(paymentAccountNumber){
    paymentAccountNumber.addEventListener('blur', () => {

        if(parseInt(paymentAccountNumber.value.trim()).toString() === paymentAccountNumber.value.trim() && paymentAccountNumber.value.trim().length === 14){
            paymentAccountNumber.classList.remove('border-red');
        }
        else{
            paymentAccountNumber.classList.add('border-red');
        }
    });
}

//핸드폰결제 전화번호 확인
const paymentMicropaymentPhone = document.getElementById('paymentMicropaymentPhone');

if(paymentMicropaymentPhone){
    paymentMicropaymentPhone.addEventListener('blur', () => {

        if(parseInt(paymentMicropaymentPhone.value.trim()).toString() === paymentMicropaymentPhone.value.trim() && paymentMicropaymentPhone.value.trim().length === 11){
            paymentMicropaymentPhone.classList.remove('border-red');
        }
        else{
            paymentMicropaymentPhone.classList.add('border-red');
        }
    });
}

function prevCompClick(comp){
    comp.previousElementSibling.click();
}

//택배 수령자 전화번호 확인
const phoneTop = document.getElementById('phoneTop');

if(phoneTop){
    phoneTop.addEventListener('blur', () => {
        if(isMatchingRegex(phoneTop.value.trim(), "^\\d{3}$") && isMatchingRegex(phoneMiddle.value.trim(), "^\\d{4}$") && isMatchingRegex(phoneBottom.value.trim(), "^\\d{4}$")){
            phoneTop.parentElement.parentElement.classList.remove('border-red');
        }
    });
}

const phoneMiddle = document.getElementById('phoneMiddle');

if(phoneMiddle){
    phoneMiddle.addEventListener('blur', () => {
        if(isMatchingRegex(phoneTop.value.trim(), "^\\d{3}$") && isMatchingRegex(phoneMiddle.value.trim(), "^\\d{4}$") && isMatchingRegex(phoneBottom.value.trim(), "^\\d{4}$")){
            console.log('clicksdfsdf');
            phoneTop.parentElement.parentElement.classList.remove('border-red');
        }
    });
}

const phoneBottom = document.getElementById('phoneBottom');

if(phoneBottom){
    phoneBottom.addEventListener('blur', () => {
        if(isMatchingRegex(phoneTop.value.trim(), "^\\d{3}$") && isMatchingRegex(phoneMiddle.value.trim(), "^\\d{4}$") && isMatchingRegex(phoneBottom.value.trim(), "^\\d{4}$")){
            phoneTop.parentElement.parentElement.classList.remove('border-red');
        }
    });
}

//택배 수령자이름 확인
const recipientName = document.getElementById('recipientName');

if(recipientName){
    recipientName.addEventListener('change', () => {
        if(recipientName.value !== null){
            recipientName.parentElement.classList.remove('border-red');
        }
    });
}

//택배 배송지 확인
const sample6AddressAndPostcode = document.getElementById('sample6_addressAndPostcode');
const sample6DetailAddress = document.getElementById('sample6_detailAddress');

if(sample6AddressAndPostcode && sample6DetailAddress){
    sample6DetailAddress.addEventListener('change', () => {
        if(sample6AddressAndPostcode.value !== '' && sample6DetailAddress.value !== ''){
            sample6AddressAndPostcode.parentElement.parentElement.parentElement.classList.remove('border-red');
        }
    });
}

//배송지 없을경우 기본세팅
if(document.getElementById('selectBar')){
    if(document.getElementById('selectBar').length === 2){
        document.getElementById('selectBar').children[0].selected = true;
        document.getElementById('addressForm').children[2].classList.remove('d-hidden');
        document.getElementById('addressInfoOpen-btn').classList.remove('d-hidden');
        document.getElementById('addressInfoOpen-btn').textContent = '⋀';
    }
}

