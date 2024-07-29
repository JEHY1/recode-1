package com.example.recode.service;

import com.example.recode.domain.*;
import com.example.recode.dto.*;
import com.example.recode.repository.CartRepository;
import com.example.recode.repository.PaymentDetailRepository;
import com.example.recode.repository.PaymentRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PaymentService {

    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");

    private final PaymentRepository paymentRepository;
    private final PaymentDetailRepository paymentDetailRepository;
    private final CartRepository cartRepository;
    private final ProductService productService;
    private final UserService userService;
    private final HistoryService historyService;
    
    //결제 요청 처리
    @Transactional
    public Payment payment(PaymentRequest request, Principal principal){

        //결제 요청 정보로 결제 테이블 db 추가
        Payment payment =  paymentRepository.save(Payment.builder()
                .userId(userService.getUserId(principal))
                .paymentPrice(request.getPaymentPrice())
                .paymentType(request.getPaymentType())
                .paymentBank(request.getPaymentBank().equals("-1") ? null : request.getPaymentBank())
                .paymentAccountNumber(request.getPaymentAccountNumber().isEmpty() ? null : request.getPaymentAccountNumber())
                .paymentDepositor(request.getPaymentDepositor().isEmpty() ? null : request.getPaymentDepositor())
                .paymentCard(request.getPaymentCard().equals("-1") ? null : request.getPaymentCard())
                .paymentCardNumber(request.getPaymentCardNumber().isEmpty() ? null : request.getPaymentCardNumber())
                .paymentInstallment(request.getPaymentInstallment() == -1 ? null : request.getPaymentInstallment())
                .paymentPhoneCompany(request.getPaymentPhoneCompany().equals("-1") ? null : request.getPaymentPhoneCompany())
                .paymentMicropaymentPhone(request.getPaymentMicropaymentPhone().isEmpty() ? null : request.getPaymentMicropaymentPhone())
                .paymentStatus("결제완료")
                .paymentPostalCode(request.getPaymentPostalCode())
                .paymentAddress(request.getPaymentAddress())
                .paymentDeliveryRequest(request.getDeliveryRequest())
                .paymentRecipientName(request.getPaymentRecipientName())
                .paymentRecipientPhone(request.getPaymentRecipientPhone())
                .paymentDeliveryFee(request.getDeliveryFee())
                .paymentFrontDoorSecret(request.getFrontDoorSecret().isEmpty() ? null : request.getFrontDoorSecret())
                .paymentDeliveryBoxNum(request.getDeliveryBoxNum().isEmpty() ? null : request.getDeliveryBoxNum())
                .build());

        //결제에 포함된 각각의 상품에 대해 결제 상세테이블 db 추가
        request.getProductIds().forEach(productId -> {
            Product product = productService.findProductByProductId(productId);
            paymentDetailRepository.save(PaymentDetail.builder()
                    .paymentId(payment.getPaymentId())
                    .productId(productId)
                    .paymentDetailStatus("결제완료")
                    .paymentDetailPrice(product.getProductDiscountPrice() == null ? product.getProductRegularPrice() : product.getProductDiscountPrice())
                    .build()
            );

            //상품을 판매된 상태로 변경
            product.updateSold(1);

            //구매 상품에 대한 히스토리(최근 본 상품) 삭제
            History history = historyService.findByProductIdAndUserId(productId, userService.getUserId(principal));
            if(history != null){
                historyService.delete(history);
            }

        });

        //구매된 상품에 대한 장바구니 삭제
        if(!request.getCartIds().isEmpty()){
            request.getCartIds().forEach(cartId -> cartRepository.deleteById(cartId));
        }
        else{
            Cart cart = cartRepository.findByUserIdAndProductId(userService.getUserId(principal), request.getProductIds().get(0))
                    .orElse(null);

            if(cart != null){
                cartRepository.delete(cart);
            }
        }

        return payment;
    }

    //마이페이지에서 보여주는 정보
    public MyPageViewResponse getMyPageInfo(Principal principal){

        //이용자의 모든 결제 내역
        int paymentReadyCount = 0; //결제 대기중인 결제 내역 
        int deliveryReadyCount = 0; //배송준비중인 결제 내역
        int deliveryCount = 0; //배송중인 결제 내역
        int deliveryCompleteCount = 0; //배송완료인 결제 내역
        int cancelCount = 0; //취소된 상품수량
//        int exchangeCount = 0; //교환된 상품 수량
        int returnCount = 0; //반품된 상품 수량

        List<Payment> list = findPaymentByPrincipal(principal);

        //결제내역 확인
        for(Payment payment : list){
            if(payment.getPaymentStatus().equals("결제완료")){
                paymentReadyCount++;
            }
            else if(payment.getPaymentStatus().equals("배송준비중")){
                deliveryReadyCount++;
            }
            else if(payment.getPaymentStatus().equals("배송중")){
                deliveryCount++;
            }
            else if(payment.getPaymentStatus().equals("배송완료")){
                deliveryCompleteCount++;
            }

            for(PaymentDetail paymentDetail : findPaymentDetailByPaymentId(payment.getPaymentId())){
                String paymentDetailStatus = paymentDetail.getPaymentDetailStatus();
                if(paymentDetailStatus.equals("주문취소")){
                    cancelCount++;
                }
//                else if(paymentDetailStatus.equals("교환") || paymentDetailStatus.equals("교환진행중")){
//                    exchangeCount++;
//                }
                else if(paymentDetailStatus.equals("반품대기") || paymentDetailStatus.equals("반품완료")){
                    returnCount++;
                }
            }
        }

        return MyPageViewResponse.builder()
                .paymentReadyCount(paymentReadyCount)
                .deliveryReadyCount(deliveryReadyCount)
                .deliveryCount(deliveryCount)
                .deliveryCompleteCount(deliveryCompleteCount)
                .cancelCount(cancelCount)
//                .exchangeCount(exchangeCount)
                .returnCount(returnCount)
                .build();
    }

    public List<Payment> findPaymentByPrincipal(Principal principal){
        return paymentRepository.findByUserId(userService.getUserId(principal))
                .orElseThrow(() -> new IllegalArgumentException("not found payment"));
    }


    public List<PaymentDetail> findPaymentDetailByPaymentId(long paymentId){
        return paymentDetailRepository.findByPaymentId(paymentId)
                .orElseThrow(() -> new IllegalArgumentException("not found paymentDetail"));
    }

    //주문에 대한 정보 확인
    public ProductOrderCheckAllInfoResponse orderCheck(Principal principal, String startDate, String endDate, Integer unitPeriod, Pageable pageable){

        Page<Payment> payments;
        List<Payment> allPayment;
        List<OrderCheckResponse> list = null;
        int nonDepositCount = 0;
        int deliveryReadyCount = 0;
        int deliveryInCount = 0;
        int deliveryCompleteCount = 0;

        //요청된 기간에 따라 검색
        if(unitPeriod != null){
            payments = paymentRepository.findByUserIdAndPaymentDateBetweenOrderByPaymentDateDesc(userService.getUserId(principal), LocalDateTime.now().minus(Period.ofMonths(unitPeriod)), LocalDateTime.now(), pageable)
                    .orElse(null);
            allPayment = paymentRepository.findByUserIdAndPaymentDateBetween(userService.getUserId(principal), LocalDateTime.now().minus(Period.ofMonths(unitPeriod)), LocalDateTime.now())
                    .orElse(null);
        }
        else if(startDate != null && endDate != null){
            LocalDateTime start = LocalDate.parse(startDate, formatter).atStartOfDay();
            LocalDateTime end = LocalDate.parse(endDate, formatter).plusDays(1).atStartOfDay();
            payments = paymentRepository.findByUserIdAndPaymentDateBetweenOrderByPaymentDateDesc(userService.getUserId(principal), start, end, pageable)
                    .orElse(null);
            allPayment = paymentRepository.findByUserIdAndPaymentDateBetween(userService.getUserId(principal), start, end)
                    .orElse(null);
        }
        else{ //둘다 미입력시 1개월
            payments = paymentRepository.findByUserIdAndPaymentDateBetweenOrderByPaymentDateDesc(userService.getUserId(principal), LocalDateTime.now().minus(Period.ofMonths(1)), LocalDateTime.now(), pageable)
                    .orElse(null);
            allPayment = paymentRepository.findByUserIdAndPaymentDateBetween(userService.getUserId(principal), LocalDateTime.now().minus(Period.ofMonths(1)), LocalDateTime.now())
                    .orElse(null);
        }

        if(allPayment != null){

            for(Payment payment : allPayment){
                switch(payment.getPaymentStatus()){
                    case "결제완료" :
                        nonDepositCount++;
                        break;

                    case "배송준비중" :
                        deliveryReadyCount++;
                        break;

                    case "배송중" :
                        deliveryInCount++;
                        break;

                    case "배송완료" :
                        deliveryCompleteCount++;
                        break;
                }
            }
        }


        if(payments != null){
            list = new ArrayList<>();

            for(Payment payment : payments){ //각 상태의 수 확인
                list.add(OrderCheckResponse.builder()
                        .orderDate(payment.getPaymentDate())
                        .paymentDetails(findPaymentDetailByPaymentId(payment.getPaymentId()).stream().map(this::toProductOrderCheckResponse).toList())
                        .build()
                );
            }
        }

        Page<OrderCheckResponse> OrderCheckInfoList = new PageImpl<>(list, payments.getPageable(), list.size());

        return ProductOrderCheckAllInfoResponse.builder()
                .orderCheckInfoList(OrderCheckInfoList)
                .nonDepositCount(nonDepositCount)
                .deliveryReadyCount(deliveryReadyCount)
                .deliveryInCount(deliveryInCount)
                .deliveryCompleteCount(deliveryCompleteCount)
                .totalPage(payments.getTotalPages())
                .build();
    }

    //PaymentDetail -> ProductOrderCheckResponse
    public ProductOrderCheckResponse toProductOrderCheckResponse(PaymentDetail paymentdetail){

        Product product = productService.findProductByProductId(paymentdetail.getProductId());

        return ProductOrderCheckResponse.builder()
                .paymentDetailId(paymentdetail.getPaymentDetailId())
                .productName(product.getProductName())
                .productRepImgSrc(product.getProductRepresentativeImgSrc())
                .paymentDetailStatus(paymentdetail.getPaymentDetailStatus())
                .paymentDetailPrice(paymentdetail.getPaymentDetailPrice())
                .build();
    }

    //요청받은 기간(문자열) 분할
    public PeriodResponse getPeriod(Integer unitPeriod, String startDate, String endDate){

        String currentDate = LocalDateTime.now().toString();

        if(startDate != null && endDate != null){
            return PeriodResponse.builder()
                    .currentYear(currentDate.substring(0, 4))
                    .currentMonthAndDay(currentDate.substring(5, 7) + currentDate.substring(8, 10))
                    .startYear(startDate.substring(0, 4))
                    .startMonth(startDate.substring(4, 6))
                    .startDay(startDate.substring(6))
                    .endYear(endDate.substring(0, 4))
                    .endMonth(endDate.substring(4, 6))
                    .endDay(endDate.substring(6))
                    .build();
        }
        else{
            unitPeriod = unitPeriod == null ? 1 : unitPeriod;
            String before = LocalDateTime.now().minus(Period.ofMonths(unitPeriod)).toString();

            return PeriodResponse.builder()
                    .currentYear(currentDate.substring(0, 4))
                    .currentMonthAndDay(currentDate.substring(5, 7) + currentDate.substring(8, 10))
                    .startYear(before.substring(0, 4))
                    .startMonth(before.substring(5, 7))
                    .startDay(before.substring(8, 10))
                    .endYear(currentDate.substring(0, 4))
                    .endMonth(currentDate.substring(5, 7))
                    .endDay(currentDate.substring(8, 10))
                    .build();
        }
    }

    //사용자의 주문에대한 변경요청처리
    @Transactional
    public PaymentDetail paymentDetailManage(OrderManageRequest request){
        PaymentDetail paymentDetail = findPaymentDetailByPaymentDetailId(request.getPaymentDetailId());

        if(paymentDetail != null){
            Payment payment = findPaymentByPaymentId(paymentDetail.getPaymentId());
            List<PaymentDetail> list = findPaymentDetailByPaymentId(payment.getPaymentId());

            //각각의 요청시 해당주문에 포함하는 모든 paymentDetail이 같은 상태가 될 경우 주문의 상태 변경
            if(request.getPaymentDetailStatusRequest().equals("주문취소")){
                boolean isAllCancel = true;

                paymentDetail.updateStatus(request.getPaymentDetailStatusRequest());

                for(PaymentDetail _paymentDetail : list){
                    if(!_paymentDetail.getPaymentDetailStatus().equals("주문취소")){
                        isAllCancel = false;
                        break;
                    }
                }
                
                if(isAllCancel){
                    payment.updatePaymentStatus("주문취소");
                }
            }
            else if(request.getPaymentDetailStatusRequest().equals("반품신청")){
                boolean isAllReturn = true;
                
                paymentDetail.updateStatus("반품진행");

                for(PaymentDetail _paymentDetail : list){
                    if(!_paymentDetail.getPaymentDetailStatus().equals("반품진행")){
                        isAllReturn = false;
                        break;
                    }
                }
                
                if(isAllReturn){
                    payment.updatePaymentStatus("반품진행");
                }
            }
        }

        return paymentDetail;
    }

    public PaymentDetail findPaymentDetailByPaymentDetailId(long paymentDetailId){
        return paymentDetailRepository.findById(paymentDetailId).orElse(null);
    }

    public Payment findPaymentByPaymentId(long paymentId){
        return paymentRepository.findById(paymentId).orElse(null);
    }

    //상품 검색시 나타나는 창(검색도우미) 에 표시될 내용
    public List<ProductNameForm> getPaymentDetailProductInProductName(String productName){

        List<PaymentDetail> list = paymentDetailRepository.findAll();
        Set<ProductNameForm> nameList = new HashSet<>(); //같은 결과 제거용
        list.forEach(paymentDetail -> {
            Product product = productService.findProductByProductId(paymentDetail.getProductId());

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

    //상품 검색 도우미와 비슷
    public List<UserRealNameForm> getUserNameInfoInUsername(String username){
        Set<UserRealNameForm> nameList = new HashSet<>();
        List<User> list = userService.findUserByUsernameContaining(username);

        list.forEach(user -> {
            int index = user.getUsername().indexOf(username);
            nameList.add(UserRealNameForm.builder()
                    .frontText(user.getUsername().substring(0, index))
                    .searchText(username)
                    .endText(user.getUsername().substring(index + username.length()))
                    .realName(" (" + user.getUserRealName() + ")")
                    .build());
        });

        return nameList.stream().toList();
    }

    //기간(월 단위) 을 입력받아 현재 날짜와 해당 기간 이전날짜를 생성
    public DateForm getServerDate(Integer period){
        period = period == null ? 120 : period; //기간 미입력시 기본값 전체
        String endDate = LocalDate.now().toString();
        String startDate = LocalDate.now().minus(Period.ofMonths(period)).toString();

        if(period == 120){ //전체(120) 은 현재 날짜로부터 10년전 1월 1일을 시작일로 함
            return DateForm.builder()
                    .currentYear(endDate.substring(0, 4))
                    .currentMonthAndDay(endDate.substring(5, 7) + endDate.substring(8, 10))
                    .startYear(startDate.substring(0, 4))
                    .startMonth("01")
                    .startDay("01")
                    .endYear(endDate.substring(0, 4))
                    .endMonth(endDate.substring(5, 7))
                    .endDay(endDate.substring(8, 10))
                    .build();
        }

        return DateForm.builder()
                .currentYear(endDate.substring(0, 4))
                .currentMonthAndDay(endDate.substring(5, 7) + endDate.substring(8, 10))
                .startYear(startDate.substring(0, 4))
                .startMonth(startDate.substring(5, 7))
                .startDay(startDate.substring(8, 10))
                .endYear(endDate.substring(0, 4))
                .endMonth(endDate.substring(5, 7))
                .endDay(endDate.substring(8, 10))
                .build();
    }

    public AdminPaymentTotalInfoResponse getPaymentInfos(String productName, String username, String startDate, String endDate, Integer minPrice,  Integer maxPrice, String paymentStatus, String paymentDetailStatus){

        //시작날 종료날 존재할경우 (첫 입장시 없음)
        if(startDate != null && endDate != null){
            LocalDateTime start = LocalDate.parse(startDate, formatter).atStartOfDay();
            LocalDateTime end = LocalDate.parse(endDate, formatter).atStartOfDay().plus(Period.ofDays(1));

            List<Payment> payments = paymentRepository.findPaymentsInDateRange(start, end)
                    .orElse(null);
            List<Payment> filteredPayments = new ArrayList<>();

            if(payments != null){
                for(Payment payment : payments){
                    //상품 검색조건(최대 가격, 최소가격, 결재내역 상태) 조건이 일치한 경우
                    if((maxPrice == null ? true : payment.getPaymentPrice() <= maxPrice) && (minPrice == null ? true : payment.getPaymentPrice() >= minPrice) && (paymentStatus.equals("전체") ? true : paymentStatus.equals(payment.getPaymentStatus()))){

                        //결제내역 상세조건이 전체 인경우
                        if(paymentDetailStatus.equals("전체")){
                            filteredPayments.add(payment);
                        }
                        else{ //결제내역 상세조건이 전체가 아닌경우 결제 내역 상세에서 해당 조건이 하나라도 포함된 상품이 있는지 확인하여 filter
                            List<PaymentDetail> paymentDetails = findPaymentDetailByPaymentId(payment.getPaymentId());
                            for(PaymentDetail paymentDetail : paymentDetails){
                                if(paymentDetail.getPaymentDetailStatus().equals(paymentDetailStatus)){
                                    filteredPayments.add(payment);
                                    break;
                                }
                            }
                        }
                    }
                }

                payments = filteredPayments;
                filteredPayments = new ArrayList<>();

                //상품이름이 검색조건에 포함된 경우
                if(productName != null){
                    //결제에 포함된 상품들에서 검색상품이름이 포함된 경우가 하나라도 있는지 없는지 확인 filter
                    for(Payment payment : payments){
                        List<PaymentDetail> paymentDetails = findPaymentDetailByPaymentId(payment.getPaymentId());
                        List<Long> productIds = paymentDetails.stream().mapToLong(paymentDetail -> paymentDetail.getProductId()).boxed().collect(Collectors.toList());
                        List<Product> products = productService.findProductAllByProductIdIn(productIds);
                        for(Product product : products){

                            if(product.getProductName().contains(productName)){
                                filteredPayments.add(payment);
                                break;
                            }
                        }
                    }
                    payments = filteredPayments;
                    filteredPayments = new ArrayList<>();
                }

                //검색조건에 username 이 있는경우 확인하여 filter
                if(username != null){
                    for(Payment payment : payments){
                        User user = userService.findById(payment.getUserId());
                        if(user.getUsername().contains(username)){
                            filteredPayments.add(payment);
                        }
                    }

                    payments = filteredPayments;
                    filteredPayments = new ArrayList<>();
                }

                //관리자 결제내역 통계 확인용
                int totalDeposit = 0;
                int totalPaymentCompleteDeposit = 0;
                int totalPaymentNonDeposit = 0;
                int totalPaymentCancelDeposit = 0;
                int totalDepositCount = 0;
                int totalPaymentCompleteDepositCount = 0;
                int totalPaymentNonDepositCount = 0;
                int totalPaymentCancelDepositCount = 0;
                int totalPaymentCount = 0;
                int totalPaymentCompleteCount = 0;
                int totalPaymentNonCount = 0;
                int totalPaymentCancelCount = 0;

                List<Long> paymentIds = payments.stream().mapToLong(payment -> payment.getPaymentId()).boxed().collect(Collectors.toList());

                for(PaymentDetail paymentDetail : findPaymentDetailByPaymentIdIn(paymentIds)){
                    totalDeposit += paymentDetail.getPaymentDetailPrice();
                    totalDepositCount++;
                    if(paymentDetail.getPaymentDetailStatus().equals("결제완료")){
                        totalPaymentNonDeposit += paymentDetail.getPaymentDetailPrice();
                        totalPaymentNonDepositCount++;
                    }
                    else if(paymentDetail.getPaymentDetailStatus().contains("배송")){
                        totalPaymentCompleteDeposit += paymentDetail.getPaymentDetailPrice();
                        totalPaymentCompleteDepositCount++;
                    }
                    else{
                        totalPaymentCancelDeposit += paymentDetail.getPaymentDetailPrice();
                        totalPaymentCancelDepositCount++;
                    }
                }

                for(Payment payment : payments){
                    totalPaymentCount++;
                    if(payment.getPaymentStatus().equals("결제완료")){
                        totalPaymentNonCount++;
                    }
                    else if(payment.getPaymentStatus().contains("배송")){
                        totalPaymentCompleteCount++;
                    }
                    else{
                        totalPaymentCancelCount++;
                    }
                }
                
                return AdminPaymentTotalInfoResponse.builder()
                        .paymentInfo(adminPaymentInfoResponsePaging(paymentToAdminPaymentInfoResponse(payments, productName), 10))
                        .totalDeposit(totalDeposit)
                        .totalPaymentCompleteDeposit(totalPaymentCompleteDeposit)
                        .totalPaymentNonDeposit(totalPaymentNonDeposit)
                        .totalPaymentCancelDeposit(totalPaymentCancelDeposit)
                        .totalDepositCount(totalDepositCount)
                        .totalPaymentNonDepositCount(totalPaymentNonDepositCount)
                        .totalPaymentCompleteDepositCount(totalPaymentCompleteDepositCount)
                        .totalPaymentCancelDepositCount(totalPaymentCancelDepositCount)
                        .totalPaymentCount(totalPaymentCount)
                        .totalPaymentNonCount(totalPaymentNonCount)
                        .totalPaymentCompleteCount(totalPaymentCompleteCount)
                        .totalPaymentCancelCount(totalPaymentCancelCount)
                        .build();
            }
        }
        else{
            List<Payment> payments = paymentRepository.findAll();
        }

        return new AdminPaymentTotalInfoResponse();
    }

    //size 로 페이징
    public List<List<AdminPaymentInfoResponse>> adminPaymentInfoResponsePaging(List<AdminPaymentInfoResponse> adminPaymentInfoResponses, int size){

        List<List<AdminPaymentInfoResponse>> pages = new ArrayList<>();
        List<AdminPaymentInfoResponse> list = new ArrayList<>();
        int index = 0;

        for(AdminPaymentInfoResponse info : adminPaymentInfoResponses){
            list.add(info);
            index++;

            if(index % size == 0){
                pages.add(list);
                list = new ArrayList<>();
            }
        }

        if(!list.isEmpty()){
            pages.add(list);
        }

        return pages;
    }


    //Payment 리스트-> AdminPaymentInfoResponse 리스트
    public List<AdminPaymentInfoResponse> paymentToAdminPaymentInfoResponse(List<Payment> payments, String productSearched){

        List<AdminPaymentInfoResponse> list = new ArrayList<>();

        payments.forEach(payment -> {
            long paymentId = payment.getPaymentId();
            List<Product> products = productService.findProductAllByProductIdIn(findPaymentDetailByPaymentId(payment.getPaymentId()).stream().mapToLong(paymentDetail -> paymentDetail.getProductId()).boxed().collect(Collectors.toList()));
            String productName = "";
            String userRealName;
            int paymentPrice = payment.getPaymentPrice();
            String paymentStatus = payment.getPaymentStatus();

            for(Product product : products){
                if(product.getProductName().contains(productSearched == null ? "" : productSearched)){
                    productName = product.getProductName();
                }
            }

            if(products.size() > 1){
                productName += " 외 " + (products.size() - 1) + "개 상품";
            }

            User user = userService.findById(payment.getUserId());
            userRealName = user.getUsername() + " (" + user.getUserRealName() + ")";

            list.add(AdminPaymentInfoResponse.builder()
                    .paymentId(paymentId)
                    .productName(productName)
                    .userRealName(userRealName)
                    .paymentPrice(paymentPrice)
                    .paymentStatus(paymentStatus)
                    .paymentDate(payment.getPaymentDate())
                    .build());

        });

        return list;
    }

    public List<OrderDetailManagerViewResponse> getOrderDetailInfo(long paymentId){
        return findPaymentDetailByPaymentId(paymentId).stream().map(this::toOrderDetailManagerViewResponse).collect(Collectors.toList());
    }

    //관리자 결제 상세정보 확인페이지 정보
    public OrderDetailManagerViewResponse toOrderDetailManagerViewResponse(PaymentDetail paymentDetail){

        Product product = productService.findProductByProductId(paymentDetail.getProductId());

        return OrderDetailManagerViewResponse.builder()
                .paymentDetailId(paymentDetail.getPaymentDetailId())
                .productRepImgSrc(product.getProductRepresentativeImgSrc())
                .productName(product.getProductName())
                .productPrice(paymentDetail.getPaymentDetailPrice())
                .paymentDetailStatus(paymentDetail.getPaymentDetailStatus())
                .build();
    }

    //관리자 주문 상태 변경 처리
    @Transactional
    public List<Payment> ordersUpdate(AdminOrderManageRequest request){

        List<Payment> payments = new ArrayList<>();
        request.getPaymentIds().forEach(id -> {
            Payment payment = findPaymentByPaymentId(id);
            payment.updatePaymentStatus(request.getPaymentStatus());
            payments.add(payment);
        });

        return payments;
    }

    //관리자 주문 상세 상태 변경 처리
    @Transactional
    public List<PaymentDetail> orderDetailsUpdate(AdminOrderDetailManageRequest request){

        List<PaymentDetail> paymentDetails = new ArrayList<>();
        request.getPaymentDetailIds().forEach(id -> {
            PaymentDetail paymentDetail = findPaymentDetailByPaymentDetailId(id);
            paymentDetail.updateStatus(request.getPaymentDetailStatus());
            paymentDetails.add(paymentDetail);
        });

        return paymentDetails;
    }

    public String getCustomerName(long paymentId){
        User user = userService.findById(findPaymentByPaymentId(paymentId).getUserId());

        return user.getUsername() + " (" + user.getUserRealName() + ")";

    }

    public List<PaymentDetail> findPaymentDetailByPaymentIdIn(List<Long> paymentIds){
        return paymentDetailRepository.findAllByPaymentIdIn(paymentIds)
                .orElseThrow(() -> new IllegalArgumentException("not found paymentDetail"));
    }
}
