package com.group1.Care_Koi_System.service;

import com.group1.Care_Koi_System.dto.HistoryResponse;
import com.group1.Care_Koi_System.dto.KoiFish.KoiFishRequest;
import com.group1.Care_Koi_System.dto.KoiFish.KoiFishResponse;
import com.group1.Care_Koi_System.dto.Pond.ViewPondResponse;
import com.group1.Care_Koi_System.entity.*;
import com.group1.Care_Koi_System.entity.Enum.*;
import com.group1.Care_Koi_System.exceptionhandler.Account.AccountException;
import com.group1.Care_Koi_System.exceptionhandler.ErrorCode;
import com.group1.Care_Koi_System.exceptionhandler.KoiFish.KoiFishException;
import com.group1.Care_Koi_System.exceptionhandler.ResponseException;
import com.group1.Care_Koi_System.exceptionhandler.SystemException;
import com.group1.Care_Koi_System.repository.*;
import com.group1.Care_Koi_System.utils.AccountUtils;
import jakarta.validation.constraints.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.group1.Care_Koi_System.entity.Enum.FoodType.AQUAMASTER;
import static com.group1.Care_Koi_System.entity.Enum.FoodType.SAKURA;

@Service
public class KoiFishService {

    @Autowired
    private KoiFishRepository koiFishRepository;

    @Autowired
    private AccountUtils accountUtils;

    @Autowired
    private PondRepository pondRepository;

    @Autowired
    private Pond_FeedingRepository pondFeedingRepository;

    @Autowired
    Pond_KoiFishRepository pond_koiFishRepository;

    @Autowired
    private FeedingRepository feedingRepository;

    public ResponseEntity<KoiFishResponse> createKoiFish(KoiFishRequest koiFishRequest, KoiSpecies species,
                                                         KoiGender gender, KoiOrigin origin, HealthyStatus healthyStatus, int pondID) {

        try {
            Account account = accountUtils.getCurrentAccount();
            if (account == null) {
                throw new KoiFishException(ErrorCode.NOT_LOGIN);
            }

            //find ponds by id
            Ponds ponds = pondRepository.findById(pondID);
            if (ponds == null) {
                throw new KoiFishException(ErrorCode.INVALIDPOND);
            }

            //save koi fish
            KoiFish koiFish = new KoiFish();
            Pond_KoiFish pondKoiFish = new Pond_KoiFish();
            koiFish.setFishName(koiFishRequest.getFishName());
            koiFish.setImageFish(koiFishRequest.getImageFish());
            koiFish.setBirthDay(koiFishRequest.getBirthDay());
            koiFish.setSpecies(species);
            koiFish.setSize(koiFishRequest.getSize());
            koiFish.setWeigh(koiFishRequest.getWeigh());
            koiFish.setGender(gender);
            koiFish.setOrigin(origin);
            koiFish.setHealthyStatus(healthyStatus);
            koiFish.setNote(koiFishRequest.getNote());
            koiFishRepository.save(koiFish);

            //save pond_koifish

            pondKoiFish.setPonds(ponds);
            pondKoiFish.setKoiFish(koiFish);
            pondKoiFish.setDateAdded(LocalDateTime.now());
            pond_koiFishRepository.save(pondKoiFish);


            // Create the response object
            KoiFishResponse response = new KoiFishResponse();
            response.setId(koiFish.getId());
            response.setPondID(ponds.getId());
            response.setFishName(koiFish.getFishName());
            response.setImageFish(koiFish.getImageFish());
            response.setBirthDay(koiFish.getBirthDay());
            response.setSpecies(koiFish.getSpecies());
            response.setSize(koiFish.getSize());
            response.setWeigh(koiFish.getWeigh());
            response.setGender(koiFish.getGender());
            response.setOrigin(koiFish.getOrigin());
            response.setHealthyStatus(koiFish.getHealthyStatus());
            response.setNote(koiFish.getNote());
            response.setDateAdded(pondKoiFish.getDateAdded());
            response.setMaximum(ponds.getMaximum());

            return ResponseEntity.ok(response);
        } catch (KoiFishException ex) {
            ErrorCode errorCode = ex.getErrorCode();
            KoiFishResponse exceptionResponse = new KoiFishResponse(errorCode.getMessage());
            return new ResponseEntity<>(exceptionResponse, errorCode.getHttpStatus());
        }
    }

    public KoiFishResponse updateKoiFish(int pondId, int fishID, KoiFishRequest koiFishRequest, KoiSpecies species,
                                         KoiGender gender, KoiOrigin origin, HealthyStatus healthyStatus) {
        KoiFish optionalKoiFish = koiFishRepository.findById(fishID);
        if (optionalKoiFish == null) {
            throw new RuntimeException("KoiFish with ID " + fishID + " not found.");
        }

        Pond_KoiFish pondKoiFish = pond_koiFishRepository.findPondsByKoiFishId(fishID);
        if (pondKoiFish == null) {
            throw new IllegalArgumentException("No pond-koi fish association found for fish ID: " + fishID);
        }

        Ponds ponds = pondRepository.findById(pondId);
        if (ponds == null) {
            throw new IllegalArgumentException("No pond found for pond ID: " + pondId);
        }

        Ponds currentPond = pondKoiFish.getPonds();

        if (ponds.getId() != currentPond.getId()) {
            pondKoiFish.setPonds(ponds);
            pondKoiFish.setEndDate(LocalDateTime.now());
            pondKoiFish.setMessage("Moved from " + currentPond.getNamePond() + " to " + ponds.getNamePond());
        } else {
            pondKoiFish.setUpdateDate(LocalDateTime.now());
        }

        // Cập nhật thông tin từ KoiFishRequest vào KoiFish
        optionalKoiFish.setFishName(koiFishRequest.getFishName());
        optionalKoiFish.setImageFish(koiFishRequest.getImageFish());
        optionalKoiFish.setBirthDay(koiFishRequest.getBirthDay());
        optionalKoiFish.setSpecies(species);
        optionalKoiFish.setSize(koiFishRequest.getSize());
        optionalKoiFish.setWeigh(koiFishRequest.getWeigh());
        optionalKoiFish.setGender(gender);
        optionalKoiFish.setOrigin(origin);
        optionalKoiFish.setHealthyStatus(healthyStatus);
        optionalKoiFish.setNote(koiFishRequest.getNote());
        // Lưu lại KoiFish đã cập nhật

        pond_koiFishRepository.save(pondKoiFish);
        koiFishRepository.save(optionalKoiFish);
        return convertToResponse(optionalKoiFish);
    }

    private KoiFishResponse convertToResponse(KoiFish koiFish) {
        KoiFishResponse response = new KoiFishResponse();
        response.setId(koiFish.getId());
        response.setFishName(koiFish.getFishName());
        response.setImageFish(koiFish.getImageFish());
        response.setBirthDay(koiFish.getBirthDay());
        response.setSpecies(koiFish.getSpecies());
        response.setSize(koiFish.getSize());
        response.setWeigh(koiFish.getWeigh());
        response.setGender(koiFish.getGender());
        response.setOrigin(koiFish.getOrigin());
        response.setHealthyStatus(koiFish.getHealthyStatus());
        response.setNote(koiFish.getNote());
        return response;
    }

    public ResponseEntity<String> deleteKoiFish(int koiFishID) {
        KoiFish koiFishOptional = koiFishRepository.findById(koiFishID);

        if (koiFishOptional == null) {
            return new ResponseEntity<>("Koi fish not found", HttpStatus.NOT_FOUND);
        }

        // Mark KoiFish as deleted
        koiFishOptional.setDeleted(true);
        koiFishRepository.save(koiFishOptional);

        // Find the associated Pond_KoiFish entry
        Pond_KoiFish pondKoiFish = pond_koiFishRepository.findPondsByKoiFishId(koiFishID);
        if (pondKoiFish == null) {
            return new ResponseEntity<>("Pond-KoiFish association not found", HttpStatus.NOT_FOUND);
        }

        // Set the end date and save the association
        pondKoiFish.setEndDate(LocalDateTime.now());
        pond_koiFishRepository.save(pondKoiFish);

        return new ResponseEntity<>("Koi fish marked as deleted successfully", HttpStatus.OK);
    }

    public Feeding calculateFood(int idPond, FoodType foodType) {
        Feeding feeding = new Feeding();
        Pond_Feeding pondFeeding = new Pond_Feeding();


        feeding.setFoodType(foodType);
        feeding.setFeedingTime(LocalDateTime.now());

        Ponds pond = pondRepository.findById(idPond);

        int fishCount = 0;
        BigDecimal pondSize = BigDecimal.valueOf(0.0);

        List<Pond_KoiFish> pondKoiFish = pond.getKoiFishList();
        pondSize = BigDecimal.valueOf(pond.getSize());
        for (Pond_KoiFish pond_koiFish : pondKoiFish) {
            KoiFish fish = pond_koiFish.getKoiFish();
            if (fish != null) {
                fishCount++;
            }
        }

        //tinh toan so lg thuc an co ban
        BigDecimal baseAmount = calculateFoodAmount(fishCount, pondSize, foodType);

        //dieu chinh so lg thuc an tren size cua ao
        BigDecimal adjustedAmount = adjustFoodForPondSize(baseAmount, pondSize);

        feeding.setAmount(adjustedAmount.doubleValue());

        feedingRepository.save(feeding);
        pondFeeding.setFeeding(feeding);
        pondFeeding.setPonds(pond);
        pondFeedingRepository.save(pondFeeding);
        return feeding;
    }

    // Phương thức tính toán lượng thức ăn
    private BigDecimal calculateFoodAmount(int fishCount, BigDecimal pondSize, @NotNull FoodType foodType) {
        BigDecimal foodPerFish = new BigDecimal("0.05"); // vi du 0.05 kg cho mỗi con cá
        BigDecimal baseFoodAmount = foodPerFish.multiply(new BigDecimal(fishCount));

        // Điều chỉnh lượng thức ăn dựa trên loại thức ăn
        switch (foodType) {
            case AQUAMASTER: //thanh phan dinh duong cao, hap dan ca
                return baseFoodAmount.multiply(new BigDecimal("1.1")); // Thêm 10% cho AQUAMASTER
            case SAKURA: // thuc an tieu chuan
                return baseFoodAmount; // Không điều chỉnh cho SAKURA
            case RIO: //thuc an co gia thanh thấp , kkem dinh duong
                return baseFoodAmount.multiply(new BigDecimal("0.9")); // Giảm 10% cho RIO
            default:
                return baseFoodAmount; // Trả về lượng thức ăn cơ bản nếu không có loại nào khớp
        }
    }

    public BigDecimal adjustFoodForPondSize(BigDecimal amount, BigDecimal pondSize) {
        // phuong thuuc dieu chinh luong thuc an dua theo size cua pond
        if (pondSize.compareTo(new BigDecimal("500")) < 0) {
            return amount.multiply(new BigDecimal("1.05")); // Ao nhỏ, tăng thêm 5%
        } else if (pondSize.compareTo(new BigDecimal("1000")) > 0) {
            return amount.multiply(new BigDecimal("0.95")); // Ao lớn, giảm đi 5%
        }
        return amount;
    }

    public ResponseEntity<?> getAllKoiFish() {
        try {

            List<KoiFish> koiFishList = koiFishRepository.findAll().stream()
                    .filter(fish -> !fish.isDeleted()).toList();

            if (koiFishList.isEmpty()) {
                throw new SystemException(ErrorCode.EMPTY);
            }
            List<KoiFishResponse> fishs = new ArrayList<>();
            for (KoiFish fish : koiFishList) {

                Pond_KoiFish pondKoiFish = pond_koiFishRepository.findPondsByKoiFishId(fish.getId());
                fishs.add(new KoiFishResponse(
                        fish.getId(),
                        fish.getFishName(),
                        fish.getImageFish(),
                        fish.getBirthDay(),
                        fish.getSpecies(),
                        fish.getSize(),
                        fish.getWeigh(),
                        fish.getGender(),
                        fish.getOrigin(),
                        fish.getHealthyStatus(),
                        fish.getNote(),
                        pondKoiFish.getPonds().getId()
                ));
            }
            return new ResponseEntity<>(fishs, HttpStatus.OK);
        } catch (SystemException ex) {
            ErrorCode errorCode = ex.getErrorCode();
            ResponseException respon = new ResponseException(ex.getMessage());
            return new ResponseEntity<>(respon, errorCode.getHttpStatus());
        }
    }

    public ResponseEntity<?> getKoiFishByAccount() {
        try {

            Account account = accountUtils.getCurrentAccount();
            if (account == null) {
                throw new SystemException(ErrorCode.NOT_LOGIN);
            }

            List<Ponds> pondsList = pondRepository.findByAccount(account).stream()
                    .filter(ponds -> !ponds.isDeleted()).toList();
            if (pondsList == null) {
                throw new SystemException(ErrorCode.EMPTY);
            }
            List<KoiFishResponse> fishs = new ArrayList<>();
            for (Ponds pond : pondsList) {
                List<Pond_KoiFish> pondKoiFish = pond.getKoiFishList();
                for (Pond_KoiFish pond_koiFish : pondKoiFish) {
                    KoiFish fish = pond_koiFish.getKoiFish();
                   if(!fish.isDeleted()){
                       Pond_KoiFish pondKoi = pond_koiFishRepository.findPondsByKoiFishId(fish.getId());
                       fishs.add(new KoiFishResponse(
                               fish.getId(),
                               fish.getFishName(),
                               fish.getImageFish(),
                               fish.getBirthDay(),
                               fish.getSpecies(),
                               fish.getSize(),
                               fish.getWeigh(),
                               fish.getGender(),
                               fish.getOrigin(),
                               fish.getHealthyStatus(),
                               fish.getNote(),
                               pondKoi.getPonds().getId()
                       ));
                   }
                }
            }
            if (fishs.isEmpty()) {
                throw new SystemException(ErrorCode.EMPTY);
            }

            return new ResponseEntity<>(fishs, HttpStatus.OK);
        } catch (SystemException ex) {
            ErrorCode errorCode = ex.getErrorCode();
            ResponseException respon = new ResponseException(ex.getMessage());
            return new ResponseEntity<>(respon, errorCode.getHttpStatus());
        }
    }

    public ResponseEntity<?> getHistory(int koiFishID) {
        try {
            Account account = accountUtils.getCurrentAccount();
            if (account == null) {
                throw new SystemException(ErrorCode.NOT_LOGIN);
            }

            List<Pond_KoiFish> histo = pond_koiFishRepository.findByKoiFishId(koiFishID);
            if (histo.isEmpty()) {
                throw new SystemException(ErrorCode.EMPTY);
            }

            List<HistoryResponse> responses = new ArrayList<>();

            for (Pond_KoiFish fish : histo) {
                String fishname = fish.getKoiFish().getFishName();
                responses.add(new HistoryResponse(
                        fish.getId(),
                        fishname,
                        fish.getDateAdded(),
                        fish.getEndDate(),
                        fish.getMessage()
                ));
            }
            if (responses.isEmpty()) {
                throw new SystemException(ErrorCode.EMPTY);
            }

            return new ResponseEntity<>(responses, HttpStatus.OK);
        } catch (SystemException ex) {
            ErrorCode errorCode = ex.getErrorCode();
            ResponseException respon = new ResponseException(ex.getMessage());
            return new ResponseEntity<>(respon, errorCode.getHttpStatus());
        }
    }

    public ResponseEntity<?> getAll() {
        try {

            Account account = accountUtils.getCurrentAccount();
            if (account == null) {
                throw new SystemException(ErrorCode.NOT_LOGIN);
            }

            List<Ponds> pondsList = pondRepository.findByAccount(account);
            if (pondsList == null) {
                throw new SystemException(ErrorCode.EMPTY);
            }
            List<KoiFishResponse> fishs = new ArrayList<>();
            for (Ponds pond : pondsList) {
                List<Pond_KoiFish> pondKoiFish = pond.getKoiFishList();
                for (Pond_KoiFish pond_koiFish : pondKoiFish) {
                    KoiFish fish = pond_koiFish.getKoiFish();
                    Pond_KoiFish pondKoi = pond_koiFishRepository.findPondsByKoiFishId(fish.getId());
                    fishs.add(new KoiFishResponse(
                            fish.getId(),
                            fish.getFishName(),
                            pondKoi.getPonds().getId()
                    ));
                }
            }
            if (fishs.isEmpty()) {
                throw new SystemException(ErrorCode.EMPTY);
            }

            return new ResponseEntity<>(fishs, HttpStatus.OK);
        } catch (SystemException ex) {
            ErrorCode errorCode = ex.getErrorCode();
            ResponseException respon = new ResponseException(ex.getMessage());
            return new ResponseEntity<>(respon, errorCode.getHttpStatus());
        }
    }

    public ResponseEntity<?> getKoiFishByID(int fishID) {
        try {

            Account account = accountUtils.getCurrentAccount();
            if (account == null) {
                throw new SystemException(ErrorCode.NOT_LOGIN);
            }

            KoiFish fish = koiFishRepository.findById(fishID);
            if (fish == null) {
                throw new SystemException(ErrorCode.EMPTY);
            }
            Pond_KoiFish pondKoi = pond_koiFishRepository.findPondsByKoiFishId(fish.getId());
            KoiFishResponse fishs = new KoiFishResponse(
                    fish.getId(),
                    fish.getFishName(),
                    fish.getImageFish(),
                    fish.getBirthDay(),
                    fish.getSpecies(),
                    fish.getSize(),
                    fish.getWeigh(),
                    fish.getGender(),
                    fish.getOrigin(),
                    fish.getHealthyStatus(),
                    fish.getNote(),
                    pondKoi.getPonds().getId()
            );

            return new ResponseEntity<>(fishs, HttpStatus.OK);
        } catch (SystemException ex) {
            ErrorCode errorCode = ex.getErrorCode();
            ResponseException respon = new ResponseException(ex.getMessage());
            return new ResponseEntity<>(respon, errorCode.getHttpStatus());
        }
    }

    public ResponseEntity<?> getHistoryByAccount() {
        try {
            Account account = accountUtils.getCurrentAccount();
            if (account == null) {
                throw new SystemException(ErrorCode.NOT_LOGIN);
            }

            List<Pond_KoiFish> histo = pond_koiFishRepository.findAll();
            if (histo.isEmpty()) {
                throw new SystemException(ErrorCode.EMPTY);
            }

            List<HistoryResponse> responses = new ArrayList<>();

            List<Ponds> pondsList = pondRepository.findByAccount(account);
            if (pondsList == null) {
                throw new SystemException(ErrorCode.EMPTY);
            }

            for (Ponds pond : pondsList) {
                List<Pond_KoiFish> pondKoiFish = pond.getKoiFishList();
                for (Pond_KoiFish pond_koiFish : pondKoiFish) {
                    KoiFish fish = pond_koiFish.getKoiFish();
                    Pond_KoiFish pondKoi = pond_koiFishRepository.findPondsByKoiFishId(fish.getId());
                    responses.add(new HistoryResponse(
                            fish.getId(),
                            fish.getFishName(),
                            pondKoi.getDateAdded(),
                            pondKoi.getEndDate(),
                            pondKoi.getMessage()
                    ));
                }
            }
            if (responses.isEmpty()) {
                throw new SystemException(ErrorCode.EMPTY);
            }

            return new ResponseEntity<>(responses, HttpStatus.OK);
        } catch (SystemException ex) {
            ErrorCode errorCode = ex.getErrorCode();
            ResponseException respon = new ResponseException(ex.getMessage());
            return new ResponseEntity<>(respon, errorCode.getHttpStatus());
        }
    }

    public ResponseEntity<?> getKoiFishByPondId(int id){
        try{
            Account account = accountUtils.getCurrentAccount();
            if (account == null) {
                throw new SystemException(ErrorCode.NOT_LOGIN);
            }

            List<Pond_KoiFish> pondKoiFish = pond_koiFishRepository.findKoiFishByPondsId(id);

            if(pondKoiFish.isEmpty()){
                throw new SystemException(ErrorCode.EMPTY);
            }

            List<KoiFishResponse> responses = new ArrayList<>();
            for(Pond_KoiFish pond_koiFish : pondKoiFish){
                KoiFish koiFish = pond_koiFish.getKoiFish();
                if(!koiFish.isDeleted()){
                    KoiFishResponse koiFishResponse = new KoiFishResponse(
                            koiFish.getId(),
                            koiFish.getFishName(),
                            koiFish.getImageFish()
                    );

                    responses.add(koiFishResponse);
                }
            }

            if(responses.isEmpty()){
                throw new SystemException(ErrorCode.EMPTY);
            }

            return new ResponseEntity<>(responses, HttpStatus.OK);
        }catch (SystemException ex){
            ErrorCode errorCode = ex.getErrorCode();
            ResponseException respon = new ResponseException(ex.getMessage());
            return new ResponseEntity<>(respon, errorCode.getHttpStatus());
        }
    }

    public ResponseEntity<?> getKoiFishUnHealthy() {
        try {

            Account account = accountUtils.getCurrentAccount();
            if (account == null) {
                throw new SystemException(ErrorCode.NOT_LOGIN);
            }

            List<Ponds> pondsList = pondRepository.findByAccount(account).stream()
                    .filter(ponds -> !ponds.isDeleted()).toList();
            if (pondsList == null) {
                throw new SystemException(ErrorCode.EMPTY);
            }
            List<KoiFishResponse> fishs = new ArrayList<>();
            for (Ponds pond : pondsList) {
                List<Pond_KoiFish> pondKoiFish = pond.getKoiFishList();
                for (Pond_KoiFish pond_koiFish : pondKoiFish) {
                    KoiFish fish = pond_koiFish.getKoiFish();
                    if(!fish.isDeleted() && fish.getHealthyStatus().equals(HealthyStatus.SICK)){
                        Pond_KoiFish pondKoi = pond_koiFishRepository.findPondsByKoiFishId(fish.getId());
                        fishs.add(new KoiFishResponse(
                                fish.getId(),
                                fish.getFishName(),
                                fish.getImageFish(),
                                fish.getBirthDay(),
                                fish.getSpecies(),
                                fish.getSize(),
                                fish.getWeigh(),
                                fish.getGender(),
                                fish.getOrigin(),
                                fish.getHealthyStatus(),
                                fish.getNote(),
                                pondKoi.getPonds().getId()
                        ));
                    }
                }
            }
            if (fishs.isEmpty()) {
                throw new SystemException(ErrorCode.EMPTY);
            }

            return new ResponseEntity<>(fishs, HttpStatus.OK);
        } catch (SystemException ex) {
            ErrorCode errorCode = ex.getErrorCode();
            ResponseException respon = new ResponseException(ex.getMessage());
            return new ResponseEntity<>(respon, errorCode.getHttpStatus());
        }
    }



}
