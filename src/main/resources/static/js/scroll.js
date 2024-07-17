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