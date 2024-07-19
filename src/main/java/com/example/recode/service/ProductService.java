package com.example.recode.service;

import com.example.recode.domain.History;
import com.example.recode.domain.PaymentDetail;
import com.example.recode.domain.Product;
import com.example.recode.domain.ProductImg;
import com.example.recode.dto.ProductDetailViewResponse;
import com.example.recode.dto.ProductNameForm;
import com.example.recode.dto.UploadProductRequest;
import com.example.recode.repository.HistoryRepository;
import com.example.recode.repository.ProductImgRepository;
import com.example.recode.repository.ProductRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.security.Principal;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProductService {

    //application.properties 의 app.upload.dir 값을 사용
    @Value("${app.upload.dir}")
    private String uploadDir;
    private final ProductRepository productRepository;
    private final ProductImgRepository productImgRepository;
    private final UserService userService;
    private final HistoryService historyService;

    public Product findProductByProductId(long productId) {
        return productRepository.findById(productId)
                .orElseThrow(() -> new IllegalArgumentException("not found product"));
    }

    public List<ProductImg> findProductImgByProductId(long productId){
        return productImgRepository.findByProductId(productId)
                .orElseThrow(() -> new IllegalArgumentException("not found imgs"));
    }

    //상품 아이디에 해당하는 상품중 품절된 상품 리스트를 반환
    public List<Product> findProductByProductIdInAndProductSold(List<Long> productIds){

        return productRepository.findAllByProductIdInAndProductSold(productIds, 1)
                .orElseThrow(() -> new IllegalArgumentException("not found product"));
    }

    // productId -> ProductDetailViewResponse
    @Transactional
    public ProductDetailViewResponse getProductInfoByProductId(long productId, Principal principal){
        List<String> productDetailImgs = findProductImgByProductId(productId).stream().map(productImg -> productImg.getProductImgSrc()).toList();
        Product product = findProductByProductId(productId);

        // 상품 조회수 증가
        product.IncProductViewCount();

        //히스토리 등록
        if(principal != null){
            long userId = userService.getUserId(principal);
            List<History> historyList = historyService.findAllByUserId(userId);
            History history = historyService.findByProductIdAndUserId(productId, userId);

            if(history != null){

                historyService.delete(history);
            }

            if(historyList.size() < 12){
                historyService.save(History.builder()
                        .productId(productId)
                        .userId(userId)
                        .build()
                );
            }
            else{
                historyService.delete(historyList.get(0));
                historyService.save(History.builder()
                        .productId(productId)
                        .userId(userId)
                        .build()
                );
            }
        }


        return ProductDetailViewResponse.builder()
                .productId(product.getProductId())
                .productName(product.getProductName())
                .productModel(product.getProductModel())
                .productRepImg(product.getProductRepresentativeImgSrc())
                .type(product.getProductType())
                .color(product.getProductColor())
                .size(product.getProductSize())
                .material(product.getProductMaterial())
                .regularPrice(product.getProductRegularPrice())
                .discountPrice(product.getProductDiscountPrice() == null ? null : product.getProductDiscountPrice())
                .productDetailImgs(productDetailImgs)
                .productSold(product.getProductSold())
                .build();
    }

    //상품 등록&수정
    @Transactional
    public Product uploadProduct(UploadProductRequest request){

        Product product = null;
        if(request.getProductId() != null) {
            product = findProductByProductId(request.getProductId()).updateProduct(request); // 상품정보 수정
        }

        else {
            product = productRepository.save(Product.builder()
                    .productName(request.getProductName())
                    .productModel(request.getProductModel())
                    .productCategory(request.getProductCategory())
                    .productRegularPrice(request.getProductRegularPrice())
                    .productDiscountPrice(request.getProductDiscountPrice() != null ? request.getProductDiscountPrice() : null)
                    .productSize(request.getProductSize())
                    .productMaterial(request.getProductMaterial())
                    .productRepresentativeImgSrc("")
                    .productSold(0)
                    .productColor(request.getProductColor())
                    .productType(request.getProductType())
                    .productViewCount(0)
                    .build());
        }

        //상품 등록시 대표이미지로 설정한 이미지파일 이름
        String originalFileName = request.getProductRepImg().getOriginalFilename();
        //파일 확장자 추출
        int extensionIndex = originalFileName.lastIndexOf(".") == -1 ? 0 : originalFileName.lastIndexOf(".");
        String extension = originalFileName.substring(extensionIndex);

        if(!request.getProductRepImg().isEmpty()) {
            //db에 상품 /image/productRep/product{상품아이디}RepImg.확장자 로 db저장
            product.updateRepImgSrc("/images/productRep/product" + product.getProductId() + "RepImg" + extension);

            //파일 업로드 (application.properties 에 저장한 경로/images/productRep/)
            fileUpload(request.getProductRepImg(), product.getProductId(), extension, "productRep", "RepImg", null);
        }

        // productExtImg 등록할 경우 저장
        if(!request.getProductExtImg().get(0).isEmpty()) {
            // 상품정보 수정 시 새로 업로드 하는 파일이 있으면 DB 에서 기존 productImg 삭제
            if(request.getProductId() != null) {
                findProductImgByProductId(request.getProductId()).forEach(productImg -> productImgRepository.deleteById(productImg.getProductImgId()));
            }

            //상세 이미지 파일들 업로드 및 db 저장
            int imgNum = 1;
            for(MultipartFile img : request.getProductExtImg()){
                originalFileName = img.getOriginalFilename();
                extensionIndex = originalFileName.lastIndexOf(".");
                extension = originalFileName.substring(extensionIndex);

                //파일 업로드 (application.properties 에 저장한 경로/images/productDetail/)
                fileUpload(img, product.getProductId(), extension, "productDetail", "detailImg", imgNum);

                productImgRepository.save(ProductImg.builder()
                        .productId(product.getProductId())
                        .productImgSrc("/images/productDetail/product" + product.getProductId() + "DetailImg" + imgNum++ + extension)
                        .build());

            }
        }
        return product;
    }

    //파일 업로드(파일, 상품 아이디, 파일 확장자, 추가 경로, 파일 이름, 파일 번호)
    public void fileUpload(MultipartFile multipartFile, long productId, String extension, String dir, String fileNick, Integer num) {

        //경로 만들기
        Path copyOfLocation = Paths.get(uploadDir + File.separator + dir + File.separator + "product" + productId + fileNick + (num == null ? "" : num) + extension);
        try {
            // inputStream 사용
            // copyOfLocation 저장위치
            // 기존 파일이 존재할 경우 덮어쓰기
            Files.copy(multipartFile.getInputStream(), copyOfLocation, StandardCopyOption.REPLACE_EXISTING);

        } catch (IOException e) {
            throw new IllegalArgumentException("Could not store file : " + multipartFile.getOriginalFilename());
        }
    }

    public List<Product> findProductAllByProductIdIn(List<Long> productIds){
        return productRepository.findAllByProductIdIn(productIds)
                .orElse(null);
    }

    public List<Product> newProduct() {
        return productRepository.newProduct()
                .orElseThrow(() -> new IllegalArgumentException("not found product"));
    }

    public Page<Product> findProductByProductCategoryAndProductSold(String productCategory, int productSold, Pageable pageable){

        return productRepository.findAllByProductCategoryAndProductSold(productCategory, productSold, pageable)
                .orElseThrow(() -> new IllegalArgumentException("not found product"));
    }

    public Page<Product> findProductByProductNameContainingAndProductSold(String searchText, int productSold, Pageable pageable){

        return productRepository.findAllByProductNameContainingAndProductSold(searchText, productSold, pageable)
                .orElseThrow(() -> new IllegalArgumentException("not found product"));

    }

    public Page<Product> searchProduct(String searchText, String productCategory, Pageable pageable){

        if(searchText != null){
            return findProductByProductNameContainingAndProductSold(searchText, 0, pageable);
        }
        else if(productCategory != null){
            return findProductByProductCategoryAndProductSold(productCategory, 0, pageable);
        }

        return null;
    }

    //최근 본 상품목록 리스트
    public List<Product> getRecentViewProductList(Principal principal){
        List<History> historyList = historyService.findAllByUserId(userService.getUserId(principal));
        historyList.sort((o1, o2) -> (int)o2.getHistoryId() - (int)o1.getHistoryId());
        System.err.println(historyList);
        List<Product> list = new ArrayList<>();

        historyList.forEach(history -> {
            Product product = findProductByProductId(history.getProductId());
            list.add(product);
        });

//        return findProductAllByProductIdIn(historyList.stream().mapToLong(History::getProductId).boxed().collect(Collectors.toList()));
        return list;
    }

    public List<Product> findByProductNameContaining(String productName) { // productName 을 포함하는 List<Product>
        return productRepository.findByProductNameContaining(productName).orElse(null);
    }

    public List<ProductNameForm> searchProductNameContaining(String productName){
        List<Product> list = findByProductNameContaining(productName);

        Set<ProductNameForm> nameList = new HashSet<>(); //같은 결과 제거용
        list.forEach(product -> {

            //결과를 검색단어를 기준으로 검색단어 앞 문자열, 검색단어, 검색단어 뒤 문자열 3개로 나눔
            if(product.getProductName().contains(productName) && product.getProductSold() == 0){
                int index = product.getProductName().indexOf(productName);
                nameList.add(ProductNameForm.builder()
                        .frontText(product.getProductName().substring(0, index))
                        .searchText(productName)
                        .endText(product.getProductName().substring(index + productName.length()))
                        .build());
            }
        });

        return nameList.stream().toList();
    }

    @Transactional
    public void updateHoldById(Long productId) { // productId로 Product 판매보류
        historyService.deleteByProductId(productId); // productId로 History 삭제
        findProductByProductId(productId).updateSold(2); // productId로 Product 판매보류
    }

    @Transactional
    public void updateHoldByIds(List<Long> productIds) { // productIds 리스트로 Product 판매보류
        for (Long productId : productIds) {
            historyService.deleteByProductId(productId); // productId로 History 삭제
            findProductByProductId(productId).updateSold(2); // productId로 Product 판매보류
        }
    }
    public List<Product> findByUserSold(int group) {
        return productRepository.findByProductSold(group)
                .orElseThrow(() -> new IllegalArgumentException("not found product"));
    }

    public Page<Product> productList(Integer group, Pageable pageable) { // group 별 상품 조회
        Page<Product> userList = null;
        if(group == 3) { // 전체상품(3) 조회
            userList = productRepository.findAll(pageable);
        }
        else { // 판매중(0), 판매완료(1), 판매보류(2) 조회
            userList = productRepository.findByProductSold(group, pageable)
                    .orElseThrow(() -> new IllegalArgumentException("not found product"));
        }
        return userList;
    }








    public Page<Product> productSearchList(Integer group, Integer category, String searchKeyword, Pageable pageable) { // category 선택 후 검색해서 페이징 처리한 Page<Product> 가져옴
        Page<Product> productSearchList = null;
        if(group == 3) { // 전체상품(3) 조회
            if(category == 1) {  // category 가 '상품명'일 때
                productSearchList = productRepository.findByProductNameContaining(searchKeyword, pageable)
                        .orElseThrow(() -> new IllegalArgumentException("not found product")); // productName 로 검색해서 페이징 처리한 Page<Product>
            }
            else if(category == 2) {  // category 가 '모델명'일 때
                productSearchList = productRepository.findByProductModelContaining(searchKeyword, pageable)
                        .orElseThrow(() -> new IllegalArgumentException("not found product")); //  productModel 로 검색해서 페이징 처리한 Page<Product>
            }
            else if(category == 3) { // category 가 '판매가'일 때
                try {
                    productSearchList = productRepository.findByProductRegularPrice(Integer.parseInt(searchKeyword), pageable)
                            .orElseThrow(() -> new IllegalArgumentException("not found product")); //  productRegularPrice 로 검색해서 페이징 처리한 Page<Product>
                } catch (NumberFormatException e) {
                    productSearchList = productList(group, pageable);
                }
            }
            else if(category == 4) { // category 가 '할인가'일 때
                try {
                    productSearchList = productRepository.findByProductDiscountPrice(Integer.parseInt(searchKeyword), pageable)
                            .orElseThrow(() -> new IllegalArgumentException("not found product")); //  productDiscountPrice 로 검색해서 페이징 처리한 Page<Product>
                } catch (NumberFormatException e) {
                    productSearchList = productList(group, pageable);
                }
            }
            else if(category == 5) { // category 가 '등록일'일 때
                // searchKeyword 가 포함된 List<Product> 만듬
                List<Product> keywordList = new ArrayList<>();
                productRepository.findAll().forEach(product -> {
                    if(product.getProductRegistrationDate().toString().contains(searchKeyword)){
                        keywordList.add(product);
                    }
                });
                keywordList.sort((o1, o2) -> Math.toIntExact(o2.getProductId() - o1.getProductId())); // productId 기준 내림차순 정렬
                int start = (int) pageable.getOffset();
                int end = Math.min((start + pageable.getPageSize()), keywordList.size());
                productSearchList = new PageImpl<>(keywordList.subList(start, end), pageable, keywordList.size()); // Page<Product> 객체 만듬
            }
        }
        else { // 판매중(0), 판매완료(1), 판매보류(2) 조회
            if(category == 1) {  // category 가 '상품명'일 때
                productSearchList = productRepository.findByProductNameContainingAndProductSold(searchKeyword, group, pageable)
                        .orElseThrow(() -> new IllegalArgumentException("not found product")); // productName 로 검색해서 페이징 처리한 Page<Product>
            }
            else if(category == 2) {  // category 가 '모델명'일 때
                productSearchList = productRepository.findByProductModelContainingAndProductSold(searchKeyword, group, pageable)
                        .orElseThrow(() -> new IllegalArgumentException("not found product")); //  productModel 로 검색해서 페이징 처리한 Page<Product>
            }
            else if(category == 3) { // category 가 '판매가'일 때
                try {
                    productSearchList = productRepository.findByProductRegularPriceAndProductSold(Integer.parseInt(searchKeyword), group , pageable)
                            .orElseThrow(() -> new IllegalArgumentException("not found product")); //  productRegularPrice 로 검색해서 페이징 처리한 Page<Product>
                } catch (NumberFormatException e) {
                    productSearchList = productList(group, pageable);
                }
            }
            else if(category == 4) { // category 가 '할인가'일 때
                try {
                    productSearchList = productRepository.findByProductDiscountPriceAndProductSold(Integer.parseInt(searchKeyword), group , pageable)
                            .orElseThrow(() -> new IllegalArgumentException("not found product")); //  productDiscountPrice 로 검색해서 페이징 처리한 Page<Product>
                } catch (NumberFormatException e) {
                    productSearchList = productList(group, pageable);
                }
            }
            else if(category == 5) { // category 가 '등록일'일 때
                // searchKeyword 가 포함된 List<Product> 만듬
                List<Product> keywordList = new ArrayList<>();
                findByUserSold(group).forEach(product -> {
                    if(product.getProductRegistrationDate().toString().contains(searchKeyword)){
                        keywordList.add(product);
                    }
                });
                keywordList.sort((o1, o2) -> Math.toIntExact(o2.getProductId() - o1.getProductId())); // productId 기준 내림차순 정렬
                int start = (int) pageable.getOffset();
                int end = Math.min((start + pageable.getPageSize()), keywordList.size());
                productSearchList = new PageImpl<>(keywordList.subList(start, end), pageable, keywordList.size()); // Page<Product> 객체 만듬
            }
        }

        return productSearchList;
    }

    public List<Product> bestProduct() {
        return productRepository.bestProduct()
                .orElseThrow(() -> new IllegalArgumentException("not found product"));
    }
}
