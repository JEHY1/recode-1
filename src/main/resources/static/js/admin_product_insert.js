$(function() {

    // 상품 대표사진 미리보기
    $("#mainImg").on("change", function(event) {

        var file = event.target.files[0];
        if(file != null) {
            var reader = new FileReader();
            reader.onload = function(e) {
                $("#mainImgShow").attr("src", e.target.result);
            }
            reader.readAsDataURL(file);
        }
        else {
            $("#mainImgShow").attr("src", "");
        }

    });

    // 상품 상세사진 미리보기
    $("#detailImg").on("change", function(event) {

        var files = event.target.files;
        if(files.length != 0) {
            $(".detailImgShow").remove();
            for(let i = 0; i < files.length; i++) {
                var reader = new FileReader();
                reader.onload = function(e) {
                    $("#detailImgShowBox").append($("<img>", {"src" : e.target.result, "class" : "detailImgShow"}));
                }
                reader.readAsDataURL(files[i]);
            }
        }
        else {
            $(".detailImgShow").remove();
        }

    });
});


