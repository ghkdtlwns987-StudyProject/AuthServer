package com.auth.studyprojectauthserver.Domain.Member.Service.impl;

import com.auth.studyprojectauthserver.Domain.Member.Dto.MemberResponseDto;
import com.auth.studyprojectauthserver.Domain.Member.Entity.MemberEntity;
import com.auth.studyprojectauthserver.Domain.Member.Repository.MemberRepository;
import com.auth.studyprojectauthserver.Domain.Member.Service.inter.UserService;
import com.auth.studyprojectauthserver.Global.Error.Exception.MemberNotFoundException;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserService {
    private final MemberRepository memberRepository;
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException{
        MemberEntity memberEntity = memberRepository.findByEmail(email).orElseThrow(() -> new MemberNotFoundException());
        return new User(memberEntity.getEmail(), memberEntity.getPwd(), true, true, true, true, new ArrayList<>());
    }

    @Override
    public MemberResponseDto getUserDtailsByEmail(String email){
        MemberEntity memberEntity = memberRepository.findByEmail(email).orElseThrow(() -> new MemberNotFoundException());

        ModelMapper mapper = new ModelMapper();
        mapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);

        MemberResponseDto memberResponseDto = mapper.map(memberEntity, MemberResponseDto.class);
        return memberResponseDto;

    }
    @Override
    public Iterable<MemberEntity> getUserByAll(){
        return memberRepository.findAll();
    }
}
