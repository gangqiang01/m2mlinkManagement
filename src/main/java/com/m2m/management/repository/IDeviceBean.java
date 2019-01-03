package com.m2m.management.repository;

import com.m2m.management.entity.Device;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IDeviceBean extends JpaRepository<Device, Long> {

}
