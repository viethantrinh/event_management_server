package com.trvihnls.services;

import com.trvihnls.domains.Role;
import com.trvihnls.repositories.RoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RoleService {

    private RoleRepository roleRepository;

}
