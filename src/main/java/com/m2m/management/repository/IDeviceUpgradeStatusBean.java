package com.m2m.management.repository;

import com.m2m.management.entity.DeviceUpgradeStatus;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IDeviceUpgradeStatusBean extends JpaRepository<DeviceUpgradeStatus, Long> {

}
