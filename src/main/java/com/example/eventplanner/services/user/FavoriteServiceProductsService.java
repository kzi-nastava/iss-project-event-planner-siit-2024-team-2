package com.example.eventplanner.services.user;

import com.example.eventplanner.dto.serviceproduct.serviceproduct.ServiceProductMapper;
import com.example.eventplanner.dto.serviceproduct.serviceproduct.ServiceProductSummaryDto;
import com.example.eventplanner.model.serviceproduct.ServiceProduct;
import com.example.eventplanner.model.user.BaseUser;
import com.example.eventplanner.repositories.serviceproduct.ServiceProductRepository;
import com.example.eventplanner.repositories.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FavoriteServiceProductsService {

    private final UserRepository userRepository;
    private final ServiceProductRepository serviceProductRepository;

    @Transactional
    public List<ServiceProductSummaryDto> getFavorites(Long userId) {
        Optional<BaseUser> userOpt = userRepository.findById(userId);
        if (userOpt.isEmpty()) return null;

        BaseUser user = userOpt.get();
        return user.getFavoriteServiceProducts()
                .stream()
                .map(ServiceProductMapper::toSummaryDto)
                .collect(Collectors.toList());
    }

    @Transactional
    public boolean addFavorite(Long userId, Long serviceProductId) {
        Optional<BaseUser> userOpt = userRepository.findById(userId);
        Optional<ServiceProduct> serviceProductOpt = serviceProductRepository.findById(serviceProductId);

        if (userOpt.isEmpty() || serviceProductOpt.isEmpty()) return false;

        BaseUser user = userOpt.get();
        ServiceProduct serviceProduct = serviceProductOpt.get();

        if (user.getFavoriteServiceProducts() == null) {
            user.setFavoriteServiceProducts(new ArrayList<>());
        }

        if (!user.getFavoriteServiceProducts().contains(serviceProduct)) {
            user.getFavoriteServiceProducts().add(serviceProduct);
            userRepository.save(user);
        }
        return true;
    }


    @Transactional
    public boolean removeFavorite(Long userId, Long serviceProductId) {
        Optional<BaseUser> userOpt = userRepository.findById(userId);
        Optional<ServiceProduct> serviceProductOpt = serviceProductRepository.findById(serviceProductId);

        if (userOpt.isEmpty() || serviceProductOpt.isEmpty()) return false;

        BaseUser user = userOpt.get();
        ServiceProduct serviceProduct = serviceProductOpt.get();

        if (user.getFavoriteServiceProducts() == null) {
            user.setFavoriteServiceProducts(new ArrayList<>());
        }

        if (user.getFavoriteServiceProducts().contains(serviceProduct)) {
            user.getFavoriteServiceProducts().remove(serviceProduct);
            userRepository.save(user);
        }
        return true;
    }
}
