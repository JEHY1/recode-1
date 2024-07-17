const testButton = document.getElementById('test-btn');

if(testButton){
    testButton.addEventListener('click', () => {
        document.getElementById('testCheckBox').focus();
    });
}