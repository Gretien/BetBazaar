package com.example.demo.services;

import com.example.demo.domain.entities.Match;
import com.example.demo.domain.entities.Result;
import com.example.demo.domain.models.binding.AddBetModel;

import java.util.List;

public interface ResultService {
   List<Match> getResults(List<AddBetModel> matches);
}
