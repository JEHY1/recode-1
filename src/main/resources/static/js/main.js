$(function(){

    // slide 사진
    setInterval(autoSlide, 3000);
    function autoSlide() {
      $("#slideBox > ul").animate({ "margin-left": "-1903px" }, 800, function () {
        $("#slideBox > ul").css({ "margin-left": "0" });
        $("#slideBox > ul > li:first-child").insertAfter("#slideBox > ul > li:last-child");
      });
    };
    // slide 하단바
    autoSlideBar();
    setInterval(autoSlideBar, 9000);
    function autoSlideBar() {
      setTimeout(function(){
        $("#slideBox > div > span").animate({ "width": "400px" }, 800);
      }, 3000);
      setTimeout(function(){
        $("#slideBox > div > span").animate({ "width": "600px" }, 800);
      }, 6000);
      setTimeout(function(){
        $("#slideBox > div > span").animate({ "width": "200px" }, 800);
      }, 9000);
    };
    // 신상품 slide
    setInterval(newBoxAutoSlide, 5000);
    function newBoxAutoSlide() {
      $("#newBox > .productBox").animate({ "margin-left": "-443px" }, 1000, function () {
        $("#newBox > .productBox").css({ "margin-left": "0" });
        $("#newBox > .productBox > div:first-child").insertAfter("#newBox > .productBox > div:last-child");
      });
    };

    // 신상품
    for(let i = 1; i <= $("#newBox > .productBox > div").length; i++) {
        let price = $("#newBox > .productBox > div:nth-child(" + i + ") > p:nth-of-type(3) > span:nth-child(1)").text();
        let sales = $("#newBox > .productBox > div:nth-child(" + i + ") > p:nth-of-type(3) > span:nth-child(2)").text();
        let discount = Math.floor((price-sales)/price*100); // 할인률 소수점 버림
        // 할인률 계산
        $("#newBox > .productBox > div:nth-child(" + i + ") > p:nth-of-type(3) > span:nth-child(3)").text(discount + "%");
        // 천단위 콤마
        $("#newBox > .productBox > div:nth-child(" + i + ") > p:nth-of-type(3) > span:nth-child(1)").text(price.replace(/\B(?=(\d{3})+(?!\d))/g, ","));
        $("#newBox > .productBox > div:nth-child(" + i + ") > p:nth-of-type(3) > span:nth-child(2)").text(sales.replace(/\B(?=(\d{3})+(?!\d))/g, ","));
    }
    // 베스트
    for(let i = 1; i <= $("#bestBox > .productBox > div").length; i++) {
        let price = $("#bestBox > .productBox > div:nth-child(" + i + ") > p:nth-of-type(3) > span:nth-child(1)").text();
        let sales = $("#bestBox > .productBox > div:nth-child(" + i + ") > p:nth-of-type(3) > span:nth-child(2)").text();
        let discount = Math.floor((price-sales)/price*100); // 할인률 소수점 버림
        // 할인률 계산
        $("#bestBox > .productBox > div:nth-child(" + i + ") > p:nth-of-type(3) > span:nth-child(3)").text(discount + "%");
        // 천단위 콤마
        $("#bestBox > .productBox > div:nth-child(" + i + ") > p:nth-of-type(3) > span:nth-child(1)").text(price.replace(/\B(?=(\d{3})+(?!\d))/g, ","));
        $("#bestBox > .productBox > div:nth-child(" + i + ") > p:nth-of-type(3) > span:nth-child(2)").text(sales.replace(/\B(?=(\d{3})+(?!\d))/g, ","));
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
        };
    }

    const imgs = document.getElementsByTagName('img');
    console.log(imgs);
    if(imgs){
        Array.from(imgs).forEach(img => {
            checkImageExists(img, '/images/logo_img/noimg_big.gif')
            img.classList.add('border');
        });
    }
});


