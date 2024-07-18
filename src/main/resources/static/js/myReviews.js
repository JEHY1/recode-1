function getUrlParameter(name) {
    const urlParams = new URLSearchParams(window.location.search);
    return urlParams.get(name);
}

const writeableReviewTap = document.getElementById('writableReviews');
const writtenReviewTap = document.getElementById('writtenReviews');

if(writeableReviewTap && writtenReviewTap){
    console.log(getUrlParameter('tap'));

    if(getUrlParameter('tap') !== '1'){
        writtenReviewTap.classList.add('d-hidden');
        document.getElementById('currentTap').value = 0;
    }
    else{
        writeableReviewTap.classList.add('d-hidden');
        document.getElementById('currentTap').value = 1;
    }
}

const showWritableReviewButton = document.getElementById('showWritableReview-btn');

if(showWritableReviewButton){
    showWritableReviewButton.addEventListener('click', () => {
        console.log('click');
        writeableReviewTap.classList.remove('d-hidden');
        writtenReviewTap.classList.add('d-hidden');
        document.getElementById('currentTap').value = 0;
    });
}

const showWrittenReviewButton = document.getElementById('showWrittenReview-btn');

if(showWrittenReviewButton){
    showWrittenReviewButton.addEventListener('click', () => {
        console.log('click');
        writeableReviewTap.classList.add('d-hidden');
        writtenReviewTap.classList.remove('d-hidden');
        document.getElementById('currentTap').value = 1;
    });
}






//페이지
const writablePageButtonField = document.getElementById('writableReviewsPageButtonField');

if(writablePageButtonField){
    let currentPageNum = getUrlParameter('page1') === null ? 0 : parseInt(getUrlParameter("page1"));
    let totalPageSize = parseInt(document.getElementById('writableReviewTotalPageSize').value);
    let currentPageGroupNum = parseInt(currentPageNum / 5);
    let totalPageGroupNum = parseInt(totalPageSize / 5);

    let repeat = 5;
    if(currentPageGroupNum === totalPageGroupNum){
        repeat = totalPageSize % 5;
    }

    for(let i = 0; i < repeat; i++){
        console.log(currentPageGroupNum * 5 + i + 1);

        let pageButton = document.createElement('button');
        pageButton.textContent = currentPageGroupNum * 5 + i + 1;
        pageButton.setAttribute('class', 'p-0 page-btn d-flex align-items-center justify-content-center mx-1');

        let divWrap = document.createElement('div');


        let param = '';
        param += '&tap=0';
        param += getUrlParameter("page2") !== null ? '&page2=' + getUrlParameter("page2") : '';

        if(currentPageNum + 1 === currentPageGroupNum  * 5 + i + 1){
            pageButton.classList.add('selectedPage-btn');
        }
        else{
            pageButton.setAttribute('onclick', "location.href='/myReviews?" + param + "&page1=" + (currentPageGroupNum * 5 + i) + "'");
        }

        writablePageButtonField.appendChild(divWrap);
        divWrap.appendChild(pageButton);

        if(currentPageNum === 0){
            document.getElementById('writablePrevPage-btn').setAttribute('disabled', '');
        }

        if(currentPageNum === totalPageSize - 1){
            document.getElementById('writableNextPage-btn').setAttribute('disabled', '');
        }
    }
}

const writablePrevPageButton = document.getElementById('writablePrevPage-btn');

if(writablePrevPageButton){
    let param = '';
    param += getUrlParameter("page2") !== null ? '&page2=' + getUrlParameter("page2") : '';
    param += '&tap=0';
    writablePrevPageButton.setAttribute('onclick', "location.href='/myReviews?" + param + "&page1=" + (parseInt(getUrlParameter('page1')) - 1) + "'");
}

const writableNextPageButton = document.getElementById('writableNextPage-btn');

if(writableNextPageButton){
    let param = '';
    param += getUrlParameter("page2") !== null ? '&page2=' + getUrlParameter("page2") : '';
    param += '&tap=0';
    writableNextPageButton.setAttribute('onclick', "location.href='/myReviews?" + param + "&page1=" + (parseInt(getUrlParameter('page1') === null ? 0 : getUrlParameter('page1')) + 1) + "'");
}

(parseInt(getUrlParameter('page2') === null ? 0 : getUrlParameter('page2')) + 1)




const writtenPageButtonField = document.getElementById('writtenReviewsPageButtonField');

if(writtenPageButtonField){
    let currentPageNum = getUrlParameter('page2') === null ? 0 : parseInt(getUrlParameter("page2"));
    let totalPageSize = parseInt(document.getElementById('writtenReviewTotalPageSize').value);
    let currentPageGroupNum = parseInt(currentPageNum / 5);
    let totalPageGroupNum = parseInt(totalPageSize / 5);

    let repeat = 5;
    if(currentPageGroupNum === totalPageGroupNum){
        repeat = totalPageSize % 5;
    }

    for(let i = 0; i < repeat; i++){
        console.log(currentPageGroupNum * 5 + i + 1);

        let pageButton = document.createElement('button');
        pageButton.textContent = currentPageGroupNum * 5 + i + 1;
        pageButton.setAttribute('class', 'p-0 page-btn d-flex align-items-center justify-content-center mx-1');

        let divWrap = document.createElement('div');


        let param = '';
        param += '&tap=1';
        param += getUrlParameter("page1") !== null ? '&page1=' + getUrlParameter("page1") : '';


        if(currentPageNum + 1 === currentPageGroupNum  * 5 + i + 1){
            pageButton.classList.add('selectedPage-btn');
        }
        else{
            pageButton.setAttribute('onclick', "location.href='/myReviews?" + param + "&page2=" + (currentPageGroupNum * 5 + i) + "'");
        }

        writtenPageButtonField.appendChild(divWrap);
        divWrap.appendChild(pageButton);

        if(currentPageNum === 0){
            document.getElementById('writtenPrevPage-btn').setAttribute('disabled', '');
        }

        if(currentPageNum === totalPageSize - 1){
            document.getElementById('writtenNextPage-btn').setAttribute('disabled', '');
        }
    }
}

const writtenPrevPageButton = document.getElementById('writtenPrevPage-btn');

if(writtenPrevPageButton){
    let param = '';
    param += getUrlParameter("page1") !== null ? '&page1=' + getUrlParameter("page1") : '';
    param += '&tap=1';
    writtenPrevPageButton.setAttribute('onclick', "location.href='/myReviews?" + param + "&page2=" + (parseInt(getUrlParameter('page2')) - 1) + "'");
}

const writtenNextPageButton = document.getElementById('writtenNextPage-btn');

if(writtenNextPageButton){
    let param = '';
    param += getUrlParameter("page1") !== null ? '&page1=' + getUrlParameter("page1") : '';
    param += '&tap=1';
    writtenNextPageButton.setAttribute('onclick', "location.href='/myReviews?" + param + "&page2=" + (parseInt(getUrlParameter('page2') === null ? 0 : getUrlParameter('page2')) + 1) + "'");
}

if(document.getElementsByClassName('writableReviewNum')){
    let writableReviewGroupNum = parseInt(getUrlParameter('page1') === null ? 0 : getUrlParameter('page1'));
    Array.from(document.getElementsByClassName('writableReviewNum')).forEach((comp, index) => {comp.textContent = 10 * writableReviewGroupNum + index + 1})
}

if(document.getElementsByClassName('writtenReviewNum')){
    let writtenReviewGroupNum = parseInt(getUrlParameter('page2') === null ? 0 : getUrlParameter('page2'));
    Array.from(document.getElementsByClassName('writtenReviewNum')).forEach((comp, index) => {comp.textContent = 10 * writtenReviewGroupNum + index + 1})
}

