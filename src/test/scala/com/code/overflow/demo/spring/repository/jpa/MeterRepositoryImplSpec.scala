package com.code.overflow.demo.spring.repository.jpa

import com.code.overflow.demo.spring.BaseScalaJpaTest
import com.code.overflow.demo.spring.model.Meter
import com.code.overflow.demo.spring.repository.MeterRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.PageRequest


class MeterRepositoryImplSpec extends BaseScalaJpaTest {
  @Autowired
  var meterRepository: MeterRepository = _

  describe("MeterRepository") {
    describe("get") {
      it("should fetch persisted role") {
        val meterName = "meter_1"

        val meter = meterRepository.get(meterName)
        meter.isDefined should be(true)
        meter.map(m => {
          m.id should not be null
          m.name should equal(meterName)
        })
      }
      it("should be None for non-persited role") {
        val meter = meterRepository.get("unknown-meter")
        meter.isDefined should be(false)
      }
    }

    describe("add") {
      it("should save meter") {
        val meterName = "meter-to-save"
        val meterLabel = "labelForSavedMeter"
        val meter = Meter(meterName, Option(meterLabel))

        val savedMeter = meterRepository.add(meter)
        savedMeter.id should not be null
        savedMeter.name should equal(meterName)
        savedMeter.label should equal(meterLabel)
      }
    }

    describe("update") {
      it("should update meter") {
        val meterName = "meter_1"
        val newLabel = "new-label"
        val meter = meterRepository.get(meterName).getOrElse(fail())

        val updatedMeter = meterRepository.update(Meter(meter.id, meter.name, Option(newLabel)))
        updatedMeter.id should equal(meter.id)
        updatedMeter.name should equal(meter.name)
        updatedMeter.label should equal(newLabel)
      }
    }

    describe("list") {
      it("should find page one of meters") {
        val page = meterRepository.list(PageRequest.of(0, 4))
        page.getTotalElements should equal(10)
        page.getNumberOfElements should equal(4)
        page.getTotalPages should equal(3)
      }
      it("should find last page of meters") {
        val page = meterRepository.list(PageRequest.of(2, 4))
        page.getTotalElements should equal(10)
        page.getNumberOfElements should equal(2)
        page.getTotalPages should equal(3)
      }
    }

    describe("remove") {
      it("should remove meter") {
        val meterName = "meter-to-delete"
        val toDeleteMeter = meterRepository.add(Meter(meterName, None))
        meterRepository.remove(toDeleteMeter)
        meterRepository.get(meterName).isDefined should be(false)
      }
    }

    describe("removeByName") {
      it("should remove meter found by name") {
        val meterName = "meter-to-delete"
        meterRepository.add(Meter(meterName, None))
        meterRepository.get(meterName).isDefined should be(true)
        meterRepository.removeByName(meterName)
        meterRepository.get(meterName).isDefined should be(false)
      }
    }

    describe("filterByNameOrLabel") {
      it("should filter by name") {
        val page = meterRepository.filterByNameOrLabel("meter_1", PageRequest.of(0, 4))
        // meter_1 and meter_10 should be in results
        page.getTotalElements should equal(2)
        page.getNumberOfElements should equal(2)
        page.getTotalPages should equal(1)
      }
      it("should filter by label") {
        val page = meterRepository.filterByNameOrLabel("label_of_1", PageRequest.of(0, 4))
        // label_of_1 and label_of_10 should be in results
        page.getTotalElements should equal(2)
        page.getNumberOfElements should equal(2)
        page.getTotalPages should equal(1)
      }
      it("should filter by name or label") {

        val meterName = "meter_3"
        val newLabel = "label_of_3_changed_to_1"
        val meter = meterRepository.get(meterName).getOrElse(fail())
        meterRepository.update(Meter(meter.id, meter.name, Option(newLabel)))
        val page = meterRepository.filterByNameOrLabel("_1", PageRequest.of(0, 4))
        // label_of_1 and label_of_10 and label_of_3_changed_to_1 should be in results
        page.getTotalElements should equal(3)
        page.getNumberOfElements should equal(3)
        page.getTotalPages should equal(1)
      }
    }
  }
}
