package com.code.overflow.demo.spring.repository.jpa

import com.code.overflow.demo.spring.BaseScalaJpaTest
import com.code.overflow.demo.spring.model.Alert
import com.code.overflow.demo.spring.repository.{AlertRepository, MeterRepository}
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.PageRequest


class AlertRepositoryImplSpec extends BaseScalaJpaTest {
  @Autowired
  var alertRepository: AlertRepository = _

  @Autowired
  var meterRepository: MeterRepository = _

  describe("AlertRepository") {
    describe("add") {
      it("should save alert") {
        val meterName = "meter_1"
        val meter = meterRepository.get(meterName).getOrElse(fail())
        val alertLabel = "alert-label"
        val alertSeverity = 10
        val alert = alertRepository.add(Alert(meter, alertLabel, alertSeverity))
        alert.id should not be null
        alert.label should equal(alertLabel)
        alert.severity should equal(alertSeverity)
      }
    }

    describe("list") {
      it("should find page one of alerts") {
        val page = alertRepository.list(PageRequest.of(0, 9))
        page.getTotalElements should equal(40)
        page.getNumberOfElements should equal(9)
        page.getTotalPages should equal(5)
      }
      it("should find last page of alerts") {
        val page = alertRepository.list(PageRequest.of(4, 9))
        page.getTotalElements should equal(40)
        page.getNumberOfElements should equal(4)
        page.getTotalPages should equal(5)
      }
    }

    describe("listForMeterAndLabel") {
      it("should find page of alerts filtered by meter and label") {
        val page = alertRepository.listForMeterAndLabel("meter_1", "alert", PageRequest.of(0, 9))
        page.getTotalElements should equal(4)
        page.getNumberOfElements should equal(4)
        page.getTotalPages should equal(1)
      }
    }
  }
}
