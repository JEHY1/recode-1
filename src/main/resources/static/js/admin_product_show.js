$(function() {
    // 천단위 콤마
    let price = $("#showBox > ul > li:nth-child(4) > p:nth-child(2)").text();
    let sales = $("#showBox > ul > li:nth-child(5) > p:nth-child(2)").text();
    $("#showBox > ul > li:nth-child(4) > p:nth-child(2)").text(price.replace(/\B(?=(\d{3})+(?!\d))/g, ",") + '원');
    if(sales != '-') { // '할인가'가 있을 때
        $("#showBox > ul > li:nth-child(5) > p:nth-child(2)").text(sales.replace(/\B(?=(\d{3})+(?!\d))/g, ",") + '원');
    }
});