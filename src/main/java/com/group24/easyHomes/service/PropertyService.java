package com.group24.easyHomes.service;
import com.group24.easyHomes.model.PropertyAddress;
import com.group24.easyHomes.model.PropertyListQuery;
import com.group24.easyHomes.repository.PropertyAddressRepository;
import com.group24.easyHomes.repository.PropertyRepository;
import com.group24.easyHomes.model.Property;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class PropertyService {

    @Autowired
    private PropertyRepository propertyRepository;

    @Autowired
    private PropertyAddressRepository propertyAddressRepository;

    @PersistenceContext
    private EntityManager entityManager;

    public List<Property> listAll()
    {
        return propertyRepository.findAll();
    }

    public List<Property> filterProperties(PropertyListQuery propertyListQuery) {
        return propertyRepository.findAll(findByCriteria(propertyListQuery));
    }

    public  Specification<Property> findByCriteria(PropertyListQuery searchCriteria) {

        return new Specification<Property>() {

            @Override
            public Predicate toPredicate(Root<Property> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                List<Predicate> predicates = new ArrayList<>();
                HashMap<String, Object> propertiesMap = new HashMap<String, Object>();
                propertiesMap.put("property_name", searchCriteria.getProperty_name());
                propertiesMap.put("amneties", searchCriteria.getAmenities());
                propertiesMap.put("property_type", searchCriteria.getProperty_type());

                for(Map.Entry<String, Object> entry : propertiesMap.entrySet()) {
                    if (entry.getValue() != null && !entry.getValue().equals("")) {
                        predicates.add(cb.equal(root.get(entry.getKey()), ((String) entry.getValue()).toLowerCase()));
                    }
                }

                if (searchCriteria.getNumberOfBedrooms() != null) {
                    predicates.add(cb.equal(root.get("bedrooms"), searchCriteria.getNumberOfBedrooms()));
                }

                if (searchCriteria.getNumberOfBathrooms() != null) {
                    predicates.add(cb.equal(root.get("bathrooms"), searchCriteria.getNumberOfBathrooms()));
                }

                if (searchCriteria.getRent() != null) {
                    predicates.add(cb.equal(root.get("rent"), searchCriteria.getRent()));
                }

                if (searchCriteria.getParkingIncluded() != null) {
                    predicates.add(cb.equal(root.get("parking_included"), searchCriteria.getParkingIncluded()));
                }

                if (searchCriteria.getCity() != null || searchCriteria.getCountry() != null  ||
                        searchCriteria.getProvince() != null ) {
                    List<PropertyAddress> address = propertyAddressRepository.findAll(findByAddressCriteria(searchCriteria));
                    List<Integer> addressIds = new ArrayList<>();

                    if(address.size()>0) {
                        for(PropertyAddress address1 : address) {
                            addressIds.add(address1.getAddress_id());
                        }
                        predicates.add(root.get("address").in(addressIds));
                    }
                }

                return cb.and(predicates.toArray(new Predicate[] {}));
            }
        };
    }

    public  Specification<PropertyAddress> findByAddressCriteria (PropertyListQuery searchCriteria) {

        return new Specification<PropertyAddress>() {

            @Override
            public Predicate toPredicate(Root<PropertyAddress> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                List<Predicate> predicates = new ArrayList<>();

                if (searchCriteria.getCity() != null && !searchCriteria.getCity().equals("")) {
                    predicates.add(cb.equal(root.get("city"), searchCriteria.getCity()));
                }
                if (searchCriteria.getProvince() != null && !searchCriteria.getProvince().equals("")) {
                    predicates.add(cb.equal(root.get("province"), searchCriteria.getProvince()));
                }
                if (searchCriteria.getCountry() != null && !searchCriteria.getCountry().equals("")) {
                    predicates.add(cb.equal(root.get("country"), searchCriteria.getCountry()));
                }
                return cb.and(predicates.toArray(new Predicate[] {}));
            }
        };
    }



    public Property addProperty(Property property)
    {
        return propertyRepository.save(property);
    }

    public Property getProperty(Integer id)
    {
        if(propertyRepository.findById(id).isPresent())
            return propertyRepository.findById(id).get();
        return null;
    }


    public String delete(Integer id)
    {
        if(getProperty(id)!= null) {
            propertyRepository.deleteById(id);
            return "SUCCESS";
        }
        return "ERROR";
    }

    public Property updateProperty(Integer id , Property property)
    {
        if(getProperty(id)!= null) {
            return  propertyRepository.save(property);
        }return null;
    }
}
