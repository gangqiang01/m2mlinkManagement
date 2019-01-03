package com.m2m.management.repository;

import com.m2m.management.entity.ClientDevice;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IClientDeviceBean extends JpaRepository<ClientDevice, Long> {

}
