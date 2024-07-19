$(function() {

    // search 열기
    $("#iconBox > ul > li:nth-child(1) > a").on("click", function() {
        $("#search").css("display","block");
        $("#search input[type=search]").focus();
    });
    // search 닫기
    $("#search > a").on("click", function() {
        $("#search").css("display","none");
    });

    // nav 열기
    $("#iconBox > ul > li:nth-child(3) > a").on("click", function() {
        $("nav").css("display","block");
    });
    // nav 닫기
    $("nav > a").on("click", function() {
        $("nav").css("display","none");
    });

    // notice 닫기
    $("#notice > div > a").on("click", function() {
        $("#notice").css("display","none");
    });

    // scroll - header 색
    $(window).on("scroll", function() {
        if($(window).scrollTop() == 0) {
            $("header").css("background-color","#ffffff00");
            if(window.location.pathname == '/') {
                $("header").css("border-bottom","none");
            }
        }
        else {
            $("header").css("background-color","#ffffffee");
            if(window.location.pathname == '/') {
                $("header").css("border-bottom","1px solid #ccc");
            }
        }
    });

    // 메인 페이지이면 header 밑에 선 제거
    if(window.location.pathname == '/') {
        $("header").css("border-bottom","none");
    }

    //이미지 없을 시 특정 이미지 표시
    const imgs = document.getElementsByTagName('img');
    console.log(imgs);
    if(imgs){
        Array.from(imgs).forEach(img => {
            checkImageExists(img, '/images/logo_img/noimg_big.gif')
        });
    }

    //검색기능
    const searchBar = document.getElementById('searchBar');

    if(searchBar){
        searchBar.addEventListener('focus', () => document.getElementById('searchedField').classList.remove('d-hidden'));
        searchBar.addEventListener('blur', () => {
            wait(100)
            .then(() => document.getElementById('searchedField').classList.add('d-hidden'));
        });

        searchBar.addEventListener('keydown', (event) => {
            if (event.key === 'Enter') {
                // Enter 키가 눌렸을 때 수행할 동작
                event.preventDefault(); // 기본 Enter 동작을 막습니다 (필요 시)
                console.log(event.key);
                document.getElementById('productSearch-btn').click();
            }
        });

        searchBar.addEventListener('input', () => {

            if(searchBar.value !== ''){
                let body = JSON.stringify({
                    productName : searchBar.value
                });

                httpRequest(`/getIncludeNameList`, 'POST', body)
                .then(response => {
                    if(response.ok){
                        return response.json();
                    }
                })
                .then(data => {

                    removeAllChildNodes(document.getElementById('searchedField'));
                    console.log(data);
                    data.forEach(result => {
                        let fullText = document.createElement('div');
                        fullText.setAttribute('class', 'row mx-auto ps-2 productNameField');
                        fullText.setAttribute('onclick', 'getProductName(this)');

                        let frontText = document.createElement('div');
                        frontText.setAttribute('class', 'col-auto p-0');
                        frontText.textContent = result.frontText;

                        let searchText = document.createElement('div');
                        searchText.setAttribute('class', 'col-auto p-0 color-blue');
                        searchText.textContent = result.searchText;

                        let endText = document.createElement('div');
                        endText.setAttribute('class', 'col-auto p-0');
                        endText.textContent = result.endText;

                        document.getElementById('searchedField').appendChild(fullText);
                        fullText.appendChild(frontText);
                        fullText.appendChild(searchText);
                        fullText.appendChild(endText);

                    });

                });
            }
            else{
                removeAllChildNodes(document.getElementById('searchedField'));
            }
        });
    }

    const productSearchButton = document.getElementById('productSearch-btn');

    if(productSearchButton){
        productSearchButton.addEventListener('click', () => {
            location.href = '/product/productGroup?searchText=' + document.getElementById('searchBar').value;
        });
    }

    $.ajax({
        url:'/user/cart/count',
        type : "post",
        dataType : 'text',
        success : function(data) {
            $(".cartCnt").text("(" + data + ")");
        }
    });
});

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
    };
}

function wait(ms) {
  return new Promise(resolve => setTimeout(resolve, ms));
}

function httpRequest(url, method, body){
    return fetch(url, {
        method: method,
        headers: {
            "Content-Type" : "application/json"
        },
        body: body
    });
}

function removeAllChildNodes(parent) {
    while (parent.firstChild) {
        parent.removeChild(parent.firstChild);
    }
}

function getProductName(Comp){
    document.getElementById('searchBar').value = Comp.children[0].textContent + Comp.children[1].textContent + Comp.children[2].textContent;
    document.getElementById('searchedField').classList.add('d-hidden');
}