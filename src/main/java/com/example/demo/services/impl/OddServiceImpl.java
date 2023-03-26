package com.example.demo.services.impl;

import com.example.demo.domain.entities.Odd;
import com.example.demo.domain.models.OddModel;
import com.example.demo.domain.models.seed.OddSeedModel;
import com.example.demo.repositories.OddRepository;
import com.example.demo.services.OddService;
import jakarta.annotation.PostConstruct;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Service
public class OddServiceImpl implements OddService {
    private OddRepository oddRepository;
    private ModelMapper modelMapper;

    @Autowired
    public OddServiceImpl(OddRepository oddRepository, ModelMapper modelMapper) {
        this.oddRepository = oddRepository;
        this.modelMapper = modelMapper;
    }

    @PostConstruct
    public void init(){
        if(oddRepository.count()==0){
            DecimalFormatSymbols symbols = DecimalFormatSymbols.getInstance();
            symbols.setDecimalSeparator('.');
            List<Odd> odds = new ArrayList<>();
            for (int i = 0; i < 100; i++) {
                Random random = new Random();
                double minValue =1.20;
                double maxValue =4.00;
                DecimalFormat df = new DecimalFormat("#.##", symbols);
                Double theRandomValue = Double.valueOf(df.format(minValue + (maxValue - minValue) * random.nextDouble()));
                OddSeedModel oddModel = new OddSeedModel(theRandomValue);
                Odd odd = this.modelMapper.map(oddModel, Odd.class);
                if( odds.stream().anyMatch(o -> o.getOdd() != null && o.getOdd().equals(odd.getOdd()))){
                    i--;
                }else {
                    odds.add(odd);
                }
            }
            this.oddRepository.saveAllAndFlush(odds);
        }
    }

    @Override
    public List<Odd> findAll() {
        return this.oddRepository.findAll();
    }


}
