package com.example.remotesessionstask.configuration;

import com.example.remotesessionstask.entity.MentorAssignmentsManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static com.example.remotesessionstask.utils.Constants.NUM_OF_CODE_BLOCKS;

@Configuration
public class Config {

    @Bean
    public MentorAssignmentsManager mentorAssignmentsManager() {return new MentorAssignmentsManager(NUM_OF_CODE_BLOCKS);}
}
