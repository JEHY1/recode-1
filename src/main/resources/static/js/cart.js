//http 요청
function httpRequest(url, method, body){
    return fetch(url, {
        method: method,
        headers: {
            "Content-Type" : "application/json"
        },
        body: body
    });
}

//component들의 value 를 리스트로 변환
function toList(components){
    let list = [];
    components.forEach(component => list.push(component.value));
    return list;
}

//component중 선택여부에 해당하는 것만 리스트로 반환
function selectToList(selects, values){
    let list = [];
    selects.forEach((select, index) => {
        if(select.checked){
            list.push(values[index].value);
        }
    });

    return list;
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

//모두 선택 체크박스
const allCheckBox = document.getElementById('allCheckBox');

if(allCheckBox){
    allCheckBox.addEventListener('click', () => {

        //모두 선택 체크박스가 체크되어있을 경우 다른 모든 항목의 체크박스에 대해 체크되도록 함
        if(allCheckBox.checked){
            if(productCheckBox){
                Array.from(productCheckBox).forEach(checkBox => {
                    if(!checkBox.checked){
                        checkBox.click();
                    }
                });
            }
        }
        //반대
        else{
            if(productCheckBox){
                Array.from(productCheckBox).forEach(checkBox => {
                    if(checkBox.checked){
                        checkBox.click();
                    }
                });
            }
        }
    });
}

//각각 상품의 체크박스
const productCheckBox = document.getElementsByClassName('productCheckBox');

if(productCheckBox){
    //각각의 체크박스에 event 등록
    Array.from(productCheckBox).forEach(checkBox => {
        checkBox.addEventListener('click', () => {
            //가격 측정을 위한 변수
            let discountPrice = checkBox.nextElementSibling.nextElementSibling.nextElementSibling.children[0].children[1].textContent;
            discountPrice = parseInt(discountPrice.substring(0, discountPrice.length - 1).replace(/,/g, ''));
            let regularPrice = checkBox.nextElementSibling.nextElementSibling.textContent;
            regularPrice = parseInt(regularPrice.substring(0, regularPrice.length - 1).replace(/,/g, ''));
            let discount = regularPrice - discountPrice;



            let totalRegularPrice = document.getElementById('totalRegularPrice').textContent;
            totalRegularPrice = parseInt(totalRegularPrice.substring(0, totalRegularPrice.length - 1).replace(/,/g, ''));
            let totalDiscount = document.getElementById('totalDiscount').textContent;
            totalDiscount = parseInt(totalDiscount.substring(0, totalDiscount.length - 1).replace(/,/g, ''));
            let totalDiscountPrice = document.getElementById('totalDiscountPrice').textContent;
            totalDiscountPrice = parseInt(totalDiscountPrice.substring(0, totalDiscountPrice.length - 1).replace(/,/g, ''));

            console.log("totalDiscountPrice : " + totalDiscount);

            //체크일 경우
            if(checkBox.checked){
                //가격 측정
                document.getElementById('totalRegularPrice').textContent = toWon(totalRegularPrice + regularPrice);
                document.getElementById('totalDiscount').textContent = toWon(totalDiscount + discount);
                document.getElementById('totalDiscountPrice').textContent = toWon(totalDiscountPrice + discountPrice);

                //html form 안에 input component 만들어 넣기
                let productForm = document.createElement('input');
                productForm.name = 'cartId';
                productForm.id = 'cartIdForm' + checkBox.previousElementSibling.value;
                productForm.type = 'hidden';
                productForm.value = checkBox.previousElementSibling.value;

                document.getElementById('selectedProductField').appendChild(productForm);

                let allCheckFlag = 1;

                //각각 체크박스 확인해서 모두 체크 상태일시 allCheckBox 체크하기
                Array.from(productCheckBox).forEach(checkBox2 => {
                    if(!checkBox2.checked){
                        allCheckFlag = 0;
                    }
                });
                if(allCheckFlag === 1){
                    allCheckBox.checked = true;
                }
            }
            //아닐경우
            else{
                //html form 안에 input component 제거
                console.log('cartIdForm' + checkBox.previousElementSibling.value);
                console.log(document.getElementById('cartIdForm' + checkBox.previousElementSibling.value));
                let comp = document.getElementById('cartIdForm' + checkBox.previousElementSibling.value);
                comp.remove();

                //가격 계산
                document.getElementById('totalRegularPrice').textContent = toWon(totalRegularPrice - regularPrice);
                document.getElementById('totalDiscount').textContent = toWon(totalDiscount - discount);
                document.getElementById('totalDiscountPrice').textContent = toWon(totalDiscountPrice - discountPrice);

                //allCheckBox 체크해제
                allCheckBox.checked = false;
            }
        });
    });
}

//장바구니 제외하기 버튼
const cartDeleteButton = document.getElementsByClassName('cartDelete-btn');

if(cartDeleteButton){
    Array.from(cartDeleteButton).forEach(button => {
        button.addEventListener('click', () => {
            console.log('click');

            //body 에 cartId를 넣어 http 요청
            let body = JSON.stringify({
                cartId: button.previousElementSibling.previousElementSibling.value
            });
            httpRequest(`/user/cart`, 'DELETE', body)
            .then(response => {
                if(response.ok){
                    alert('장바구니에서 삭제되었습니다.');
                    location.replace('/user/cart');
                }
                else{
                    alert('error');
                }
            });
        });
    });
}

//품절된 상품 장바구니에서 제거 버튼
const deleteSoldProductButton = document.getElementById('deleteSoldProduct-btn');

if(deleteSoldProductButton){
    deleteSoldProductButton.addEventListener('click', () => {
        //장바구니의 모든 cartId를 리스트 형식으로 body 에 담아 http 요청
        let body = JSON.stringify({
            cartIds : toList(Array.from(document.getElementsByClassName('cartIds')))
        });
        httpRequest(`/user/cart/cleanList`, 'DELETE', body)
        .then(response => {
            if(response.ok){
                return response.json();
            }
            else{
                alert('error');
                throw new error('error');
            }
        })
        .then(data => {
            console.log(data);
            if(data.products.length === 0){
                alert('품절된 상품이 없습니다.');
            }
            else{
                let text = '';
                data.products.forEach(product => text += product.productName + '\n');
                alert('품절된 상품 있음\n' + text + '\n\n 해당 상품이 장바구니에서 제외되었습니다.');
                location.replace('/user/cart');
            }
        });
    });
}

//선택 상품 장바구니에서 제거 버튼
const deleteSelectProductButton = document.getElementById('deleteSelectProduct-btn');

if(deleteSelectProductButton){
    deleteSelectProductButton.addEventListener('click', () => {

        //선택된것 리스트로 변환
        let cartIds = selectToList(Array.from(document.getElementsByClassName('productCheckBox')), Array.from(document.getElementsByClassName('cartIds')));
        //선택된것이 있는지 확인
        if(cartIds.length === 0){
            alert('상품을 선택하세요');
            return;
        }

        //선택된 cartId를 포함하는 http요청
        let body = JSON.stringify({
            cartIds : cartIds
        });
        httpRequest(`/user/cartList`, 'DELETE', body)
        .then(response => {
            if(response.ok){
                alert('삭제 완');
                location.replace('/user/cart');
            }
            else{
                alert('error');
            }
        });
    });
}

//전체 주문하기 버튼
const allOrderButton = document.getElementById('allOrder-btn');

if(allOrderButton){
    allOrderButton.addEventListener('click', () => {
        //모두선택 체크박스가 선택되어 있지 않은 경우 선택하고, 선택주문 클릭
        if(!allCheckBox.checked){
            allCheckBox.click();
        }
        document.getElementById('selectOrder-btn').click();
    });
}

//선택 주문 버튼
const selectOrderButton = document.getElementById('selectOrder-btn');

if(selectOrderButton){
    selectOrderButton.addEventListener('click', () => {
        let cartIds = selectToList(Array.from(document.getElementsByClassName('productCheckBox')), Array.from(document.getElementsByClassName('cartIds')));
        console.log(cartIds);

        if(cartIds.length === 0){
            alert('상품을 선택하세요');
            return;
        }
        document.getElementById('selectedProductField').submit();

    });
}

//원화 표시
if(document.getElementsByClassName('price')){
    Array.from(document.getElementsByClassName('price')).forEach(comp => comp.textContent = toWon(comp.textContent));
}

//페이지 입장시 기본으로 모든 상품이 선택상태
allCheckBox.click();

