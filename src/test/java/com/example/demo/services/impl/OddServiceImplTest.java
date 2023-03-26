package com.example.demo.services.impl;

import com.example.demo.domain.entities.Odd;
import com.example.demo.domain.models.seed.OddSeedModel;
import com.example.demo.repositories.OddRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class OddServiceImplTest {

    @Mock
    private OddRepository oddRepository;

    @Mock
    private ModelMapper modelMapper;

    @Captor
    private ArgumentCaptor<List<Odd>> oddArgumentCaptor;
    @InjectMocks
    private OddServiceImpl oddService;

    @Test
    public void testInit() {
        List<Odd> expectedOdds = new ArrayList<>();
        for (int i = 0; i < 100; i++) {
            OddSeedModel oddSeedModel = new OddSeedModel(1.23);
            Odd odd = new Odd();
            odd.setOdd(1.23);
            expectedOdds.add(odd);
        }
        when(oddRepository.count()).thenReturn(0L);
        when(modelMapper.map(any(OddSeedModel.class), eq(Odd.class))).thenReturn(new Odd());
        when(oddRepository.saveAllAndFlush(anyList())).thenReturn(expectedOdds);


        oddService.init();

        verify(oddRepository).saveAllAndFlush(oddArgumentCaptor.capture());
        List<Odd> odds = oddArgumentCaptor.getValue();
        Assertions.assertEquals(expectedOdds.size(),odds.size());
    }

    @Test
    public void testFindAll() {
        List<Odd> expectedOdds = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            Odd odd = new Odd();
            odd.setId(String.valueOf(i));
            odd.setOdd(1.23 + i);
            expectedOdds.add(odd);
        }
        when(oddRepository.findAll()).thenReturn(expectedOdds);


        List<Odd> actualOdds = oddService.findAll();

        assertEquals(expectedOdds, actualOdds);
    }
}
