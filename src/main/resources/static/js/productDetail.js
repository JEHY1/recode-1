function httpRequest(url, method, body){
    return fetch(url, {
        method: method,
        headers: {
            "Content-Type" : "application/json"
        },
        body: body,
    });
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

const productDetailInfoButton = document.getElementById('product-detail-info-btn');

if(productDetailInfoButton){
    productDetailInfoButton.addEventListener('click', () => {
        productDetailInfoButton.classList.remove('border-left-gainsboro', 'border-top-gainsboro', 'color-gainsboro');
        productDetailInfoButton.classList.add('border-left-black', 'border-top-black', 'border-bottom-white', 'color-black');

        productSaleInfoButton.classList.remove('border-left-ginsboro', 'border-top-black', 'border-left-gainsboro', 'border-bottom-white', 'color-black');
        productSaleInfoButton.classList.add('border-left-black', 'border-top-gainsboro', 'border-right-gainsboro', 'color-gainsboro');

        productQnAButton.classList.remove('border-top-black', 'border-right-black', 'border-bottom-white');
        productQnAButton.classList.add('border-top-gainsboro', 'border-right-gainsboro', 'color-gainsboro');

        document.getElementById('product-detail-info-tap').classList.remove('d-hidden');
        document.getElementById('product-sale-info-tap').classList.add('d-hidden');
        document.getElementById('product-QnA-tap').classList.add('d-hidden');

    });
}

const productSaleInfoButton = document.getElementById('product-sale-info-btn');

if(productSaleInfoButton){
    productSaleInfoButton.addEventListener('click', () => {
        productDetailInfoButton.classList.remove('border-left-black', 'border-top-black', 'border-bottom-white', 'color-black');
        productDetailInfoButton.classList.add('border-left-gainsboro', 'border-top-gainsboro', 'color-gainsboro');

        productSaleInfoButton.classList.remove('border-top-gainsboro', 'border-left-gainsboro', 'border-right-gainsboro', 'color-gainsboro');
        productSaleInfoButton.classList.add('border-top-black', 'border-right-black', 'border-left-black', 'border-bottom-white', 'color-black');

        productQnAButton.classList.remove('border-top-black', 'border-right-black', 'border-bottom-white');
        productQnAButton.classList.add('border-top-gainsboro', 'border-right-gainsboro', 'color-gainsboro');

        document.getElementById('product-sale-info-tap').classList.remove('d-hidden');
        document.getElementById('product-detail-info-tap').classList.add('d-hidden');
        document.getElementById('product-QnA-tap').classList.add('d-hidden');

    });
}

const productQnAButton = document.getElementById('product-QnA-btn');

if(productQnAButton){
    productQnAButton.addEventListener('click', () => {
        productDetailInfoButton.classList.remove('border-left-black', 'border-top-black', 'border-bottom-white', 'color-black');
        productDetailInfoButton.classList.add('border-left-gainsboro', 'border-top-gainsboro', 'color-gainsboro');

        productSaleInfoButton.classList.remove('border-left-black', 'border-top-black', 'border-bottom-white', 'border-right-gainsboro', 'color-black');
        productSaleInfoButton.classList.add('border-left-gainsboro', 'border-top-gainsboro', 'border-right-black', 'color-gainsboro');

        productQnAButton.classList.remove('border-top-gainsboro', 'border-right-gainsboro', 'color-gainsboro');
        productQnAButton.classList.add('border-top-black', 'border-right-black', 'border-bottom-white', 'color-black');

        document.getElementById('product-sale-info-tap').classList.add('d-hidden');
        document.getElementById('product-detail-info-tap').classList.add('d-hidden');
        document.getElementById('product-QnA-tap').classList.remove('d-hidden');
    });
}

const QnATitleButtons = document.getElementsByClassName('QnATitle-btn');

if(QnATitleButtons){
    Array.from(QnATitleButtons).forEach(button => {
        button.addEventListener('click', () => {

            Array.from(QnATitleButtons).forEach(button2 => {
                if(button2 !== button && button2.nextElementSibling !== null){
                    button2.nextElementSibling.classList.add('d-hidden')
                    button2.classList.remove('bg-gainsboro');
                }
            });

            if(button.nextElementSibling != null){
               if(button.nextElementSibling.classList.contains('d-hidden')){
                   button.nextElementSibling.classList.remove('d-hidden');
                   button.classList.add('bg-gainsboro');
               }
               else{
                   button.nextElementSibling.classList.add('d-hidden');
                   button.classList.remove('bg-gainsboro');
               }
            }
        });
    });
}

if(document.getElementById('productDiscountPrice').value !== ''){
    document.getElementById('salePercentage').textContent = parseInt(100 - parseInt(document.getElementById('productDiscountPrice').value) * 100 / parseInt(document.getElementById('productRegularPrice').value)) + '%';
}

const moveTopButton = document.getElementById('move-top-btn');

if(moveTopButton){
    moveTopButton.addEventListener('click', () => {
        window.scrollTo({
            top: 0,
            behavior: 'smooth'
        });
    });
}

const moveBottomButton = document.getElementById('move-bottom-btn');

if(moveBottomButton){
    moveBottomButton.addEventListener('click', () => {
        console.log('click');
        window.scrollTo({
            top: document.body.scrollHeight,
            behavior: 'smooth'
        });
    });
}

const pages = document.getElementsByClassName('pages');

if(pages.length !== 0){
    pages[0].classList.remove('d-hidden');
    pages[0].setAttribute('id', 'QnA-show');
}

const QnAPageUpButton = document.getElementById('QnAPageUp-btn');

if(QnAPageUpButton){
    QnAPageUpButton.addEventListener('click', () => {
        if(document.getElementById('QnA-show').nextElementSibling != null){
            Array.from(QnATitleButtons).forEach(button => button.nextElementSibling.classList.add('d-hidden'));

            let currentPage = document.getElementById('QnA-show');
            currentPage.setAttribute('id', '');
            currentPage.classList.add('d-hidden');
            currentPage.nextElementSibling.setAttribute('id', 'QnA-show');
            document.getElementById('QnA-show').classList.remove('d-hidden');

            let pageCount = document.getElementById('QnAPage');
            pageCount.textContent = parseInt(pageCount.textContent) + 1;
            parseInt(document.getElementById('QnAPage').textContent)

        }
        else{
            alert('마지막 페이지입니다.');
        }
    });
}

const QnAPageDownButton = document.getElementById('QnAPageDown-btn');

if(QnAPageDownButton){
    QnAPageDownButton.addEventListener('click', () => {
        if(document.getElementById('QnAPage').textContent !== '1'){
            Array.from(QnATitleButtons).forEach(button => button.nextElementSibling.classList.add('d-hidden'));

            let currentPage = document.getElementById('QnA-show');
            currentPage.setAttribute('id', '');
            currentPage.classList.add('d-hidden');

            currentPage.previousElementSibling.setAttribute('id', 'QnA-show');
            document.getElementById('QnA-show').classList.remove('d-hidden');

            let pageCount = document.getElementById('QnAPage');
            pageCount.textContent = parseInt(pageCount.textContent) - 1;
            parseInt(document.getElementById('QnAPage').textContent)
        }
        else{
            alert('이전 페이지가 없습니다.')
        }
    });
}

const cartButton = document.getElementById('cart-btn');

if(cartButton){
    cartButton.addEventListener('click', () => {
        let body = JSON.stringify({
            productId : document.getElementById('productId').value
        });

        httpRequest(`/user/addCart`, 'POST', body)
        .then(response => {
            if(response.ok){
                alert('장바구니에 등록되었습니다.')
            }
            else{
                alert('error');
            }
        });
    });
}

//바로 구매시 결제 페이지 이동
const buyButton = document.getElementById('buy-btn');

if(buyButton){
    buyButton.addEventListener('click', () => {
        document.getElementById('requestPaymentView').submit();
    });
}

//문의 등록
const qnaSubmitButton = document.getElementById('qnaSubmit-btn');

if(qnaSubmitButton){
    qnaSubmitButton.addEventListener('click', () => {

        let title = document.getElementById('submitQnaTitle').value;
        let content = document.getElementById('submitQnaContent').value;

        if(title === ''){
            alert('제목을 작성해주세요.');
            return;
        }

        if(content === ''){
            alert('내용을 작성해주세요.');
            return;
        }

        let body = JSON.stringify({
            query: '(' + title + ', ' + content + ') 해당 질문이 가방쇼핑몰에 적절한 질문인지 \'적절\', \'부적절\' 로 대답해줘 질문을 이해할 수 없으면 \'부적절\'로 대답해줘'
        });

        httpRequest(`/GPTQuery`, 'POST', body).
        then(response => {
            console.log(response);
            if(response.ok){
                return response.json();
            }
            else{
                alert('error');
            }
        })
        .then(data => {
            console.log(data);
            console.log(data.choices[0].message.content);
            if(data.choices[0].message.content === '적절'){
                body = JSON.stringify({
                    productId : document.getElementById('productId').value,
                    qnaTitle: title,
                    qnaContent: content,
                    qnaSecret: document.getElementById('qnaSecretCheckBox').checked ? 1 : 0
                });

                httpRequest(`/user/qna/submit`, 'POST', body)
                .then(response => {
                    if(response.ok){
                        alert('등록되었습니다.');
                        location.reload();
                    }
                });
            }
            else{
                alert('부적절한 내용이 확인되었습니다.\n내용을 확인해주세요.');
            }
        });
    });
}

function resetQnAForm(){
    document.getElementById('submitQnaTitle').value = '';
    document.getElementById('submitQnaContent').value = '';
}

function checkLogin(){
    let returnVal = false;

    httpRequest(`/checkLogin`, 'GET', null)
    .then(response => {
        console.log(response);
        if(response.ok){
            return response.json();
        }
    })
    .then(data => {
        console.log(data);
        console.log(data.value);
        if(data.value === false){
            location.href = "/login";
        }
        else{
            returnVal = true;
        }
    });
    return returnVal;
}

const loginButton = document.getElementById('login-btn');

if(loginButton){
    loginButton.addEventListener('click', () => {
        let body = JSON.stringify({
            username : document.getElementById('username').value,
            password : document.getElementById('password').value
        });

        console.log(body);

        httpRequest(`/login`, 'POST', body)
        .then(response => {
            console.log(response);
        });
    });
}

//페이지 로딩시 설정
//원화 표시로 변경
if(document.getElementsByClassName('price')){
    Array.from(document.getElementsByClassName('price')).forEach(comp => comp.textContent = toWon(comp.textContent));
}

//헤더 border-bottom 적용
if(document.getElementsByTagName('header')){
    document.getElementsByTagName('header')[0].classList.add('border-b');
}

//이미지 없을 시 특정 이미지 표시
function checkImageExists(imgElement, fallbackSrc) {
    const img = new Image();
    img.src = imgElement.src;
    img.onload = function() {
        // 이미지가 정상적으로 로드되었으므로 아무 작업도 하지 않음
    };
    img.onerror = function() {
        // 이미지가 없으므로 대체 이미지로 설정
        imgElement.src = fallbackSrc;
        img.classList.add('border');
    };
}

const imgs = document.getElementsByTagName('img');
console.log(imgs);
if(imgs){
    Array.from(imgs).forEach(img => {
        checkImageExists(img, '/images/logo_img/noimg_big.gif')
    });
}