document.addEventListener('DOMContentLoaded', function() {
    var table = document.getElementById('userReviewList');
    var rows = table.getElementsByTagName('tr');

    for (var i = 0; i < rows.length; i++) {
        var firstTd = rows[i].getElementsByTagName('td')[0];
        firstTd.innerText = i + 1;
    }
});