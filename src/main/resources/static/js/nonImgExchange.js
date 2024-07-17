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

const firstImg = document.getElementsByTagName('img');
console.log(firstImg);

Array.from(firstImg).forEach(img => {
    checkImageExists(img, '/images/logo_img/noimg_big.gif');
    img.classList.add('border');
});